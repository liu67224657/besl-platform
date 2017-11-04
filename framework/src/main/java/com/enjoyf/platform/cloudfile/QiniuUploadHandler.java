package com.enjoyf.platform.cloudfile;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StreamUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpByteData;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.common.QiniuException;
import com.qiniu.processing.OperationManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.grammar.v3.ANTLRv3Parser.finallyClause_return;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/12
 * Description:
 */
public class QiniuUploadHandler extends AbstractUploadHandler {

    public static final String joyme_accessKey = "G8_5kjfXfaufU53Da4bnGQ3YP-dhdmqct9sR6ImI";
    public static final String joyme_secretKey = "KXwyeZMxYnsZMqAwojI_IEDkYj69zkwvu8jZP5_a";
    public static final Map<String, String> TRANSCODE_RULE_MAP=new HashMap<String, String>();
    static{
    	TRANSCODE_RULE_MAP.put("mp4", "avthumb/mp4/ab/128k/ar/44100/acodec/libfaac/r/30/vb/1200k/vcodec/libx264/s/854x480/autoscale/1/stripmeta/0");
//    	TRANSCODE_RULE_MAP.put("ogv", "avthumb/ogv/r/30/vb/1800k/vcodec/libtheora/ar/44100/ab/128k/acodec/libvorbis");
//    	TRANSCODE_RULE_MAP.put("flv", "avthumb/flv/r/30/vb/256k/vcodec/libx264/ar/22050/ab/64k/acodec/libmp3lame");
    }

    private String accessKey;
    private String secretKey;


    public QiniuUploadHandler() {
        this(joyme_accessKey, joyme_secretKey);
    }

    public QiniuUploadHandler(String joyme_accessKey, String joyme_secretKey) {
        this.accessKey = joyme_accessKey;
        this.secretKey = joyme_secretKey;
    }


    @Override
    public String upload(String path, String bucket, InputStream inputStream, String contentType, long contentLength) throws Exception {
        Mac mac = new Mac(this.accessKey, this.secretKey);
        PutPolicy putPolicy = new PutPolicy(bucket);
        String token = putPolicy.token(mac);

        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }
        HttpByteData byteData = new HttpByteData(StreamUtil.streamToBytes(inputStream), contentType);
        return sendRequestToqiniu(path, token, byteData);
    }

    @Override
    public String upload(String path, String bucket, String localFile, String contentType, long contentLength) throws Exception {
        Mac mac = new Mac(this.accessKey, this.secretKey);
        PutPolicy putPolicy = new PutPolicy(bucket);
        String token = putPolicy.token(mac);

        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }
        HttpByteData byteData = new HttpByteData(StreamUtil.streamToBytes(new FileInputStream(localFile)), contentType);
        return sendRequestToqiniu(path, token, byteData);
    }


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


    public String getToken(String bucket) throws Exception {
        Mac mac = new Mac(this.accessKey, this.secretKey);
        PutPolicy putPolicy = new PutPolicy(bucket);
        String token = putPolicy.token(mac);
        return token;
    }

    @Override
    public List<String> getPersistentIds(String key){
		try {
			Auth auth=Auth.create(joyme_accessKey, joyme_secretKey);
			OperationManager operationManager=new OperationManager(auth);
			StringMap str=new StringMap();
			//str.put("force", "1");
			//str.put("pipeline", "joymevideo");
			List<String> pIds=new ArrayList<String>();
			for (String keyStr : TRANSCODE_RULE_MAP.keySet()) {
				String persistentId=operationManager.pfop("joymepic",key , TRANSCODE_RULE_MAP.get(keyStr),str);
				if (!StringUtil.isEmpty(persistentId)) {
					pIds.add(persistentId);
				}
			}
			return pIds;
		} catch (QiniuException e) {
			GAlerter.lab(this.getClass().getName() + " transcode exception:" + e.getMessage());
			return null;
		}
    }

    @Override
    public String processImage(String url, ImageProcessObject obj) {
        if (obj.getResizer() != null) {
            if (!url.contains("?")) {
                url += "?";
            } else {
                url += "/";
            }
            url += "imageView2/" + getFormat(obj.getResizer().getFormat());
            if (obj.getResizer().getWidth() > 0) {
                url += "/w/" + obj.getResizer().getWidth();
            }

            if (obj.getResizer().getHeight() > 0) {
                url += "/h/" + obj.getResizer().getHeight();
            }

            url += "/q/" + ((int) obj.getQ());
        }

        if (obj.getCroper() != null) {
            if (!url.contains("?")) {
                url += "?";
            } else {
                url += "/";
            }
            url += "imageMogr2/crop/!" + obj.getCroper().getWidth() + "x" + obj.getCroper().getHeight() + "a" + obj.getCroper().getX() + "a" + obj.getCroper().getY();
            url += "/quality/" + ((int) obj.getQ());
        }

        if (obj.getResizer() == null && obj.getCroper() == null) {
            if (!url.contains("?")) {
                url += "?";
            } else {
                url += "/";
            }
            url += "imageView2/q/" + Integer.parseInt(String.valueOf(obj.getQ()));
        }

        return url;
    }

    private int getFormat(int format) {
        return format;
    }

    public static void main(String[] args) {
        String s = "http://developer.qiniu.com/resource/gogopher.jpg";

        ImageProcessObject object = new ImageProcessObject();
        object.setCroper(new ImageProcessObject.Croper(10, 10, 100, 100));
        object.setQ(10);

        System.out.println(new QiniuUploadHandler().processImage(s, object));
    }

}
