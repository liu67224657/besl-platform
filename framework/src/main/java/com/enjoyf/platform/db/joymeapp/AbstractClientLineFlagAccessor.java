package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.ClientLineFlag;
import com.enjoyf.platform.service.joymeapp.ClientLineFlagType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-1-13
 * Time: 下午12:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractClientLineFlagAccessor extends AbstractBaseTableAccessor<ClientLineFlag> implements ClientLineFlagAccessor{

    private static final Logger logger = LoggerFactory.getLogger(AbstractClientLineFlagAccessor.class);

    private static final String KEY_TABLE_NAME = "client_line_flag";

    @Override
    public ClientLineFlag insert(ClientLineFlag flag, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, flag.getFlagDesc());
            pstmt.setLong(2, flag.getLineId());
            pstmt.setString(3, flag.getLineCode());
            pstmt.setLong(4, flag.getMaxItemId());
            pstmt.setInt(5, flag.getClientLineFlagType().getCode());
            pstmt.setString(6, flag.getValidStatus().getCode());
            pstmt.setTimestamp(7, new Timestamp(flag.getCreateDate()==null? System.currentTimeMillis():flag.getCreateDate().getTime()));
            pstmt.setString(8, flag.getCreateIp());
            pstmt.setString(9, flag.getCreateUserId());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                flag.setFlagId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert ClientLineFlag,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return flag;
    }

    @Override
    public ClientLineFlag get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ClientLineFlag> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ClientLineFlag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected ClientLineFlag rsToObject(ResultSet rs) throws SQLException {
        ClientLineFlag flag = new ClientLineFlag();
        flag.setFlagId(rs.getLong("flag_id"));
        flag.setFlagDesc(rs.getString("flag_desc"));
        flag.setLineId(rs.getLong("line_id"));
        flag.setLineCode(rs.getString("line_code"));
        flag.setMaxItemId(rs.getLong("max_item_id"));
        flag.setClientLineFlagType(ClientLineFlagType.getByCode(rs.getInt("flag_type")));
        flag.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        flag.setCreateDate(rs.getTimestamp("create_date"));
        flag.setCreateIp(rs.getString("create_ip"));
        flag.setCreateUserId(rs.getString("create_userid"));
        flag.setModifyDate(rs.getTimestamp("modify_date"));
        flag.setModifyIp(rs.getString("modify_ip"));
        flag.setModifyUserId(rs.getString("modify_userid"));
        return flag;
    }

     private String getInsertSql() {
         String insertSql = "INSERT INTO "+KEY_TABLE_NAME+"(flag_desc,line_id,line_code,max_item_id,flag_type,valid_status,create_date,create_ip,create_userid) VALUES(?,?,?,?,?,?,?,?,?)";
         if (logger.isDebugEnabled()) {
            logger.debug("insert ClientLineFlag sql:" + insertSql);
        }
        return insertSql;
    }
}
