package com.mall.controller.client;

import com.google.common.collect.Maps;
import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.common.consts.UserEnum;
import com.mall.controller.BaseController;
import com.mall.entity.User;
import com.mall.service.ProductService;
import com.mall.service.UserService;
import com.mall.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.UUID;

/**
 * @Author lijun
 * @Date 2020-05-19 19:45
 * @Description 用户访问控制器
 * @Since version-1.0
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return rest
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @WebParamNotEmpty
    public Rest<User> login(String username, String password) {
        User user = userService.login(username, password);
        if (ObjectUtils.isEmpty(user)) {
            return Rest.errorMsg("用户名或密码错误");
        }
        RedisPoolUtil.setEx(CookieUtil.writeLoginToken(response), JsonUtil.obj2String(user));
        return Rest.okData(user);
    }

    /**
     * 用户退出
     *
     * @return 返回
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public Rest<String> logout() {
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        RedisPoolUtil.del(loginToken);
        return Rest.okMsg("用户退出成功");
    }

    /**
     * 用户信息
     *
     * @return 返回user
     */
    @RequestMapping(value = "userInfo", method = RequestMethod.POST)
    @ResponseBody
    @UserLogin
    public Rest<User> getUserInfo() {
        User user = getUser();
        if (user != null) {
            return Rest.okData(user);
        }
        return Rest.errorMsg("用户未登录,无法获取当前用户的信息");
    }

    /**
     * 效验 是否存在 数据
     *
     * @param validString 效验字符串
     * @param type        效验类型
     * @return boolean
     */
    @RequestMapping(value = "checkUserValid", method = RequestMethod.POST)
    @ResponseBody
    @WebParamNotEmpty
    public Rest<String> checkUserValid(String validString, String type) {
        Map<String, String> param = Maps.newHashMapWithExpectedSize(1);
        if (UserEnum.isUserCheckExist(type)) {
            param.put(type, validString);
            String result = userService.checkUserValidMap(param);
            if (MyStringUtil.isNotBlank(result)) {
                String value = UserEnum.getValue(result);
                return Rest.errorMsg((value == null ? "" : value) + "重复");
            }
            return Rest.okMsg("效验成功");
        } else {
            return Rest.illegalParam();
        }
    }

    /**
     * 注册
     *
     * @param user 用户对象
     * @return rest
     */

    @RequestMapping(value = "register", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Rest<User> register(User user) {
        if (MyBeanUtil.isValueBankExclude(user, true, User::getRole)) {
            return Rest.illegalParam();
        }
        Map<String, String> param = Maps.newHashMapWithExpectedSize(2);
        param.put(UserEnum.USERNAME.getName(), user.getUsername());
        param.put(UserEnum.EMAIL.getName(), user.getEmail());
        String result = userService.checkUserValidMap(param);
        if (MyStringUtil.isNotBlank(result)) {
            String value = UserEnum.getValue(result);
            return Rest.errorMsg((value == null ? "" : value) + "重复");
        }
        user = userService.register(user);
        if (ObjectUtils.isEmpty(user)) {
            return Rest.errorMsg("注册失败");
        } else {
            RedisPoolUtil.setEx(CookieUtil.writeLoginToken(response), JsonUtil.obj2String(user));
            return Rest.okDataMsg("注册成功", user);
        }

    }

    /**
     * 获得问题
     *
     * @param username 用户名
     * @return rest
     */
    @RequestMapping(value = "getQuestion", method = RequestMethod.POST)
    @ResponseBody
    @WebParamNotEmpty
    public Rest<String> getQuestion(String username) {
        String question = userService.selectQuestion(username);
        if (MyBeanUtil.isNull(question)) {
            return Rest.errorMsg("无此用户");
        }
        if (MyStringUtil.isBlank(question)) {
            return Rest.errorMsg("无提示问题");
        }
        return Rest.okData(question);
    }

    /**
     * 效验密码提示问题
     *
     * @param username 用户名
     * @param question 问题
     * @param answer   答案
     * @return rest
     */
    @RequestMapping(value = "checkAnswer", method = RequestMethod.POST)
    @ResponseBody
    @WebParamNotEmpty
    public Rest<String> checkAnswer(String username, String question, String answer) {
        if (userService.checkAnswer(username, question, answer)) {
            String forgetToken = UUID.randomUUID().toString();
            RedisPoolUtil.setEx(UserEnum.TOKEN_PASS.getName() + username, 60 * 60 * 12, forgetToken);
            return Rest.okData(forgetToken);
        }
        return Rest.errorMsg("密码提示答案错误");
    }

    /**
     * 修改新密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param forgetToken 密码token
     * @return rest
     */
    @RequestMapping(value = "forgetResetPassword", method = RequestMethod.POST)
    @ResponseBody
    @WebParamNotEmpty
    public Rest<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        String token = RedisPoolUtil.get(UserEnum.TOKEN_PASS.getName() + username);
        if (MyStringUtil.isBlank(token) || !MyStringUtil.equals(forgetToken, token)) {
            return Rest.errorMsg("token无效或者过期");
        }
        if (userService.resetPassword(username, passwordNew, null, false)) {
            RedisPoolUtil.del(UserEnum.TOKEN_PASS.getName() + username);
            CookieUtil.delLoginToken(request, response);
            return Rest.okMsg("修改成功");
        } else {
            return Rest.errorMsg("修改失败");
        }
    }

    /**
     * 老密码修改新密码
     *
     * @param passwordOld 原密码
     * @param passwordNew 新密码
     * @return rest
     */
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    @ResponseBody
    @WebParamNotEmpty
    @UserLogin
    public Rest<String> resetPassword(String passwordOld, String passwordNew) {
        User user = getUser();
        if (userService.resetPassword(user.getUsername(), passwordNew, passwordOld, true)) {
            return Rest.okMsg("密码修改成功");
        } else {
            return Rest.errorMsg("原密码不正确");
        }
    }

    /**
     * 更新用户信息
     *
     * @param user user用户信息
     * @return 返回更新的用户
     */
    @RequestMapping(value = "updateInformation", method = RequestMethod.POST)
    @ResponseBody
    @UserLogin
    public Rest<User> updateInformation(User user) {
        String loginToken = getLoginToken();
        User currentUser = getUser();
        if (MyStringUtil.isBlank(user.getEmail())) {
            return Rest.errorMsg("邮箱不正确");
        } else {
            if (!MyStringUtil.equals(currentUser.getEmail(), user.getEmail())) {
                Map<String, String> param = Maps.newHashMapWithExpectedSize(2);
                param.put(UserEnum.EMAIL.getName(), user.getEmail());
                String result = userService.checkUserValidMap(param);
                if (MyStringUtil.isNotBlank(result)) {
                    String value = UserEnum.getValue(result);
                    return Rest.errorMsg((value == null ? "" : value) + "重复,请更换");
                }
            }
        }
        user.setId(currentUser.getId());
        user.setPassword(null);
        user.setUsername(currentUser.getUsername());
        user.setRole(currentUser.getRole());
        boolean flag = userService.updateById(user);
        if (flag) {
            RedisPoolUtil.setEx(loginToken, JsonUtil.obj2String(user));
            return Rest.okDataMsg("更新个人信息成功", user);
        } else {
            return Rest.errorMsg("更新个人信息失败");
        }

    }

    /**
     * 获得用户信息更新缓存
     *
     * @return 用户信息
     */
    @RequestMapping(value = "getInformation", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    @UserLogin
    public Rest<User> getInformation() {
        String loginToken = getLoginToken();
        User currentUser = getUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            return Rest.noLgoin();
        }
        User user = userService.getById(currentUser.getId());
        if (ObjectUtils.isEmpty(user)) {
            return Rest.errorMsg("找不到当前用户");
        }
        RedisPoolUtil.setEx(loginToken, JsonUtil.obj2String(user));
        return Rest.okData(user);
    }
}
