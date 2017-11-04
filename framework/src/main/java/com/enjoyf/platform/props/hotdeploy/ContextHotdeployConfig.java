package com.enjoyf.platform.props.hotdeploy;


import com.enjoyf.platform.props.EnvConfig;
import com.google.common.base.Strings;
import com.tfc.analysis.KWSeeker;
import com.tfc.analysis.entity.Keyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto: ericliu@enjoyfound.com> ericliu</a>
 */
public class ContextHotdeployConfig extends HotdeployConfig {
    private static Logger logger = LoggerFactory.getLogger(ContextHotdeployConfig.class);

    /////////////////////////////////////////////////////////
    //正则表达式列表
    private static final String KEY_CHECK_REGEX_LIST = "check.regex.list";

    //regex's value
    private static final String SUFFIX_KEY_REGEX_VALUE = ".value";
    private static final String PREFIX_KEY_REGEX = "regex.";

    /////////////////////////////////////////////////////////
    //the keys of the contents
    private static final String KEY_RICHEDITOR_BACK_LIST = "richeditor.back.list";
    private static final String KEY_RICHEDITOR_FORMULA_LIST = "richeditor.formula.list";

    private static final String KEY_SIMPLEEDITOR_BACK_LIST = "simpleeditor.back.list";
    private static final String KEY_SIMPLEEDITOR_FORMULA_LIST = "simpleeditor.formula.list";

    private static final String KEY_NICKNAME_BACK_LIST = "nickname.back.list";
    private static final String KEY_NICKNAME_FORMULA_LIST = "nickname.formula.list";

    private static final String KEY_DOMAINNAME_BACK_LIST = "domainname.back.list";
    private static final String KEY_DOMAINNAME_FORMULA_LIST = "domainname.formula.list";

    private static final String KEY_TAG_BACK_LIST = "tag.back.list";
    private static final String KEY_TAG_FORMULA_LIST = "tag.formula.list";

    private static final String KEY_SUBJECT_BACK_LIST = "subject.back.list";
    private static final String KEY_SUBJECT_FORMULA_LIST = "subject.formula.list";

    private static final String KEY_BLOGTOPIC_BACK_LIST = "blogtopic.back.list";
    private static final String KEY_BLOGTOPIC_FORMULA_LIST = "blogtopic.formula.list";

    private static final String APPURL_FORMULA_VALUE = "appurl.formula.value";

    private static final String KEY_AUTHURL_FORMULA_LIST = "authurl.formula.list";
    private static final String KEY_AUTHURL_BACK_LIST = "authurl.back.list";

    //
    private static final String KEY_CONVERT_LIST = "convert.list";
    private static final String SUFFIX_KEY_CONVERT_FETCH_REGEX = ".fetch.regex";
    private static final String SUFFIX_KEY_CONVERT_REPLACE_TEMPLATE = ".replace.template";

    private static final String KEY_CUSTOMMODULE_BACK_LIST = "custommodule.back.list";
    private static final String KEY_CUSTOMMODULE_FORMULA_LIST = "custommodule.formula.list";

