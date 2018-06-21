package org.obsessive.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.obsessive.web.factory.BeanFactory;
import org.obsessive.web.factory.ClassFactory;
import org.obsessive.web.factory.ConfigFactory;
import org.obsessive.web.lang.annotation.*;
import org.obsessive.web.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ObsessiveVerticle extends AbstractVerticle {

    private Router router = Router.router(vertx);


    public ObsessiveVerticle(ClassFactory classFactory, BeanFactory beanFactory) {

        Set<Class<?>> controllerClassSet = classFactory.getAnnotationClassSet(Controller.class);
        if (CollectionUtils.isNotEmpty(controllerClassSet)) {

            //设置 Bodyhandler 处理请求体
            router.route().handler(BodyHandler.create());

            controllerClassSet.forEach(controllerClass -> {
                //获取controller类中的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)) {
                    //遍历类中所有方法
                    Arrays.asList(methods).forEach(method -> {
                        //判断是否是被 Route 注解
                        if (method.isAnnotationPresent(Route.class)) {
                            Class<?>[] paramTypeClasses = method.getParameterTypes();
                            Route route = method.getAnnotation(Route.class);
                            String path = route.value();
                            HttpMethod[] httpMethods = route.method();
                            String[] consumes = route.consumes();
                            String[] produces = route.produces();
                            //对每个方法进行翻译
                            for (HttpMethod httpMethod : httpMethods) {
                                io.vertx.ext.web.Route vertxRoute = router.route(httpMethod, path);
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
                                    List<Object> args = new ArrayList<>();
                                    //对于每个注解对应的值与请求值绑定
                                    for (Annotation[] annotation : annotations) {
                                        Object arg = null;
                                        for (int i = 0; i < annotation.length; i++) {
                                            Annotation a = annotation[i];
                                            if (a instanceof PathParam) {
                                                String paramName = ((PathParam) a).value();
                                                String paramValue = httpServerRequest.getParam(paramName);
                                                if (arg == null)
                                                    arg = paramValue;
                                            } else if (a instanceof RequestParam) {
                                                String paramName = ((RequestParam) a).value();
                                                String defaultValue = ((RequestParam) a).defaultValue();
                                                String value = httpServerRequest.getParam(paramName);
                                                System.out.println(value);
                                                if (value == null)
                                                    value = httpServerRequest.getFormAttribute(paramName);
                                                System.out.println(value);
                                                if (arg == null)
                                                    arg = StringUtils.getOrDefault(value, defaultValue);
                                            } else if (a instanceof RequestBody) {
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
                        }
                    });
                }
            });
        }
    }

    @Override
    public void start() throws Exception {
        super.start();
        vertx.createHttpServer().requestHandler(router::accept).listen(ConfigFactory.getServerPort(), ConfigFactory.getServerHost());
    }

}
