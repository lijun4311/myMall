package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.mall.common.annotation.ArgsNotNull;
import com.mall.common.consts.propertiesconsts.FtpConsts;
import com.mall.entity.BaseEntity;
import com.mall.entity.Category;
import com.mall.entity.Product;
import com.mall.mapper.CategoryMapper;
import com.mall.mapper.ProductMapper;
import com.mall.service.ProductService;
import com.mall.util.MyBeanUtil;
import com.mall.util.MyDateUtil;
import com.mall.util.lambda.LambdaUtil;
import com.mall.vo.in.MyPageIn;
import com.mall.vo.out.ProductDetailVo;
import com.mall.vo.out.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-05-02
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @ArgsNotNull
    public ProductListVo assembleProductVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(FtpConsts.HTTP_PREFIX);
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    @Override
    @ArgsNotNull
    public List<ProductListVo> assembleProductListVo(List<Product> products) {
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : products) {
            productListVoList.add(assembleProductVo(product));
        }
        return productListVoList;
    }

    @Override
    @ArgsNotNull("categoryIds")
    public Page<Product> getProductPage(MyPageIn myPageIn, Set<Integer> categoryIds) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(MyBeanUtil.isNotRequired(categoryIds), LambdaUtil.convertToFieldName(Product::getCategoryId), categoryIds);
        return getPage(myPageIn, queryWrapper, Product.class);
    }

    @Override
    @ArgsNotNull
    public ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setImageHost(FtpConsts.HTTP_PREFIX);
        Category category = Optional.ofNullable(product.getCategoryId()).map(categoryId -> categoryMapper.selectById(categoryId)).orElse(null);
        productDetailVo.setParentCategoryId(Optional.ofNullable(category).map(BaseEntity::getId).orElse(0));
        productDetailVo.setCreateTime(MyDateUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(MyDateUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }


}
