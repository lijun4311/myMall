package com.mall.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.entity.Category;
import com.mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author lijun
 * @date 2020-07-14 19:40
 * @description 商品类别控制器
 * @since version-1.0
 * @error
 */
@Controller
@RequestMapping("/manage/category")
@UserLogin
public class CategoryManageController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("addCategory")
    @ResponseBody
    @WebParamNotEmpty
    public Rest<String> addCategory(@WebParamNotEmpty String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setParentId(parentId);
        return categoryService.save(category) ? Rest.okMsg("添加成功") : Rest.errorMsg("添加失败");
    }

    @RequestMapping("setCategoryName")
    @ResponseBody
    @WebParamNotEmpty
    public Rest setCategoryName(Integer categoryId, String categoryName) {
        return categoryService.update(new LambdaUpdateWrapper<Category>()
                .eq(Category::getId, categoryId).set(Category::getName, categoryName)
        ) ? Rest.okMsg("成功") : Rest.errorMsg("失败");

    }

    @RequestMapping("getCategory")
    @ResponseBody
    public Rest getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return Rest.okData(categoryService.list(new LambdaQueryWrapper<Category>().eq(Category::getParentId, categoryId)));
    }

    @RequestMapping("getDeepCategory")
    @ResponseBody
    public Rest getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return Rest.okData(categoryService.selectCategoryById(categoryId));
    }


}
