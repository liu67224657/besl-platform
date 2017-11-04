package com.enjoyf.platform.webapps.common.dto.index;

import com.enjoyf.platform.service.viewline.ViewItemType;
import com.enjoyf.platform.service.viewline.ViewLineItem;

import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class IndexHeadLineElementDTO {
    private String editor;
    private String editUrl;
    private List<ViewLineItem> lineItems;
    private ViewItemType itemType;

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }

    public List<ViewLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<ViewLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public ViewItemType getItemType() {
        return itemType;
    }

    public void setItemType(ViewItemType itemType) {
        this.itemType = itemType;
    }
}
