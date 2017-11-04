package com.enjoyf.platform.util.oauth.youku;

import com.enjoyf.platform.props.YoukuConfig;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.weixinservice.WeixinServUserInfo;
import net.sf.json.JSONObject;


/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/13
 * Description:
 */
public class YoukuUserService {

    /**
     * 通过code得到WeiXinAuth对象里面包括了accesstoken等属性openId
     *
     * @param accessToken
     * @return
     */
    public YoukuUser getUserInfo(String accessToken) {
        YoukuUser returnObj = new YoukuUser();
        HttpParameter[] params = {new HttpParameter("client_id", YoukuConfig.get().getApiKey()),
                new HttpParameter("access_token", accessToken)};
        HttpResult result = new HttpClientManager().get("https://openapi.youku.com/v2/users/myinfo.json", params);
        if (result.getReponseCode() == HttpClientManager.OK) {
            JSONObject json = JSONObject.fromObject(result.getResult());
            if (json.containsKey("id")) {
                try {
                    returnObj.setId(json.getInt("id"));
                    returnObj.setName(json.getString("name"));
                    if (json.containsKey("gender")) {
                        returnObj.setSex(json.getString("gender").equals("m") ? 0 : 1);
                    }
                    returnObj.setDescription(json.getString("description"));
                    returnObj.setAvatar_large(json.getString("avatar_large"));
                } catch (Exception e) {
                    GAlerter.lan(this.getClass().getName() + " weixin auth prase accesstoken from json error.", e);
                    return null;
                }
            } else {
                GAlerter.lan(this.getClass().getName() + " weixin auth error." + result.getResult());
                return null;
            }
        } else {
            GAlerter.lab("weixin getUserInfo,accessToken=" + accessToken + ",response result=" + result.getResult());
        }
        return returnObj;
    }
}
