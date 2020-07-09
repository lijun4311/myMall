package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall.common.annotation.ArgsNotEmpty;
import com.mall.common.consts.UserEnum;
import com.mall.entity.User;
import com.mall.mapper.UserMapper;
import com.mall.service.UserService;
import com.mall.util.Md5Util;
import com.mall.util.MyBeanUtil;
import com.mall.util.MyStringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
    @Override
    @ArgsNotEmpty
    public User login(String username, String password) {
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        String md5Password = Md5Util.md5EncodeUtf8(password);
        userQuery.eq(User::getUsername, username);
        userQuery.eq(User::getPassword, md5Password);
        return this.getOne(userQuery);
    }


    @Override
    @ArgsNotEmpty
    public String checkUserValidMap(Map<String, String> param) {
        List<Map<String, String>> list = baseMapper.checkUserValid(param);
        param.clear();
        for (Map<String, String> maps : list) {
            String key = UserEnum.COLUMNCOUNT.getName();
            if (Integer.parseInt(maps.get(key)) > 0) {
                return maps.get(UserEnum.COLUMNNAME.getName());
            }
        }
        return null;
    }

    @Override
    public User register(User user) {
        if (MyBeanUtil.isValueBankExclude(user, true, User::getRole)) {
            throw new IllegalArgumentException();
        }
        user.setRole(UserEnum.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(Md5Util.md5EncodeUtf8(user.getPassword()));
        boolean flag = this.save(user);
        return flag ? user : null;
    }

    @Override
    @ArgsNotEmpty
    public String selectQuestion(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("question");
        queryWrapper.eq("username", username);
        User user = this.getOne(queryWrapper);
        return Optional.ofNullable(user).map(User::getQuestion).orElse(null);
    }

    @Override
    @ArgsNotEmpty
    public boolean checkAnswer(String username, String question, String answer) {
        if (MyStringUtil.isBlanks(username, question, answer)) {
            throw new IllegalArgumentException();
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.eq(User::getAnswer, answer);
        queryWrapper.eq(User::getQuestion, question);
        int count = this.count(queryWrapper);
        return count > 0;
    }

    @Override
    public boolean resetPassword(String username, String passwordNew, String passwordOld, boolean flag) {
        if (MyStringUtil.isBlanks(username, passwordNew)) {
            throw new IllegalArgumentException();
        }
        String md5Password = Md5Util.md5EncodeUtf8(passwordNew);

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getPassword, md5Password);
        updateWrapper.eq(User::getUsername, username);
        if (flag) {
            if (MyStringUtil.isNotBlank(passwordOld)) {
                String md5PasswordOld = Md5Util.md5EncodeUtf8(passwordOld);
                updateWrapper.eq(User::getPassword, md5PasswordOld);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return this.update(null, updateWrapper);
    }

}
