package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.point.PointServiceException;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.userprops.UserPropDomain;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.service.userprops.UserPropsServiceSngl;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:46
 * To change this template use File | Settings | File Templates.
 */
public class LotteryAcitivtyAwardStragy implements AcitivtyAwardStragy{

    public AwardResult sendAward(Activity activity,String uno,String profileid,String appKey,Date awardDate,String ip)throws ServiceException{
        try {
            UserPropKey propKey=new UserPropKey(UserPropDomain.DEFAULT, uno,ActivityConstants.KEY_LOTTERY_ACTIVITY+activity.getAwardId());

            UserProperty userProperty=new UserProperty();
            userProperty.setUserPropKey(propKey);
            userProperty.setInitialDate(new Date());
            userProperty.setValue("1");

            UserPropsServiceSngl.get().setUserProperty(userProperty);

            return null;
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName()+" occrued ServiceException.e: ",e);
            if(e.equals(PointServiceException.GOODS_ITEM_NOT_EXISTS)){
                throw ActivityServiceException.AWARD_NOT_ENOUGH;
            }else if(e.equals(PointServiceException.GOODS_OUTOF_RESTAMMOUNT)){
                throw ActivityServiceException.AWARD_NOT_ENOUGH;
            }else{
                throw ActivityServiceException.AWARD_GET_FAILED;
            }
        }
    }
}
