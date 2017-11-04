package com.enjoyf.platform.props.hotdeploy;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <a href="mailto:taijunli@staff.com">Li Taijun</a>
 */
public class TemplateHotdeployConfig extends HotdeployConfig {
    //
    private static final Logger logger = LoggerFactory.getLogger(TemplateHotdeployConfig.class);

    ////////////////////////////////////////////////////////////////
    private static final String KEY_CONTENT_GAME_FTL_URI = "content.game.ftl.uri";
    private static final String KEY_CONTENT_HOTNEWS_FTL_URI = "content.hotnews.ftl.uri";
    private static final String KEY_CONTENT_RECOMMEND_FTL_URI = "content.recommend.ftl.uri";

    private static final String KEY_INDEX_FINDGAME_FTL_URI = "index.findgame.ftl.uri";
    private static final String KEY_INDEX_ASSESSMENT_FTL_URI = "index.assessment.ftl.uri";
    private static final String KEY_INDEX_HOANDBOOK_FTL_URI = "index.handbook.ftl.uri";
    private static final String KEY_INDEX_HOTNEWS_FTL_URI = "index.hotnews.ftl.uri";
    private static final String KEY_INDEX_TOPIC_FTL_URI = "index.topic.ftl.uri";
    private static final String KEY_INDEX_GROUP_FTL_URI = "index.group.ftl.uri";
    private static final String KEY_INDEX_RECOMMEND_FTL_URL = "index.recommend.ftl.uri";
    private static final String KEY_INDEX_WIKI_FTL_URL = "index.wiki.ftl.uri"; //todo notuse
    private static final String KEY_INDEX_PREOGRESS_FTL_URI = "index.progress.ftl.uri";
    private static final String KEY_INDEX_WIKIMODULE_FTL_URI = "index.wikimodule.ftl.uri";

    private static final String KEY_GAME_PAGE_HEADLINE_TFL_URI = "game.headline.ftl.uri";
    private static final String KEY_GAME_PAGE_GAMEWIKI_TFL_URI = "game.gamewiki.ftl.uri";
    private static final String KEY_GAME_PAGE_RECOMMENDGAME_TFL_URI = "game.recommendgame.ftl.uri";
    private static final String KEY_GAME_PAGE_BULLETIN_TFL_URI = "game.bulletin.ftl.uri";

    private static final String KEY_GAME_PAGE_DANGJI_TFL_URI = "game.dangji.ftl.uri";
    private static final String KEY_GAME_PAGE_ZHANGJI_TFL_URI = "game.zhangji.ftl.uri";
    private static final String KEY_GAME_PAGE_MMO_TFL_URI = "game.mmo.ftl.uri";
    private static final String KEY_GAME_PAGE_MOBILE_TFL_URI = "game.mobile.ftl.uri";
    private static final String KEY_GAME_PAGE_NEWLIST_TFL_URI = "game.newlist.ftl.uri";
    private static final String KEY_GAME_PAGE_CHANGELIST_TFL_URI = "game.changelist.ftl.uri";
    private static final String KEY_GAME_PAGE_WIKI_FTL_URI = "game.wiki.ftl.uri";

    private static final String KEY_MAIL_INVITE_FTL_URI = "mail.invite.ftl.uri";
    private static final String KEY_MAIL_MODIFY_FTL_URI = "mail.modify.ftl.uri";
    private static final String KEY_MAIL_PWDFORGOT_FTL_URI = "mail.pwdforgot.ftl.uri";
    private static final String KEY_MAIL_REGISTER_FTL_URI = "mail.register.ftl.uri";
    private static final String KEY_MAIL_VERIFY_FTL_URI = "mail.verify.ftl.uri";
    private static final String KEY_CHINA_JOY_2013_FTL_URL = "mail.cj2013.ftl.uri";
    private static final String KEY_CHINA_JOY_2013_FTL_SUBJECT = "mail.cj2013.ftl.subject";

    private static final String KEY_ACTIVITY_RECOMMEND_FTL_URI = "activity.recommend.ftl.uri";
    private static final String KEY_ACTIVITY_BULLETIN_FTL_URI = "activity.bulletin.ftl.uri";

    private static final String KEY_MAIL_BATCHCODE_FTL_URI = "mail.batchcode.ftl.uri";

    private static final String KEY_BAIKE_SYSTEM_MESSAGE_TEMPLATE = "baike.system.message.template";
    private static final String KEY_GAMECOVER_SYSTEM_MESSAGE_TEMPLATE = "gamecover.system.message.template";
    private static final String KEY_ESS_SYSTEM_MESSAGE_TEMPLATE = "ess.system.message.template";
    private static final String KEY_MAGAZINE_SYSTEM_MESSAGE_TEMPLATE = "magazine.system.message.template";
    private static final String KEY_BAIKE_NOTCREATEUSER_SYSTEM_MESSAGE_TEMPLATE = "baike.notcreateuser.system.message.template";
    private static final String KEY_ESS_NOTCREATEUSER_SYSTEM_MESSAGE_TEMPLATE = "ess.notcreateuser.system.message.template";
    private static final String KEY_INDEX_SYSTEM_MESSAGE_TEMPLATE = "index.system.message.template";
    private static final String KEY_GAME_INVITE_SYSTEM_MESSAGE_TEMPLATE = "game.invite.system.message.template";

    private static final String KEY_VOTE_POST_MESSAGE_TEMPLATE = "vote.post.content.template";
    private static final String KEY_VOTE_PART_MESSAGE_TEMPLATE = "vote.part.content.template";

    private static final String KEY_POST_MODULEDEF_MESSAGE_TEMPLATE = "game.post.default.message.template";
    private static final String KEY_POST_CMODULE_MESSAGE_TEMPLATE = "game.post.moudle.message.template";
    private static final String KEY_POST_CMODULE_SUCCESS_MESSAGE_TEMPLATE = "game.post.moudle.success.message.template";

    private static final String KEY_GOODS_EXCHANGED_SUCCESS_V_TEMPLATE = "goods.exchenged.success.v.template";
    private static final String KEY_GOODS_EXCHANGED_SUCCESS_G_TEMPLATE = "goods.exchanged.success.g.template";

    private static final String KEY_ADMIN_ADJUST_POINT_TEMPLATE = "admin.adjust.point.template";

    private static final String KEY_LOTTERY_AWARD_V_TEMPLATE = "lottery.award.v.template";
    private static final String KEY_LOTTERY_AWARD_G_TEMPLATE = "lottery.award.g.template";
    private static final String KEY_LOTTERY_AWARD_P_TEMPLATE = "lottery.award.p.template";
    private static final String KEY_EMAIL_GIFT_GETCODE_SUCCESS = "email.gift.getcode.success";
    private static final String KEY_GAME_NEW_RELEASE_SUBMIT = "gameres.newgame.submit.notice";
    private static final String KEY_GAME_NEW_RELEASE_SUBMIT_FTL_URL = "mail.game.newrelease.submit.ftl.uri";
    private static final String KEY_GAME_NEW_RELEASE_SUBMIT_FTL_SUBJECT = "mail.game.newrelease.submit.ftl.subject";

