package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GamePropertyInfo;
import com.enjoyf.platform.service.gameres.GamePropertyKeyNameCode;
import com.enjoyf.platform.service.gameres.GamePropertyValueType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午1:58
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGamePropertyInfoAccessor extends AbstractSequenceBaseTableAccessor<GamePropertyInfo> implements GamePropertyInfoAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGamePropertyInfoAccessor.class);

    private static final String KEY_TABLE_NAME = "game_property";
    private static final String KEY_SEQUENCE_NAME = "SEQ_GAME_PROPERTY_INFO_ID";

    @Override
    public GamePropertyInfo insert(GamePropertyInfo gamePropertyInfo, Connection conn) throws DbException {
        return null;
    }

    @Override
    public List<GamePropertyInfo> batchInsert(List<GamePropertyInfo> infoList, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<GamePropertyInfo> returnList = new LinkedList<GamePropertyInfo>();
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            Long seqId = getSeqNo(KEY_SEQUENCE_NAME, conn);
            for (GamePropertyInfo info : infoList) {
                pstmt.setLong(1, seqId);

                pstmt.setString(2, info.getKeyName());
                pstmt.setString(3, info.getKeyNameCode().getValue());
                if (GamePropertyValueType.DEFAULT.equals(info.getGamePropertyValueType())) {
                    pstmt.setNull(4, Types.SMALLINT);
                    pstmt.setNull(5, Types.BIGINT);
                    pstmt.setNull(6, Types.VARCHAR);
                    pstmt.setNull(7, Types.TIMESTAMP);
                    pstmt.setNull(8, Types.TINYINT);
                    pstmt.setInt(9, info.getGamePropertyValueType().getValue());
                } else if (GamePropertyValueType.INTEGER.equals(info.getGamePropertyValueType())) {
                    pstmt.setInt(4, info.getIntValue());
                    pstmt.setNull(5, Types.BIGINT);
                    pstmt.setNull(6, Types.VARCHAR);
                    pstmt.setNull(7, Types.TIMESTAMP);
                    pstmt.setNull(8, Types.TINYINT);
                    pstmt.setInt(9, info.getGamePropertyValueType().getValue());
                } else if (GamePropertyValueType.STRING.equals(info.getGamePropertyValueType())) {
                    pstmt.setNull(4, Types.SMALLINT);
                    pstmt.setNull(5, Types.BIGINT);
                    pstmt.setString(6, info.getStringValue());
                    pstmt.setNull(7, Types.TIMESTAMP);
                    pstmt.setNull(8, Types.TINYINT);
                    pstmt.setInt(9, info.getGamePropertyValueType().getValue());
                } else if (GamePropertyValueType.DATE.equals(info.getGamePropertyValueType())) {
                    pstmt.setNull(4, Types.SMALLINT);
                    pstmt.setNull(5, Types.BIGINT);
                    pstmt.setNull(6, Types.VARCHAR);
                    pstmt.setTimestamp(7, new Timestamp(info.getDateValue() == null ? null : info.getDateValue().getTime()));
                    pstmt.setNull(8, Types.TINYINT);
                    pstmt.setInt(9, info.getGamePropertyValueType().getValue());
                } else if (GamePropertyValueType.BOOLEAN.equals(info.getGamePropertyValueType())) {
                    pstmt.setNull(4, Types.SMALLINT);
                    pstmt.setNull(5, Types.BIGINT);
                    pstmt.setNull(6, Types.VARCHAR);
                    pstmt.setNull(7, Types.TIMESTAMP);
                    pstmt.setBoolean(8, info.getBooleanValue());
                    pstmt.setInt(9, info.getGamePropertyValueType().getValue());
                }
                pstmt.setTimestamp(10, new Timestamp(info.getCreateDate() == null ? System.currentTimeMillis() : info.getCreateDate().getTime()));
                pstmt.setString(11, info.getCreateId());
                pstmt.setString(12, info.getCreateIp());

                pstmt.executeUpdate();
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    info.setPrimaryKeyId(rs.getLong(1));
                }
                returnList.add(info);
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert GamePropertyInfo,SQLException:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnList;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<GamePropertyInfo> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GamePropertyInfo> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected GamePropertyInfo rsToObject(ResultSet rs) throws SQLException {
        GamePropertyInfo info = new GamePropertyInfo();
        info.setPrimaryKeyId(rs.getLong("primary_id"));
        info.setKeyId(rs.getLong("key_id"));
        info.setKeyNameCode(GamePropertyKeyNameCode.getByValue(rs.getString("code")));
        info.setKeyName(rs.getString("key_name"));
        info.setIntValue(rs.getInt("int_value"));
        info.setLongValue(rs.getLong("long_value"));
        info.setStringValue(rs.getString("string_value"));
        info.setBooleanValue(rs.getBoolean("boolean_value"));
        info.setDateValue(rs.getTimestamp("date_value"));
        info.setGamePropertyValueType(GamePropertyValueType.getByValue(rs.getInt("value_type")));
        info.setCreateDate(rs.getTimestamp("create_date"));
        info.setCreateId(rs.getString("create_id"));
        info.setCreateIp(rs.getString("create_ip"));
        info.setLastModifyDate(rs.getTimestamp("last_modify_date"));
        info.setLastModifyId(rs.getString("last_modify_id"));
        info.setLastModifyIp(rs.getString("last_modify_ip"));
        return info;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(key_id,code,key_name,int_value,long_value,string_value,date_value,boolean_value,value_type,create_date,create_id,create_ip) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert GamePropertyInfo sql:" + insertSql);
        }
        return insertSql;
    }

    @Override
    public long getSeqNo(String sequenceName, Connection conn) throws DbException {
        return super.getSeqNo(KEY_SEQUENCE_NAME, conn);
    }

}
