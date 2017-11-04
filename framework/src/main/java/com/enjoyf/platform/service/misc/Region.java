/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-5 下午3:05
 * Description:
 */
public class Region implements Serializable {
    //
    private Integer regionId;

    private String regionName;

    private Integer parentRegionId;
    private Integer regionLevel;

    public Region() {
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getParentRegionId() {
        return parentRegionId;
    }

    public void setParentRegionId(Integer parentRegionId) {
        this.parentRegionId = parentRegionId;
    }

    public Integer getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(Integer regionLevel) {
        this.regionLevel = regionLevel;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
