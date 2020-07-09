package com.mall.util.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.common.consts.propertiesconsts.SystemConsts;
import com.mall.util.MyBeanUtil;
import com.mall.util.MyStringUtil;
import com.mall.vo.in.MyPageIn;

/**
 * @author lijun
 * @date 2020-06-30 15:54
 * @description 自定义mp查询对象
 * @since version-1.0
 * @error
 */
public class MyQueryWrapper {
    /**
     * 通过分页对象获得筛选条件
     *
     * @param pageIn 分页对象
     * @param tClass 传入对象类型
     * @return QueryWrapper 查询条件对象
     */
    public static <T> QueryWrapper<T> getQueryWrapperPage(MyPageIn pageIn, Class<T> tClass, QueryWrapper<T> queryWrapper) {
        assert pageIn != null;
        queryWrapper = MyBeanUtil.isRequired(queryWrapper) ? new QueryWrapper<>() : queryWrapper;
        queryWrapper.like(
                MyBeanUtil.isFieldExist(pageIn.getFuzzySearchKey(), tClass)
                        && !MyBeanUtil.isRequired(pageIn.getFuzzySearchValue()),
                MyStringUtil.camelToUnderline(pageIn.getFuzzySearchKey()),
                pageIn.getFuzzySearchValue());
        queryWrapper.eq(MyBeanUtil.isFieldExist(pageIn.getAccurateSearchKey(), tClass)
                        && !MyBeanUtil.isRequired(pageIn.getAccurateSearchValue()),
                MyStringUtil.camelToUnderline(pageIn.getAccurateSearchKey()),
                pageIn.getAccurateSearchValue());
        queryWrapper.orderBy(MyBeanUtil.isFieldExist(pageIn.getOrderBy(), tClass),
                pageIn.getIsAsc() == SystemConsts.ASCEND,
                MyStringUtil.camelToUnderline(pageIn.getOrderBy()));
        return queryWrapper;
    }
}
