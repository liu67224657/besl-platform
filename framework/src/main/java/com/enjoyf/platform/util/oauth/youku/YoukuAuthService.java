package com.enjoyf.platform.util.oauth.youku;

import com.enjoyf.platform.props.YoukuConfig;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import net.sf.json.JSONObject;


/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/13
 * Description:
 */
public class YoukuAuthService {

    private YoukuConfig youkuConfig = YoukuConfig.get();
    //http://passport.joyme.test/auth/thirdapi/youku/bind
    public String getAuthorizeUrl(String redirectUrl) {
        String stat = Md5Utils.md5(String.valueOf(System.currentTimeMillis()));
        String url = "https://openapi.youku.com/v2/oauth2/authorize?" +
                "client_id=" + youkuConfig.getApiKey() + "&redirect_uri=" + redirectUrl + "&response_type=code&state=" + stat;
        return url;
    }


    /**
     * 通过code得到youkuAuth对象里面包括了accesstoken等属性
     *
     * @param code
     * @return
     */
    public YoukuAuth getAccessToken(String code,String redirect_uri) {

        YoukuAuth returnObj = new YoukuAuth();

        HttpParameter[] params = {new HttpParameter("grant_type", "authorization_code"),
                new HttpParameter("client_id", youkuConfig.getApiKey()),
                new HttpParameter("client_secret", youkuConfig.getApiSecret()),
                new HttpParameter("code", code),
                new HttpParameter("grant_type", "authorization_code"),
                new HttpParameter("redirect_uri", redirect_uri)};
        HttpResult result = new HttpClientManager().post("https://openapi.youku.com/v2/oauth2/token", params, new HttpParameter[]{});
        if (result.getReponseCode() == HttpClientManager.OK) {

            JSONObject json = JSONObject.fromObject(result.getResult());
            if (json.containsKey("access_token")) {
                try {
                    returnObj.setAccess_token(json.getString("access_token"));
                    returnObj.setExpires_in(json.getInt("expires_in"));
                    returnObj.setRefresh_token(json.getString("refresh_token"));
                    returnObj.setRefresh_token(json.getString("token_type"));
                } catch (Exception e) {
                    GAlerter.lan(this.getClass().getName() + " youku auth prase accesstoken from json error.", e);
                    return null;
                }
            } else {
                GAlerter.lan(this.getClass().getName() + " youku auth error." + result.getResult());
                return null;
            }
        } else {
            GAlerter.lab("youku getAccessToken,code=" + code + ",responsecode=" + result.getReponseCode() + ",response result=" + result.getResult());
        }
        return returnObj;
    }

    /**
     * accesstoken过期后调用改方法获取新的refreshtoken
     *
     * @param refreshToken
     * @return
     */
    public YoukuAuth refreshToken(String refreshToken) {
        YoukuAuth returnObj = new YoukuAuth();
        HttpParameter[] params = {new HttpParameter("grant_type", "refresh_token"),
                new HttpParameter("client_id", youkuConfig.getApiKey()),
                new HttpParameter("client_secret", youkuConfig.getApiSecret()),
                new HttpParameter("refresh_token", refreshToken)};
        HttpResult result = new HttpClientManager().get("https://openapi.youku.com/v2/oauth2/token", params);
        if (result.getReponseCode() == HttpClientManager.OK) {
            JSONObject json = JSONObject.fromObject(result.getResult());
            if (json.containsKey("access_token")) {
                try {
                    returnObj.setAccess_token(json.getString("access_token"));
                    returnObj.setExpires_in(json.getInt("expires_in"));
                    returnObj.setRefresh_token(json.getString("refresh_token"));
                    returnObj.setRefresh_token(json.getString("token_type"));
                } catch (Exception e) {
                    GAlerter.lan(this.getClass().getName() + " youku auth prase accesstoken from json error.", e);
                    return null;
                }
            } else {
                GAlerter.lan(this.getClass().getName() + " youku auth error." + result.getResult());
                return null;
            }
        } else {
            GAlerter.lab("youku getAccessToken,refreshToken=" + refreshToken);
        }
        return returnObj;
    }


    public static void main(String[] args) {

    }

}
