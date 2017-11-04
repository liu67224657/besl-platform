package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import com.enjoyf.platform.webapps.common.multimedia.VideoUtil;

import java.util.Map;

/**
 * <p/>
 * Description:得到youku在线播放信息
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class Ku6Fetcher implements VideoFetcher {
    private final static String KU6_API_URL = "http://v.ku6.com/fetch.htm?t=getVideo4Player&vid=";

    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        String flashId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        String apiUrl = KU6_API_URL + flashId;
        String reString = VideoUtil.httpClient(apiUrl);
        JSONObject jo = new JSONObject(reString);

        if (jo.getInt("status") != 1) {
            return null;
        }

        Video video = new Video();
        JSONObject ku6Data = jo.getJSONObject("data");
        video.setPic(ku6Data.getString("picpath"));
        video.setBigpic(ku6Data.getString("bigpicpath"));
        video.setFlash("http://player.ku6.com/refer/" + flashId + "/v.swf");
        video.setTime(DateUtil.cSecond2HHmmss(Long.valueOf(ku6Data.getString("vtime"))));
        video.setDescription(ku6Data.getString("t"));
        video.setTag(ku6Data.getString("tag"));
        video.setOrgUrl(url);

        return video;
    }

}
