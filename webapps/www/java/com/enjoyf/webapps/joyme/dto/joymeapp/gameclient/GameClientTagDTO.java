package com.enjoyf.webapps.joyme.dto.joymeapp.gameclient;

import com.enjoyf.platform.service.joymeapp.anime.GameClientTagType;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-27
 * Time: 下午12:20
 * To change this template use File | Settings | File Templates.
 */
public class GameClientTagDTO implements Serializable {
    private String type = GameClientTagType.DEFAULT.getCode();
    private String tagid;
    private String tagname;
    private String tagdesc;
    //private String selected;
    private String iconurl;

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

//    public String getSelected() {
//        return selected;
//    }
//
//    public void setSelected(String selected) {
//        this.selected = selected;
//    }


    public String getTagdesc() {
        return tagdesc;
    }

    public void setTagdesc(String tagdesc) {
        this.tagdesc = tagdesc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
