package com.enjoyf.platform.cloud.contentservice.game;

import java.io.Serializable;

/**
 * Created by zhimingli on 2017/6/21.
 */
public class GameTagDTO implements Serializable {
    private Long id;
    private String tagName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
