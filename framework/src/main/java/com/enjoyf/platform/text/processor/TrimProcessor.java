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
public class TrimProcessor implements TextProcessor{

    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText = RegexUtil.replace(content.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getTrimLeftRegex(), "", -1);
        contentText = RegexUtil.replace(contentText, HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getTrimRightRegex(), "", -1);
        content.setContent(contentText);
        return content;
    }
}
