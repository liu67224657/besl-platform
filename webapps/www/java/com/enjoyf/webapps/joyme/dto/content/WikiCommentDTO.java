package com.enjoyf.webapps.joyme.dto.content;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;

import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-20 下午5:19
 * Description:
 */
public class WikiCommentDTO {
    private long cid;
    private int csum;
    private PageRows<WikiCommentReplyDTO> comments;

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public int getCsum() {
        return csum;
    }

    public void setCsum(int csum) {
        this.csum = csum;
    }

    public PageRows<WikiCommentReplyDTO> getComments() {
        return comments;
    }

    public void setComments(PageRows<WikiCommentReplyDTO> comments) {
        this.comments = comments;
    }
}
