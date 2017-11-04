package com.enjoyf.webapps.joyme.dto.joymeapp.ask;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/9/12 0012.
 */
public class AskGameDTO implements Serializable {
    private String tagid;
    private String tagname;
    private String tagdesc;
    private String tagpic;

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

    public String getTagdesc() {
        return tagdesc;
    }

    public void setTagdesc(String tagdesc) {
        this.tagdesc = tagdesc;
    }

    public String getTagpic() {
        return tagpic;
    }

    public void setTagpic(String tagpic) {
        this.tagpic = tagpic;
    }
}
