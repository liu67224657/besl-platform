package com.enjoyf.platform.cloudfile;

import com.enjoyf.platform.service.content.ResourceFileType;
import com.qiniu.api.auth.AuthException;
import com.qiniu.util.Auth;

import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/8/17
 * Description:
 */
interface UploadHandler {

    /**
     * 网络流上传
     *
     * @param path
     * @param inputStream
     * @param contentType
     * @return
     * @throws Exception
     */
    public String upload(String path, String bucket, InputStream inputStream, String contentType, long contentLength) throws Exception;

    /**
     * 将本地文件上传
     *
     * @param path
     * @param localFile
     * @param contentType
     * @return
     * @throws Exception
     */
    public String upload(String path, String bucket, String localFile, String contentType, long contentLength) throws Exception;

    /**
     * 生成文件path和名称
     *
     * @param thirdKey
     * @param fileType
     * @param fileName
     * @return
     */
    public String getPath(String thirdKey, ResourceFileType fileType, String fileName, String extname);


    public String getToken(String bucket) throws Exception;


    public String processImage(String url, ImageProcessObject obj);
    
    /**
     * 七牛转码状态查询id
     * @param key
     * @return
     */
    public List<String> getPersistentIds(String key);
}
