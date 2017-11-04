package com.enjoyf.platform.webapps.common;

import com.enjoyf.platform.service.point.WanbaPointType;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONObject;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-22 下午4:20
 * Description:
 */
public class ResultCodeConstants {

    protected static final int WANBABASECODE = -50000;

    public static final ResultCodeConstants APP_NOT_EXISTS = new ResultCodeConstants(-100, "app.not.exists");

    //全局的正确和错误代码          http://www.joyme.dev/my/list
    public static final ResultCodeConstants FAILED = new ResultCodeConstants(2, "failed", "失败");
    public static final ResultCodeConstants SUCCESS = new ResultCodeConstants(1, "success", "成功");
    public static final ResultCodeConstants ERROR = new ResultCodeConstants(0, "system.error", "系统错误");
    public static final ResultCodeConstants USER_NOT_LOGIN = new ResultCodeConstants(-1, "user.not.login", "用户没有登录");

    public static final ResultCodeConstants PARAM_EMPTY = new ResultCodeConstants(-1001, "param.empty", "参数为空");

    public static final ResultCodeConstants SYSTEM_ERROR = new ResultCodeConstants(-1000, "system.error", "系统错误");
    //维护
    public static final ResultCodeConstants SYSTEM_MAINTENANCE = new ResultCodeConstants(-99999, "system.maintenance", "系统维护");

    ///////////////////oauth2Inteceptor start////////////////////
    public static final ResultCodeConstants ACCESSTOKEN_OVERDUE = new ResultCodeConstants(-100, "accesstoken.overdue");
    public static final ResultCodeConstants UNO_IS_NULL = new ResultCodeConstants(-200, "uno.is.null");
    public static final ResultCodeConstants ACCESSTOKEN_APPKEY_IS_NULL = new ResultCodeConstants(-300, "accesstoken.appkey.is.null");
    public static final ResultCodeConstants REFRESHTOKEN_APPKEY_IS_NULL = new ResultCodeConstants(-400, "refreshtoken.appkey.null");
    public static final ResultCodeConstants APPKEY_ERROR = new ResultCodeConstants(-500, "appkey.error","appkey错误");
    public static final ResultCodeConstants OAUTH_SUCCESS = new ResultCodeConstants(1, "success","成功");
    ///////////////////oauth2Inteceptor end////////////////////

    public static final int _CODE_ERROR_APP_NOTEXISTS = -100;
    public static final int _CODE_ERROR_SIGN_FAIED = -101;
    public static final int _CODE_ERROR_SIGN_PARAMEMPTY = -102;
    public static final int _CODE_ERROR_PARAM_EMPTY = -103;

    /////////////////ApiSignInteceptor start///////////////////
    public static final ResultCodeConstants CODE_ERROR_APP_NOTEXISTS = new ResultCodeConstants(-100, "sign.failed");
    public static final ResultCodeConstants CODE_ERROR_SIGN_FAIED = new ResultCodeConstants(-101, "sign.failed");
    public static final ResultCodeConstants CODE_ERROR_SIGN_PARAMEMPTY = new ResultCodeConstants(-102, "sign.failed");
    public static final ResultCodeConstants CODE_ERROR_PARAM_EMPTY = new ResultCodeConstants(-103, "sign.failed");
    /////////////////ApiSignInteceptor end//////////////////


    //////////////JsonShareInteceptor start///////////
    public static final ResultCodeConstants PARAM_ISEMPTY = new ResultCodeConstants(0, "param.is.empty","参数为空");
    public static final ResultCodeConstants REQUEST_ERROR = new ResultCodeConstants(-101, "request.error");//"request.error";
    public static final ResultCodeConstants APP_INVALID = new ResultCodeConstants(-101, "app.invalid");//"app.invalid";
    public static final ResultCodeConstants SIGN_FAILD = new ResultCodeConstants(-102, "sign.falid");//"sign.falid";
    public static final ResultCodeConstants NO_PRIVILEGE = new ResultCodeConstants(-103, "no.privilege");//"sign.falid";
    ///////////////////JsonShareInteceptor end//////////

    //////SyncController///
    public static final ResultCodeConstants OAUTH2_AUTHPARAM_AUTHINFO = new ResultCodeConstants(-201, "authparam.error");
    public static final ResultCodeConstants OAUTH2_APPKEY = new ResultCodeConstants(-202, "appkey.error");
    ////SyncController/////


