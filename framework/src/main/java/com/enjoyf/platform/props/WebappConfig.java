package com.enjoyf.platform.props;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * webapp 属性文件
 */
public class WebappConfig {
    private static final String KEY_USER_SERVICE_URL = "user-service.cloud.url";
    private static WebappConfig instance;

    private FiveProps props;

    /*--------------    验证属性 ---------------------------------- */
    private final String ILLEGALCONTENT_KEY = "illegalcontent.list";
    private final String REGEX_EMAIL_KEY = "regex.email";
    private final String OPEN_REGISTER_KEY = "open.register";

    private Pattern patternEmail = null;
    private List<String> illegalList = new ArrayList<String>();

    /*------------------------------------------------------------*/


    /*--------------    邮件属性   ---------------------------------*/
    // 注册属性
    private static final String KEY_MAIL_REGISTER_FROM_NAME = "register.mail.fromName";
    private static final String KEY_MAIL_REGISTER_FROM_ADDR = "register.mail.fromAddr";
    private static final String KEY_MAIL_REGISTER_SUBJECT = "register.mail.subject";

    // 邀请
    private static final String KEY_MAIL_INVITE_FROM_NAME = "invite.mail.fromName";
    private static final String KEY_MAIL_INVITE_FROM_ADDR = "invite.mail.fromAddr";
    private static final String KEY_MAIL_INVITE_SUBJECT = "invite.mail.subject";

    // 验证邮箱
    private static final String KEY_MAIL_VERIFY_FROM_NAME = "verify.mail.fromName";
    private static final String KEY_MAIL_VERIFY_FROM_ADDR = "verify.mail.fromAddr";
    private static final String KEY_MAIL_VERIFY_SUBJECT = "verify.mail.subject";

    private static final String KEY_MAIL_PWD_FORGOT_FROM_NAME = "mail.pwd.forgot.fromName";
    private static final String KEY_MAIL_PWD_FORGOT_FROM_ADDR = "mail.pwd.forgot.fromAddr";

    //修改邮箱
    private static final String KEY_MAIL_MODIFY_SUBJECT = "mail.modify.subject";
    private static final String KEY_MAIL_MODIFY_FROM_NAME = "mail.modify.fromName";
    private static final String KEY_MAIL_MODIFY_FROM_ADDR = "mail.modify.fromAddr";


    private static final String KEY_CMS_API_URI = "cms.api.uri";

    private static final String KEY_DISCUZ_API_HOST = "discuz.api.host";
    private static final String KEY_DISCUZ_API_AUTHCOOKIEKEY = "discuz.api.authcookiekey";


    //验证邮箱
    private String verifyMailFromAddr = "";
    private String verifyMailFromName = "";
    private String verifyMailSubject = "";

    //注册之后，发送邮件的主题和发件人
    private String registerMailFromAddr = "";
    private String registerMailFromName = "";
    private String registerMailSubject = "";

    //注册之后，发送邮件的主题和发件人
    private String inviteMailFromAddr = "";
    private String inviteMailFromName = "";
    private String inviteMailSubject = "";

    //密码找回，发件人和邮件主题
    private String pwdForgotMailFromAddr = "";
    private String pwdForgotMailFromName = "";
    private String pwdForgotMailSubject = "";

    //修改登录邮箱，发送邮件的主题
    private String modifyMailFromAddr = "";
    private String modifyMailFromName = "";
    private String modifyMailSubject = "";

    /*------------------------------------------------------------*/

    /*------------------------------------------------------------*/
    // 注册开关
    private boolean openRegister = false;


    /*--------------    博文属性 ---------------------------------- */
    private static final String KEY_BLOG_SUBJECT_TITLE_LENGTH = "blog.subject.title.length";
    private static final String KEY_BLOG_CHAT_LENGTH = "blog.chat.length";
    private static final String KEY_BLOG_TEXT_LENGTH = "blog.text.length";
    private static final String KEY_BLOG_TEXT_PREVIEW_LENGTH = "blog.text.preview.length";
    private static final String KEY_SYNC_TEXT_LENGTH = "sync.text.length";
    private static final String KEY_BLOG_TEXT_PREVIEW_LINES = "blog.text.preview.lines";
    private static final String KEY_BLOG_TAG_NUMBER = "blog.tag.num";
    private static final String KEY_MESSAGEBODY_LENGTH = "messagebody.length";

