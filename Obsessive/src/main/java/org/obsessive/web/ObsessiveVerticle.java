package org.obsessive.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import org.obsessive.web.factory.BeanFactory;
import org.obsessive.web.factory.ClassFactory;
import org.obsessive.web.factory.ConfigFactory;
import org.obsessive.web.lang.annotation.Controller;
import org.obsessive.web.lang.annotation.Route;
import org.obsessive.web.util.ArrayUtils;
import org.obsessive.web.util.CollectionUtils;
import org.obsessive.web.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class ObsessiveVerticle extends AbstractVerticle {

    private Router router = Router.router(vertx);


    public ObsessiveVerticle(ClassFactory classFactory,BeanFactory beanFactory) {

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
                            Route route = method.getAnnotation(Route.class);
                            String path = route.value();
                            HttpMethod httpMethod = route.method();
                            String consumes = route.consumes();
                            String produces = route.produces();
                            router.route(httpMethod, path).consumes(consumes).produces(produces).handler(routingContext -> {
                                ReflectionUtils.invokeMethod(beanFactory.getBean(controllerClass),method,routingContext);
                            });
                        }
                    });
                }
            });
        }
    }

    @Override
    public void start() throws Exception {
        super.start();
        vertx.createHttpServer().requestHandler(router::accept).listen(Integer.valueOf(ConfigFactory.getServerPort()));
    }

}
