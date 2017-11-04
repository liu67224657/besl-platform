package com.enjoyf.webapps.joyme.dto.Wanba;

import java.io.Serializable;

/**
 * Created by zhimingli on 2016/9/19 0019.
 */
public class WanbaReplyDTO  implements Serializable {
    private WanbaReplyEntity reply;
    private WanbaProfileDTO replyprofile;

    public WanbaReplyEntity getReply() {
        return reply;
    }

    public void setReply(WanbaReplyEntity reply) {
        this.reply = reply;
    }

    public WanbaProfileDTO getReplyprofile() {
        return replyprofile;
    }

    public void setReplyprofile(WanbaProfileDTO replyprofile) {
        this.replyprofile = replyprofile;
    }
}
