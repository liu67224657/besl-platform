package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.webapps.common.dto.ViewCategoryDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class BaikeTreeDTO {
    private GameResource gameResource;
    private List<ViewCategoryDTO> baikeDTOList;
    private List<ViewCategory> baikePathCategory = new ArrayList<ViewCategory>();

    public GameResource getGameResource() {
        return gameResource;
    }

    public void setGameResource(GameResource gameResource) {
        this.gameResource = gameResource;
    }

    public List<ViewCategoryDTO> getBaikeDTOList() {
        return baikeDTOList;
    }

    public void setBaikeDTOList(List<ViewCategoryDTO> baikeDTOList) {
        this.baikeDTOList = baikeDTOList;
    }

    public List<ViewCategory> getBaikePathCategory() {
        return baikePathCategory;
    }

    public void setBaikePathCategory(List<ViewCategory> baikePathCategory) {
        this.baikePathCategory = baikePathCategory;
    }

    public void addBaikePathCategory(ViewCategory baikePathCategory) {
        this.baikePathCategory.add(baikePathCategory);
    }
}
