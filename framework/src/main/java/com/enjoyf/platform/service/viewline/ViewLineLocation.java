/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description:
 */
public class ViewLineLocation implements Serializable {
    //编号.
    private String code;

    //
    private String name;
    private String description;

    //
    private ViewItemType itemType;
    private int itemMinCount;

    //
    public ViewLineLocation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ViewItemType getItemType() {
        return itemType;
    }

    public void setItemType(ViewItemType itemType) {
        this.itemType = itemType;
    }

    public int getItemMinCount() {
        return itemMinCount;
    }

    public void setItemMinCount(int itemMinCount) {
        this.itemMinCount = itemMinCount;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
