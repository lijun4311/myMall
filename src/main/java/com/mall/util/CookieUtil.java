package com.mall.util;

import com.mall.common.consts.propertiesconsts.CookieConsts;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 * @Author lijun
 * @Date 2020-05-01 13:23
 * @Description cookie工具类
 * @Since version-1.0
 */
@Slf4j
public class CookieUtil {

    /**
     * 获得登录token
     *
     * @param request 请求
     * @return token  String
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (MyStringUtil.equals(ck.getName(), CookieConsts.COOKIE_TOKEN)) {
                    log.debug("return cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 写入token
     *
     * @param response 返回
     */
    public static String writeLoginToken(HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        Cookie ck = new Cookie(CookieConsts.COOKIE_TOKEN,token);
        //设置共享域
        ck.setDomain(CookieConsts.COOKIE_DOMAIN);
        //设置共享路径
        ck.setPath(CookieConsts.COOKIE_PATH);
        //设置只读
        ck.setHttpOnly(true);
        //单位是秒。
        //如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效。如果是-1，代表永久
        ck.setMaxAge(60 * 60 * 24 * 365);
        log.debug("write cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
        return token;
    }

    /**
     * 删除 Cookie
     *
     * @param request  请求
     * @param response 返回
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (MyStringUtil.equals(ck.getName(), CookieConsts.COOKIE_TOKEN)) {
                    ck.setDomain(CookieConsts.COOKIE_DOMAIN);
                    ck.setPath(CookieConsts.COOKIE_PATH);
                    //设置成0，代表删除此cookie。
                    ck.setMaxAge(0);
                    log.debug("del cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
}
