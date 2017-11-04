package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;

import java.util.regex.Matcher;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class RemoveMoodyProcessor implements TextProcessor {
    @Override
    public ResolveContent process(ResolveContent content) {
        Matcher m = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getMoodFetchRegex().matcher(content.getContent());
        String text = m.replaceAll("");
        content.setContent(text);
        return content;
    }
}