    private static final String KEY_GAME_NEW_RELEASE_PASS = "gameres.newgame.pass.notice";
    private static final String KEY_GAME_NEW_RELEASE_PASS_FTL_URL = "mail.game.newrelease.pass.ftl.uri";
    private static final String KEY_GAME_NEW_RELEASE_PASS_FTL_SUBJECT = "mail.game.newrelease.pass.ftl.subject";
    private static final String KEY_JOIN_USER_GROUP_TEMPLATE = "join.user.group.template";
    private static final String KEY_REJECTED_JOIN_GROUP_TEMPLATE = "rejected.join.group.template";

    private static final String KEY_EXCHANGE_POINT_ACTION_HISTORY_TEMPLATE = "point.exchange.action.history.description";
    private static final String KEY_SHARE_POINT_ACTION_HISTORY_TEMPLATE = "point.share.action.history.description";
    private static final String KEY_GROUP_POINT_ACTION_HISTORY_TEMPLATE = "point.group.action.history.description";
    private static final String KEY_GIFTMARKET_LOGS_TEMPLATE = "giftmarket.logs.template";

    private static final String KEY_MOBILE_MESSAGE_GET_VALID_CONTENT = "mobile.message.get.valid.content";

    private static final String KEY_MOBILE_MESSAGE_GET_PC_VALID_CONTENT = "mobile.message.getpc.valid.content";

    private static final String KEY_GIFT_MARKET_SEND_CODE_TO_MOBILE = "point.giftmarket.send.code.to.mobile.message";

    private static final String KEY_CJ2014_LOTTERY_AWARD_TEMPLATE = "cj2014.lottery.award.notice.body";
    private static final String KEY_CJ2014_LOTTERY_AWARD_ITEM_TEMPLATE = "cj2014.lottery.award.item.notice.body";

    //邮件
    private static final String contnetGameFtlUri = "/hotdeploy/joymetemplate/freemarker/content/game.ftl";
    private static final String contentHotnewsFtlUri = "/hotdeploy/joymetemplate/freemarker/content/contenthotnews.ftl";
    private static final String contentRecommendFtlUri = "/hotdeploy/joymetemplate/freemarker/content/recommend.ftl";


    private static final String indexFindGameFtlUri = "/hotdeploy/joymetemplate/freemarker/index/findgame.ftl";
    private static final String indexAssesmentFtlUri = "/hotdeploy/joymetemplate/freemarker/index/assessment.ftl";
    private static final String indexHandbookFtlUri = "/hotdeploy/joymetemplate/freemarker/index/handbook.ftl";
    private static final String indexHotNewsFtlUri = "/hotdeploy/joymetemplate/freemarker/index/hotnews.ftl";
    private static final String indexTopicFtlUri = "/hotdeploy/joymetemplate/freemarker/index/topic.ftl";
    private static final String indexGroupFtlUri = "/hotdeploy/joymetemplate/freemarker/index/group.ftl";
    private static final String indexRecommendFtlUri = "/hotdeploy/joymetemplate/freemarker/index/recommend.ftl";
    private static final String indexWikiFtlUri = "/hotdeploy/joymetemplate/freemarker/index/wiki.ftl";

    private static final String indexProgressFtlUri = "/hotdeploy/joymetemplate/freemarker/index/progress.ftl";

    private static final String indexWikiModuleFtlUri = "/hotdeploy/joymetemplate/freemarker/index//wikimodule.ftl";


    private static final String gamePageHeadLineFtlUri = "/hotdeploy/joymetemplate/freemarker/game/headline.ftl";
    private static final String gamePageGameWikiFtlUri = "/hotdeploy/joymetemplate/freemarker/game/gamewiki.ftl";
    private static final String gamePageRecommendGameFtlUri = "/hotdeploy/joymetemplate/freemarker/game/recommendgame.ftl";
    private static final String gamePageBulletinFtlUri = "/hotdeploy/joymetemplate/freemarker/game/bulletin.ftl";

    private static final String gamePageDanjiFtlUri = "/hotdeploy/joymetemplate/freemarker/game/danji.ftl";
    private static final String gamePageZhangjiFtlUri = "/hotdeploy/joymetemplate/freemarker/game/zhangji.ftl";
    private static final String gamePageMmoFtlUri = "/hotdeploy/joymetemplate/freemarker/game/mmo.ftl";
    private static final String gamePageMobileFtlUri = "/hotdeploy/joymetemplate/freemarker/game/mobile.ftl";
    private static final String gamePageNewListFtlUri = "/hotdeploy/joymetemplate/freemarker/game/newlist.ftl";
    private static final String gamePageChangeListFtlUri = "/hotdeploy/joymetemplate/freemarker/game/changelist.ftl";
    private static final String gamePageWikiFtlUri = "/hotdeploy/joymetemplate/freemarker/game/wiki.ftl";

    private static final String mailInviteFtl = "/hotdeploy/joymetemplate/freemarker/mail/mailinvite.ftl";
    private static final String mailModifyFtlUri = "/hotdeploy/joymetemplate/freemarker/mail/mailmodify.ftl";
    private static final String mailPwdForgetFtlUri = "/hotdeploy/joymetemplate/freemarker/mail/mailpwdforgot.ftl";
    private static final String mailRegisterFtlUri = "/hotdeploy/joymetemplate/freemarker/mail/mailregister.ftl";
    private static final String mailVerifyFtlUri = "/hotdeploy/joymetemplate/freemarker/mail/mailverify.ftl";
    private static final String mailChinajoyFtlUri = "/hotdeploy/joymetemplate/freemarker/mail/cj2013.ftl";
    private static final String mailChinajoyFtlSubject = "/hotdeploy/joymetemplate/freemarker/mail/cj2013.ftl";

    private static final String mailBatchCodeFtlUri = "/hotdeploy/joymetemplate/freemarker/mail/mailbatchcode.ftl";

    private static final String activityRecommendFtlUri = "/hotdeploy/joymetemplate/freemarker/activity/recommend.ftl";
    private static final String activityBulltinFtlUri = "/hotdeploy/joymetemplate/freemarker/activity/bulletin.ftl";

    private static final String mailNewRealeseSubmitFtlUri = "/hotdeploy/joymetemplate/freemarker/mail/newreleasesubmit.ftl";
    private static final String mailNewRealeseSubmitFtlSubject = "/hotdeploy/joymetemplate/freemarker/mail/newreleasesubmit.ftl";

    private static final String mailNewRealesePassFtlUri = "/hotdeploy/joymetemplate/freemarker/mail/newreleasepass.ftl";
    private static final String mailNewRealesePassFtlSubject = "/hotdeploy/joymetemplate/freemarker/mail/newreleasepass.ftl";


    private static final String giftMarketLogsFtlUri = "/hotdeploy/joymetemplate/freemarker/giftmarket/addlogs.ftl";


    private TemplatePropsCache templatePropsCache;


    public TemplateHotdeployConfig() {
        super(EnvConfig.get().getTemplateHotdeployConfigureFile());
    }

