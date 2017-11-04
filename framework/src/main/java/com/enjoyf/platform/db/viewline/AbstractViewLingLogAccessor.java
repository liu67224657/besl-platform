package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewLineLog;
import com.enjoyf.platform.service.viewline.ViewLineLogDomain;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public abstract class AbstractViewLingLogAccessor extends AbstractSequenceBaseTableAccessor<ViewLineLog> implements ViewLineLogAccessor {

    private static Logger logger = LoggerFactory.getLogger(AbstractViewLingLogAccessor.class);

    private static final String TABLE_NAME = "VIEWLINE_LOG";
    private static final String SEQUENCE_NAME_LOG = "SEQ_VIEWLINE_LOG_ID";

    @Override
    public ViewLineLog insert(ViewLineLog viewLineLog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //LOGID,SRCID,SUBDOMAIN,LOGNAME,LOGCONTENT,CREATEDATE,CREATEUNO,CREATEIP,CREATEUSERID
            viewLineLog.setLogId(getSeqNo(SEQUENCE_NAME_LOG, conn));
            pstmt.setLong(1, viewLineLog.getLogId());
            pstmt.setInt(2, viewLineLog.getSrcId());
            pstmt.setString(3, viewLineLog.getLogDomain().getCode());
            pstmt.setString(4, viewLineLog.getLogName());
            pstmt.setString(5, viewLineLog.getLogContent());
            pstmt.setTimestamp(6, new Timestamp(viewLineLog.getCreateDate() == null ? System.currentTimeMillis() : viewLineLog.getCreateDate().getTime()));
            pstmt.setString(7, StringUtil.isEmpty(viewLineLog.getCreateUno()) ? null : viewLineLog.getCreateUno());
            pstmt.setString(8, StringUtil.isEmpty(viewLineLog.getCreateIp()) ? null : viewLineLog.getCreateIp());
            pstmt.setString(9, StringUtil.isEmpty(viewLineLog.getCreateUserId()) ? null : viewLineLog.getCreateUserId());

            pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + " On insert ViewLineLog, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return viewLineLog;
    }

    @Override
    public ViewLineLog get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ViewLineLog> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ViewLineLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress,pagination, conn);
    }

    @Override
    protected ViewLineLog rsToObject(ResultSet rs) throws SQLException {
        ViewLineLog reutrnObj = new ViewLineLog();

        //LOGID,SRCID,SUBDOMAIN,LOGNAME,LOGTYPE,LOGCONTENT,CREATEDATE,CREATEUNO,CREATEIP,CREATEUSERID
        reutrnObj.setLogId(rs.getLong("LOGID"));
        reutrnObj.setSrcId(rs.getInt("SRCID"));
        reutrnObj.setLogDomain(ViewLineLogDomain.getByCode(rs.getString("SUBDOMAIN")));
        reutrnObj.setLogName(rs.getString("LOGNAME"));
        reutrnObj.setLogContent(rs.getString("LOGCONTENT"));
        reutrnObj.setCreateDate(new Date(rs.getTimestamp("CREATEDATE").getTime()));
        reutrnObj.setCreateIp(rs.getString("CREATEIP"));
        reutrnObj.setCreateUno(rs.getString("CREATEUNO"));
        reutrnObj.setCreateUserId(rs.getString("CREATEUSERID"));

        return reutrnObj;
    }


    private static String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + "(LOGID, SRCID, SUBDOMAIN, LOGNAME, LOGCONTENT, CREATEDATE, CREATEUNO, CREATEIP, CREATEUSERID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sql);
        }

        return sql;
    }
}
