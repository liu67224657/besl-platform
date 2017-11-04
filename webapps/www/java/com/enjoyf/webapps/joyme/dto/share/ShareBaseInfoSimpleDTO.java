package com.enjoyf.webapps.joyme.dto.share;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class ShareBaseInfoSimpleDTO {
    private long share_id;
    private String display_style;

    public long getShare_id() {
        return share_id;
    }

    public void setShare_id(long share_id) {
        this.share_id = share_id;
    }

    public String getDisplay_style() {
        return display_style;
    }

    public void setDisplay_style(String display_style) {
        this.display_style = display_style;
    }
}