    @Override
    public void init() {
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        //
        TemplatePropsCache tempCache = new TemplatePropsCache();

        tempCache.setContentGameFtlUri(getString(KEY_CONTENT_GAME_FTL_URI, contnetGameFtlUri));
        tempCache.setContentHotNewsFtlUri(getString(KEY_CONTENT_HOTNEWS_FTL_URI, contentHotnewsFtlUri));
        tempCache.setContentRecommendFtlUri(getString(KEY_CONTENT_RECOMMEND_FTL_URI, contentRecommendFtlUri));


        tempCache.setIndexFindGameFtlUri(getString(KEY_INDEX_FINDGAME_FTL_URI, indexFindGameFtlUri));
        tempCache.setIndexAssessmentFtlUri(getString(KEY_INDEX_ASSESSMENT_FTL_URI, indexAssesmentFtlUri));
        tempCache.setIndexHandbookFtlUri(getString(KEY_INDEX_HOANDBOOK_FTL_URI, indexHandbookFtlUri));
        tempCache.setIndexHotNewsFtlUri(getString(KEY_INDEX_HOTNEWS_FTL_URI, indexHotNewsFtlUri));
        tempCache.setIndexTopicFtlUri(getString(KEY_INDEX_TOPIC_FTL_URI, indexTopicFtlUri));
        tempCache.setIndexGroupFtlUri(getString(KEY_INDEX_GROUP_FTL_URI, indexGroupFtlUri));
        tempCache.setIndexRecommendFtlUri(getString(KEY_INDEX_RECOMMEND_FTL_URL, indexRecommendFtlUri));
        tempCache.setIndexWikiFtlUri(getString(KEY_INDEX_WIKI_FTL_URL, indexWikiFtlUri));
        tempCache.setIndexProgressFtlUri(getString(KEY_INDEX_PREOGRESS_FTL_URI, indexProgressFtlUri));
        tempCache.setIndexWikiMoudleFtlUri(getString(KEY_INDEX_WIKIMODULE_FTL_URI, indexWikiModuleFtlUri));

        tempCache.setGamePageGameWikiFtlUri(getString(KEY_GAME_PAGE_GAMEWIKI_TFL_URI, gamePageGameWikiFtlUri));
        tempCache.setGamePageHeadLineFtlUri(getString(KEY_GAME_PAGE_HEADLINE_TFL_URI, gamePageHeadLineFtlUri));
        tempCache.setGamePageBulletinFtlUri(getString(KEY_GAME_PAGE_BULLETIN_TFL_URI, gamePageBulletinFtlUri));
        tempCache.setGamePageRecommendGameFtlUri(getString(KEY_GAME_PAGE_RECOMMENDGAME_TFL_URI, gamePageRecommendGameFtlUri));

        tempCache.setGamePageDanjiFtlUri(getString(KEY_GAME_PAGE_DANGJI_TFL_URI, gamePageDanjiFtlUri));
        tempCache.setGamePageMmoFtlUri(getString(KEY_GAME_PAGE_MMO_TFL_URI, gamePageMmoFtlUri));
        tempCache.setGamePageZhangjiFtlUri(getString(KEY_GAME_PAGE_ZHANGJI_TFL_URI, gamePageZhangjiFtlUri));
        tempCache.setGamePageMobileFtlUri(getString(KEY_GAME_PAGE_MOBILE_TFL_URI, gamePageMobileFtlUri));
        tempCache.setGamePageNewListFtlUri(getString(KEY_GAME_PAGE_NEWLIST_TFL_URI, gamePageNewListFtlUri));
        tempCache.setGamePageChangeListFtlUri(getString(KEY_GAME_PAGE_CHANGELIST_TFL_URI, gamePageChangeListFtlUri));
        tempCache.setGamePageWikiFtlUri(getString(KEY_GAME_PAGE_WIKI_FTL_URI, gamePageWikiFtlUri));

        tempCache.setMailInviteFtl(getString(KEY_MAIL_INVITE_FTL_URI, mailInviteFtl));
        tempCache.setMailModifyFtlUri(getString(KEY_MAIL_MODIFY_FTL_URI, mailModifyFtlUri));
        tempCache.setMailPwdForgetFtlUri(getString(KEY_MAIL_PWDFORGOT_FTL_URI, mailPwdForgetFtlUri));
        tempCache.setMailRegisterFtlUri(getString(KEY_MAIL_REGISTER_FTL_URI, mailRegisterFtlUri));
        tempCache.setMailVerifyFtlUri(getString(KEY_MAIL_VERIFY_FTL_URI, mailVerifyFtlUri));
        tempCache.setMailChinajoyFtlUri(getString(KEY_CHINA_JOY_2013_FTL_URL, mailChinajoyFtlUri));
        tempCache.setMailChinajoyFtlSubject(getString(KEY_CHINA_JOY_2013_FTL_SUBJECT, mailChinajoyFtlSubject));

        tempCache.setMailBatchCodeFtlUri(getString(KEY_MAIL_BATCHCODE_FTL_URI, mailBatchCodeFtlUri));

        tempCache.setActivityBulltinFtlUri(getString(KEY_ACTIVITY_BULLETIN_FTL_URI, activityBulltinFtlUri));
        tempCache.setActivityRecommendFtlUri(getString(KEY_ACTIVITY_RECOMMEND_FTL_URI, activityRecommendFtlUri));

        tempCache.setBaikeSystemMessageTemplate(getString(KEY_BAIKE_SYSTEM_MESSAGE_TEMPLATE));
        tempCache.setEssSystemMessageTemplate(getString(KEY_ESS_SYSTEM_MESSAGE_TEMPLATE));
        tempCache.setGameCoverSystemMessageTemplate(getString(KEY_GAMECOVER_SYSTEM_MESSAGE_TEMPLATE));
        tempCache.setMagazineSystemMessageTemplate(getString(KEY_MAGAZINE_SYSTEM_MESSAGE_TEMPLATE));
        tempCache.setBaikeNotCreateMessageTemplate(getString(KEY_BAIKE_NOTCREATEUSER_SYSTEM_MESSAGE_TEMPLATE));
        tempCache.setEssNotCreateMessageTemplate(getString(KEY_ESS_NOTCREATEUSER_SYSTEM_MESSAGE_TEMPLATE));
        tempCache.setIndexSystemMessageTemplate(getString(KEY_INDEX_SYSTEM_MESSAGE_TEMPLATE));
        tempCache.setGameInviteSystemMessageTemplate(getString(KEY_GAME_INVITE_SYSTEM_MESSAGE_TEMPLATE));

        tempCache.setVotePostContentMessageTemplate(getString(KEY_VOTE_POST_MESSAGE_TEMPLATE));
        tempCache.setVotePartContentMessageTemplate(getString(KEY_VOTE_PART_MESSAGE_TEMPLATE));

        tempCache.setPostModuleDefMessageTemplate(getString(KEY_POST_MODULEDEF_MESSAGE_TEMPLATE));
        tempCache.setPostCModuleMessageTemplate(getString(KEY_POST_CMODULE_MESSAGE_TEMPLATE));
        tempCache.setPostCModuleSuccessMessageTemplate(getString(KEY_POST_CMODULE_SUCCESS_MESSAGE_TEMPLATE));

        tempCache.setExchangedGTemplate(getString(KEY_GOODS_EXCHANGED_SUCCESS_G_TEMPLATE));
        tempCache.setExchangedVTemplate(getString(KEY_GOODS_EXCHANGED_SUCCESS_V_TEMPLATE));

        tempCache.setAdminAdjustPointTemplate(getString(KEY_ADMIN_ADJUST_POINT_TEMPLATE));


        tempCache.setLotteryAwardGTemplate(getString(KEY_LOTTERY_AWARD_G_TEMPLATE));
        tempCache.setLotteryAwardVTemplate(getString(KEY_LOTTERY_AWARD_V_TEMPLATE));
        tempCache.setLotteryAwardPTemplate(getString(KEY_LOTTERY_AWARD_P_TEMPLATE));

        tempCache.setActivityGetCode(getString(KEY_EMAIL_GIFT_GETCODE_SUCCESS));
        tempCache.setGameNewReleaseSubmitTemplate(getString(KEY_GAME_NEW_RELEASE_SUBMIT));
        tempCache.setJoinUserGroupTemplate(getString(KEY_JOIN_USER_GROUP_TEMPLATE));
        tempCache.setRejecteJoinGroupTemplate(getString(KEY_REJECTED_JOIN_GROUP_TEMPLATE));
        tempCache.setMailNewReleaseSubmitFtlUri(getString(KEY_GAME_NEW_RELEASE_SUBMIT_FTL_URL, mailNewRealeseSubmitFtlUri));
        tempCache.setMailNewReleaseSubmitFtlSubject(getString(KEY_GAME_NEW_RELEASE_SUBMIT_FTL_SUBJECT, mailNewRealeseSubmitFtlSubject));

        tempCache.setGameNewReleasePassTemplate(getString(KEY_GAME_NEW_RELEASE_PASS));
        tempCache.setMailNewReleasePassFtlUri(getString(KEY_GAME_NEW_RELEASE_PASS_FTL_URL, mailNewRealesePassFtlUri));
        tempCache.setMailNewReleasePassFtlSubject(getString(KEY_GAME_NEW_RELEASE_PASS_FTL_SUBJECT, mailNewRealesePassFtlSubject));

        tempCache.setExchangePointActionHistoryTemplate(getString(KEY_EXCHANGE_POINT_ACTION_HISTORY_TEMPLATE));
        tempCache.setSharePointActionHistoryTemplate(getString(KEY_SHARE_POINT_ACTION_HISTORY_TEMPLATE));
        tempCache.setGroupPointActionHistoryTemplate(getString(KEY_GROUP_POINT_ACTION_HISTORY_TEMPLATE));

        tempCache.setGiftMarketLogsFtlUri(getString(KEY_GIFTMARKET_LOGS_TEMPLATE, giftMarketLogsFtlUri));
        tempCache.setVerifyMobileSmsTemplate(getString(KEY_MOBILE_MESSAGE_GET_VALID_CONTENT));

        tempCache.setVerifyPCMobileSmsTemplate(getString(KEY_MOBILE_MESSAGE_GET_PC_VALID_CONTENT));


        tempCache.setGiftCodeMobileSmsTemplate(getString(KEY_GIFT_MARKET_SEND_CODE_TO_MOBILE));

        tempCache.setCj2014LotteryAwardTemplate(getString(KEY_CJ2014_LOTTERY_AWARD_TEMPLATE));
        tempCache.setCj2014LotteryAwardItemTemplate(getString(KEY_CJ2014_LOTTERY_AWARD_ITEM_TEMPLATE));
        //
        this.templatePropsCache = tempCache;

        logger.info("TemplateHotdeployConfig Props init finished." + this.toString());
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String getIndexFindGameFtlUri() {
        return templatePropsCache.getIndexFindGameFtlUri();
    }

    public String getMailInviteFtl() {
        return templatePropsCache.getMailInviteFtl();
    }

    public String getMailModifyFtlUri() {
        return templatePropsCache.getMailModifyFtlUri();
    }

    public String getMailPwdForgetFtlUri() {
        return templatePropsCache.getMailPwdForgetFtlUri();
    }

    public String getMailRegisterFtlUri() {
        return templatePropsCache.getMailRegisterFtlUri();
    }

    public String getMailVerifyFtlUri() {
        return templatePropsCache.getMailVerifyFtlUri();
    }


    public String getMailBatchCodeFtlUri() {
        return templatePropsCache.getMailBatchCodeFtlUri();
    }

    public String getMailChinajoyFtlUri() {
        return templatePropsCache.getMailChinajoyFtlUri();
    }

    public String getMailChinajoyFtlSubject() {
        return templatePropsCache.getMailChinajoyFtlSubject();
    }

    public TemplatePropsCache getTemplatePropsCache() {
        return templatePropsCache;
    }

    public void setTemplatePropsCache(TemplatePropsCache templatePropsCache) {
        this.templatePropsCache = templatePropsCache;
    }

    public String getActivityRecommendFtlUri() {
        return templatePropsCache.getActivityRecommendFtlUri();
    }

    public String getActivityBulltinFtlUri() {
        return templatePropsCache.getActivityBulltinFtlUri();
    }

    public String getBaikeSystemMessageTemplate() {
        return templatePropsCache.getBaikeSystemMessageTemplate();
    }

    public String getBaikeNotCreateMessageTemplate() {
        return templatePropsCache.getBaikeNotCreateMessageTemplate();
    }

    public String getGameCoverSystemMessageTemplate() {
        return templatePropsCache.getGameCoverSystemMessageTemplate();
    }

    public String getEssSystemMessageTemplate() {
        return templatePropsCache.getEssSystemMessageTemplate();
    }

    public String getEssNotCreateMessageTemplate() {
        return templatePropsCache.getEssNotCreateMessageTemplate();
    }

    public String getMagazineSystemMessageTemplate() {
        return templatePropsCache.getMagazineSystemMessageTemplate();
    }

    public String getIndexSystemMessageTemplate() {
        return templatePropsCache.getIndexSystemMessageTemplate();
    }


    public String getIndexProgressFtlUri() {
        return templatePropsCache.getIndexProgressFtlUri();
    }

    public String getGameInviteSystemMessageTemplate() {
        return templatePropsCache.getGameInviteSystemMessageTemplate();
    }

    public String getVotePostContentMessageTemplate() {
        return templatePropsCache.getVotePostContentMessageTemplate();
    }

    public String getVotePartContentMessageTemplate() {
        return templatePropsCache.getVotePartContentMessageTemplate();
    }

    public String getPostModuleDefMessageTemplate() {
        return templatePropsCache.getPostModuleDefMessageTemplate();
    }

    public String getPostCustomModuleMessageTemplate() {
        return templatePropsCache.getPostCModuleMessageTemplate();
    }

    public String getPostCModuleSuccessMessageTemplate() {
        return templatePropsCache.getPostCModuleSuccessMessageTemplate();
    }

    public String getLotteryAwardGTemplate() {
        return templatePropsCache.getLotteryAwardGTemplate();
    }

    public String getLotteryAwardVTemplate() {
        return templatePropsCache.getLotteryAwardVTemplate();
    }

    public String getLotteryAwardPTemplate() {
        return templatePropsCache.getLotteryAwardPTemplate();
    }

    public String getIndexGroupFtlUri() {
        return templatePropsCache.getIndexGroupFtlUri();
    }


    public String getGamePageDanjiFtlUri() {
        return templatePropsCache.getGamePageDanjiFtlUri();
    }

    public String getGamePageMmoFtlUri() {
        return templatePropsCache.getGamePageMmoFtlUri();
    }

    public String getGamePageZhangjiFtlUri() {
        return templatePropsCache.getGamePageZhangjiFtlUri();
    }


    public String getGamePageMobileFtlUri() {
        return templatePropsCache.getGamePageMobileFtlUri();
    }

    public String getGamePageNewListFtlUri() {
        return templatePropsCache.getGamePageNewListFtlUri();
    }

    public String getGamePageChangeListFtlUri() {
        return templatePropsCache.getGamePageChangeListFtlUri();
    }

    public String getContentGameFtlUri() {
        return templatePropsCache.getContentGameFtlUri();
    }

    public String getIndexAssessmentFtlUri() {
        return templatePropsCache.getIndexAssessmentFtlUri();
    }

    public String getIndexHandbookFtlUri() {
        return templatePropsCache.getIndexHandbookFtlUri();
    }

    public String getIndexHotNewsFtlUri() {
        return templatePropsCache.getIndexHotNewsFtlUri();
    }

    public String getIndexWikiFtlUri() {
        return templatePropsCache.getIndexWikiFtlUri();
    }

    public String getIndexRecommendFtlUri() {
        return templatePropsCache.getIndexRecommendFtlUri();
    }

    public String getIndexTopicFtlUri() {
        return templatePropsCache.getIndexTopicFtlUri();
    }

    public String getContentHotNewsFtlUri() {
        return templatePropsCache.getContentHotNewsFtlUri();
    }

    public String getIndexWikiMoudleFtlUri() {
        return templatePropsCache.getIndexWikiMoudleFtlUri();
    }

    public String getGamePageWikiFtlUri() {
        return templatePropsCache.getGamePageWikiFtlUri();
    }

    public String getExchangedGTemplate() {
        return templatePropsCache.getExchangedGTemplate();
    }

    public String getExchangedVTemplate() {
        return templatePropsCache.getExchangedVTemplate();
    }

    public String getActivityGetCode() {
        return templatePropsCache.getActivityGetCode();
    }

    public String getGamePageHeadLineFtlUri() {
        return templatePropsCache.getGamePageHeadLineFtlUri();
    }

    public String getGamePageGameWikiFtlUri() {
        return templatePropsCache.getGamePageGameWikiFtlUri();
    }

    public String getGamePageRecommendGameFtlUri() {
        return templatePropsCache.getGamePageRecommendGameFtlUri();
    }

    public String getGamePageBulletinFtlUri() {
        return templatePropsCache.getGamePageBulletinFtlUri();
    }


    public String getContentRecommendFtlUri() {
        return templatePropsCache.getContentRecommendFtlUri();
    }

    public String getAdminAdjustPointTemplate() {
        return templatePropsCache.getAdminAdjustPointTemplate();
    }

    public String getGameNewReleaseSubmitTemplate() {
        return templatePropsCache.getGameNewReleaseSubmitTemplate();
    }

    public String getMailNewReleaseSubmitFtlUri() {
        return templatePropsCache.getMailNewReleaseSubmitFtlUri();
    }

    public String getMailNewReleaseSubmitFtlSubject() {
        return templatePropsCache.getMailNewReleaseSubmitFtlSubject();
    }

    public String getGameNewReleasePassTemplate() {
        return templatePropsCache.getGameNewReleasePassTemplate();
    }

    public String getMailNewReleasePassFtlUri() {
        return templatePropsCache.getMailNewReleasePassFtlUri();
    }

    public String getMailNewReleasePassFtlSubject() {
        return templatePropsCache.getMailNewReleasePassFtlSubject();
    }

    public String getJoinUserGroup() {
        return templatePropsCache.getJoinUserGroupTemplate();
    }

    public String getRejectedJoinUserGroup() {
        return templatePropsCache.getRejecteJoinGroupTemplate();
    }

    public String getExchangePointActionHistoryTemplate() {
        return templatePropsCache.getExchangePointActionHistoryTemplate();
    }

    public String getGiftMarketLogsFtlUri() {
        return templatePropsCache.getGiftMarketLogsFtlUri();
    }

    public String getSharePointActionHistoryTemplate() {
        return templatePropsCache.getSharePointActionHistoryTemplate();
    }

    public String getGroupPointActionHistoryTemplate() {
        return templatePropsCache.getGroupPointActionHistoryTemplate();
    }

    public String getVerifyMobileSmsTemplate() {
        return templatePropsCache.getVerifyMobileSmsTemplate();
    }

    public String getGiftCodeMobileSmsTemplate(){
        return templatePropsCache.getGiftCodeMobileSmsTemplate();
    }

    public String getCJ2014LotteryAwardTemplate(){
        return templatePropsCache.getCj2014LotteryAwardTemplate();
    }

    public String getCJ2014LotteryAwardItemTemplate(){
        return templatePropsCache.getCj2014LotteryAwardItemTemplate();
    }



    public String getVerifyPCMobileSmsTemplate() {
        return templatePropsCache.getVerifyPCMobileSmsTemplate();
    }

    private class TemplatePropsCache {
        private String contentGameFtlUri;
        private String contentHotNewsFtlUri;
        private String contentRecommendFtlUri;

        private String indexFindGameFtlUri;
        private String indexAssessmentFtlUri;
        private String indexHandbookFtlUri;
        private String indexHotNewsFtlUri;
        private String indexTopicFtlUri;
        private String indexRecommendFtlUri;
        private String indexWikiFtlUri;
        private String indexProgressFtlUri;
        private String indexGroupFtlUri;

        private String indexWikiMoudleFtlUri;

        private String gamePageHeadLineFtlUri;
        private String gamePageGameWikiFtlUri;
        private String gamePageRecommendGameFtlUri;
        private String gamePageBulletinFtlUri;

        private String gamePageDanjiFtlUri;
        private String gamePageMmoFtlUri;
        private String gamePageZhangjiFtlUri;
        private String gamePageMobileFtlUri;
        private String gamePageNewListFtlUri;
        private String gamePageChangeListFtlUri;
        private String gamePageWikiFtlUri;

        private String mailInviteFtl;
        private String mailModifyFtlUri;
        private String mailPwdForgetFtlUri;
        private String mailRegisterFtlUri;
        private String mailVerifyFtlUri;

        private String mailBatchCodeFtlUri;
        private String mailChinajoyFtlUri;
        private String mailChinajoyFtlSubject;

        private String activityRecommendFtlUri;
        private String activityBulltinFtlUri;

        private String baikeSystemMessageTemplate;
        private String baikeNotCreateMessageTemplate;
        private String gameCoverSystemMessageTemplate;
        private String essSystemMessageTemplate;
        private String essNotCreateMessageTemplate;
        private String magazineSystemMessageTemplate;
        private String indexSystemMessageTemplate;
        private String gameInviteSystemMessageTemplate;

        private String votePostContentMessageTemplate;
        private String votePartContentMessageTemplate;


        private String postModuleDefMessageTemplate;
        private String postCModuleMessageTemplate;
        private String postCModuleSuccessMessageTemplate;

        private String exchangedGTemplate;
        private String exchangedVTemplate;
        private String adminAdjustPointTemplate;
        private String joinUserGroupTemplate;
        private String rejecteJoinGroupTemplate;
        private String lotteryAwardGTemplate;
        private String lotteryAwardVTemplate;
        private String lotteryAwardPTemplate;
        private String ActivityGetCode;
        private String gameNewReleaseSubmitTemplate;
        private String mailNewReleaseSubmitFtlSubject;
        private String mailNewReleaseSubmitFtlUri;

        private String gameNewReleasePassTemplate;
        private String mailNewReleasePassFtlSubject;
        private String mailNewReleasePassFtlUri;

        private String exchangePointActionHistoryTemplate;
        private String sharePointActionHistoryTemplate;
        private String groupPointActionHistoryTemplate;
        private String giftMarketLogsFtlUri;
        private String verifyMobileSmsTemplate;
        private String verifyPCMobileSmsTemplate;

        private String giftCodeMobileSmsTemplate;

        private String cj2014LotteryAwardTemplate;
        private String cj2014LotteryAwardItemTemplate;

        public String getVerifyMobileSmsTemplate() {
            return verifyMobileSmsTemplate;
        }

        public void setVerifyMobileSmsTemplate(String verifyMobileSmsTemplate) {
            this.verifyMobileSmsTemplate = verifyMobileSmsTemplate;
        }

        public String getVerifyPCMobileSmsTemplate() {
            return verifyPCMobileSmsTemplate;
        }

        public void setVerifyPCMobileSmsTemplate(String verifyPCMobileSmsTemplate) {
            this.verifyPCMobileSmsTemplate = verifyPCMobileSmsTemplate;
        }

        public String getActivityGetCode() {
            return ActivityGetCode;
        }

        public void setActivityGetCode(String activityGetCode) {
            ActivityGetCode = activityGetCode;
        }

        public String getContentGameFtlUri() {
            return contentGameFtlUri;
        }

        public void setContentGameFtlUri(String contentGameFtlUri) {
            this.contentGameFtlUri = contentGameFtlUri;
        }

        public String getContentRecommendFtlUri() {
            return contentRecommendFtlUri;
        }

        public void setContentRecommendFtlUri(String contentRecommendFtlUri) {
            this.contentRecommendFtlUri = contentRecommendFtlUri;
        }

        public String getIndexFindGameFtlUri() {
            return indexFindGameFtlUri;
        }

        public void setIndexFindGameFtlUri(String indexFindGameFtlUri) {
            this.indexFindGameFtlUri = indexFindGameFtlUri;
        }

        public String getIndexAssessmentFtlUri() {
            return indexAssessmentFtlUri;
        }

        public void setIndexAssessmentFtlUri(String indexAssessmentFtlUri) {
            this.indexAssessmentFtlUri = indexAssessmentFtlUri;
        }

        public String getIndexHandbookFtlUri() {
            return indexHandbookFtlUri;
        }

        public void setIndexHandbookFtlUri(String indexHandbookFtlUri) {
            this.indexHandbookFtlUri = indexHandbookFtlUri;
        }

        public String getIndexHotNewsFtlUri() {
            return indexHotNewsFtlUri;
        }

        public void setIndexHotNewsFtlUri(String indexHotNewsFtlUri) {
            this.indexHotNewsFtlUri = indexHotNewsFtlUri;
        }

        public String getIndexTopicFtlUri() {
            return indexTopicFtlUri;
        }

        public void setIndexTopicFtlUri(String indexTopicFtlUri) {
            this.indexTopicFtlUri = indexTopicFtlUri;
        }

        public String getIndexRecommendFtlUri() {
            return indexRecommendFtlUri;
        }

        public void setIndexRecommendFtlUri(String indexRecommendFtlUri) {
            this.indexRecommendFtlUri = indexRecommendFtlUri;
        }

        public String getIndexWikiFtlUri() {
            return indexWikiFtlUri;
        }

        public void setIndexWikiFtlUri(String indexWikiFtlUri) {
            this.indexWikiFtlUri = indexWikiFtlUri;
        }

        public String getIndexProgressFtlUri() {
            return indexProgressFtlUri;
        }

        public void setIndexProgressFtlUri(String indexProgressFtlUri) {
            this.indexProgressFtlUri = indexProgressFtlUri;
        }

        public String getIndexGroupFtlUri() {
            return indexGroupFtlUri;
        }

        public void setIndexGroupFtlUri(String indexGroupFtlUri) {
            this.indexGroupFtlUri = indexGroupFtlUri;
        }

        public String getGamePageHeadLineFtlUri() {
            return gamePageHeadLineFtlUri;
        }

        public void setGamePageHeadLineFtlUri(String gamePageHeadLineFtlUri) {
            this.gamePageHeadLineFtlUri = gamePageHeadLineFtlUri;
        }

        public String getGamePageGameWikiFtlUri() {
            return gamePageGameWikiFtlUri;
        }

        public void setGamePageGameWikiFtlUri(String gamePageGameWikiFtlUri) {
            this.gamePageGameWikiFtlUri = gamePageGameWikiFtlUri;
        }

        public String getGamePageRecommendGameFtlUri() {
            return gamePageRecommendGameFtlUri;
        }

        public void setGamePageRecommendGameFtlUri(String gamePageRecommendGameFtlUri) {
            this.gamePageRecommendGameFtlUri = gamePageRecommendGameFtlUri;
        }

        public String getGamePageBulletinFtlUri() {
            return gamePageBulletinFtlUri;
        }

        public void setGamePageBulletinFtlUri(String gamePageBulletinFtlUri) {
            this.gamePageBulletinFtlUri = gamePageBulletinFtlUri;
        }

        public String getGamePageDanjiFtlUri() {
            return gamePageDanjiFtlUri;
        }

        public void setGamePageDanjiFtlUri(String gamePageDanjiFtlUri) {
            this.gamePageDanjiFtlUri = gamePageDanjiFtlUri;
        }

        public String getGamePageMmoFtlUri() {
            return gamePageMmoFtlUri;
        }

        public void setGamePageMmoFtlUri(String gamePageMmoFtlUri) {
            this.gamePageMmoFtlUri = gamePageMmoFtlUri;
        }

        public String getGamePageZhangjiFtlUri() {
            return gamePageZhangjiFtlUri;
        }

        public void setGamePageZhangjiFtlUri(String gamePageZhangjiFtlUri) {
            this.gamePageZhangjiFtlUri = gamePageZhangjiFtlUri;
        }

        public String getGamePageMobileFtlUri() {
            return gamePageMobileFtlUri;
        }

        public void setGamePageMobileFtlUri(String gamePageMobileFtlUri) {
            this.gamePageMobileFtlUri = gamePageMobileFtlUri;
        }

        public String getGamePageNewListFtlUri() {
            return gamePageNewListFtlUri;
        }

        public void setGamePageNewListFtlUri(String gamePageNewListFtlUri) {
            this.gamePageNewListFtlUri = gamePageNewListFtlUri;
        }

        public String getGamePageChangeListFtlUri() {
            return gamePageChangeListFtlUri;
        }

        public void setGamePageChangeListFtlUri(String gamePageChangeListFtlUri) {
            this.gamePageChangeListFtlUri = gamePageChangeListFtlUri;
        }

        public String getMailInviteFtl() {
            return mailInviteFtl;
        }

        public void setMailInviteFtl(String mailInviteFtl) {
            this.mailInviteFtl = mailInviteFtl;
        }

        public String getMailModifyFtlUri() {
            return mailModifyFtlUri;
        }

        public void setMailModifyFtlUri(String mailModifyFtlUri) {
            this.mailModifyFtlUri = mailModifyFtlUri;
        }

        public String getMailPwdForgetFtlUri() {
            return mailPwdForgetFtlUri;
        }

        public void setMailPwdForgetFtlUri(String mailPwdForgetFtlUri) {
            this.mailPwdForgetFtlUri = mailPwdForgetFtlUri;
        }

        public String getMailRegisterFtlUri() {
            return mailRegisterFtlUri;
        }

        public void setMailRegisterFtlUri(String mailRegisterFtlUri) {
            this.mailRegisterFtlUri = mailRegisterFtlUri;
        }

        public String getMailVerifyFtlUri() {
            return mailVerifyFtlUri;
        }

        public void setMailVerifyFtlUri(String mailVerifyFtlUri) {
            this.mailVerifyFtlUri = mailVerifyFtlUri;
        }

        public String getMailBatchCodeFtlUri() {
            return mailBatchCodeFtlUri;
        }

        public void setMailBatchCodeFtlUri(String mailBatchCodeFtlUri) {
            this.mailBatchCodeFtlUri = mailBatchCodeFtlUri;
        }

        public String getMailChinajoyFtlUri() {
            return mailChinajoyFtlUri;
        }

        public void setMailChinajoyFtlUri(String mailChinajoyFtlUri) {
            this.mailChinajoyFtlUri = mailChinajoyFtlUri;
        }

        public String getMailChinajoyFtlSubject() {
            return mailChinajoyFtlSubject;
        }

        public void setMailChinajoyFtlSubject(String mailChinajoyFtlSubject) {
            this.mailChinajoyFtlSubject = mailChinajoyFtlSubject;
        }

        public String getActivityRecommendFtlUri() {
            return activityRecommendFtlUri;
        }

        public void setActivityRecommendFtlUri(String activityRecommendFtlUri) {
            this.activityRecommendFtlUri = activityRecommendFtlUri;
        }

        public String getActivityBulltinFtlUri() {
            return activityBulltinFtlUri;
        }

        public void setActivityBulltinFtlUri(String activityBulltinFtlUri) {
            this.activityBulltinFtlUri = activityBulltinFtlUri;
        }

        public String getBaikeSystemMessageTemplate() {
            return baikeSystemMessageTemplate;
        }

        public void setBaikeSystemMessageTemplate(String baikeSystemMessageTemplate) {
            this.baikeSystemMessageTemplate = baikeSystemMessageTemplate;
        }

        public String getBaikeNotCreateMessageTemplate() {
            return baikeNotCreateMessageTemplate;
        }

        public void setBaikeNotCreateMessageTemplate(String baikeNotCreateMessageTemplate) {
            this.baikeNotCreateMessageTemplate = baikeNotCreateMessageTemplate;
        }

        public String getGameCoverSystemMessageTemplate() {
            return gameCoverSystemMessageTemplate;
        }

        public void setGameCoverSystemMessageTemplate(String gameCoverSystemMessageTemplate) {
            this.gameCoverSystemMessageTemplate = gameCoverSystemMessageTemplate;
        }

        public String getEssSystemMessageTemplate() {
            return essSystemMessageTemplate;
        }

        public void setEssSystemMessageTemplate(String essSystemMessageTemplate) {
            this.essSystemMessageTemplate = essSystemMessageTemplate;
        }

        public String getEssNotCreateMessageTemplate() {
            return essNotCreateMessageTemplate;
        }

        public void setEssNotCreateMessageTemplate(String essNotCreateMessageTemplate) {
            this.essNotCreateMessageTemplate = essNotCreateMessageTemplate;
        }

        public String getMagazineSystemMessageTemplate() {
            return magazineSystemMessageTemplate;
        }

        public void setMagazineSystemMessageTemplate(String magazineSystemMessageTemplate) {
            this.magazineSystemMessageTemplate = magazineSystemMessageTemplate;
        }

        public String getIndexSystemMessageTemplate() {
            return indexSystemMessageTemplate;
        }

        public void setIndexSystemMessageTemplate(String indexSystemMessageTemplate) {
            this.indexSystemMessageTemplate = indexSystemMessageTemplate;
        }

        public String getGameInviteSystemMessageTemplate() {
            return gameInviteSystemMessageTemplate;
        }

        public void setGameInviteSystemMessageTemplate(String gameInviteSystemMessageTemplate) {
            this.gameInviteSystemMessageTemplate = gameInviteSystemMessageTemplate;
        }

        public String getVotePostContentMessageTemplate() {
            return votePostContentMessageTemplate;
        }

        public void setVotePostContentMessageTemplate(String votePostContentMessageTemplate) {
            this.votePostContentMessageTemplate = votePostContentMessageTemplate;
        }

        public String getVotePartContentMessageTemplate() {
            return votePartContentMessageTemplate;
        }

        public void setVotePartContentMessageTemplate(String votePartContentMessageTemplate) {
            this.votePartContentMessageTemplate = votePartContentMessageTemplate;
        }

        public String getPostModuleDefMessageTemplate() {
            return postModuleDefMessageTemplate;
        }

        public void setPostModuleDefMessageTemplate(String postModuleDefMessageTemplate) {
            this.postModuleDefMessageTemplate = postModuleDefMessageTemplate;
        }

        public String getPostCModuleMessageTemplate() {
            return postCModuleMessageTemplate;
        }

        public void setPostCModuleMessageTemplate(String postCModuleMessageTemplate) {
            this.postCModuleMessageTemplate = postCModuleMessageTemplate;
        }

        public String getPostCModuleSuccessMessageTemplate() {
            return postCModuleSuccessMessageTemplate;
        }

        public void setPostCModuleSuccessMessageTemplate(String postCModuleSuccessMessageTemplate) {
            this.postCModuleSuccessMessageTemplate = postCModuleSuccessMessageTemplate;
        }

        public String getContentHotNewsFtlUri() {
            return contentHotNewsFtlUri;
        }

        public void setContentHotNewsFtlUri(String contentHotNewsFtlUri) {
            this.contentHotNewsFtlUri = contentHotNewsFtlUri;
        }

        public String getIndexWikiMoudleFtlUri() {
            return indexWikiMoudleFtlUri;
        }

        public void setIndexWikiMoudleFtlUri(String indexWikiMoudleFtlUri) {
            this.indexWikiMoudleFtlUri = indexWikiMoudleFtlUri;
        }

        public String getGamePageWikiFtlUri() {
            return gamePageWikiFtlUri;
        }

        public void setGamePageWikiFtlUri(String gamePageWikiFtlUri) {
            this.gamePageWikiFtlUri = gamePageWikiFtlUri;
        }

        public String getExchangedGTemplate() {
            return exchangedGTemplate;
        }

        public void setExchangedGTemplate(String exchangedGTemplate) {
            this.exchangedGTemplate = exchangedGTemplate;
        }

        public String getExchangedVTemplate() {
            return exchangedVTemplate;
        }

        public void setExchangedVTemplate(String exchangedVTemplate) {
            this.exchangedVTemplate = exchangedVTemplate;
        }

        public String getAdminAdjustPointTemplate() {
            return adminAdjustPointTemplate;
        }

        public void setAdminAdjustPointTemplate(String adminAdjustPointTemplate) {
            this.adminAdjustPointTemplate = adminAdjustPointTemplate;
        }

        public String getLotteryAwardGTemplate() {
            return lotteryAwardGTemplate;
        }

        public void setLotteryAwardGTemplate(String lotteryAwardGTemplate) {
            this.lotteryAwardGTemplate = lotteryAwardGTemplate;
        }

        public String getLotteryAwardVTemplate() {
            return lotteryAwardVTemplate;
        }

        public void setLotteryAwardVTemplate(String lotteryAwardVTemplate) {
            this.lotteryAwardVTemplate = lotteryAwardVTemplate;
        }

        public String getLotteryAwardPTemplate() {
            return lotteryAwardPTemplate;
        }

        public void setLotteryAwardPTemplate(String lotteryAwardPTemplate) {
            this.lotteryAwardPTemplate = lotteryAwardPTemplate;
        }

        public String getGameNewReleaseSubmitTemplate() {
            return gameNewReleaseSubmitTemplate;
        }

        public void setGameNewReleaseSubmitTemplate(String gameNewReleaseSubmitTemplate) {
            this.gameNewReleaseSubmitTemplate = gameNewReleaseSubmitTemplate;
        }

        public String getMailNewReleaseSubmitFtlSubject() {
            return mailNewReleaseSubmitFtlSubject;
        }

        public void setMailNewReleaseSubmitFtlSubject(String mailNewReleaseSubmitFtlSubject) {
            this.mailNewReleaseSubmitFtlSubject = mailNewReleaseSubmitFtlSubject;
        }

        public String getMailNewReleaseSubmitFtlUri() {
            return mailNewReleaseSubmitFtlUri;
        }

        public void setMailNewReleaseSubmitFtlUri(String mailNewReleaseSubmitFtlUri) {
            this.mailNewReleaseSubmitFtlUri = mailNewReleaseSubmitFtlUri;
        }

        public String getGameNewReleasePassTemplate() {
            return gameNewReleasePassTemplate;
        }

        public void setGameNewReleasePassTemplate(String gameNewReleasePassTemplate) {
            this.gameNewReleasePassTemplate = gameNewReleasePassTemplate;
        }

        public String getMailNewReleasePassFtlSubject() {
            return mailNewReleasePassFtlSubject;
        }

        public void setMailNewReleasePassFtlSubject(String mailNewReleasePassFtlSubject) {
            this.mailNewReleasePassFtlSubject = mailNewReleasePassFtlSubject;
        }

        public String getMailNewReleasePassFtlUri() {
            return mailNewReleasePassFtlUri;
        }

        public void setMailNewReleasePassFtlUri(String mailNewReleasePassFtlUri) {
            this.mailNewReleasePassFtlUri = mailNewReleasePassFtlUri;
        }

        public String getJoinUserGroupTemplate() {
            return joinUserGroupTemplate;
        }

        public void setJoinUserGroupTemplate(String joinUserGroupTemplate) {
            this.joinUserGroupTemplate = joinUserGroupTemplate;
        }

        public String getRejecteJoinGroupTemplate() {
            return rejecteJoinGroupTemplate;
        }

        public void setRejecteJoinGroupTemplate(String rejecteJoinGroupTemplate) {
            this.rejecteJoinGroupTemplate = rejecteJoinGroupTemplate;
        }


        public String getExchangePointActionHistoryTemplate() {
            return exchangePointActionHistoryTemplate;
        }

        public void setExchangePointActionHistoryTemplate(String exchangePointActionHistoryTemplate) {
            this.exchangePointActionHistoryTemplate = exchangePointActionHistoryTemplate;
        }

        public String getSharePointActionHistoryTemplate() {
            return sharePointActionHistoryTemplate;
        }

        public void setSharePointActionHistoryTemplate(String sharePointActionHistoryTemplate) {
            this.sharePointActionHistoryTemplate = sharePointActionHistoryTemplate;
        }

        public String getGroupPointActionHistoryTemplate() {
            return groupPointActionHistoryTemplate;
        }

        public void setGroupPointActionHistoryTemplate(String groupPointActionHistoryTemplate) {
            this.groupPointActionHistoryTemplate = groupPointActionHistoryTemplate;
        }


        public String getGiftMarketLogsFtlUri() {
            return giftMarketLogsFtlUri;
        }

        public void setGiftMarketLogsFtlUri(String giftMarketLogsFtlUri) {
            this.giftMarketLogsFtlUri = giftMarketLogsFtlUri;
        }

        public String getGiftCodeMobileSmsTemplate() {
            return giftCodeMobileSmsTemplate;
        }

        public void setGiftCodeMobileSmsTemplate(String giftCodeMobileSmsTemplate) {
            this.giftCodeMobileSmsTemplate = giftCodeMobileSmsTemplate;
        }

        public void setCj2014LotteryAwardTemplate(String cj2014LotteryAwardTemplate) {
            this.cj2014LotteryAwardTemplate = cj2014LotteryAwardTemplate;
        }

        public void setCj2014LotteryAwardItemTemplate(String cj2014LotteryAwardItemTemplate) {
            this.cj2014LotteryAwardItemTemplate = cj2014LotteryAwardItemTemplate;
        }

        public String getCj2014LotteryAwardTemplate() {
            return cj2014LotteryAwardTemplate;
        }

        public String getCj2014LotteryAwardItemTemplate() {
            return cj2014LotteryAwardItemTemplate;
        }
    }
}
