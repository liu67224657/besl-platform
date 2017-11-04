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

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Author: ericliu
 * Date: 11-8-25
 * Time: 下午4:53
 * Desc:
 */
abstract class AbstractGamePrivacyAccessor extends AbstractBaseTableAccessor<GamePrivacy> implements GamePrivacyAccessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TABLE_NAME = "GAME_RESOURCE_PRIVACY";

    @Override
    public GamePrivacy insert(GamePrivacy gamePrivcacy, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //RESOURCEID,UNO,PRIVCAYLEVEL,RESOURCEDOMAIN,CREATEDATE,CREATEUSERID,UPDATEDATE,UPDATEUSERID
            pstmt.setLong(1, gamePrivcacy.getResourceId());
            pstmt.setString(2, gamePrivcacy.getUno());
            pstmt.setString(3, gamePrivcacy.getPrivacyLevel().getCode());
            pstmt.setString(4, gamePrivcacy.getResourceDomain().getCode());
            pstmt.setTimestamp(5, new Timestamp(gamePrivcacy.getCreateDate() == null ? System.currentTimeMillis() : gamePrivcacy.getCreateDate().getTime()));
            pstmt.setString(6, gamePrivcacy.getCreateUserid());
            pstmt.setTimestamp(7, new Timestamp(gamePrivcacy.getUpdateDate() == null ? System.currentTimeMillis() : gamePrivcacy.getUpdateDate().getTime()));
            pstmt.setString(8, gamePrivcacy.getUpdateUserid());

            pstmt.execute();
            return gamePrivcacy;
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    private String getInsertSql() {
        String sqlScript = "INSERT INTO " + TABLE_NAME + "(RESOURCEID,UNO,PRIVCAYLEVEL,RESOURCEDOMAIN,CREATEDATE,CREATEUSERID,UPDATEDATE,UPDATEUSERID) VALUES (? ,?, ? ,?, ? ,?, ? ,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }

    @Override
    protected GamePrivacy rsToObject(ResultSet rs) throws SQLException {
        GamePrivacy gamePrivcacy = new GamePrivacy();
        gamePrivcacy.setResourceId(rs.getLong("RESOURCEID"));
        gamePrivcacy.setUno(rs.getString("UNO"));
        gamePrivcacy.setPrivacyLevel(GamePrivacyLevel.getByCode(rs.getString("PRIVCAYLEVEL")));
        gamePrivcacy.setResourceDomain(ResourceDomain.getByCode(rs.getString("RESOURCEDOMAIN")));
        gamePrivcacy.setCreateDate(new Date(rs.getTimestamp("CREATEDATE").getTime()));
        gamePrivcacy.setCreateUserid(rs.getString("CREATEUSERID"));
        gamePrivcacy.setUpdateDate(new Date(rs.getTimestamp("UPDATEDATE").getTime()));
        gamePrivcacy.setUpdateUserid(rs.getString("UPDATEUSERID"));
        return gamePrivcacy;
    }

    @Override
    public GamePrivacy get(QueryExpress getExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, getExpress, conn);
    }

    @Override
    public List<GamePrivacy> query(QueryExpress queryExpress, Connection conn) throws DbException {
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
