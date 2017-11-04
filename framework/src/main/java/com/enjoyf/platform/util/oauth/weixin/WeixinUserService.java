package com.enjoyf.platform.util.oauth.weixin;

import com.enjoyf.platform.props.WeixinConfig;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import net.sf.json.JSONObject;


/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/13
 * Description:
 */
public class WeixinUserService {

    private WeixinConfig weixinConfig = WeixinConfig.get();

    /**
     * 通过code得到WeiXinAuth对象里面包括了accesstoken等属性openId
     *
     * @param accessToken
     * @return
     */
    public WeixinUserInfo getUserInfo(String accessToken, String openId) {
        WeixinUserInfo returnObj = new WeixinUserInfo();
        HttpParameter[] params = {new HttpParameter("access_token", accessToken),
                new HttpParameter("openid", openId)};
        HttpResult result = new HttpClientManager().get("https://api.weixin.qq.com/sns/userinfo", params);
        if (result.getReponseCode() == HttpClientManager.OK) {
            /**
             * openid":"OPENID",
             "nickname":"NICKNAME",
             "sex":1,
             "province":"PROVINCE",
             "city":"CITY",
             "country":"COUNTRY",
             "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
             "privilege":[
             "PRIVILEGE1",
             "PRIVILEGE2"
             ],
             "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"

             *
             *
             */
            JSONObject json = JSONObject.fromObject(result.getResult());
            if (json.containsKey("openid")) {
                try {
                    returnObj.setOpenId(json.getString("openid"));
                    returnObj.setNickname(json.getString("nickname"));
                    returnObj.setSex(json.getInt("sex"));
                    returnObj.setProvince(json.getString("province"));
                    returnObj.setCity(json.getString("city"));

                    returnObj.setCountry(json.getString("country"));
                    returnObj.setHeadimgurl(json.getString("headimgurl"));

                    if(json.containsKey("privilege")){
                        Object [] objs=json.getJSONArray("privilege").toArray();
                        String [] strs=new String[objs.length];
                        for(int i=0;i<objs.length;i++){
                            strs[i]=objs[i].toString();
                        }
                        returnObj.setPrivilege(strs);
                    }
//                    returnObj.setUnionid(json.getString("unionid"));
                } catch (Exception e) {
                    GAlerter.lan(this.getClass().getName() + " weixin auth prase accesstoken from json error.", e);
                    return null;
                }
            } else {
                GAlerter.lan(this.getClass().getName() + " weixin auth error." + result.getResult());
                return null;
            }
        } else {
            GAlerter.lab("weixin getUserInfo,accessToken=" + accessToken + ",openId=" + openId + ",response result=" + result.getResult());
        }
        return returnObj;
    }
}
