package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.annotation.ArgsNotEmpty;
import com.mall.service.BaseService;
import com.mall.util.mybatisplus.MyQueryWrapper;
import com.mall.vo.in.MyPageIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author lijun
 * @Date 2020-05-25 19:24
 * @Description service实现类 基类
 * @Since version-1.0
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {
    protected final static Logger log = LoggerFactory.getLogger("AsyncLogger");

    @Override
    @ArgsNotEmpty("queryWrapper")
    public Page<T> getPage(MyPageIn myPageIn, QueryWrapper<T> queryWrapper, Class<T> tClass) {
        Page<T> pageData = new Page<>(myPageIn.getCurrent(), myPageIn.getSize());
        queryWrapper = MyQueryWrapper.getQueryWrapperPage(myPageIn, tClass, queryWrapper);
        pageData = this.page(pageData, queryWrapper);
        return pageData;
    }

    @Override
    @ArgsNotEmpty
    public Page<T> getPage(MyPageIn myPageIn) {
        Page<T> pageData = new Page<>(myPageIn.getCurrent(), myPageIn.getSize());
        pageData = this.page(pageData);
        return pageData;
    }
}
