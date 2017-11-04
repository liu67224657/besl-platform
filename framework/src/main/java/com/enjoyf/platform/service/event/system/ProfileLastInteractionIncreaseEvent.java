/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.profile.ProfileSumField;
import com.enjoyf.platform.util.reflect.ReflectUtil;

public class ProfileLastInteractionIncreaseEvent extends SystemEvent {
    //
    private String ownUno;
    private ProfileSumField field;
    private String value;

    //
    public ProfileLastInteractionIncreaseEvent() {
        super(SystemEventType.PROFILE_SUM_INCREASE);
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
