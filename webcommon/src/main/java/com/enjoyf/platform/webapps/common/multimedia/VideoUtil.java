/**
 *
 */
package com.enjoyf.platform.webapps.common.multimedia;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONException;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author xinzhao
 */
public class VideoUtil {
    public final static String DEFAULT_VIDEO_PIC = WebappConfig.get().getUrlLib() + "/static/theme/default/img/video_default.jpg";


    /**
     * 获取script某个变量的值
     *
     * @param name 变量名称
     * @return 返回获取的值
     */
    public static String getScriptVarByName(String name, String content) {
        String script = content;

        int begin = script.indexOf(name);

        script = script.substring(begin + name.length() + 2);

        int end = script.indexOf(",");

        script = script.substring(0, end);

        String result = script.replaceAll("'", "");
        result = result.trim();

        return result;
    }

    /**
     * 根据HTML的ID键及属于名，获取属于值
     *
     * @param id       HTML的ID键
     * @param attrName 属于名
     * @return 返回属性值为空则返回""空字符串
     */
    public static String getElementAttrById(Document doc, String id,
                                            String attrName) throws Exception {
        Element et = doc.getElementById(id);

        String attrValue = et != null ? et.attr(attrName) : "";

        return attrValue;
    }

    /**
     * 获取网页的内容
     */
    public static Document getURLContentPost(String url) throws Exception {
        Document doc = Jsoup.connect(url).timeout(20000).post();
        return doc;
    }
    public static Document getURLContentGet(String url) throws Exception {
        Document doc = Jsoup.connect(url).timeout(20000).get();
        return doc;
    }
    public static String httpClient(String rurl) {
        URL url = null;
        String reStr = "";
        BufferedReader br = null;
        try {
            url = new URL(rurl);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setRequestMethod("GET");

            InputStream is = huc.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "utf-8"));

            String line = "";
            while ((line = br.readLine()) != null) {
                reStr += line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        }

        return reStr;
    }


    public static JSONObject getReJson(String apiurl, String key) throws JSONException {
        String reString = httpClient(apiurl + key);
        JSONObject jo = new JSONObject(reString);
        return jo;
    }

}
