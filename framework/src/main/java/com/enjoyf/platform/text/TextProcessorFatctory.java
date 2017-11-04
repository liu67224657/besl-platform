package com.enjoyf.platform.text;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.ContextHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.text.processor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:文章解析的工厂类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class TextProcessorFatctory {
    private static Logger logger = LoggerFactory.getLogger(TextProcessorFatctory.class);

    Map<WordProcessorKey, TextProcessor> contentProcessorMap = new HashMap<WordProcessorKey, TextProcessor>();

    private static TextProcessorFatctory instance;

    private TextProcessorFatctory() {
        init();
    }

    public synchronized static TextProcessorFatctory get() {
        if (instance == null) {
            instance = new TextProcessorFatctory();
        }
        return instance;
    }

    private void init() {
        contentProcessorMap.put(WordProcessorKey.KEY_PRIVIEW_TEXT, textPriviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_SIMPLE_TEXT, simpleTextPriviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_INDEX_TEXT, indexTextPriviewProcessor());

        contentProcessorMap.put(WordProcessorKey.KEY_REMOVE_BREAKLINE_CHAT, allSimpleChatProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_REMOVE_BREAKLINE_TEXT, allSimpleTextProcessor());

        contentProcessorMap.put(WordProcessorKey.KEY_PRIVIEW_CHAT, chatPrivewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_SIMPLE_CHAT, simpleChatPriviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_DISCOVERY_CHAT, discoveryPriviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_DISCOVERY_SUBJECT, discoverySubjectProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_ALL_TEXT_HIDE, allTextHideProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_ALL_TEXT_DISPLAY, allTextDisplayProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_ALL_CHAT, allChatProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_EDIT_TEXT, editTextProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_EDIT_CHAT, editChatProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_PRIVIEW_MESSAGE, privateMessageProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_PRIVIEW_REPLAY, replyPriviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_PRIVIEW_REPLY_AFTER_POST, replyAfterPostPriviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_SIMPLE_REPLAY_CONTENT, simpleReplyContentProcessor());

        contentProcessorMap.put(WordProcessorKey.KEY_MOBILE_ALL_TEXT, allMobileTextProcessor());

        contentProcessorMap.put(WordProcessorKey.KEY_POST_CHAT, postChatProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_POST_TEXT, postTextProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_POST_FORWARD, postForwardProcessor());

        contentProcessorMap.put(WordProcessorKey.KEY_POST_REPLY, postReplyProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_SYNC_CONTENT, syncContentProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_SYNC_RELATION_CONTENT, syncRelationContentProcessor());
        // --- iso ------
        contentProcessorMap.put(WordProcessorKey.KEY_IOS_TIMELINE_PRIVIEW_TEXT, textPriviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_IOS_TIMELINE_TEXT, iosContentProcessor());

        //search
        contentProcessorMap.put(WordProcessorKey.KEY_SEARCH_PREVIEW_TEXT, searchTextPreviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_SEARCH_PREVIEW_CHAT, searchChatPreviewProcessor());
        contentProcessorMap.put(WordProcessorKey.KEY_ONLY_TEXT, onlyTextProcessor());
    }


    public TextProcessor getProcessorByKey(WordProcessorKey key) {
        if (!contentProcessorMap.containsKey(key)) {
            if (logger.isDebugEnabled()) {
                logger.debug("getProcessorByKey key is not exists.:" + key);
                return null;
            }
        }
        return contentProcessorMap.get(key);
    }

    //todo 去掉html hide joymetag 等标签合并换行标签
    private static TextProcessor textPriviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getManyLineBreaksReplaceTemplate()));
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getTextPreviewLength(), "...", WebappConfig.get().getTextPreviewLines()));
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor simpleTextPriviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getTextPreviewLength(), "...", WebappConfig.get().getTextPreviewLines()));
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor indexTextPriviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(""));
        chainProcessor.addProcessor(new RemoveSpaceProcessor());
        chainProcessor.addProcessor(new RemoveLineBreakProcessor());
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new RemoveUncomplateTagProcessor());

        return chainProcessor;
    }

    private static TextProcessor chatPrivewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
