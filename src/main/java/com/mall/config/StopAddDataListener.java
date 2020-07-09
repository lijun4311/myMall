package com.mall.config;

import com.mall.common.RedisPool;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
/**
 * @Author lijun
 * @Date 2020-05-19 19:44
 * @Description  spring 容器关闭执行类
 * @Since version-1.0
 */
@Component
public class StopAddDataListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent contextClosedEvent) {
        //redis连接池关闭
        RedisPool.close();
    }
}
