package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class LotteryServiceException extends ServiceException {
    public static final LotteryServiceException NOT_SUPPORT_STRAGY = new LotteryServiceException(BASE_LOTTERY + 1, "not suppodrt award stragy");

    //goods
    public static final LotteryServiceException LOTTERY_NOT_EXISTS = new LotteryServiceException(BASE_LOTTERY + 2, "lottery not exists");

    //goods out of rest amount
    public static final LotteryServiceException LOTTERY_AWARD_NOT_EXISTS = new LotteryServiceException(BASE_LOTTERY + 3, "lottery award not exists");

    public static final LotteryServiceException LOTTERY_AWARD_OUTOF_REST_AMMOUNT = new LotteryServiceException(BASE_LOTTERY + 4, "lottery award out of rest amount");

    public static final LotteryServiceException LOTTERY_AWARD_GET_FAILED = new LotteryServiceException(BASE_LOTTERY + 5, "lottery award get failed");

    public static final LotteryServiceException LOTTERY_AWARD_ITEM_NOT_EXISTS = new LotteryServiceException(BASE_LOTTERY + 6, "lottery award item not exists");

    public static final LotteryServiceException LOTTERY_AWARD_ITEM_GET_FAILED = new LotteryServiceException(BASE_LOTTERY + 7, "lottery award item get failed");

    public static final LotteryServiceException USER_DAY_LOTTERY_INSERT_FAILD = new LotteryServiceException(BASE_LOTTERY + 8, "usr day lottery insert failed");

    public static final LotteryServiceException USER_LOTTERY_LOG_INSERT_FAILD = new LotteryServiceException(BASE_LOTTERY + 9, "user lottery log insert failed");

    public static final LotteryServiceException USER_HAD_LOTTERY_AWARD = new LotteryServiceException(BASE_LOTTERY + 10, "user had lottery award");

    public static final LotteryServiceException USER_HAD_LOTTERY_AWARD_TODAY = new LotteryServiceException(BASE_LOTTERY + 11, "user had lottery award today");

    public static final LotteryServiceException LOTTERY_AWARD_UPDATE_FAILED = new LotteryServiceException(BASE_LOTTERY + 12, "lottery award update failed");

    public static final LotteryServiceException LOTTERY_AWARD_ITEM_UPDATE_FAILED = new LotteryServiceException(BASE_LOTTERY + 13, "lottery award item update failed");
    public static final LotteryServiceException USER_LOTTERY_NOT_GET_AWARD = new LotteryServiceException(BASE_LOTTERY + 14, "user lottery not get award");
    public static final LotteryServiceException USER_HAS_NO_TIME = new LotteryServiceException(BASE_LOTTERY + 15, "user has no time");

    public LotteryServiceException(int i, String s) {
        super(i, s);
    }
}
