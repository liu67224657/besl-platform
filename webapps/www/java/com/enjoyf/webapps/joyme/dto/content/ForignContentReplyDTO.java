package com.enjoyf.webapps.joyme.dto.content;

import com.enjoyf.platform.service.content.ForignContentReply;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.util.PageRows;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-20 下午5:59
 * Description:
 */
public class ForignContentReplyDTO {
    private ForignContentReplyEntry reply;

    private PageRows<ForignContentReplyEntry> reReplys;

    public ForignContentReplyEntry getReply() {
        return reply;
    }

    public void setReply(ForignContentReplyEntry reply) {
        this.reply = reply;
    }

    public PageRows<ForignContentReplyEntry> getReReplys() {
        return reReplys;
    }

    public void setReReplys(PageRows<ForignContentReplyEntry> reReplys) {
        this.reReplys = reReplys;
    }
}