    //search
    private static final String KEY_SEARCH_CHAT_LENGTH = "search.chat.length";
    private static final String KEY_SEARCH_SUBJECT_LENGTH = "search.subject.length";
    private static final String KEY_SEARCH_TEXT_LENGTH = "search.text.length";

    private static final String KEY_DEFAULT_TEXT_CONTENT = "default.text.content";
    private static final String KEY_HOME_PAGE_SIZE = "home.page.size";
    private static final String HOME_SCREEN_PER_PAGE = "home.screen.prepage";
    private static final String KEY_BLOG_PAGE_SIZE = "blog.page.size";
    private static final String KEY_EDITOR_BLOG_PAGE_SIZE = "editor.blog.page.size";

    private static final String KEY_FOCUS_PAGE_SIZE = "focus.page.size";
    private static final String KEY_MESSAGE_PAGE_SIZE = "message.list.pagesize";
    private static final String KEY_MESSAGE_REPLY_PAGE_SIZE = "message.reply.list.pagesize";

    private static final String KEY_BLOG_REPLY_PAGE_SIZE = "blog.reply.page.size";
    private static final String KEY_MYREPLY_PAGE_SIZE = "myreply.list.page.size";
    private static final String KEY_LIST_REPLY_PAGE_SIZE = "list.reply.page.size";

    private static final String KEY_FAVOR_CONTENT_PROFILE_PAGE_SIZE = "favor.content.profile.page.size";
    private static final String KEY_HOTCONTENT_PAGE_SIZE = "hotcontent.page.size";
    private static final String KEY_HOTCONTENT_SUBJCT_LENGTH = "hotcontent.subject.length";

    private static final String KEY_SEARCHCONTENT_PAGE_SIZE = "searchcontent.page.size";
    private static final String KEY_SEARCH_GROUP_PAGE_SIZE = "search.group.page.size";
    private static final String KEY_GUIDE_MAX_PAGE_NUM = "guide.max.page.num";
    private static final String KEY_GUIDE_PAGE_SIZE = "guide.page.size";

    private static final String KEY_GAME_TALK_PAGE_SIZE = "game.talk.page.size";
    private static final String KEY_GROUP_CONTENT_LENGTH = "group.content.length";
    private static final String KEY_TALK_BOARD_TOP_SIZE = "talk.board.top.size";

    private static final String KEY_ACTIVITY_PAGE_SIZE = "activity.page.size";

    private static final String KEY_DISCOVERY_WALL_INIT_DISPLAY_SIZE = "discovery.wall.init.display.size";
    private static final String KEY_DISCOVERY_WALL_PER_DISPLAY_SIZE = "discovery.wall.per.display.size";
    private static final String KEY_WALL_BLOCK_TEMPLATE_PATH = "wall.block.template.path";

    private static final String KEY_UPLOAD_EXTNAME_LIST = "upload.extname.list";
    private static final String KEY_UPLOAD_MAX_SIZE = "upload.max.size";
    private static final String KEY_UPLOAD_REPLY_MAX_SIZE = "upload.reply.max.size";

    private static final String KEY_REPLY_TEXT_LENGTH = "reply.text.length";

    private static final String KEY_VOTE_OPTION_MAX_SIZE = "vote.option.max.size";
    private static final String KEY_VOTE_OPTION_MIN_SIZE = "vote.option.min.size";

    private static final String KEY_GAME_DOWNLOAD_URL_PAGE_SIZE = "game.download.url.page.size";
    private static final String KEY_GAME_RELATION_GROUP_CONTENT_SIZE = "game.relation.group.content.size";
    private static final String KEY_GAMEMODULE_LIST_PAGE_SIZE = "gamemodule.list.page.size";
    private static final String KEY_GAMEMODULE_IMAGE_PAGE_SIZE = "gamemodule.image.page.size";
    private static final String KEY_GAMEMODULE_EDIT_CONTENTLIST_PAGE_SIZE = "gamemodule.edit.contentlist";
    private static final String KEY_GAMEMODULE_EDIT_IMAGELIST_PAGE_SIZE = "gamemodule.edit.imagelist";
    private static final String KEY_GAMEMODULE_HANDBOOK_NAME = "gamemodule.handbook.name";
    private static final String KEY_GAMEMENU_LINE_NAME = "gamemenu.line.name";


    private static final String KEY_INDEX_PAGE_SIZE = "index.news.pagesize";

    private static final String KEY_QINIU_HOST = "qiniu.host";

    private static final String DEFAULT_UPLOAD_BUCKET = "default.upload.bucket";

