package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.text.ResolveContent;
import org.springframework.web.util.HtmlUtils;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class HtmlEscapeProcessor implements TextProcessor {

    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText = HtmlUtils.htmlEscape(content.getContent());
        content.setContent(contentText);
        return content;
    }
}
