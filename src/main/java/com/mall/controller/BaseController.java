package com.mall.controller;

import com.mall.common.consts.UserEnum;
import com.mall.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author lijun
 * @Date 2020-05-19 19:38
 * @Description 用户控制器基类
 * @Since version-1.0
 */
public class BaseController {
    protected final static Logger log = LoggerFactory.getLogger("AsyncLogger");
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;

    /**
     * 获得登录用户对象
     *
     * @return 用户信息
     */
    protected User getUser() {
        return (User) request.getAttribute(UserEnum.REQUEST_USER.getName());
    }

    /**
     * 获得用户登录token
     *
     * @return token
     */
    protected String getLoginToken() {
        return (String) request.getAttribute(UserEnum.REQUEST_TOKEN.getName());
    }
}
