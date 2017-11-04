package com.enjoyf.platform.util;

import com.enjoyf.platform.props.SinaWeiboConfig;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by zhimingli
 * Date: 2015/02/02
 * Time: 11:35
 * api:http://open.weibo.com/wiki/Short_url/shorten
 */
public class ShortUrlUtils {
    private static String SINA_SHORT_URL_API = "http://api.t.sina.com.cn/short_url/shorten.json";

    public static String getSinaURL(String url_long) {
        if (StringUtil.isEmpty(url_long)) {
            return url_long;
        }
        HttpResult httpResult = null;
        try {
            HttpParameter[] params = new HttpParameter[]{
                    new HttpParameter("source", SinaWeiboConfig.get().getClientID()),
                    new HttpParameter("url_long", url_long),
            };
            httpResult = new HttpClientManager().get(SINA_SHORT_URL_API, params);
        } catch (Exception e) {
            GAlerter.lab("ShortUrlUtils occured Exception.e, short url:httpResult" + httpResult);
            return url_long;
        }
        if (httpResult.getReponseCode() == 200) {
            JSONObject jsonObject = JSONObject.fromObject(JSONArray.fromObject(httpResult.getResult()).get(0));
            return jsonObject.get("url_short").toString();
        }
        return url_long;
    }

    public static void main(String[] args) {
        String s = getSinaURL("http://www.joyme.com/");
        System.out.println(s);
    }
}
