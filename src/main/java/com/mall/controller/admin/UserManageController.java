package com.mall.controller.admin;

import com.mall.common.Rest;
import com.mall.common.consts.UserEnum;
import com.mall.controller.BaseController;
import com.mall.entity.User;
import com.mall.service.UserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * @author lijun
 * @date 2020-07-14 19:39
 * @description 管理员用户控制器
 * @since version-1.0
 * @error
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Rest<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        User user = userService.login(username, password);
        if (ObjectUtils.isEmpty(user)) {
            return Rest.errorMsg("用户名或密码错误");
        }
        if (user.getRole() == UserEnum.Role.ROLE_ADMIN) {
            RedisPoolUtil.setEx(CookieUtil.writeLoginToken(response), JsonUtil.obj2String(user));
            return Rest.okData(user);
        }
        return Rest.errorMsg("不是管理员,无法登录");
    }
}
