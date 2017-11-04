package com.enjoyf.platform.webapps.common.dto.index;

import com.enjoyf.platform.service.viewline.ViewCategory;

import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GroupPageDTO {

    private List<ViewLineItemElementDTO> groupList;

    private ViewCategory viewCategory;

    public ViewCategory getViewCategory() {
        return viewCategory;
    }

    public void setViewCategory(ViewCategory viewCategory) {
        this.viewCategory = viewCategory;
    }

    public List<ViewLineItemElementDTO> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<ViewLineItemElementDTO> groupList) {
        this.groupList = groupList;
    }
}
