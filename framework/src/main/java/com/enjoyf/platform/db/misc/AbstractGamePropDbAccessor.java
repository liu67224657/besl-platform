/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;


import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.GamePropDb;
import com.enjoyf.platform.service.misc.GamePropValueType;
import com.enjoyf.platform.service.misc.GamePropDbQueryParam;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractGamePropDbAccessor extends AbstractSequenceBaseTableAccessor<GamePropDb> implements GamePropDbAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractGamePropDbAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "_propdb";
    private static final String KEY_SEQUENCE_NAME = "SEQ_GAMEPROP_DB_ID";


    @Override
    public long getSeqNo(Connection conn) throws DbException {
        return getSeqNo(KEY_SEQUENCE_NAME, conn);
    }

    @Override
    public GamePropDb insert(GamePropDb gamePropDb, String gamePropCode, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(gamePropCode));

            //key_id,key_name,type,string_value,int_value,date_value
            pstmt.setLong(1, gamePropDb.getKey_id());
            pstmt.setString(2, gamePropDb.getKey_name());
            pstmt.setInt(3, gamePropDb.getType());
            pstmt.setString(4, gamePropDb.getString_value());
            if (gamePropDb.getNum_value() == null) {
                pstmt.setNull(5, Types.FLOAT);
            } else {
                pstmt.setFloat(5, gamePropDb.getNum_value());
            }
            pstmt.setTimestamp(6, gamePropDb.getDate_value() == null ? null : new Timestamp(gamePropDb.getDate_value().getTime()));
            pstmt.setInt(7, gamePropDb.getValueType().getCode());
            pstmt.execute();
            return gamePropDb;
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public GamePropDb insert(GamePropDb gamePropDb, Connection conn) throws DbException {
        return gamePropDb;
    }

    private String getInsertSql(String gamePropCode) {
        String sqlScript = "INSERT INTO " + getTableName(gamePropCode) + "(key_id,key_name,type,string_value,num_value,date_value,value_type) VALUES (? ,?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }

    @Override
    //key_id,key_name,type,string_value,int_value,date_value
    protected GamePropDb rsToObject(ResultSet rs) throws SQLException {
        GamePropDb gamePropDb = new GamePropDb();
        gamePropDb.setId(rs.getInt("id"));
        gamePropDb.setKey_id(rs.getInt("key_id"));
        gamePropDb.setKey_name(rs.getString("key_name"));
        gamePropDb.setType(rs.getInt("type"));
        gamePropDb.setString_value(rs.getString("string_value"));
        gamePropDb.setNum_value(rs.getFloat("num_value"));
        gamePropDb.setDate_value(rs.getTimestamp("date_value"));
        gamePropDb.setValueType(GamePropValueType.getByCode(rs.getInt("value_type")));
        return gamePropDb;
    }


    @Override
    public GamePropDb get(QueryExpress queryExpress, String gamePropCode, Connection conn) throws DbException {
        return super.get(getTableName(gamePropCode), queryExpress, conn);
    }


    @Override
    public List<GamePropDb> query(QueryExpress queryExpress, Pagination pagination, String gamePropCode, Connection conn) throws DbException {
        return super.query(getTableName(gamePropCode), queryExpress, pagination, conn);
    }

    @Override
    public List<GamePropDb> query(QueryExpress queryExpress, String gamePropCode, Connection conn) throws DbException {
        return super.query(getTableName(gamePropCode), queryExpress, conn);
    }

    //    @Override
    public List<GamePropDb> getByParamList(List<GamePropDbQueryParam> paramList, String gamePropCode, Connection conn) throws DbException {
        List<GamePropDb> returnValue = new ArrayList<GamePropDb>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = getInnterJoinSql(paramList, gamePropCode);
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            setPstmt(1, pstmt, paramList);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public void insertBatch(String gamePropCode, List<GamePropDb> gamePropDbList, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(gamePropCode));

            for (GamePropDb gamePropDb : gamePropDbList) {
                //key_id,key_name,type,string_value,int_value,date_value,value_type
                pstmt.setLong(1, gamePropDb.getKey_id());
                pstmt.setString(2, gamePropDb.getKey_name());
                pstmt.setInt(3, gamePropDb.getType());
                pstmt.setString(4, gamePropDb.getString_value());
                if (gamePropDb.getNum_value() == null) {
                    pstmt.setNull(5, Types.FLOAT);
                } else {
                    pstmt.setFloat(5, gamePropDb.getNum_value());
                }
                pstmt.setTimestamp(6, gamePropDb.getDate_value() == null ? null : new Timestamp(gamePropDb.getDate_value().getTime()));
                pstmt.setInt(7, gamePropDb.getValueType().getCode());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    protected String getTableName(String gamePropCode) {
        return gamePropCode + KEY_TABLE_NAME;
    }

    protected String getInnterJoinSql(List<GamePropDbQueryParam> paramList, String gamePropCode) {
        StringBuilder stringBuilder = new StringBuilder("SELECT t0.* FROM ").append(getTableName(gamePropCode)).append(" t0 ");
        for (int i = 1; i < paramList.size(); i++) {
            stringBuilder.append("INNER JOIN mt_propdb t").append(i).append(" ON t0.key_id=t").append(i).append(".key_id ");
        }

        stringBuilder.append("WHERE ");
        for (int i = 0; i < paramList.size(); i++) {
            if (i > 0) {
                stringBuilder.append("AND ");
            }

            GamePropDbQueryParam param = paramList.get(i);
            stringBuilder.append("t").append(i).append(".key_name=").append("? ");

            stringBuilder.append("AND t").append(i).append(".").append(getValueByQueryParam(param)).append("=").append("? ");
        }
        return stringBuilder.toString();
    }

    private int setPstmt(int startIndx, PreparedStatement pstmt, List<GamePropDbQueryParam> paramObjectList) throws SQLException {
        for (GamePropDbQueryParam obj : paramObjectList) {
            pstmt.setString(startIndx++, obj.getKey());

            if (obj.getGamePropValueType().equals(GamePropValueType.DATE_VALUE)) {
                pstmt.setTimestamp(startIndx++, new Timestamp(obj.getDateValue().getTime()));
            } else if (obj.getGamePropValueType().equals(GamePropValueType.NUM_VALUE)) {
                pstmt.setFloat(startIndx++, obj.getNumValue());
            } else {
                pstmt.setString(startIndx++, obj.getStringValue());
            }
        }

        return startIndx;
    }

    private String getValueByQueryParam(GamePropDbQueryParam param) {
        if (param.getGamePropValueType().equals(GamePropValueType.DATE_VALUE)) {
            return "date_value";
        } else if (param.getGamePropValueType().equals(GamePropValueType.NUM_VALUE)) {
            return "num_value";
        } else {
            return "string_value";
        }
    }
}
