package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import com.enjoyf.platform.webapps.common.multimedia.VideoUtil;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-9-11
 * Time: 下午3:47
 * To change this template use File | Settings | File Templates.
 */
public class Open56Fetcher implements VideoFetcher {

    private final static String appKey = "3000000442";
    private final static String secret = "b440cc9f86fd7390";
    private final static String INTERFACE_URL = "http://oapi.56.com/video/getVideoInfo.json";

    Pattern V_PATTERN = Pattern.compile("v_([^./_]+)");
    Pattern VID_PATTERN = Pattern.compile("vid-([^./_]+)");

    public Open56Fetcher() {
    }

    @Override
    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        /**
         *视频ID标识，可以是数字 ‘68993948’ ，也可以是字符串，‘Njc5NDI3MTU’，同时支持混合用逗号隔开，批量查询，如 69031287,68999495,Njc5NDI3MTU,68993948,Njc5NDI3MTU
         */
        List<Map<String, String>> resultList = RegexUtil.fetch(url, V_PATTERN, null);
        if (CollectionUtil.isEmpty(resultList) || !resultList.get(0).containsKey("1")) {
            resultList = RegexUtil.fetch(url, VID_PATTERN, null);
        }

        String vid = "";
        if (!CollectionUtil.isEmpty(resultList) && resultList.get(0).containsKey("1")) {
            vid = resultList.get(0).get("1");
        }


//        String vid = url.substring(url.lastIndexOf("_") + 1, url.lastIndexOf(".html"));
//        int vidStartIndx = vid.indexOf("vid-");
//        if (vid.contains("vid-")) {
//            vid =vid.substring(vidStartIndx+"vid-".length());
//        }

        Map<String, String> params = new HashMap<String, String>();
        String ts = genericTs();

        params.put("vid", vid);
        params.put("sign", genericSign(vid, ts));
        params.put("appkey", appKey);
        params.put("ts", ts);

        String getVideoURL = new StringBuilder().append(INTERFACE_URL).append("?").append(map2String(params)).toString();

        String jsonStr = VideoUtil.httpClient(getVideoURL);
        JSONObject jo = new JSONObject(jsonStr);

        if (jo.getInt("code") != 0) {
            FiveSixFetcher fetcher = new FiveSixFetcher();

            return fetcher.fetchByUrl(url, null);
        }

        Video video = new Video();

        JSONObject data56 = jo.getJSONObject("0");
        video.setPic(data56.getString("mimg"));
        video.setBigpic(data56.getString("bimg"));
        video.setFlash(data56.getString("swf"));
        String totaltime = data56.getString("totaltime");
        if (!StringUtil.isEmpty(totaltime)) {
            video.setTime(DateUtil.cSecond2HHmmss(Long.valueOf(totaltime) / 1000));
        }
        video.setDescription(data56.getString("desc"));
        video.setTag(data56.getString("tag"));
        video.setOrgUrl(url);

        return video;
    }

    /**
     * 56接口要求要有sign参数 ，见http://dev.56.com/wiki/index.php?doc-view-54
     *
     * @param vid
     * @return
     */
    private String genericSign(String vid, String ts) {
        //第一次MD5加密
        String vidMd5 = Md5Utils.md5("vid=" + vid);

        //第二次MD5加密
        return Md5Utils.md5(vidMd5 + "#" + appKey + "#" + secret + "#" + ts);
    }

    /**
     * 56接口要求要有ts参数 ，见http://dev.56.com/wiki/index.php?doc-view-54
     *
     * @return
     */
    private String genericTs() {

        //获取时间戳
        return String.valueOf(System.currentTimeMillis()).substring(0, 10);
    }

    private String map2String(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        String returnValue = sb.toString();

        return returnValue.substring(0, returnValue.length() - 1);
    }


    public static void main(String[] args) {
        Open56Fetcher a56 = new Open56Fetcher();

        try {
            JSONObject jo = new JSONObject("{}");
            System.out.println(jo.getInt("code"));
            //http://www.56.com/w22/play_album-aid-10133106_vid-NzI3NjgxNTY.html
            System.out.println("s" + a56.fetchByUrl("http://www.56.com/w94/play_album-aid-10231256_vid-NzI4NDQ4NDI.html", null));
            System.out.println("s" + a56.fetchByUrl("http://www.56.com/u36/v_NzM1OTMwMTc.html/sdfasdfasdfasdfasdfasdfasdfas ", null));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
