package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-24 上午10:04
 * Description:
 */
public class ActivityCalEventTimeEvent extends SystemEvent {

    private Long activityId;

    public ActivityCalEventTimeEvent() {
        super(SystemEventType.ACTIVITY_CALEVENTTIME_GIFT);
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

        //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
