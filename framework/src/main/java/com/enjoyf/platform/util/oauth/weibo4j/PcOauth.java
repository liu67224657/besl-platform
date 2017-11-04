package com.enjoyf.platform.util.oauth.weibo4j;

import com.enjoyf.platform.props.PcSinaWeiboConfig;
import com.enjoyf.platform.util.oauth.weibo4j.http.AccessToken;
import com.enjoyf.platform.util.oauth.weibo4j.http.BASE64Encoder;
import com.enjoyf.platform.util.oauth.weibo4j.model.PostParameter;
import com.enjoyf.platform.util.oauth.weibo4j.model.WeiboException;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONException;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PcOauth {
    // ----------------------------针对站内应用处理SignedRequest获取accesstoken----------------------------------------
    public String access_token;
    public String user_id;

    public String getToken() {
        return access_token;
    }

    /*
      * 解析站内应用post的SignedRequest split为part1和part2两部分
      */
    public String parseSignedRequest(String signed_request) throws IOException,
            InvalidKeyException, NoSuchAlgorithmException {
        String[] t = signed_request.split("\\.", 2);
        // 为了和 url encode/decode 不冲突，base64url 编码方式会将
        // '+'，'/'转换成'-'，'_'，并且去掉结尾的'='。 因此解码之前需要还原到默认的base64编码，结尾的'='可以用以下算法还原
        int padding = (4 - t[0].length() % 4);
        for (int i = 0; i < padding; i++)
            t[0] += "=";
        String part1 = t[0].replace("-", "+").replace("_", "/");

        SecretKey key = new SecretKeySpec(PcSinaWeiboConfig.get().getClientSercret().getBytes(), "hmacSHA256");
        Mac m;
        m = Mac.getInstance("hmacSHA256");
        m.init(key);
        m.update(t[1].getBytes());
        String part1Expect = BASE64Encoder.encode(m.doFinal());

        Base64 decode = new Base64();
        String s = new String(decode.decode(t[1]));
        if (part1.equals(part1Expect)) {
            return ts(s);
        } else {
            return null;
        }
    }

    /*
      * 处理解析后的json解析
      */
    public String ts(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            access_token = jsonObject.getString("oauth_token");
            user_id = jsonObject.getString("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return access_token;

    }

    /*----------------------------Oauth接口--------------------------------------*/

    public AccessToken getAccessTokenByCode(String code) throws WeiboException {
        return new AccessToken(Weibo.client.post(PcSinaWeiboConfig.get().getAccessTokenUri(),
                new PostParameter[]{
                        new PostParameter("client_id", PcSinaWeiboConfig.get().getClientID()),
                        new PostParameter("client_secret", PcSinaWeiboConfig.get().getClientSercret()),
                        new PostParameter("grant_type", "authorization_code"),
                        new PostParameter("code", code),
                        new PostParameter("redirect_uri", PcSinaWeiboConfig.get().getRedirectUri())}, false));
    }

    public AccessToken refreshAccessToken(String refreshToken) throws WeiboException {
        return new AccessToken(Weibo.client.post(PcSinaWeiboConfig.get().getAccessTokenUri(),
                new PostParameter[]{
                        new PostParameter("client_id", PcSinaWeiboConfig.get().getClientID()),
                        new PostParameter("client_secret", PcSinaWeiboConfig.get().getClientSercret()),
                        new PostParameter("grant_type", "refresh_token"),
                        new PostParameter("refresh_token", refreshToken),
                        new PostParameter("redirect_uri", PcSinaWeiboConfig.get().getRedirectUri())}, false));
    }


    public String authorize(String response_type, String redirectUri) throws WeiboException {
        return PcSinaWeiboConfig.get().getAuthorizeUri().trim() + "?client_id="
                + PcSinaWeiboConfig.get().getClientID().trim() + "&redirect_uri="
                + redirectUri
                + "&response_type=" + response_type;
    }
}
