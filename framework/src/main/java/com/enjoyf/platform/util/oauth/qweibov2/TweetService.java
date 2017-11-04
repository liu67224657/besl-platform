package com.enjoyf.platform.util.oauth.qweibov2;

import com.enjoyf.platform.props.QWeiboAuthV2Config;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.IOException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class TweetService {

    public JSONObject add(RequestAuthInfo authInfo, String content, String format) {
        return add(authInfo, content, "127.0.0.1", "", "", 0, format);
    }

    public JSONObject addPic(RequestAuthInfo authInfo, String content, String img, String format) {
        return addPic(authInfo, content, img, "127.0.0.1", "", "", 0, format);
    }

    public JSONObject add(RequestAuthInfo authInfo, String content, String ip, String jing, String wei, int syncFlag, String format) {
        HttpParameter[] params = {
                new HttpParameter("oauth_consumer_key", authInfo.getClientId()),
                new HttpParameter("access_token", authInfo.getAccessToken()),
                new HttpParameter("openid", authInfo.getOpenId()),
                new HttpParameter("oauth_version", "2.a"),
                new HttpParameter("scope", "all"),
                new HttpParameter("format", format),
                new HttpParameter("syncFlag", syncFlag),
                new HttpParameter("wei", wei),
                new HttpParameter("scope", jing),
                new HttpParameter("ip", ip),
                new HttpParameter("content", content),
        };

        HttpResult result = new HttpClientManager().post(QWeiboAuthV2Config.get().getQweiboApiUrl() + "t/add", params, null);

        return (JSONObject) JSONValue.parse(result.getResult());
    }


    public JSONObject addPic(RequestAuthInfo authInfo, String content, String pic, String ip, String jing, String wei, int syncFlag, String format) {
        HttpParameter[] params = {
                new HttpParameter("oauth_consumer_key", authInfo.getClientId()),
                new HttpParameter("access_token", authInfo.getAccessToken()),
                new HttpParameter("openid", authInfo.getOpenId()),
                new HttpParameter("oauth_version", "2.a"),
                new HttpParameter("scope", "all"),
                new HttpParameter("format", format),
                new HttpParameter("syncFlag", syncFlag),
                new HttpParameter("wei", wei),
                new HttpParameter("scope", jing),
                new HttpParameter("ip", ip),
                new HttpParameter("content", content),
                new HttpParameter("pic",  new File(pic))

        };

        HttpResult result = null;

        try {
            result = new HttpClientManager().multPartURL(QWeiboAuthV2Config.get().getQweiboApiUrl() + "t/add_pic", params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (JSONObject) JSONValue.parse(result.getResult());
    }
}
