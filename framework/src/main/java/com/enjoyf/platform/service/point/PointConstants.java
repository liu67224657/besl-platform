package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class PointConstants {
    public static final String SERVICE_SECTION = "pointservice";
    public static final String SERVICE_PREFIX = "pointserver";
    public static final String SERVICE_TYPE = "pointserver";

    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public final static byte ADD_USER_POINT = 1;
    public final static byte QUERY_USER_POION_BY_PAGE = 2;
    public final static byte GET_USER_POION = 3;

    public final static byte INSERT_ACTIVITY_GOODS = 4;
    public final static byte QUERY_ACTIVITY_GOODS_BY_PAGE = 5;
    public final static byte GET_ACTIVITY_GOODS = 6;
    public final static byte MODIFY_ACTIVITY_GOODS = 7;
    public final static byte QUERY_ACTIVITY_GOODS = 8;

    public final static byte QUERY_GIFTMARKET_INDEX = 9;

    public final static byte ADD_POINT_ACTION_HISTORY = 10;
    public final static byte QUERY_POINT_ACTION_HISTORY_BY_PAGE = 11;
    public final static byte QUERY_POINT_ACTION_HISTORY = 12;
    public final static byte GET_POINT_ACTION_HISTORY = 13;
    public final static byte MODIFY_POINT_ACTION_HISTORY = 14;
    public final static byte QUERY_ACTIVITY_GOODS_BY_LETTER = 15;
    public final static byte COUNT_ACTIVITY_GOODS = 16;
    public static final byte QUERY_ACTIVITY_HOT = 17;
    public static final byte MODIFY_ACTIVITY_HOT_RANKS = 18;

    public final static byte GET_USER_DAY_POINT = 20;

    public static final byte ADD_GOODS = 30;
    public static final byte QUERY_GOODS_BY_PAGE = 31;
    public static final byte GET_GOODS_BY_ID = 32;
    public static final byte MODIFY_GOODS_BY_ID = 33;
    public static final byte QUERY_GOODS = 34;

    public static final byte ADD_GOODS_ITEM = 40;
    public static final byte QUERY_GOODS_ITEM_BY_PAGE = 41;
    public static final byte GET_GOODS_ITEM_BY_ID = 42;
    public static final byte MODIFY_GOODS_ITEM_BY_ID = 43;

    public static final byte USER_CONSUME = 50;
    public static final byte QUERY_USER_CONSUME_LOG_BY_PAGE = 51;

    public static final byte QUERY_USER_CONSUME_LOG_BY_UNO_GOODSID_COONSUMETIME = 52;
    public static final byte QUERY_USER_CONSUME_LOG_BY_UNO_GOODSID = 53;

    public static final byte QUERY_USER_DAY_POINT = 54;
    public static final byte QUERY_EXCHANGE_GOODS_BY_PAGE = 55;
    public static final byte GET_EXCHANGE_GOODS = 56;
    public static final byte QUERY_EXCHANGE_GOODS_LIST = 57;
    public static final byte INSERT_EXCHANGE_GOODS = 58;
    public static final byte UPDATE_EXCHANGE_GOODS = 59;
    public static final byte TAO_EXGOODS_ITEM = 60;
    public static final byte EXCHNAGE_EXGOODS_ITEM = 61;


    public static final byte QUERY_EXCHANGE_GOODS_ITEM_BY_PAGE = 70;
    public static final byte ADD_EXCHANGE_GOODS_ITEM = 71;
    public static final byte QUERY_USER_EXCHANGE_LOG_BY_PAGE = 72;
    public static final byte QUERY_USER_EXCHANGE_LOG_BY_UNOPAGE = 73;
    public static final byte QUERY_USER_EXCHANGE_LOG_BY_QUERYEXPRESS = 74;
    public static final byte QUERY_USER_EXCHANGE_LOG_BY_DATE = 75;

    public static final byte QUERY_USER_RECENT_LOG = 80;
    public static final byte INCREASE_SMS_COUNT_EXCHANGE_LOG = 81;
    public static final byte UPDATE_EXCHANGE_ITEM_GOODS = 82;

    public static final byte QUERY_GIFT_RESERVE_BY_PAGE = 83;
    public static final byte QUERY_GIFT_RESERVE_BY_LIST = 84;
    public static final byte GET_GIFT_RESERVE = 85;
    public static final byte MODIFY_GIFT_RESERVE = 86;
    public static final byte CREATE_GIFT_RESERVE = 87;
    public static final byte RECIEVE_EVENT = 88;
    public static final byte QUERY_USER_EXCHANEG_LOG_BY_QUERY = 89;


    //积分墙--积分墙列表维护
    public static final byte QUERY_POINTWALL_WALL = 90;
    public static final byte UPDATE_POINTWALL_WALL = 91;
    public static final byte INSERT_POINTWALL_WALL = 92;
    public static final byte DELETE_POINTWALL_WALL = 93;
    public static final byte GET_POINTWALL_WALL = 94;

    //积分墙--app列表维护
    public static final byte QUERY_POINTWALL_APP = 95;
    public static final byte UPDATE_POINTWALL_APP = 96;
    public static final byte INSERT_POINTWALL_APP = 97;
    public static final byte DELETE_POINTWALL_APP = 98;
    public static final byte GET_POINTWALL_APP = 99;


    //积分墙--单个积份墙app列表管理

    public static final byte QUERY_POINTWALL_WALL_APP = 100;
    public static final byte UPDATE_POINTWALL_WALL_APP = 101;
    public static final byte INSERT_POINTWALL_WALL_APP = 102;
    public static final byte DELETE_POINTWALL_WALL_APP = 103;
    public static final byte GET_POINTWALL_WALL_APP = 104;
    public static final byte COUNT_POINTWALL_WALL_APP = 105;
    public static final byte COUNT_TOTAL_POINTWALL_WALL_APP = 106;
    public static final byte QUERY_POINTWALL_WALL_APP_NOT_BY_PAGE = 107;

    public static final byte QUERY_POINTWALL_TASKLOG = 108;
    public static final byte GET_POINTWALL_TASKLOG = 109;
    public static final byte INSERT_POINTWALL_TASKLOG = 110;
    public static final byte QUERY_POINTWALL_TASKLOG_ALL = 111;
    public static final byte COUNT_POINTWALL_TASKLOG_ALL = 112;
    public static final byte QUERY_CONSUMELOG_BY_PAGE = 113;

    public static final byte COUNT_POINTWALL_WALL = 114;
    public static final byte QUERY_POINTWALL_WALL_ALL = 115;

    public static final byte CHECK_GIFT_RESERVER = 116;
    public static final byte GET_POINT_KEY_TYPE = 117;
    public static final byte QUERY_ACTIVITY_GOODS_BY_GAME_ID = 118;
    public static final byte QUERY_USER_CONSUME_LOG_LIST = 119;

    public static final byte CREATE_GOODS_SECKILL = 120;
    public static final byte GET_GOODS_SECKILL = 121;
    public static final byte QUERY_GOODS_SECKILL = 122;
    public static final byte QUERY_GOODS_SECKILL_BY_PAGE = 123;
    public static final byte MODIFY_GOODS_SECKILL = 124;
    public static final byte GET_GOODS_SECKILL_BY_ID = 125;
    public static final byte SECKILL_GOODS = 126;
    public static final byte QUERY_ACTIVITY_GOODS_IDSET = -1;
    public static final byte QUERY_GOODS_SECKILL_BY_CACHE = -2;
    public static final byte QUERY_GOODS_ITEM = -3;
    public static final byte QUERY_ACTIVITY_GOODS_BY_CACHE = -4;
    public static final byte GET_USER_EXCHANGE_LOG = -5;
    public static final byte INCR_POINT_RULE = -6;
    public static final byte GET_WANBA_QUESTION_POINT = -7;
    public static final byte ADD_PRESTIGE_ACTION_HISTORY = -8;
    public static final byte QUERY_RANK_LIST_BY_TYPE = -9;
    public static final byte OPEN_GIFT_LOTTERY = -10;
    public static final byte QUERY_GIFT_LOTTERY_BY_PAGE = -11;

    public static final byte GET_GIFT_BOX_NUM=-12;
    public static final byte EXCHANGE_GIFT_BOX=-13;
    public static final byte QUERY_USER_LOTTERY_LOG_BY_CACHE=-14;
    public static final byte CHOOSE_LOTTERY=-15;
    public static final byte GET_USER_CHOOSE_LOTTERY=-16;
    public static final byte QUERY_USER_CHOOSE_LOTTERY=-17;
    public static final byte GET_USER_POINT_BY_DAY=-18;

    public static final byte QUERY_WORSHIP=-19;
    public static final byte QUERY_MY_POINT_LIST_BY_CACHE=-20;






    static {
        transProfileContainer.put(new TransProfile(ADD_USER_POINT, "ADD_USER_POINT"));
        transProfileContainer.put(new TransProfile(QUERY_USER_POION_BY_PAGE, "QUERY_USER_POION_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_USER_POION, "GET_USER_POION"));

        transProfileContainer.put(new TransProfile(ADD_POINT_ACTION_HISTORY, "ADD_POINT_ACTION_HISTORY"));
        transProfileContainer.put(new TransProfile(QUERY_POINT_ACTION_HISTORY_BY_PAGE, "QUERY_POINT_ACTION_HISTORY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_POINT_ACTION_HISTORY, "QUERY_POINT_ACTION_HISTORY"));
        transProfileContainer.put(new TransProfile(GET_POINT_ACTION_HISTORY, "GET_POINT_ACTION_HISTORY"));
        transProfileContainer.put(new TransProfile(MODIFY_POINT_ACTION_HISTORY, "MODIFY_POINT_ACTION_HISTORY"));

        transProfileContainer.put(new TransProfile(GET_USER_DAY_POINT, "GET_USER_DAY_POINT"));

        transProfileContainer.put(new TransProfile(ADD_GOODS, "ADD_GOODS"));
        transProfileContainer.put(new TransProfile(QUERY_GOODS_BY_PAGE, "QUERY_GOODS_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_GOODS_BY_ID, "GET_GOODS_BY_ID"));
        transProfileContainer.put(new TransProfile(MODIFY_GOODS_BY_ID, "MODIFY_GOODS_BY_ID"));

        transProfileContainer.put(new TransProfile(ADD_GOODS_ITEM, "ADD_GOODS_ITEM"));
        transProfileContainer.put(new TransProfile(QUERY_GOODS_ITEM_BY_PAGE, "QUERY_GOODS_ITEM_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_GOODS_ITEM_BY_ID, "GET_GOODS_ITEM_BY_ID"));
        transProfileContainer.put(new TransProfile(MODIFY_GOODS_ITEM_BY_ID, "MODIFY_GOODS_ITEM_BY_ID"));

        transProfileContainer.put(new TransProfile(USER_CONSUME, "USER_CONSUME"));
        transProfileContainer.put(new TransProfile(QUERY_USER_CONSUME_LOG_BY_PAGE, "QUERY_USER_CONSUME_LOG_BY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_USER_CONSUME_LOG_BY_UNO_GOODSID_COONSUMETIME, "QUERY_USER_CONSUME_LOG_BY_UNO_GOODS_COONSUMETIME"));
        transProfileContainer.put(new TransProfile(QUERY_USER_CONSUME_LOG_BY_UNO_GOODSID, "QUERY_USER_CONSUME_LOG_BY_UNO_GOODS"));

        transProfileContainer.put(new TransProfile(QUERY_USER_DAY_POINT, "QUERY_USER_DAY_POINT"));
        transProfileContainer.put(new TransProfile(QUERY_EXCHANGE_GOODS_BY_PAGE, "QUERY_EXCHANGE_GOODS_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_EXCHANGE_GOODS, "GET_EXCHANGE_GOODS"));
        transProfileContainer.put(new TransProfile(QUERY_EXCHANGE_GOODS_LIST, "QUERY_EXCHANGE_GOODS_LIST"));
        transProfileContainer.put(new TransProfile(INSERT_EXCHANGE_GOODS, "INSERT_EXCHANGE_GOODS"));
        transProfileContainer.put(new TransProfile(TAO_EXGOODS_ITEM, "TAO_EXGOODS_ITEM"));
        transProfileContainer.put(new TransProfile(UPDATE_EXCHANGE_GOODS, "UPDATE_EXCHANGE_GOODS"));
        transProfileContainer.put(new TransProfile(EXCHNAGE_EXGOODS_ITEM, "EXCHNAGE_EXGOODS_ITEM"));
        transProfileContainer.put(new TransProfile(QUERY_EXCHANGE_GOODS_ITEM_BY_PAGE, "QUERY_EXCHANGE_GOODS_ITEM_BY_PAGE"));
        transProfileContainer.put(new TransProfile(ADD_EXCHANGE_GOODS_ITEM, "ADD_EXCHANGE_GOODS_ITEM"));
        transProfileContainer.put(new TransProfile(QUERY_USER_EXCHANGE_LOG_BY_PAGE, "QUERY_USER_EXCHANGE_LOG_BY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_USER_EXCHANGE_LOG_BY_UNOPAGE, "QUERY_USER_EXCHANGE_LOG_BY_UNOPAGE"));
        transProfileContainer.put(new TransProfile(QUERY_USER_EXCHANGE_LOG_BY_QUERYEXPRESS, "QUERY_USER_EXCHANGE_LOG_BY_QUERYEXPRESS"));

        transProfileContainer.put(new TransProfile(QUERY_USER_RECENT_LOG, "QUERY_USER_RECENT_LOG"));
        transProfileContainer.put(new TransProfile(QUERY_USER_EXCHANGE_LOG_BY_DATE, "QUERY_USER_EXCHANGE_LOG_BY_DATE"));
        transProfileContainer.put(new TransProfile(INCREASE_SMS_COUNT_EXCHANGE_LOG, "INCREASE_SMS_COUNT_EXCHANGE_LOG"));
        transProfileContainer.put(new TransProfile(UPDATE_EXCHANGE_ITEM_GOODS, "UPDATE_EXCHANGE_ITEM_GOODS"));

        transProfileContainer.put(new TransProfile(QUERY_GIFT_RESERVE_BY_PAGE, "QUERY_GIFT_RESERVE_BY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_GIFT_RESERVE_BY_LIST, "QUERY_GIFT_RESERVE_BY_LIST"));
        transProfileContainer.put(new TransProfile(GET_GIFT_RESERVE, "GET_GIFT_RESERVE"));
        transProfileContainer.put(new TransProfile(MODIFY_GIFT_RESERVE, "MODIFY_GIFT_RESERVE"));
        transProfileContainer.put(new TransProfile(CREATE_GIFT_RESERVE, "CREATE_GIFT_RESERVE"));
        transProfileContainer.put(new TransProfile(RECIEVE_EVENT, "RECIEVE_EVENT"));
        transProfileContainer.put(new TransProfile(QUERY_USER_EXCHANEG_LOG_BY_QUERY, "QUERY_USER_EXCHANEG_LOG_BY_QUERY"));


        transProfileContainer.put(new TransProfile(QUERY_POINTWALL_WALL, "QUERY_POINTWALL_WALL"));
        transProfileContainer.put(new TransProfile(UPDATE_POINTWALL_WALL, "UPDATE_POINTWALL_WALL"));
        transProfileContainer.put(new TransProfile(INSERT_POINTWALL_WALL, "INSERT_POINTWALL_WALL"));
        transProfileContainer.put(new TransProfile(DELETE_POINTWALL_WALL, "DELETE_POINTWALL_WALL"));
        transProfileContainer.put(new TransProfile(GET_POINTWALL_WALL, "GET_POINTWALL_WALL"));

        transProfileContainer.put(new TransProfile(QUERY_POINTWALL_APP, "QUERY_POINTWALL_APP"));
        transProfileContainer.put(new TransProfile(UPDATE_POINTWALL_APP, "UPDATE_POINTWALL_APP"));
        transProfileContainer.put(new TransProfile(INSERT_POINTWALL_APP, "INSERT_POINTWALL_APP"));
        transProfileContainer.put(new TransProfile(DELETE_POINTWALL_APP, "DELETE_POINTWALL_APP"));
        transProfileContainer.put(new TransProfile(GET_POINTWALL_APP, "GET_POINTWALL_APP"));


        transProfileContainer.put(new TransProfile(QUERY_POINTWALL_WALL_APP, "QUERY_POINTWALL_WALL_APP"));
        transProfileContainer.put(new TransProfile(UPDATE_POINTWALL_WALL_APP, "UPDATE_POINTWALL_WALL_APP"));
        transProfileContainer.put(new TransProfile(INSERT_POINTWALL_WALL_APP, "INSERT_POINTWALL_WALL_APP"));
        transProfileContainer.put(new TransProfile(DELETE_POINTWALL_WALL_APP, "DELETE_POINTWALL_WALL_APP"));
        transProfileContainer.put(new TransProfile(GET_POINTWALL_WALL_APP, "GET_POINTWALL_WALL_APP"));
        transProfileContainer.put(new TransProfile(COUNT_POINTWALL_WALL_APP, "COUNT_POINTWALL_WALL_APP"));
        transProfileContainer.put(new TransProfile(COUNT_TOTAL_POINTWALL_WALL_APP, "COUNT_TOTAL_POINTWALL_WALL_APP"));
        transProfileContainer.put(new TransProfile(QUERY_POINTWALL_WALL_APP_NOT_BY_PAGE, "QUERY_POINTWALL_WALL_APP_NOT_BY_PAGE"));


        transProfileContainer.put(new TransProfile(QUERY_POINTWALL_TASKLOG, "QUERY_POINTWALL_TASKLOG"));
        transProfileContainer.put(new TransProfile(GET_POINTWALL_TASKLOG, "GET_POINTWALL_TASKLOG"));
        transProfileContainer.put(new TransProfile(INSERT_POINTWALL_TASKLOG, "INSERT_POINTWALL_TASKLOG"));
        transProfileContainer.put(new TransProfile(QUERY_POINTWALL_TASKLOG_ALL, "QUERY_POINTWALL_TASKLOG_ALL"));
        transProfileContainer.put(new TransProfile(QUERY_CONSUMELOG_BY_PAGE, "QUERY_CONSUMELOG_BY_PAGE"));

        transProfileContainer.put(new TransProfile(COUNT_POINTWALL_WALL, "COUNT_POINTWALL_WALL"));
        transProfileContainer.put(new TransProfile(QUERY_POINTWALL_WALL_ALL, "QUERY_POINTWALL_WALL_ALL"));
        transProfileContainer.put(new TransProfile(CHECK_GIFT_RESERVER, "CHECK_GIFT_RESERVER"));
        transProfileContainer.put(new TransProfile(INSERT_ACTIVITY_GOODS, "INSERT_ACTIVITY_GOODS"));
        transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_GOODS_BY_PAGE, "QUERY_ACTIVITY_GOODS_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_ACTIVITY_GOODS, "GET_ACTIVITY_GOODS"));
        transProfileContainer.put(new TransProfile(MODIFY_ACTIVITY_GOODS, "MODIFY_ACTIVITY_GOODS"));
        transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_GOODS, "QUERY_ACTIVITY_GOODS"));
        transProfileContainer.put(new TransProfile(QUERY_GIFTMARKET_INDEX, "QUERY_GIFTMARKET_INDEX"));
        transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_GOODS_BY_LETTER, "QUERY_ACTIVITY_GOODS_BY_LETTER"));
        transProfileContainer.put(new TransProfile(COUNT_ACTIVITY_GOODS, "COUNT_ACTIVITY_GOODS"));
        transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_HOT, "QUERY_ACTIVITY_HOT"));
        transProfileContainer.put(new TransProfile(MODIFY_ACTIVITY_HOT_RANKS, "MODIFY_ACTIVITY_HOT_RANKS"));
        transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_GOODS_BY_GAME_ID, "QUERY_ACTIVITY_GOODS_BY_GAME_ID"));

        transProfileContainer.put(new TransProfile(GET_POINT_KEY_TYPE, "GET_POINT_KEY_TYPE"));
        transProfileContainer.put(new TransProfile(QUERY_USER_CONSUME_LOG_LIST, "QUERY_USER_CONSUME_LOG_LIST"));

        transProfileContainer.put(new TransProfile(CREATE_GOODS_SECKILL, "CREATE_GOODS_SECKILL"));
        transProfileContainer.put(new TransProfile(GET_GOODS_SECKILL, "GET_GOODS_SECKILL"));
        transProfileContainer.put(new TransProfile(QUERY_GOODS_SECKILL, "QUERY_GOODS_SECKILL"));
        transProfileContainer.put(new TransProfile(QUERY_GOODS_SECKILL_BY_PAGE, "QUERY_GOODS_SECKILL_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_GOODS_SECKILL, "MODIFY_GOODS_SECKILL"));
        transProfileContainer.put(new TransProfile(GET_GOODS_SECKILL_BY_ID, "GET_GOODS_SECKILL_BY_ID"));
        transProfileContainer.put(new TransProfile(SECKILL_GOODS, "SECKILL_GOODS"));
        transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_GOODS_IDSET, "QUERY_ACTIVITY_GOODS_IDSET"));
        transProfileContainer.put(new TransProfile(QUERY_GOODS_SECKILL_BY_CACHE, "QUERY_GOODS_SECKILL_BY_CACHE"));
        transProfileContainer.put(new TransProfile(QUERY_GOODS_ITEM, "QUERY_GOODS_ITEM"));
        transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_GOODS_BY_CACHE, "QUERY_ACTIVITY_GOODS_BY_CACHE"));
        transProfileContainer.put(new TransProfile(GET_USER_EXCHANGE_LOG, "GET_USER_EXCHANGE_LOG"));
        transProfileContainer.put(new TransProfile(INCR_POINT_RULE, "INCR_POINT_RULE"));
        transProfileContainer.put(new TransProfile(GET_WANBA_QUESTION_POINT, "GET_WANBA_QUESTION_POINT"));
        transProfileContainer.put(new TransProfile(ADD_PRESTIGE_ACTION_HISTORY, "ADD_PRESTIGE_ACTION_HISTORY"));
        transProfileContainer.put(new TransProfile(QUERY_RANK_LIST_BY_TYPE, "QUERY_RANK_LIST_BY_TYPE"));
        transProfileContainer.put(new TransProfile(OPEN_GIFT_LOTTERY, "OPEN_GIFT_LOTTERY"));
        transProfileContainer.put(new TransProfile(QUERY_GIFT_LOTTERY_BY_PAGE, "QUERY_GIFT_LOTTERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_GIFT_BOX_NUM, "GET_GIFT_BOX_NUM"));
        transProfileContainer.put(new TransProfile(EXCHANGE_GIFT_BOX, "EXCHANGE_GIFT_BOX"));
        transProfileContainer.put(new TransProfile(QUERY_USER_LOTTERY_LOG_BY_CACHE, "QUERY_USER_LOTTERY_LOG_BY_CACHE"));
        transProfileContainer.put(new TransProfile(CHOOSE_LOTTERY, "CHOOSE_LOTTERY"));
        transProfileContainer.put(new TransProfile(GET_USER_CHOOSE_LOTTERY, "GET_USER_CHOOSE_LOTTERY"));
        transProfileContainer.put(new TransProfile(QUERY_USER_CHOOSE_LOTTERY, "QUERY_USER_CHOOSE_LOTTERY"));
        transProfileContainer.put(new TransProfile(GET_USER_POINT_BY_DAY, "GET_USER_POINT_BY_DAY"));
        transProfileContainer.put(new TransProfile(QUERY_WORSHIP, "QUERY_WORSHIP"));
        transProfileContainer.put(new TransProfile(QUERY_MY_POINT_LIST_BY_CACHE, "QUERY_MY_POINT_LIST_BY_CACHE"));




    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}