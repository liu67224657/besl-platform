package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.point.PointActionHistoryField;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.util.Date;

/**
 * Created by pengxu on 2016/9/26.
 */
public class WanbaLoginAndBindPointAwardStragy implements PointAwardStragy {


    @Override
    public PointResultMsg arwardPoint(PointActionHistory history) throws ServiceException {
        String profileId = history.getProfileId();
        if (StringUtil.isEmpty(profileId)) {
            return PointResultMsg.OUT_OF_POINT_TIMES;
        }
        String key = "";
        if (history.getActionType().equals(PointActionType.OTHER_LOGIN_AND_BIND)) {
            key = history.getActionType().getCode() + "_" + profileId + history.getLogindomain();
        } else {
            key = history.getActionType().getCode() + "_" + profileId;
        }

        long value = PointServiceSngl.get().incrPointRule(key, 1l);
        //如果>1表示已经进来给过积分了
        if (value > 1) {
            return PointResultMsg.OUT_OF_POINT_LIMIT;
        }
        //防止redis数据丢失重复给积分
        PointActionHistory pointActionHistory = PointServiceSngl.get().getPointActionHistory(new QueryExpress().add(QueryCriterions.eq(PointActionHistoryField.DESTID, key)));
        if (pointActionHistory != null) {
            return PointResultMsg.OUT_OF_POINT_LIMIT;
        }
        //发送事件
        UserPointEvent userPointEvent = new UserPointEvent();
        userPointEvent.setProfileId(profileId);
        userPointEvent.setObjectId(key);
        userPointEvent.setUno(history.getUserNo());
        userPointEvent.setPoint(history.getPointValue());
        userPointEvent.setActionDate(new Date());
        userPointEvent.setAppkey(history.getAppkey());
        userPointEvent.setPointActionType(history.getActionType());
        boolean bool = EventDispatchServiceSngl.get().dispatch(userPointEvent);
        if (bool) {
            return PointResultMsg.SUCCESS;
        }
        return PointResultMsg.FAILED;
    }
}
