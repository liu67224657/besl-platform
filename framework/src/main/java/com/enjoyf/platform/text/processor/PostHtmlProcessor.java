package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.regex.RegexUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class PostHtmlProcessor implements TextProcessor {

    private String convertBreaklines=HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getP2BRReplaceTemplate();

    public PostHtmlProcessor(String convertBreaklines) {
        this.convertBreaklines = convertBreaklines;
    }

    public PostHtmlProcessor() {
    }

    @Override
    public ResolveContent process(ResolveContent resolveContent) {
        String text = RegexUtil.replace(resolveContent.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getPostHtmlTagFetchRegex(), "", -1);
        //</p>转化成</br>
        text = RegexUtil.replace(text, HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getP2BRFetchRegex(),convertBreaklines , -1);
        resolveContent.setContent(text);

        return resolveContent;
    }
}
