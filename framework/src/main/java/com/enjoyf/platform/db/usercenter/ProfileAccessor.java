package com.enjoyf.platform.db.usercenter;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/10/23
 * Description:
 */
public interface ProfileAccessor {

    public Profile insert(Profile profile, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public Profile get(QueryExpress queryExpess, Connection conn) throws DbException;

    public List<Profile> query(QueryExpress add, Connection conn)throws DbException;

    public List<Profile> query(QueryExpress queryExpress, Pagination pagination, Connection conn)throws DbException;
}