//        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new MerageLineBreaksProcessor("\n"));
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor simpleChatPriviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
//        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor simpleReplyContentProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getTextPreviewLength(), "...", WebappConfig.get().getTextPreviewLines()));
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor discoveryPriviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getManyLineBreaksReplaceTemplate()));
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor discoverySubjectProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));
        return chainProcessor;
    }

    //todo hide
    private static TextProcessor allTextHideProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new ReplyHideTemplateProcessor());
        chainProcessor.addProcessor(new JoymeMediaTemplateProcessor());
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor allTextDisplayProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new ReplyDisplayTemplateProcessor());
        chainProcessor.addProcessor(new JoymeMediaTemplateProcessor());
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor allMobileTextProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new ReplyHideTemplateProcessor());
        chainProcessor.addProcessor(new JoymeMobileMediaTemplateProcessor());
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor allSimpleTextProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new ConvertSimpleJoymeImageProcessor());
        chainProcessor.addProcessor(new JoymeMediaTemplateProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor allChatProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
//        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor allSimpleChatProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor editTextProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new RecoverJoymeMediaProcessor());
        chainProcessor.addProcessor(new RecoverSurlProcessor());
        return chainProcessor;
    }

    private static TextProcessor editChatProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new HtmlUnEscapeProcessor());
        chainProcessor.addProcessor(new RecoverSurlProcessor());
        return chainProcessor;
    }

    private static TextProcessor privateMessageProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new HtmlEscapeProcessor());
        chainProcessor.addProcessor(new MoodyProcessor());
        return chainProcessor;
    }

    private static TextProcessor replyAfterPostPriviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new RemoveMoodyProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(""));
        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getTextPreviewLength(), "...", WebappConfig.get().getTextPreviewLines()));
        chainProcessor.addProcessor(new MoodyProcessor());
        return chainProcessor;
    }

    private static TextProcessor replyPriviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        //空格
        chainProcessor.addProcessor(new TrimProcessor());
        //html
        chainProcessor.addProcessor(new HtmlEscapeProcessor());
        //表情
        chainProcessor.addProcessor(new MoodyProcessor());
        //楼中楼@符号
        //chainProcessor.addProcessor(new ConvertAtProcessor());
        //j0y.me  短链
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        return chainProcessor;
    }

    //post
    private static TextProcessor postChatProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new PostSurlProcessor());
        chainProcessor.addProcessor(new HtmlEscapeProcessor());
        chainProcessor.addProcessor(new PostHtmlProcessor("\n"));
        return chainProcessor;
    }

    //todo
    private static TextProcessor postTextProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new MerageLineBreakNProcessor(""));
        chainProcessor.addProcessor(new PostMediaProcessor());
        chainProcessor.addProcessor(new HasReplyHideProcessor());
        chainProcessor.addProcessor(new PostSurlProcessor());
        chainProcessor.addProcessor(new PostHtmlProcessor());
        return chainProcessor;
    }

    private static TextProcessor postForwardProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new PostSurlProcessor());
        chainProcessor.addProcessor(new HtmlEscapeProcessor());
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        return chainProcessor;
    }

    private static TextProcessor postReplyProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new PostSurlProcessor());
        chainProcessor.addProcessor(new HtmlEscapeProcessor());
//        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getReplyTextLength(),"",-1));
        return chainProcessor;
    }

    private static TextProcessor syncContentProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new ConvertAtSyncProcessor());
        chainProcessor.addProcessor(new HtmlUnEscapeProcessor());
        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getSyncTextLength(), "", WebappConfig.get().getTextPreviewLines()));
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));

        return chainProcessor;
    }

    private static TextProcessor syncRelationContentProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(""));
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new HtmlUnEscapeProcessor());
        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getTextPreviewLength(), "...", WebappConfig.get().getTextPreviewLines()));
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));

        return chainProcessor;
    }

    //todo
    private static TextProcessor searchTextPreviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveMediaTagProcessor());
        chainProcessor.addProcessor(new MerageSpaceProcessor(""));
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(" "));
        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getSearchTextLength(), "...", 1));
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTagReplaceTemplate()));
        chainProcessor.addProcessor(new TrimProcessor());
        return chainProcessor;
    }

    private static TextProcessor searchChatPreviewProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new MerageLineBreaksProcessor(" "));
        chainProcessor.addProcessor(new SubStringTextProcessor(WebappConfig.get().getSearchChatLength(), "...", 1));
        chainProcessor.addProcessor(new MoodyProcessor());
        chainProcessor.addProcessor(new ConvertAtProcessor());
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor iosContentProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new RemoveHtmlProcessor());
        chainProcessor.addProcessor(new TrimProcessor());
        chainProcessor.addProcessor(new HtmlUnEscapeProcessor());
        chainProcessor.addProcessor(new MerageLineBreaksProcessor("\n"));
        chainProcessor.addProcessor(new ConvertSurlProcessor(HotdeployConfigFactory.get().getConfig(ContextHotdeployConfig.class).getSurlTextReplaceTemplate()));
        return chainProcessor;
    }

    private static TextProcessor onlyTextProcessor() {
        ChainProcessor chainProcessor = new ChainProcessor();
        chainProcessor.addProcessor(new IngoreReplyHideProcessor());
        chainProcessor.addProcessor(new ReplyHideListTemplateProcessor());
        chainProcessor.addProcessor(new RemoveAllHtmlProcessor());
        return chainProcessor;
    }

}

