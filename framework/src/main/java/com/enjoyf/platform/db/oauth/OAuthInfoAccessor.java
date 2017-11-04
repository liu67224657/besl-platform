/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.service.oauth.OAuthInfo;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface OAuthInfoAccessor {

    public OAuthInfo insert(OAuthInfo oAuthInfo, Connection conn) throws DbException;

    public OAuthInfo getAccess(QueryExpress queryExpress, Connection conn) throws DbException;

    public OAuthInfo getRefresh(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<OAuthInfo> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

}
