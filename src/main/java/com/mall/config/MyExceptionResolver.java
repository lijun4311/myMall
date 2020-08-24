package com.mall.config;


import com.google.common.collect.Maps;
import com.mall.common.consts.RestEnum;
import com.mall.common.myexception.RestException;
import com.mall.util.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;


/**
 * @Author lijun
 * @Date 2020-05-03 18:23
 * @Description 异常处理器
 * @Since version-1.0
 */
@Component
public class MyExceptionResolver extends
        SimpleMappingExceptionResolver {






    private final static Logger log = LoggerFactory.getLogger("AsyncLogger");

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, @NonNull HttpServletResponse response, Object handler, @NonNull Exception ex) {
        Enumeration<String> param = request.getParameterNames();
        Map<String, String> paramMap = Maps.newHashMap();
        while (param.hasMoreElements()) {
            String key = param.nextElement();
            paramMap.put(key, request.getParameter(key));
        }
        String token = CookieUtil.readLoginToken(request);
        log.error("\n 请求路径为: {}\n 请求Token为: {}\n 请求参数为: {}\n Exception:", request.getRequestURI(), token, paramMap, ex);
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        ModelAndView modelAndView = new ModelAndView(view);
        if (ex instanceof RestException) {
            RestEnum restConst = RestEnum.ERROR;
            modelAndView.addObject("status", restConst.getCode());
            modelAndView.addObject("msg", ex.getMessage());
            return modelAndView;
        }
        RestEnum restConst = RestEnum.ERROR;
        modelAndView.addObject("status", restConst.getCode());
        modelAndView.addObject("msg", restConst.getDesc());
        modelAndView.addObject("data", ex.toString());
        return modelAndView;
    }
}
