package com.mall.common.myinterceptor;

import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;
import com.mall.common.consts.UserEnum;
import com.mall.common.consts.propertiesconsts.RedisConsts;
import com.mall.entity.User;
import com.mall.util.*;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lijun
 * @date 2020-06-16 20:15
 * @description 用户登录信息拦截器
 * @error 返回 {@link Rest#noLgoin()}
 * @since version-1.0
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UserLogin methodClass = handlerMethod.getBean().getClass().getAnnotation(UserLogin.class);
        UserLogin method = handlerMethod.getMethod().getAnnotation(UserLogin.class);
        if (!MyBeanUtil.isNull(method) || !MyBeanUtil.isNull(methodClass)) {
            String loginToken = CookieUtil.readLoginToken(request);
            if (MyStringUtil.isNotEmpty(loginToken)) {
                String userJsonStr = RedisPoolUtil.get(loginToken);
                User user = JsonUtil.string2Obj(userJsonStr, User.class);
                if (!MyBeanUtil.isRequired(user)) {
                    if (request.getServletPath().indexOf(UserEnum.Role.ADIMN_PATH) > 0) {
                        if (user.getRole() != UserEnum.Role.ROLE_ADMIN) {
                            return false;
                        }
                    }
                    request.setAttribute(UserEnum.REQUEST_USER.getName(), user);
                    request.setAttribute(UserEnum.REQUEST_TOKEN.getName(), loginToken);
                    RedisPoolUtil.expire(loginToken, RedisConsts.REDIS_SESSION_EXTIME);
                    return true;
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.append(JsonUtil.obj2String(Rest.noLgoin()));
            out.close();
            return false;
        }
        return true;
    }
}
