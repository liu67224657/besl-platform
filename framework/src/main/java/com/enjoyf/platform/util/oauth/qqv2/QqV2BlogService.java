package com.enjoyf.platform.util.oauth.qqv2;

import com.enjoyf.platform.props.QWeiboAuthV2Config;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class QqV2BlogService {
    private String clientId;

    public QqV2BlogService(String clientId) {
        this.clientId = clientId;
    }

    public JSONObject add(QqV2RequestAuthInfo authInfoQqV2, String content, String format) {
        return add(authInfoQqV2, content, "127.0.0.1", "", "", 0, format);
    }

    public JSONObject addPic(QqV2RequestAuthInfo authInfoQqV2, String content, String img, String format) {
        return addPic(authInfoQqV2, content, img, "127.0.0.1", "", "", 0, format);
    }

    public JSONObject add(QqV2RequestAuthInfo authInfoQqV2, String content, String ip, String jing, String wei, int syncFlag, String format) {
        HttpParameter[] params = {
                new HttpParameter("oauth_consumer_key", clientId),
                new HttpParameter("access_token", authInfoQqV2.getAccessToken()),
                new HttpParameter("openid", authInfoQqV2.getOpenId()),
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


    public JSONObject addPic(QqV2RequestAuthInfo authInfoQqV2, String content, String pic, String ip, String jing, String wei, int syncFlag, String format) {
        HttpParameter[] params = {
                new HttpParameter("oauth_consumer_key", clientId),
                new HttpParameter("access_token", authInfoQqV2.getAccessToken()),
                new HttpParameter("openid", authInfoQqV2.getOpenId()),
                new HttpParameter("oauth_version", "2.a"),
                new HttpParameter("scope", "all"),
                new HttpParameter("format", format),
                new HttpParameter("syncFlag", syncFlag),
                new HttpParameter("wei", wei),
                new HttpParameter("scope", jing),
                new HttpParameter("ip", ip),
                new HttpParameter("content", content),
                new HttpParameter("pic", pic)
        };

        HttpResult result = null;

        try {
//            result = new HttpClientManager().multPartURL(QWeiboAuthV2Config.get().getQweiboApiUrl() + "t/add_pic", params, FileUtils.readFileToByteArray(new File(pic)),"pic");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return (JSONObject) JSONValue.parse(result.getResult());
    }
}
