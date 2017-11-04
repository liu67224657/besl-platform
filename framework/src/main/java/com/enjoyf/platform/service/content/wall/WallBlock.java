package com.enjoyf.platform.service.content.wall;

import com.enjoyf.platform.service.content.DiscoveryWallContent;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-8
 * Time: 下午1:57
 * To change this template use File | Settings | File Templates.
 */
public class WallBlock implements Serializable {
    // wall content type
    private WallContentType wallContentType;
    // wall block type
    private WallBlockType wallBlockType;
    // style
    private String style;
    // style config
    private WallBlockStyleConfig styleConfig = new WallBlockStyleConfig();
    //wallcontent
    private DiscoveryWallContent discoveryWallContent;

    public WallContentType getWallContentType() {
        return wallContentType;
    }

    public void setWallContentType(WallContentType wallContentType) {
        this.wallContentType = wallContentType;
    }

    public WallBlockType getWallBlockType() {
        return wallBlockType;
    }

    public void setWallBlockType(WallBlockType wallBlockType) {
        this.wallBlockType = wallBlockType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public WallBlockStyleConfig getStyleConfig() {
        return styleConfig;
    }

    public void setStyleConfig(WallBlockStyleConfig styleConfig) {
        this.styleConfig = styleConfig;
    }

    public DiscoveryWallContent getDiscoveryWallContent() {
        return discoveryWallContent;
    }

    public void setDiscoveryWallContent(DiscoveryWallContent discoveryWallContent) {
        this.discoveryWallContent = discoveryWallContent;
    }
}