    private static final String KEY_CONVERT_AT = "at";
    private static final String KEY_CONVERT_ATSYNC = "atsync";
    private static final String KEY_CONVERT_TOPIC = "topic";
    private static final String KEY_CONVERT_EMAIL = "email";
    private static final String KEY_CONVERT_URL = "url";
    private static final String KEY_CONVERT_SURL = "surl";
    private static final String KEY_CONVERT_JOYME_URL_TAG = "joymeurl";
    private static final String KEY_CONVERT_SURL_TAG = "surltag";
    private static final String KEY_CONVERT_SURL_TEXT = "surltext";
    private static final String KEY_CONVERT_HTML_TAG = "htmltag";
    private static final String KEY_ALL_HTML_TAG = "allhtmltag";
    private static final String KEY_CONVERT_POST_HTML_TAG = "posthtmltag";
    private static final String KEY_CONVERT_JOYME_TAG = "joymetag";
    private static final String KEY_CONVERT_MANY_LINE_BREAKS = "manylinkbraks";
    private static final String KEY_CONVERT_LINE_BREAKS = "linkbraks";
    private static final String KEY_CONVERT_LINE_BREAKN = "linebreakn";
    private static final String KEY_CONVERT_MANY_NBSP = "manynbsp";
    private static final String KEY_CONVERT_NBSP = "nbsp";
    private static final String KEY_CONVERT_MANY_P2BR = "p2br";
    private static final String KEY_CONVERT_MOOD = "mood";
    private static final String KEY_CONVERT_INCOMPLETE_TAG = "incompletetag";
    private static final String KEY_CONVERT_JOYME_IMAGE = "joymeimage";
    private static final String KEY_CONVERT_BAN_JOYME_IMAGE = "banjoymeimage";
    private static final String KEY_CONVERT_SIMPLE_JOYME_IMAGE = "simplejoymeimage";

    private static final String KEY_FETCH_IMGHTML = "imghtml";
    private static final String KEY_FETCH_HTMLATTR = "htmlattrfetch";

    private static final String KEY_CONVERT_JOYME_VIDEO = "joymevideo";
    private static final String KEY_CONVERT_JOYME_AUDIO = "joymeaudio";
    private static final String KEY_CONVERT_JOYME_APP = "joymeapp";
    private static final String KEY_EDIT_JOYME_IMAGE = "editimage";
    private static final String KEY_EDIT_JOYME_VIDEO = "editvideo";
    private static final String KEY_EDIT_JOYME_AUDIO = "editaudio";
    private static final String KEY_EDIT_JOYME_APP = "editapp";
    private static final String KEY_EDIT_JOYME_GAME = "editgame";

    private static final String KEY_CONVERT_MOBILE_VIDEO = "mvideo";
    private static final String KEY_CONVERT_MOBILE_AUDIO = "maudio";
    private static final String KEY_CONVERT_MOBILE_APP = "mapp";
    private static final String KEY_CONVERT_MOBILE_IMAGE = "mimage";


    private static final String KEY_REPLY_HIDE = "replyhide";
    private static final String KEY_REPLY_HIDE_LIST = "replyhidelist";
    private static final String KEY_REPLY_HIDE_BLOG = "replyhideblog";
    private static final String KEY_REPLY_DISPLAY_BLOG = "replydisplayblog";

    private static final String KEY_EDIT_JOYME_TRIM_LEFT = "trimleft";
    private static final String KEY_EDIT_JOYME_TRIM_RIGHT = "trimright";
    private static final String KEY_CHINESEBOOK = "chinesebook";

    private static final String KEY_POST_IMAGE = "postimage";
    private static final String KEY_POST_AUDIO = "postaudio";
    private static final String KEY_POST_VIDEO = "postvideo";
    private static final String KEY_POST_APP = "postapp";

    private static final String KEY_CONTENT_URL = "contenturl";

    private static final String KEY_POST_BLACK_LIST = "post.black.list";

    private static final String KADA_KEY_POST_BLACK_LIST = "kada.post.black.list";

    private ContextCache cache;

    public ContextHotdeployConfig() {
        super(EnvConfig.get().getContextHotdeployConfigureFile());
    }

    public void init() {
        logger.debug("ContextHotdeployConfig Props init start......");

        reload();
    }

