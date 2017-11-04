package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewItemType;
import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.service.viewline.ViewLineAutoFillType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.StringUtil;
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
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public abstract class AbstractViewLineAccessor extends AbstractSequenceBaseTableAccessor<ViewLine> implements ViewLineAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractViewLineAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "VIEWLINE_LINE";
    protected static final String KEY_SEQUENCE_NAME = "SEQ_VIEWLINE_LINE_ID";

    @Override
    public ViewLine insert(ViewLine entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            entry.setLineId((int) getSeqNo(KEY_SEQUENCE_NAME, conn));

            // LINEID, LINENAME, LINEDESC, CATEGORYID, CATEGORYASPECT, LOCATIONCODE, ITEMTYPE, ITEMMINCOUNT, DISPLAYORDER,
            // AUTOFILLTYPE, AUTOFILLRULE, CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID, VALIDSTATUS
            pstmt.setInt(1, entry.getLineId());

            pstmt.setString(2, entry.getLineName());
            pstmt.setString(3, entry.getLineDesc());

            pstmt.setInt(4, entry.getCategoryId());
            pstmt.setString(5, entry.getCategoryAspect() != null ? entry.getCategoryAspect().getCode() : null);

            pstmt.setString(6, StringUtil.isEmpty(entry.getLocationCode())?String.valueOf(entry.getLineId()):entry.getLocationCode());
            pstmt.setString(7, entry.getItemType().getCode());
            pstmt.setInt(8, entry.getItemMinCount());

            pstmt.setInt(9, entry.getDisplayOrder());

            pstmt.setString(10, entry.getAutoFillType() != null ? entry.getAutoFillType().getCode() : null);
            pstmt.setString(11, entry.getAutoFillRule() != null ? entry.getAutoFillRule().toJson() : null);

            pstmt.setTimestamp(12, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(13, entry.getCreateUserid());

            pstmt.setTimestamp(14, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : null);
            pstmt.setString(15, entry.getUpdateUserid());

            pstmt.setString(16, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ViewLine, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public ViewLine get(QueryExpress getExpress, Connection conn) throws DbException {
        //call the parent get method to get example entry.
        return get(KEY_TABLE_NAME, getExpress, conn);
    }

    @Override
    protected ViewLine rsToObject(ResultSet rs) throws SQLException {
        ViewLine entry = new ViewLine();

        // LINEID, LINENAME, LINEDESC, CATEGORYID, CATEGORYASPECT, LOCATIONCODE, ITEMTYPE, ITEMMINCOUNT, DISPLAYORDER,
        // AUTOFILLTYPE, AUTOFILLRULE, CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID, VALIDSTATUS

        entry.setLineId(rs.getInt("LINEID"));

        //
        entry.setLineName(rs.getString("LINENAME"));
        entry.setLineDesc(rs.getString("LINEDESC"));

        //
        entry.setCategoryId(rs.getInt("CATEGORYID"));
        entry.setCategoryAspect(ViewCategoryAspect.getByCode(rs.getString("CATEGORYASPECT")));

        //
        entry.setLocationCode(rs.getString("LOCATIONCODE"));
        entry.setItemType(ViewItemType.getByCode(rs.getString("ITEMTYPE")));
        entry.setItemMinCount(rs.getInt("ITEMMINCOUNT"));
        entry.setDisplayOrder(rs.getInt("DISPLAYORDER"));

        entry.setAutoFillType(ViewLineAutoFillType.getByCode(rs.getString("AUTOFILLTYPE")));
        entry.setAutoFillRule(entry.getAutoFillType() != null ? entry.getItemType().parse(rs.getString("AUTOFILLRULE")) : null);

        //
        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        entry.setCreateUserid(rs.getString("CREATEUSERID"));

        entry.setUpdateDate(rs.getTimestamp("UPDATEDATE") != null ? new Date(rs.getTimestamp("UPDATEDATE").getTime()) : null);
        entry.setUpdateUserid(rs.getString("UPDATEUSERID"));

        entry.setValidStatus(rs.getString("VALIDSTATUS") != null ? ValidStatus.getByCode(rs.getString("VALIDSTATUS")) : null);

        return entry;
    }

    @Override
    public List<ViewLine> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ViewLine> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress,pagination, conn);
    }

    @Override
    public List<ViewLine> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress,range, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (LINEID, LINENAME, LINEDESC, CATEGORYID, CATEGORYASPECT, LOCATIONCODE, ITEMTYPE, ITEMMINCOUNT, DISPLAYORDER, " +
                "AUTOFILLTYPE, AUTOFILLRULE, CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID, VALIDSTATUS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ViewLine INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
