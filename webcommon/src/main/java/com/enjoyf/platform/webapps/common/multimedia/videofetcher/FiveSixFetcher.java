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
 * <p/>
 * Description:得到youku在线播放信息
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class FiveSixFetcher implements VideoFetcher {
    private final static Pattern FLASH_OBJECT_56_PATTERN = Pattern.compile("var\\s+_oFlv_c\\s*=\\s*(\\{[^}]*\\});?");

    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        Document doc = VideoUtil.getURLContentPost(url);
        String content = doc.html();

        /**
         * 获取视频地址
         */
        String urlParam = url.substring(url.lastIndexOf("/"), url.lastIndexOf(".html"));
        int vidStartIndx = urlParam.indexOf("vid-");
        if (vidStartIndx > 0) {
            urlParam ="/v_"+urlParam.substring(vidStartIndx+"vid-".length());
        }

        String flash = "http://player.56.com"+ urlParam + ".swf";

        Video video = getByRegex(content);
        if (video != null) {
            video.setFlash(flash);
            video.setOrgUrl(url);
            video.setTime("");
        }

        return video;
    }

    private static Video getByRegex(String html) throws JSONException {

        Matcher m = FLASH_OBJECT_56_PATTERN.matcher(html);
        if (m.find()) {
            String jsonStr = m.group(1);
            JSONObject jo = new JSONObject(jsonStr);


            Video video = new Video();
            String imgSrc = jo.getString("img").replace("\\", "");
            imgSrc = StringUtil.isEmpty(imgSrc) ? VideoUtil.DEFAULT_VIDEO_PIC : imgSrc;
            video.setPic(imgSrc);
            video.setDescription(jo.getString("Subject"));
            video.setTag(jo.getString("tag"));
            return video;
        } else {
            return null;
        }
    }
}
