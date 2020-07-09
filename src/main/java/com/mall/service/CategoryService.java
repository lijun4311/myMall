package com.mall.service;

import com.mall.entity.Category;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
public interface CategoryService extends BaseService<Category> {
    /**
     * 获得类目id子类目集合
     * @param categoryId 类目id
     * @return 类目id集合
     */
    Set<Integer> selectCategoryById(int categoryId);
}
