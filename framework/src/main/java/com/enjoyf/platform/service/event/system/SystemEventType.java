/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

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
public class SystemEventType implements Serializable {
    private static Map<String, SystemEventType> map = new HashMap<String, SystemEventType>();
    //new accoun register.
    public static final SystemEventType ACCOUNT_REGISTER = new SystemEventType("account.register");
    public static final SystemEventType INVITE_REGISTER = new SystemEventType("invite.register");
    //timeline insert events.
    public static final SystemEventType TIMELINE_INSERT = new SystemEventType("timeline.insert");
    public static final SystemEventType TIMELINE_REMOVE = new SystemEventType("timeline.remove");
    public static final SystemEventType TIMELINE_INIT = new SystemEventType("timeline.init");
    public static final SystemEventType TIMELINE_FOCUS_REMOVE = new SystemEventType("timeline.focus.remove");
    public static final SystemEventType TIMELINE_FAVORITE_CONTENT_INSERT = new SystemEventType("timeline.favorite.content.insert");
    public static final SystemEventType TIMELINE_FAVORITE_CONTENT_REMOVE = new SystemEventType("timeline.favorite.content.remove");
    //notice insert events
    public static final SystemEventType NOTICE_INSERT = new SystemEventType("notice.insert");
    public static final SystemEventType NOTICE_RESET = new SystemEventType("notice.reset");
    //social broadcast event
    public static final SystemEventType SOCIAL_CONTENT_POST_BROADCAST = new SystemEventType("social.content.post.broadcast");
    public static final SystemEventType SOCIAL_CONTENT_REMOVE_BROADCAST = new SystemEventType("social.content.remove.broadcast");
    public static final SystemEventType SOCIAL_REPLY_POST_BROADCAST = new SystemEventType("social.reply.post.broadcast");
    public static final SystemEventType SOCIAL_REPLY_REMOVE_BROADCAST = new SystemEventType("social.reply.remove.broadcast");
    public static final SystemEventType SOCIAL_CATEGORY_REMOVE = new SystemEventType("social.category.remove");
    public static final SystemEventType SOCIAL_CONTENT_FAVORITE_BROADCAST = new SystemEventType("social.content.favorite.broadcast");
    public static final SystemEventType SOCIAL_CONTENT_FAVORITE_REMOVE_BROADCAST = new SystemEventType("social.content.favorite.remove.broadcast");
    //content sum increase event
    public static final SystemEventType CONTENT_SUM_INCREASE = new SystemEventType("content.sum.increase");
    public static final SystemEventType CONTENT_SUM_SET = new SystemEventType("content.sum.set");
    public static final SystemEventType CONTENT_FAVORITE = new SystemEventType("content.favorite");
    public static final SystemEventType CONTENT_FAVORITE_REMOVE = new SystemEventType("content.favorite.remove");
    public static final SystemEventType CONTENT_REPLIED = new SystemEventType("content.replied");
    public static final SystemEventType CONTENT_POST = new SystemEventType("content.post");
    public static final SystemEventType CONTENT_RELATION_CREATE = new SystemEventType("content.relation.create");
    public static final SystemEventType CONTENT_RELATION_REMOVE = new SystemEventType("content.relation.remove");
    public static final SystemEventType SOCIAL_CONTENT_RELATION_CREATE = new SystemEventType("social.content.relation.create");
    public static final SystemEventType SOCIAL_TIMELINE_RELATION_CREATE = new SystemEventType("social.timeline.relation.create");
    public static final SystemEventType SOCIAL_TIMELINE_RELATION_REMOVE = new SystemEventType("social.timeline.relation.remove");
    public static final SystemEventType CONTENT_INTERACTION_SUM_INCREASE = new SystemEventType("contentinteraction.sum.increase");
    public static final SystemEventType SOCIAL_CONTENT_HOT_INSERT = new SystemEventType("social.content.hot.insert");
    public static final SystemEventType HOT_ACTIVITY = new SystemEventType("content.activity.hot.ranks");
    //profile sum increase event

    public static final SystemEventType PROFILE_SUM_SET = new SystemEventType("profile.sum.set");
    public static final SystemEventType PROFILE_ONLINE_ON = new SystemEventType("profile.online.on");
    public static final SystemEventType PROFILE_ONLINE_OFF = new SystemEventType("profile.online.off");
    public static final SystemEventType PROFILE_LAST_INTERACTION_INCREASE = new SystemEventType("profile.last.interaction.increase");
    public static final SystemEventType CHECK_PROFILE_MOBILE_DEVICE = new SystemEventType("check.profile.mobile.device");
    //habit tag
    public static final SystemEventType HABIT_TAG_SAVE = new SystemEventType("habit.tag.save");
    //short url
    public static final SystemEventType SHORTURL_SUM_INCREASE = new SystemEventType("shorturl.sum.increase");
    public static final SystemEventType SYNC_POST_CONTENT = new SystemEventType("sync.post.content");
    public static final SystemEventType RESOURCE_IOS_INSERT = new SystemEventType("resource.ios.insert");
    public static final SystemEventType GAME_SUM_INCREASE = new SystemEventType("game.sum.increase");
    public static final SystemEventType BILLING_ENCOURAGE_EVENT = new SystemEventType("billing.encourage");
    //viewline
    public static final SystemEventType VIEWLINE_ITEM_SUM_INCREASE_EVENT = new SystemEventType("viewline.item.sumincrease");
    public static final SystemEventType VIEWLINE_ITEM_INSERT_EVENT = new SystemEventType("viewline.item.insert");
    public static final SystemEventType VIEWLINE_ITEM_REMOVE_EVENT = new SystemEventType("viewline.item.remove");
    public static final SystemEventType VIEWLINE_POST_SYSTEM_MESSAGE_EVENT = new SystemEventType("viewline.post.system.message");
    //advertise events
    public static final SystemEventType ADV_PUBLISH_CLICK = new SystemEventType("adv.publish.click");
    public static final SystemEventType ADV_PAGE_VIEW = new SystemEventType("adv.page.view");
    public static final SystemEventType ADV_USER_REGISTER = new SystemEventType("adv.user.register");
    public static final SystemEventType SOCIAL_RECOMMEND_EVENT = new SystemEventType("social.recommend.event");
    public static final SystemEventType SOCIAL_RECOMMEND_MODIFY_EVENT = new SystemEventType("social.recommend.modify");
    public static final SystemEventType JOYMEAPP_PUSHMESSAGEDEVICE_REGISTER = new SystemEventType("joymeapp.pushmessagedevice.register");
    public static final SystemEventType SHARE_EXCHANGE_GIFT = new SystemEventType("share.exchange.gift");
    public static final SystemEventType ACTIVITY_CALEVENTTIME_GIFT = new SystemEventType("activity.event.type");
    public static final SystemEventType MESSAGE_SOCIAL_MESSAGE_CREATE = new SystemEventType("message.social.message.create");



