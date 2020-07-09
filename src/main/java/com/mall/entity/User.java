package com.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
@TableName("mmall_user")
@Data
public class User extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码，MD5加密
     */
    @JsonIgnore
    private String password;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 找回密码问题
     */
    private String question;

    /**
     * 找回密码答案
     */

    private String answer;

    /**
     * 角色0-管理员,1-普通用户
     */
    private Integer role;

}
