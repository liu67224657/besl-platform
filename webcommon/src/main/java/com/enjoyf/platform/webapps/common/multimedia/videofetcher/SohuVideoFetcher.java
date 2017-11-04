package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONArray;
import com.enjoyf.platform.util.oauth.weibo4j.org.json.JSONObject;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-9-13
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public class SohuVideoFetcher implements VideoFetcher {
    /**
     * http://share.vrs.sohu.com/Video_Share.action?code=true&url=http://my.tv.sohu.com/u/vw/10863383?xuid=u13773057
     * 其中code=true返回的数据中包含网页内嵌代码；为false或不写则不包含
     * url表示待解析的url
     */
    private static final String API_VIDEO_SHARE = "http://share.vrs.sohu.com/Video_Share.action";
    /**
     *  根据视频专辑ID获取UGC视频列表
     *  playlistId：视频专辑ID
     */
    private static final String API_VIDEO_LIST = "http://my.tv.sohu.com/user/a/playlist/getvideos.do";

    private Document document;

    @Override
    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        String videoUrl = null;

        Pattern pattern = Pattern.compile("/u/pw/([0-9]+)_([0-9]+)_([0-9]+)");
        Matcher m = pattern.matcher(url);
        if(m.find()){
            //第三个括号里的内容
            String playListId = m.group(1);
            videoUrl = API_VIDEO_LIST + "?playlistId=" + playListId;

            return getVideoByListId(videoUrl, url, Integer.valueOf(m.group(3)));
        } else {
            videoUrl = API_VIDEO_SHARE + "?url=" + url;

            return getVideoByVideoId(videoUrl);
        }

    }

    private Video getVideoByVideoId(String url) throws Exception{
        HttpClient client = new HttpClient();
        HttpMethod get = new GetMethod(url);

        client.executeMethod(get);

        Video video = new Video();

        this.document = getDocument(get.getResponseBodyAsStream());

        Element element = (Element)document.getElementsByTagName("result").item(0);
        if(!"0".equals(element.getAttribute("type"))){
            return null;
        }

        video.setPic(getValueFromXML("coverurl"));
        video.setBigpic(getValueFromXML("smallcover"));
        video.setFlash(getValueFromXML("flash"));
        video.setDescription(getValueFromXML("title"));
        video.setOrgUrl(url);

        return video;
    }

    private Video getVideoByListId(String actionUrl, String url, int th) throws Exception {
        HttpClient client = new HttpClient();
        HttpMethod get = new GetMethod(actionUrl);

        client.executeMethod(get);

        JSONObject jo = new JSONObject(get.getResponseBodyAsString());

        if(jo.getInt("status") == 1){
            Video video = new Video();
            JSONObject data = jo.getJSONObject("data");
            JSONArray jsonArray = (JSONArray)data.opt("list");
            if(th <= data.getInt("sum")){
                JSONObject videoData = (JSONObject)jsonArray.opt(th-1);
                video.setBigpic(videoData.getString("smallPic"));
                video.setPic(videoData.getString("smallPic"));
                video.setDescription(videoData.getString("title"));
                video.setOrgUrl(url);
                //http://share.vrs.sohu.com/{videoId}/v.swf
//                video.setFlash("http://share.vrs.sohu.com/" + videoData.getString("videoId") + "/v.swf&api_key=" + "56136583e464f92595c6460289027c58");
                video.setFlash("http://share.vrs.sohu.com/my/v.swf&id=" + videoData.getString("videoId"));
                video.setTime(DateUtil.cSecond2HHmmss(Long.valueOf(videoData.getString("videoLength"))));

                return video;
            }
        }

        return null;

    }

    private Document getDocument(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        inputStream.close();
        return doc;
    }

    private String getValueFromXML(String nodeName) throws Exception {

        NodeList result = document.getElementsByTagName(nodeName);
        StringBuilder sb = new StringBuilder();

        for(int i=0; i < result.getLength(); i++){
            Node node = result.item(i);

            NodeList childNodes = node.getChildNodes();

            for(int j=0;j<childNodes.getLength();j++){
                sb.append(childNodes.item(j).getNodeValue());
            }

        }

        return sb.toString().trim();
    }

    public static void main(String[] args) {

        SohuVideoFetcher sohu = new SohuVideoFetcher();
        try {
            Video v = sohu.fetchByUrl("http://my.tv.sohu.com/u/pw/5069691_1_5",null);
            System.out.println(";;;;" + v);
//
//            VideoFetcherSngl.get().getVideoByUrl("http://tv.sohu.com/20120918/n353473775.shtml",null);
//            VideoFetcherSngl.get().getVideoByUrl("http://tv.sohu.com/20120918/n353473775.shtml",null);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
