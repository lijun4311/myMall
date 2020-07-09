package com.mall.config.objectmapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
/**
 * @Author lijun
 * @Date 2020-05-19 19:41
 * @Description  我的json序列化时间处理类
 * @Since version-1.0
 */
public class MyLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    public static final MyLocalDateTimeDeserializer INSTANCE = new MyLocalDateTimeDeserializer();

    private MyLocalDateTimeDeserializer() {
        super();
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        long timestamp = jsonParser.getValueAsLong();
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

    }
}
