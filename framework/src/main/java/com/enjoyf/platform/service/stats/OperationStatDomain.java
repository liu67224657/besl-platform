/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.stats;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-17 下午8:17
 * Description:
 */
public class OperationStatDomain extends AbstractStatDomain {
    //pvs
    public static OperationStatDomain PAGE_VIEW = new OperationStatDomain("pv", false);
    public static OperationStatDomain UNIQUE_USER = new OperationStatDomain("uv", false);
    public static OperationStatDomain IP = new OperationStatDomain("ip", false);
    public static OperationStatDomain TOS = new OperationStatDomain("tos", false);

    public static OperationStatDomain UV_REF = new OperationStatDomain("uv.ref", true);
    public static OperationStatDomain PV_PAGE = new OperationStatDomain("pv.page", true);

    //back
    public static OperationStatDomain BACK = new OperationStatDomain("back", true);

    //content
    public static OperationStatDomain CONTENT_VIEW_TOP = new OperationStatDomain("cnt.view.top", false);
    public static OperationStatDomain CONTENT_HOT_TOP = new OperationStatDomain("cnt.hot.top", false);

    //user event
    public static OperationStatDomain USER_EVENT_POST = new OperationStatDomain("ue.post", false);
    public static OperationStatDomain USER_EVENT_COMMENT = new OperationStatDomain("ue.comment", false);
    public static OperationStatDomain USER_EVENT_FORWARD = new OperationStatDomain("ue.forward", false);
    public static OperationStatDomain USER_EVENT_LIKE = new OperationStatDomain("ue.like", false);
    public static OperationStatDomain USER_EVENT_REGISTER = new OperationStatDomain("ue.register", false);

    public static OperationStatDomain USER_EVENT_REGISTER_REF = new OperationStatDomain("ue.register.ref", true);

    public static OperationStatDomain USER_EVENT_POST_PHASE = new OperationStatDomain("ue.post.phase", true);
    public static OperationStatDomain USER_EVENT_COMMENT_PHASE = new OperationStatDomain("ue.comment.phase", true);
    public static OperationStatDomain USER_EVENT_FORWARD_PHASE = new OperationStatDomain("ue.forward.phase", true);
    public static OperationStatDomain USER_EVENT_LIKE_PHASE = new OperationStatDomain("ue.like.phase", true);

    public static OperationStatDomain USER_EVENT_POST_TOP = new OperationStatDomain("ue.post.top", true);
    public static OperationStatDomain USER_EVENT_COMMENT_TOP = new OperationStatDomain("ue.comment.top", true);
    public static OperationStatDomain USER_EVENT_FORWARD_TOP = new OperationStatDomain("ue.forward.top", true);
    public static OperationStatDomain USER_EVENT_LIKE_TOP = new OperationStatDomain("ue.like.top", true);

    protected OperationStatDomain(String c, boolean multi) {
        super(StatDomainPrefix.DOMAIN_PREFIX_OPS.getCode() + StatDomainPrefix.KEY_DOMAIN_SEPARATOR + c, multi);
    }
}
