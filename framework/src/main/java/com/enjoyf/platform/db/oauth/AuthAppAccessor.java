/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface AuthAppAccessor {
    //insert app
    public AuthApp insert(AuthApp app, Connection conn) throws DbException;

    //update app
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    //get app
    public AuthApp get(String appId, Connection conn) throws DbException;

    public List<AuthApp> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<AuthApp> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public AuthApp get(QueryExpress queryExpress, Connection conn) throws DbException;
}
