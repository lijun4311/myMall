package com.mall.common.consts.propertiesconsts;

import com.mall.util.PropertiesUtil;

/**
 * @author lijun
 * @date 2020-06-16 19:33
 * @description Ftp 配置常量
 * @error
 * @since version-1.0
 */
public interface FtpConsts {
    /**
     * ftp资源路径前缀
     */
    String HTTP_PREFIX = PropertiesUtil.getFtpConfig("HTTP_PREFIX", "http://localhost/");
    /**
     * ftp 服务ip
     */
    String IP = PropertiesUtil.getFtpConfig("SERVER_IP", "182.92.82.103");
    /**
     * 用户名
     */
    String USER = PropertiesUtil.getFtpConfig("USER", "mmallftp");
    /**
     * 密码
     */
    String PASS = PropertiesUtil.getFtpConfig("PASS", "ftppassword");
}
