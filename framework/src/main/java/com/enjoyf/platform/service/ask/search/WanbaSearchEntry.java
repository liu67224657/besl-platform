package com.enjoyf.platform.service.ask.search;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/21
 */
public class WanbaSearchEntry implements Serializable {
    private String entryid;//唯一(id+type)
    private String id;
    private int type = WanbaSearchType.QUESION.getCode();
    private String tag = "";
    private String title;
    private String content;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String getIndexField() {
        StringBuffer sb = new StringBuffer();
        sb.append("entryid").append("=").append(this.entryid).append(",")
                .append("id").append("=").append(this.id).append(",")
                .append("type").append("=").append(this.type).append(",")
                .append("tag").append("=").append(this.tag).append(",")
                .append("title").append("=").append(this.title).append(",")
                .append("content").append("=").append(this.content);
        return sb.toString();
    }

}
