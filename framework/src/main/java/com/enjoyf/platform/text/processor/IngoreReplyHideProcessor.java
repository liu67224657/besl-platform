package com.enjoyf.platform.text.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.regex.RegexUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:评论后显示隐藏的处理类用于文章单页
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class IngoreReplyHideProcessor implements TextProcessor {

    private ContextHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    private Pattern IMAGE_TAG_PATTERN = Pattern.compile("\\[image:(\\d+)\\]");
    private Pattern AUDIO_TAG_PATTERN = Pattern.compile("\\[audio:(\\d+)\\]");
    private Pattern VIDEO_TAG_PATTERN = Pattern.compile("\\[video:(\\d+)\\]");
    private Pattern APP_TAG_PATTERN = Pattern.compile("\\[app:(\\d+)\\]");
    private Pattern GAME_TAG_PATTERN = Pattern.compile("\\[game:(\\d+)\\]");


    @Override
    public ResolveContent process(ResolveContent content) {
        String contentText = content.getContent();
        Matcher match = config.getReplyHideRegex().matcher(contentText);

        while (match.find()) {
             removeReplyHideMedia(match.group(1),content);
        }

        return content;
    }

    private ResolveContent removeReplyHideMedia(String hideText, ResolveContent content) {
        List<Map<String, String>> imageFetchDatas = RegexUtil.fetch(hideText, IMAGE_TAG_PATTERN, -1);
        for(Map<String, String> fetchData:imageFetchDatas){
            content.getImages().remove(Integer.parseInt(fetchData.get("1")));
        }

        List<Map<String, String>> audioFetchDatas = RegexUtil.fetch(hideText, AUDIO_TAG_PATTERN, -1);
        for(Map<String, String> fetchData:audioFetchDatas){
            content.getAudios().remove(Integer.parseInt(fetchData.get("1")));
        }

        List<Map<String, String>> videofetchDatas = RegexUtil.fetch(hideText, VIDEO_TAG_PATTERN, -1);
        for(Map<String, String> fetchData:videofetchDatas){
            content.getVideos().remove(Integer.parseInt(fetchData.get("1")));
        }

        List<Map<String, String>> appFetchDatas = RegexUtil.fetch(hideText, APP_TAG_PATTERN, -1);
        for(Map<String, String> fetchData:appFetchDatas){
            content.getApps().remove(Integer.parseInt(fetchData.get("1")));
        }

        List<Map<String, String>> gameFetchDatas = RegexUtil.fetch(hideText, GAME_TAG_PATTERN, -1);
        for(Map<String, String> fetchData:gameFetchDatas){
            content.getGames().remove(Integer.parseInt(fetchData.get("1")));
        }

        return content;
    }

}
