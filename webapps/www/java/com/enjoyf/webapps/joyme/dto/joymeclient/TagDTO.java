package com.enjoyf.webapps.joyme.dto.joymeclient;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 下午5:17
 * Description:
 */
public class TagDTO {
    private long tid;
    private String tname;

    public TagDTO(long tid, String tname) {
        this.tid = tid;
        this.tname = tname;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }
}
