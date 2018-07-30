package org.obsessive.web.util;

/**
 * 字符串处理类
 */
public class Strings {

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(String s) {
        if (s != null) {
            s = s.trim();
        }
        return s == null || s.length() == 0;
    }

    public static String getOrDefault(String value, String defaultValue){
        if(isNotEmpty(value)) {
            return value;
        }
        return defaultValue;
    }
}
