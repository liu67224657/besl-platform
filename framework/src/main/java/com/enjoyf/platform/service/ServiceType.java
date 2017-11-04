package com.enjoyf.platform.service;

import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.billing.BillingServiceSngl;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.event.EventReceiver;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
//import com.enjoyf.platform.service.habit.HabitServiceSngl;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.notice.NoticeServiceSngl;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.search.SearchServiceSngl;
import com.enjoyf.platform.service.social.SocialServiceSngl;
import com.enjoyf.platform.service.stats.StatServiceSngl;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.service.timeline.TimeLineServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.viewline.ViewLineServiceSngl;
import com.enjoyf.platform.service.vote.VoteServiceSngl;
import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class ServiceType {
    private static Map<String, ServiceType> map = new HashMap<String, ServiceType>();

    //all the service list
    public static final ServiceType EVENT = new ServiceType("event", EventServiceSngl.get());

    public static final ServiceType MESSAGE = new ServiceType("message", MessageServiceSngl.get());
    public static final ServiceType SOCIAL = new ServiceType("social", SocialServiceSngl.get());
    public static final ServiceType TIMELINE = new ServiceType("timeline", TimeLineServiceSngl.get());
    public static final ServiceType ADVERTISE = new ServiceType("advertise", AdvertiseServiceSngl.get());

    public static final ServiceType CONTENT = new ServiceType("content", ContentServiceSngl.get());
    public static final ServiceType PROFILE = new ServiceType("profile", ProfileServiceSngl.get());
    public static final ServiceType SYNC = new ServiceType("sync", SyncServiceSngl.get());
    public static final ServiceType GAME = new ServiceType("game", GameResourceServiceSngl.get());
    public static final ServiceType VOTE = new ServiceType("vote", VoteServiceSngl.get());
    public static final ServiceType BILLING = new ServiceType("billing", BillingServiceSngl.get());
    public static final ServiceType JOYMEAPP = new ServiceType("joymeapp", JoymeAppServiceSngl.get());
    public static final ServiceType POINT = new ServiceType("point", PointServiceSngl.get());
    public static final ServiceType LOTTERY = new ServiceType("search", SearchServiceSngl.get());

    public static final ServiceType USERCENTER = new ServiceType("usercenter", UserCenterServiceSngl.get());
    public static final ServiceType STATS = new ServiceType("stats", StatServiceSngl.get());

    public static final ServiceType NOTICE = new ServiceType("notice", NoticeServiceSngl.get());

    public static final ServiceType ASK = new ServiceType("ask", AskServiceSngl.get());

    private String code;
    private EventReceiver eventReceiver;

    ServiceType(String c, EventReceiver p) {
        code = c.toLowerCase();
        eventReceiver = p;

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public EventReceiver getEventReceiver() {
        return eventReceiver;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ServiceType=code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ServiceType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ServiceType) obj).getCode());
    }

    public static ServiceType getByCode(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return null;
        }

        return map.get(code.toLowerCase());
    }

    public static Collection<ServiceType> getAll() {
        return map.values();
    }
}
