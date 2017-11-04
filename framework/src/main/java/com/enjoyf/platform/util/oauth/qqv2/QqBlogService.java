package com.enjoyf.platform.util.oauth.qqv2;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.QqV2Config;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-7-31
 * Time: 下午9:42
 * To change this template use File | Settings | File Templates.
 */
public class QqBlogService {
    //
    private static QqBlogService instance;

    public static QqBlogService get() {
        if (instance == null) {
            synchronized (QqBlogService.class) {
                if (instance == null) {
                    instance = new QqBlogService();
                }
            }
        }
        return instance;
    }

    private QqBlogService() {
    }

    public String addShare(String accessToken, String openid, String format, SyncContent syncContent)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        HttpParameter[] params = {
                new HttpParameter("oauth_consumer_key", QqV2Config.get().getClientId()),
                new HttpParameter("access_token", accessToken),
                new HttpParameter("oauth_version", "2.a"),
                new HttpParameter("openid", openid),
                new HttpParameter("format", format),
//                new HttpParameter("comment",  StringUtil.isEmpty(syncContent.getSyncTitle())?syncContent.getSyncText():syncContent.getSyncTitle()),
                new HttpParameter("summary", syncContent.getSyncText()),
                new HttpParameter("images", syncContent.getSyncImg()),

                //必须填写
                new HttpParameter("title", StringUtil.isEmpty(syncContent.getSyncTitle()) ? syncContent.getSyncText() : syncContent.getSyncTitle()),
                new HttpParameter("url", syncContent.getSyncContentUrl()),
                new HttpParameter("site", "着迷网"),
                new HttpParameter("fromurl", WebappConfig.get().getUrlWww()),
        };

        HttpResult result = new HttpClientManager().post(QqV2Config.get().getApiURL() + "share/add_share", params, null);

        JSONObject resultObj = (JSONObject) JSONValue.parse(result.getResult());
        return resultObj.toJSONString();
    }
}
