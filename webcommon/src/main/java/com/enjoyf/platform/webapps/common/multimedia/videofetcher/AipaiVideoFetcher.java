package com.enjoyf.platform.webapps.common.multimedia.videofetcher;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.webapps.common.multimedia.Video;
import com.enjoyf.platform.webapps.common.multimedia.VideoUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-10-15
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
public class AipaiVideoFetcher implements VideoFetcher {

    @Override
    public Video fetchByUrl(String url, Map<String, String> paramMap) throws Exception {
        Pattern regexSwfUrl = Pattern.compile("copy_flash\\s*=\\s*'(.*)'");
        Pattern regexMp4Url = Pattern.compile("asset_mUrl\\s*=\\s*'(.*)'");

        Document doc = VideoUtil.getURLContentGet(url);

        Matcher m = regexSwfUrl.matcher(doc.html());
        Matcher m2 = regexMp4Url.matcher(doc.html());

        if(m.find() && m2.find()){

            String swfUrl = m.group(1); //原swf视频的url
            String mp4Url = m2.group(1); //mp4 url http://hc13.aipai.com/user/506/4516506/2902838/card/9468953/card.mp4?l=q

            int lastIndex = mp4Url.lastIndexOf("/");
            String tempUrl = mp4Url.substring(0, lastIndex);

            String prefixId = tempUrl.substring(tempUrl.lastIndexOf("/") + 1, tempUrl.length());

            String bigImageUrl = tempUrl + "/" + prefixId + "_big.jpg";
            String middleImageUrl = tempUrl + "/" + prefixId + "_middle.jpg";


            //
            Video video = new Video();
            video.setDescription(getElement(doc, "title").html());
            video.setOrgUrl(url);
            video.setDescription(getElement(doc, "meta[name=Description]").attr("content"));
            video.setFlash(swfUrl);
            video.setPic(middleImageUrl);
            video.setBigpic(bigImageUrl);

            return video;
        }else {
            return null;
        }
    }

    private Element getElement(Document doc, String expression) {
        Elements elements = doc.select(expression);
        if(!CollectionUtil.isEmpty(elements)){
            for(Element element : elements){
                return element;
            }
        }
        return null;
    }

}
