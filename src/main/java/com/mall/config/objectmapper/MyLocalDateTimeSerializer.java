package com.mall.config.objectmapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
/**
 * @Author lijun
 * @Date 2020-05-19 19:25
 * @Description  JSON序列化时间处理类
 * @Since version-1.0
 */
public class MyLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    public static final MyLocalDateTimeSerializer INSTANCE = new MyLocalDateTimeSerializer();

    private MyLocalDateTimeSerializer() {
        super();
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        jsonGenerator.writeNumber(timestamp);
    }
}