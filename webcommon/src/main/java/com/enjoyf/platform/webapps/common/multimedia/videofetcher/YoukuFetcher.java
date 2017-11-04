package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import com.enjoyf.platform.webapps.common.multimedia.VideoUtil;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class YoukuFetcher implements VideoFetcher {

    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        Document doc = VideoUtil.getURLContentPost(url);

        /**
         * 获取视频缩略图
         */
        String pic = VideoUtil.getElementAttrById(doc, "s_msn1", "href");
        int local = pic.indexOf("screenshot=");
        pic = pic.substring(local + 11);

        /**
         * 获取视频地址
         */
        String flash = VideoUtil.getElementAttrById(doc, "link2", "value");

        /**
         * 获取视频时间
         */

        String time = "";
        Elements elements = doc.select("li[class=current]>div[class=show_time]>span[class=num]");
        if(!CollectionUtil.isEmpty(elements)){
            for(Element element : elements){
                time = element.html();
            }
        }

        String desc = doc.select("meta[name=title]").attr("content");

        Video video = new Video();
        video.setPic(pic);
        video.setFlash(flash);
        video.setOrgUrl(url);
        video.setTime(time);
        video.setDescription(desc);

        return video;
    }
}
