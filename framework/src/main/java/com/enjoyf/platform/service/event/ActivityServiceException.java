package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.service.ServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-6
 * Time: 上午8:19
 * To change this template use File | Settings | File Templates.
 */
public class ActivityServiceException extends ServiceException{
    public static final ActivityServiceException AWARD_NOT_EXISTS = new ActivityServiceException(BASE_ACTIVITY + 1, "award.not.exists");

    public static final ActivityServiceException AWARD_NOT_ENOUGH = new ActivityServiceException(BASE_ACTIVITY + 3, "award.not.enough");

    public static final ActivityServiceException AWARD_GET_FAILED = new ActivityServiceException(BASE_ACTIVITY + 6, "award.get.failed");

    public static final ActivityServiceException USER_HAS_AWARD = new ActivityServiceException(BASE_ACTIVITY + 7, "user.has.award");

    public ActivityServiceException(int i, String s) {
        super(i, s);
    }

}
