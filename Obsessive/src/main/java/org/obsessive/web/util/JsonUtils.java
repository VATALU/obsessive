package org.obsessive.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        if (StringUtils.isNotEmpty(jsonStr)) {
            try {
                return new ObjectMapper().readValue(jsonStr, clazz);
            } catch (IOException e) {
                LOGGER.error("{} can not transfer to class[{}]", jsonStr, clazz);
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("{} can not transfer to Json String",object.getClass().getName());
            e.printStackTrace();
        }
        return null;
    }
}
