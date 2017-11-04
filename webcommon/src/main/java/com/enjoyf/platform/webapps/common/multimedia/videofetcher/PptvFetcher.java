package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONException;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import com.enjoyf.platform.webapps.common.multimedia.VideoUtil;
import org.jsoup.nodes.Document;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-4-10
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
public class PptvFetcher implements VideoFetcher {
    private final static String PPTV_SWF_PREFIX = "http://player.pptv.com/v/";
    private final static Pattern FLASH_OBJECT_PPTV_PATTERN = Pattern.compile("var\\s+webcfg\\s*=\\s*(\\{.*\\});?");

    @Override
    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        String flashId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        String apiUrl = PPTV_SWF_PREFIX + flashId + ".swf";
        Document doc = VideoUtil.getURLContentPost(url);
        String content = doc.html();

        Video video = getByRegex(content);
        video.setFlash(apiUrl);
        video.setOrgUrl(url);
        video.setTime("");
        return video;
    }

    private static Video getByRegex(String html) throws JSONException {

        Matcher m = FLASH_OBJECT_PPTV_PATTERN.matcher(html);
        if (m.find()) {
            String jsonStr = m.group(1);
            JSONObject jo = new JSONObject(jsonStr);


            Video video = new Video();
            String imgSrc = jo.getString("cover").replace("\\", "");
            imgSrc = StringUtil.isEmpty(imgSrc) ? VideoUtil.DEFAULT_VIDEO_PIC : imgSrc;
            video.setPic(imgSrc);
            video.setDescription(jo.getString("title"));
            return video;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
       PptvFetcher fetcher=new PptvFetcher();
        try {
            System.out.println(fetcher.fetchByUrl("http://v.pptv.com/show/RzkKiaPBWxgRn5U0.html", null));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
