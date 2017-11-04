package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:46
 * To change this template use File | Settings | File Templates.
 */
public class GiftAcitivtyAwardStragy implements AcitivtyAwardStragy {

    public AwardResult sendAward(Activity activity, String uno, String profileid, String appKey, Date awardDate, String ip) throws ServiceException {
        try {
            UserPoint userPoint = PointServiceSngl.get().getUserPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.USERNO, uno)));
            if (userPoint == null) {
                userPoint = new UserPoint();
                userPoint.setUserNo(uno);
                userPoint.setConsumeAmount(0);
                userPoint.setConsumeExchange(0);
                userPoint.setUserPoint(0);
                userPoint.setProfileId(profileid);
                userPoint.setPointKey(PointKeyType.WWW.getValue());
                PointServiceSngl.get().addUserPoint(userPoint);
            }

            UserConsumeLog userConsumeLog = PointServiceSngl.get().consumeGoods(uno, profileid, appKey, activity.getAwardId(), awardDate, ip, GoodsActionType.WWW,"");

            AwardResult result = new AwardResult();
            result.setStatus(0);
            if (userConsumeLog != null) {
                GoodsItem item = PointServiceSngl.get().getGoodsItemById(userConsumeLog.getGoodsItemId());

                if (item != null) {
                    result.setCode(String.valueOf(item.getSnValue1()));
                    result.setStatus(1);
                }
            }

            return result;
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occrued ServiceException.e: ", e);
            if (e.equals(PointServiceException.GOODS_ITEM_NOT_EXISTS)) {
                throw ActivityServiceException.AWARD_NOT_ENOUGH;
            } else if (e.equals(PointServiceException.GOODS_OUTOF_RESTAMMOUNT)) {
                throw ActivityServiceException.AWARD_NOT_ENOUGH;
            } else {
                throw ActivityServiceException.AWARD_GET_FAILED;
            }
        }
    }
}