    public static final SystemEventType LOCATION_INSERT = new SystemEventType("location.insert");
    public static final SystemEventType SOCIAL_INCREASE_SUM = new SystemEventType("social.increase.sum");
    public static final SystemEventType USER_POINT_INCREASE = new SystemEventType("point.user.point.increase");
    public static final SystemEventType SOCIAL_DEFAULT_FOCUS = new SystemEventType("social.default.focus");
    public static final SystemEventType PROFILE_SOLRJ_EVENT = new SystemEventType("profile.solrj.event");
    public static final SystemEventType USERCENTER_SUM_INCREASE = new SystemEventType("usercenter.sum.increase");
    public static final SystemEventType TASK_AWARD_EVENT = new SystemEventType("task.award.event");
    public static final SystemEventType GAMEDB_MODIFYTIME_EVENT = new SystemEventType("gamedb.modifytime.event");
    public static final SystemEventType GAMEDB_PC_AGREE_EVENT = new SystemEventType("gamedb.pc.agree.event");
    public static final SystemEventType GAMEDB_SUM_INCREASE_EVENT = new SystemEventType("gamedb.sum.increase");

    public static final SystemEventType PROFILE_SUM_INCREASE = new SystemEventType("profile.sum.increase");
    public static final SystemEventType USERRELATION_BUILD = new SystemEventType("userrelation.build");
    public static final SystemEventType USERRELATION_REMOVE = new SystemEventType("userrelation.remove");
    public static final SystemEventType USERTIMELINE_INSERT = new SystemEventType("usertimeline.insert");
    public static final SystemEventType USERTIMELINE_INSERT_BOARD = new SystemEventType("usertimeline.insert.board");


    //push
    public static final SystemEventType APP_PUSH_EVENT = new SystemEventType("app.push.event");

    public static final SystemEventType ACTIVITY_USER = new SystemEventType("activity.user");

    //GameClient
    public static final SystemEventType GAMECLIENT_MIYOU_POST_EVENT = new SystemEventType("gc.miyou.post");
    public static final SystemEventType MIYOU_ADD_LONGSUM = new SystemEventType("miyou.add.longsum");

    //pc
    public static final SystemEventType PCSTAT_GAME_STAT_PCSTAT_EVENT = new SystemEventType("pcstat.game.stat.pcstat.event");

    //new accoun register.
    public static final SystemEventType ADVERTISE_DEVICE_ACTIVITY = new SystemEventType("advertise.device.activity");

    public static final SystemEventType ADVERTISE_DEVICE_CLICK = new SystemEventType("advertise.device.click");

    /////////UGC wiki//////////////////
    public static final SystemEventType WIKI_PROFILE_SUM_INCREASE = new SystemEventType("wiki.profile.sum.increase");

    public static final SystemEventType WIKI_NOTICE = new SystemEventType("wiki.notice.send");

    public static final SystemEventType WIKI_SOCIAL_STATUS_BROAD = new SystemEventType("wiki.social.broad");

    public static final SystemEventType JOYMEAPP_CLIENT_LINE_ITEM = new SystemEventType("client.line.item");

    public static final SystemEventType GAME_INCR_COLLECTION_LIST_CACHE = new SystemEventType("incr.game.collection.list.cache");

    public static final SystemEventType VOTE_KEY = new SystemEventType("vote.key");

    public static final SystemEventType WANBA_PROFILESUM_INCREASE = new SystemEventType("wanba.profilesum.increase");

    public static final SystemEventType WANBA_QUESTION_NOTICE = new SystemEventType("wanba.question.notice");

    public static final SystemEventType WANBA_REPLY_NOTICE = new SystemEventType("wanba.reply.notice");

    public static final SystemEventType WIKI_NOTICE_EVENT=new SystemEventType("wiki.notice.event");


    public static final SystemEventType WIKIAPP_ADDSEARCH_EVENT=new SystemEventType("wikiapp.addsearch.event");

    public static final SystemEventType WIKIAPP_DELSEARCH_EVENT=new SystemEventType("wikiapp.delsearch.event");

    //point evnt
    public static final SystemEventType POINT_RECOMMEND_EVENT = new SystemEventType("point.recommend");

    //
    private String code;


    public SystemEventType(String c) {
        this.code = c.toLowerCase();

        map.put(code, this);
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
        return "SystemEventType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SystemEventType)) {
            return false;
        }

        return code.equalsIgnoreCase(((SystemEventType) obj).getCode());
    }

    public static SystemEventType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<SystemEventType> getAll() {
        return map.values();
    }
}
