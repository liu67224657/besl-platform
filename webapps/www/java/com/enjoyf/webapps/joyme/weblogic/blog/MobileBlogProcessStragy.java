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
public class MobileBlogProcessStragy implements ContentBodyProcessStragy {

     @Override
    public String format(Content content) {
        //短文
        if (content.getContentType().hasPhrase()) {
            return processContentByKey(WordProcessorKey.KEY_ALL_CHAT, content);
        }

        return processContentByKey(WordProcessorKey.KEY_MOBILE_ALL_TEXT, content);
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
