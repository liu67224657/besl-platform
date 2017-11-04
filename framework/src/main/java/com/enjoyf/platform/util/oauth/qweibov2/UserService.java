package com.enjoyf.platform.util.oauth.qweibov2;

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
public class UserService {

    public JSONObject getUserInfo(RequestAuthInfo authInfo, String format) {
        JSONObject user = null;

        HttpParameter[] params = {
                new HttpParameter("oauth_consumer_key", authInfo.getClientId()),
                new HttpParameter("access_token", authInfo.getAccessToken()),
                new HttpParameter("oauth_version", "2.a"),
                new HttpParameter("scope", "all"),
                new HttpParameter("openid", authInfo.getOpenId()),
                new HttpParameter("format", format),
        };

        HttpResult result = new HttpClientManager().get(QWeiboAuthV2Config.get().getQweiboApiUrl() + "user/info", params);

        JSONObject resultObj = (JSONObject) JSONValue.parse(result.getResult());
        Long ret = (Long) resultObj.get("ret");
        if (ret == 0) {
            user = (JSONObject) resultObj.get("data");
        }

        return user;
    }

}
