package Redis;

import com.mall.util.RedisPoolUtil;
import org.junit.Test;

public class RedisPoolUtilTest {
    @Test
    public void test() {
        for(int i=0;i<=100;i++ ){
            RedisPoolUtil.set("keyTest", "value");
            String value = RedisPoolUtil.get("keyTest");
            RedisPoolUtil.setEx("keyex", 60 * 10, "valueex");
            RedisPoolUtil.expire("keyTest", 60 * 20);
            RedisPoolUtil.del("keyTest");
            RedisPoolUtil.setEx("1",22,"23");
            System.out.println(value);
            System.out.println("end");
            System.out.println(RedisPoolUtil.set("key", "value"));
            System.out.println(RedisPoolUtil.expire("key", 1000));
        }

    }
}
