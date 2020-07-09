package com.mall.util.lambda;

import com.mall.util.MyStringUtil;
import com.mall.util.lambda.functional.MyFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lijun
 * @Date 2020-05-25 19:25
 * @Description java labda工具类
 * @Since version-1.0
 */
public class LambdaUtil {
    /**
     * lambda 表达式缓存
     */
    static Map<Class, SerializedLambda> CLASS_LAMDBA_CACHE = new ConcurrentHashMap<>();

    /***
     * 转换方法引用为属性名
     * @param fn 类get方法
     * @return 属性名
     */
    public static <T, R> String convertToFieldName(MyFunction<T, R> fn, boolean flag) {
        SerializedLambda lambda = LambdaUtil.getSerializedLambda(fn);
        // 获取方法名
        String methodName = lambda.getImplMethodName();
        String prefix = "get";
        // 截取get/is之后的字符串并转换首字母为小写
        String fieldName = MyStringUtil.toLowerCaseFirstOne(methodName.replace(prefix, ""));
        return flag ? MyStringUtil.camelToUnderline(fieldName) : fieldName;
    }

    public static <T, R> String convertToFieldName(MyFunction<T, R> fn) {
        return convertToFieldName(fn, true);
    }

    /**
     * 获得lambda 序列化对象
     *
     * @param fn 传入lambda表达式
     * @return 序列化lambda表达式
     */
    static <T, R> SerializedLambda getSerializedLambda(MyFunction<T, R> fn) {
        SerializedLambda lambda = CLASS_LAMDBA_CACHE.get(fn.getClass());
        // 先检查缓存中是否已存在
        if (lambda == null) {
            try {
                // 提取SerializedLambda并缓存
                Method[] methods = fn.getClass().getMethods();
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMDBA_CACHE.put(fn.getClass(), lambda);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lambda;
    }
}

