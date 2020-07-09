package com.mall.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author lijun
 * @Date 2020-05-19 19:25
 * @Description 我的字符串工具类
 * @Since version-1.0
 */
public class MyStringUtil extends StringUtils {

    /**
     * 判断参数组 是否不为空
     *
     * @param strs 参数组
     * @return 布尔
     */
    public static boolean isNotBlanks(String... strs) {
        for (String str : strs) {
            if (isBlank(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断参数组是否有为空
     *
     * @param strs 参数组
     * @return 布尔值
     */
    public static boolean isBlanks(String... strs) {
        for (String str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 首字母转小写
     *
     * @param s 传入字符
     * @return 传出字符
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线
     *
     * @param camel 传入字符
     * @return 传出字符
     */
    public static String camelToUnderline(String camel) {
        if (isBlank(camel)) {
            return camel;
        }
        Matcher matcher = HUMP_PATTERN.matcher(camel);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
