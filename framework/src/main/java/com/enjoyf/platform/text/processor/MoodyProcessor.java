package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.MoodHotdeployConfig;
import com.enjoyf.platform.service.content.Mood;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.StringUtil;

import java.util.regex.Matcher;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class MoodyProcessor implements TextProcessor {
    @Override
    public ResolveContent process(ResolveContent content) {
//        if (HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getMoodFetchRegex() == null) {
//            return "";
//        }
        String contentText = content.getContent();
        Matcher m = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getMoodFetchRegex().matcher(contentText);

//        while (m.find()) {
//            String sUrl = getMoodHref(m.group(2));
//            if (!StringUtil.isEmpty(sUrl)) {
//                contentText = contentText.replace(m.group(1), sUrl);
//                //srcText 再次匹配 减少 循环次数
//                m = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getMoodFetchRegex().matcher(contentText);
//            }
//        }
//        content.setContent(contentText);

        StringBuffer sbuf = new StringBuffer();
        while (m.find()) {
            String sUrl = getMoodHref(m.group(2));
            if (!StringUtil.isEmpty(sUrl)) {
                m.appendReplacement(sbuf, sUrl);
            }
        }
        m.appendTail(sbuf);
        content.setContent(sbuf.toString());
        return content;
    }

    private static String getMoodHref(String keyName) {
        Mood mood = HotdeployConfigFactory.get().getConfig(MoodHotdeployConfig.class).getImageUrlByCode(keyName);
        if (mood == null) {
            return "";
        }
        return "<img title='" + keyName + "' src='" + mood.getImgUrl() + "' alt='" + keyName + "' />";
    }
}
