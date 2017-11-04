package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.GroupCount;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-16
 * Time: 下午7:14
 * To change this template use File | Settings | File Templates.
 */
public interface GroupCountAccessor {

    public GroupCount insert(GroupCount groupCount, Connection conn) throws DbException;

    public GroupCount get(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException;
}
