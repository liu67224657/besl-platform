/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.pageview;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-6 下午1:47
 * Description:
 */
public class PageViewLocation implements Serializable {
    //the id store in the db.
    private String locationId;

    //the location function name and level.
    private String locationName;
    private Integer locationLevel;

    //the location url regex.
    private String locationUrlRegex;

    //
    private ValidStatus validStatus = ValidStatus.VALID;

    //
    public PageViewLocation() {
    }

    ///////////////////////////////////////////////////
    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getLocationLevel() {
        return locationLevel;
    }

    public void setLocationLevel(Integer locationLevel) {
        this.locationLevel = locationLevel;
    }

    public String getLocationUrlRegex() {
        return locationUrlRegex;
    }

    public void setLocationUrlRegex(String locationUrlRegex) {
        this.locationUrlRegex = locationUrlRegex;
    }

    public ValidStatus getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(ValidStatus validStatus) {
        this.validStatus = validStatus;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return locationId.hashCode();
    }
}
