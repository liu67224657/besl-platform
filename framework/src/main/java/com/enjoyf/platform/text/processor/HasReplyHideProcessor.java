package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.text.ImageSize;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:set resolveContent hasReplyFlag if finde is true hasReplyHide
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class HasReplyHideProcessor implements TextProcessor {

    private ContextHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    @Override
    public ResolveContent process(ResolveContent resolveContent) {
        String content = resolveContent.getContent();

        Matcher match = config.getReplyHideRegex().matcher(content);
        resolveContent.setHasReplyHide(match.find());

        return resolveContent;
    }

}
