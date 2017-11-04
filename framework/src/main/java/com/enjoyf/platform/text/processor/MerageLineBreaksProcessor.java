package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.regex.RegexUtil;

/**
 * <p/>
 * Description:移除不需要的HTML标签
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class MerageLineBreaksProcessor implements TextProcessor {

    private String destStr;

    public MerageLineBreaksProcessor(String destStr) {
        this.destStr = destStr;
    }

    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText=RegexUtil.replace(content.getContent(),HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getManyLineBreaksFetchRegex(), destStr, -1);
        content.setContent(contentText);
        return content;
    }
}
