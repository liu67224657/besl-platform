package com.enjoyf.platform.webapps.common.wordfilter;

import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONObject;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-22 下午4:20
 * Description:
 */
public class WanbaResultCodeConstants extends ResultCodeConstants {

    public static final WanbaResultCodeConstants WANBA_TAG_NOTEXISTS = new WanbaResultCodeConstants(WANBABASECODE - 1, "tag.not.exists");

    public static final WanbaResultCodeConstants WANBA_ASK_TITLE_ILLEGLE = new WanbaResultCodeConstants(WANBABASECODE - 100, "ask.title.illegle");
    public static final WanbaResultCodeConstants WANBA_ASK_QUESTION_OUT_TIMELIMIT = new WanbaResultCodeConstants(WANBABASECODE - 101, "question.out.timelimit");
    public static final WanbaResultCodeConstants WANBA_ASK_QUESTION_OUT_EXISTS = new WanbaResultCodeConstants(WANBABASECODE - 102, "question.not.exists");
    public static final WanbaResultCodeConstants WANBA_ASK_QUESTION_TYPE_ERROR = new WanbaResultCodeConstants(WANBABASECODE - 103, "question.type.error");
    public static final WanbaResultCodeConstants WANBA_ASK_QUESTION_HAS_ANSWER = new WanbaResultCodeConstants(WANBABASECODE - 104, "question.has.answer");
    public static final WanbaResultCodeConstants WANBA_ASK_ANSWERPROFILE_NOT_INVITED = new WanbaResultCodeConstants(WANBABASECODE - 105, "answerprofile.not.invited");
    public static final WanbaResultCodeConstants WANBA_ASK_ANSWERHAS_NOT_EXISTS = new WanbaResultCodeConstants(WANBABASECODE - 106, "answer.not.exists");
    public static final WanbaResultCodeConstants WANBA_ASK_ANSWER_AGREE_FAILED = new WanbaResultCodeConstants(WANBABASECODE - 107, "answer.agree.failed", "你已经赞过了");
    public static final WanbaResultCodeConstants WANBA_ASK_QUESTIONFOLLOW_FAILED = new WanbaResultCodeConstants(WANBABASECODE - 108, "question.follow.failed");
    public static final WanbaResultCodeConstants WANBA_ASK_QUESTIONUNFOLLOW_FAILED = new WanbaResultCodeConstants(WANBABASECODE - 109, "question.unfollow.failed");
    public static final WanbaResultCodeConstants WANBA_ASK_QUESTIONINVITE_FAILED = new WanbaResultCodeConstants(WANBABASECODE - 110, "question.invite.failed");
    public static final WanbaResultCodeConstants WANBA_USER_QUESTION_POINT_CONFIG = new WanbaResultCodeConstants(WANBABASECODE - 111, "question.point.config.error");
    public static final WanbaResultCodeConstants WANBA_ASK_ANSWER_ACCEPT = new WanbaResultCodeConstants(WANBABASECODE - 112, "question.answer.accept");

    //todo
    public static final WanbaResultCodeConstants WANBA_ASK_ACCEPTPROFILE_ISNOT_ASKPROFILE = new WanbaResultCodeConstants(WANBABASECODE - 200, "acceptprofile.isnot.askprofile");
    public static final WanbaResultCodeConstants WANBA_VALIDATE_OLD_PHONE_FAIL = new WanbaResultCodeConstants(WANBABASECODE - 201, "validate.old.phone.fail");
    public static final WanbaResultCodeConstants VALIDATE_OLD_PHONE_EMPTY = new WanbaResultCodeConstants(WANBABASECODE - 202, "validate.old.phone.empty");
    public static final WanbaResultCodeConstants AUTH_UNBIND_ERROR_ONLYONE = new WanbaResultCodeConstants(WANBABASECODE - 203, "auth.unbind.error.onlyone");
    public static final WanbaResultCodeConstants MODIFY_MOBILE_ERROR = new WanbaResultCodeConstants(WANBABASECODE - 204, "modify.mobile.error");

    public static final WanbaResultCodeConstants FAVORITE_EXISTS = new WanbaResultCodeConstants(WANBABASECODE - 205, "favorite.exists");
    public static final WanbaResultCodeConstants REMOVE_FAVORITE_ERROR = new WanbaResultCodeConstants(WANBABASECODE - 206, "remove.favorite.error");

    //notice
    public static final WanbaResultCodeConstants USERNOTICE_NOTEXISTS = new WanbaResultCodeConstants(WANBABASECODE - 301, "notice.notexists");
    public static final WanbaResultCodeConstants USERNOTICE_PROFILENOTEQ = new WanbaResultCodeConstants(WANBABASECODE - 301, "notice.profile.notequals");

    //point
    public static final WanbaResultCodeConstants WANBA_USER_POINT_NOT_ENOUGH = new WanbaResultCodeConstants(WANBABASECODE - 401, "user.point.enough");


    public WanbaResultCodeConstants(int code, String msg) {
        super(code, msg);
    }

    public WanbaResultCodeConstants(int code, String msg, String extmsg) {
        super(code, msg, extmsg);
    }
}
