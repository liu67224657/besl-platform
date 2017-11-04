/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.dto;

import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.service.viewline.ViewLineItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-8 下午1:14
 * Description:
 */
public class ViewCategoryDTO implements Serializable {
    //the category object
    private ViewCategory category;

    private List<ViewLineItem> itemsByCategory;

    private List<ViewCategoryDTO> children;

    private Map<Integer, List<ViewLine>> lineMapByCategoryId;

    private Map<Integer, Map<Integer, List<ViewLine>>> itemMapsByLineIdCategoryId;

    private Map<Integer, List<ViewLineItem>> itemsByLineId;


    public ViewCategory getCategory() {
        return category;
    }

    public void setCategory(ViewCategory category) {
        this.category = category;
    }

    public List<ViewLineItem> getItemsByCategory() {
        return itemsByCategory;
    }

    public void setItemsByCategory(List<ViewLineItem> itemsByCategory) {
        this.itemsByCategory = itemsByCategory;
    }

    public Map<Integer, List<ViewLine>> getLineMapByCategoryId() {
        return lineMapByCategoryId;
    }

    public void setLineMapByCategoryId(Map<Integer, List<ViewLine>> lineMapByCategoryId) {
        this.lineMapByCategoryId = lineMapByCategoryId;
    }

    public Map<Integer, Map<Integer, List<ViewLine>>> getItemMapsByLineIdCategoryId() {
        return itemMapsByLineIdCategoryId;
    }

    public void setItemMapsByLineIdCategoryId(Map<Integer, Map<Integer, List<ViewLine>>> itemMapsByLineIdCategoryId) {
        this.itemMapsByLineIdCategoryId = itemMapsByLineIdCategoryId;
    }

    public Map<Integer, List<ViewLineItem>> getItemsByLineId() {
        return itemsByLineId;
    }

    public void setItemsByLineId(Map<Integer, List<ViewLineItem>> itemsByLineId) {
        this.itemsByLineId = itemsByLineId;
    }

    public List<ViewCategoryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ViewCategoryDTO> children) {
        this.children = children;
    }
}