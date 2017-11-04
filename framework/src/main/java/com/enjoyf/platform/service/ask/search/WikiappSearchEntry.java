package com.enjoyf.platform.service.ask.search;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/21
 */
public class WikiappSearchEntry implements Serializable {
    private String entryid;//唯一(id+type)
    private String id;
    private int type = WikiappSearchType.GAME.getCode();
    private String title;
    private long createtime;

    public String getEntryid() {
        return entryid;
    }

    public void setEntryid(String entryid) {
        this.entryid = entryid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public static WikiappSearchEntry toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, WikiappSearchEntry.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
