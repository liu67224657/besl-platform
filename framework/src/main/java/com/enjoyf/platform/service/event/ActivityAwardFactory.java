package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:51
 * To change this template use File | Settings | File Templates.
 */
public class ActivityAwardFactory {
    private Map<ActivityAwardType, AcitivtyAwardStragy> map = new HashMap<ActivityAwardType, AcitivtyAwardStragy>();

    private static ActivityAwardFactory instance = null;

    public static ActivityAwardFactory get() {
        if (instance == null) {
            synchronized (ActivityAwardFactory.class) {
                if (instance == null) {
                    instance = new ActivityAwardFactory();
                }
            }
        }
        return instance;
    }

    private ActivityAwardFactory() {
        map.put(ActivityAwardType.AWARD_GIFT,new GiftAcitivtyAwardStragy());
    }

    public AwardResult sendAwardByAwardType(Activity activity, String uno, String profileId,String appKey,Date awardDate, String ip) throws ServiceException {

        if (activity.getLimit().equals(ActivityLimit.AWARD_LIMIT_DAY)) {
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityAwardLogField.ACTIVITY_ID, activity.getActivityId()))
                    .add(QueryCriterions.eq(ActivityAwardLogField.UNO, uno))
                    .add(QueryCriterions.eq(ActivityAwardLogField.AWARD_DATE, awardDate));
            List<ActivityAwardLog> logs = ActivityServiceSngl.get().queryAwardLog(queryExpress);
            if (!CollectionUtil.isEmpty(logs)) {
                throw ActivityServiceException.USER_HAS_AWARD;
            }
        }


        //发布奖品
        AcitivtyAwardStragy stragy = map.get(activity.getAwardType());
        if (stragy == null) {
            GAlerter.lab(this.getClass().getName()+" has not support stragy process award");
            return null;
        }

        AwardResult result=stragy.sendAward(activity, uno,profileId,appKey, awardDate, ip);

        ActivityAwardLog log = new ActivityAwardLog();
        log.setActivityId(activity.getActivityId());
        log.setUno(uno);
        log.setAwardDate(awardDate);
        log.setCreateTime(new Date());
        try {
            ActivityServiceSngl.get().createAwardLog(log);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName()+"occured ServiceException.e:",e);
        }
        return result;
    }
}
