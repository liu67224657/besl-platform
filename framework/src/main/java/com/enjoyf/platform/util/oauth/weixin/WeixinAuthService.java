package com.enjoyf.platform.util.oauth.weixin;

import com.enjoyf.platform.props.WeixinConfig;
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
public class WeixinAuthService {

    private WeixinConfig weixinConfig=WeixinConfig.get();

    public String getWeiXinAuthUrl(String redirectUrl){
      String stat= Md5Utils.md5(String.valueOf(System.currentTimeMillis()));
      String url="https://open.weixin.qq.com/connect/qrconnect?" +
              "appid="+weixinConfig.getApiKey()+"&redirect_uri="+redirectUrl+"&response_type=code&scope=snsapi_login&state="+stat+"#wechat_redirect";
      return url;
    }


    /**
     * 通过code得到WeiXinAuth对象里面包括了accesstoken等属性
     * @param code
     * @return
     */
    public WeiXinAuth getAccessToken(String code){
//       String getAccesssTokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+weixinConfig.getApiKey()+"" +
//               "&secret="+weixinConfig.getApiSecret()+"&code="+code+"&grant_type=authorization_code";

        WeiXinAuth returnObj = new WeiXinAuth();

        HttpParameter[] params = {new HttpParameter("grant_type", "authorization_code"),
                new HttpParameter("appid", weixinConfig.getApiKey()),
                new HttpParameter("secret", weixinConfig.getApiSecret()),
                new HttpParameter("code", code),
                new HttpParameter("grant_type", "authorization_code")};
        HttpResult result = new HttpClientManager().post("https://api.weixin.qq.com/sns/oauth2/access_token", params, new HttpParameter[]{});
        if (result.getReponseCode() == HttpClientManager.OK) {

            JSONObject json=JSONObject.fromObject(result.getResult());
            if(json.containsKey("access_token")){
                try {
                    returnObj.setAccess_token(json.getString("access_token"));
                    returnObj.setOpenid(json.getString("openid"));
                    returnObj.setExpires_in(json.getInt("expires_in"));
                    returnObj.setRefresh_token(json.getString("refresh_token"));
                    returnObj.setScope(json.getString("scope"));
                } catch (Exception e) {
                    GAlerter.lan(this.getClass().getName()+" weixin auth prase accesstoken from json error.", e);
                    return null;
                }
            }else{
                GAlerter.lan(this.getClass().getName()+" weixin auth error."+ result.getResult());
                return null;
            }
        } else {
            GAlerter.lab("weixin getAccessToken,code=" + code + ",responsecode=" + result.getReponseCode() + ",response result=" + result.getResult());
        }
        return returnObj;
    }

    /**
     * accesstoken过期后调用改方法获取新的refreshtoken
     * @param refreshToken
     * @return
     */
    public WeiXinAuth refreshToken(String refreshToken){
        WeiXinAuth returnObj = new WeiXinAuth();
        HttpParameter[] params = {new HttpParameter("grant_type", "refresh_token"),
                new HttpParameter("appid", weixinConfig.getApiKey()),
                new HttpParameter("refresh_token", refreshToken)};
        HttpResult result = new HttpClientManager().get("https://api.weixin.qq.com/sns/oauth2/refresh_token", params);
        if (result.getReponseCode() == HttpClientManager.OK) {
            JSONObject json=JSONObject.fromObject(result.getResult());
            if(json.containsKey("access_token")){
                try {
                    returnObj.setAccess_token(json.getString("access_token"));
                    returnObj.setOpenid(json.getString("openid"));
                    returnObj.setExpires_in(json.getInt("expires_in"));
                    returnObj.setRefresh_token(json.getString("refresh_token"));
                    returnObj.setScope(json.getString("scope"));
                } catch (Exception e) {
                    GAlerter.lan(this.getClass().getName()+" weixin auth prase accesstoken from json error.", e);
                    return null;
                }
            }else{
                GAlerter.lan(this.getClass().getName()+" weixin auth error."+ result.getResult());
                return null;
            }
        } else {
            GAlerter.lab("weixin getAccessToken,refreshToken="+refreshToken);
        }
        return returnObj;
    }

}
