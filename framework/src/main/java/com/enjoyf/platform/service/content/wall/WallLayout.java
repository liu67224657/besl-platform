package com.enjoyf.platform.service.content.wall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-8
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
public class WallLayout implements Serializable{
    //内存索引
    private int idx;
    //版面版式
    private WallLayoutType layoutType;
    //版面版块list
    private List<WallBlock> wallBlockList = new ArrayList<WallBlock>();

    public WallLayout(){

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

    public List<WallBlock> getWallBlockList() {
        return wallBlockList;
    }

    public void setWallBlockList(List<WallBlock> wallBlockList) {
        this.wallBlockList = wallBlockList;
    }
}
