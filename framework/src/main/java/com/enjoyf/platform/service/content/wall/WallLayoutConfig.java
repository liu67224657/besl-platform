package com.enjoyf.platform.service.content.wall;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-4-1
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
public class WallLayoutConfig {
    //版面版式
    private WallLayoutType layoutType;
    //版面版块 type list
    private List<WallBlockType> wallBlockTypeList;

    public WallLayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(WallLayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public List<WallBlockType> getWallBlockTypeList() {
        return wallBlockTypeList;
    }

    public void setWallBlockTypeList(List<WallBlockType> wallBlockTypeList) {
        this.wallBlockTypeList = wallBlockTypeList;
    }
}
