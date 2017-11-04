package com.enjoyf.platform.util.oauth.qqv2;

import com.enjoyf.platform.props.hotdeploy.QqV2Config;
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
public class QqV2UserService {
    private String clientId;

    public QqV2UserService(String clientId) {
        this.clientId = clientId;
    }

    public QqUserInfo getUserInfo(QqV2RequestAuthInfo authInfoQqV2, String format) {

        HttpParameter[] params = {
                new HttpParameter("oauth_consumer_key", clientId),
                new HttpParameter("access_token", authInfoQqV2.getAccessToken()),
                new HttpParameter("oauth_version", "2.a"),
                new HttpParameter("openid", authInfoQqV2.getOpenId()),
                new HttpParameter("format", format),
        };

//        String unionId = getUnionid(authInfoQqV2.getAccessToken(),clientId,authInfoQqV2.getOpenId());
//        if (unionId == null) {
//            return null;
//        }

        HttpResult result = new HttpClientManager().post(QqV2Config.get().getApiURL() + "user/get_user_info", params, null);
        JSONObject resultObj = (JSONObject) JSONValue.parse(result.getResult());
        Long ret = (Long) resultObj.get("ret");
        if (ret == 0) {
            QqUserInfo qqUserInfo = new QqUserInfo();
//            qqUserInfo.setUnionId(unionId);
            qqUserInfo.setNickname(String.valueOf(resultObj.get("nickname")));
            qqUserInfo.setFigureurl_1(String.valueOf(resultObj.get("figureurl_1")));
            qqUserInfo.setFigureurl_2(String.valueOf(resultObj.get("figureurl_2")));
            qqUserInfo.setFigureurl(String.valueOf(resultObj.get("figureurl")));
            qqUserInfo.setVip(Boolean.valueOf(String.valueOf(resultObj.get("vip"))));
            qqUserInfo.setLevel(Integer.parseInt(String.valueOf(resultObj.get("level"))));
            qqUserInfo.setYellowYearVip(Boolean.valueOf(String.valueOf(resultObj.get("is_yellow_year_vip"))));
            qqUserInfo.setFigureurl_qq_1(String.valueOf(resultObj.get("figureurl_qq_1")));
            qqUserInfo.setFigureurl_qq_2(String.valueOf(resultObj.get("figureurl_qq_2")));
            return qqUserInfo;
        }


        return null;
    }

//
//    public String getUnionid(String accesstoken,String clientId,String openid) {
//        //ttps://graph.qq.com/oauth2.0/me?access_token=ACCESSTOKEN&unionid=1
//        HttpResult result = new HttpClientManager().get(QqV2Config.get().getApiURL() + "oauth2.0/me" +
//                "?access_token=" + accesstoken + "&unionid=1&client_id="+clientId+"&openid="+openid, null, "utf-8");
//
//        JSONObject resultObj = (JSONObject) JSONValue.parse(result.getResult().replace("callback(", "").replace(");", "").replace("\n",""));
//        try {
//            return String.valueOf(resultObj.get("unionid"));
//        } catch (Exception e) {
//            return null;
//        }
//    }

}
