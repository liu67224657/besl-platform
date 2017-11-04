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
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class PostMediaProcessor implements TextProcessor {
    Logger logger = LoggerFactory.getLogger(PostMediaProcessor.class);

    private final static String RESOLVE_TAG_APP = "app";
    private final static String RESOLVE_TAG_IMAGE = "image";
    private final static String RESOLVE_TAG_VIDEO = "video";
    private final static String RESOLVE_TAG_AUDIO = "audio";
    private final static String RESOLVE_TAG_GAME = "game";

    private ContextHotdeployConfig config= HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    @Override
    public ResolveContent process(ResolveContent resolveContent) {
        String content = resolveContent.getContent();

        int appIdx = 0;
        Map<Integer, AppsContent> appMap = new LinkedHashMap<Integer, AppsContent>();

        int gameIdx = 0;
        Map<Integer, GameContent> gameMap = new LinkedHashMap<Integer, GameContent>();

        int imageIdx = 0;
        Map<Integer, ImageContent> imageMap = new LinkedHashMap<Integer, ImageContent>();

        int videoIdx = 0;
        Map<Integer, VideoContent> videoMap = new LinkedHashMap<Integer, VideoContent>();

        int audioIdx = 0;
        Map<Integer, AudioContent> audioMap = new LinkedHashMap<Integer, AudioContent>();

        Matcher match = config.getFetchImgHtml().matcher(content);

        StringBuffer sb = new StringBuffer();
        while (match.find()) {
            String img = match.group();

            Map<String, String> paramMap = praseImgTag(img);

            if (paramMap.containsKey("joymet")) {
                String type = paramMap.get("joymet");
                if (type.equals("game")) {
                    GameContent gameContent = resolveGame(paramMap);
                    if (gameContent != null) {
                        gameMap.put(gameIdx, gameContent);
                        match.appendReplacement(sb, "[" + RESOLVE_TAG_GAME + ":" + gameIdx + "]");
                        gameIdx++;
                    } else {
                        match.appendReplacement(sb, "");
                    }
                } else if (type.equals("img")) {
                    ImageContent imageContent = resolveImage(paramMap);
                    if (imageContent != null) {
                        imageMap.put(imageIdx, imageContent);
                        match.appendReplacement(sb, "[" + RESOLVE_TAG_IMAGE + ":" + imageIdx + "]");
                        imageIdx++;
                    } else {
                        match.appendReplacement(sb, "");
                    }
                } else if (type.equals("video")) {
                    VideoContent videoContent = resolveVideo(paramMap);
                    if (videoContent != null) {
                        videoMap.put(videoIdx, videoContent);
                        match.appendReplacement(sb, "[" + RESOLVE_TAG_VIDEO + ":" + videoIdx + "]");
                        videoIdx++;
                    } else {
                        match.appendReplacement(sb, "");
                    }
                } else if (type.equals("app")) {
                    AppsContent appContent = resolveApp(paramMap);
                    if (appContent != null) {
                        appMap.put(appIdx, appContent);
                        match.appendReplacement(sb, "[" + RESOLVE_TAG_APP + ":" + appIdx + "]");
                        appIdx++;
                    } else {
                        match.appendReplacement(sb, "");
                    }
                } else if (type.equals("audio")) {
                    AudioContent audioContent = resolveAudio(paramMap);
                    if (audioContent != null) {
                        audioMap.put(audioIdx, audioContent);
                        match.appendReplacement(sb, "[" + RESOLVE_TAG_AUDIO + ":" + audioIdx + "]");
                        audioIdx++;
                    } else {
                        match.appendReplacement(sb, "");
                    }
                } else {
                    match.appendReplacement(sb, "");
                }
            } else {
                match.appendReplacement(sb, "");
            }

        }
        match.appendTail(sb);
        resolveContent.setContent(sb.toString());
        resolveContent.setImages(imageMap);
        resolveContent.setVideos(videoMap);
        resolveContent.setAudios(audioMap);
        resolveContent.setApps(appMap);
        resolveContent.setGames(gameMap);
        return resolveContent;
    }

    //<img joymed="([^>"]*)" joymeh="([^>"]*)" joymet="img" joymeu="([^>"]+)" joymew="([^>"]*)" src="(?:[^>"]+)" />
    private ImageContent resolveImage(Map<String, String> paramMap) {
        if (!StringUtil.isEmpty(paramMap.get("joymeu"))) {
            ImageContent image = new ImageContent();
            image.setS(ImageResolveUtil.getImageBySize(paramMap.get("joymeu"), ImageSize.IMAGE_SIZE_S));
            image.setSs(ImageResolveUtil.getImageBySize(paramMap.get("joymeu"), ImageSize.IMAGE_SIZE_SS));
            image.setM(ImageResolveUtil.getImageBySize(paramMap.get("joymeu"), ImageSize.IMAGE_SIZE_M));
            image.setUrl(ImageResolveUtil.getImageBySize(paramMap.get("joymeu"), null));
            image.setDesc(paramMap.get("joymed"));
            try {
                image.setW(Integer.parseInt(paramMap.get("joymew")));
            } catch (NumberFormatException e) {
            }
            try {
                image.setH(Integer.parseInt(paramMap.get("joymeh")));
            } catch (NumberFormatException e) {
            }
            return image;
        }
        return null;
    }

    //<img joymed="" joymef="" joymet="audio" src="" title="" />
    private AudioContent resolveAudio(Map<String, String> paramMap) {
        if (!StringUtil.isEmpty(paramMap.get("joymef"))) {
            AudioContent audio = new AudioContent();
            audio.setSs(ImageResolveUtil.parseAudioSs(paramMap.get("src")));
            audio.setS(ImageResolveUtil.parseAudioS(paramMap.get("src")));
            audio.setM(ImageResolveUtil.parseAudioM(paramMap.get("src")));
            audio.setUrl(ImageResolveUtil.parseAudioB(paramMap.get("src")));
            audio.setTitle(paramMap.get("title"));
            audio.setDesc(paramMap.get("joymed"));
            audio.setFlashUrl(paramMap.get("joymef"));
            return audio;
        }
        return null;
    }

    //<img joymeappt="([^>"]*)" joymec="([^>"]+)" joymed="([^>"]*)" joymei="([^>"]+)" joymer="([^>"]+)" joymet="app" src="(?:[^>"]+)" title="([^>"]*)" />
    private AppsContent resolveApp(Map<String, String> paramMap) {

        if (StringUtil.isEmpty(paramMap.get("joymec"))) {
            return null;
        }

        String desc = "";
        try {
            desc = URLDecoder.decode(paramMap.get("joymed"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        } catch (Exception e) {
            logger.error("get desc occured Exception.", e);
        }

        AppsContent appsContent = new AppsContent();
        appsContent.setAppType(paramMap.get("joymeappt"));
        appsContent.setAppSrc(paramMap.get("joymec"));
        appsContent.setDesc(desc);
        appsContent.setIcon(paramMap.get("joymei"));
        appsContent.setResourceUrl(paramMap.get("joymer"));
        appsContent.setTitle(paramMap.get("title"));

        return appsContent;
    }

    //<img height="141" joymed="" joymef="http://player.youku.com/player.php/sid/XNTEwODYzOTEy/v.swf" joymeo="http://v.youku.com/v_show/id_XNTEwODYzOTEy.html" joymet="video" joymevtime="" src="http://g1.ykimg.com/0100401F46510F4C0C77C10851C114B5E514E8-7727-6DAC-DAA6-3EE6B534EBB8" title="《涟漪》 A Ripple of Love—在线播放—优酷网，视频高清在线观看" width="188" />
    private VideoContent resolveVideo(Map<String, String> paramMap) {
        //封装video
        if (!StringUtil.isEmpty(paramMap.get("joymef"))) {
            VideoContent video = new VideoContent();
            video.setUrl(paramMap.get("src"));
            video.setDesc(paramMap.get("joymed"));
            video.setFlashUrl(paramMap.get("joymef"));
            video.setTitle(paramMap.get("title"));
            video.setOrgUrl(paramMap.get("joymeo"));
            video.setvTime(paramMap.get("joymevtime"));
            return video;
        }
        return null;
    }

    //<img joymegid="112590" joymet="game" src="http://r001.joyme.test/r001/game/2013/02/49/AFBBAE9D970B3DF65A9FB92E1E37907C_GLL.jpg" />
    private GameContent resolveGame(Map<String, String> paramMap) {
        //封装video
        if (!StringUtil.isEmpty(paramMap.get("joymegid"))) {
            GameContent gameContent = new GameContent();
            try {
                gameContent.setId(Long.parseLong(paramMap.get("joymegid")));
                gameContent.setLogo(paramMap.get("src"));
            } catch (NumberFormatException e) {
                return null;
            }

            return gameContent;
        }

        return null;
    }

    private Map<String, String> praseImgTag(String imgTagHtml) {
        Map<String, String> map = new HashMap<String, String>();

        Matcher valueMatcher = config.getFetchHtmlAttr().matcher(imgTagHtml);
        while (valueMatcher.find()) {
            map.put(valueMatcher.group(1), valueMatcher.group(2));
        }

        return map;
    }
}
