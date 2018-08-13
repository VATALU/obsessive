package org.obsessive.web.factory;

import org.obsessive.web.entity.constant.ConfigConstant;
import org.obsessive.web.log.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * config factory
 */
public class ConfigFactory {
    private static final Record LOGGER = Record.get(ConfigFactory.class);

    private static final Map<String, String> CONFIG_MAP;

    static {
        Properties props;
        InputStream is = null;
        CONFIG_MAP = new HashMap<>();
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigConstant.CONFIG_FILE);
            if (is == null) {
                throw new FileNotFoundException(ConfigConstant.CONFIG_FILE + " file is not found ");
            }
            props = new Properties();
            props.load(is);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String property = props.getProperty(key);
                CONFIG_MAP.put(key, property);
            }
        } catch (IOException e) {
            LOGGER.error("load properties file failure ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream failure ", e);
                }
            }
        }
    }

    public static int getServerPort() {
        if (CONFIG_MAP.containsKey(ConfigConstant.SERVER_PORT))
            return Integer.valueOf(CONFIG_MAP.get(ConfigConstant.SERVER_PORT));
        else
            return ConfigConstant.DEFAULT_SERVER_PORT;
    }

    public static String getServerHost() {
        return CONFIG_MAP.getOrDefault(ConfigConstant.SERVER_HOST,ConfigConstant.DEFAULT_SERVER_HOST);
    }

    public static String getServerContextPath() {
        return CONFIG_MAP.getOrDefault(ConfigConstant.SERVER_CONTEXT_PATH,"");
    }
}

