/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.oauth;

import com.enjoyf.platform.db.*;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.oauth.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;


/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class OAuthHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private AuthTokenAccessor authTokenAccessor;
    private AuthAppAccessor authAppAccessor;

    private OAuthInfoAccessor oAuthInfoAccessor;

    private GameChannelInfoAccessor gameChannelInfoAccessor;
    private GameChannelConfigAccessor gameChannelConfigAccessor;

    //
    public OAuthHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        authTokenAccessor = OAuthAccessorFactory.factoryAuthTokenAccessor(dataBaseType);
        authAppAccessor = OAuthAccessorFactory.factoryAuthAppAccessor(dataBaseType);

        oAuthInfoAccessor = TableAccessorFactory.get().factoryAccessor(OAuthInfoAccessor.class, dataBaseType);

        gameChannelInfoAccessor = TableAccessorFactory.get().factoryAccessor(GameChannelInfoAccessor.class, dataBaseType);

        gameChannelConfigAccessor = TableAccessorFactory.get().factoryAccessor(GameChannelConfigAccessor.class, dataBaseType);
    }

    //
    public AuthToken createAuthToken(AuthToken entry) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authTokenAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AuthToken getAuthToken(String token) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authTokenAccessor.get(token, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean removeAuthToken(String token) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authTokenAccessor.remove(token, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int clearExpireTokens() throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authTokenAccessor.clearExpired(conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //
    public AuthApp createAuthApp(AuthApp entry) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authAppAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AuthApp getAuthApp(String appId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authAppAccessor.get(appId, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AuthApp getAuthAppByGameKey(String gamekey) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authAppAccessor.get(new QueryExpress().add(QueryCriterions.eq(AuthAppField.GAMEKEY, gamekey)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateAuthApp(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authAppAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AuthApp> queryAuthApp(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return authAppAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AuthApp> queryAuthApp(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<AuthApp> returnObj = null;

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            returnObj = new PageRows<AuthApp>();
            returnObj.setRows(authAppAccessor.query(queryExpress, pagination, conn));
            returnObj.setPage(pagination);

            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<OAuthInfo> query(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return oAuthInfoAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return oAuthInfoAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public OAuthInfo insert(OAuthInfo authInfo) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return oAuthInfoAccessor.insert(authInfo, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public OAuthInfo getAccess(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return oAuthInfoAccessor.getAccess(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public OAuthInfo getRefresh(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return oAuthInfoAccessor.getRefresh(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /////////////////////////////////////////
    public GameChannelInfo insertAuthAppChannelInfo(GameChannelInfo channelInfo) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameChannelInfoAccessor.insert(channelInfo, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GameChannelInfo getAuthAppChannelInfo(String infoId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameChannelInfoAccessor.get(new QueryExpress().add(QueryCriterions.eq(GameChannelInfoField.INFOID, infoId)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GameChannelInfo> queryAuthAppChannelInfo(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameChannelInfoAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateAuthAppChannelInfo(UpdateExpress updateExpress, String infoId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameChannelInfoAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(GameChannelInfoField.INFOID, infoId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean deleteAuthAppChannelInfo(String infoId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameChannelInfoAccessor.delete(new QueryExpress().add(QueryCriterions.eq(GameChannelInfoField.INFOID, infoId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GameChannelConfig insertGameChannelConfig(GameChannelConfig config) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gameChannelConfigAccessor.insert(config, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public PageRows<GameChannelConfig> queryGameChannelConfig(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            PageRows<GameChannelConfig> rows = new PageRows<GameChannelConfig>();
            rows.setPage(pagination);
            rows.setRows(gameChannelConfigAccessor.query(queryExpress, pagination, conn));

            return rows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public GameChannelConfig getGameChannelConfig(String configId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return gameChannelConfigAccessor.get(new QueryExpress().add(QueryCriterions.eq(GameChannelConfigField.CONFIGID, configId)), conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateGameChannelConfig(UpdateExpress updateExpress, String configId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);


            return gameChannelConfigAccessor.update(updateExpress, new QueryExpress().add(QueryCriterions.eq(GameChannelConfigField.CONFIGID, configId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean deleteGameChannelConfig(String configId) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);


            return gameChannelConfigAccessor.delete(new QueryExpress().add(QueryCriterions.eq(GameChannelConfigField.CONFIGID, configId)), conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}

