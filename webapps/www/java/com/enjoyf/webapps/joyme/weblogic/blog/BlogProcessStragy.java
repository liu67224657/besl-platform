package com.enjoyf.webapps.joyme.weblogic.blog;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.InteractionType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-3-20
 * Time: 下午12:14
 * To change this template use File | Settings | File Templates.
 */
public class BlogProcessStragy implements ContentBodyProcessStragy {
    private String srcUno;

    public BlogProcessStragy(String srcUno) {
        this.srcUno = srcUno;
    }

    @Override
    public String format(Content content) {
        //短文
        if (content.getContentType().hasPhrase()) {
            return processContentByKey(WordProcessorKey.KEY_ALL_CHAT, content);
        }

        //长文 观看者的uno不为空 且 有回复可见部分
        if (!StringUtil.isEmpty(srcUno) && content.getContentType().hasReplayHide()) {
            if (srcUno.equals(content.getUno())) {
                return processContentByKey(WordProcessorKey.KEY_ALL_TEXT_DISPLAY, content);
            }

            List<ContentInteraction> replyList = null;
            try {
                replyList = ContentServiceSngl.get().queryInteractionByCidIUno(srcUno, content.getContentId(), InteractionType.REPLY);
            } catch (ServiceException e) {
                GAlerter.lab(" getBlogContent get user replyList occured ServiceException: ", e);
            }

            //todo 有回复可见部分无回复可见
            if (!CollectionUtil.isEmpty(replyList)) {
                return processContentByKey(WordProcessorKey.KEY_ALL_TEXT_DISPLAY, content);
            } else {
                return processContentByKey(WordProcessorKey.KEY_ALL_TEXT_HIDE, content);
            }
        } else {
            //无回复可见部分
            return processContentByKey(WordProcessorKey.KEY_ALL_TEXT_HIDE, content);
        }
    }

    private String processContentByKey(WordProcessorKey key, Content content) {
        ResolveContent resolveContent = ResolveContent.transferByContent(content);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(key);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }
}
