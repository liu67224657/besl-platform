package com.enjoyf.webapps.joyme.dto.joymeapp.gameclient;

import java.util.List;

/**
 * Created by zhimingli
 * Date: 2014/12/26
 * Time: 15:28
 */
public class GameClientTagDetailDTO {
    private List<GameClientContentDTO> content;
    private String type;
    private String archiveid="";

    public List<GameClientContentDTO> getContent() {
        return content;
    }

    public void setContent(List<GameClientContentDTO> content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getArchiveid() {
        return archiveid;
    }

    public void setArchiveid(String archiveid) {
        this.archiveid = archiveid;
    }
}
