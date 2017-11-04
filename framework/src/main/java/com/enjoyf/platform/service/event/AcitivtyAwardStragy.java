package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.service.ServiceException;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:46
 * To change this template use File | Settings | File Templates.
 */
public interface AcitivtyAwardStragy {

    public AwardResult sendAward(Activity activity,String uno,String profileid,String appKey,Date awardDate,String ip)throws ServiceException;
}
