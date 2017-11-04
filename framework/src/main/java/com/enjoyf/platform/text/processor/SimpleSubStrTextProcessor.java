package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.StringUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class SimpleSubStrTextProcessor implements TextProcessor {
    private int length;

    public SimpleSubStrTextProcessor(int length) {
        this.length = length;
    }

    @Override
    public ResolveContent process(ResolveContent content) {
        String srcText = StringUtil.length(content.getContent()) <= length ? content.getContent() : StringUtil.subString(content.getContent(), length) + "…";
        content.setContent(srcText);
        return content;
    }
}
