package com.enjoyf.platform.webapps.common.dto.game;

import com.enjoyf.platform.service.gameres.GamePostType;
import com.enjoyf.platform.service.gameres.GameViewLayoutType;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.webapps.common.dto.ViewCategoryDTO;
import com.enjoyf.platform.webapps.common.dto.index.ViewLineItemElementDTO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-5
 * Time: 下午11:23
 * game layout dto for jsp
 */
public class GameLayoutDTO {
    private long viewId; //对应的lineid 百科的id
    private String viewName;

    private GameViewLayoutType type;

    private String desc;
    private String desc2;

    private GamePostType modulePostType;
    private PageRows<ViewLineItemElementDTO> contentRows;

    private List<ViewCategoryDTO> baikeDTOTree;

    public long getViewId() {
        return viewId;
    }

    public void setViewId(long viewId) {
        this.viewId = viewId;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public GameViewLayoutType getType() {
        return type;
    }

    public void setType(GameViewLayoutType type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public GamePostType getModulePostType() {
        return modulePostType;
    }

    public void setModulePostType(GamePostType modulePostType) {
        this.modulePostType = modulePostType;
    }

    public PageRows<ViewLineItemElementDTO> getContentRows() {
        return contentRows;
    }

    public void setContentRows(PageRows<ViewLineItemElementDTO> contentRows) {
        this.contentRows = contentRows;
    }

    public List<ViewCategoryDTO> getBaikeDTOTree() {
        return baikeDTOTree;
    }

    public void setBaikeDTOTree(List<ViewCategoryDTO> baikeDTOTree) {
        this.baikeDTOTree = baikeDTOTree;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }
}
