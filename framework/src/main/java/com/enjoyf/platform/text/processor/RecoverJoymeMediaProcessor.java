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
 * Description:用于编辑文章时候将文章还原为有joyme标签的img
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class RecoverJoymeMediaProcessor extends AbstractJoymeMediaTemplateProcessor {

    //链状返回替换成模板结果
    protected ResolveContent convertImage(Map<Integer, String> tagMap, ResolveContent content) {
        if (CollectionUtil.isEmpty(content.getImages())) {
            return content;
        }

        String contentText = content.getContent();
        String banTemplate = StringUtil.isEmpty(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getBanJoymeImageTemplate()) ? "" : HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getBanJoymeImageTemplate();

        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            ImageContent imagetContent = content.getImages().get(entry.getKey());
            if (imagetContent == null || !imagetContent.getValidStatus()) {
                contentText = contentText.replace(entry.getValue(), banTemplate);
            } else {
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("imgh", String.valueOf(imagetContent.getH()));
                paramMap.put("imgw", String.valueOf(imagetContent.getW()));
                paramMap.put("desc", imagetContent.getDesc());
                paramMap.put("url", imagetContent.getUrl());
                paramMap.put("src", ImageResolveUtil.genImageByTemplate(imagetContent.getM(), ImageSize.IMAGE_SIZE_SS));
                contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getEditImageReplaceNameTemplate()).format(paramMap));
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
        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            if (content.getGames().get(entry.getKey()) == null) {
                continue;
            }

            GameContent gameContent = content.getGames().get(entry.getKey());
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("gameid", String.valueOf(gameContent.getId()));
            paramMap.put("src", gameContent.getLogo());

                contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getEditGameReplaceNameTemplate()).format(paramMap));
        }

        content.setContent(contentText);
        return content;
    }

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
            paramMap.put("desc", videoContent.getDesc());
            paramMap.put("flashUrl", videoContent.getFlashUrl());
            paramMap.put("src", videoContent.getUrl());
            paramMap.put("title", videoContent.getTitle());
            paramMap.put("orgUrl", videoContent.getOrgUrl());
            paramMap.put("vTime", videoContent.getvTime());
            contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getEditVideoReplaceNameTemplate()).format(paramMap));
        }

        content.setContent(contentText);
        return content;
    }

    protected ResolveContent convertApp(Map<Integer, String> tagMap, ResolveContent content) {
        if (CollectionUtil.isEmpty(content.getApps())) {
            return content;
        }

        String contentText = content.getContent();
        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {

            if (content.getApps().get(entry.getKey()) == null) {
                continue;
            }

            AppsContent appsContent = content.getApps().get(entry.getKey());
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("title", appsContent.getTitle());
            paramMap.put("desc", appsContent.getDesc());
            paramMap.put("src", ImageResolveUtil.genImageByTemplate(appsContent.getAppSrc(), null));
            paramMap.put("cardSrc", appsContent.getAppSrc());
            paramMap.put("appType", appsContent.getAppType());
            paramMap.put("appIcon", appsContent.getIcon());
            paramMap.put("resourceUrl", appsContent.getResourceUrl());
            contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getEditAppReplaceNameTemplate()).format(paramMap));

        }

        content.setContent(contentText);
        return content;
    }

    protected ResolveContent convertAudio(Map<Integer, String> tagMap, ResolveContent content) {
        if (CollectionUtil.isEmpty(content.getAudios())) {
            return content;
        }

        String contentText = content.getContent();

        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            if (content.getAudios().get(entry.getKey()) == null) {
                continue;
            }

            AudioContent audioContent = content.getAudios().get(entry.getKey());
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("desc", audioContent.getDesc());
            paramMap.put("flashUrl", audioContent.getFlashUrl());
            paramMap.put("src", ImageResolveUtil.parseAudioSs(audioContent.getUrl()));
            paramMap.put("title", audioContent.getTitle());
            contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getEditAudioReplaceNameTemplate()).format(paramMap));
        }

        content.setContent(contentText);
        return content;

    }

}
