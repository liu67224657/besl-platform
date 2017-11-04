package com.enjoyf.platform.util.html;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-16
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class HeadMenuGroup {
    private String groupName;
    private String moreUrl;
    private List<HeadMenuProps> headMenuPropsList;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<HeadMenuProps> getHeadMenuPropsList() {
        return headMenuPropsList;
    }

    public void setHeadMenuPropsList(List<HeadMenuProps> headMenuPropsList) {
        this.headMenuPropsList = headMenuPropsList;
    }

    public String getMoreUrl() {
        return moreUrl;
    }

    public void setMoreUrl(String moreUrl) {
        this.moreUrl = moreUrl;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
