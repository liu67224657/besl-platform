package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.AskReport;
import com.enjoyf.platform.service.ask.AskReportType;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.ItemType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractAskReportAccessor extends AbstractBaseTableAccessor<AskReport> implements AskReportAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAskReportAccessor.class);

    private static final String KEY_TABLE_NAME = "ask_report";

    @Override
    public AskReport insert(AskReport askReport, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, askReport.getReportId());
            pstmt.setInt(2, askReport.getItemType().getCode());
            pstmt.setLong(3, askReport.getDestId());
            pstmt.setString(4, askReport.getDestProfileId());
            pstmt.setInt(5, askReport.getAskReportType().getCode());
            pstmt.setTimestamp(6, new Timestamp(askReport.getCreateTime().getTime()));
            pstmt.setString(7, askReport.getValidStatus().getCode());
            pstmt.setString(8, askReport.getExtstr());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return askReport;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(report_id,item_type,dest_id,dest_profileid,ask_report_type,create_time,valid_status,extstr) VALUES (?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AskReport insert sql" + sql);
        }
        return sql;
    }

    @Override
    public AskReport get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<AskReport> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AskReport> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected AskReport rsToObject(ResultSet rs) throws SQLException {

        AskReport returnObject = new AskReport();

        returnObject.setReportId(rs.getString("report_id"));
        returnObject.setItemType(ItemType.getByCode(rs.getInt("item_type")));
        returnObject.setDestId(rs.getLong("dest_id"));
        returnObject.setDestProfileId(rs.getString("dest_profileid"));
        returnObject.setAskReportType(AskReportType.getByCode(rs.getInt("ask_report_type")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        returnObject.setExtstr(rs.getString("extstr"));


        return returnObject;
    }
}