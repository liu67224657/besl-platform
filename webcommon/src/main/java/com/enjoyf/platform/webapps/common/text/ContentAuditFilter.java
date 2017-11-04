package com.enjoyf.platform.webapps.common.text;

import com.enjoyf.platform.service.content.*;

import java.util.Iterator;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ContentAuditFilter {

    public static Content filterContent(Content content) {
        if (content == null) {
            return content;
        }

        filterImage(content);
        filterAudio(content);
        filterVideo(content);
        return content;
    }

    public static Content filterItemContent(Content content) {
        if (content == null) {
            return content;
        }

        filterItemImage(content);
        filterAudio(content);
        filterVideo(content);
        return content;
    }

    private static Content filterImage(Content content) {
        if(content==null || content.getImages()==null){
            return content;
        }
        Iterator<ImageContent> iterator = content.getImages().getImages().iterator();
        while (iterator.hasNext()) {
            ImageContent imageContent = iterator.next();
            if (!imageContent.getValidStatus()) {
                iterator.remove();
            }
        }
        return content;
    }

    private static Content filterItemImage(Content content) {
        if(content==null || content.getImages()==null){
            return content;
        }
        Iterator<ImageContent> iterator = content.getImages().getImages().iterator();
        while (iterator.hasNext()) {
            ImageContent imageContent = iterator.next();
            if (!imageContent.getValidStatus() ||
                    ((imageContent.getW() != 0 && imageContent.getH() != 0) && (imageContent.getW()<100 || imageContent.getH()<100))) {
                iterator.remove();
            }
        }
        return content;
    }

    private static Content filterAudio(Content content) {
        return content;
    }

    private static Content filterVideo(Content content) {
        return content;
    }
}
