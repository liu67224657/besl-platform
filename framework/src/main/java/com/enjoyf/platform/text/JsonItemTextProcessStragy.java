package com.enjoyf.platform.text;

import com.enjoyf.platform.util.regex.RegexUtil;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-27 下午3:47
 * Description:
 */
public class JsonItemTextProcessStragy implements JsonItemProcessStragy {
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");

    @Override
    public TextJsonItem processorText(TextJsonItem jsonItem) {
        String contentText = HtmlUtils.htmlUnescape(jsonItem.getItem());
        contentText = RegexUtil.replace(contentText, HTML_PATTERN, "", -1);
        jsonItem.setItem(contentText);
        return jsonItem;
    }
}