    //////JsonClientLoginController///
    public static final ResultCodeConstants LOGIN_JSON_USERID_IS_NULL = new ResultCodeConstants(-101, "userid.is.null");
    public static final ResultCodeConstants LOGIN_JSON_TIME_IS_NULL = new ResultCodeConstants(-102, "time.is.null");
    public static final ResultCodeConstants LOGIN_JSON_APPKEY_IS_NULL = new ResultCodeConstants(-103, "appkey.is.null");
    public static final ResultCodeConstants LOGIN_JSON_SIGN_IS_NULL = new ResultCodeConstants(-104, "sign.is.null");
    public static final ResultCodeConstants LOGIN_JSON_APP_IS_NULL = new ResultCodeConstants(-105, "app.is.null");
    public static final ResultCodeConstants LOGIN_JSON_USER_IS_NULL = new ResultCodeConstants(-106, "user.is.null");
    public static final ResultCodeConstants LOGIN_JSON_PROFILE_IS_NULL = new ResultCodeConstants(-107, "profile.is.null");
    public static final ResultCodeConstants LOGIN_JSON_FORBIDLOGIN_IS_NULL = new ResultCodeConstants(-108, "forbidlogin.is.null");
    public static final ResultCodeConstants LOGIN_JSON_BAN_IS_NULL = new ResultCodeConstants(-109, "ban.is.null");
    public static final ResultCodeConstants LOGIN_LUOSIMAO_IS_ERROR = new ResultCodeConstants(-110, "luosimao.is.error");
    ////JsonClientLoginController/////

    //////SocialAPIInterceptor///
    public static final ResultCodeConstants INTERCEPT_UNO_IS_NULL = new ResultCodeConstants(-501, "intercept.uno.is.null");
    public static final ResultCodeConstants INTERCEPT_TIME_IS_NULL = new ResultCodeConstants(-502, "intercept.time.is.null");
    public static final ResultCodeConstants INTERCEPT_SECRET_IS_NULL = new ResultCodeConstants(-503, "intercept.secret.is.null");
    public static final ResultCodeConstants INTERCEPT_APPKEY_IS_NULL = new ResultCodeConstants(-504, "intercept.appkey.is.null");
    public static final ResultCodeConstants INTERCEPT_FORBID = new ResultCodeConstants(-505, "intercept.forbid");
    public static final ResultCodeConstants INTERCEPT_SECRET_IS_ERROR = new ResultCodeConstants(-506, "intercept.secret.is.error");
    //////SocialAPIInterceptor///

    //////socialclient start///
    public static final ResultCodeConstants SOCIAL_UNO_IS_NULL = new ResultCodeConstants(-301, "social.uno.is.null");
    public static final ResultCodeConstants SOCIAL_CID_IS_NULL = new ResultCodeConstants(-302, "social.cid.is.null");
    public static final ResultCodeConstants SOCIAL_CONTENT_IS_NULL = new ResultCodeConstants(-303, "social.content.is.null");
    public static final ResultCodeConstants SOCIAL_ACTION_IS_NULL = new ResultCodeConstants(-304, "social.action.is.null");
    public static final ResultCodeConstants SOCIAL_CONTENTBLACK = new ResultCodeConstants(-305, "social.contentblack");
    public static final ResultCodeConstants SOCIAL_PIC_IS_NULL = new ResultCodeConstants(-306, "social.pic.is.null");
    public static final ResultCodeConstants SOCIAL_BODY_IS_NULL = new ResultCodeConstants(-307, "social.body.is.null");
    public static final ResultCodeConstants SOCIAL_CONTENTUNO_IS_NULL = new ResultCodeConstants(-308, "social.contentuno.is.null");
    public static final ResultCodeConstants SOCIAL_REPLYID_IS_NULL = new ResultCodeConstants(-309, "social.replyid.is.null");
    public static final ResultCodeConstants SOCIAL_PROFILE_HAS_FORBID = new ResultCodeConstants(-311, "social.profile.has.forbid");
    public static final ResultCodeConstants SOCIAL_PROFILE_HAS_BAN = new ResultCodeConstants(-312, "social.profile.has.ban");
    public static final ResultCodeConstants SOCIAL_AGREE_FAIL = new ResultCodeConstants(-314, "social.agree.fail");
    public static final ResultCodeConstants SOCIAL_DELETE_CONTENT_FAIL = new ResultCodeConstants(-315, "social.delete.content.fail");
    public static final ResultCodeConstants SOCIAL_DESUNO_IS_NULL = new ResultCodeConstants(-316, "social.desuno.is.null");
    public static final ResultCodeConstants SOCIAL_REPORTTYPE_IS_NULL = new ResultCodeConstants(-317, "social.reporttype.is.null");
    public static final ResultCodeConstants SOCIAL_REPORTREASON_IS_NULL = new ResultCodeConstants(-318, "social.reportreason.is.null");
    public static final ResultCodeConstants SOCIAL_UNAGREE_FAIL = new ResultCodeConstants(-319, "social.unagree.fail");
    public static final ResultCodeConstants SOCIAL_ACTION_WRONG_TYPE = new ResultCodeConstants(-320, "social.action.wrong.type");

