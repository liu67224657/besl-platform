package com.enjoyf.platform.util.oauth.qqv2;

import com.enjoyf.platform.props.hotdeploy.QqV2Config;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.UpdateExpress;
import net.sf.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class QqV2Oauth {

    private String clientId;
    private String clientSecr;

    Pattern pattern = Pattern.compile("\"openid\":\"([^\"]+)");

    public QqV2Oauth(String clientId, String clientSecr) {
        this.clientId = clientId;
        this.clientSecr = clientSecr;
    }

    public String getAuthorizeUrl(String redirectUrl) {
        return QqV2Config.get().getAuthorizeUri() + "?client_id=" +
                clientId + "&response_type=code&state=joyme&redirect_uri=" + redirectUrl + "&scope=get_user_info,add_share,get_other_info,get_fanslist,get_idollist,add_idol,del_idol,add_t,del_t,add_pic_t";
    }

    public QqV2AuthToken getAccessToken(String code, String redirectUrl) throws JSONException {
        QqV2AuthToken returnObj = new QqV2AuthToken();

        HttpParameter[] params = {new HttpParameter("grant_type", "authorization_code"),
                new HttpParameter("client_id", clientId),
                new HttpParameter("client_secret", clientSecr),
                new HttpParameter("redirect_uri", redirectUrl),
                new HttpParameter("code", code)};
        HttpResult result = new HttpClientManager().post(com.enjoyf.platform.props.hotdeploy.QqV2Config.get().getAccessTokenUri(), params, new HttpParameter[]{});
        if (result.getReponseCode() == HttpClientManager.OK) {
            String[] returnResponse = result.getResult().split("&");

            if (returnResponse.length == 0) {
                GAlerter.lan("qqv2 oauth returnResponse error.length is null");
                return null;
            }

            Map<String, String> mapString = new HashMap<String, String>();
            for (String responseParam : returnResponse) {
                String[] returnResponseEntry = responseParam.split("=");

                if (returnResponseEntry.length != 2) {
                    GAlerter.lab("GAlerter lab qqv2 oauth returnResponseEntry split error.client_id=" + clientId + ",client_secret=" + clientSecr + "," +
                            "redirect_uri=" + redirectUrl + ",code=" + code + ",url=" + com.enjoyf.platform.props.hotdeploy.QqV2Config.get().getAccessTokenUri() + "," +
                            "result=" + result.getResult());
                    GAlerter.lan("GAlerter lan qqv2 oauth returnResponseEntry split error.responseParam:" + responseParam);
                    return null;
                }

                mapString.put(returnResponseEntry[0], returnResponseEntry[1]);
            }
            String access_token = mapString.get("access_token");
            if (StringUtil.isEmpty(access_token)) {
                return null;
            }

            QqV2UnionidOpenid qqV2Unionid = getOpenId(access_token);
            if (qqV2Unionid == null) {
                return null;
            }
            
            QqV2OpenId2UnionId.openId2UnionId(qqV2Unionid.getOpenid(),qqV2Unionid.getUnioid());

            returnObj.setAccessToken(access_token);
            returnObj.setOpenid(qqV2Unionid.getOpenid());
            returnObj.setUnionId(qqV2Unionid.getUnioid());
            returnObj.setExpires(Long.parseLong(mapString.get("expires_in")));
            returnObj.setRefreshToken(mapString.get("refresh_token"));
            returnObj.setName(mapString.get("name"));
        } else {
            GAlerter.lab("QqV2Oauth getAccessToken,code=" + code + ",redirectUrl=" + redirectUrl + ",responsecode=" + result.getReponseCode() + ",response result=" + result.getResult());
        }
        return returnObj;
    }

    //callback( {"client_id":"100292505","openid":"A996ABC92BCD6C7D518AD095858C184E"} );
    private QqV2UnionidOpenid getOpenId(String accessToken) {
//        HttpParameter[] params = {new HttpParameter("access_token", accessToken)};
//        HttpResult result = new HttpClientManager().post(com.enjoyf.platform.props.hotdeploy.QqV2Config.get().getApiURL() + "oauth2.0/me", params, new HttpParameter[]{});
        HttpResult result = new HttpClientManager().get(QqV2Config.get().getApiURL() + "oauth2.0/me" +
                "?access_token=" + accessToken + "&unionid=1&client_id=" + clientId, null, "utf-8");

        QqV2UnionidOpenid qqV2Unionid = null;
        if (result.getReponseCode() == HttpClientManager.OK) {
            // Matcher matcher = pattern.matcher(result.getResult());


            String resultStr = result.getResult();
            //todo get unionId
            if (!StringUtil.isEmpty(resultStr) && resultStr.contains("client_id")) {
                qqV2Unionid = new QqV2UnionidOpenid();
                resultStr = resultStr.replaceAll("callback\\(", "");
                resultStr = resultStr.replaceAll("\\);", "");

                JSONObject jsonObject = JSONObject.fromObject(resultStr);
                qqV2Unionid.setClient_id(jsonObject.getString("client_id"));
                qqV2Unionid.setOpenid(jsonObject.getString("openid"));
                qqV2Unionid.setUnioid(jsonObject.getString("unionid"));

            }
        }
        return qqV2Unionid;
    }

    public QqV2AuthToken refreshToken(String refreshToken) throws JSONException {
        QqV2AuthToken returnObj = new QqV2AuthToken();

        HttpParameter[] params = {new HttpParameter("grant_type", "refresh_token"),
                new HttpParameter("client_id", clientId),
                new HttpParameter("client_secret", clientSecr),
                new HttpParameter("refresh_token", refreshToken)};
        HttpResult result = new HttpClientManager().post(com.enjoyf.platform.props.hotdeploy.QqV2Config.get().getAccessTokenUri(), params, new HttpParameter[]{});

        if (result.getReponseCode() == HttpClientManager.OK) {
            String[] returnResponse = result.getResult().split("&");
            Map<String, String> mapString = new HashMap<String, String>();
            for (String responseParam : returnResponse) {
                String[] returnResponseEntry = responseParam.split("=");
                mapString.put(returnResponseEntry[0], returnResponseEntry[1]);
            }

            returnObj.setAccessToken(mapString.get("access_token"));
            returnObj.setExpires(Long.parseLong(mapString.get("expires_in")));
            returnObj.setRefreshToken(mapString.get("refresh_token"));
            returnObj.setName(mapString.get("name"));
            returnObj.setOpenid(mapString.get("openid"));
        }
        return returnObj;
    }
}
