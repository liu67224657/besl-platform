package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialReportAccessor extends AbstractBaseTableAccessor<SocialReport> implements SocialReportAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractForignContentAccessor.class);

    private static final String TABLE_NAME = "social_report";

    @Override
    public SocialReport insert(SocialReport socialReport, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, socialReport.getContentId());
            pstmt.setString(2, socialReport.getUno());
            pstmt.setString(3, socialReport.getPostUno());
            pstmt.setTimestamp(4, new Timestamp(socialReport.getCreateDate() == null ? System.currentTimeMillis() : socialReport.getCreateDate().getTime()));
            pstmt.setInt(5, socialReport.getReportType().getCode());
            pstmt.setString(6, socialReport.getValidStatus().getCode());
            pstmt.setInt(7, socialReport.getReportReason());
            pstmt.setLong(8, socialReport.getReplyId());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                socialReport.setReportId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return socialReport;
    }

    @Override
    public List<SocialReport> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public List<SocialReport> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public SocialReport get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    private String getInsertSql() {
        String sql = "insert into " + TABLE_NAME + "(content_id,uno,post_uno,create_date,report_type,valid_status,report_reason,reply_id) values(?,?,?,?,?,?,?,?)";


        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }


    @Override
    protected SocialReport rsToObject(ResultSet rs) throws SQLException {
        SocialReport socialReport = new SocialReport();
        socialReport.setReportId(rs.getLong("report_id"));
        socialReport.setContentId(rs.getLong("content_id"));
        socialReport.setUno(rs.getString("uno"));
        socialReport.setPostUno(rs.getString("post_uno"));
        socialReport.setCreateDate(rs.getTimestamp("create_date"));
        socialReport.setReportType(SocialReportType.getByCode(rs.getInt("report_type")));
        socialReport.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        socialReport.setReportReason(rs.getInt("report_reason"));
        socialReport.setReplyId(rs.getInt("reply_id"));

        return socialReport;
    }
}
