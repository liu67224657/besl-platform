package com.enjoyf.platform.serv.sync;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.*;
import com.enjoyf.platform.text.processor.SimpleSubStrTextProcessor;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.regex.RegexUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
class SyncContextProcessor {
    private ContextHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class);

    private static final int CONTENT_TEXT_LENGTH = 128;
    private static final String ENG_BRACKETS = "（";
    private static final String ENG_ANTI_BRACKETS = "）";
    private static final String CHINESE_BRACKETS = "【";
    private static final String CHINESE_ANTI_BRACKETS = "】";

    private static final String CONNECTOR = "-";

    public SyncContent processor(Content content, String tempImagePath) {
        SyncContent returnSyncContent = new SyncContent();
        if (!StringUtil.isEmpty(tempImagePath)) {
            returnSyncContent.setSyncImg(tempImagePath);
        }

        //resolve subject
        String subject = processSubject(content.getSubject());

        //resolve content
        String contentText = "";
        ResolveContent resolveContent = processoContent(content);
        if (!StringUtil.isEmpty(resolveContent.getContent())) {
            contentText = resolveContent.getContent();
        }

        //contentUrl
        String contentUrl = null;
        try {
            contentUrl = genContentUrl(content.getContentId(), content.getUno());
        } catch (ServiceException e) {
            GAlerter.lab(SyncContextProcessor.class.getName() + " getContentProfile occured ServiceException.e", e);
        }
        returnSyncContent.setSyncContentUrl(contentUrl);

        String syncText = "";

        //有app直接添加返回
        if (!CollectionUtil.isEmpty(resolveContent.getApps())) {

            syncText = appendContext(subject, contentText, contentUrl);

            returnSyncContent.setSyncText(syncText);
            returnSyncContent.setSyncContentImageUrl(ImageResolveUtil.genImageByTemplate(content.getApps().getApps().iterator().next().getAppSrc(), null));
            return returnSyncContent;
        } else if (!CollectionUtil.isEmpty(resolveContent.getImages())) {
            //其他多媒体
            if (!StringUtil.isEmpty(tempImagePath)) {
                returnSyncContent.setSyncImg(tempImagePath);
                returnSyncContent.setSyncContentImageUrl(ImageResolveUtil.genImageByTemplate(content.getImages().getImages().iterator().next().getM(), ImageSize.IMAGE_SIZE_M));
            }
        }

        syncText = appendContext(subject, contentText, contentUrl);
        returnSyncContent.setSyncText(syncText);
        return returnSyncContent;
    }

    private String processSubject(String subject) {
        if (!StringUtil.isEmpty(subject)) {
            subject = RegexUtil.replace(subject, config.getChineseBookRegex(), "", -1);
        }
        if (!StringUtil.isEmpty(subject)) {
            subject = CHINESE_BRACKETS + subject + CHINESE_ANTI_BRACKETS;
        }
        return subject;
    }

    private ResolveContent processoContent(Content content) {
        ResolveContent resolveContent = ResolveContent.transferByContent(content);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_SYNC_CONTENT);
        resolveContent = textProcessor.process(resolveContent);
        return resolveContent;
    }

    private String genContentUrl(String contentId, String contentUno) throws ServiceException {
        Profile profile = ProfileServiceSngl.get().getProfileByUno(contentUno);
        String contentUrl = WebappConfig.get().getContentUrlPrefix() + "/" + contentId + "?utm_source=" + profile.getBlog().getDomain();
        return contentUrl;
    }

    private String genContentUrlText(String contentUrl) {
//        contentUrl = ENG_BRACKETS + "传送门→" + contentUrl + ENG_ANTI_BRACKETS;
        contentUrl = " " + contentUrl;
        return contentUrl;
    }

    private String appendContext(String subject, String content, String contentUrl) {
        ResolveContent resolveContent = new ResolveContent();
        String syncText = "";
        if (!StringUtil.isEmpty(subject)) {
            syncText += subject;
        }
        if (!StringUtil.isEmpty(content)) {
            syncText += content;
        }

        //文字内容126个字 1为半角空格长度
        if (syncText.length() > CONTENT_TEXT_LENGTH - 1) {
            int subStringLength = syncText.length() - (syncText.length() - CONTENT_TEXT_LENGTH + 1);
            SimpleSubStrTextProcessor subStringProcessor = new SimpleSubStrTextProcessor(subStringLength - 1);
            resolveContent.setContent(syncText);
            resolveContent = subStringProcessor.process(resolveContent);
            syncText = resolveContent.getContent();
        }

        String contentUrlText = genContentUrlText(contentUrl);
        if (!StringUtil.isEmpty(contentUrlText)) {
            syncText += " " + genContentUrlText(contentUrl);
        }
        return syncText;
    }
}
