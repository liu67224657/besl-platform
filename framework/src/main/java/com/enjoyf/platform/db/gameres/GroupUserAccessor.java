package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GroupUser;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-25 下午12:00
 * Description:
 */
public interface GroupUserAccessor {


    public GroupUser insert(GroupUser groupUser, Connection conn) throws DbException;

    public GroupUser getByGroupIdUno(String uno, long groupid, Connection conn) throws DbException;

    public List<GroupUser> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<GroupUser> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
