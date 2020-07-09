package com.mall.vo.out;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.mall.vo.in.MyPageIn;
import lombok.Data;

import java.util.List;
/**
 * @author lijun
 * @date 2020-06-30 19:09
 * @description  自定义分页封装对象
 * @since version-1.0
 * @error
 */
@Data
public class MyPageVo<T, R> {
    private List<T> records;
    private long total;
    private long size;
    private long current;
    private List<OrderItem> orders;
    private boolean optimizeCountSql;
    private boolean isSearchCount;

    private MyPageVo(List<T> records, Page<R> page) {
        this.records = records;
        this.total = page.getTotal();
        this.size = page.getSize();
        this.current = page.getCurrent();
        this.orders = page.getOrders();
        this.optimizeCountSql = page.optimizeCountSql();
        this.isSearchCount = page.isSearchCount();
    }

    private MyPageVo(MyPageIn myPageIn) {
        this.records = Lists.newArrayListWithExpectedSize(1);
        this.total = 0;
        this.size = myPageIn.getSize();
        this.current = myPageIn.getCurrent();
        this.orders = Lists.newArrayListWithExpectedSize(1);
        this.optimizeCountSql = false;
        this.isSearchCount = false;
    }

    public static <X, Y> MyPageVo<X, Y> getInstance(List<X> list, Page<Y> page) {
        return new MyPageVo<>(list, page);
    }

    public static <X, Y> MyPageVo<X, Y> getEmpty(MyPageIn myPageIn) {
        return new MyPageVo<>(myPageIn);
    }

    public static <Y> MyPageVo<Y, Y> getInstance(Page<Y> page) {
        return new MyPageVo<>(page.getRecords(), page);
    }
}
