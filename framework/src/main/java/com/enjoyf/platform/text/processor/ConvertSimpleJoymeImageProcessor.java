package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.content.ImageContent;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.text.ImageSize;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ConvertSimpleJoymeImageProcessor implements TextProcessor {

    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText = content.getContent();
        Matcher m = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getJoymeImageRegex().matcher(contentText);
        int idx = 0;

        String banTemplate = StringUtil.isEmpty(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getBanJoymeImageTemplate()) ? "" : HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getBanJoymeImageTemplate();
        while (m.find()) {
            String img = m.group();
            ImageContent imagetContent = content.getImages().get(idx);

            if (imagetContent == null || !imagetContent.getValidStatus()) {
                contentText = contentText.replace(img, banTemplate);
            } else {
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("imgSrcM", ImageResolveUtil.genImageByTemplate(content.getImages().get(idx).getM(), ImageSize.IMAGE_SIZE_M));
                contentText = contentText.replace(img, NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSimpleJoymeImageTemplate()).format(paramMap));
            }
            idx++;
        }
        content.setContent(contentText);
        return content;
    }
}