    public static final ResultCodeConstants SOCIAL_PHONE_IS_NULL = new ResultCodeConstants(-320, "social.phone.is.null");
    public static final ResultCodeConstants SOCIAL_PHONE_NOT_MATCHES = new ResultCodeConstants(-321, "social.phone.not.matches");
    public static final ResultCodeConstants SOCIAL_ACCOUNT_MOBILE_HAS_EXISTED = new ResultCodeConstants(-322, "social.account.mobile.has.existed");
    public static final ResultCodeConstants SOCIAL_ACCOUNT_MOBILE_NOT_EXISTED = new ResultCodeConstants(-323, "social.account.mobile.not.existed");
    public static final ResultCodeConstants SOCIAL_POST_MESSAGE_LESS_THAN_60M = new ResultCodeConstants(-324, "social.post.message.less.than.60m");
    public static final ResultCodeConstants SOCIAL_POST_MESSAGE_ERROR = new ResultCodeConstants(-325, "social.post.message.error");
    public static final ResultCodeConstants SOCIAL_VERIFY_CODE_IS_NULL = new ResultCodeConstants(-326, "social.verify.code.is.null");
    public static final ResultCodeConstants SOCIAL_VERIFY_CODE_ERROR = new ResultCodeConstants(-327, "social.verify.code.error");
    public static final ResultCodeConstants SOCIAL_NAME_IS_NULL = new ResultCodeConstants(-328, "social.name.is.null");
    public static final ResultCodeConstants SOCIAL_NAME_ILLEGL = new ResultCodeConstants(-329, "social.name.illegl");
    public static final ResultCodeConstants SOCIAL_NAME_HAS_EXISTED = new ResultCodeConstants(-330, "social.name.has.existed");
    public static final ResultCodeConstants SOCIAL_PASSWORD_IS_NULL = new ResultCodeConstants(-331, "social.password.is.null");
    public static final ResultCodeConstants SOCIAL_SEX_IS_NULL = new ResultCodeConstants(-332, "social.sex.is.null");
    public static final ResultCodeConstants SOCIAL_BIRTHDAY_IS_NULL = new ResultCodeConstants(-333, "social.birthday.is.null");
    public static final ResultCodeConstants SOCIAL_SIGNATURE_ILLEGL = new ResultCodeConstants(-334, "social.signature.illegl");
    public static final ResultCodeConstants SOCIAL_APP_IS_NULL = new ResultCodeConstants(-335, "social.app.is.null");
    public static final ResultCodeConstants SOCIAL_PROFILE_IS_NULL = new ResultCodeConstants(-336, "social.profile.is.null");
    public static final ResultCodeConstants SOCIAL_OAUTHINFO_IS_NULL = new ResultCodeConstants(-337, "social.oauthinfo.is.null");
    public static final ResultCodeConstants SOCIAL_APPKEY_IS_NULL = new ResultCodeConstants(-338, "social.appkey.is.null");
    public static final ResultCodeConstants SOCIAL_USERID_PWD_ERROR = new ResultCodeConstants(-339, "social.userid.pwd.error");
    public static final ResultCodeConstants SOCIAL_PLAYINGGAMES_ILLEGL = new ResultCodeConstants(-340, "social.playinggames.illegl");
    public static final ResultCodeConstants SOCIAL_POST_MESSAGE_MORE_THAN_5_IN_6_HOURS = new ResultCodeConstants(-341, "social.post.message.more.than.5.in.6.hours");
    public static final ResultCodeConstants SOCIAL_VERIFY_CODE_EXPIRE = new ResultCodeConstants(-342, "social.verify.code.expire");
    public static final ResultCodeConstants SOCIAL_CLIENT_ID_IS_NULL = new ResultCodeConstants(-343, "social.client.id.is.null");
    public static final ResultCodeConstants SOCIAL_CLIENT_TOKEN_IS_NULL = new ResultCodeConstants(-344, "social.client.token.is.null");
    public static final ResultCodeConstants SOCIAL_PLATFORM_IS_NULL = new ResultCodeConstants(-345, "social.request.param.platform.is.null");
    public static final ResultCodeConstants SOCIAL_ACTIVITY_ID_IS_NULL = new ResultCodeConstants(-346, "social.request.param.activityid.is.null");
    public static final ResultCodeConstants SOCIAL_BALCK_IS_FORBID = new ResultCodeConstants(-347, "social.balck.is.forbid");

