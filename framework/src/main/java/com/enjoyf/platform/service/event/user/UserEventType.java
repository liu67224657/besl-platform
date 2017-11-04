/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.user;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class UserEventType implements Serializable {
    private static Map<String, UserEventType> map = new HashMap<String, UserEventType>();

    //the account events.
    public static final UserEventType USER_ACCOUNT_REGISTER = new UserEventType(UserEventTypePrefix.ACCOUNT, "register");
    public static final UserEventType USER_ACCOUNT_LOGIN = new UserEventType(UserEventTypePrefix.ACCOUNT, "login");

    public static final UserEventType USER_ACCOUNT_CHPWD_APPLY = new UserEventType(UserEventTypePrefix.ACCOUNT, "chgpwd.apply");
    public static final UserEventType USER_ACCOUNT_CHPWD_OK = new UserEventType(UserEventTypePrefix.ACCOUNT, "chgpwd.ok");

    public static final UserEventType USER_ACCOUNT_EMAIL_AUTH_APPLY = new UserEventType(UserEventTypePrefix.ACCOUNT, "eauth.apply");
    public static final UserEventType USER_ACCOUNT_EMAIL_AUTH_OK = new UserEventType(UserEventTypePrefix.ACCOUNT, "eauth.ok");

    public static final UserEventType USER_ACCOUNT_BLOG_SET = new UserEventType(UserEventTypePrefix.ACCOUNT, "blog.set");
    public static final UserEventType USER_ACCOUNT_PROFILE_SET = new UserEventType(UserEventTypePrefix.ACCOUNT, "profile.set");

    public static final UserEventType USER_ACCOUNT_SEARCH = new UserEventType(UserEventTypePrefix.ACCOUNT, "search");
    public static final UserEventType USER_ACCOUNT_SEARCH_CLICK = new UserEventType(UserEventTypePrefix.ACCOUNT, "search.click");

    //the content events
    public static final UserEventType USER_CONTENT_POST = new UserEventType(UserEventTypePrefix.CONTENT, "post");
    public static final UserEventType USER_CONTENT_REMOVE = new UserEventType(UserEventTypePrefix.CONTENT, "remove");

    public static final UserEventType USER_CONTENT_PHOTO_POST = new UserEventType(UserEventTypePrefix.CONTENT, "photo.post");
    public static final UserEventType USER_CONTENT_AUDIO_POST = new UserEventType(UserEventTypePrefix.CONTENT, "audio.post");
    public static final UserEventType USER_CONTENT_VIDEO_POST = new UserEventType(UserEventTypePrefix.CONTENT, "video.post");
    public static final UserEventType USER_CONTENT_VOTE_POST = new UserEventType(UserEventTypePrefix.CONTENT, "vote.post");

    public static final UserEventType USER_CONTENT_REPLY_POST = new UserEventType(UserEventTypePrefix.CONTENT, "reply.post");
    public static final UserEventType USER_CONTENT_REPLY_REMOVE = new UserEventType(UserEventTypePrefix.CONTENT, "reply.rm");

    public static final UserEventType USER_CONTENT_FORWARD = new UserEventType(UserEventTypePrefix.CONTENT, "forward");

    public static final UserEventType USER_CONTENT_DING = new UserEventType(UserEventTypePrefix.CONTENT, "ding");

    public static final UserEventType USER_CONTENT_FAVOR_ADD = new UserEventType(UserEventTypePrefix.CONTENT, "favor.add");
    public static final UserEventType USER_CONTENT_FAVOR_REMOVE = new UserEventType(UserEventTypePrefix.CONTENT, "favor.rm");
    public static final UserEventType USER_CONTENT_FAVOR_CLICK = new UserEventType(UserEventTypePrefix.CONTENT, "favor.click");

    public static final UserEventType USER_CONTENT_TAG_FAVOR_ADD = new UserEventType(UserEventTypePrefix.CONTENT, "tag.favor.add");
    public static final UserEventType USER_CONTENT_TAG_FAVOR_REMOVE = new UserEventType(UserEventTypePrefix.CONTENT, "tag.favor.rm");
    public static final UserEventType USER_CONTENT_TAG_FAVOR_CLICK = new UserEventType(UserEventTypePrefix.CONTENT, "tag.favor.click");

    public static final UserEventType USER_CONTENT_TAG_CLICK = new UserEventType(UserEventTypePrefix.CONTENT, "tag.click");
    public static final UserEventType USER_CONTENT_TAG_POST = new UserEventType(UserEventTypePrefix.CONTENT, "tag.post");

    public static final UserEventType USER_CONTENT_WALL_VIEW = new UserEventType(UserEventTypePrefix.CONTENT, "wall.view");
    public static final UserEventType USER_CONTENT_WALL_CLICK = new UserEventType(UserEventTypePrefix.CONTENT, "wall.click");
    public static final UserEventType USER_CONTENT_HOT_VIEW = new UserEventType(UserEventTypePrefix.CONTENT, "hot.view");

    public static final UserEventType USER_CONTENT_SEARCH = new UserEventType(UserEventTypePrefix.CONTENT, "search");
    public static final UserEventType USER_CONTENT_SEARCH_CLICK = new UserEventType(UserEventTypePrefix.CONTENT, "search.click");

    public static final UserEventType USER_GAME_SEARCH = new UserEventType(UserEventTypePrefix.CONTENT, "search.game");
    public static final UserEventType USER_GAME_SEARCH_CLICK = new UserEventType(UserEventTypePrefix.CONTENT, "search.game.click");

    public static final UserEventType USER_CONTENT_SHORTURL_CLICK = new UserEventType(UserEventTypePrefix.CONTENT, "shorturl.click");

    public static final UserEventType USER_ACTIVITY_PAGE_CLICK = new UserEventType(UserEventTypePrefix.CONTENT, "activity.page.click");

    //the social events
    public static final UserEventType USER_SOCIAL_FOCUS = new UserEventType(UserEventTypePrefix.SOCIAL, "focus");
    public static final UserEventType USER_SOCIAL_UNFOCUS = new UserEventType(UserEventTypePrefix.SOCIAL, "unfocus");

    public static final UserEventType USER_SOCIAL_MESSAGE_POST = new UserEventType(UserEventTypePrefix.SOCIAL, "msg.post");
    public static final UserEventType USER_SOCIAL_MESSAGE_REMOVE = new UserEventType(UserEventTypePrefix.SOCIAL, "msg.rm");

    public static final UserEventType USER_CONTENT_SYNC = new UserEventType(UserEventTypePrefix.CONTENT, "sync.content");
    //

    //
    private UserEventTypePrefix prefix;
    private String code;

    public UserEventType(UserEventTypePrefix d, String c) {
        this.prefix = d;
        this.code = d.getCode() + UserEventTypePrefix.KEY_DOMAIN_SEPARATOR + c.toLowerCase();

        map.put(code, this);
    }

    public UserEventTypePrefix getPrefix() {
        return prefix;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "UserEventType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof UserEventType)) {
            return false;
        }

        return code.equalsIgnoreCase(((UserEventType) obj).getCode());
    }

    public static UserEventType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<UserEventType> getAll() {
        return map.values();
    }
}
