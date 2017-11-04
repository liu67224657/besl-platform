package com.enjoyf.webapps.joyme.webpage.util;


import com.enjoyf.platform.props.WebappConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p/>
 * Description:日期类型的标签
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ContentTag {
    private static final String REG_HTML_BR = "<\\s*br\\s*/?\\s*>";
    private static final Pattern PATTERN_BR = Pattern.compile(REG_HTML_BR, Pattern.CASE_INSENSITIVE);

    public static boolean hasContent(String content) {
        content = content.trim();
        Matcher m = PATTERN_BR.matcher(content);

        int brCount = 0;
        while (m.find()) {
            brCount++;
        }

        return (content.length() >= WebappConfig.get().getTextPreviewLength() || brCount >= WebappConfig.get().getTextPreviewLines() - 1) && content.endsWith("...");
    }


}
