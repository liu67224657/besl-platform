package com.enjoyf.platform.util.umeng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.enjoyf.platform.util.log.GAlerter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UmengNotification {

    private static final Logger logger = LoggerFactory.getLogger(UmengNotification.class);
    // This JSONObject is used for constructing the whole request string.
    protected final JSONObject rootJson = new JSONObject();

    // This object is used for sending the post request to Umeng
    protected HttpClient client = new DefaultHttpClient();

    // The host
    protected static final String host = "http://msg.umeng.com";

    // The upload path
    protected static final String uploadPath = "/upload";

    // The post path
    protected static final String postPath = "/api/send";

    // The user agent
    protected final String USER_AGENT = "Mozilla/5.0";

    public static final String APP_KEY = "appkey";

    // Keys can be set in the root level
    protected static final HashSet<String> ROOT_KEYS = new HashSet<String>(Arrays.asList(new String[]{
            "appkey", "timestamp", "validation_token", "type", "device_tokens", "alias", "file_id",
            "filter", "production_mode", "feedback", "description", "thirdparty_id"}));

    // Keys can be set in the policy level
    protected static final HashSet<String> POLICY_KEYS = new HashSet<String>(Arrays.asList(new String[]{
            "start_time", "expire_time", "max_send_num"
    }));

    // Set predefined keys in the rootJson, for extra keys(Android) or customized keys(IOS) please
    // refer to corresponding methods in the subclass.
    public abstract boolean setPredefinedKeyValue(String key, Object value) throws Exception;

    public boolean send() throws Exception {
        String url = host + postPath;
        HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", USER_AGENT);
        StringEntity se = new StringEntity(rootJson.toString(), "UTF-8");
        System.out.println(rootJson.toString());
        logger.info("----------------------------------" + rootJson.toString() + "-------------------------------------");

        post.setEntity(se);
        // Send the post request and get the response
        HttpResponse response = client.execute(post);
        int status = response.getStatusLine().getStatusCode();

        logger.info("--------------------------------UmengNotification Response Code : " + status + "---------------------------------");

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        logger.info("--------------------------------UmengNotification result : " + result.toString() + "---------------------------------");

        if (status == 200) {
            logger.info("--------------------------------UmengNotification Notification sent successfully.---------------------------------");
            return true;
        } else {
            logger.info("--------------------------------UmengNotification Failed to send the notification!---------------------------------");
            return false;
        }
    }

}