    public static final ResultCodeConstants SOCIAL_SINA_FOLLOWLIST_IS_NULL = new ResultCodeConstants(-348, "social.sinafollowlist.is.null");
    public static final ResultCodeConstants SOCIAL_SEARCH_SINA_FOLLOWLIST_IS_NULL = new ResultCodeConstants(-349, "social..search.sinafollowlist.is.null");
    public static final ResultCodeConstants SOCIAL_ACTIVITY_IS_NULL = new ResultCodeConstants(-350, "social.activity.is.null");
    public static final ResultCodeConstants SOCIAL_ADTYPE_IS_NULL = new ResultCodeConstants(-351, "social.request.param.adtype.is.null");
    public static final ResultCodeConstants SOCIAL_THIRDCODE_IS_NULL = new ResultCodeConstants(-352, "social.request.param.thirdcode.is.null");

    public static final ResultCodeConstants SOCIAL_MESSAGE_PAGE_ERROR = new ResultCodeConstants(-353, "social.message.page.error");

    public static final ResultCodeConstants SOCIAL_MESSAGE_TYPE_NULL = new ResultCodeConstants(-354, "social.message.type.param.null");

    public static final ResultCodeConstants SOCIAL_MESSAGE_TYPE_ERROR = new ResultCodeConstants(-355, "social.message.type.param.error");

    public static final ResultCodeConstants SOCIAL_RECOMMEND_TYPE_NULL = new ResultCodeConstants(-356, "social.recommend.type.null");

    public static final ResultCodeConstants SOCIAL_QUERYSTR_IS_NULL = new ResultCodeConstants(-357, "social.querystr.is.null");
    //////socialclient end///

    //////oauth sdk start///
    public static final ResultCodeConstants OAUTH_SDK_FORIGN_ID_IS_NULL = new ResultCodeConstants(-600, "oauth.sdk.forign.id.is.null");
    //////oauth sdk end///
    public static final ResultCodeConstants APP_WRONG_ADTYPE = new ResultCodeConstants(-700, "ad.wrong.adtype");

    public static final ResultCodeConstants CONTEXT_CONTENT_NULL = new ResultCodeConstants(-800, "context.content.null");
    public static final ResultCodeConstants CONTEXT_APPKEY_NULL = new ResultCodeConstants(-801, "context.appkey.null");
    public static final ResultCodeConstants CONTEXT_TYPE_NULL = new ResultCodeConstants(-802, "context.type.null");
    public static final ResultCodeConstants CONTEXT_REGULAR_ERROR = new ResultCodeConstants(-803, "context.regular.error");
    public static final ResultCodeConstants CONTEXT_CONTENT_ILLEGAL = new ResultCodeConstants(-804, "context.illegal");
    public static final ResultCodeConstants CONTEXT_KADA_RESERVED = new ResultCodeConstants(-805, "context.kada.reserved");


    //activity//
    public static final ResultCodeConstants ACTIVITY_ACTIVITY_NOTEXISTS = new ResultCodeConstants(-900, "activity.not.exists");
    public static final ResultCodeConstants AWARD_USER_NOT_EXISTS = new ResultCodeConstants(-901, "user.not.exists");
    public static final ResultCodeConstants AWARD_USER_HAS_AWARD = new ResultCodeConstants(-902, "user.has.award");
    public static final ResultCodeConstants AWARD_USER_GET_FAILED = new ResultCodeConstants(-903, "user.get.failed");
    public static final ResultCodeConstants AWARD_NOT_ENOUGH = new ResultCodeConstants(-904, "award.not.enough");


    //anime///////
    public static final ResultCodeConstants TAGID_NOT_EXISTS = new ResultCodeConstants(-1000, "tagid.not.exists");
    public static final ResultCodeConstants TAGNAME_NOT_EXISTS = new ResultCodeConstants(-1001, "tagname.not.exists");
    public static final ResultCodeConstants TVID_NOT_EXISTS = new ResultCodeConstants(-1002, "tvid.not.exists");
    public static final ResultCodeConstants M3U8_NOT_EXISTS = new ResultCodeConstants(-1003, "m3u8.not.exists");
    public static final ResultCodeConstants SPECIALID_NOT_EXISTS = new ResultCodeConstants(-1004, "specialid.not.exists");


