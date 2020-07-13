package com.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.entity.Product;
import com.mall.vo.in.MyPageIn;
import com.mall.vo.out.ProductDetailVo;
import com.mall.vo.out.ProductListVo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
public interface ProductService extends BaseService<Product> {

    /**
     * 封装物品对象
     * @param product 物品对象
     * @return 封装对象
     */
    ProductListVo assembleProductVo(Product product);

    /**
     * 封装物品对象集合
     * @param products 物品对象集合
     * @return 封装对象集合
     */
    List<ProductListVo> assembleProductListVo(List<Product> products);

    /**
     *  根据类别id分页搜索 物品
     * @param myPageIn 分页条件
     * @param categoryIds 类别id
     * @return 分页对象
     */
    Page<Product> getProductPage(MyPageIn myPageIn, Set<Integer> categoryIds);

    /**
     *  封装物品详情
     * @param product 物品对象
     * @return  封装物品详情对象
     */
    ProductDetailVo assembleProductDetailVo(Product product);


}
