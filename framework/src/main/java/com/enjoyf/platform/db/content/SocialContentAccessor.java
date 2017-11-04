package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialContent;
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
public interface SocialContentAccessor {

    public SocialContent insert(SocialContent socialContent, Connection connection) throws DbException;

    public SocialContent getByContentId(QueryExpress queryExpress, Connection connection) throws DbException;

    public SocialContent getByContentId(long contentId, Connection connection) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;

    public List<SocialContent> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialContent> queryByUno(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;
}
