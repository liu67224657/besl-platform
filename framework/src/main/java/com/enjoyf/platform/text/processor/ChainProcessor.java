package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ChainProcessor implements TextProcessor {
    private List<TextProcessor> textProcessors = new ArrayList<TextProcessor>();


    @Override
    public ResolveContent process(ResolveContent content) {
        for (TextProcessor textProcessor : textProcessors) {
            if(StringUtil.isEmpty(content.getContent())){
                 return content;
            }

            content = textProcessor.process(content);
        }

        return content;
    }

    public void addProcessor(TextProcessor p) {
        textProcessors.add(p);
    }
}
