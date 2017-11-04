package com.enjoyf.webapps.joyme.dto.content;

import com.enjoyf.platform.util.PageRows;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-20 下午5:59
 * Description:
 */
public class WikiCommentReplyDTO {
    private WikiCommentReplyEntry reply;

    private PageRows<WikiCommentReplyEntry> subreplys;

    public WikiCommentReplyEntry getReply() {
        return reply;
    }

    public void setReply(WikiCommentReplyEntry reply) {
        this.reply = reply;
    }

    public PageRows<WikiCommentReplyEntry> getSubreplys() {
        return subreplys;
    }

    public void setSubreplys(PageRows<WikiCommentReplyEntry> subreplys) {
        this.subreplys = subreplys;
    }
}
