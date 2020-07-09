package com.mall.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mall.config.objectmapper.MyObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * @Author lijun
 * @Date 2020-05-19 19:38
 * @Description json工具类
 * @Since version-1.0
 */
public class JsonUtil {
    private final static Logger log = LoggerFactory.getLogger("AsyncLogger");
    private static final ObjectMapper OBJECT_MAPPER = new MyObjectMapper();
    static {
        //ALWAYS 默认
        //NON_DEFAULT 属性为默认值不序列化
        //NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
        //NON_NULL 属性为NULL 不序列化

        //对象的所有字段全部列入
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(MyDateUtil.STANDARD_FORMAT));
        //取消默认转换timestamps形式
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    /**
     * 对象序列化为json字符
     *
     * @param obj obj
     * @param <T> 泛型
     * @return string
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }


    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (MyStringUtil.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(str, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Parse String to Object error", e);
            return null;
        }
    }


    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (MyStringUtil.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(str, typeReference);
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }


    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }
}
