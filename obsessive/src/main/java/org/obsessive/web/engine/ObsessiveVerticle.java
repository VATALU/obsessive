package org.obsessive.web.engine;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.obsessive.web.factory.BeanFactory;
import org.obsessive.web.factory.ClassFactory;
import org.obsessive.web.factory.ConfigFactory;
import org.obsessive.web.lang.annotation.*;
import org.obsessive.web.util.*;

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
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {

            controllerClassSet.forEach(controllerClass -> {
                //获取controller类中的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    //遍历类中所有方法
                    Arrays.asList(methods).forEach(method -> {
                        //判断是否是被 Route 注解
                        if (method.isAnnotationPresent(Route.class)) {
                            //获取所有的参数类型
                            Class<?>[] paramTypeClasses = method.getParameterTypes();
                            Route route = method.getAnnotation(Route.class);
                            String path = ConfigFactory.getServerContextPath()+route.value();
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

                            vertxRoute.handler(routingContext -> ReflectUtil.invokeMethod(beanFactory.getBean(controllerClass), method, routingContext));
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

}
