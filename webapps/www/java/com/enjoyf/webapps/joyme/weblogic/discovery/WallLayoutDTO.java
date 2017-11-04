package com.enjoyf.webapps.joyme.weblogic.discovery;

import com.enjoyf.platform.service.content.wall.WallLayoutType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-27
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class WallLayoutDTO implements Serializable {
    //内存索引
    private int idx;
    //版面版式
    private WallLayoutType layoutType;
    //版面版块list
    private List<WallBlockDTO> wallBlockDTOList = new ArrayList<WallBlockDTO>();
    
    // html
    private String layoutHtml;

    public WallLayoutDTO() {

    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public WallLayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(WallLayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public List<WallBlockDTO> getWallBlockDTOList() {
        return wallBlockDTOList;
    }

    public void setWallBlockDTOList(List<WallBlockDTO> wallBlockDTOList) {
        this.wallBlockDTOList = wallBlockDTOList;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }
}
