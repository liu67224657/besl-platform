package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.regex.RegexUtil;

/**
 * <p/>
 * Description:评论后显示隐藏的处理类用于文章单页
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ReplyHideListTemplateProcessor implements TextProcessor {

    private ContextHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText = content.getContent();
        String template= config.getReplyHideListTemplate();
        content.setContent(RegexUtil.replace(contentText, config.getReplyHideRegex(),StringUtil.isEmpty(template)?"":template , -1));
        return content;
    }

}
