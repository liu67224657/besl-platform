package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.shorturl.ShortUrlServiceSngl;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.regex.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Matcher;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class PostSurlProcessor implements TextProcessor {
    Logger logger= LoggerFactory.getLogger(PostSurlProcessor.class);

    @Override
    public ResolveContent process(ResolveContent content) {
         //转换原有的j0y.me
        String orignContent = RegexUtil.replace(content.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getJoymeUrlFetchRegex(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getJoymeUrlReplaceTemplate(), -1);

        Matcher m = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlFetchRegex().matcher(orignContent);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            try {
                ShortUrl shortUrl = ShortUrlServiceSngl.get().generateUrl(HtmlUtils.htmlUnescape(m.group(0)), content.getContentUno());
                String template = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlReplaceTemplate();
                template = template.replace("{urlkey}", shortUrl.getUrlKey());
                m.appendReplacement(sb, template);
            } catch (ServiceException e) {
                logger.error(" generator occuredServiceException.e", e);
            }
        }
        m.appendTail(sb);
        content.setContent(sb.toString());
        return content;
    }
}
