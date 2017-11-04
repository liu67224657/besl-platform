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

import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class RecoverSurlProcessor implements TextProcessor {
    Logger logger = LoggerFactory.getLogger(RecoverSurlProcessor.class);

    @Override
    public ResolveContent process(ResolveContent content) {
        List<Map<String, String>> urlGroupMaps = RegexUtil.fetch(content.getContent(), HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagFetchRegex(), null);

        String contentText = content.getContent();
        for (Map<String, String> groupMap : urlGroupMaps) {
            try {
                ShortUrl url = ShortUrlServiceSngl.get().getUrl(groupMap.get("1"));

                contentText = contentText.replace(groupMap.get("0"), url.getUrl());
            } catch (ServiceException e) {
                logger.error("process surl occured Service:" + e.getMessage());
            }
        }
        content.setContent(contentText);
        return content;
    }
}
