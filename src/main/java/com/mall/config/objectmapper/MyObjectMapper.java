package com.mall.config.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.mall.util.MyDateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/**
 * @Author lijun
 * @Date 2020-05-19 19:41
 * @Description  jackson  自定义 ObjectMapper  主要时间处理
 * @Since version-1.0
 */
public class MyObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 8120031118939768733L;

    public MyObjectMapper() {

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, MyLocalDateTimeSerializer.INSTANCE);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(MyDateUtil.DAY_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(MyDateUtil.TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, MyLocalDateTimeDeserializer.INSTANCE);
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(MyDateUtil.DAY_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(MyDateUtil.TIME_FORMAT)));
        this.registerModule(javaTimeModule);

    }
}
