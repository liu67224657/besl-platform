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
public class RemoveMediaTagProcessor implements TextProcessor {
    @Override
    public ResolveContent process(ResolveContent content) {
        String text= RegexUtil.replace(content.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getJoymeTagFetchRegex(), "", -1);
        content.setContent(text);
        return content;
    }
}
