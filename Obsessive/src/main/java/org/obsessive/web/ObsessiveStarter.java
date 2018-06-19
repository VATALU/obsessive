package org.obsessive.web;

import org.obsessive.web.factory.BeanFactory;
import org.obsessive.web.factory.ClassFactory;
import org.obsessive.web.factory.ConfigFactory;

/**
 * 启动类
 */
public class ObsessiveStarter {


    public static void run(Class<?> clazz) {
        ConfigFactory configFactory = new ConfigFactory();
        ClassFactory classFactory = new ClassFactory(clazz);
        BeanFactory beanFactory = new BeanFactory(classFactory.getAnnotationClassSet());
    }

}
