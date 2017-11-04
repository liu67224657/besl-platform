package com.enjoyf.webapps.joyme.dto.comment;

import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-12
 * Time: 下午9:28
 * To change this template use File | Settings | File Templates.
 */
public class MainReplyDTO {

    private ReplyDTO reply;

    private PageRows<ReplyDTO> subreplys;

    public ReplyDTO getReply() {
        return reply;
    }

    public void setReply(ReplyDTO reply) {
        this.reply = reply;
    }

    public PageRows<ReplyDTO> getSubreplys() {
        return subreplys;
    }

    public void setSubreplys(PageRows<ReplyDTO> subreplys) {
        this.subreplys = subreplys;
    }
}