    private static final String KEY_GIFT_SITEMAP_URL = "gift.sitemap.url";
    private static final String KEY_GAME_SITEMAP_URL = "game.sitemap.url";
    private static final String KEY_SITEMAP_FOLDER = "sitemap.folder";
    private static final String KEY_VIRTUAL_USER_NAME = "virtual_user_name";


    private static final String KEY_CONTENT_SERVICE_URL = "content-service.cloud.url";
    private static final String KEY_PROFILE_SERVICE_URL = "profile-service.cloud.url";
    private static final String KEY_MESSAGE_SERVICE_URL = "message-service.cloud.url";

    private int blogSubjctTitleLength = 140;
    private int blogChatLength = 140;
    private int blogTextLength = 30000;
    private int textPreviewLength = 140;
    private int syncTextLength = 130;
    private int textPreviewLines = 10;
    private int messageBodyLength = 300;
    private int searchChatLength = 60;
    private int searchSubjectLength = 30;
    private int searchTextLength = 35;

    private int blogTagNum = 20;
    private String defaultTextDefault;
    private int homePageSize = 50;
    private int homeScreenPerPage = 3;
    private int blogPageSize = 20;
    private int editorBlogPageSize = 15;
    private int focusPageSize = 20;
    private int messageListPageSize = 20;
    private int messageReplyListPageSize = 10;

    private int blogReplyPageSize = 10;
    private int myReplyListPageSize = 20;
    //    list.reply.page.size
    private int listReplyPageSize = 5;
    private int favorContentProfilePageSize = 5;

    private int hotcontentPageSize = 30;
    private int hotContentSubjectLength = 34;

    private int searchcontentPageSize = 30;
    private int searchGroupPageSize = 15;

    private int gameTalkPageSize = 15;
    private int groupContentLength = 40;
    private int talkBoardTopSize = 10;

    private int guideMaxPageNum = 10;
    private int guidePageSize = 30;

    private int discoveryInitSize = 24;
    private int discoveryPerSize = 6;
    private String wallBlockTemplatePath = "/hotdeploy/views/ftl/wall/";

    private int activityPageSize = 10;

    private int replyTextLength = 300;

    private int voteOptionMaxSize = 20;
    private int voteOptionMinSize = 2;

    private int gameDownloadUrlPageSize = 5;
    private int gameRelationGroupContentSize = 5;
    private int gameModuleImagePageSize = 60;
    private int gameModuleListPageSize = 20;
    private int gameModuleEditContentListPageSize = 10;
    private int gameModuleEditImageListPageSize = 12;
    private String handBookName = "攻略百科";
    private String gameMenuLineName = "条目菜单";

    private int indexNewsPageSize = 6;

    /*------------------------------------------------------------*/

    /*-------------------system properties begin ---------------------------------------*/
    public String URL_TOOLS = "http://tools.joyme.com";
    public String URL_WWW = "http://www.joyme.com";
    public String DOMAIN = "joyme.com";
    public String URL_LIB = "http://lib.joyme.com";
    public String URL_STATIC = "http://static.joyme.com";
    public String URL_M = "http://m.joyme.com";
    public String URL_UC = "http://uc.joyme.com";
    public String URL_YKDOMAIN = "http://joyme.youku.com";
    private String URL_YKCOOKIEDOMAIN = ".youku.com";

    public static final String KEY_URL_TOOLS = "URL_TOOLS";
    public static final String KEY_URL_WWW = "URL_WWW";
    public static final String KEY_DOMAIN = "DOMAIN";
    public static final String KEY_URL_LIB = "URL_LIB";
    public static final String KEY_URL_STATIC = "URL_STATIC";
    public static final String KEY_URL_M = "URL_M";
    public static final String KEY_URL_UC = "URL_UC";

    public static final String KEY_URL_YKDOMAIN = "URL_YKDOMAIN";

    private String uploadRootpath = "/opt/uploads";
    private static final String KEY_UPLOAD_ROOTPATH = "upload.rootpath";
    /*----------------------system properties end --------------------------------------*/

    private static final String SYSTEM_FOCUS_USER_UNO = "system.focus.user.uno";
    private String systemUno = "";
    private static final String SYSTEM_FOCUS_USER_DOMAINS = "system.focus.user.domains";
    private List<String> systemDomains = new ArrayList<String>();

