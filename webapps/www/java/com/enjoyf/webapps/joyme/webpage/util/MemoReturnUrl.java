package com.enjoyf.webapps.joyme.webpage.util;

import com.enjoyf.platform.service.message.NoticeType;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:读取小纸条的跳转容器类类。
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class MemoReturnUrl {
    //容器 key Memokey value 存放地址
    private static Map<NoticeType, String> map = new HashMap<NoticeType, String>();

    private static MemoReturnUrl FOCUS_RETUENURL = new MemoReturnUrl(NoticeType.NEW_FANS, "redirect:/social/fans/list");
    private static MemoReturnUrl REPLY_RETUENURL = new MemoReturnUrl(NoticeType.NEW_REPLY, "redirect:/reply/receivelist");
    private static MemoReturnUrl MESSAGE_RETUENURL = new MemoReturnUrl(NoticeType.NEW_MESSAGE, "redirect:/message/private/list");
    private static MemoReturnUrl NOTICE_RETUENURL = new MemoReturnUrl(NoticeType.NEW_BULLETIN, "redirect:/message/notice/list");
    private static MemoReturnUrl CONTENT_RETUENURL = new MemoReturnUrl(NoticeType.NEW_CONTENT, "redirect:/home");
    private static MemoReturnUrl COMPLETE_PROFILE_RETUENURL = new MemoReturnUrl(NoticeType.COMPLETE_PROFILE, "redirect:/userinfostep");
    private static MemoReturnUrl EMAIL_AUTH_RETUENURL = new MemoReturnUrl(NoticeType.MAIL_AUTH, "redirect:/security/email/auth/send");
    private static MemoReturnUrl AT_RETURN_URL = new MemoReturnUrl(NoticeType.NEW_AT, "redirect:/atme");

    private NoticeType type;
    private String returnUrl;

    private MemoReturnUrl(NoticeType type, String returnUrl) {
        this.type = type;
        this.returnUrl = returnUrl;
        map.put(type, returnUrl);
    }

    /**
     * 通过key得到返回地址
     *
     * @param type
     * @return
     */
    public static String getUrlByKey(NoticeType type) {
        return map.containsKey(type) ? map.get(type) : null;
    }

}
