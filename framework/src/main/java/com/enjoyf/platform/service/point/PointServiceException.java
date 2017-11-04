package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class PointServiceException extends ServiceException {
    public static final PointServiceException NOT_SUPPORT_STRAGY = new PointServiceException(BASE_POINT + 1, "not suppodrt award stragy");
    //goods
    public static final PointServiceException GOODS_NOT_EXISTS = new PointServiceException(BASE_POINT + 2, "goods not exists");
    //goods out of rest amount
    public static final PointServiceException GOODS_OUTOF_RESTAMMOUNT = new PointServiceException(BASE_POINT + 3, "goods out of restamount");

    public static final PointServiceException GOODS_GET_FAILED = new PointServiceException(BASE_POINT + 4, "goods get failed");

    public static final PointServiceException GOODS_ITEM_NOT_EXISTS = new PointServiceException(BASE_POINT + 5, "goods item not exists");

    public static final PointServiceException GOODS_ITEM_GET_FAILED = new PointServiceException(BASE_POINT + 6, "goods item get failed");

    public static final PointServiceException POINT_ACTION_HISTORY_INSERT_FAILED = new PointServiceException(BASE_POINT + 7, "point action history insert failed");

    public static final PointServiceException USER_POINT_GET_FAILED = new PointServiceException(BASE_POINT + 8, "user point not exists");

    public static final PointServiceException USER_POINT_UPDATE_FAILED = new PointServiceException(BASE_POINT + 9, "user point update failed");

    public static final PointServiceException USER_DAY_POINT_INSERT_FAILED = new PointServiceException(BASE_POINT + 10, "user day point insert failed");

    public static final PointServiceException USER_DAY_POINT_UPDATE_FAILED = new PointServiceException(BASE_POINT + 11, "user day point update failed");

    public static final PointServiceException USER_POINT_NOT_ENOUGH = new PointServiceException(BASE_POINT + 12, "user point not enough");

    public static final PointServiceException GOODS_SECKILL_IS_NULL = new PointServiceException(BASE_POINT + 13, "goods seckill is null");
    public static final PointServiceException GOODS_SECKILL_TOTAL_BEYOND = new PointServiceException(BASE_POINT + 14, "goods seckill total beyond");
    public static final PointServiceException GOODS_SECKILL_END = new PointServiceException(BASE_POINT + 15, "goods seckill end");
    public static final PointServiceException GOODS_SECKILL_NO_START = new PointServiceException(BASE_POINT + 16, "goods seckill no start");
    public static final PointServiceException GIFT_LOTTERY_NOT_ENOUGH = new PointServiceException(BASE_POINT + 17, " gift lottery not enough");
    public static final PointServiceException GIFT_LOTTERY_NOT_EXISTS = new PointServiceException(BASE_POINT + 18, " gift lottery not exists");
    public static final PointServiceException GIFT_LOTTERY_EXIST = new PointServiceException(BASE_POINT + 19, " gift lottery exists");
    public static final PointServiceException USER_LOTTERY_LOG_INSERT_FAILED = new PointServiceException(BASE_POINT + 20, "user lottery log insert failed");

    public PointServiceException(int i, String s) {
        super(i, s);
    }
}
