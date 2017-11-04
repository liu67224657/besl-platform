package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.GroupProfile;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-15
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public interface GroupProfileAccessor {

    public GroupProfile insert(GroupProfile groupMember, Connection conn) throws DbException;

    public GroupProfile get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GroupProfile> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GroupProfile> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