    /*----------------------scoring game begin --------------------------------------*/
    private static final String SCORING_HIDDENSHOW_RADIO = "scoring.hiddenshow.radio";
    private String hiddenshowradio = "0.3";
    /*----------------------scoring game end --------------------------------------*/

    private static final String AT_PAGE_SIZE = "at.page.size";
    private static int atPageSize = 20;
    private static final String AT_NOTICE_SIZE = "at.notice.size";
    private static int atNoticeSize = 15;
    private static final String AT_FOCUSLIST_SIZE = "at.focuslist.size";
    private static int atFocusListSize = 10;


    private int uploadMaxSize = 8192;
    private int uploadReplyMaxSize = 3072;
    private List<String> uploadExtList;

    //    userPropKeyActivityTips
    private static String userPropKeyActivityTips = "activity_tips";

    //recommend group tips
    private static final String RECOMMEND_GROUP_TIPS = "recommend_group_tips";

    private String cmsApiURL = "";

    private String discuzApiHost = "";
    private String discuzAuthCookieKey = "";

    private String qiniuHost = "http://joymepic.joyme.com";

    private String defaultUploadBucket = "joymepic";
    private String giftSitemapUrl = "http://www.joyme.com/gift/{giftId}";
    private String gameSitemapUrl = "http://www.joyme.com/collection/{anotherName}";
    private String sitemapFolder = "/opt/sitemap";

    // 用户中心微服务url
    private String userServiceUrl = "";


    private String contentServiceUrl = "";


    private String profileServiceUrl = "";

    private String messageServiceUrl="";

    private String virtualUserName;

    private MemCachedConfig memCachedConfig;

    private MemCachedManager memCachedManager;

    private WebappConfig() {
        init();
    }

    public static synchronized WebappConfig get() {
        if (instance == null) {
            instance = new WebappConfig();
        }

        return instance;
    }