    //////////////User Center//////////////
    public static final ResultCodeConstants USERCENTER_TOKEN_VALIDATE_FAILED = new ResultCodeConstants(-10102, "token.validate.failed");
    public static final ResultCodeConstants USERCENTER_USERLOGIN_NOT_EXISTS = new ResultCodeConstants(-10103, "login.not.exists");
    public static final ResultCodeConstants USERCENTER_PROFILE_NOT_EXISTS = new ResultCodeConstants(-10104, "profile.not.exists");
    public static final ResultCodeConstants USERCENTER_PROFILE_HAS_BIND = new ResultCodeConstants(-10105, "profile.has.bind");
    public static final ResultCodeConstants USERCENTER_WRONG_DOMAIN = new ResultCodeConstants(-10106, "login.wrong.logindomain");
    public static final ResultCodeConstants USERCENTER_AUTH_SIGNERROR = new ResultCodeConstants(-10107, "auth.sign.error");
    public static final ResultCodeConstants USERCENTER_PROFILE_HAS_EXISTS = new ResultCodeConstants(-10108, "profile.has.exists");
    public static final ResultCodeConstants USERCENTER_ACCOUNT_HAS_EXISTS = new ResultCodeConstants(-10109, "user.has.exists");
    public static final ResultCodeConstants USERCENTER_LIKE_GAME_NOT_MORE_THAN_6 = new ResultCodeConstants(-10110, "profile.like.game.not.more.than.6");
    public static final ResultCodeConstants USERCENTER_NICK_HAS_MODIFY = new ResultCodeConstants(-10111, "nick.has.modify");
    public static final ResultCodeConstants USERCENTER_NICK_HAS_EXISTS = new ResultCodeConstants(-10112, "nick.has.exists");
    public static final ResultCodeConstants USERCENTER_LIKE_GAME_NOT_LESS_THAN_1 = new ResultCodeConstants(-10113, "profile.like.game.not.less.than.1");
    public static final ResultCodeConstants USERCENTER_LOGINKEY_ILLEGAL = new ResultCodeConstants(-10114, "loginkey.illegal");
    public static final ResultCodeConstants USERCENTER_PASSWORD_ILLEGAL = new ResultCodeConstants(-10115, "password.illegal");
    public static final ResultCodeConstants USERCENTER_PASSWORD_INCORRECT = new ResultCodeConstants(-10116, "password.incorrect");
    public static final ResultCodeConstants USERCENTER_IMPRESS_SIZE_NOT_MORE_THAN_6 = new ResultCodeConstants(-10117, "impress.size.not.more.than.6");
    public static final ResultCodeConstants USERCENTER_IMPRESS_EXIST = new ResultCodeConstants(-10118, "impress.exist");
    public static final ResultCodeConstants USERCENTER_PROFILEKEY_NOT_NULL = new ResultCodeConstants(-10119, "profilekey.not.null");
    public static final ResultCodeConstants USERCENTER_PASSWORD_LENGTH_OUT_LIMIT = new ResultCodeConstants(-10120, "password.length.out.limit");
    public static final ResultCodeConstants USERCENTER_PASSWORD_AGAIN_ERROR = new ResultCodeConstants(-10121, "password.again.error");
    public static final ResultCodeConstants USERCENTER_PASSWORD_NOT_NULL = new ResultCodeConstants(-10122, "password.not.null");
    public static final ResultCodeConstants USERCENTER_PROFILE_HASBIND_MOBILE = new ResultCodeConstants(-10123, "profile.hasbind.mobile");
    public static final ResultCodeConstants USERCENTER_USERLOGIN_HAS_EXISTS = new ResultCodeConstants(-10124, "login.has.exists");
    public static final ResultCodeConstants USERCENTER_PASSWORD_SAME_OLD = new ResultCodeConstants(-10125, "password.same.old");
    public static final ResultCodeConstants USERCENTER_NICK_ILLEGAL = new ResultCodeConstants(-10126, "nick.illegal");
    public static final ResultCodeConstants USERCENTER_FOLLOWSAME_USER = new ResultCodeConstants(-10127, "follow.same.user");
    public static final ResultCodeConstants USERCENTER_FOLLOW_USER_NO_AGREE = new ResultCodeConstants(-10128, "follow.user.no.agree");
    public static final ResultCodeConstants USERCENTER_FOLLOW_ALREADY_EXISTS = new ResultCodeConstants(-10129, "follow.already.exists");

