package org.vatalu;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.obsessive.web.ObsessiveStarter;
import org.vatalu.controller.UserController;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main extends AbstractVerticle{

    public static void main(String[] args) {
        ObsessiveStarter.run(Main.class);
    }


}