    private void init() {
        props = new FiveProps(EnvConfig.get().getWebappConfigFile());

        if (props != null) {

            memCachedConfig = new MemCachedConfig(props);
            memCachedManager = new MemCachedManager(memCachedConfig);


            if (null == patternEmail) {
                patternEmail = Pattern.compile("^(\\w)+([-_.](\\w)+)*@(\\w)+([-.](\\w)+)*(\\.)(\\w)+([-.](\\w)+)*$");
            }

            illegalList = props.getList(ILLEGALCONTENT_KEY);

            openRegister = props.getBoolean(OPEN_REGISTER_KEY, openRegister);

            registerMailFromName = props.get(KEY_MAIL_REGISTER_FROM_NAME, registerMailFromName);
            registerMailFromAddr = props.get(KEY_MAIL_REGISTER_FROM_ADDR, registerMailFromAddr);
            registerMailSubject = props.get(KEY_MAIL_REGISTER_SUBJECT, registerMailSubject);

            inviteMailFromName = props.get(KEY_MAIL_INVITE_FROM_NAME, inviteMailFromName);
            inviteMailFromAddr = props.get(KEY_MAIL_INVITE_FROM_ADDR, inviteMailFromAddr);
            inviteMailSubject = props.get(KEY_MAIL_INVITE_SUBJECT, inviteMailSubject);

            pwdForgotMailFromName = props.get(KEY_MAIL_PWD_FORGOT_FROM_NAME, pwdForgotMailFromName);
            pwdForgotMailFromAddr = props.get(KEY_MAIL_PWD_FORGOT_FROM_ADDR, pwdForgotMailFromAddr);

            modifyMailFromName = props.get(KEY_MAIL_MODIFY_FROM_NAME, modifyMailFromName);
            modifyMailFromAddr = props.get(KEY_MAIL_MODIFY_FROM_ADDR, modifyMailFromAddr);
            modifyMailSubject = props.get(KEY_MAIL_MODIFY_SUBJECT, modifyMailSubject);

            verifyMailFromName = props.get(KEY_MAIL_VERIFY_FROM_NAME, verifyMailFromName);
            verifyMailFromAddr = props.get(KEY_MAIL_VERIFY_FROM_ADDR, verifyMailFromAddr);
            verifyMailSubject = props.get(KEY_MAIL_VERIFY_SUBJECT, verifyMailSubject);

            blogSubjctTitleLength = props.getInt(KEY_BLOG_SUBJECT_TITLE_LENGTH, blogSubjctTitleLength);
            blogChatLength = props.getInt(KEY_BLOG_CHAT_LENGTH, blogChatLength);
            blogTextLength = props.getInt(KEY_BLOG_TEXT_LENGTH, blogTextLength);
            textPreviewLength = props.getInt(KEY_BLOG_TEXT_PREVIEW_LENGTH, textPreviewLength);
            syncTextLength = props.getInt(KEY_SYNC_TEXT_LENGTH, textPreviewLength);
            textPreviewLines = props.getInt(KEY_BLOG_TEXT_PREVIEW_LINES, textPreviewLines);
            blogTagNum = props.getInt(KEY_BLOG_TAG_NUMBER, blogTagNum);

            messageBodyLength = props.getInt(KEY_MESSAGEBODY_LENGTH, messageBodyLength);
            messageListPageSize = props.getInt(KEY_MESSAGE_PAGE_SIZE, this.messageListPageSize);
            messageReplyListPageSize = props.getInt(KEY_MESSAGE_REPLY_PAGE_SIZE, this.messageReplyListPageSize);


            //search
            searchChatLength = props.getInt(KEY_SEARCH_CHAT_LENGTH, searchChatLength);
            searchSubjectLength = props.getInt(KEY_SEARCH_SUBJECT_LENGTH, searchSubjectLength);
            searchTextLength = props.getInt(KEY_SEARCH_TEXT_LENGTH, searchTextLength);

            homePageSize = props.getInt(KEY_HOME_PAGE_SIZE, homePageSize);
            homeScreenPerPage = props.getInt(HOME_SCREEN_PER_PAGE, homeScreenPerPage);

            blogPageSize = props.getInt(KEY_BLOG_PAGE_SIZE, this.blogPageSize);

            editorBlogPageSize = props.getInt(KEY_EDITOR_BLOG_PAGE_SIZE, editorBlogPageSize);

            focusPageSize = props.getInt(KEY_FOCUS_PAGE_SIZE, this.focusPageSize);

            blogReplyPageSize = props.getInt(KEY_BLOG_REPLY_PAGE_SIZE, this.blogReplyPageSize);

            myReplyListPageSize = props.getInt(KEY_MYREPLY_PAGE_SIZE, myReplyListPageSize);

            listReplyPageSize = props.getInt(KEY_LIST_REPLY_PAGE_SIZE, this.listReplyPageSize);
            favorContentProfilePageSize = props.getInt(KEY_FAVOR_CONTENT_PROFILE_PAGE_SIZE, favorContentProfilePageSize);

            hotcontentPageSize = props.getInt(KEY_HOTCONTENT_PAGE_SIZE, this.hotcontentPageSize);
            hotContentSubjectLength = props.getInt(KEY_HOTCONTENT_SUBJCT_LENGTH, this.hotContentSubjectLength);

            searchcontentPageSize = props.getInt(KEY_SEARCHCONTENT_PAGE_SIZE, this.searchcontentPageSize);
            searchGroupPageSize = props.getInt(KEY_SEARCH_GROUP_PAGE_SIZE, this.searchGroupPageSize);

            guideMaxPageNum = props.getInt(KEY_GUIDE_MAX_PAGE_NUM, this.guideMaxPageNum);
            guidePageSize = props.getInt(KEY_GUIDE_PAGE_SIZE, this.guidePageSize);
//            searchGamePageSzie = props.getInt(KEY_SEARCH_GAME_PAGE_SIZE, this.searchGamePageSzie);
            gameTalkPageSize = props.getInt(KEY_GAME_TALK_PAGE_SIZE, this.gameTalkPageSize);
            groupContentLength = props.getInt(KEY_GROUP_CONTENT_LENGTH, this.groupContentLength);

            defaultTextDefault = props.get(KEY_DEFAULT_TEXT_CONTENT);

            discoveryInitSize = props.getInt(KEY_DISCOVERY_WALL_INIT_DISPLAY_SIZE, discoveryInitSize);
            discoveryPerSize = props.getInt(KEY_DISCOVERY_WALL_PER_DISPLAY_SIZE, discoveryPerSize);
            wallBlockTemplatePath = props.get(KEY_WALL_BLOCK_TEMPLATE_PATH, wallBlockTemplatePath);

            // 域名常量
            URL_TOOLS = props.get(KEY_URL_TOOLS, URL_TOOLS);
            URL_WWW = props.get(KEY_URL_WWW, URL_WWW);
            DOMAIN = props.get(KEY_DOMAIN, DOMAIN);
            URL_LIB = props.get(KEY_URL_LIB, URL_LIB);
            URL_STATIC = props.get(KEY_URL_STATIC);
            URL_M = props.get(KEY_URL_M);
            URL_UC = props.get(KEY_URL_UC, URL_UC);

            URL_YKDOMAIN = props.get(KEY_URL_YKDOMAIN, URL_YKDOMAIN);

            String[] arrayYKDOMAIN = URL_YKDOMAIN.split("\\.");
            if (arrayYKDOMAIN.length >= 2) {
                URL_YKCOOKIEDOMAIN = new StringBuffer(".").append(arrayYKDOMAIN[arrayYKDOMAIN.length - 2]).append(".").append(arrayYKDOMAIN[arrayYKDOMAIN.length - 1]).toString();
            }


            systemUno = props.get(SYSTEM_FOCUS_USER_UNO);

            //
            systemDomains = props.getList(SYSTEM_FOCUS_USER_DOMAINS);

            //游戏打分机率事件
            hiddenshowradio = props.get(SCORING_HIDDENSHOW_RADIO, hiddenshowradio);
            // 用户上传目录
            uploadRootpath = props.get(KEY_UPLOAD_ROOTPATH, uploadRootpath);

            uploadMaxSize = props.getInt(KEY_UPLOAD_MAX_SIZE, uploadMaxSize);
            uploadReplyMaxSize = props.getInt(KEY_UPLOAD_REPLY_MAX_SIZE, uploadReplyMaxSize);

            uploadExtList = props.getList(KEY_UPLOAD_EXTNAME_LIST);

//            blogPreviewTextLength = props.getInt(KEY_BLOG_PREIVIEW_TEXT_LENGTH, blogPreviewTextLength);

            atNoticeSize = props.getInt(AT_NOTICE_SIZE, atNoticeSize);
            atPageSize = props.getInt(AT_PAGE_SIZE, atPageSize);
            atFocusListSize = props.getInt(AT_FOCUSLIST_SIZE, atFocusListSize);

//            favoriteTagSize = props.getInt(KEY_HABIT_FAVORITE_TAG_SIZE, favoriteTagSize);

            talkBoardTopSize = props.getInt(KEY_TALK_BOARD_TOP_SIZE, talkBoardTopSize);

            activityPageSize = props.getInt(KEY_ACTIVITY_PAGE_SIZE, activityPageSize);

            replyTextLength = props.getInt(KEY_REPLY_TEXT_LENGTH, replyTextLength);

            voteOptionMaxSize = props.getInt(KEY_VOTE_OPTION_MAX_SIZE, voteOptionMaxSize);
            voteOptionMinSize = props.getInt(KEY_VOTE_OPTION_MIN_SIZE, voteOptionMinSize);

            gameDownloadUrlPageSize = props.getInt(KEY_GAME_DOWNLOAD_URL_PAGE_SIZE, gameDownloadUrlPageSize);
            gameRelationGroupContentSize = props.getInt(KEY_GAME_RELATION_GROUP_CONTENT_SIZE, gameRelationGroupContentSize);
            gameModuleListPageSize = props.getInt(KEY_GAMEMODULE_LIST_PAGE_SIZE, gameModuleListPageSize);
            gameModuleImagePageSize = props.getInt(KEY_GAMEMODULE_IMAGE_PAGE_SIZE, gameModuleImagePageSize);
            gameModuleEditContentListPageSize = props.getInt(KEY_GAMEMODULE_EDIT_CONTENTLIST_PAGE_SIZE, gameModuleEditContentListPageSize);
            gameModuleEditImageListPageSize = props.getInt(KEY_GAMEMODULE_EDIT_IMAGELIST_PAGE_SIZE, gameModuleEditImageListPageSize);
            handBookName = props.get(KEY_GAMEMODULE_HANDBOOK_NAME, handBookName);
            gameMenuLineName = props.get(KEY_GAMEMENU_LINE_NAME, gameMenuLineName);
//            gameRecentLineName = props.get(KEY_GAMERECENT_LINE_NAME, gameRecentLineName);
//            gameHeadLineLineName = props.get(KEY_GAMEHEADLINE_LINE_NAME, gameHeadLineLineName);

            indexNewsPageSize = props.getInt(KEY_INDEX_PAGE_SIZE, indexNewsPageSize);

            cmsApiURL = props.get(KEY_CMS_API_URI);

            discuzApiHost = props.get(KEY_DISCUZ_API_HOST);
            discuzAuthCookieKey = props.get(KEY_DISCUZ_API_AUTHCOOKIEKEY);

            qiniuHost = props.get(KEY_QINIU_HOST, qiniuHost);
            defaultUploadBucket = props.get(DEFAULT_UPLOAD_BUCKET, defaultUploadBucket);

            giftSitemapUrl = props.get(KEY_GIFT_SITEMAP_URL, giftSitemapUrl);
            gameSitemapUrl = props.get(KEY_GAME_SITEMAP_URL, gameSitemapUrl);
            sitemapFolder = props.get(KEY_SITEMAP_FOLDER, sitemapFolder);
            virtualUserName = props.get(KEY_VIRTUAL_USER_NAME);
            userServiceUrl = props.get(KEY_USER_SERVICE_URL, "");

            contentServiceUrl = props.get(KEY_CONTENT_SERVICE_URL, "");


            profileServiceUrl =props.get(KEY_PROFILE_SERVICE_URL,"");

            messageServiceUrl =props.get(KEY_MESSAGE_SERVICE_URL,"");
        }
    }

