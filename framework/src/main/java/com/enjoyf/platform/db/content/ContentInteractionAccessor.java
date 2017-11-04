package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ContentInteraction;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public interface ContentInteractionAccessor {
    public ContentInteraction insert(ContentInteraction interaction, Connection conn) throws DbException;

    public ContentInteraction getInteraction(String contentId, QueryExpress getExpress, Connection conn) throws DbException;

    public long getFloorNum(Connection conn) throws DbException;

    public long getMaxFloorNum(String contentId, Connection conn) throws DbException;

    public boolean updateInteraction(String contentId, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ContentInteraction> queryInteraction(String contentId, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<ContentInteraction> queryInteraction(String contentId, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ContentInteraction> queryByIIdsCid(List<String> iIds, String cid, Connection conn) throws DbException;

    public List<ContentInteraction> queryContentReply(QueryExpress queryExpress, Pagination p, Connection conn) throws DbException;

    //
    public int countByQueryExpress(String contentId, QueryExpress getExpress, Connection conn) throws DbException;

    //
    public long queryReplyTimes(String contentId, Date from, Date to, Connection conn) throws DbException;
}
