package com.mall.common.consts.propertiesconsts;

import com.mall.util.PropertiesUtil;

/**
 * @author lijun
 * @date 2020-06-16 19:30
 * @description 全局cookie 配置常量
 * @error
 * @since version-1.0
 */
public interface CookieConsts {
    /**
     * domain域信息
     */
    String COOKIE_DOMAIN = PropertiesUtil.getCookieConfig("COOKIE_DOMAIN", "localhost");
    /**
     * path根路径
     */
    String COOKIE_PATH = PropertiesUtil.getCookieConfig("COOKIE_PATH", "/");
    /**
     * cookie 存储标识
     */
    String COOKIE_TOKEN = PropertiesUtil.getCookieConfig("COOKIE_TOKEN", "token");

}
