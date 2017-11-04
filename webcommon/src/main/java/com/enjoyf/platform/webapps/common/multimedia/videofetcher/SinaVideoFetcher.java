package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import com.enjoyf.platform.webapps.common.multimedia.VideoUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class SinaVideoFetcher implements VideoFetcher {
    private final static Pattern PATTERN_HEADER_JS_OBJ = Pattern.compile("video\\s*:\\s*(\\{[^;()]+\\})");
    private final static Pattern PATTERN_NULL_CHAR = Pattern.compile("\\s+");
    private final static Pattern PATTERN_HEADER_JS_OBJ_2 = Pattern.compile("SCOPE\\['video'\\]\\s*=\\s*(\\{[^;()]+\\})");

    @Override
    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        Document doc = VideoUtil.getURLContentGet(url);
        Video video = null;

        Pattern pattern = Pattern.compile("#([0-9]+)");
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            Elements elements = doc.select("li[vid=" + matcher.group(1) + "]>span[class=title]>a");
            if (!CollectionUtil.isEmpty(elements)) {
                String childUrl = null;
                for (Element element : elements) {
                    childUrl = element.attr("href").toString();
                }
                video = getByRegex(VideoUtil.getURLContentPost(childUrl).html());
            }
        } else {
            video = getByRegex(doc.html());

            if(video==null){
                video=getByRegex2(doc.html());
            }
        }

        return video;
    }

    private static Video getByRegex(String html) throws JSONException {

        Matcher m = PATTERN_HEADER_JS_OBJ.matcher(html);
        if (m.find()) {
            String jsonStr = m.group(1);
            jsonStr = PATTERN_NULL_CHAR.matcher(jsonStr).replaceAll("");


            JSONObject jo = new JSONObject(jsonStr);

            String imgSrc = String.valueOf(jo.get("pic"));
            imgSrc = StringUtil.isEmpty(imgSrc) ? VideoUtil.DEFAULT_VIDEO_PIC : imgSrc;
            imgSrc = imgSrc.replace("\\", "");

            String flashUrl = String.valueOf(jo.get("swfOutsideUrl"));
            String title = String.valueOf(jo.get("title"));

            if (StringUtil.isEmpty(flashUrl)) {
                return null;
            }

            Video video = new Video();
            video.setPic(imgSrc);
            video.setDescription(title);
            video.setFlash(flashUrl);
            video.setTime("");
            return video;

        } else {
            return null;
        }
    }


    private static Video getByRegex2(String html) throws JSONException {

        Matcher m = PATTERN_HEADER_JS_OBJ_2.matcher(html);
        if (m.find()) {
            String jsonStr = m.group(1);
            jsonStr = PATTERN_NULL_CHAR.matcher(jsonStr).replaceAll("");


            JSONObject jo = new JSONObject(jsonStr);

            String imgSrc = String.valueOf(jo.get("pic"));
            imgSrc = StringUtil.isEmpty(imgSrc) ? VideoUtil.DEFAULT_VIDEO_PIC : imgSrc;
            imgSrc = imgSrc.replace("\\", "");

            String flashUrl = String.valueOf(jo.get("swfOutsideUrl"));
            String title = String.valueOf(jo.get("title"));

            if (StringUtil.isEmpty(flashUrl)) {
                return null;
            }

            Video video = new Video();
            video.setPic(imgSrc);
            video.setDescription(title);
            video.setFlash(flashUrl);
            video.setTime("");
            return video;

        } else {
            return null;
        }
    }

}
