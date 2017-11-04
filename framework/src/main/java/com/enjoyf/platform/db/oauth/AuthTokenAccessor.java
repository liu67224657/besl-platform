/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.oauth.AuthToken;

import java.sql.Connection;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface AuthTokenAccessor {

    //insert token
    public AuthToken insert(AuthToken token, Connection conn) throws DbException;

    //remove token
    public boolean remove(String token, Connection conn) throws DbException;

    //remove token
    public int clearExpired(Connection conn) throws DbException;

    //get token
    public AuthToken get(String token, Connection conn) throws DbException;


}
