package com.enjoyf.platform.service.event.system.wanba;

import com.enjoyf.platform.service.event.system.SystemEvent;
import com.enjoyf.platform.service.event.system.SystemEventType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class WanBaProfileSumIncrease extends SystemEvent {
    //
    private String profileId;
    private String field;//todo wanba sum field
    private int value;

    //
    public WanBaProfileSumIncrease() {
        super(SystemEventType.WANBA_PROFILESUM_INCREASE);
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