    @Override
    public void reload() {
        super.reload();

        ContextCache tmp = new ContextCache();
        List<String> regexList = getList(KEY_CHECK_REGEX_LIST);

        Map<String, Pattern> patternMap = new HashMap<String, Pattern>();
        for (String regex : regexList) {
            String regexValue = getString(PREFIX_KEY_REGEX + regex + SUFFIX_KEY_REGEX_VALUE);

            if (!Strings.isNullOrEmpty(regexValue)) {
                patternMap.put(regex, Pattern.compile(regexValue, Pattern.CASE_INSENSITIVE));
            }
        }

        tmp.getCheckRegexPatternsMap().putAll(patternMap);

        //
        List<String> values = null;

        values = getList(KEY_RICHEDITOR_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_RICHEDITOR_BACK_LIST, v);
            }
        }

        values = getList(KEY_RICHEDITOR_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_RICHEDITOR_FORMULA_LIST, v);
            }
        }

        values = getList(KEY_SIMPLEEDITOR_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_SIMPLEEDITOR_BACK_LIST, v);
            }
        }

        values = getList(KEY_SIMPLEEDITOR_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_SIMPLEEDITOR_FORMULA_LIST, v);
            }
        }

        values = getList(KEY_DOMAINNAME_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_DOMAINNAME_BACK_LIST, v);
            }
        }

        values = getList(KEY_DOMAINNAME_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_DOMAINNAME_FORMULA_LIST, v);
            }
        }

        values = getList(KEY_NICKNAME_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_NICKNAME_BACK_LIST, v);
            }
        }

        values = getList(KEY_NICKNAME_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_NICKNAME_FORMULA_LIST, v);
            }
        }

        values = getList(KEY_TAG_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_TAG_BACK_LIST, v);
            }
        }

        values = getList(KEY_TAG_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_TAG_FORMULA_LIST, v);
            }
        }

        values = getList(KEY_SUBJECT_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_SUBJECT_BACK_LIST, v);
            }
        }

        values = getList(KEY_SUBJECT_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_SUBJECT_FORMULA_LIST, v);
            }
        }

        values = getList(KEY_BLOGTOPIC_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_BLOGTOPIC_BACK_LIST, v);
            }
        }

        values = getList(KEY_BLOGTOPIC_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_BLOGTOPIC_FORMULA_LIST, v);
            }
        }

        values = getList(APPURL_FORMULA_VALUE);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(APPURL_FORMULA_VALUE, v);
            }
        }

        values = getList(KEY_AUTHURL_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_AUTHURL_FORMULA_LIST, v);
            }
        }

        values = getList(KEY_AUTHURL_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_AUTHURL_BACK_LIST, v);
            }
        }

        values = getList(KEY_CUSTOMMODULE_BACK_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_CUSTOMMODULE_BACK_LIST, v);
            }
        }

        values = getList(KEY_CUSTOMMODULE_FORMULA_LIST);
        for (String v : values) {
            if (patternMap.containsKey(v)) {
                tmp.setContentRegex(KEY_CUSTOMMODULE_FORMULA_LIST, v);
            }
        }

        //the convert setting.
        List<String> convertList = this.getList(KEY_CONVERT_LIST);
        for (String convert : convertList) {
            String fetchRegex = getString(convert + SUFFIX_KEY_CONVERT_FETCH_REGEX);
            String replaceTemplate = getString(convert + SUFFIX_KEY_CONVERT_REPLACE_TEMPLATE);

            if (!Strings.isNullOrEmpty(fetchRegex)) {
                tmp.getConvertRegexPatternsMap().put(convert, Pattern.compile(fetchRegex, Pattern.DOTALL + Pattern.CASE_INSENSITIVE));
            }

            if (!Strings.isNullOrEmpty(replaceTemplate)) {
                tmp.getConvertTemplatesMap().put(convert, replaceTemplate);
            }

        }

        List<String> richEditorWordList = getList(KEY_RICHEDITOR_BACK_LIST);
        Set<String> richEditorWordSet = new HashSet<String>();
        for (String key : richEditorWordList) {
            String regexString = getString(PREFIX_KEY_REGEX + key + SUFFIX_KEY_REGEX_VALUE);
            String[] regexArray = regexString.split("\\|");
            for(String s:regexArray){
                richEditorWordSet.add(s);
            }
        }
        List<Keyword> richSeekerList = new ArrayList<Keyword>();
        for(String word:richEditorWordSet){
            richSeekerList.add(new Keyword(word));
        }
        KWSeeker seSeeker = KWSeeker.getInstance(richSeekerList);
        tmp.setRichEditorSeeker(seSeeker);

        //排重
        String postBlackWordString = getString(KEY_POST_BLACK_LIST);
        String[] postBlackArray = postBlackWordString.split("\\|");

        //todo
        Set<String> postBlackWordSet = new HashSet<String>();
        for (String s : postBlackArray) {
            postBlackWordSet.add(s);
        }

        List<Keyword> kwSeekerList = new ArrayList<Keyword>();
        for (String word : postBlackWordSet) {
            kwSeekerList.add(new Keyword(word));
        }
        KWSeeker kwSeeker = KWSeeker.getInstance(kwSeekerList);
        tmp.setPostSeeker(kwSeeker);


        //kada
        String kadaPostBlackWordString = getString(KADA_KEY_POST_BLACK_LIST);
        String[] kadapostBlackArray = kadaPostBlackWordString.split("\\|");
        Set<String> kadapostBlackWordSet = new HashSet<String>();
        for (String s : kadapostBlackArray) {
            kadapostBlackWordSet.add(s);
        }
        List<Keyword> kadakwSeekerList = new ArrayList<Keyword>();
        for (String word : kadapostBlackWordSet) {
            kadakwSeekerList.add(new Keyword(word));
        }
        KWSeeker kadakwSeeker = KWSeeker.getInstance(kadakwSeekerList);
        tmp.setKadaPostSeeker(kadakwSeeker);


        //switch the cache.
        cache = tmp;

        logger.info("ContextHotdeployConfig template Props init finished.");
    }

    public Set<Pattern> getSimpleEditorBackRegexs() {
        return queryRegexPatterns(KEY_SIMPLEEDITOR_BACK_LIST);
    }

    public Set<Pattern> getSimpleEditorFormulaRegexs() {
        return queryRegexPatterns(KEY_SIMPLEEDITOR_FORMULA_LIST);
    }

    public Set<Pattern> getRichEditorBackRegexs() {
        return queryRegexPatterns(KEY_RICHEDITOR_BACK_LIST);
    }


    public Set<Pattern> getRichEditorFormulaRegexs() {
        return queryRegexPatterns(KEY_RICHEDITOR_FORMULA_LIST);
    }

    public Set<Pattern> getNicknameBackRegexs() {
        return queryRegexPatterns(KEY_NICKNAME_BACK_LIST);
    }

    public Set<Pattern> getNicknameFormulaRegexs() {
        return queryRegexPatterns(KEY_NICKNAME_FORMULA_LIST);
    }

    public Set<Pattern> getDomainNameBackRegexs() {
        return queryRegexPatterns(KEY_DOMAINNAME_BACK_LIST);
    }

    public Set<Pattern> getDomainNameFormulaRegexs() {
        return queryRegexPatterns(KEY_DOMAINNAME_FORMULA_LIST);
    }

    public Set<Pattern> getTagBackRegexs() {
        return queryRegexPatterns(KEY_TAG_BACK_LIST);
    }

    public Set<Pattern> getTagFormulaRegexs() {
        return queryRegexPatterns(KEY_TAG_FORMULA_LIST);
    }

    public Set<Pattern> getSubjectBackRegexs() {
        return queryRegexPatterns(KEY_SUBJECT_BACK_LIST);
    }

    public Set<Pattern> getSubjectFormulaRegexs() {
        return queryRegexPatterns(KEY_SUBJECT_FORMULA_LIST);
    }

    public Set<Pattern> getBlogTopicBackRegexs() {
        return queryRegexPatterns(KEY_BLOGTOPIC_BACK_LIST);
    }

    public Set<Pattern> getBlogTopicFormulaRegexs() {
        return queryRegexPatterns(KEY_BLOGTOPIC_FORMULA_LIST);
    }

    public Set<Pattern> getAppFormulaRegexs() {
        return queryRegexPatterns(APPURL_FORMULA_VALUE);
    }

    public Set<Pattern> getAuthUrlFormulaRegexs() {
        return queryRegexPatterns(KEY_AUTHURL_FORMULA_LIST);
    }

    public Set<Pattern> getAuthUrlBackRegexs() {
        return queryRegexPatterns(KEY_AUTHURL_BACK_LIST);
    }

    public Set<Pattern> getCustomModuleFormulaRegexs() {
        return queryRegexPatterns(KEY_CUSTOMMODULE_FORMULA_LIST);
    }

    public Set<Pattern> getCustomModuleBackRegexs() {
        return queryRegexPatterns(KEY_CUSTOMMODULE_BACK_LIST);
    }


    public Pattern getAtFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_AT);
    }

    public String getAtReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_AT);
    }

    public Pattern getJoymeUrlFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_JOYME_URL_TAG);
    }

    public String getJoymeUrlReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_JOYME_URL_TAG);
    }

    public Pattern getSurlFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_SURL);
    }

    public String getSurlReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_SURL);
    }

    public Pattern getSurlTagFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_SURL_TAG);
    }

    public String getSurlTagReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_SURL_TAG);
    }

    public String getSurlTextReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_SURL_TEXT);
    }

    public Pattern getHtmlTagFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_HTML_TAG);
    }

    public Pattern getALLHtmlTagFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_ALL_HTML_TAG);
    }


    public Pattern getPostHtmlTagFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_POST_HTML_TAG);
    }


    public Pattern getJoymeTagFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_JOYME_TAG);
    }

    public Pattern getManyLineBreaksFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_MANY_LINE_BREAKS);
    }

    public String getManyLineBreaksReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_MANY_LINE_BREAKS);
    }

    public Pattern getLineBreaksFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_LINE_BREAKS);
    }

    public Pattern getLineBreakNFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_LINE_BREAKN);
    }

    public Pattern getManyNBSPFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_MANY_NBSP);
    }

    public String getManyNBSPReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_MANY_NBSP);
    }

    public Pattern getNBSPFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_NBSP);
    }

    public Pattern getP2BRFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_MANY_P2BR);
    }

    public String getP2BRReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_MANY_P2BR);
    }

    public Pattern getMoodFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_MOOD);
    }

    public Pattern getInCompleteFetchRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_INCOMPLETE_TAG);
    }

    public Pattern getFetchImgHtml() {
        return cache.getConvertRegexPatternsMap().get(KEY_FETCH_IMGHTML);
    }

    public Pattern getFetchHtmlAttr() {
        return cache.getConvertRegexPatternsMap().get(KEY_FETCH_HTMLATTR);
    }

    public Pattern getJoymeImageRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_JOYME_IMAGE);
    }

    public String getJoymeImageReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_JOYME_IMAGE);
    }

    public String getJoymeAudioReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_JOYME_AUDIO);
    }

    public String getJoymeVideoReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_JOYME_VIDEO);
    }

    public String getMobileVideoReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_MOBILE_VIDEO);
    }

    public String getMobileAudioReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_MOBILE_AUDIO);
    }

    public String getMobileAppReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_MOBILE_APP);
    }

    public String getMobileImageReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_MOBILE_IMAGE);
    }

    public String getEditImageReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_EDIT_JOYME_IMAGE);
    }


    public String getEditAudioReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_EDIT_JOYME_AUDIO);
    }


    public String getEditVideoReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_EDIT_JOYME_VIDEO);
    }

    public String getEditGameReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_EDIT_JOYME_GAME);
    }

    public Pattern getTrimLeftRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_EDIT_JOYME_TRIM_LEFT);
    }

    public Pattern getTrimRightRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_EDIT_JOYME_TRIM_RIGHT);
    }

    public String getEditAppReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_EDIT_JOYME_APP);
    }

    public String getJoymeAppReplaceNameTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_JOYME_APP);
    }

    public String getBanJoymeImageTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_BAN_JOYME_IMAGE);
    }

    public String getSimpleJoymeImageTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_SIMPLE_JOYME_IMAGE);
    }

    public Pattern getChineseBookRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CHINESEBOOK);
    }

    public String getChineseBookTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CHINESEBOOK);
    }

    public String getAtSyncReplaceTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_CONVERT_ATSYNC);
    }

    public Pattern getAtSyncRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONVERT_ATSYNC);
    }

    public Pattern getContentUrlRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_CONTENT_URL);
    }

    public Pattern getReplyHideRegex() {
        return cache.getConvertRegexPatternsMap().get(KEY_REPLY_HIDE);
    }

    public String getReplyHideListTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_REPLY_HIDE_LIST);
    }

    public String getReplyHideBlogTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_REPLY_HIDE_BLOG);
    }

    public String getReplyDisplayBlogTemplate() {
        return cache.getConvertTemplatesMap().get(KEY_REPLY_DISPLAY_BLOG);
    }

    public KWSeeker getPostSeeker() {
        return cache.getPostSeeker();
    }

    public KWSeeker getKadaPostSeeker() {
        return cache.getKadaPostSeeker();
    }

    public KWSeeker getRichEditorSeeker() {
        return cache.getRichEditorSeeker();

    }

    //todo
    private Set<Pattern> queryRegexPatterns(String key) {
        Set<Pattern> returnValue = null;

        Set<String> regexs = cache.contentRegexsMap.get(key);
        if (regexs != null) {
            returnValue = new HashSet<Pattern>();
            for (String regex : regexs) {
                Pattern p = cache.getCheckRegexPatternsMap().get(regex);

                if (p != null) {
                    returnValue.add(p);
                }
            }
        }

        return returnValue;
    }

    private class ContextCache {
        //
        private Map<String, Pattern> checkRegexPatternsMap = new HashMap<String, Pattern>();
        private Map<String, Set<String>> contentRegexsMap = new HashMap<String, Set<String>>();

        //
        private Map<String, Pattern> convertRegexPatternsMap = new HashMap<String, Pattern>();
        private Map<String, String> convertTemplatesMap = new HashMap<String, String>();

        private KWSeeker postSeeker;

        private KWSeeker kadaPostSeeker;

        private KWSeeker richEditorSeeker;

        public KWSeeker getRichEditorSeeker() {
            return richEditorSeeker;
        }

        public void setRichEditorSeeker(KWSeeker richEditorSeeker) {
            this.richEditorSeeker = richEditorSeeker;
        }

        private KWSeeker getKadaPostSeeker() {
            return kadaPostSeeker;
        }

        private void setKadaPostSeeker(KWSeeker kadaPostSeeker) {
            this.kadaPostSeeker = kadaPostSeeker;
        }

        public KWSeeker getPostSeeker() {
            return postSeeker;
        }

        public void setPostSeeker(KWSeeker postSeeker) {
            this.postSeeker = postSeeker;
        }

        public Map<String, Pattern> getCheckRegexPatternsMap() {
            return checkRegexPatternsMap;
        }

        public Map<String, Pattern> getConvertRegexPatternsMap() {
            return convertRegexPatternsMap;
        }

        public Map<String, String> getConvertTemplatesMap() {
            return convertTemplatesMap;
        }

        public Map<String, Set<String>> getContentRegexsMap() {
            return contentRegexsMap;
        }

        public void setContentRegex(String key, String regex) {
            Set<String> set = contentRegexsMap.get(key);

            if (set == null) {
                set = new HashSet<String>();
                contentRegexsMap.put(key, set);
            }

            set.add(regex);
        }

        public void clear() {

        }
    }
}
