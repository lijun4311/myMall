package com.mall.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/**
 * @Author lijun
 * @Date 2020-05-19 19:39
 * @Description  mybatis_plus 数据自动填充实现类
 * @Since version-1.0
 */
@Component
public class MybatisFillHandler implements MetaObjectHandler {
    protected final static Logger log = LoggerFactory.getLogger("AsyncLogger");

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
