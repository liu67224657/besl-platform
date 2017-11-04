package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.text.ImageSize;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NamedTemplate;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.template.FreemarkerTemplateGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:用于文章单页多媒体标签的展示
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class JoymeMediaTemplateProcessor extends AbstractJoymeMediaTemplateProcessor {



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
                paramMap.put("imgSrcM", ImageResolveUtil.genImageByTemplate(imagetContent.getM(), ImageSize.IMAGE_SIZE_M));
                paramMap.put("imgSrcB", ImageResolveUtil.genImageByTemplate(imagetContent.getM(), null));
                paramMap.put("joymew", ImageResolveUtil.genImageByTemplate(imagetContent.getM(), null));
                paramMap.put("joymeh", String.valueOf(imagetContent.getH()));
                paramMap.put("joymew", String.valueOf(imagetContent.getW()));
                paramMap.put("URL_LIB", WebappConfig.get().URL_LIB);
                contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getJoymeImageReplaceNameTemplate()).format(paramMap));
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
        Set<Long> gameResIds = new HashSet<Long>();
        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            if (content.getGames().get(entry.getKey()) == null) {
                continue;
            }
            GameContent gameContent = content.getGames().get(entry.getKey());
            if (gameContent != null && gameContent.getId() != null) {
                gameResIds.add(gameContent.getId());
            }
        }

        //调用接口得到文章中游戏对应的gameResource对象
        Map<Long, GameResource> gameResourceMap = new HashMap<Long, GameResource>();
        if (!CollectionUtil.isEmpty(gameResIds)) {
            QueryCriterions[] gameResourceIdCriterionArray = new QueryCriterions[gameResIds.size()];
            int gameResIdIndex = 0;
            for (Long gameResId : gameResIds) {
                gameResourceIdCriterionArray[gameResIdIndex] = QueryCriterions.eq(GameResourceField.RESOURCEID, gameResId);
                gameResIdIndex++;
            }
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.or(gameResourceIdCriterionArray));
            queryExpress.add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.UNACT.getCode()));

            try {
                for (GameResource gameResource : GameResourceServiceSngl.get().queryGameResources(queryExpress)) {
                    gameResourceMap.put(gameResource.getResourceId(), gameResource);
                }
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " format content query gameResource occured Exception.e: ", e);
            }
        }

        Map<Integer, String> gameTemplateMap = new HashMap<Integer, String>();
        for (Map.Entry<Integer, GameContent> entry : content.getGames().entrySet()) {

            GameResource gameResource = gameResourceMap.get(entry.getValue().getId());


            Map<String,Object> paramMap=new HashMap<String,Object>();

            paramMap.put("game",gameResource);
            String gameHtml=generatorHtmlCache(HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class).getContentGameFtlUri(),paramMap);
            gameTemplateMap.put(entry.getKey(), gameHtml);
        }

        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            if (!gameTemplateMap.containsKey(entry.getKey())) {
                continue;
            }
            String template = gameTemplateMap.get(entry.getKey());
            contentText = contentText.replace(entry.getValue(), template);
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
            paramMap.put("flash", videoContent.getFlashUrl());
            contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getJoymeVideoReplaceNameTemplate()).format(paramMap));
        }

        content.setContent(contentText);
        return content;
    }

    protected ResolveContent convertApp(Map<Integer, String> tagMap, ResolveContent content) {
        String contentText = content.getContent();

        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            if (CollectionUtil.isEmpty(content.getApps())) {
                break;
            }
            if (content.getApps().get(entry.getKey()) == null) {
                continue;
            }

            AppsContent appsContent = content.getApps().get(entry.getKey());
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("src", ImageResolveUtil.genImageByTemplate(appsContent.getAppSrc(), null));
            paramMap.put("resourceUrl", appsContent.getResourceUrl());
            paramMap.put("title", appsContent.getTitle());
            contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getJoymeAppReplaceNameTemplate()).format(paramMap));
        }

        content.setContent(contentText);
        return content;
    }

    protected ResolveContent convertAudio(Map<Integer, String> tagMap, ResolveContent content) {
        String contentText = content.getContent();

        for (Map.Entry<Integer, String> entry : tagMap.entrySet()) {
            if (CollectionUtil.isEmpty(content.getAudios())) {
                break;
            }
            if (content.getAudios().get(entry.getKey()) == null) {
                continue;
            }

            AudioContent audioContent = content.getAudios().get(entry.getKey());
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("imgSrcB", audioContent.getUrl());
            paramMap.put("flash", audioContent.getFlashUrl());
            paramMap.put("title", audioContent.getTitle());
            contentText = contentText.replace(entry.getValue(), NamedTemplate.parse(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getJoymeAudioReplaceNameTemplate()).format(paramMap));
        }

        content.setContent(contentText);
        return content;

    }

    private String generatorHtmlCache(String templateUrl, Map<String, Object> params) {
        String returnString = "";
        try {
            returnString = FreemarkerTemplateGenerator.get().generateTemplate(templateUrl, params);
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " generatorHtmlCache TemplateException error.templateUrl:" + templateUrl, e);
        } catch (TemplateException e) {
            GAlerter.lab(this.getClass().getName() + " generatorHtmlCache TemplateException error.templateUrl:" + templateUrl, e);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " generatorHtmlCache Exception error.templateUrl:" + templateUrl, e);
        }
        return returnString;
    }
}
