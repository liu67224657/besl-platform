package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.regex.RegexUtil;
import org.springframework.web.util.HtmlUtils;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ConvertAtProcessor implements TextProcessor{

    @Override
    public ResolveContent process(ResolveContent content) {

        String contentText = RegexUtil.replace(content.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getAtFetchRegex(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getAtReplaceTemplate(), -1);
        content.setContent(contentText);
        return content;
    }
}
