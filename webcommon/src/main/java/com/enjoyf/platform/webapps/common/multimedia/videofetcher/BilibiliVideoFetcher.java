package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import com.enjoyf.platform.webapps.common.multimedia.VideoUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-9-24
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */
public class BilibiliVideoFetcher implements VideoFetcher {
    /**
     * Public-Key: 421b8842a5e101fec3953d1f84c9d354

     App-Key: c8a7ded127f939a8

     App-Secret: 3ea3c789b41245c40fb926135023647b
     */
    /**
     * 哔哩哔哩获取视频信息API
     * 其中包括参数有：
     * type  ——————————————类型，可选xml/json
     * appkey  ————————————输入获得的AppKey
     * id      ————————————aid号
     * page   —————————————page号
     */
    private static final String BILIBILI_API_URL = "http://api.bilibili.tv/view";

    @Override
    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        Pattern suffix = Pattern.compile("av([0-9]+)");
        Matcher m = suffix.matcher(url);
        String id = null;
        if (m.find()) {
            id = m.group(1);

//            String param = "&appkey=c8a7ded127f939a8&type=json&id=" + id + "&page=1&3ea3c789b41245c40fb926135023647b";
//            System.out.println(MD5Util.Md5(param));
            StringBuilder sb = new StringBuilder();
            sb.append(BILIBILI_API_URL).append("?appkey=c8a7ded127f939a8&type=json&page=1&id=").append(id);


            String jsonStr = VideoUtil.httpClient(sb.toString());
            JSONObject jo = new JSONObject(jsonStr);

            Video video = new Video();
            video.setBigpic(jo.getString("pic"));
            video.setDescription(jo.getString("title"));
            video.setPic(jo.getString("pic"));
            if (StringUtil.isEmpty(jo.getString("offsite"))) {
                throw new Exception();
            } else {
                video.setFlash(jo.getString("offsite"));
            }

            video.setOrgUrl(url);
            video.setTag(jo.getString("tag"));

            return video;
        } else {

            return null;
        }

    }

    public static void main(String[] args) {
        BilibiliVideoFetcher b = new BilibiliVideoFetcher();
        try {
            b.fetchByUrl("http://bilibili.kankanews.com/video/av38060/", null);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String param = "&appkey=c8a7ded127f939a8&api=http%3A%2F%2Fwww.joyme.com%2F&3ea3c789b41245c40fb926135023647b";
        System.out.println(MD5Util.Md5(param));
    }
}
