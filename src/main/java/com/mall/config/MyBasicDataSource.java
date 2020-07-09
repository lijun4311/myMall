package com.mall.config;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * @Author lijun
 * @Date 2020-05-19 19:39
 * @Description  我的数据源继承类
 * @Since version-1.0
 */
public class MyBasicDataSource extends BasicDataSource {
    @Override
    public synchronized void close() throws SQLException {
        //以下两句代码分别对应两个资源的关闭
        DriverManager.deregisterDriver(DriverManager.getDriver(getUrl()));
        AbandonedConnectionCleanupThread.checkedShutdown();
        super.close();
    }
}
