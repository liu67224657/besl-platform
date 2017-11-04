package com.enjoyf.platform.serv.content.processor;

import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public class ResourceFileUsingProcessor implements ContentProcessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(ResourceFileUsingProcessor.class);

    private ContentHandler writeableHandler;

    //
    public ResourceFileUsingProcessor(ContentHandler handler) {
        this.writeableHandler = handler;
    }

    //
    public void process(Object obj) {
        //
        if (obj instanceof Content) {
            processContextQueueContent((Content) obj);
        } else if (obj instanceof ContentReply) {
            processContextQueueReply((ContentReply) obj);
        } else {
            GAlerter.lab("In contextProcessQueueThreadN, there is a unknown obj.");
        }
    }

    //
    private void processContextQueueContent(Content content) {
        //
        if (content.getImages() != null) {
            for (ImageContent image : content.getImages().getImages()) {
                increateResourceFileUsedTimes(image.getUrl());
            }
        }

        //
        if (content.getApps() != null) {
            for (AppsContent app : content.getApps().getApps()) {
                increateResourceFileUsedTimes(app.getAppSrc());
            }
        }
    }

    //
    private void processContextQueueReply(ContentReply reply) {

    }

    private void increateResourceFileUsedTimes(String fileId) {
        try {
            writeableHandler.increaseResourceFileUsedTimes(fileId, 1);
        } catch (Exception e) {
            //
            GAlerter.lab("ResourceFileUsingProcessor call writeableHandler to increaseResourceFileUsedTimes error.", e);
        }
    }
}
