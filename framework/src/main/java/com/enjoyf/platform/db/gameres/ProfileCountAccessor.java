package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.privilege.ProfileCount;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-16
 * Time: 下午7:38
 * To change this template use File | Settings | File Templates.
 */
public interface ProfileCountAccessor {

    public ProfileCount insert(ProfileCount profileCount, Connection conn) throws DbException;

    public ProfileCount get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ProfileCount> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
