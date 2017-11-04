package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialContentReply;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-14 下午8:46
 * Description:
 */
public interface SocialContentReplyAccessor {

    public SocialContentReply insert(SocialContentReply reply, Connection conn) throws DbException;

    public List<SocialContentReply> queryByRootId(long contentId, Long rootId, Pagination page, Connection conn) throws DbException;

    public List<SocialContentReply> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialContentReply> queryByPage(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
