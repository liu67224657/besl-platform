package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.tools.EditorStatsItemType;
import com.enjoyf.platform.service.tools.StatsEditorItem;
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
class AbstractStatsEditorItemAccessor extends AbstractSequenceBaseTableAccessor<StatsEditorItem> implements StatsEditorItemAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractStatsEditorItemAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "TOOLS_STATS_EDITOR_ITEM";
    private static final String KEY_SEQUENCE_NAME = "SEQ_STATS_EDITOR_ITEM_NO";

    @Override
    public StatsEditorItem get(QueryExpress getExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, getExpress, conn);
    }

    @Override
    public StatsEditorItem insert(StatsEditorItem entity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = getInsertSql();

        try {
            //
            entity.setItemNo(getItemNo(conn));

            pstmt = conn.prepareStatement(sql);

            //ITEMNO, ADMINUNO, ITEMTYPE, ITEMSUBTYPE, ITEMSRCNO, VALIDSTATUS, CREATEDATE, CREATEIP
            pstmt.setString(1, entity.getItemNo());

            pstmt.setInt(2, entity.getAdminUno());
            pstmt.setString(3, entity.getItemType() != null ? entity.getItemType().getCode() : null);
            pstmt.setString(4, entity.getItemSubType());

            pstmt.setString(5, entity.getItemSrcNo());
            pstmt.setString(6, entity.getSourceId());

            pstmt.setString(7, entity.getValidStatus().getCode());
            pstmt.setTimestamp(8, new Timestamp(entity.getCreateDate() != null ? entity.getCreateDate().getTime() : System.currentTimeMillis()));
            pstmt.setString(9, entity.getCreateIp());

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
    public List<StatsEditorItem> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<StatsEditorItem> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    private String getItemNo(Connection conn) throws DbException {
        return String.valueOf(getSeqNo(KEY_SEQUENCE_NAME, conn));
    }

    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (ITEMNO, ADMINUNO, ITEMTYPE, ITEMSUBTYPE, ITEMSRCNO, SOURCEID , VALIDSTATUS, CREATEDATE, CREATEIP) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ? , ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    //ITEMNO, ADMINUNO, ITEMTYPE, ITEMSUBTYPE, ITEMSRCNO, VALIDSTATUS, CREATEDATE, CREATEIP
    protected StatsEditorItem rsToObject(ResultSet rs) throws SQLException {
        StatsEditorItem entity = new StatsEditorItem();

        //
        entity.setItemNo(rs.getString("ITEMNO"));

        entity.setAdminUno(rs.getInt("ADMINUNO"));
        entity.setItemType(EditorStatsItemType.getByCode(rs.getString("ITEMTYPE")));
        entity.setItemSubType(rs.getString("ITEMSUBTYPE"));

        entity.setItemSrcNo(rs.getString("ITEMSRCNO"));
        entity.setSourceId(rs.getString("SOURCEID"));

        entity.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));
        entity.setCreateDate(rs.getTimestamp("CREATEDATE"));
        entity.setCreateIp(rs.getString("CREATEIP"));

        //
        return entity;
    }
}
