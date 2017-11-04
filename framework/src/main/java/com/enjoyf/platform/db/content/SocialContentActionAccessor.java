package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialContentAction;
import com.enjoyf.platform.service.content.social.SocialContentActionType;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-15 下午1:56
 * Description:
 */
public interface SocialContentActionAccessor {

    public SocialContentAction insert(SocialContentAction action, Connection conn) throws DbException;

    public List<SocialContentAction> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialContentAction> query(long contentId,SocialContentActionType type,NextPagination nextPagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn)throws DbException;

    public SocialContentAction get(QueryExpress queryExpress, Connection conn) throws DbException;
}
