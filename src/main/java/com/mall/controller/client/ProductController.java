package com.mall.controller.client;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.Rest;
import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.controller.BaseController;
import com.mall.entity.Product;
import com.mall.service.CategoryService;
import com.mall.service.ProductService;
import com.mall.util.MyBeanUtil;
import com.mall.util.MyStringUtil;
import com.mall.vo.in.MyPageIn;
import com.mall.vo.out.MyPageVo;
import com.mall.vo.out.ProductDetailVo;
import com.mall.vo.out.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;
/**
 * @Author lijun
 * @Date 2020-06-15 17:08
 * @Description  商品详情控制器
 * @Since version-1.0
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 获得物品详情分页
     * @param myPageIn 分页查询对象
     * @return 分页对象集合
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    @WebParamNotEmpty
    public Rest<MyPageVo> list(MyPageIn myPageIn) {
        Set<Integer> categoryIds = MyBeanUtil.isRequired(myPageIn.getSearchId())
                ? null : categoryService.selectCategoryById(myPageIn.getSearchId());
        if (CollectionUtils.isEmpty(categoryIds) && MyStringUtil.isBlank(myPageIn.getFuzzySearchValue())) {
            return Rest.okData(MyPageVo.getEmpty(myPageIn));
        }
        Page<Product> pageData = productService.getProductPage(myPageIn, categoryIds);
        List<ProductListVo> productListVoList = productService.assembleProductListVo(pageData.getRecords());
        MyPageVo pageVo = MyPageVo.getInstance(productListVoList, pageData);
        return Rest.okData(pageVo);
    }

    /**
     * 获得物品详情
     * @param productId 物品详情id
     * @return 物品详情对象
     */
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ResponseBody
    @WebParamNotEmpty
    public Rest<ProductDetailVo> detail(Integer productId) {
        Product product = productService.getById(productId);
        if (MyBeanUtil.isRequired(product)) {
            return Rest.errorMsg("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = productService.assembleProductDetailVo(product);
        return Rest.okData(productDetailVo);
    }
}