package com.strr.util;

/**
 * 字符串工具类
 */
public class StringUtils {
    /**
     * 下划线转驼峰
     * @param name
     * @return
     */
    public static String toCamelCase(CharSequence name) {
        return toCamelCase(name, '_');
    }

    /**
     * 下划线转驼峰
     * @param name
     * @param symbol
     * @return
     */
    public static String toCamelCase(CharSequence name, char symbol) {
        if (null == name) {
            return null;
        } else {
            String name2 = name.toString();
            if (name2.indexOf(symbol) > -1) {
                int length = name2.length();
                StringBuilder sb = new StringBuilder(length);
                boolean upperCase = false;
                for(int i = 0; i < length; ++i) {
                    char c = name2.charAt(i);
                    if (c == symbol) {
                        upperCase = true;
                    } else if (upperCase) {
                        sb.append(Character.toUpperCase(c));
                        upperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                }
                return sb.toString();
            } else {
                return name2.toLowerCase();
            }
        }
    }
}
