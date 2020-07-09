package com.mall.service;

import com.mall.entity.User;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
public interface UserService extends BaseService<User> {
    /**
     * 用户登录服务
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 用户是否登录成功
     */
    User login(String username, String password);

    /**
     * 效验参数是否重复
     *
     * @param param 效验参数键值集合
     * @return 效验结果集合
     */
    String checkUserValidMap(Map<String, String> param);

    /**
     * 用户注册
     *
     * @param user 用户对象
     * @return 是否成功
     */
    User register(User user);

    /**
     * 获得用户密码提示问题
     *
     * @param username 用户名
     * @return null为无用户 empty 为无密码提示问题
     */
    String selectQuestion(String username);

    /**
     * 验证密码答案是否正确
     *
     * @param username 用户名
     * @param question 提示问题
     * @param answer   答案
     * @return boolean
     */
    boolean checkAnswer(String username, String question, String answer);


    /**
     * 重置密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param passwordOld 原密码 可为null
     * @param flag        是否效验原密码
     * @return boolean
     */
    boolean resetPassword(String username, String passwordNew, String passwordOld, boolean flag);
}
