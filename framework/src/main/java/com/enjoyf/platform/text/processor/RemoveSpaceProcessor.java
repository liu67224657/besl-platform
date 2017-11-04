package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.regex.RegexUtil;

import java.util.regex.Pattern;

/**
 * <p/>
 * Description:移除不需要的HTML标签
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class RemoveSpaceProcessor implements TextProcessor {


    public RemoveSpaceProcessor() {
    }

    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText=RegexUtil.replace(content.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getNBSPFetchRegex(),"", -1);
        content.setContent(contentText);
        return content;
    }
}
