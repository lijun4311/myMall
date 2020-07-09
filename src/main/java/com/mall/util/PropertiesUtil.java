package com.mall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @Author lijun
 * @Date 2020-05-03 19:48
 * @Description 基础配置文件获取类
 * @Since version-1.0
 */
public class PropertiesUtil {
    protected final static Logger log = LoggerFactory.getLogger("AsyncLogger");
    private static final String REDIS_CONFIG = "/properties/redis.properties";
    private static final String COOKIE_CONFIG = "/properties/cookie.properties";
    private static final String SYSTEM_CONFIG = "/properties/system.properties";
    private static final String FTP_CONFIG = "/properties/ftp.properties";


    private static final Properties REDIS_PROPS;
    private static final Properties COOKIE_PROPS;
    private static final Properties SYSTEM_PROPS;
    private static final Properties FTP_PROPS;


    static {
        REDIS_PROPS = readConfig(REDIS_CONFIG);
        COOKIE_PROPS = readConfig(COOKIE_CONFIG);
        SYSTEM_PROPS = readConfig(SYSTEM_CONFIG);
        FTP_PROPS = readConfig(FTP_CONFIG);

    }

    private static Properties readConfig(String url) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            url = PropertiesUtil.class.getResource(url).getPath();
            // 创建文件输入流
            inputStream = new FileInputStream(url);
            // 通过输入流加载properties配置文件到properties对象中
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("配置文件读取异常", e);
        } catch (NullPointerException e) {
            log.error("配置文件路径读取错误", e);
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭输入流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }


    public static String getRedisConfig(String key) {
        String value = REDIS_PROPS.getProperty(key.trim());
        if (MyStringUtil.isBlank(value)) {
            log.error("redis配置文件读取异常,参数为" + key);
            return null;
        }
        return value.trim();
    }

    public static String getRedisConfig(String key, String defaultValue) {

        String value = REDIS_PROPS.getProperty(key.trim());
        if (MyStringUtil.isBlank(value)) {
            log.warn("redis配置文件读取异常,参数为" + key + "启用默认值" + defaultValue);
            value = defaultValue;
        }
        return value.trim();
    }

    public static int getRedisConfig(String key, int defaultValue) {
        int value;
        try {
            value = Integer.parseInt(REDIS_PROPS.getProperty(key.trim()));
        } catch (NumberFormatException e) {
            log.warn("redis配置文件读取异常,参数为" + key + "启用默认值" + defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static String getCookieConfig(String key) {
        String value = COOKIE_PROPS.getProperty(key.trim());
        if (MyStringUtil.isBlank(value)) {
            log.error("cookie配置文件读取异常,参数为" + key);
            return null;
        }
        return value.trim();
    }

    public static String getCookieConfig(String key, String defaultValue) {

        String value = COOKIE_PROPS.getProperty(key.trim());
        if (MyStringUtil.isBlank(value)) {
            log.warn("cookie配置文件读取异常,参数为" + key + "启用默认值" + defaultValue);
            value = defaultValue;
        }
        return value.trim();
    }

    public static String getSystemConfig(String key) {
        String value = SYSTEM_PROPS.getProperty(key.trim());
        if (MyStringUtil.isBlank(value)) {
            log.error("System配置文件读取异常,参数为" + key);
            return null;
        }
        return value.trim();
    }

    public static String getSystemConfig(String key, String defaultValue) {

        String value = SYSTEM_PROPS.getProperty(key.trim());
        if (MyStringUtil.isBlank(value)) {
            log.warn("System配置文件读取异常,参数为" + key + "启用默认值" + defaultValue);
            value = defaultValue;
        }
        return value.trim();
    }

    public static int getSystemConfig(String key, int defaultValue) {
        int value;
        try {
            value = Integer.parseInt(getSystemConfig(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            log.warn("consts配置文件读取异常,参数为" + key + "启用默认值" + defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static String getFtpConfig(String key, String defaultValue) {
        String value;
        try {
            value = FTP_PROPS.getProperty(key.trim());
        } catch (NumberFormatException e) {
            log.warn("consts配置文件读取异常,参数为" + key + "启用默认值" + defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static String getFtpConfig(String key) {
        String value = FTP_PROPS.getProperty(key.trim());
        if (MyStringUtil.isBlank(value)) {
            log.error("System配置文件读取异常,参数为" + key);
            return null;
        }
        return value.trim();

    }
}
