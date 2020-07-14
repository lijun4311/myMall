package com.mall.service;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author lijun
 * @date 2020-07-14 19:37
 * @description 文件上传服务
 * @since version-1.0
 * @error
 */
public interface FileService {
    /**
     *  文件上传功能
     * @param file 文件对象
     * @param path 上传路径
     * @return 实际路径
     */
    String upload(MultipartFile file, String path);
}
