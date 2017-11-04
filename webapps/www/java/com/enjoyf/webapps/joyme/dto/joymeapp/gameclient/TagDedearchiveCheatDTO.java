package com.enjoyf.webapps.joyme.dto.joymeapp.gameclient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Created by zhimingli on 2015/7/31.
 */
public class TagDedearchiveCheatDTO implements Serializable {
    private String archivesid;
    private String readnum;

    public String getArchivesid() {
        return archivesid;
    }

    public void setArchivesid(String archivesid) {
        this.archivesid = archivesid;
    }

    public String getReadnum() {
        return readnum;
    }

    public void setReadnum(String readnum) {
        this.readnum = readnum;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);

    }
}
