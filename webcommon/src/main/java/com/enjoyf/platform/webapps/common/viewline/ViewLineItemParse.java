package com.enjoyf.platform.webapps.common.viewline;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.SubStringTextProcessor;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-14
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class ViewLineItemParse {

    public static Content parseItemContent(Content content) {
        if (content == null) {
            return null;
        }

        return content;
    }

    // format search content
    public static Content formatSearchContent(Content content) {
        if (!StringUtil.isEmpty(content.getContent())) {
            String contentText = content.getContentType().hasText() ? processContentByKey(WordProcessorKey.KEY_SEARCH_PREVIEW_TEXT, content) : processContentByKey(WordProcessorKey.KEY_SEARCH_PREVIEW_CHAT, content);
            content.setContent(contentText);
        }

        if (!StringUtil.isEmpty(content.getSubject())) {
            ResolveContent resolveContent = new ResolveContent();
            resolveContent.setContent(content.getSubject());
            SubStringTextProcessor subStringTextProcessor = new SubStringTextProcessor(WebappConfig.get().getSearchSubjectLength(), "...", 1);
            subStringTextProcessor.process(resolveContent);
            content.setSubject(resolveContent.getContent());
        }

        if (StringUtil.isEmpty(content.getSubject())) {
            //分享
            content.setSubject("分享文章");
            if (content.getContentType().hasAudio()) {
                content.setSubject("分享音乐");
            }
            if (content.getContentType().hasVideo()) {
                content.setSubject("分享视频");
            }
            if (content.getContentType().hasImage()) {
                content.setSubject("分享图片");
            }
            if (content.getContentType().hasApp()) {
                content.setSubject("分享APP");
            }

        }

        return content;
    }

    //
    // format item content
    public static Content formatItemContent(Content content, int displaySubjectLength) {

        if (content.getContentType().hasPhrase()) {
            if (!StringUtil.isEmpty(content.getContent())) {
                ResolveContent resolveContent = new ResolveContent();
                resolveContent.setContent(content.getContent());
                SubStringTextProcessor subStringTextProcessor = new SubStringTextProcessor(displaySubjectLength, "...", 1);
                subStringTextProcessor.process(resolveContent);
                content.setContent(resolveContent.getContent());
                String contentText = processContentByKey(WordProcessorKey.KEY_DISCOVERY_SUBJECT, content);
                content.setContent(contentText);
            }

            if (StringUtil.isEmpty(content.getContent())) {
                //分享
                content.setContent("分享文章");
                if (content.getContentType().hasAudio()) {
                    content.setContent("分享音乐");
                }
                if (content.getContentType().hasVideo()) {
                    content.setContent("分享视频");
                }
                if (content.getContentType().hasImage()) {
                    content.setContent("分享图片");
                }
                if (content.getContentType().hasApp()) {
                    content.setContent("分享APP");
                }
            }
        } else if (content.getContentType().hasText()) {
            if (!StringUtil.isEmpty(content.getContent())) {
                String contentText = processContentByKey(WordProcessorKey.KEY_SEARCH_PREVIEW_TEXT, content);
                content.setContent(contentText);
            }

            if (!StringUtil.isEmpty(content.getSubject())) {
                ResolveContent resolveContent = new ResolveContent();
                resolveContent.setContent(content.getSubject());
                SubStringTextProcessor subStringTextProcessor = new SubStringTextProcessor(displaySubjectLength, "...", 1);
                subStringTextProcessor.process(resolveContent);
                content.setSubject(resolveContent.getContent());
            }

            if (StringUtil.isEmpty(content.getSubject())) {
                //分享
                content.setSubject("分享文章");
                if (content.getContentType().hasAudio()) {
                    content.setSubject("分享音乐");
                }
                if (content.getContentType().hasVideo()) {
                    content.setSubject("分享视频");
                }
                if (content.getContentType().hasImage()) {
                    content.setSubject("分享图片");
                }
                if (content.getContentType().hasApp()) {
                    content.setSubject("分享APP");
                }
            }
        }


        return content;
    }

    private static String processContentByKey(WordProcessorKey key, Content content) {
        ResolveContent resolveContent = ResolveContent.transferByContent(content);
        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(key);
        if (textProcessor != null) {
            resolveContent = textProcessor.process(resolveContent);
        }
        return resolveContent.getContent();
    }

    public static String getDefaultContent(Content content) {
        String contentText = "";

        int mediaCount=0;
        if (StringUtil.isEmpty(content.getContent())) {
            //分享
            contentText="分享文章";
            if (content.getContentType().hasAudio()) {
               contentText="分享音乐";
                mediaCount++;
            }
            if (content.getContentType().hasVideo()) {
                contentText="分享视频";
                mediaCount++;
            }
            if (content.getContentType().hasImage()) {
               contentText="分享图片";
                mediaCount++;
            }
            if (content.getContentType().hasApp()) {
               contentText="分享APP";
                mediaCount++;
            }

            if(mediaCount>1){
                contentText="分享";
            }
        }

        return contentText;
    }


}