    public static final ResultCodeConstants MOBILE_VERIFY_CODE_OUTLIMIT = new ResultCodeConstants(-10201, "mobile.verifycode.out.of.limit");
    public static final ResultCodeConstants MOBILE_VERIFY_CODE_FAILED = new ResultCodeConstants(-10202, "mobile.verifycode.failed");
    public static final ResultCodeConstants MOBILE_HAS_BINDED = new ResultCodeConstants(-10203, "mobile.has.binded");
    public static final ResultCodeConstants MOBILE_VERIFY_CODE_NULL = new ResultCodeConstants(-10204, "mobile.verifycode.empty");
    public static final ResultCodeConstants MOBILE_PHONE_NULL = new ResultCodeConstants(-10205, "mobile.phone.null");
    public static final ResultCodeConstants MOBILE_VERIFY_CODE_ERROR = new ResultCodeConstants(-10206, "mobile.verifycode.error");
    public static final ResultCodeConstants MOBILE_VERIFY_CODE_SENDERROR = new ResultCodeConstants(-10207, "mobile.verifycode.senderror");


    public static final ResultCodeConstants WIKI_VOTE_PARAM_UID_ERROR = new ResultCodeConstants(-10301, "missed.uid.or.uid.is.empty.or.uid.is.wrong");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_VOTETITLE_ERROR = new ResultCodeConstants(-10302, "votetitle.is.empty");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_CHOOSETYPE_ERROR = new ResultCodeConstants(-10303, "choosetype.can.only.be.1.or.2");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_MAXCHOOSEITEMS_ERROR = new ResultCodeConstants(-10304, "maxchooseitems.must.less.than.10.and.big.than.0");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_RESTRICT_ERROR = new ResultCodeConstants(-10305, "restrict.can.only.be.0.or.1.or.2.or.3");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_RESULTVISIBLE_ERROR = new ResultCodeConstants(-10306, "resultvisible.can.only.be.0.or.1.or.2");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_STARTTIME_ERROR = new ResultCodeConstants(-10307, "starttime.must.be.a.legal.value.and.cannot.be.smaller.than.today.");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_ENDTIME_ERROR = new ResultCodeConstants(-10308, "endtime.must.be.a.legal.value.and.cannot.be.larger.than.2145887999000");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_ENDTIME_LESS_THAN_STARTTIME_ERROR = new ResultCodeConstants(-10309, "endtime.must.bigger.than.starttime");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_OPTIONTITLES_ERROR = new ResultCodeConstants(-10310, "optiontitles.is.wrong");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_PICS_ERROR = new ResultCodeConstants(-10311, "pics.is.wrong");
    public static final ResultCodeConstants WIKI_VOTE_CREATE_OPTIONTITLES_NOT_MATCH_PICS_ERROR = new ResultCodeConstants(-10312, "optiontitles.donot.match.pics");
    public static final ResultCodeConstants WIKI_VOTE_VOTE_NOT_FOUND_ERROR = new ResultCodeConstants(-10313, "unikey.is.wrong.or.the.vote.had.been.deleted");
    public static final ResultCodeConstants WIKI_VOTE_PARAM_IP_ERROR = new ResultCodeConstants(-10314, "ip.address.is.null.or.wrong.format");
    public static final ResultCodeConstants WIKI_VOTE_PARAM_OPTION_ID_ERROR = new ResultCodeConstants(-10315, "vote_option_id.is.empty.or.wrong.value");
    public static final ResultCodeConstants WIKI_VOTE_PARAM_OPTION_ID_MORE_THAN_LIMIT_ERROR = new ResultCodeConstants(-10316, "vote_option_ids.are.more.than.limit");
    public static final ResultCodeConstants WIKI_VOTE_PARAM_UNIKEY_ERROR = new ResultCodeConstants(-10317, "missed.param.unikey.or.unikey.is.empty");
    public static final ResultCodeConstants WIKI_VOTE_VOTE_TIME_EXCEED_LIMIT_ERROR = new ResultCodeConstants(-10318, "vote.time.must.after.starttime.and.before.endtime");
    public static final ResultCodeConstants WIKI_VOTE_PARAM_VOTEOPTIONIDS_EMPTY_ERROR = new ResultCodeConstants(-10319, "missed.param.voteoptionids.or.voteoptionids.is.empty");
    public static final ResultCodeConstants WIKI_VOTE_PARAM_VOTEOPTIONIDS_AMOUNT_ERROR = new ResultCodeConstants(-10320, "the.amount.of.voteoptionids.do.not.meet.the.requirements");
    public static final ResultCodeConstants WIKI_VOTE_PARAM_VOTEOPTIONIDS_VALID_ERROR = new ResultCodeConstants(-10321, "at.least.one.voteoptionid.does.not.valid");
    public static final ResultCodeConstants WIKI_VOTE_VOTE_HAS_NO_OPTIONS_ERROR = new ResultCodeConstants(-10322, "the.vote.has.no.options");
    public static final ResultCodeConstants WIKI_VOTE_VOTE_IP_RESTRICT_VIOLATE_ERROR = new ResultCodeConstants(-10323, "the.ip.address.has.already.voted.today.");
    public static final ResultCodeConstants WIKI_VOTE_VOTE_PROFILE_ID_RESTRICT_VIOLATE_ERROR = new ResultCodeConstants(-10324, "the.uid.has.already.voted.");

