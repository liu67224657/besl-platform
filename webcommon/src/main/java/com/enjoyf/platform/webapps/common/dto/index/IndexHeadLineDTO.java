package com.enjoyf.platform.webapps.common.dto.index;

import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class IndexHeadLineDTO {
    private String editor;
    private String editUrl;
    private List<ViewLineItemElementDTO> headlines;
    private String itemType;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

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

    public List<ViewLineItemElementDTO> getHeadlines() {
        return headlines;
    }

    public void setHeadlines(List<ViewLineItemElementDTO> headlines) {
        this.headlines = headlines;
    }
}
