/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.usercenter.ProfileSumField;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class ProfileSumIncreaseEvent extends SystemEvent {
    //
    private String profileId;
    private ProfileSumField field;
    private Integer count;

    //
    public ProfileSumIncreaseEvent() {
        super(SystemEventType.PROFILE_SUM_INCREASE);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public ProfileSumField getField() {
        return field;
    }

    public void setField(ProfileSumField field) {
        this.field = field;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return profileId.hashCode();
    }
}
