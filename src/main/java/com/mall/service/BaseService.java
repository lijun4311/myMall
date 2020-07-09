package com.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.annotation.ArgsNotEmpty;
import com.mall.vo.in.MyPageIn;

/**
 * @Author lijun
 * @Date 2020-05-25 19:22
 * @Description service 基类
 * @Since version-1.0
 */
public interface BaseService<T> extends IService<T> {
    /**
     * 通用分页查询默认
     * @param myPageIn 前端分页对象
     * @param queryWrapper 查询构造器 可以为 null
     * @param tClass 分页对象class
     * @return 分页数据
     */
    Page<T> getPage(MyPageIn myPageIn, QueryWrapper<T> queryWrapper, Class<T> tClass);

    /**
     * 通用分页查询
     * @param myPageIn
     * @return
     */
    @ArgsNotEmpty("queryWrapper")
    Page<T> getPage(MyPageIn myPageIn);
}
