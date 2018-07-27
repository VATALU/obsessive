package org.obsessive.web.engine;

import io.vertx.core.Vertx;
import org.obsessive.web.factory.BeanFactory;
import org.obsessive.web.factory.ClassFactory;
import org.obsessive.web.factory.ConfigFactory;
import org.obsessive.web.log.Record;

/**
 * 启动类
 */
public class ObsessiveStarter {

    private static final Record LOGGER = Record.get(ObsessiveStarter.class);

    private transient final Class<?> clazz;

    private ObsessiveStarter(final Class<?> clazz) {
        this.clazz=clazz;
    }

    public static void run(final Class<?> clazz) {
        ConfigFactory configFactory = new ConfigFactory();
        ClassFactory classFactory = new ClassFactory(clazz);
        BeanFactory beanFactory = new BeanFactory(classFactory.getAnnotationClassSet());
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ObsessiveVerticle(classFactory,beanFactory));
    }

    public static void run(final Class<?> clazz, final Object... args) {

    }
}
