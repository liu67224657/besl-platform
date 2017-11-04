package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.PointKeyType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.http.AppUtil;

import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description:
 */
public class TaskAwardPointStrategy implements TaskAwardStrategy {

    public boolean award(String taskId, TaskAward taskAward, String profileId, String appkey, Date taskDate, String uno) {
        UserPointEvent event = new UserPointEvent();
        event.setUno(uno);
        event.setObjectId(taskId);
        event.setPoint(Integer.parseInt(taskAward.getValue()));
        event.setPointActionType(PointActionType.SIGN);
        event.setDescription("");
        event.setActionDate(taskDate);
        event.setProfileId(profileId);
        event.setAppkey(appkey);

     //   event.setProfileKey(profileKey);
        //modified by tony 2015-05-14，不再使用pointKey和profileKey,使用appkey,在pointLogic中处理事件时再用appkey去查询pointkey
    //    event.setPointKey(PointKeyType.getByCode(AppUtil.getAppKey(appkey)).getValue());

        try {
            EventDispatchServiceSngl.get().dispatch(event);
        } catch (ServiceException e) {
        }

        return true;
    }
}
