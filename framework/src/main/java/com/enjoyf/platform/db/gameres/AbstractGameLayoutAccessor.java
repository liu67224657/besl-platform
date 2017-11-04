package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Author: ericliu
 * Date: 11-8-25
 * Time: 下午4:53
 * Desc:
 */
abstract class AbstractGameLayoutAccessor extends AbstractBaseTableAccessor<GameLayout> implements GameLayoutAccessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TABLE_NAME = "GAME_RESOURCE_LAYOUT";

    @Override
    public GameLayout insert(GameLayout gameLayout, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //RESOURCEID,LAYOUTSETTING
            pstmt.setLong(1, gameLayout.getResourceId());
            pstmt.setString(2, gameLayout.getGameLayoutSetting().toJson());
            pstmt.execute();
            return gameLayout;
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    private String getInsertSql() {
        String sqlScript = "INSERT INTO " + TABLE_NAME + "(RESOURCEID,LAYOUTSETTING) VALUES (? ,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }

    @Override
    protected GameLayout rsToObject(ResultSet rs) throws SQLException {
        //RESOURCEID,LAYOUTCODE,VIEWLIST
        GameLayout gameLayout = new GameLayout();
        gameLayout.setResourceId(rs.getLong("RESOURCEID"));
        gameLayout.setGameLayoutSetting(GameLayoutSetting.parse(rs.getString("LAYOUTSETTING")));
        return gameLayout;
    }

    @Override
    public GameLayout get(QueryExpress getExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, getExpress, conn);
    }

    @Override
    public List<GameLayout> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(TABLE_NAME, queryExpress, conn);
    }
}
