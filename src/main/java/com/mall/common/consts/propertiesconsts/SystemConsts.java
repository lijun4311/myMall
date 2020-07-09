package com.mall.common.consts.propertiesconsts;

import com.mall.util.PropertiesUtil;

/**
 * @author lijun
 * @date 2020-06-16 19:38
 * @description 系统常量配置
 * @error
 * @since version-1.0
 */
public interface SystemConsts {
    /**
     * 密码加盐key
     */
    String PASSWORD_SALT = PropertiesUtil.getSystemConfig("PASSWORD_SALT", "");
    /**
     * 分页默认当前页
     */
    Integer PAGE_CURRENT = PropertiesUtil.getSystemConfig("PAGE_CURRENT", 1);
    /**
     * 分页总页数
     */
    Integer PAGE_SIZE = PropertiesUtil.getSystemConfig("PAGE_SIZE", 20);
    /**
     * 升序排序
     */
    int ASCEND = 0;
    /**
     * 降序排序
     */
    int DESCEND = 1;
}