    //小端
    public static final ResultCodeConstants GAME_GUIDE_RESULT_NULL = new ResultCodeConstants(-30000, "result.null");
    public static final ResultCodeConstants GAME_GUIDE_PARAM_NULL = new ResultCodeConstants(-30001, "param.null");
    public static final ResultCodeConstants MINI_CLIENT_PARAM_APPKEY_NULL = new ResultCodeConstants(-30002, "param.appkey.null");
    public static final ResultCodeConstants MINI_CLIENT_PARAM_PLATFORM_NULL = new ResultCodeConstants(-30003, "param.platform.null");

    //评论
    public static final ResultCodeConstants COMMENT_PARAM_UNIKEY_NULL = new ResultCodeConstants(-40000, "comment.param.unikey.null");
    public static final ResultCodeConstants COMMENT_PARAM_DOMAIN_NULL = new ResultCodeConstants(-40001, "comment.param.domain.null");
    public static final ResultCodeConstants COMMENT_PARAM_JSONPARAM_NULL = new ResultCodeConstants(-40002, "comment.param.jsonparam.null");
    public static final ResultCodeConstants COMMENT_PARAM_COMMENTPARAM_ERROR = new ResultCodeConstants(-40003, "comment.param.body.error");
    public static final ResultCodeConstants COMMENT_PARAM_SCORE_NULL = new ResultCodeConstants(-40004, "comment.param.score.null");
    public static final ResultCodeConstants COMMENT_PARAM_BODY_NULL = new ResultCodeConstants(-40005, "comment.param.body.null");
    public static final ResultCodeConstants COMMENT_PARAM_OID_ERROR = new ResultCodeConstants(-40006, "comment.param.oid.error");
    public static final ResultCodeConstants COMMENT_PARAM_PID_ERROR = new ResultCodeConstants(-40007, "comment.param.pid.error");
    public static final ResultCodeConstants COMMENT_BEAN_NULL = new ResultCodeConstants(-40008, "comment.bean.null");
    public static final ResultCodeConstants COMMENT_ROOT_REPLY_NULL = new ResultCodeConstants(-40009, "comment.root.reply.null");
    public static final ResultCodeConstants COMMENT_PARENT_REPLY_NULL = new ResultCodeConstants(-40010, "comment.parent.reply.null");
    public static final ResultCodeConstants COMMENT_PARAM_RID_NULL = new ResultCodeConstants(-40011, "comment.param.rid.null");
    public static final ResultCodeConstants COMMENT_PARAM_RID_ERROR = new ResultCodeConstants(-40012, "comment.param.rid.error");
    public static final ResultCodeConstants COMMENT_PARAM_DOMAIN_ERROR = new ResultCodeConstants(-40013, "comment.param.domain.error");
    public static final ResultCodeConstants COMMENT_SCORE_TWENTY_TIMES_A_DAY_ONE_COMMENT = new ResultCodeConstants(-40014, "comment.score.twenty.times.a.day");
    public static final ResultCodeConstants COMMENT_PARAM_OID_NULL = new ResultCodeConstants(-40015, "comment.param.oid.null");
    public static final ResultCodeConstants COMMENT_HAS_AGREE = new ResultCodeConstants(-40016, "comment.has.agree");
    public static final ResultCodeConstants COMMENT_REPLY_BODY_TEXT_ILLEGE = new ResultCodeConstants(-40017, "comment.reply.body.text.illege");//敏感词
    public static final ResultCodeConstants COMMENT_REPLY_NULL = new ResultCodeConstants(-40018, "comment.reply.null");
    public static final ResultCodeConstants COMMENT_PROFILE_FORBID = new ResultCodeConstants(-40019, "comment.profile.forbid");
    public static final ResultCodeConstants COMMENT_POST_SAME_TEXT_INTERVAL = new ResultCodeConstants(-40020, "comment.post.same.text.interval");//一分钟不能发相同的内容
    public static final ResultCodeConstants COMMENT_DELETE_FAILED = new ResultCodeConstants(-40021, "comment.delete.failed");//
    public static final ResultCodeConstants COMMENT_POST_SAME_TEXT_FIFTEEN_INTERVAL = new ResultCodeConstants(-40022, "comment.post.text.15sec.interval");//

