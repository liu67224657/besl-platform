package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;

import java.util.Date;

/**
 * Created by pengxu on 2016/9/26.
 */
public class WanbaOtherPointAwardStragy implements PointAwardStragy {


    @Override
    public PointResultMsg arwardPoint(PointActionHistory history) throws ServiceException {

        String profileId = history.getProfileId();
        if (StringUtil.isEmpty(profileId)) {
            return PointResultMsg.OUT_OF_POINT_TIMES;
        }
        String key = history.getAppkey() + "_" + history.getActionType().getCode() + "_" + profileId + "_" + DateUtil.formatDateToString(new Date(), "yyyyMMdd");
        if (PointActionType.WANBA_SIGN.equals(history.getActionType())) { //在WIKI和玩霸之间 只能签一次到
            key = +history.getActionType().getCode() + "_" + profileId + "_" + DateUtil.formatDateToString(new Date(), "yyyyMMdd");
        }
        long value = PointServiceSngl.get().incrPointRule(key, 1l);
        //签到每日一次 其余每日五次
        if (history.getActionType().equals(PointActionType.WANBA_SIGN) && value > 1) {
            return PointResultMsg.OUT_OF_POINT_LIMIT;
        } else if (history.getActionType().equals(PointActionType.WIKI_CREATE_PAGE) && value > 4) {
            return PointResultMsg.OUT_OF_POINT_LIMIT;
        } else if (value > 5) {
            return PointResultMsg.OUT_OF_POINT_LIMIT;
        }
        //发送事件
        UserPointEvent userPointEvent = new UserPointEvent();
        userPointEvent.setProfileId(profileId);
        userPointEvent.setObjectId(key);
        userPointEvent.setPoint(history.getPointValue());
        userPointEvent.setActionDate(new Date());
        userPointEvent.setUno(history.getUserNo());
        userPointEvent.setAppkey(history.getAppkey());
        userPointEvent.setPointActionType(history.getActionType());
        boolean bool = EventDispatchServiceSngl.get().dispatch(userPointEvent);
        if (bool) {
            return PointResultMsg.SUCCESS;
        }
        return PointResultMsg.FAILED;
    }
}
