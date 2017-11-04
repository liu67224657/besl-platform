package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-19
 * Time: 上午9:56
 * To change this template use File | Settings | File Templates.
 */
public class AbstractTaskEventProcessor {

    public void increaseUserPoint(String profileId, int point, String objectId, Date actionDate, PointActionType pointActionType, String description, String appKey, String pointKey) {
        try {
            UserPointEvent userPointEvent = new UserPointEvent();
            userPointEvent.setProfileId(profileId);
            userPointEvent.setPoint(point);
            userPointEvent.setDescription(description);
            userPointEvent.setObjectId(objectId);
            userPointEvent.setActionDate(actionDate);
         //   userPointEvent.setProfileKey(appKey);    modified by tony 2015-05-14，不再使用pointKey和profileKey,使用appkey,在pointLogic中处理事件时再用appkey去查询pointkey

            userPointEvent.setAppkey(appKey);
            userPointEvent.setPointActionType(pointActionType);
            EventDispatchServiceSngl.get().dispatch(userPointEvent);
        } catch (Exception e) {
            GAlerter.lab("AbstractTaskEventProcessor occur Exception.e:", e);
        }
    }

}
