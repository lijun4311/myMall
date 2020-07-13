package com.mall.util;

import com.mall.common.consts.propertiesconsts.FtpConsts;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


/**
 * @author lijun
 * @date 2020-06-30 15:57
 * @description ftp工具类
 * @error
 * @since version-1.0
 */
public class FtpUtil implements MyLogUtil {

    private static FTPClient getFtpClient() {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FtpConsts.IP);
            ftpClient.login(FtpConsts.USER, FtpConsts.PASS);
        } catch (IOException e) {
            log.error("连接FTP服务器异常", e);
        }
        return ftpClient;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        return FtpUtil.upLoadFile("img", fileList);
    }

    public static boolean upLoadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP服务器
        FTPClient ftpClient = getFtpClient();
        if (!MyBeanUtil.isRequired(ftpClient)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }

            } catch (IOException e) {
                log.error("上传文件异常", e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }


}
