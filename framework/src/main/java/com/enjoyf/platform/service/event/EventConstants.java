/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class EventConstants {
    public static final String SERVICE_SECTION = "eventservice";
    public static final String SERVICE_PREFIX = "eventserver";
    public static final String SERVICE_TYPE = "eventserver";


    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte EVENT_WRITE = 1;
    public static final byte RECEIVE_TASK_EVENT = 2;

    public static final byte PAGEVIEW_LOCATION_GET = 11;
    public static final byte PAGEVIEW_LOCATION_QUERY = 12;

    //
    public static final byte PAGEVIEW_STATS_PAGEVIEW = 21;
    public static final byte PAGEVIEW_STATS_UNIQUEUSER = 22;

    public static final byte ACTIVITY_CREATE = 23;
    public static final byte ACTIVITY_MODIFY = 24;
    public static final byte ACTIVITY_QUERY_BY_PAGE = 25;
    public static final byte ACTIVITY_GET_BY_ID = 26;
    public static final byte ACTIVITYLOG_CREATE = 27;
    public static final byte ACTIVITYLOG_QUERY = 28;

    public static final byte CREATE_TASK = 29;
    public static final byte GET_TASK = 30;
    public static final byte QUERY_TASK = 31;
    public static final byte QUERY_TASK_BY_PAGE = 32;
    public static final byte MODIFY_TASK = 33;
    public static final byte CREATE_TASK_LOG = 34;
    public static final byte GET_TASK_LOG = 35;
    public static final byte QUERY_TASK_LOG = 36;
    public static final byte QUERY_TASK_LOG_BY_PAGE = 37;
    public static final byte MODIFY_TASK_LOG = 38;

    public static final byte CREATE_TASK_GROUP = 39;
    public static final byte GET_TASK_GROUP = 40;
    public static final byte QUERY_TASK_GROUP = 41;
    public static final byte MODIFY_TASK_GROUP = 42;

    public static final byte QUERY_TASK_BY_GROUPID_PROFILEID = 43;
    public static final byte GET_TASKLOG_BY_GROUPID_PROFILEID = 44;
    public static final byte CHECK_COMPLETE_TASK = 45;
   // public static final byte QUERY_TASK_BY_APPKEY_PLATFORM = 46;
    public static final byte QUERY_TASK_BY_GROUPIDS = 47;
    public static final byte GET_TASK_AWARD = 48;
   // public static final byte GET_TASK_AWARD_WANBA_GROUP_ID = 49;
    public static final byte QUERY_TASKLOG_SIGN_SUM_BY_PROFILEID_GROUPID = 50;
    public static final byte QUERY_COMPLETE_GROUP_BY_PROFILEID_GROUPID = 51;
    public static final byte SET_COMPLETE_GROUP_BY_PROFILEID_GROUPID = 52;
    public static final byte QUERY_TASK_LIST_BY_GROUPID = 53;

    static {
        transProfileContainer.put(new TransProfile(EVENT_WRITE, "EVENT_WRITE"));
        transProfileContainer.put(new TransProfile(RECEIVE_TASK_EVENT, "RECEIVE_TASK_EVENT"));

        transProfileContainer.put(new TransProfile(PAGEVIEW_LOCATION_GET, "PAGEVIEW_LOCATION_GET"));
        transProfileContainer.put(new TransProfile(PAGEVIEW_LOCATION_QUERY, "PAGEVIEW_LOCATION_QUERY"));

        transProfileContainer.put(new TransProfile(PAGEVIEW_STATS_PAGEVIEW, "PAGEVIEW_STATS_PAGEVIEW"));
        transProfileContainer.put(new TransProfile(PAGEVIEW_STATS_UNIQUEUSER, "PAGEVIEW_STATS_UNIQUEUSER"));

        transProfileContainer.put(new TransProfile(ACTIVITY_CREATE, "ACTIVITY_CREATE"));
        transProfileContainer.put(new TransProfile(ACTIVITY_MODIFY, "ACTIVITY_MODIFY"));
        transProfileContainer.put(new TransProfile(ACTIVITY_QUERY_BY_PAGE, "ACTIVITY_QUERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(ACTIVITY_GET_BY_ID, "ACTIVITY_GET_BY_ID"));
        transProfileContainer.put(new TransProfile(ACTIVITYLOG_CREATE, "ACTIVITYLOG_CREATE"));
        transProfileContainer.put(new TransProfile(ACTIVITYLOG_QUERY, "ACTIVITYLOG_QUERY"));

        transProfileContainer.put(new TransProfile(CREATE_TASK, "CREATE_TASK"));
        transProfileContainer.put(new TransProfile(GET_TASK, "GET_TASK"));
        transProfileContainer.put(new TransProfile(QUERY_TASK, "QUERY_TASK"));
        transProfileContainer.put(new TransProfile(QUERY_TASK_BY_PAGE, "QUERY_TASK_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_TASK, "MODIFY_TASK"));
        transProfileContainer.put(new TransProfile(CREATE_TASK_LOG, "CREATE_TASK_LOG"));
        transProfileContainer.put(new TransProfile(GET_TASK_LOG, "GET_TASK_LOG"));
        transProfileContainer.put(new TransProfile(QUERY_TASK_LOG, "QUERY_TASK_LOG"));
        transProfileContainer.put(new TransProfile(QUERY_TASK_LOG_BY_PAGE, "QUERY_TASK_LOG_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_TASK_LOG, "MODIFY_TASK_LOG"));

        transProfileContainer.put(new TransProfile(CREATE_TASK_GROUP, "CREATE_TASK_GROUP"));
        transProfileContainer.put(new TransProfile(GET_TASK_GROUP, "GET_TASK_GROUP"));
        transProfileContainer.put(new TransProfile(QUERY_TASK_GROUP, "QUERY_TASK_GROUP"));
        transProfileContainer.put(new TransProfile(MODIFY_TASK_GROUP, "MODIFY_TASK_GROUP"));

        transProfileContainer.put(new TransProfile(QUERY_TASK_BY_GROUPID_PROFILEID, "QUERY_TASK_BY_GROUPID_PROFILEID"));
        transProfileContainer.put(new TransProfile(GET_TASKLOG_BY_GROUPID_PROFILEID, "QUERY_TASKLOG_BY_GROUPID_PROFILEID"));
        transProfileContainer.put(new TransProfile(CHECK_COMPLETE_TASK, "CHECK_COMPLETE_TASK"));
     //   transProfileContainer.put(new TransProfile(QUERY_TASK_BY_APPKEY_PLATFORM, "QUERY_TASK_BY_APPKEY_PLATFORM"));
        transProfileContainer.put(new TransProfile(QUERY_TASK_BY_GROUPIDS, "QUERY_TASK_BY_GROUPIDS"));
        transProfileContainer.put(new TransProfile(GET_TASK_AWARD, "GET_TASK_AWARD"));

    //    transProfileContainer.put(new TransProfile(GET_TASK_AWARD_WANBA_GROUP_ID, "GET_TASK_AWARD_WANBA_GROUP_ID"));
        transProfileContainer.put(new TransProfile(QUERY_TASKLOG_SIGN_SUM_BY_PROFILEID_GROUPID, "QUERY_TASKLOG_SIGN_SUM_BY_PROFILEID_GROUPID"));

        transProfileContainer.put(new TransProfile(QUERY_COMPLETE_GROUP_BY_PROFILEID_GROUPID, "QUERY_COMPLETE_GROUP_BY_PROFILEID_GROUPID"));
        transProfileContainer.put(new TransProfile(SET_COMPLETE_GROUP_BY_PROFILEID_GROUPID, "SET_COMPLETE_GROUP_BY_PROFILEID_GROUPID"));

        transProfileContainer.put(new TransProfile(QUERY_TASK_LIST_BY_GROUPID, "QUERY_TASK_LIST_BY_GROUPID"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
