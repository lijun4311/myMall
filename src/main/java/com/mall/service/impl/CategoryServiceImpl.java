package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mall.entity.BaseEntity;
import com.mall.entity.Category;
import com.mall.mapper.CategoryMapper;
import com.mall.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public Set<Integer> selectCategoryById(int categoryId) {
        assert categoryId == 0;
        Category category = baseMapper.selectById(categoryId);
        Set<Category> categorySet = Sets.newHashSet();
        Optional.ofNullable(category).ifPresent(inCategory -> {
            categorySet.add(inCategory);
            findChildCategory(categorySet, Lists.newArrayList(inCategory.getId()));
        });
        return categorySet.stream().map(Category::getId).collect(Collectors.toSet());
    }

    /**
     * 递归算法获得所有类别
     *
     * @param categorySet 类别集合
     * @param ids         父级id
     */
    private void findChildCategory(Set<Category> categorySet, List<Integer> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<Category> categoryList = baseMapper.selectList(new LambdaQueryWrapper<Category>().in(Category::getParentId, ids));
            categoryList.forEach(
                    in -> findChildCategory(categorySet, Lists.transform(categoryList, BaseEntity::getId))
            );
            categorySet.addAll(categoryList);
        }
    }

}
