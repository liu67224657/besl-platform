package com.enjoyf.platform.service.content.processor;

import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.shorturl.ShortUrlServiceSngl;
import com.enjoyf.platform.util.regex.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-13
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */
public class ContentProcessUtil {

    private static ContextHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    /**
     * 根据size得到@
     *
     * @param context
     * @param size
     * @return
     */
    public static List<Map<String, String>> getAtList(String context, Integer size) {
        List<Map<String, String>> list = RegexUtil.fetch(context, config.getAtFetchRegex(), size);

        return list;
    }

    /**
     * 文本处理(@..后期加:mood images videos audios formate 等)
     *
     * @param context
     * @return
     */
    public static String processContext(String context) {
        context = replaceAt(context);
        context = replaceSUrlTagToSurl(context);
        return context;
    }

    //替换@
    private static String replaceAt(String context) {
        context = RegexUtil.replace(context, config.getAtFetchRegex(), config.getAtReplaceTemplate(), -1);
        return context;
    }

    /**
     * 查询文章时候将短链接标签替换成短链接
     *
     * @param context
     * @return
     */
    public static String replaceSUrlTagToSurl(String context) {
        context = RegexUtil.replace(context, config.getSurlTagFetchRegex(), config.getSurlTagReplaceTemplate(), -1);
        return context;
    }

    public static String replaceSurlTagToText(String text){
         text = RegexUtil.replace(text, config.getSurlTagFetchRegex(), config.getSurlTextReplaceTemplate(), -1);
        return text;
    }

     public static String replaceSurlTagToStr(String text,String replacement){
         text = RegexUtil.replace(text, config.getSurlTagFetchRegex(), replacement, -1);
        return text;
    }

}
