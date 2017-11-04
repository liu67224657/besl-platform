package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.regex.RegexUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-25
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
public class ReplaceLineBreakProcessor implements TextProcessor {
    private String destStr;

    public ReplaceLineBreakProcessor(String destStr) {
        this.destStr = destStr;
    }

    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText = RegexUtil.replace(content.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getManyLineBreaksFetchRegex(), destStr, -1);
        content.setContent(contentText.replaceAll("\r", ""));
        return content;
    }
}
