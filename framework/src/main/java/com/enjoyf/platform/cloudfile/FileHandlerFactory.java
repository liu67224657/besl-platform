package com.enjoyf.platform.cloudfile;

import com.enjoyf.platform.service.content.ResourceFileType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.regex.RegexUtil;
import com.qiniu.util.Auth;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/8/17
 * Description:
 */
public class FileHandlerFactory {

    private static Map<BucketInfo, UploadHandler> map = new HashMap<BucketInfo, UploadHandler>();

    private static QiniuUploadHandler qnHandler = new QiniuUploadHandler();
    private static AliyUploadHandler alyHandler = new AliyUploadHandler();
    private static final Pattern HTTP_PATTERN = Pattern.compile("https?://([^.]+).*");

    private static final Pattern HTTP_HOST_PATTERN = Pattern.compile("https?://([^/]+)");

    static {
        map.put(BucketInfo.BUCKET_INFO_JOYMEPIC_QN, qnHandler);
        map.put(BucketInfo.BUCKET_INFO_JOYMEPIC_ALIYUN, alyHandler);
    }


    public static String upload(BucketInfo bucket, String key, InputStream in, String contentType, int contentLength) throws Exception {
        return getHandler(bucket).upload(key, bucket.getBucket(), in, contentType, contentLength);
    }

    /**
     * @param bucket
     * @param key
     * @param localFile
     * @param contentType
     * @param contentLength
     * @return
     * @throws Exception
     */
    public static String upload(BucketInfo bucket, String key, InputStream localFile, String contentType, long contentLength) throws Exception {
        return getHandler(bucket).upload(key, bucket.getBucket(), localFile, contentType, contentLength);
    }

    public static String process(BucketInfo bucketInfo, String url, ImageProcessObject object) {
        return getHandler(bucketInfo).processImage(url, object);
    }


    /**
     * 生成文件名称
     *
     * @param bucket
     * @param fileType
     * @param fileName
     * @return
     */
    public static String getPath(BucketInfo bucket, ResourceFileType fileType, String fileName, String extname) {
        return getHandler(bucket).getPath(bucket.getThirdKey(), fileType, fileName, extname);
    }

    /**
     * 返回上传的token
     *
     * @param bucketInfo
     * @return
     * @throws Exception
     */
    public static String getToken(BucketInfo bucketInfo) throws Exception {
        return getHandler(bucketInfo).getToken(bucketInfo.getBucket());
    }

    /**
     * 通过图片的path获得全路径
     *
     * @param path
     * @return
     */
    public static String getUrlByPath(String path) {
        if (StringUtil.isEmpty(path)) {
            return path;
        }

        return getUrlByThirdKey(path);
    }


    private static UploadHandler getHandler(BucketInfo bucketInfo) {
        return map.get(bucketInfo);
    }


    private static String getUrlByThirdKey(String path) {
        String thirdCode = "";
        if (path.contains("http://") || path.contains("https://")) {
            List<Map<String, String>> bucketKeyFetchMap = RegexUtil.fetch(path, HTTP_PATTERN, 1);

            if (CollectionUtil.isEmpty(bucketKeyFetchMap)) {
                return path;
            }
            String bucketKey = bucketKeyFetchMap.get(0).get("1");
            BucketInfo bucketInfo = BucketInfo.getByCode(bucketKey);
            if (bucketInfo == null) {
                return path;
            }
            thirdCode = bucketInfo.getThirdKey();
        } else {
            thirdCode = path;
            if (thirdCode.startsWith("/")) {
                thirdCode = thirdCode.substring(1, thirdCode.length());
            }

            thirdCode = thirdCode.substring(0, thirdCode.indexOf("/")).replace("/", "");
        }

        return getAllPathByThirdCode(thirdCode, path);
    }

    private static String getAllPathByThirdCode(String thirdCode, String path) {
        if (path.contains("http://") || path.contains("https://")) {
            path = RegexUtil.replace(path, HTTP_HOST_PATTERN, "", -1);
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (thirdCode.equals(BucketInfo.QINIU)) {
            return BucketInfo.BUCKET_INFO_JOYMEPIC_QN.getHost() + path;
        } else if (thirdCode.equals(BucketInfo.ALIYUN)) {
            return BucketInfo.BUCKET_INFO_JOYMEPIC_ALIYUN.getHost() + path;
        } else {
            return "http://" + thirdCode + ".joyme.com" + path;
        }
    }

    /**
     * 视屏转码获取七牛服务器返回的事件id
     * @param bucketInfo
     * @param key
     * @param rules
     * @return
     */
    public static List<String> getPersistentIds(BucketInfo bucketInfo,String key){
    	return getHandler(bucketInfo).getPersistentIds(key);
    }

    public static void main(String[] args) {
//        System.out.println(FileHandlerFactory.getUrlByPath("http://r001.joyme.com/r001/game/xxx.jpg"));

//        System.out.println(FileHandlerFactory.getUrlByPath("/r001/game/xxx.jpg"));

        System.out.println(FileHandlerFactory.getUrlByPath("http://joymepic.qiniudn.com/r001/game/xxx.jpg"));

//        System.out.println(FileHandlerFactory.getUrlByPath("/qiniu/game/xxx.jpg"));
    }


}
