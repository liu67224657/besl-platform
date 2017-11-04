package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.regex.RegexUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-9-27
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class RemoveUncomplateTagProcessor implements TextProcessor{

    @Override
    public ResolveContent process(ResolveContent content) {
        String conteText = RegexUtil.replace(content.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getInCompleteFetchRegex(), "", -1);
        content.setContent(conteText);
        return content;
    }
}
