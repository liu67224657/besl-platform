package com.enjoyf.webapps.joyme.dto.content;

import com.enjoyf.platform.util.Pagination;

import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-20 下午5:19
 * Description:
 */
public class ForignContentDTO {
    private long contentId;
    private int replyNum;
    private List<ForignContentReplyDTO> replyList;
    private Pagination page;

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public List<ForignContentReplyDTO> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ForignContentReplyDTO> replyList) {
        this.replyList = replyList;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public Pagination getPage() {
        return page;
    }

    public void setPage(Pagination page) {
        this.page = page;
    }
}
