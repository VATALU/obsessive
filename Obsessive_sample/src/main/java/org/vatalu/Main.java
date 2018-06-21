package org.vatalu;


import io.vertx.core.Vertx;
import org.obsessive.web.ObsessiveStarter;
import org.vatalu.controller.UserController;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        ObsessiveStarter.run(Main.class);
//        Method[] methods = UserController.class.getMethods();
//        String[] arg = {"1", "2"};
//        try {
//            System.out.println(methods[0].invoke(new UserController(), arg));
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }
}


