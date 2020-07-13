package com.mall.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.mall.common.Rest;
import com.mall.common.annotation.UserLogin;

import com.mall.common.annotation.WebParamNotEmpty;
import com.mall.common.consts.propertiesconsts.FtpConsts;
import com.mall.controller.BaseController;
import com.mall.entity.Product;
import com.mall.service.FileService;
import com.mall.service.ProductService;
import com.mall.util.MyBeanUtil;
import com.mall.util.lambda.LambdaUtil;
import com.mall.vo.in.MyPageIn;
import com.mall.vo.out.MyPageVo;
import com.mall.vo.out.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by geely
 */

@Controller
@RequestMapping("/manage/product")
@UserLogin
public class ProductManageController extends BaseController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;

    @RequestMapping("save")
    @ResponseBody
    @WebParamNotEmpty
    public Rest productSave(Product product) {
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImageArray = product.getSubImages().split(",");
            if (subImageArray.length > 0) {
                product.setMainImage(subImageArray[0]);
            }
        }
        return Rest.okData(productService.saveOrUpdate(product));
    }

    @RequestMapping("setSaleStatus")
    @ResponseBody
    @WebParamNotEmpty
    public Rest setSaleStatus(Integer productId, Integer status) {
        return Rest.okData(productService.update(new LambdaUpdateWrapper<Product>().eq(Product::getId, productId).set(Product::getStatus, status)));
    }

    @RequestMapping("detail")
    @ResponseBody
    public Rest getDetail(Integer productId) {
        Product product = productService.getById(productId);
        ProductDetailVo productDetailVo = productService.assembleProductDetailVo(product);
        return Rest.okData(productDetailVo);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Rest getList(MyPageIn myPageIn) {
        QueryWrapper queryWrapper = new QueryWrapper<Product>();
        queryWrapper.eq(!MyBeanUtil.isRequired(myPageIn.getSearchId()) , LambdaUtil.convertToFieldName(Product::getId), myPageIn.getSearchId());
        Page<Product> pageData = productService.getPage(myPageIn, queryWrapper, Product.class);
        return Rest.okData(MyPageVo.getInstance(pageData));
    }


    @RequestMapping("upload")
    @ResponseBody
    public Rest upload(@RequestParam(value = "upload_file", required = false) MultipartFile file) {

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file, path);
        String url = FtpConsts.HTTP_PREFIX + targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return Rest.okData(fileMap);

    }


    @RequestMapping("richtextImgUpload")
    @ResponseBody
    public Map richtextImgUpload(@RequestParam(value = "upload_file", required = false) MultipartFile file) {
        Map resultMap = Maps.newHashMap();
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = FtpConsts.HTTP_PREFIX + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }


}
