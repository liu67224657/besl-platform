/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.profile.ProfileSumField;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-25 下午8:15
 * Description:
 */
public class ProfileSumSetEvent extends SystemEvent {
    //
    private String ownUno;
    private ProfileSumField field;
    private Integer count;

    //
    public ProfileSumSetEvent() {
        super(SystemEventType.PROFILE_SUM_SET);
    }

    public String getOwnUno() {
        return ownUno;
    }

    public void setOwnUno(String ownUno) {
        this.ownUno = ownUno;
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
        return ownUno.hashCode();
    }
}
