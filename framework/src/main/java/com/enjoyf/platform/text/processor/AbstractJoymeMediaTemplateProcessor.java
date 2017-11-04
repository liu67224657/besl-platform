package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-20
 * Time: 下午3:31
 * To change this template use File | Settings | File Templates.
 */

public abstract class AbstractJoymeMediaTemplateProcessor  implements TextProcessor {

    protected final static String RESOLVE_TAG_APP = "app";
    protected final static String RESOLVE_TAG_IMAGE = "image";
    protected final static String RESOLVE_TAG_VIDEO = "video";
    protected final static String RESOLVE_TAG_AUDIO = "audio";
    protected final static String RESOLVE_TAG_GAME = "game";

      private ContextHotdeployConfig config= HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText = content.getContent();

        Map<Integer, String> imageMap = new HashMap<Integer, String>();

        Map<Integer, String> gameMap = new HashMap<Integer, String>();

        Map<Integer, String> videoMap = new HashMap<Integer, String>();

        Map<Integer, String> appMap = new HashMap<Integer, String>();

        Map<Integer, String> audioMap = new HashMap<Integer, String>();

        Matcher matcher = config.getJoymeTagFetchRegex().matcher(contentText);
        while (matcher.find()) {
            String mediaType = matcher.group(1);
            try {
                if (mediaType.equals(RESOLVE_TAG_IMAGE)) {
                    imageMap.put(Integer.parseInt(matcher.group(2)), matcher.group());
                } else if (mediaType.equals(RESOLVE_TAG_GAME)) {
                    gameMap.put(Integer.parseInt(matcher.group(2)), matcher.group());
                } else if (mediaType.equals(RESOLVE_TAG_VIDEO)) {
                    videoMap.put(Integer.parseInt(matcher.group(2)), matcher.group());
                } else if (mediaType.equals(RESOLVE_TAG_APP)) {
                    appMap.put(Integer.parseInt(matcher.group(2)), matcher.group());
                } else if (mediaType.equals(RESOLVE_TAG_AUDIO)) {
                    audioMap.put(Integer.parseInt(matcher.group(2)), matcher.group());
                }
            } catch (NumberFormatException e) {
                //不做处理
            }
        }

        content = convertImage(imageMap, content);
        content = convertGame(gameMap, content);
        content = convertVideo(videoMap, content);
        content = convertApp(appMap, content);
        content = convertAudio(audioMap, content);

        return content;
    }

    //链状返回替换成模板结果
    protected abstract ResolveContent convertImage(Map<Integer, String> tagMap, ResolveContent content);
    //链状返回解析过的游戏元素的文本结果
    protected abstract ResolveContent convertGame(Map<Integer, String> tagMap, ResolveContent content) ;

    protected abstract ResolveContent convertVideo(Map<Integer, String> tagMap, ResolveContent content);

    protected abstract ResolveContent convertApp(Map<Integer, String> tagMap, ResolveContent content);

    protected abstract ResolveContent convertAudio(Map<Integer, String> tagMap, ResolveContent content);
}
