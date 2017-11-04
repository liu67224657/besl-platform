package com.enjoyf.platform.webapps.common.picture;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.enjoyf.platform.util.FileUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.fop.ImageView;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.net.CallRet;
import com.qiniu.api.resumableio.ResumeableIoApi;
import com.qiniu.api.rs.PutPolicy;
import net.sf.json.JSONObject;
import org.json.JSONException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/12
 * Description:
 */
public class AliPicManager {

    public static final String joyme_bucket = "joyme-test";
    public static final String joyme_accessKey = "qwsCWWgFxGLRCEDE";   //由TO提供
    public static final String joyme_secretKey = "kxPU8KqUudqABBcLXzqK4dXo1f28Rz";
    private static final String host = "http://oss-cn-beijing.aliyuncs.com";


    private String bucket;
    private String accessKey;
    private String secretKey;


    public AliPicManager() {
        this(joyme_bucket, joyme_accessKey, joyme_secretKey);
    }

    public AliPicManager(String bucket, String accessKey, String secretKey) {
        this.bucket = bucket;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    public PutObjectResult uploadFile(String key, MultipartFile file) throws IOException {
        // 初始化OSSClient
        OSSClient client = new OSSClient(host, accessKey, joyme_secretKey);
        // 获取指定文件的输入流
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentLength(file.getSize());
        meta.setContentType(file.getContentType());

        // 上传Object.
        PutObjectResult result = client.putObject(bucket, key, file.getInputStream(), meta);

        return result;
    }


    public String getToken() throws AuthException, JSONException {
        Mac mac = new Mac(this.accessKey, this.secretKey);
        PutPolicy putPolicy = new PutPolicy(this.bucket);
        String token = putPolicy.token(mac);
        return token;
    }

}