    /////////////////////////////////////////////////////////////////
    public FiveProps getProps() {
        return props;
    }

    public Pattern getPatternEmail() {
        return patternEmail;
    }

    public String getVerifyMailFromName() {
        return verifyMailFromName;
    }

    public String getVerifyMailFromAddr() {
        return verifyMailFromAddr;
    }

    public String getVerifyMailSubject() {
        return verifyMailSubject;
    }

    public String getRegisterMailFromName() {
        return registerMailFromName;
    }

    public String getRegisterMailFromAddr() {
        return registerMailFromAddr;
    }

    public String getRegisterMailSubject() {
        return registerMailSubject;
    }

    public String getInviteMailFromAddr() {
        return inviteMailFromAddr;
    }

    public String getInviteMailFromName() {
        return inviteMailFromName;
    }

    public String getInviteMailSubject() {
        return inviteMailSubject;
    }

    public String getPwdForgotMailFromAddr() {
        return pwdForgotMailFromAddr;
    }

    public String getPwdForgotMailFromName() {
        return pwdForgotMailFromName;
    }

    public String getPwdForgotMailSubject() {
        return pwdForgotMailSubject;
    }

    public String getModifyMailFromName() {
        return modifyMailFromName;
    }

    public String getModifyMailFromAddr() {
        return modifyMailFromAddr;
    }

