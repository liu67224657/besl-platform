package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.PrivilegeRoleRelation;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-15
 * Time: 下午1:32
 * To change this template use File | Settings | File Templates.
 */
public interface PrivilegeRoleRelationAccessor {

    public PrivilegeRoleRelation insert(PrivilegeRoleRelation privilegeRoleRelation, Connection conn) throws DbException;

    public PrivilegeRoleRelation get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<PrivilegeRoleRelation> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<PrivilegeRoleRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
