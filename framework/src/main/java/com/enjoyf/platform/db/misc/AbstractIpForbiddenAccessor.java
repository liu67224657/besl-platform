package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.misc.IpForbidden;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-10
 * Time: 下午4:31
 * To change this template use File | Settings | File Templates.
 */
class AbstractIpForbiddenAccessor extends AbstractSequenceBaseTableAccessor<IpForbidden> implements IpForbiddenAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractIpForbiddenAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_IP_FORBIDDEN_ID";
    protected static final String KEY_TABLE_NAME = "IP_FORBIDDEN";


    @Override
    public IpForbidden insert(IpForbidden entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try{

            entry.setIpId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());

            //DESC是关键字
            //IPID, STARTIP, ENDIP, CREATEDATE, CREATEUSERID, UTILLDATE, DESCRIPTION, VALIDSTATUS
            pstmt.setLong(1, entry.getIpId());
            pstmt.setLong(2, entry.getStartIP());
            pstmt.setLong(3, entry.getEndIp());
            pstmt.setTimestamp(4, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : null);
            pstmt.setString(5, entry.getCreateUserid());
            pstmt.setTimestamp(6, entry.getUtillDate() != null ? new Timestamp(entry.getUtillDate().getTime()) : null);
            pstmt.setString(7, entry.getDescription());
            pstmt.setString(8, entry.getStatus() != null ? entry.getStatus().getCode() : ValidStatus.VALID.getCode());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            GAlerter.lab("On insert IpForbidden, a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public IpForbidden get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<IpForbidden> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<IpForbidden> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected IpForbidden rsToObject(ResultSet rs) throws SQLException {
        IpForbidden ipForbidden = new IpForbidden();

        //IPID, STARTIP, ENDIP, CREATEDATE, CREATEUSERID, UTILLDATE, DESCRIPTION, VALIDSTATUS
        if(rs != null){
            ipForbidden.setIpId(rs.getLong("IPID"));
            ipForbidden.setStartIP(rs.getLong("STARTIP"));
            ipForbidden.setEndIp(rs.getLong("ENDIP"));
            ipForbidden.setCreateDate(rs.getTimestamp("CREATEDATE"));
            ipForbidden.setCreateUserid(rs.getString("CREATEUSERID"));
            ipForbidden.setUtillDate(rs.getTimestamp("UTILLDATE"));
            ipForbidden.setDescription(rs.getString("DESCRIPTION"));
            ipForbidden.setStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));
        }
        return ipForbidden;
    }

    private String getInsertSql() throws DbException{
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (IPID, STARTIP, ENDIP, CREATEDATE, CREATEUSERID, UTILLDATE, DESCRIPTION, VALIDSTATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("IP_FORBIDDEN INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
