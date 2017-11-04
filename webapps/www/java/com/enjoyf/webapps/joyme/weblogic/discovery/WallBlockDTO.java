package com.enjoyf.webapps.joyme.weblogic.discovery;

import com.enjoyf.platform.service.content.wall.WallBlockStyleConfig;
import com.enjoyf.platform.service.content.wall.WallBlockType;
import com.enjoyf.platform.service.content.wall.WallContentType;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-27
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class WallBlockDTO implements Serializable {
        // wall content type
    private WallContentType wallContentType;
    // wall block type
    private WallBlockType wallBlockType;
    // style config
    private WallBlockStyleConfig styleConfig = new WallBlockStyleConfig();
    // html
    private String html;

    public WallBlockDTO(){

    }

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

    public WallBlockStyleConfig getStyleConfig() {
        return styleConfig;
    }

    public void setStyleConfig(WallBlockStyleConfig styleConfig) {
        this.styleConfig = styleConfig;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
