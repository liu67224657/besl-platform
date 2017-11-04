package com.enjoyf.webapps.joyme.webpage.controller.app;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ContentTag;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.joyme.webpage.base.mvc.BaseRestSpringController;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
@Controller
@RequestMapping("/json/app/sjcs/talent/")
public class JsonTalentCaluatorvController extends BaseRestSpringController {


    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    //发布文字
    @RequestMapping(value = "/share")
    @ResponseBody
    public String share(@RequestParam(value = "content", required = true) String content,
                        @RequestParam(value = "blogsubject", required = false) String blogSubject,
                        @RequestParam(value = "tags", required = false) String tags,
                        HttpServletRequest request) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        //是否登录
        UserSession userSession = getUserBySession(request);
        Content postContent = new Content();
        try {
            String uno = userSession.getBlogwebsite().getUno();

            postContent.setUno(uno);
            //解析
            ResolveContent resolveContent = processPostContent(WordProcessorKey.KEY_POST_TEXT, uno, content);
            postContent.setContent(resolveContent.getContent());

            postContent.setPublishDate(new Date());
            postContent.setPublishIp(getIp(request));
            postContent.setSubject(blogSubject);

            //处理tag
            ContentTag contentTag = new ContentTag(tags);
            postContent.setContentTag(contentTag);

            ContentType type = new ContentType();
            type.has(ContentType.TEXT);

            String thumbImgLink = "";
            if (!CollectionUtil.isEmpty(resolveContent.getAudios().values())) {
                postContent.setAudios(new AudioContentSet(resolveContent.getAudios().values()));
                type.has(ContentType.AUDIO);
                thumbImgLink = resolveContent.getAudios().values().iterator().next().getSs();
            }
            if (!CollectionUtil.isEmpty(resolveContent.getVideos().values())) {
                postContent.setVideos(new VideoContentSet(resolveContent.getVideos().values()));
                type.has(ContentType.VIDEO);
                thumbImgLink = resolveContent.getVideos().values().iterator().next().getUrl();
            }
            if (!CollectionUtil.isEmpty(resolveContent.getImages().values())) {
                postContent.setImages(new ImageContentSet(resolveContent.getImages().values()));
                type.has(ContentType.IMAGE);
                thumbImgLink = resolveContent.getImages().values().iterator().next().getSs();
            }
            if (!CollectionUtil.isEmpty(resolveContent.getApps().values())) {
                postContent.setApps(new AppsContentSet(resolveContent.getApps().values()));
                type.has(ContentType.APP);
            }
            if (!StringUtil.isEmpty(thumbImgLink)) {
                postContent.setThumbImgLink(thumbImgLink);
            }


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " postText occured ServiceException:", e);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg(i18nSource.getMessage("system.error", new Object[]{}, Locale.CHINA));
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        List<Content> returnList = new ArrayList<Content>();
        returnList.add(postContent);
        resultMsg.setResult(returnList);

        JsonBinder jsonBinder = JsonBinder.buildNormalBinder();
        jsonBinder.setDateFormat("yyyy-MM-dd HH:mm");
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    //发布文字
    @RequestMapping(value = "/sharethird")
    @ResponseBody
    public String shareThird(@RequestParam(value = "content", required = true) String content,
                             @RequestParam(value = "blogsubject", required = false) String blogSubject,
                             @RequestParam(value = "tags", required = false) String tags,
                             HttpServletRequest request) {
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_S);

        //是否登录
        Content postContent = new Content();
        try {
            String uno = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSjcsTalentShareUno();
            if (StringUtil.isEmpty(uno)) {
                GAlerter.lab("share third content uno is empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            postContent.setUno(uno);
            //解析
            ResolveContent resolveContent = processPostContent(WordProcessorKey.KEY_POST_TEXT, uno, content);
            postContent.setContent(resolveContent.getContent());

            postContent.setPublishDate(new Date());
            postContent.setPublishIp(getIp(request));
            postContent.setSubject(blogSubject);

            //处理tag
            ContentTag contentTag = new ContentTag(tags);
            postContent.setContentTag(contentTag);

            ContentType type = new ContentType();
            type.has(ContentType.TEXT);

            String thumbImgLink = "";
            if (!CollectionUtil.isEmpty(resolveContent.getAudios().values())) {
                postContent.setAudios(new AudioContentSet(resolveContent.getAudios().values()));
                type.has(ContentType.AUDIO);
                thumbImgLink = resolveContent.getAudios().values().iterator().next().getSs();
            }
            if (!CollectionUtil.isEmpty(resolveContent.getVideos().values())) {
                postContent.setVideos(new VideoContentSet(resolveContent.getVideos().values()));
                type.has(ContentType.VIDEO);
                thumbImgLink = resolveContent.getVideos().values().iterator().next().getUrl();
            }
            if (!CollectionUtil.isEmpty(resolveContent.getImages().values())) {
                postContent.setImages(new ImageContentSet(resolveContent.getImages().values()));
                type.has(ContentType.IMAGE);
                thumbImgLink = resolveContent.getImages().values().iterator().next().getSs();
            }
            if (!CollectionUtil.isEmpty(resolveContent.getApps().values())) {
                postContent.setApps(new AppsContentSet(resolveContent.getApps().values()));
                type.has(ContentType.APP);
            }
            if (!StringUtil.isEmpty(thumbImgLink)) {
                postContent.setThumbImgLink(thumbImgLink);
            }


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " postText occured ServiceException:", e);
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
            resultMsg.setMsg(i18nSource.getMessage("system.error", new Object[]{}, Locale.CHINA));
            return JsonBinder.buildNormalBinder().toJson(resultMsg);
        }

        List<Content> returnList = new ArrayList<Content>();
        returnList.add(postContent);
        resultMsg.setResult(returnList);

        JsonBinder jsonBinder = JsonBinder.buildNormalBinder();
        jsonBinder.setDateFormat("yyyy-MM-dd HH:mm");
        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

    private ResolveContent processPostContent(WordProcessorKey key, String uno, String content) {
        ResolveContent resolveContent = new ResolveContent();
        resolveContent.setContent(content);
        resolveContent.setContentUno(uno);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(key);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent;
    }
}
