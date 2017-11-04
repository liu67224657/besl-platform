package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.Role;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-15
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public interface RoleAccessor {

    public Role insert(Role role, Connection conn) throws DbException;

    public List<Role> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public Role get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Role> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;
}
