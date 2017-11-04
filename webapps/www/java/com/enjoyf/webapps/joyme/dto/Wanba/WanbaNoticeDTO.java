package com.enjoyf.webapps.joyme.dto.Wanba;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/23
 */
public class WanbaNoticeDTO {
    private long noticeid;
    private String jt;
    private String ji;
    private String text;
    private String title;
    private String time;


    public long getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(long noticeid) {
        this.noticeid = noticeid;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    public String getJi() {
        return ji;
    }

    public void setJi(String ji) {
        this.ji = ji;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
