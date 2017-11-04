package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.tools.StatsEditor;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * the abstract class of stats editor accessor.
 */
class AbstractStatsEditorAccessor extends AbstractBaseTableAccessor<StatsEditor> implements StatsEditorAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractStatsEditorAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "TOOLS_STATS_EDITOR";

    @Override
    public StatsEditor get(QueryExpress getExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, getExpress, conn);
    }

    @Override
    public StatsEditor insert(StatsEditor entity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = getInsertSql();

        try {
            pstmt = conn.prepareStatement(sql);

            //ADMINUNO, EDITORNAME, EDITORDESC, VALIDSTATUS, CREATEDATE, CREATEIP
            pstmt.setInt(1, entity.getAdminUno());
            pstmt.setString(2, entity.getEditorName());
            pstmt.setString(3, entity.getEditorDesc());

            pstmt.setString(4, entity.getValidStatus().getCode());
            pstmt.setTimestamp(5, new Timestamp(entity.getCreateDate() != null ? entity.getCreateDate().getTime() : System.currentTimeMillis()));
            pstmt.setString(6, entity.getCreateIp());

            pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return entity;
    }


    @Override
    public List<StatsEditor> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<StatsEditor> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
         return query(KEY_TABLE_NAME, queryExpress,pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (ADMINUNO, EDITORNAME, EDITORDESC, VALIDSTATUS, CREATEDATE, CREATEIP) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    //ADMINUNO, EDITORNAME, EDITORDESC, VALIDSTATUS, CREATEDATE, CREATEIP
    protected StatsEditor rsToObject(ResultSet rs) throws SQLException {
        StatsEditor entity = new StatsEditor();

        //
        entity.setAdminUno(rs.getInt("ADMINUNO"));
        entity.setEditorName(rs.getString("EDITORNAME"));
        entity.setEditorDesc(rs.getString("EDITORDESC"));
        entity.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));
        entity.setCreateDate(rs.getTimestamp("CREATEDATE"));
        entity.setCreateIp(rs.getString("CREATEIP"));

        //
        return entity;
    }
}
