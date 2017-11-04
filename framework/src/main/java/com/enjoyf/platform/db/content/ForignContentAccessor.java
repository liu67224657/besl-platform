package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ForignContent;
import com.enjoyf.platform.service.content.ForignContentDomain;
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
public interface ForignContentAccessor {

    public ForignContent insert(ForignContent forignContent, Connection connection) throws DbException;

    public ForignContent getByContentId(long contentId, Connection connection) throws DbException;

    public ForignContent getByFroignId(String fid,ForignContentDomain domain, Connection connection) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;

    public List<ForignContent> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ForignContent> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;
}