    public static final ResultCodeConstants UPLOAD_IMAGE_FAILED = new ResultCodeConstants(-50015, "upload_image_failed");


    public static final ResultCodeConstants GAMEDB_GAME_NOTEXISTS = new ResultCodeConstants(-60001, "game.not.exists", "游戏不存在");

    //
    public static final ResultCodeConstants TASK_NOT_EXISTS = new ResultCodeConstants(-70000, "task.not.exists");
    public static final ResultCodeConstants TASK_HAS_COMPLETE = new ResultCodeConstants(-70001, "task.has.complete");
    public static final ResultCodeConstants TASK_GETAWARD_FALIED = new ResultCodeConstants(-70002, "task.getaward.failed");


    public static final ResultCodeConstants PIC_NOT_EXISTS = new ResultCodeConstants(-80000, "pic.removed.falied");
    public static final ResultCodeConstants PIC_REMOVED_FAILED = new ResultCodeConstants(-80001, "pic.removed.falied");
    //游戏库
    public static final ResultCodeConstants GAME_COLLECTION_HAS_LIKE_GAME = new ResultCodeConstants(-90000, "user.has.like.game");
    public static final ResultCodeConstants GAME_COLLECTION_HAS_EXIST = new ResultCodeConstants(-90001, "game.has.exist");
    //优酷
    public static final ResultCodeConstants GOODS_SECKILL_RESULT_NULL = new ResultCodeConstants(-20000, "goods.seckill.result.null");
    //圈子
    public static final ResultCodeConstants ANIME_TAG_IS_NULL = new ResultCodeConstants(-10001, "anime.tag.is.null");
    //防刷返回

    public static final ResultCodeConstants VALID_INFO_FAILED = new ResultCodeConstants(-20004, "valid.info.failed");

    public static final ResultCodeConstants POINT_NOT_ENOUGH = new ResultCodeConstants(-2000, "point.not.enough");
    public static final ResultCodeConstants EXCEEDS_LIMIT = new ResultCodeConstants(-2001, "exceeds.limit");
    public static final ResultCodeConstants GIFT_LOTTERY_NOT_ENOUGH = new ResultCodeConstants(-2002, "GIFT_LOTTERY_NOT_ENOUGH"); //宝箱不够
    public static final ResultCodeConstants GIFT_LOTTERY_NOT_EXISTS = new ResultCodeConstants(-2003, "GIFT_LOTTERY_NOT_EXISTS"); //没有奖品
    public static final ResultCodeConstants GIFT_LOTTERY_EXIST = new ResultCodeConstants(-2004, "GIFT_LOTTERY_EXIST"); //已经存在 返还积分


    public static final ResultCodeConstants FORMAT_ERROR = new ResultCodeConstants(-2005, "format.error","数据格式化失败");
    public static final ResultCodeConstants USER_COLLECT_EXIST = new ResultCodeConstants(-2006, "user.collect.exist","收藏已存在");
    public static final ResultCodeConstants CONTENT_NOT_EXIST = new ResultCodeConstants(-2007, "content.not.exist","文章不存在");
    public static final ResultCodeConstants NOT_COLLECT = new ResultCodeConstants(-2008, "user.collect.not.exist","收藏不存在");
    public static final ResultCodeConstants GAME_WIKI_NOT_ADD = new ResultCodeConstants(-2009, "game_wiki_not_add","");

    private String msg;
    private int code;
    private String extmsg;

    public ResultCodeConstants(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public ResultCodeConstants(int code, String msg, String extmsg) {
        this.msg = msg;
        this.code = code;
        this.extmsg = extmsg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public String getExtmsg() {
        return extmsg;
    }

    public void setExtmsg(String extmsg) {
        this.extmsg = extmsg;
    }

    public String getJsonString() {
        return getJsonObject().toString();
    }

    public JSONObject getJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", String.valueOf(this.getCode()));
        jsonObject.put("msg", this.getMsg());
        jsonObject.put("extmsg", StringUtil.isEmpty(this.getExtmsg()) ? this.getMsg() : this.getExtmsg());
        return jsonObject;
    }


    public static String resultCheckCallback(JSONObject jsonObject, String callback) {
        if (StringUtil.isEmpty(callback)) {
            return jsonObject.toString();
        } else {
            return callback + "([" + jsonObject.toString() + "])";
        }
    }

    public String getJsonString(String callback) {
        String jsonString = getJsonString();
        return StringUtil.isEmpty(callback) ? jsonString : callback + "([" + jsonString + "])";
    }

}
