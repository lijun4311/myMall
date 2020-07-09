package com.mall.util;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author lijun
 * @Date 2020-05-02 10:25
 * @Description 时间处理工具类 依赖jar包 joda.time
 * @Since version-1.0
 */

public class MyDateUtil {

    /**
     *  标准时间格式
     */
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     *  年月日时间格式
     */
    public static final String DAY_FORMAT = "yyyy-MM-dd";
    /**
     *  时分秒时间格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * 日期字符串转化时间对象
     *
     * @param dateTimeStr 日期
     * @param formatStr   格式 默认 STANDARD_FORMAT
     * @return Date对象;
     */

    public static LocalDateTime strToDate(String dateTimeStr, String formatStr) {
        if (MyStringUtil.isBlank(dateTimeStr)) {
            return null;
        }
        formatStr = MyStringUtil.defaultIfBlank(formatStr, STANDARD_FORMAT);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatStr);
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }

    /**
     * data对象转化时间格式
     *
     * @param date 时间对象
     * @param formatStr 格式 默认 STANDARD_FORMAT
     * @return 时间
     */

    public static String dateToStr(LocalDateTime date, String formatStr) {
        if (date == null) {
            return MyStringUtil.EMPTY;
        }
        formatStr = MyStringUtil.defaultIfBlank(formatStr, STANDARD_FORMAT);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatStr);
        return date.format(dateTimeFormatter);
    }

    /**
     * 时间转换为date对象  格式为 STANDARD_FORMAT
     *
     * @param dateTimeStr 日期
     * @return date
     */

    public static LocalDateTime strToDateTime(String dateTimeStr) {
        if (MyStringUtil.isBlank(dateTimeStr)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_FORMAT);
        return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
    }

    /**
     * 时间对象转化字符时间 格式为 STANDARD_FORMAT
     *
     * @param date 时间对象
     * @return 字符时间
     */

    public static String dateToStr(LocalDateTime date) {
        if (date == null) {
            return MyStringUtil.EMPTY;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STANDARD_FORMAT);
        return date.format(dateTimeFormatter);
    }

    /**
     * 时间转换为date对象  格式为 DAY_FORMAT
     *
     * @param dateStr 日期
     * @return date
     */

    public static LocalDate strToDate(String dateStr) {
        if (MyStringUtil.isBlank(dateStr)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DAY_FORMAT);
        return LocalDate.parse(dateStr, dateTimeFormatter);
    }

    /**
     * 时间对象转化字符时间 格式为 DAY_FORMAT
     *
     * @param date 时间对象
     * @return 字符时间
     */

    public static String dateToStr(LocalDate date) {
        if (date == null) {
            return MyStringUtil.EMPTY;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DAY_FORMAT);
        return date.format(dateTimeFormatter);
    }
}
