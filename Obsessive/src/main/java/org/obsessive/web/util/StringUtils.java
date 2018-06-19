package org.obsessive.web.util;

/**
 * 字符串处理类
 */
public class StringUtils {

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    private static boolean isEmpty(String s) {
        if (s != null) {
            s = s.trim();
        }
        return s == null || s.length() == 0;
    }

}
