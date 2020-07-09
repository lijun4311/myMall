package com.mall.common.consts;

import com.mall.util.MyStringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author lijun
 * @Date 2020-05-19 19:44
 * @Description 用户信息常量类
 * @Since version-1.0
 */
public enum UserEnum {
    //用户名
    USERNAME("username", "用户名"),
    //邮箱
    EMAIL("email", "邮箱"),
    //返回列总条数
    COLUMNCOUNT("columnCount", "字段总条数"),
    //返回字段名
    COLUMNNAME("columnName", "字段名"),
    //重置密码token字段名
    TOKEN_PASS("token_pass", "密码重置"),
    //用户对象
    REQUEST_USER("request_user", "用户对象"),
    //用户token
    REQUEST_TOKEN("request_token", "用户token");

    /**
     * 用户角色
     */
    public interface Role {
        //普通用户
        int ROLE_CUSTOMER = 0;
        //管理员
        int ROLE_ADMIN = 1;
    }

    /**
     * 用户信息验证参数字段
     */
    final static private String[] CHECK_NAMES = new String[]{USERNAME.name, EMAIL.name};
    private final String name;
    private final String value;

    UserEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 是否存在可用验证字段
     *
     * @param type 字段类型
     * @return boolean
     */
    public static boolean isUserCheckExist(String type) {
        for (String name : CHECK_NAMES) {
            if (MyStringUtil.equals(name, type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得枚举类值
     *
     * @param name 枚举类名
     * @return 枚举类值
     */
    public static String getValue(String name) {
        for (UserEnum userConst : UserEnum.values()) {
            if (StringUtils.equals(userConst.name, name)) {
                return userConst.value;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }


    public String getValue() {
        return value;
    }

    /**
     * 对比枚举类名是否相等
     *
     * @param other 枚举类name
     * @return boolean
     */
    public boolean equalsName(String other) {
        if (StringUtils.isBlank(other)) {
            return false;
        }
        return this.name.equals(other);
    }
}
