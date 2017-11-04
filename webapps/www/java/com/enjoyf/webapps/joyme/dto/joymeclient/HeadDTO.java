package com.enjoyf.webapps.joyme.dto.joymeclient;

import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 下午4:57
 * Description: 首页的推荐和头图的dto
 */
public class HeadDTO {
    private List<LineItemDTO> menu;
    private List<LineItemDTO> recom;

    public List<LineItemDTO> getMenu() {
        return menu;
    }

    public void setMenu(List<LineItemDTO> menu) {
        this.menu = menu;
    }

    public List<LineItemDTO> getRecom() {
        return recom;
    }

    public void setRecom(List<LineItemDTO> recom) {
        this.recom = recom;
    }
}
