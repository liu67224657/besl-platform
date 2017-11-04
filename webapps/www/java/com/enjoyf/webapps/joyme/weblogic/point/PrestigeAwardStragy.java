package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.service.ServiceException;

import java.util.Date;

/**
 * Created by zhimingli on 2016/10/26 0026.
 */
public class PrestigeAwardStragy implements PointAwardStragy {
    @Override
    public PointResultMsg arwardPoint(PointActionHistory history) throws ServiceException {
        String key = history.getActionType().getCode() + "_" + history.getProfileId();
        //发送事件
        UserPointEvent userPointEvent = new UserPointEvent();
        userPointEvent.setProfileId(history.getProfileId());
        userPointEvent.setObjectId(key);
        userPointEvent.setPoint(history.getPointValue());
        userPointEvent.setPrestige(history.getPrestige());
        userPointEvent.setActionDate(new Date());
        userPointEvent.setUno(history.getUserNo());
        userPointEvent.setAppkey(history.getAppkey());
        userPointEvent.setPointActionType(history.getActionType());
        userPointEvent.setHistoryActionType(history.getHistoryActionType());
        boolean bool = EventDispatchServiceSngl.get().dispatch(userPointEvent);
        if (bool) {
            return PointResultMsg.SUCCESS;
        }
        return PointResultMsg.FAILED;
    }
}
