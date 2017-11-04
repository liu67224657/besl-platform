package com.enjoyf.platform.cloudfile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.qiniu.common.QiniuException;
import com.qiniu.processing.OperationManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/12
 * Description:
 */
public class AliyUploadHandler extends AbstractUploadHandler {

    public static final String joyme_accessKey = "m2LJu94lrAKPMGBm";   //由TO提供
    public static final String joyme_secretKey = "jO3aBvvxQKfBoBEHXadiLhG0YFi8OJ";
    private static final String host = "oss-cn-beijing.aliyuncs.com";

    public static final String STS_API_VERSION = "2015-04-01";
    public static final String STS_VERSION = "1";

    private String accessKey;
    private String secretKey;


    public AliyUploadHandler() {
        this(joyme_accessKey, joyme_secretKey);
    }

    public AliyUploadHandler(String joyme_accessKey, String joyme_secretKey) {
        this.accessKey = joyme_accessKey;
        this.secretKey = joyme_secretKey;
    }

    @Override
    public String upload(String path, String bucket, InputStream inputStream, String contentType, long contentLength) throws Exception {
        return uploadFile(path, bucket, inputStream, contentType, contentLength);
    }

    @Override
    public String upload(String path, String bucket, String localFile, String contentType, long contentLength) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File(localFile));

        return uploadFile(path, bucket, fileInputStream, contentType, contentLength);
    }


    public String uploadFile(String key, String bucket, InputStream inputStream, String contentType, long contentLength) throws IOException {
        // 初始化OSSClient
        OSSClient client = new OSSClient(host, accessKey, joyme_secretKey);
        // 获取指定文件的输入流
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentLength(contentLength);
        meta.setContentType(contentType);

        if (key.startsWith("/")) {
            key = key.substring(1, key.length());
        }
        // 上传Object.
        PutObjectResult result = client.putObject(bucket, key, inputStream, meta);

        return key;
    }

    public String getToken(String bucket) throws Exception {
        AssumeRoleResponse response = assumeRole(bucket);

        String accessId = response.getCredentials().getAccessKeyId();
        String accessSecr = response.getCredentials().getAccessKeySecret();
        String stsToken = response.getCredentials().getSecurityToken();
        String expiration = response.getCredentials().getExpiration();
        expiration = expiration.replace("T", " ").replace("Z", " ");
        //2015-08-28T02:13:38Z
        Date date = DateUtil.formatStringToDate(expiration, "yyyy-MM-dd HH:mm:ss");

        return accessId + "|" + accessSecr + "|" + stsToken + "|" + date.getTime();
    }


    @Override
    public String processImage(String url, ImageProcessObject obj) {

        url += "@";
        if (obj.getResizer() != null) {
            if (obj.getResizer().getWidth() > 0) {
                url += obj.getResizer().getWidth() + "w";
            }

            if (obj.getResizer().getHeight() > 0) {
                url += "_" + obj.getResizer().getHeight() + "h";
            }

            url += "_" + getFormat(obj.getResizer().getFormat());

        }

        if (obj.getCroper() != null) {
            url += obj.getCroper().getX() + "-" + obj.getCroper().getY() + "-" + obj.getCroper().getWidth() + "-" + obj.getCroper().getHeight() + "a";
        }

        url += "_" + ((int) obj.getQ()) + "q";

        return url;
    }

    private int getFormat(int format) {
        int f = 0;
        switch (format) {
            case 0:
                f = 0;
                break;
            case 1:
                f = 1;
                break;
            case 2:
                f = 2;
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }
        return f;

    }

    private AssumeRoleResponse assumeRole(String bucket) throws ClientException {
        String accessKeyID = "Sacm9Up2rawapY4N";
        String accessSecr = "A0lYsNE1rAZYVRFJ3ydaW2odFMm3Ag";
        String name = "aliypicpro";
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:DeleteObject\", \n" +
                "                \"oss:GetObject\", \n" +
                "                \"oss:PutObject\" \n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:1076711706756132:" + bucket + "\", \n" +
                "                \"acs:oss:*:1076711706756132:" + bucket + "/*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        try {
            return assumeRole(accessKeyID, accessSecr, "acs:ram::1076711706756132:role/aliypicpro", name, policy, ProtocolType.HTTPS);
        } catch (ClientException e) {
            throw e;
        }
    }

    private AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,
                                          String roleArn, String roleSessionName, String policy, ProtocolType protocolType) throws ClientException {
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-beijing", accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);

            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);

            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);

            final AssumeRoleResponse response = client.getAcsResponse(request);

            System.out.println(response);

            return response;
        } catch (ClientException e) {
            throw e;
        }
    }
    
    @Override
    public List<String> getPersistentIds(String key){
    	return null;
    }

    public static void main(String[] args) throws Exception {


        String[] s1 = new AliyUploadHandler().getToken("aliypicpro").split("\\|");

//        System.out.println(new AliyUploadHandler().getToken("aliypicpro"));
//        System.out.println(s1[0]);
//        System.out.println(s1[1]);
//        System.out.println(s1[2]);

        String bucketName = "aliypicpro";
        String filepath = "user/test/";
        String accessKeyId = s1[0];
//        "STS.cm0stjRH42fBF9Hw88sd";
        String accessKeySecret = s1[1];
        String securityToken = s1[2];
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        String uploadFilePath = "/Users/ericliu/Downloads/liuhao.mov";

        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret, securityToken);
        File file = new File(uploadFilePath);
        InputStream content = new FileInputStream(file);
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentLength(file.length());
        String key = filepath + "liuhao.mov";
        PutObjectResult result = client.putObject(bucketName, key, content, meta);

        System.out.println(result);
    }

}
