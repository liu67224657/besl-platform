package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.social.InviteMailProvider;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.html.HeadMenuGroup;
import com.enjoyf.platform.util.html.HeadMenuProps;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class WebHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(WebHotdeployConfig.class);

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String KEY_HOT_GAME_LIST = "hot.game.list";

    private static final String KEY_HOT_TAG_LIST = "hot.tag.list";
    private static final String KEY_HOT_UPDATE_RATE_STEP_NUM = "hot.update.rate.step.num";

    private static final String KEY_GUIDE_RECOMMEND_USER_SIZE = "guide.recommend.user.size";
    private static final String KEY_RECOMMEND_USER_LIST = "recommend.user.list";
    private static final String RECOMMEND_SUFFIX_TIPS = ".tips";

    private static final String KEY_INVITE_MAIL_PROVIDER = "invite.mail.provider";

    //the page view configure keys
    private static final String KEY_PAGEVIEW_SUPPORT = "web.pageview.support";
    private static final String KEY_ADVERTISE_CLICK_SUPPORT = "web.advertise.click.support";

    private static final String KEY_SJCS_TALENT_SHARE_UNO = "sjcs.talent.share.uno";

    private static final String KEY_INDEX_TALENT_UNO = "index.talent.uno";

    private static final String KEY_GAME_SUPER_PRIVACY_UNO = "game.super.privacy.uno";

    private static final String LIST_SOCIAL_RECOMEND_USER = "social.recommend.user.list";

    //the content view times configures keys
    private static final String KEY_CONTENT_VIEW_TIMES_INCREASE_IPS = "content.view.times.increase.ips";
    private static final String KEY_CONTENT_VIEW_TIMES_INC_START = "content.view.times.inc.start";
    private static final String KEY_CONTENT_VIEW_TIMES_INC_END = "content.view.times.inc.end";
    private static final String KEY_CONTENT_VIEW_TIMES_DEF_START = "content.view.times.def.start";
    private static final String KEY_CONTENT_VIEW_TIMES_DEF_END = "content.view.times.def.end";

    private static final String KEY_CONTENT_SUBJECT_EMPTY_TEXT = "content.subject.empty.text";


    private static final String KEY_FETCH_INDEX_URL = "fetch.index.url";
    private static final String KEY_FETCH_MOBILE_INDEX_URL = "fetch.mobile.index.url";

    private static final String KEY_KADA_ACTIVITY_IDS = "kada.activity.ids";
    private static final String KEY_KADA_ACTIVITY_CONTENT_LIST = ".kada.activity.content.list";
    private static final String KEY_KADA_GUEST_UNO = "kada.guest.uno";

    private static final String KEY_USERLOGIN_TRIGGER = "userlogin.trigger";
    private static final String KEY_GIFTMARKET_TRIGGER = "giftmarket.trigger";
    private static final String KEY_COLLECTION_TRIGGER = "game.collection.trigger";
    private static final String KEY_USERCENTER_PROFILEKEY = "usercenter.profilekey";
    private static final String KEY_SEARCH_URL = "search.url";
    private static final String KEY_SEARCH_AAVE_URL = "search.save.url";
    private static final String KEY_SEARCH_DELETE_URL = "search.delete.url";

    private static final String AINFO_ADWALKER_CN = "ainfo.adwalker.cn";

    private static final String COMMENT_REPLY_NON_ESCAPE_HTML = "comment.reply.non.escape.html";

    private static final String JOYME_WIKI_HOST = "joyme.wikipage.wiki.host";
    private static final String JOYME_WWW_HOST = "joyme.www.host";
    private static final String JOYME_WIKI_WIKI_LIST = "joyme.wiki.wiki.list";
    private static final String JOYME_WWW_WIKI_LIST = "joyme.www.wiki.list";
    private static final String JOYME_SUBKEY_WIKI_LIST = "joyme.subkey.wiki.list";
    private static final String GAMEDB_PUBLIC_TIPS_MAIL = "gamedb.public.tips.mail";

    private ReadCache readCache;
    private Random random = new Random();
    private String commentReplyNonEscapeHtml;
    private String wikiHost;

    public WebHotdeployConfig() {
        super(EnvConfig.get().getWebHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    @Override
    public synchronized void reload() {
        super.reload();

        List<String> gameLists = getList(KEY_HOT_GAME_LIST);
        List<String> tagLists = getList(KEY_HOT_TAG_LIST);
        List<String> recommendLists = getList(KEY_RECOMMEND_USER_LIST);
        String sjcsTalentShareUno = getString(KEY_SJCS_TALENT_SHARE_UNO);
        String contentSubjectEmptyText = getString(KEY_CONTENT_SUBJECT_EMPTY_TEXT);

        int stepNum = getInt(KEY_HOT_UPDATE_RATE_STEP_NUM, 10);
        int recommendUserSizeNum = getInt(KEY_GUIDE_RECOMMEND_USER_SIZE, 10);
        Map<String, String> recommendMap = new HashMap<String, String>();
        for (String code : recommendLists) {
            String tips = getString(code + RECOMMEND_SUFFIX_TIPS);
            recommendMap.put(code, tips);
        }

        List<String> mailProviderCodes = getList(KEY_INVITE_MAIL_PROVIDER);
        Map<String, InviteMailProvider> inviteMailProviderMap = new LinkedHashMap<String, InviteMailProvider>();
        for (String providerCode : mailProviderCodes) {
            if (InviteMailProvider.getByCode(providerCode) != null) {
                inviteMailProviderMap.put(providerCode, InviteMailProvider.getByCode(providerCode));
            }
        }

        boolean pageViewSupport = getBoolean(KEY_PAGEVIEW_SUPPORT, false);
        boolean advertiseClickSupport = getBoolean(KEY_ADVERTISE_CLICK_SUPPORT, false);

        String indexTalentUno = getString(KEY_INDEX_TALENT_UNO);
        List<String> gameSuperPrivacyUno = getList(KEY_GAME_SUPER_PRIVACY_UNO);

        //
        String contentViewTimesIpsRegex = getString(KEY_CONTENT_VIEW_TIMES_INCREASE_IPS);
        int contentViewTimesStart = getInt(KEY_CONTENT_VIEW_TIMES_INC_START, 1);
        int contentViewTimesEnd = getInt(KEY_CONTENT_VIEW_TIMES_INC_END, 1);
        int contentViewTimesDefStart = getInt(KEY_CONTENT_VIEW_TIMES_DEF_START, 1);
        int contentViewTimesDefEnd = getInt(KEY_CONTENT_VIEW_TIMES_DEF_END, 1);

        //
        ReadCache tmpCache = new ReadCache();
        tmpCache.setHotGameList(gameLists);
        tmpCache.setHotTagList(tagLists);
        tmpCache.setRateUpdateStepNum(stepNum);
        tmpCache.setRecommendUserSize(recommendUserSizeNum);
        tmpCache.setRecommendMap(recommendMap);
        tmpCache.setInviteMailProviderMap(inviteMailProviderMap);
        tmpCache.setSjcsTalentShareUno(sjcsTalentShareUno);
        tmpCache.setIndexTalentUno(indexTalentUno);
        tmpCache.setGameSuperPrivacyUnoList(gameSuperPrivacyUno);

        //
        tmpCache.setPageViewSupport(pageViewSupport);
        tmpCache.setAdvertiseClickSupport(advertiseClickSupport);

        tmpCache.setContentSubjectEmptyText(contentSubjectEmptyText);

        String fecherIndexUrl = getString(KEY_FETCH_INDEX_URL);
        tmpCache.setFetcherIndexUrl(fecherIndexUrl);

        String fecherMobileIndexUrl = getString(KEY_FETCH_MOBILE_INDEX_URL);
        tmpCache.setFetcherMobileIndexUrl(fecherMobileIndexUrl);

        //
        if (!Strings.isNullOrEmpty(contentViewTimesIpsRegex)) {
            tmpCache.setContentViewTimesIpsPattern(Pattern.compile(contentViewTimesIpsRegex));
            tmpCache.setContentViewTimesIncStart(contentViewTimesStart);
            tmpCache.setContentViewTimesIncEnd(contentViewTimesEnd);
        }

        tmpCache.setContentViewTimesDefStart(contentViewTimesDefStart);
        tmpCache.setContentViewTimesDefEnd(contentViewTimesDefEnd);

        List<String> socialRecommendList = getList(LIST_SOCIAL_RECOMEND_USER);
        tmpCache.setSocialRecommendUnoList(socialRecommendList);

        Map<Long, List<String>> kadaAMap = new HashMap<Long, List<String>>();
        List<String> kadaAlist = getList(KEY_KADA_ACTIVITY_IDS);
        for (String aid : kadaAlist) {
            List<String> kadaAContentList = getList(aid + KEY_KADA_ACTIVITY_CONTENT_LIST);

            if (!CollectionUtil.isEmpty(kadaAContentList)) {
                try {
                    kadaAMap.put(Long.parseLong(aid), kadaAContentList);
                } catch (NumberFormatException e) {
                }
            }
        }

        tmpCache.setKadaActivityMap(kadaAMap);

        String kadaGuestUno = getString(KEY_KADA_GUEST_UNO);
        tmpCache.setKadaGuestUno(kadaGuestUno);

        tmpCache.setProfileKeyList(getList(KEY_USERCENTER_PROFILEKEY));
        tmpCache.setUserLoginTrigger(getBoolean(KEY_USERLOGIN_TRIGGER, false));
        tmpCache.setGiftmarketTrigger(getBoolean(KEY_GIFTMARKET_TRIGGER, false));
        tmpCache.setCollectionTrigger(getBoolean(KEY_COLLECTION_TRIGGER, false));
        tmpCache.setSearchUrl(getString(KEY_SEARCH_URL, "http://search.joyme.com/search/query.do"));
        tmpCache.setSearchSaveUrl(getString(KEY_SEARCH_AAVE_URL, "http://search.joyme.com/search/save.do"));
        tmpCache.setSearchDeteleUrl(getString(KEY_SEARCH_DELETE_URL, "http://search.joyme.com/search/delete.do"));

        tmpCache.setAinfoAdwalkercn(getString(AINFO_ADWALKER_CN));

        //reply non escape
        String commentReplyNonEscapeHtml = getString(COMMENT_REPLY_NON_ESCAPE_HTML);
        tmpCache.setCommentReplyNonEscapeHtml(commentReplyNonEscapeHtml);
        //
        String wikiHost = getString(JOYME_WIKI_HOST);
        tmpCache.setWikiHost(wikiHost);
        String wwwHost = getString(JOYME_WWW_HOST);
        tmpCache.setWwwHost(wwwHost);
        List<String> wikiWikiList = getList(JOYME_WIKI_WIKI_LIST);
        tmpCache.setWikiWikiList(wikiWikiList);
        List<String> wwwWikiList = getList(JOYME_WWW_WIKI_LIST);
        tmpCache.setWwwWikiList(wwwWikiList);
        List<String> mail = getList(GAMEDB_PUBLIC_TIPS_MAIL);
        tmpCache.setTipsMail(mail);
        this.readCache = tmpCache;

        logger.info("Event Props init finished.");
    }

    private class ReadCache {
        //
        private List<String> hotGameList = new ArrayList<String>();
        private List<String> hotTagList = new ArrayList<String>();
        private int rateUpdateStepNum = 10;
        private int recommendUserSize = 10;
        private Map<String, InviteMailProvider> inviteMailProviderMap = new LinkedHashMap<String, InviteMailProvider>();
        private Map<String, String> recommendMap = new HashMap<String, String>();
        private String sjcsTalentShareUno = "";
        private String indexTalentUno = "";
        private List<String> gameSuperPrivacyUnoList = new ArrayList<String>();


        //the pageview configure.
        private boolean pageViewSupport = false;
        private boolean advertiseClickSupport = false;

        //the content view times.
        private Pattern contentViewTimesIpsPattern;
        private int contentViewTimesIncStart = 1;
        private int contentViewTimesIncEnd = 1;
        private int contentViewTimesDefStart = 1;
        private int contentViewTimesDefEnd = 1;

        private String contentSubjectEmptyText = "";

        private String fetcherIndexUrl = "";
        private String fetcherMobileIndexUrl = "";

        private String kadaGuestUno;


        private List<String> profileKeyList;
        private boolean userLoginTrigger;
        private String searchUrl;
        private String searchSaveUrl;
        private String searchDeteleUrl;
        private String ainfoAdwalkercn;

        private boolean giftmarketTrigger;
        private boolean collectionTrigger;

        private String commentReplyNonEscapeHtml;

        private String wikiHost;
        private List<String> wikiWikiList;
        private List<String> wwwWikiList;
        private String wwwHost;
        private List<String> tipsMail;

        public List<String> getProfileKeyList() {
            return profileKeyList;
        }

        public void setProfileKeyList(List<String> profileKeyList) {
            this.profileKeyList = profileKeyList;
        }

        public boolean getUserLoginTrigger() {
            return userLoginTrigger;
        }

        public void setUserLoginTrigger(boolean userLoginTrigger) {
            this.userLoginTrigger = userLoginTrigger;
        }

        public boolean isGiftmarketTrigger() {
            return giftmarketTrigger;
        }

        public void setGiftmarketTrigger(boolean giftmarketTrigger) {
            this.giftmarketTrigger = giftmarketTrigger;
        }

        public boolean isCollectionTrigger() {
            return collectionTrigger;
        }

        public void setCollectionTrigger(boolean collectionTrigger) {
            this.collectionTrigger = collectionTrigger;
        }

        public String getSearchUrl() {
            return searchUrl;
        }

        public void setSearchUrl(String searchUrl) {
            this.searchUrl = searchUrl;
        }

        private List<HeadMenuProps> defaultMenuList;

        private List<String> socialRecommendUnoList;

//        private Map<String, HeadMenuGroup> headMenuGroupMap;


        private Map<Long, List<String>> kadaActivityMap;

        private Map<Long, List<String>> getKadaActivityMap() {
            return kadaActivityMap;
        }

        private void setKadaActivityMap(Map<Long, List<String>> kadaActivityMap) {
            this.kadaActivityMap = kadaActivityMap;
        }

        private String getFetcherMobileIndexUrl() {
            return fetcherMobileIndexUrl;
        }

        private void setFetcherMobileIndexUrl(String fetcherMobileIndexUrl) {
            this.fetcherMobileIndexUrl = fetcherMobileIndexUrl;
        }

        public List<String> getSocialRecommendUnoList() {
            return socialRecommendUnoList;
        }

        public void setSocialRecommendUnoList(List<String> socialRecommendUnoList) {
            this.socialRecommendUnoList = socialRecommendUnoList;
        }

        public List<String> getHotGameList() {
            return hotGameList;
        }

        public void setHotGameList(List<String> hotGameList) {
            this.hotGameList = hotGameList;
        }

        public List<String> getHotTagList() {
            return hotTagList;
        }

        public void setHotTagList(List<String> hotTagList) {
            this.hotTagList = hotTagList;
        }

        public int getRateUpdateStepNum() {
            return rateUpdateStepNum;
        }

        public void setRateUpdateStepNum(int rateUpdateStepNum) {
            this.rateUpdateStepNum = rateUpdateStepNum;
        }

        public int getRecommendUserSize() {
            return recommendUserSize;
        }

        public void setRecommendUserSize(int recommendUserSize) {
            this.recommendUserSize = recommendUserSize;
        }

        public Map<String, String> getRecommendMap() {
            return recommendMap;
        }

        public void setRecommendMap(Map<String, String> recommendMap) {
            this.recommendMap = recommendMap;
        }

        public Map<String, InviteMailProvider> getInviteMailProviderMap() {
            return inviteMailProviderMap;
        }

        public void setInviteMailProviderMap(Map<String, InviteMailProvider> inviteMailProviderMap) {
            this.inviteMailProviderMap = inviteMailProviderMap;
        }

        public boolean isPageViewSupport() {
            return pageViewSupport;
        }

        public void setPageViewSupport(boolean pageViewSupport) {
            this.pageViewSupport = pageViewSupport;
        }

        public boolean isAdvertiseClickSupport() {
            return advertiseClickSupport;
        }

        public void setAdvertiseClickSupport(boolean advertiseClickSupport) {
            this.advertiseClickSupport = advertiseClickSupport;
        }

        public String getSjcsTalentShareUno() {
            return sjcsTalentShareUno;
        }

        public void setSjcsTalentShareUno(String sjcsTalentShareUno) {
            this.sjcsTalentShareUno = sjcsTalentShareUno;
        }

        public String getIndexTalentUno() {
            return indexTalentUno;
        }

        public void setIndexTalentUno(String indexTalentUno) {
            this.indexTalentUno = indexTalentUno;
        }

        public List<String> getGameSuperPrivacyUnoList() {
            return gameSuperPrivacyUnoList;
        }

        public void setGameSuperPrivacyUnoList(List<String> gameSuperPrivacyUnoList) {
            this.gameSuperPrivacyUnoList = gameSuperPrivacyUnoList;
        }

        public Pattern getContentViewTimesIpsPattern() {
            return contentViewTimesIpsPattern;
        }

        public void setContentViewTimesIpsPattern(Pattern contentViewTimesIpsPattern) {
            this.contentViewTimesIpsPattern = contentViewTimesIpsPattern;
        }

        public int getContentViewTimesIncStart() {
            return contentViewTimesIncStart;
        }

        public void setContentViewTimesIncStart(int contentViewTimesIncStart) {
            this.contentViewTimesIncStart = contentViewTimesIncStart;
        }

        public int getContentViewTimesIncEnd() {
            return contentViewTimesIncEnd;
        }

        public void setContentViewTimesIncEnd(int contentViewTimesIncEnd) {
            this.contentViewTimesIncEnd = contentViewTimesIncEnd;
        }

        public int getContentViewTimesDefStart() {
            return contentViewTimesDefStart;
        }

        public void setContentViewTimesDefStart(int contentViewTimesDefStart) {
            this.contentViewTimesDefStart = contentViewTimesDefStart;
        }

        public int getContentViewTimesDefEnd() {
            return contentViewTimesDefEnd;
        }

        public void setContentViewTimesDefEnd(int contentViewTimesDefEnd) {
            this.contentViewTimesDefEnd = contentViewTimesDefEnd;
        }

        public String getContentSubjectEmptyText() {
            return contentSubjectEmptyText;
        }

        public void setContentSubjectEmptyText(String contentSubjectEmptyText) {
            this.contentSubjectEmptyText = contentSubjectEmptyText;
        }

        public String getFetcherIndexUrl() {
            return fetcherIndexUrl;
        }

        public void setFetcherIndexUrl(String fetcherIndexUrl) {
            this.fetcherIndexUrl = fetcherIndexUrl;
        }

        private String getKadaGuestUno() {
            return kadaGuestUno;
        }

        private void setKadaGuestUno(String kadaGuestUno) {
            this.kadaGuestUno = kadaGuestUno;
        }

        public String getAinfoAdwalkercn() {
            return ainfoAdwalkercn;
        }

        public void setAinfoAdwalkercn(String ainfoAdwalkercn) {
            this.ainfoAdwalkercn = ainfoAdwalkercn;
        }

        public String getCommentReplyNonEscapeHtml() {
            return commentReplyNonEscapeHtml;
        }

        public void setCommentReplyNonEscapeHtml(String commentReplyNonEscapeHtml) {
            this.commentReplyNonEscapeHtml = commentReplyNonEscapeHtml;
        }

        public String getWikiHost() {
            return wikiHost;
        }

        public void setWikiHost(String wikiHost) {
            this.wikiHost = wikiHost;
        }

        public void setWikiWikiList(List<String> wikiWikiList) {
            this.wikiWikiList = wikiWikiList;
        }

        public List<String> getWikiWikiList() {
            return wikiWikiList;
        }

        public void setWwwWikiList(List<String> wwwWikiList) {
            this.wwwWikiList = wwwWikiList;
        }

        public List<String> getWwwWikiList() {
            return wwwWikiList;
        }

        public String getWwwHost() {
            return wwwHost;
        }

        public void setWwwHost(String wwwHost) {
            this.wwwHost = wwwHost;
        }

        public List<String> getTipsMail() {
            return tipsMail;
        }

        public void setTipsMail(List<String> tipsMail) {
            this.tipsMail = tipsMail;
        }

        public String getSearchSaveUrl() {
            return searchSaveUrl;
        }

        public void setSearchSaveUrl(String searchSaveUrl) {
            this.searchSaveUrl = searchSaveUrl;
        }

        public String getSearchDeteleUrl() {
            return searchDeteleUrl;
        }

        public void setSearchDeteleUrl(String searchDeteleUrl) {
            this.searchDeteleUrl = searchDeteleUrl;
        }
    }

    public String getSearchSaveUrl() {
        return readCache.getSearchSaveUrl();
    }


    public String getSearchDeleteUrl() {
        return readCache.getSearchDeteleUrl();
    }

    public List<String> getHotTagList() {
        return readCache.getHotTagList();
    }

    public List<String> getHotGameList() {
        return readCache.getHotGameList();
    }

    public int getRateUpdateStepNum() {
        return readCache.getRateUpdateStepNum();
    }

    public Map<String, String> getRecommendMap() {
        return readCache.getRecommendMap();
    }

    public Map<String, InviteMailProvider> getInviteMailProviderMap() {
        return readCache.getInviteMailProviderMap();
    }

    public int getRecommendUserSizeNum() {
        return readCache.getRecommendUserSize();
    }


    public boolean isPageViewSupport() {
        return readCache.isPageViewSupport();
    }

    public boolean isAdvertiseClickSupport() {
        return readCache.isAdvertiseClickSupport();
    }

    public String getSjcsTalentShareUno() {
        return readCache.getSjcsTalentShareUno();
    }

    public List<String> getGameSuperPrivacyUnoList() {
        return readCache.getGameSuperPrivacyUnoList();
    }

    public String getContentSubjectEmptyText() {
        return readCache.getContentSubjectEmptyText();
    }

    public String getFetcherIndexUrl() {
        return readCache.getFetcherIndexUrl();
    }

    public String getFetcherMobileIndexUrl() {
        return readCache.getFetcherMobileIndexUrl();
    }

    public int getContentViewTimes(String ip) {
        int returnValue = 1;

        if (readCache.getContentViewTimesIpsPattern() != null && !Strings.isNullOrEmpty(ip) && readCache.getContentViewTimesIpsPattern().matcher(ip).find()) {
            if ((readCache.getContentViewTimesIncEnd() - readCache.getContentViewTimesIncStart()) > 0) {
                returnValue = readCache.getContentViewTimesIncStart() + random.nextInt(readCache.getContentViewTimesIncEnd() - readCache.getContentViewTimesIncStart() + 1);
            } else {
                returnValue = readCache.getContentViewTimesIncStart();
            }
        } else {
            returnValue = getContentViewTimesDefault();
        }

        return returnValue < 1 ? 1 : returnValue;
    }

    public List<String> getSocialRecommendUnoList() {
        return readCache.getSocialRecommendUnoList();
    }

    public Map<Long, List<String>> getKadaAMap() {
        return readCache.getKadaActivityMap();
    }

    public String getKadaGuestUno() {
        return readCache.getKadaGuestUno();
    }

    private int getContentViewTimesDefault() {
        int returnValue = 1;

        if ((readCache.getContentViewTimesDefEnd() - readCache.getContentViewTimesDefStart()) > 0) {
            returnValue = readCache.getContentViewTimesDefStart() + random.nextInt(readCache.getContentViewTimesDefEnd() - readCache.getContentViewTimesDefStart() + 1);
        } else {
            returnValue = readCache.getContentViewTimesDefStart();
        }

        return returnValue;
    }


    public List<String> getProfileKeyList() {
        return readCache.getProfileKeyList();
    }

    public boolean getGiftmakretTrigger() {
        return readCache.isGiftmarketTrigger();
    }

    public boolean getCollectionTrigger() {
        return readCache.isCollectionTrigger();
    }

    public boolean getUserLoginTrigger() {
        return readCache.getUserLoginTrigger();
    }

    public String getSearchUrl() {
        return readCache.getSearchUrl();
    }

    public String getAinfoAdwalkercn() {
        return readCache.getAinfoAdwalkercn();
    }

    public String getCommentReplyNonEscapeHtml() {
        return readCache.getCommentReplyNonEscapeHtml();
    }

    public String getWikiHost() {
        return readCache.getWikiHost();
    }

    public List<String> getWikiWikiList() {
        return readCache.getWikiWikiList();
    }

    public List<String> getWwwWikiList() {
        return readCache.getWwwWikiList();
    }

    public String getWwwHost() {
        return readCache.getWwwHost();
    }

    public List<String> getTipsMail() {
        return readCache.getTipsMail();
    }


}
