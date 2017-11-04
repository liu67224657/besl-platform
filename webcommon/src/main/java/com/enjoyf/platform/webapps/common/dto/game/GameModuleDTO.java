package com.enjoyf.platform.webapps.common.dto.game;

import com.enjoyf.platform.service.gameres.GamePostType;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.webapps.common.dto.index.ViewLineItemElementDTO;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-11-19
 * Time: 上午10:44
 * To change this template use File | Settings | File Templates.
 */
public class GameModuleDTO {
    private long moduleId;
    private String moduleName;
    private String moduleDesc;
    private GamePostType modulePostType;
    private GameDisplayType displyType;

    private PageRows<ViewLineItemElementDTO> contentRows;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public GamePostType getModulePostType() {
        return modulePostType;
    }

    public void setModulePostType(GamePostType modulePostType) {
        this.modulePostType = modulePostType;
    }

    public GameDisplayType getDisplyType() {
        return displyType;
    }

    public void setDisplyType(GameDisplayType displyType) {
        this.displyType = displyType;
    }

    public PageRows<ViewLineItemElementDTO> getContentRows() {
        return contentRows;
    }

    public void setContentRows(PageRows<ViewLineItemElementDTO> contentRows) {
        this.contentRows = contentRows;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }
}
