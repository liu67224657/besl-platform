package com.enjoyf.webapps.joyme.dto.content;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-20 下午5:19
 * Description:
 */
public class ForignContentSimpleDTO {
    private long contentid;
    private int replynum;

    public long getContentid() {
        return contentid;
    }

    public void setContentid(long contentid) {
        this.contentid = contentid;
    }

    public int getReplynum() {
        return replynum;
    }

    public void setReplynum(int replynum) {
        this.replynum = replynum;
    }
}
