package com.enjoyf.platform.service.ask.search;


import com.enjoyf.platform.service.ask.AskUtil;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/21
 */
public class WikiappSearchDeleteEntry implements Serializable {
    private String id;//唯一(id+type)  //调用askutil
    private WikiappSearchType type = WikiappSearchType.GAME;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WikiappSearchType getType() {
        return type;
    }

    public void setType(WikiappSearchType type) {
        this.type = type;
    }

    public String getDeleteId(WikiappSearchDeleteEntry deleteEntry) {
        StringBuffer sb = new StringBuffer();
        sb.append("(entryid:" + AskUtil.getWikiappSearchEntryId(deleteEntry.getId(), deleteEntry.type) + ")");
        return sb.toString();
    }
}
