package org.obsessive.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.obsessive.web.entity.HttpStatusCode;
import org.obsessive.web.entity.ValueConstant;
import org.obsessive.web.factory.BeanFactory;
import org.obsessive.web.factory.ClassFactory;
import org.obsessive.web.factory.ConfigFactory;
import org.obsessive.web.lang.annotation.*;
import org.obsessive.web.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ObsessiveVerticle extends AbstractVerticle {


    private ClassFactory classFactory;

    private BeanFactory beanFactory;

    public ObsessiveVerticle(ClassFactory classFactory, BeanFactory beanFactory) {
        this.classFactory = classFactory;
        this.beanFactory = beanFactory;
    }

    public ObsessiveVerticle initRouter(Router router) {
        //设置 Bodyhandler 处理请求体
        router.route().handler(BodyHandler.create());

        Set<Class<?>> controllerClassSet = classFactory.getAnnotationClassSet(Controller.class);
        if (CollectionUtils.isNotEmpty(controllerClassSet)) {

            controllerClassSet.forEach(controllerClass -> {
                //获取controller类中的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)) {
                    //遍历类中所有方法
                    Arrays.asList(methods).forEach(method -> {
                        //判断是否是被 Route 注解
                        if (method.isAnnotationPresent(Route.class)) {
                            //获取所有的参数类型
                            Class<?>[] paramTypeClasses = method.getParameterTypes();
                            Route route = method.getAnnotation(Route.class);
                            String path = route.value();
                            HttpMethod[] httpMethods = route.method();
                            String[] consumes = route.consumes();
                            String[] produces = route.produces();

                            io.vertx.ext.web.Route vertxRoute = router.route(path);

                            //设置
                            for (HttpMethod httpMethod : httpMethods) {
                                vertxRoute.method(httpMethod);
                            }
                            for (String consume : consumes) {
                                vertxRoute.consumes(consume);
                            }
                            for (String produce : produces) {
                                vertxRoute.produces(produce);
                            }

                            vertxRoute.handler(routingContext -> {
                                HttpServerRequest httpServerRequest = routingContext.request();
                                HttpServerResponse httpServerResponse = routingContext.response();

                                //获取所有的参数的注解
                                Annotation[][] annotations = method.getParameterAnnotations();

                                //方法的每个参数值
                                List<Object> args = new ArrayList<>();

                                //对于每个注解对应的值与请求值绑定
                                for (Annotation[] annotation : annotations) {
                                    Object arg = null;
                                    for (int i = 0; i < annotation.length; i++) {
                                        Annotation a = annotation[i];
                                        if (a instanceof PathParam) {

                                            String paramName = ((PathParam) a).value();
                                            String paramValue = httpServerRequest.getParam(paramName);
                                            //判断参数是否为空
                                            if (StringUtils.isNotEmpty(paramValue)) {
                                                if (arg == null)
                                                    arg = paramValue;
                                            } else {
                                                //404
                                                sendError(HttpStatusCode.NOT_FOUND, httpServerResponse);
                                            }

                                        } else if (a instanceof RequestParam) {
                                            //获取表单数据
                                            String paramName = ((RequestParam) a).value();
                                            String defaultValue = ((RequestParam) a).defaultValue();
                                            boolean required = ((RequestParam) a).required();

                                            //获取 url 中的键值对
                                            String value = httpServerRequest.getParam(paramName);
                                            //获取请求 form 数据
                                            if (StringUtils.isEmpty(value)) {
                                                value = httpServerRequest.getFormAttribute(paramName);
                                            }
                                            if(arg==null) {
                                                if (required && StringUtils.isEmpty(value)) {
                                                    sendError(HttpStatusCode.NOT_FOUND, httpServerResponse);
                                                }else if(!required && StringUtils.isEmpty(value)) {
                                                    if(!defaultValue.equals(ValueConstant.DEFAULT_NONE)) {
                                                        Class<?> clazz = paramTypeClasses[i];
                                                        arg = defaultValue;
                                                    }
                                                } else {
                                                    arg=value;
                                                }
                                            }

                                        } else if (a instanceof BodyParam) {
                                            //获取类名
                                            Class<?> paramTypeClass = paramTypeClasses[i];
                                            String jsonStr = routingContext.getBodyAsString();
                                            System.out.println(jsonStr);
                                            if (arg == null)
                                                arg = JsonUtils.fromJson(jsonStr, paramTypeClass);
                                        }
                                    }
                                    args.add(arg);
                                }
                                Object result = ReflectionUtils.invokeMethod(beanFactory.getBean(controllerClass), method, args.toArray());
                                httpServerResponse.end(JsonUtils.toJson(result));
                            });
                        }
                    });
                }
            });
        }
        return this;
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        initRouter(router);
        vertx.createHttpServer().requestHandler(router::accept).listen(ConfigFactory.getServerPort(), ConfigFactory.getServerHost());
    }

    private void sendError(HttpStatusCode httpStatusCode, HttpServerResponse response) {
        response.setStatusCode(httpStatusCode.code()).end();
    }

}
