package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.text.ImageSize;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:用于文章单页多媒体标签的展示
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class JoymeMobileMediaTemplateProcessor extends AbstractJoymeMediaTemplateProcessor {
    private ContextHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    //链状返回替换成模板结果
    protected ResolveContent convertImage(Map<Integer, String> tagMap, ResolveContent content) {
        if (CollectionUtil.isEmpty(content.getImages())) {
            return content;
        }

        String contentText = content.getContent();
        String banTemplate = StringUtil.isEmpty(config.getBanJoymeImageTemplate()) ? "" : HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getBanJoymeImageTemplate();

        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            ImageContent imagetContent = content.getImages().get(entry.getKey());
            if (imagetContent == null || !imagetContent.getValidStatus()) {
                contentText = contentText.replace(entry.getValue(), banTemplate);
            } else {
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("imgSrcM", ImageResolveUtil.genImageByTemplate(imagetContent.getM(), ImageSize.IMAGE_SIZE_M));
                paramMap.put("imgSrcB", ImageResolveUtil.genImageByTemplate(imagetContent.getM(), null));
                contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(config.getMobileImageReplaceNameTemplate()).format(paramMap));
            }
        }

        content.setContent(contentText);
        return content;
    }

    //链状返回解析过的游戏元素的文本结果
    protected ResolveContent convertGame(Map<Integer, String> tagMap, ResolveContent content) {
        if (CollectionUtil.isEmpty(content.getGames())) {
            return content;
        }

        String contentText = content.getContent();

        //调用接口得到文章中游戏对应的gameResource对象
        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            String template = "";
            contentText = contentText.replace(entry.getValue(), template);
        }

        content.setContent(contentText);
        return content;

    }

    //todo
    protected ResolveContent convertVideo(Map<Integer, String> tagMap, ResolveContent content) {
        if (CollectionUtil.isEmpty(content.getVideos())) {
            return content;
        }

        String contentText = content.getContent();
        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            if (content.getVideos().get(entry.getKey()) == null) {
                continue;
            }

            VideoContent videoContent = content.getVideos().get(entry.getKey());
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("url", videoContent.getOrgUrl());
            paramMap.put("src", videoContent.getUrl());
            contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(config.getMobileVideoReplaceNameTemplate()).format(paramMap));
        }

        content.setContent(contentText);
        return content;
    }

    protected ResolveContent convertApp(Map<Integer, String> tagMap, ResolveContent content) {
        String contentText = content.getContent();

        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            Map<String, String> paramMap = new HashMap<String, String>();
            if (StringUtil.isEmpty(config.getMobileAppReplaceNameTemplate())) {
                contentText = contentText.replace(entry.getValue(), "");
            } else {
                contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(config.getMobileAppReplaceNameTemplate()).format(paramMap));
            }
        }

        content.setContent(contentText);
        return content;
    }

    protected ResolveContent convertAudio(Map<Integer, String> tagMap, ResolveContent content) {
        String contentText = content.getContent();

        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            if (StringUtil.isEmpty(config.getMobileAudioReplaceNameTemplate())) {
                contentText = contentText.replace(entry.getValue(), "");
            } else {
                Map<String, String> paramMap = new HashMap<String, String>();
                contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(config.getMobileAudioReplaceNameTemplate()).format(paramMap));
            }


        }

        content.setContent(contentText);
        return content;

    }
}
