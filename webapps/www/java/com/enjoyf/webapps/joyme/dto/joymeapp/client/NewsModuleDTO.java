package com.enjoyf.webapps.joyme.dto.joymeapp.client;

import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/2/10
 * Description:
 */
public class NewsModuleDTO {
    private String name;
    private List<NewsModuleDataDTO> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NewsModuleDataDTO> getList() {
        return list;
    }

    public void setList(List<NewsModuleDataDTO> list) {
        this.list = list;
    }
}
