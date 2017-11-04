package com.enjoyf.platform.text;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.text.processor.HtmlEscapeProcessor;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.StreamUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.regex.RegexUtil;
import org.codehaus.jackson.type.TypeReference;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.util.HtmlUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 上午9:17
 * Description:
 */
public class ProcessItemsUtil {
    private static ContextHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);
    private static final String REPLACE_TEMPLATE = "</p><p>$0</p><p>";

    private static final Pattern P_PATTERN = Pattern.compile("^<p>");
    private static final Pattern REPLACE_P_TEMPLATE = Pattern.compile("<p>[^<]*</p>");

    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");

    public static List<TextJsonItem> praseRichTextToItems(String text) {
        List<TextJsonItem> resultString = new ArrayList<TextJsonItem>();

        if (!text.startsWith("<p>")) {
            text = "<p>" + text;
        }

        if (!text.endsWith("</p>")) {
            text = text + "</p>";
        }

        text = RegexUtil.replace(text, config.getFetchImgHtml(), REPLACE_TEMPLATE, -1);

        Document document = Jsoup.parse(text);
        Elements elements = document.getElementsByTag("p");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (StringUtil.isEmpty(element.html())) {
                continue;
            }

            Elements imgElements = element.getElementsByTag("img");
            TextJsonItem jsonItems = null;
            if (imgElements != null && imgElements.size() > 0
                    && imgElements.get(0).attr("src") != null && imgElements.get(0).attr("src").length() > 0) {
                jsonItems = new TextJsonItem(TextJsonItem.IMAGE_TYPE, imgElements.get(0).attr("src"));
                jsonItems = buildImageJson(jsonItems);
            } else {
                jsonItems = new TextJsonItem(TextJsonItem.TEXT_TYPE, element.text());
                jsonItems = buildTextJson(jsonItems);

            }
            resultString.add(jsonItems);
        }

        return resultString;
    }


    private static TextJsonItem buildImageJson(TextJsonItem textJsonItem) {
        URL url = null;
        InputStream inputStream = null;
        try {
            url = new URL(textJsonItem.getItem());
            inputStream = url.openStream();
            Image img = ImageIO.read(inputStream);
            textJsonItem.setWidth(img.getWidth(null));
            textJsonItem.setHeight(img.getHeight(null));
        } catch (Exception e) {
            textJsonItem.setWidth(200);
            textJsonItem.setHeight(200);
        } finally {
            StreamUtil.closeInputStream(inputStream);
        }
        return textJsonItem;
    }

    private static TextJsonItem buildTextJson(TextJsonItem textJsonItem) {
        String contentText = HtmlUtils.htmlUnescape(textJsonItem.getItem());
        contentText = RegexUtil.replace(contentText, HTML_PATTERN, "", -1);
        textJsonItem.setItem(contentText);
        return textJsonItem;
    }


    public static void main(String[] args) {
        String s = "<p>112121</p><p>12121</p><p>444</p><p>555</p><p>6666</p><p><B>sdfdsfds</B><a href=\"www.baidu.com\">百度</a></p><p><img src=\"http://www.joyme.com/article/uploads/allimg/130806/7_130806145333_1.png\"/></p>";

        List<TextJsonItem> jsObjList = ProcessItemsUtil.praseRichTextToItems(s);
        System.out.println("-" + TextJsonItem.toJson(jsObjList));


        System.out.println("list::::" + TextJsonItem.fromJson("[{\"type\":1,\"item\":\"11111\"},{\"type\":2,\"item\":\"tupian.jpg\"},{\"type\":1,\"item\":\"aa\"},{\"type\":2,\"item\":\"tupian.jpg\"},{\"type\":1,\"item\":\"aa\"},{\"type\":1,\"item\":\"ccc\"}]"));

    }
}
