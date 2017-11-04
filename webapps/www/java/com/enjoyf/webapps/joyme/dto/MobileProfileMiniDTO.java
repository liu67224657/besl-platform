/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-23 下午1:47
 * Description:
 */
public class MobileProfileMiniDTO {
    //
    private String uno;
    private String name;
    private String icon;

    //constructor
    public MobileProfileMiniDTO() {
    }

    //the getter and setter
    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    //to string
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
