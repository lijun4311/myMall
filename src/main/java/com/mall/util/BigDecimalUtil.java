package com.mall.util;

import java.math.BigDecimal;

/**
 * @Author lijun
 * @Date 2020-05-19 19:28
 * @Description  运算工具类
 * @Since version-1.0
 */
public class BigDecimalUtil {
    /**
     *  除法精度小数位
     */
    private static final int DEF_DIV_SCALE = 10;

    private BigDecimalUtil() {

    }

    /**
     * 加法运算
     *
     * @param value1 加数浮点型
     * @param value2 加数浮点型
     * @return 结果 BigDecimal
     */
    public static BigDecimal add(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2);
    }

    /**
     * 加法运算
     *
     * @param value1 加数 String
     * @param value2 加数 String
     * @return 结果 BigDecimal
     */
    public static BigDecimal add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2);
    }

    /**
     * 减法运算
     *
     * @param value1 被减数浮点型
     * @param value2 减数浮点型
     * @return 结果 BigDecimal
     */
    public static BigDecimal sub(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2);
    }

    /**
     * 减法运算
     *
     * @param value1 被减数 String
     * @param value2 减数 String
     * @return 结果 BigDecimal
     */
    public static BigDecimal sub(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2);
    }

    /**
     * 乘法运算
     *
     * @param value1 乘数浮点型
     * @param value2 乘数浮点型
     * @return 结果 BigDecimal
     */
    public static BigDecimal mul(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2);
    }

    /**
     * 乘法运算
     *
     * @param value1 乘数  String
     * @param value2 乘数  String
     * @return 结果 BigDecimal
     */
    public static BigDecimal mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2);
    }

    /**
     * 除法运算
     *
     * @param value1 被除数 浮点型
     * @param value2 除数 浮点型
     * @return 结果 BigDecimal
     * @throws IllegalAccessException 默认精度异常
     */
    public static BigDecimal div(double value1, double value2) throws IllegalAccessException {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 除法运算
     *
     * @param value1 被除数   String
     * @param value2 除数   String
     * @return 结果 BigDecimal
     * @throws IllegalAccessException 默认精度异常
     */
    public static BigDecimal div(String value1, String value2) throws IllegalAccessException {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 除法运算
     *
     * @param value1 被除数   String
     * @param value2 除数   String
     * @param scale  int 运算精度 不能小于0
     * @return 结果 BigDecimal
     * @throws IllegalAccessException 输入精度异常
     */
    public static BigDecimal div(double value1, double value2, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除法运算
     *
     * @param value1 被除数   String
     * @param value2 除数   String
     * @param scale  int 运算精度 不能小于0
     * @return 结果 BigDecimal
     * @throws IllegalAccessException 输入精度异常
     */
    public static BigDecimal div(String value1, String value2, int scale) throws IllegalAccessException {
        if (scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 四舍五入
     *
     * @param value 传入参数 浮点型
     * @param scale 小数点后保留几位
     * @return 结果 BigDecimal
     * @throws IllegalAccessException 输入精度异常
     */
    public static BigDecimal round(double value, int scale) throws IllegalAccessException {
        return div(value, 1, scale);
    }

    /**
     * 四舍五入
     *
     * @param value 传入参数 String
     * @param scale 小数点后保留几位
     * @return 结果 BigDecimal
     * @throws IllegalAccessException 输入精度异常
     */
    public static BigDecimal round(String value, int scale) throws IllegalAccessException {
        return div(value, "1", scale);
    }

    /**
     * 比较是否相等
     *
     * @param value1 参数1 BigDecimal
     * @param value2 参数2  BigDecimal
     * @return boolean
     */
    public static boolean equ(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        return value1.compareTo(value2) == 0;

    }
}
