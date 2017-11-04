package com.enjoyf.webapps.joyme.weblogic.blog;

import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.TextProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-3-20
 * Time: 下午12:14
 * To change this template use File | Settings | File Templates.
 */
public class NoneProcessStragy implements ContentBodyProcessStragy {

    @Override
    public String format(Content content) {
        return content.getContent();
    }
}
