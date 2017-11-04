package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-9 下午7:38
 * Description:
 */
public class ArchiveTag implements Serializable {
    private int tagId;
    private String tag;

    public ArchiveTag(int tagId, String tag) {
        this.tagId = tagId;
        this.tag = tag;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
