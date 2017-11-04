package com.enjoyf.webapps.joyme.dto.joymewiki;

import java.util.List;

/**
 * Created by pengxu on 2017/3/28.
 */
public class AlphabetGameDTO {
    private String title;
    private List<GameDTO> content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GameDTO> getContent() {
        return content;
    }

    public void setContent(List<GameDTO> content) {
        this.content = content;
    }
}
