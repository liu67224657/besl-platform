package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ForignContentReply;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-15 下午4:50
 * Description:
 */
public interface ForignContentReplyAccessor {

    public ForignContentReply insert(ForignContentReply contentReply, Connection connection) throws DbException;

    public ForignContentReply get(QueryExpress getExpress, Connection connection) throws DbException;

    public List<ForignContentReply> queryByRootId(long contentId, long rootId, Pagination pagination, Connection connection) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;

    public List<ForignContentReply> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ForignContentReply> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<Long> queryHotReplyId(long contentId,int size, Connection conn)throws DbException;

    public ForignContentReply getById(long replyId, Connection conn)throws DbException;

    public int getMaxFollorNum(long rootId, long contentId, Connection connection) throws DbException;
}
