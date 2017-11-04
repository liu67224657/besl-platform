package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameProperty;
import com.enjoyf.platform.service.gameres.GamePropertyDomain;
import com.enjoyf.platform.service.gameres.GameRelation;
import com.enjoyf.platform.service.gameres.GameRelationType;
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
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
abstract class AbstractGamePropertyAccessor extends AbstractBaseTableAccessor<GameProperty> implements GamePropertyAccessor {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TABLE_NAME = "GAME_RESOURCE_PROPERTY";


    @Override
    public GameProperty insert(GameProperty gameProperty, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //RESOURCEID,PROPERTYDOMAIN,PROPERTYTYPE,VALUE,EXTSTR01,EXTSTR02,SORTNUM
            pstmt.setLong(1, gameProperty.getResourceId());
            pstmt.setString(2, gameProperty.getGamePropertyDomain().getCode());
            pstmt.setString(3, gameProperty.getPropertyType());
            pstmt.setString(4, gameProperty.getValue());
            pstmt.setString(5, gameProperty.getValue2());
            pstmt.setString(6, gameProperty.getValue3());
            pstmt.setInt(7, gameProperty.getSortNum());

            pstmt.execute();
            return gameProperty;

        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public List<GameProperty> insert(List<GameProperty> gamePropertyList, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(getInsertSql());

            for (GameProperty gameProperty : gamePropertyList) {
                pstmt.setLong(1, gameProperty.getResourceId());
                pstmt.setString(2, gameProperty.getGamePropertyDomain().getCode());
                pstmt.setString(3, gameProperty.getPropertyType());
                pstmt.setString(4, gameProperty.getValue());
                pstmt.setString(5, gameProperty.getValue2());
                pstmt.setString(6, gameProperty.getValue3());
                pstmt.setInt(7, gameProperty.getSortNum());

                pstmt.addBatch();
            }
            pstmt.executeBatch();

            return gamePropertyList;

        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public GameProperty get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GameProperty> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() {
        String sqlScript = "INSERT INTO " + TABLE_NAME + "(RESOURCEID,PROPERTYDOMAIN,PROPERTYTYPE,VALUE,EXTSTR01,EXTSTR02,SORTNUM) VALUES (? ,? ,? ,? ,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }

    @Override
    protected GameProperty rsToObject(ResultSet rs) throws SQLException {
        GameProperty returnObj = new GameProperty();
        returnObj.setResourceId(rs.getLong("RESOURCEID"));
        returnObj.setSortNum(rs.getInt("SORTNUM"));
        returnObj.setGamePropertyDomain(GamePropertyDomain.getByCode(rs.getString("PROPERTYDOMAIN")));
        returnObj.setPropertyType(rs.getString("PROPERTYTYPE"));
        returnObj.setValue(rs.getString("VALUE"));
        returnObj.setValue2(rs.getString("EXTSTR01"));
        returnObj.setValue3(rs.getString("EXTSTR02"));

        return returnObj;
    }

}
