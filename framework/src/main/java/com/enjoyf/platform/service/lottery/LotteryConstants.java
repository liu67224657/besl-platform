package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class LotteryConstants {
    public static final String SERVICE_SECTION = "lotteryservice";
    public static final String SERVICE_PREFIX = "lotteryserver";
    public static final String SERVICE_TYPE = "lotteryserver";

    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte CREATE_LOTTERY = 10;
    public static final byte QUERY_LOTTERY = 11;
    public static final byte QUERY_LOTTERY_BY_PAGE = 12;
    public static final byte GET_LOTTERY_BY_ID = 13;
    public static final byte MODIFY_LOTTERY_BY_ID = 14;

    public static final byte CREATE_LOTTERY_AWARD = 15;
    public static final byte QUERY_LOTTERY_AWARD = 16;
    public static final byte QUERY_LOTTERY_AWARD_BY_PAGE = 17;
    public static final byte GET_LOTTERY_AWARD_BY_ID = 18;
    public static final byte MODIFY_LOTTERY_AWARD_BY_ID = 19;

    public static final byte CREATE_LOTTERY_AWARD_ITEM = 20;
    public static final byte QUERY_LOTTERY_AWARD_ITEM_BY_PAGE = 21;
    public static final byte QUERY_LOTTERY_AWARD_ITEM = 22;
    public static final byte GET_LOTTERY_AWARD_ITEM_BY_ID = 23;
    public static final byte MODIFY_LOTTERY_AWARD_ITEM_BY_ID = 24;

    public static final byte CREATE_USER_LOTTERY_LOG = 25;
    public static final byte QUERY_USER_LOTTERY_LOG_BY_PAGE = 26;

    public static final byte QUERY_USER_DAY_LOTTERY = 27;
    public static final byte USER_LOTTERY_AWARD = 28;

    public static final byte GET_LOTTERY_AWARD_BY_LEVEL = 29;
    public static final byte GET_LOTTERY_AWARD_BY_RATE = 30;

    public static final byte QUERY_USER_LOTTERY_LOG = 31;
    public static final byte GET_USER_DAY_LOTTERY = 32;
    public static final byte GET_USER_LOTTERY_LOG = 33;

    public static final byte QUERY_LASTEST_USERLOTTERY_LOG = 34;


    public static final byte QUERY_TICKET_LIST = 40;
    public static final byte QUERY_TICKET_PAGEROWS = 41;
    public static final byte MODIFY_TICKET = 42;
    public static final byte CREATE_TICKET = 43;
    public static final byte GET_TICKET_BY_ID = 44;


    public static final byte CREATE_TICKETAWARD = 50;
    public static final byte QUERY_TICKETAWARD_LIST = 51;
    public static final byte MODIFY_TICKETAWARD = 52;
    public static final byte QUERY_TICKETAWARD_PAGEROWS = 53;
    public static final byte GET_TICKETAWARD_BY_ID = 54;
    public static final byte QUERY_LOTTERY_AWARD_BY_CACHE = 55;
    public static final byte USER_LOTTERY_AWARD_ENTRY = 56;
    public static final byte USER_LOTTERY_AWARD_FIVE = 57;
    public static final byte USER_LOTTERY_AWARD_FIVE_ADD_SHARE_TIME = 58;
    public static final byte ADD_ORIN_CRTIME = 59;

    public static final byte PUT_S12_CACHE = 60;
    public static final byte GET_S12_CACHE = 61;
    public static final byte INCR_S12_SHARE_TIME = 62;

    public static final byte LOTTERY = 63;
    public static final byte ADD_CHANCE = 64;

    public static final byte MODIFY_LOTTERY_ADDRESS = 65;
    public static final byte TAKE_LOTTERY = 66;

    public static final byte ADD_LOTTERY_CHANCE = 67;

    static {
        transProfileContainer.put(new TransProfile(CREATE_LOTTERY, "CREATE_LOTTERY"));
        transProfileContainer.put(new TransProfile(QUERY_LOTTERY, "QUERY_LOTTERY"));
        transProfileContainer.put(new TransProfile(QUERY_LOTTERY_BY_PAGE, "QUERY_LOTTERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_LOTTERY_BY_ID, "GET_LOTTERY_BY_ID"));
        transProfileContainer.put(new TransProfile(MODIFY_LOTTERY_BY_ID, "MODIFY_LOTTERY_BY_ID"));

        transProfileContainer.put(new TransProfile(CREATE_LOTTERY_AWARD, "CREATE_LOTTERY_AWARD"));
        transProfileContainer.put(new TransProfile(QUERY_LOTTERY_AWARD, "QUERY_LOTTERY_AWARD"));
        transProfileContainer.put(new TransProfile(QUERY_LOTTERY_AWARD_BY_PAGE, "QUERY_LOTTERY_AWARD_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_LOTTERY_AWARD_BY_ID, "GET_LOTTERY_AWARD_BY_ID"));
        transProfileContainer.put(new TransProfile(MODIFY_LOTTERY_AWARD_BY_ID, "MODIFY_LOTTERY_AWARD_BY_ID"));

        transProfileContainer.put(new TransProfile(CREATE_LOTTERY_AWARD_ITEM, "CREATE_LOTTERY_AWARD_ITEM"));
        transProfileContainer.put(new TransProfile(QUERY_LOTTERY_AWARD_ITEM_BY_PAGE, "QUERY_LOTTERY_AWARD_ITEM_BY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_LOTTERY_AWARD_ITEM, "QUERY_LOTTERY_AWARD_ITEM"));
        transProfileContainer.put(new TransProfile(GET_LOTTERY_AWARD_ITEM_BY_ID, "GET_LOTTERY_AWARD_ITEM_BY_ID"));
        transProfileContainer.put(new TransProfile(MODIFY_LOTTERY_AWARD_ITEM_BY_ID, "MODIFY_LOTTERY_AWARD_ITEM_BY_ID"));

        transProfileContainer.put(new TransProfile(CREATE_USER_LOTTERY_LOG, "CREATE_USER_LOTTERY_LOG"));
        transProfileContainer.put(new TransProfile(QUERY_USER_LOTTERY_LOG_BY_PAGE, "QUERY_USER_LOTTERY_LOG_BY_PAGE"));

        transProfileContainer.put(new TransProfile(QUERY_USER_DAY_LOTTERY, "QUERY_USER_DAY_LOTTERY"));

        transProfileContainer.put(new TransProfile(USER_LOTTERY_AWARD, "USER_LOTTERY_AWARD"));

        transProfileContainer.put(new TransProfile(GET_LOTTERY_AWARD_BY_LEVEL, "GET_LOTTERY_AWARD_BY_LEVEL"));

        transProfileContainer.put(new TransProfile(GET_LOTTERY_AWARD_BY_RATE, "GET_LOTTERY_AWARD_BY_RATE"));

        transProfileContainer.put(new TransProfile(QUERY_USER_LOTTERY_LOG, "QUERY_USER_LOTTERY_LOG"));

        transProfileContainer.put(new TransProfile(GET_USER_DAY_LOTTERY, "GET_USER_DAY_LOTTERY"));

        transProfileContainer.put(new TransProfile(GET_USER_LOTTERY_LOG, "GET_USER_LOTTERY_LOG"));


        transProfileContainer.put(new TransProfile(QUERY_TICKET_LIST, "QUERY_TICKET_LIST"));
        transProfileContainer.put(new TransProfile(QUERY_TICKET_PAGEROWS, "QUERY_TICKET_PAGEROWS"));
        transProfileContainer.put(new TransProfile(MODIFY_TICKET, "MODIFY_TICKET"));
        transProfileContainer.put(new TransProfile(CREATE_TICKET, "CREATE_TICKET"));
        transProfileContainer.put(new TransProfile(GET_TICKET_BY_ID, "GET_TICKET_BY_ID"));

        transProfileContainer.put(new TransProfile(CREATE_TICKETAWARD, "CREATE_TICKETAWARD"));
        transProfileContainer.put(new TransProfile(QUERY_TICKETAWARD_LIST, "QUERY_TICKETAWARD_LIST"));
        transProfileContainer.put(new TransProfile(MODIFY_TICKETAWARD, "MODIFY_TICKETAWARD"));
        transProfileContainer.put(new TransProfile(QUERY_TICKETAWARD_PAGEROWS, "QUERY_TICKETAWARD_PAGEROWS"));
        transProfileContainer.put(new TransProfile(GET_TICKETAWARD_BY_ID, "GET_TICKETAWARD_BY_ID"));
        transProfileContainer.put(new TransProfile(QUERY_LOTTERY_AWARD_BY_CACHE, "QUERY_LOTTERY_AWARD_BY_CACHE"));
        transProfileContainer.put(new TransProfile(USER_LOTTERY_AWARD_ENTRY, "USER_LOTTERY_AWARD_ENTRY"));
        transProfileContainer.put(new TransProfile(USER_LOTTERY_AWARD_FIVE, "USER_LOTTERY_AWARD_FIVE"));
        transProfileContainer.put(new TransProfile(USER_LOTTERY_AWARD_FIVE_ADD_SHARE_TIME, "USER_LOTTERY_AWARD_FIVE_ADD_SHARE_TIME"));
        transProfileContainer.put(new TransProfile(ADD_ORIN_CRTIME, "ADD_ORIN_CRTIME"));

        transProfileContainer.put(new TransProfile(PUT_S12_CACHE, "PUT_S12_CACHE"));
        transProfileContainer.put(new TransProfile(GET_S12_CACHE, "GET_S12_CACHE"));
        transProfileContainer.put(new TransProfile(INCR_S12_SHARE_TIME, "INCR_S12_SHARE_TIME"));

        transProfileContainer.put(new TransProfile(LOTTERY, "LOTTERY"));
        transProfileContainer.put(new TransProfile(ADD_CHANCE, "ADD_CHANCE"));

        transProfileContainer.put(new TransProfile(MODIFY_LOTTERY_ADDRESS, "MODIFY_LOTTERY_ADDRESS"));

        transProfileContainer.put(new TransProfile(TAKE_LOTTERY, "TAKE_LOTTERY"));
        transProfileContainer.put(new TransProfile(ADD_LOTTERY_CHANCE, "ADD_LOTTERY_CHANCE"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}