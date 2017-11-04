package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.GroupPrivilege;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-15
 * Time: 下午12:17
 * To change this template use File | Settings | File Templates.
 */
public interface GroupPrivilegeAccessor {

    public GroupPrivilege insert(GroupPrivilege privilege, Connection conn) throws DbException;

    public GroupPrivilege get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GroupPrivilege> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GroupPrivilege> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