    public String getModifyMailSubject() {
        return modifyMailSubject;
    }

    public boolean isOpenRegister() {
        return openRegister;
    }

    public int getBlogChatLength() {
        return blogChatLength;
    }

    public int getBlogTextLength() {
        return blogTextLength;
    }

    public int getTextPreviewLength() {
        return textPreviewLength;
    }

    public int getTextPreviewLines() {
        return textPreviewLines;
    }

    public int getBlogTagNum() {
        return blogTagNum;
    }

    public int getMessageBodyLength() {
        return messageBodyLength;
    }

    public int getSearchChatLength() {
        return searchChatLength;
    }

    public int getSearchSubjectLength() {
        return searchSubjectLength;
    }

    public int getSearchTextLength() {
        return searchTextLength;
    }

    public String getDefaultTextDefault() {
        return defaultTextDefault;
    }

    public int getHomePageSize() {
        return this.homePageSize;
    }

    public String getWallBlockTemplatePath() {
        return wallBlockTemplatePath;
    }

    public int getGameTalkPageSize() {
        return gameTalkPageSize;
    }

    public String getUrlTools() {
        return URL_TOOLS;
    }

    public String getUrlWww() {
        return URL_WWW;
    }

    public String getContentUrlPrefix() {
        return URL_WWW + "/note/";
    }

    public String getDomain() {
        return DOMAIN;
    }

    public String getUrlLib() {
        return URL_LIB;
    }

    public String getUrlStatic() {
        return URL_STATIC;
    }

    public String getUrlM() {
        return URL_M;
    }

    public String getUrlUc() {
        return URL_UC;
    }

    public String getUrlYouku() {
        return URL_YKDOMAIN;
    }

    public String getURL_YKCOOKIEDOMAIN() {
        return URL_YKCOOKIEDOMAIN;
    }

    public String getUploadRootpath() {
        return uploadRootpath;
    }

    public String getSystemUno() {
        return systemUno;
    }

    public List<String> getSystemDomains() {
        return systemDomains;
    }

    public String getHiddenshowradio() {
        return hiddenshowradio;
    }

    public int getUploadMaxSize() {
        return uploadMaxSize;
    }

