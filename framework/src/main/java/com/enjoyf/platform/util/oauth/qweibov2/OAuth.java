package com.enjoyf.platform.util.oauth.qweibov2;

import com.enjoyf.platform.props.QWeiboAuthV2Config;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class OAuth {

    private String clientId;
    private String clientSecr;

    public OAuth(String clientId, String clientSecr) {
        this.clientId = clientId;
        this.clientSecr = clientSecr;
    }

    //https://open.t.qq.com/cgi-bin/oauth2/authorize
    public String getAuthorizeUrl(String redirectUrl) {
        try {
            return QWeiboAuthV2Config.get().getAuthorizeUri() + "?client_id=" +
                    clientId + "&response_type=code&redirect_uri=" + URLEncoder.encode(redirectUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); //todo not occured error.
        }
        return null;
    }

    public QweiboAuthToken getAccessToken(String code, String redirectUrl) throws JSONException {
        QweiboAuthToken returnObj = new QweiboAuthToken();

        String url= null;
        try {
            url = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  
        }
        HttpParameter[] params = {new HttpParameter("grant_type", "authorization_code"),
                new HttpParameter("client_id", clientId),
                new HttpParameter("client_secret", clientSecr),
                new HttpParameter("redirect_uri", url),
                new HttpParameter("code", code)};
        HttpResult result = new HttpClientManager().post(QWeiboAuthV2Config.get().getAccessTokenUri(), params, new HttpParameter[]{});

        if (result.getReponseCode() == HttpClientManager.OK) {
            String[] returnResponse = result.getResult().split("&");
            Map<String, String> mapString = new HashMap<String, String>();
            for (String responseParam : returnResponse) {
                String[] returnResponseEntry = responseParam.split("=");
                if(returnResponseEntry!=null && returnResponseEntry.length==2){
                       mapString.put(returnResponseEntry[0], returnResponseEntry[1]);
                }
            }

            returnObj.setAccessToken(mapString.get("access_token"));
            try {
                returnObj.setExpires(Long.parseLong(mapString.get("expires_in")));
            } catch (NumberFormatException e) {
                GAlerter.lan(this.getClass().getName() + " occured NumberFormatException.expires_in:" + mapString.get("expires_in") + " e,", e);
            }
            returnObj.setRefreshToken(mapString.get("refresh_token"));
            returnObj.setName(mapString.get("name"));
            returnObj.setOpenId(mapString.get("openid"));

        }else{
			GAlerter.lab("QweiboAuthToken getAccessToken,code=" + code + ",redirectUrl=" + redirectUrl + ",responsecode=" + result.getReponseCode() + ",response result=" + result.getResult());
		}
        return returnObj;
    }


     public QweiboAuthToken refreshToken(String refreshToken) throws JSONException {
        QweiboAuthToken returnObj = new QweiboAuthToken();

        HttpParameter[] params = {new HttpParameter("grant_type", "refresh_token"),
                new HttpParameter("client_id", clientId),
                new HttpParameter("client_secret", clientSecr),
                new HttpParameter("refresh_token", refreshToken)};
        HttpResult result = new HttpClientManager().post(QWeiboAuthV2Config.get().getAccessTokenUri(), params, new HttpParameter[]{});

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
        }
        return returnObj;
    }
}
