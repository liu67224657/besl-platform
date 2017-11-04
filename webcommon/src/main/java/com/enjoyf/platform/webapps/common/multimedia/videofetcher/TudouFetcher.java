package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONException;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import com.enjoyf.platform.webapps.common.multimedia.VideoUtil;

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
public class TudouFetcher implements VideoFetcher {
    private final static String TU_DOU_APPKEY = "343599b58d858a7d";
    private final static String TU_DOU_APP_SECRET = "39c9d01228eba1f7b13e0e7c665efa32";

    private static final Pattern PATTERN_TUDOU_LISTPALY_DEFAULT_URL = Pattern.compile("/listplay/([^/.?]+)");
    private static final Pattern PATTERN_TUDOU_LISTPALY_SINGLE_URL = Pattern.compile("/listplay/([^/.?]+)/([^/.?]+)");
    private static final Pattern PATTERN_TUDOU_VIEW_URL = Pattern.compile("/view/([^/.?]+)");

    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {

        Matcher matcher = PATTERN_TUDOU_VIEW_URL.matcher(url);
        Video video = new Video();
        if (matcher.find()) {
            String key = matcher.group(1);
            video = fetchTudouByVideoId(url, key);
            return video;
        }

        Matcher matcherList = PATTERN_TUDOU_LISTPALY_SINGLE_URL.matcher(url);
        String key = "";
        if (matcherList.find()) {
            key = matcherList.group(2);
            video = fetchTudouByVideoId(url, key);
            return video;
        }

        matcherList = PATTERN_TUDOU_LISTPALY_DEFAULT_URL.matcher(url);
        if (matcherList.find()) {
            key = matcherList.group(1);
            video = fetchTudouByListId(url, key);
            return video;
        }

        return null;
    }

    private Video fetchTudouByVideoId(String url, String videoId) throws JSONException {
        Video video = new Video();
        String tudouViewApiUrl = "http://api.tudou.com/v3/gw?method=item.info.get&appKey=" + TU_DOU_APPKEY + "&format=json&itemCodes=";

        JSONObject resultByJson = VideoUtil.getReJson(tudouViewApiUrl, videoId);
        JSONObject viedeoJsonObj = resultByJson.getJSONObject("multiResult").getJSONArray("results").getJSONObject(0);

        video.setPic(viedeoJsonObj.getString("picUrl"));
        video.setFlash(viedeoJsonObj.getString("outerPlayerUrl"));
        video.setOrgUrl(url);
        video.setTime(DateUtil.cSecond2HHmmss(Long.valueOf(viedeoJsonObj.getString("totalTime")) / 1000));
        video.setDescription(viedeoJsonObj.getString("description"));
        video.setTag(viedeoJsonObj.getString("tags"));
        return video;
    }

    private Video fetchTudouByListId(String url, String listId) throws JSONException {
        Video video = new Video();

        String tudouPlayListApiurl = "http://api.tudou.com/v3/gw?method=playlist.info.get&appKey=" + TU_DOU_APPKEY + "&format=json&playlistCodes=";

        JSONObject resultByPlayList = VideoUtil.getReJson(tudouPlayListApiurl, listId);
        JSONObject videoJsonByPlayList = resultByPlayList.getJSONObject("multiResult").getJSONArray("results").getJSONObject(0);

        if (StringUtil.isEmpty(videoJsonByPlayList.getString("picUrl"))) {
            video.setPic(videoJsonByPlayList.getString("playlistPicUrl"));
        } else {
            video.setPic(videoJsonByPlayList.getString("picUrl"));
        }

        video.setFlash(videoJsonByPlayList.getString("outerPlayerUrl"));
        video.setOrgUrl(url);

        if (StringUtil.isEmpty(videoJsonByPlayList.getString("description"))) {
            video.setDescription(videoJsonByPlayList.getString("title"));
        } else {
            video.setDescription(videoJsonByPlayList.getString("description"));
        }

        if (!StringUtil.isEmpty(videoJsonByPlayList.getString("totalTime"))) {
            video.setTime(DateUtil.cSecond2HHmmss(Long.valueOf(videoJsonByPlayList.getString("totalTime")) / 1000));
        } else {
            video.setTime("");
        }

        video.setTag(videoJsonByPlayList.getString("tags"));

        return video;
    }

    public static void main(String[] args) {
        try {
            System.out.println(new TudouFetcher().fetchByUrl("http://www.tudou.com/listplay/_EWD17sq8Tc/0TBQukvIIIU.html",null));
            System.out.println(new TudouFetcher().fetchByUrl("http://www.tudou.com/listplay/_EWD17sq8Tc",null));
            System.out.println(new TudouFetcher().fetchByUrl("http://www.tudou.com/programs/view/oPfToBqwAu0?FR=LIAN",null));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