    public int getUploadReplyMaxSize() {
        return uploadReplyMaxSize;
    }

    public List<String> getUploadExtList() {
        return uploadExtList;
    }

    public int getAtNoticeSize() {
        return atNoticeSize;
    }

    public int getAtPageSize() {
        return atPageSize;
    }

    public int getBlogPageSize() {
        return blogPageSize;
    }

    public int getEditorBlogPageSize() {
        return editorBlogPageSize;
    }

    public int getFocusPageSize() {
        return focusPageSize;
    }

    public int getMessageListPageSize() {
        return messageListPageSize;
    }

    public int getMessageReplyListPageSize() {
        return messageReplyListPageSize;
    }

    public int getBlogReplyPageSize() {
        return blogReplyPageSize;
    }

    public int getMyReplyListPageSize() {
        return myReplyListPageSize;
    }

    public int getListReplyPageSize() {
        return listReplyPageSize;
    }

    public int getHotcontentPageSize() {
        return hotcontentPageSize;
    }

    public int getHotContentSubjectLength() {
        return hotContentSubjectLength;
    }

    public int getSearchcontentPageSize() {
        return searchcontentPageSize;
    }

    public int getSearchGroupPageSize() {
        return searchGroupPageSize;
    }

    public int getGuideMaxPageNum() {
        return guideMaxPageNum;
    }

    public int getGuidePageSize() {
        return guidePageSize;
    }

    public int getSyncTextLength() {
        return syncTextLength;
    }

    public int getHomeScreenPerPage() {
        return homeScreenPerPage;
    }

    public int getTalkBoardTopSize() {
        return talkBoardTopSize;
    }

    public int getFavorContentProfilePageSize() {
        return favorContentProfilePageSize;
    }

    public int getActivityPageSize() {
        return activityPageSize;
    }

    public int getReplyTextLength() {
        return replyTextLength;
    }

    public int getVoteOptionMaxSize() {
        return voteOptionMaxSize;
    }

    public int getVoteOptionMinSize() {
        return voteOptionMinSize;
    }

    public int getGameDownloadUrlPageSize() {
        return gameDownloadUrlPageSize;
    }

    public int getGameRelationGroupContentSize() {
        return gameRelationGroupContentSize;
    }

    public String getUserPropKeyActivityTips() {
        return userPropKeyActivityTips;
    }

    public String getRecommendGroupTips() {
        return RECOMMEND_GROUP_TIPS;
    }

    public int getIndexNewsPageSize() {
        return indexNewsPageSize;
    }

    public int getGameModuleImagePageSize() {
        return gameModuleImagePageSize;
    }

    public int getGameModuleListPageSize() {
        return gameModuleListPageSize;
    }

    public int getGameModuleEditContentListPageSize() {
        return gameModuleEditContentListPageSize;
    }

    public int getGameModuleEditImageListPageSize() {
        return gameModuleEditImageListPageSize;
    }

    public String getHandBookName() {
        return handBookName;
    }

    public int getGroupContentLength() {
        return groupContentLength;
    }

    public String getGameMenuLineName() {
        return gameMenuLineName;
    }

    public String getCmsApiURL() {
        return cmsApiURL;
    }

    public String getDiscuzApiHost() {
        return discuzApiHost;
    }

    public String getDiscuzAuthCookieKey() {
        return discuzAuthCookieKey;
    }

    public String getQiniuHost() {
        return qiniuHost;
    }

    public String getDefaultUploadBucket() {
        return defaultUploadBucket;
    }

    public String getGiftSitemapUrl() {
        return giftSitemapUrl;
    }

    public String getGameSitemapUrl() {
        return gameSitemapUrl;
    }

    public String getSitemapFolder() {
        return sitemapFolder;
    }

    public String getVirtualUserName() {
        return virtualUserName;
    }

    public void setVirtualUserName(String virtualUserName) {
        this.virtualUserName = virtualUserName;
    }

    public MemCachedManager getMemCacheManager() {
        return memCachedManager;
    }


    public String getUserServiceUrl() {
        return userServiceUrl;
    }

    public void setUserServiceUrl(String userServiceUrl) {
        this.userServiceUrl = userServiceUrl;
    }

    public String getContentServiceUrl() {
        return contentServiceUrl;
    }

    public String getProfileServiceUrl() {
        return profileServiceUrl;
    }

    public String getMessageServiceUrl() {
        return messageServiceUrl;
    }
}
