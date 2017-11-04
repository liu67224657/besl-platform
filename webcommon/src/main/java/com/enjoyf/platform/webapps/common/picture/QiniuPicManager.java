package com.enjoyf.platform.webapps.common.picture;

import com.enjoyf.platform.util.FileUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpByteData;
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

import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/12
 * Description:
 */
@Deprecated
public class QiniuPicManager {

    public static final String joyme_bucket = "joymepic";
    public static final String joyme_accessKey = "G8_5kjfXfaufU53Da4bnGQ3YP-dhdmqct9sR6ImI";
    public static final String joyme_secretKey = "KXwyeZMxYnsZMqAwojI_IEDkYj69zkwvu8jZP5_a";

    private static final Pattern PATTERN = Pattern.compile("at[^\\[]*\\[([^\\]]+)\\]");

    private String bucket;
    private String accessKey;
    private String secretKey;


    public QiniuPicManager() {
        this(joyme_bucket, joyme_accessKey, joyme_secretKey);
    }

    public QiniuPicManager(String bucket, String accessKey, String secretKey) {
        this.bucket = bucket;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String uploadFile(String key, InputStream is, String mimeType) throws AuthException, JSONException {
        Mac mac = new Mac(this.accessKey, this.secretKey);
        PutPolicy putPolicy = new PutPolicy(this.bucket);
        String token = putPolicy.token(mac);

        if (key.startsWith("/")) {
            key = key.substring(1, key.length());
        }

        PutRet ret = ResumeableIoApi.put(is, token, key, mimeType);
        return ret.getKey();
    }

    public String uploadFile(String key, String localFile) throws Exception {

        Mac mac = new Mac(this.accessKey, this.secretKey);
        PutPolicy putPolicy = new PutPolicy(this.bucket);
        String token = putPolicy.token(mac);

        if (key.startsWith("/")) {
            key = key.substring(1, key.length());
        }

        return sendRequestToqiniu(key, token, localFile);
    }

    public String uploadFile(String key, HttpByteData byteData) throws Exception {

        Mac mac = new Mac(this.accessKey, this.secretKey);
        PutPolicy putPolicy = new PutPolicy(this.bucket);
        String token = putPolicy.token(mac);

        if (key.startsWith("/")) {
            key = key.substring(1, key.length());
        }

        return sendRequestToqiniu(key, token, byteData);
    }


//    public String uploadFile(String uploadpath, String key, MultipartFile multipartFile, String mimeType) throws Exception {
//        Mac mac = new Mac(this.accessKey, this.secretKey);
//        PutPolicy putPolicy = new PutPolicy(this.bucket);
//        String token = putPolicy.token(mac);
//        if (key.startsWith("/")) {
//            key = key.substring(1, key.length());
//        }
//
//
//        String partTmpPath = uploadpath + "/" + key;
//        String returnQiniuFilePath = null;
//        try {
//            File fileLocalPath = new File(partTmpPath);
//            CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartFile;
//            commonsMultipartFile.transferTo(fileLocalPath);
//            returnQiniuFilePath = sendRequestToqiniu(key, token, partTmpPath);
//        } catch (Exception e) {
//            return returnQiniuFilePath;
//        } finally {
//            FileUtil.deleteFileOrDir(partTmpPath);
//        }
//        return returnQiniuFilePath;
//    }

    private String sendRequestToqiniu(String key, String token, HttpByteData httpByteData) throws Exception {
        HttpClientManager httpClientManager = new HttpClientManager();
        HttpResult result = httpClientManager.multPartURL("http://up.qiniu.com/", new HttpParameter[]{
                new HttpParameter("key", key),
                new HttpParameter("token", token),
                new HttpParameter("file", httpByteData)
        });

        if (result.getReponseCode() == 200) {
            JSONObject jsonObject = JSONObject.fromObject(result.getResult());
            if (jsonObject.containsKey("key")) {
                return key;
            }
        } else {
            GAlerter.lab(this.getClass().getName() + " upload qiniu response not ok." + result);
        }
        return null;
    }


    private String sendRequestToqiniu(String key, String token, String fileRealPath) throws Exception {
        HttpClientManager httpClientManager = new HttpClientManager();
        HttpResult result = httpClientManager.multPartURL("http://up.qiniu.com/", new HttpParameter[]{
                new HttpParameter("key", key),
                new HttpParameter("token", token),
                new HttpParameter("file", new File(fileRealPath))
        });

        if (result.getReponseCode() == 200) {
            JSONObject jsonObject = JSONObject.fromObject(result.getResult());
            if (jsonObject.containsKey("key")) {
                return key;
            }
        } else {
            GAlerter.lab(this.getClass().getName() + " upload qiniu response not ok." + result);
        }
        return null;
    }


    public String getToken() throws AuthException, JSONException {
        Mac mac = new Mac(this.accessKey, this.secretKey);
        PutPolicy putPolicy = new PutPolicy(this.bucket);
        String token = putPolicy.token(mac);
        return token;
    }

    //qnuptk.tld 自定义标签
    public static String getUpTokenTag() throws AuthException, JSONException {
        Mac mac = new Mac(joyme_accessKey, joyme_secretKey);
        PutPolicy putPolicy = new PutPolicy(joyme_bucket);
        String token = putPolicy.token(mac);
        return token;
    }

    public static void main(String[] args) {
//        try {
//            System.out.println(new QiniuPicManager().getToken());
//
//            System.out.println(new QiniuPicManager().getToken());
//
//            System.out.println(new QiniuPicManager().getToken());
//        } catch (AuthException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}
