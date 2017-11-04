package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayInfo;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.google.gdata.util.common.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public abstract class AbstractViewLineItemAccessor extends AbstractSequenceBaseTableAccessor<ViewLineItem> implements ViewLineItemAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractViewLineItemAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_VIEWLINE_LINEITEM_ID";
    protected static final String KEY_TABLE_NAME = "VIEWLINE_LINEITEM";
    protected static final String KEY_TABLE_VIEWLINE_NAME = "VIEWLINE_LINE";

    @Override
    public ViewLineItem insert(ViewLineItem entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setItemId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());

            //ITEMID, LINEID, CATEGORYID, CATEGORYASPECT, DISPLAYINFO, DIRECTUNO, DIRECTID, PARENTUNO, PARENTID, RELATIONUNO, RELATIONID, ITEMCREATEDATE,
            //DISPLAYORDER, DISPLAYTYPE, VALIDSTATUS, ITEMDESC, CREATEDATE, CREATEUNO
            pstmt.setLong(1, entry.getItemId());
            pstmt.setInt(2, entry.getLineId());
            pstmt.setInt(3, entry.getCategoryId());
            pstmt.setString(4, entry.getCategoryAspect() == null ? null : entry.getCategoryAspect().getCode());
            pstmt.setString(5, entry.getDisplayInfo() == null ? null : entry.getDisplayInfo().toJson());

            pstmt.setString(6, StringUtil.isEmpty(entry.getDirectUno()) ? entry.getItemId() + "" : entry.getDirectUno());
            pstmt.setString(7, StringUtil.isEmpty(entry.getDirectId()) ? entry.getItemId() + "" : entry.getDirectId());

            pstmt.setString(8, entry.getParentUno());
            pstmt.setString(9, entry.getParentId());

            pstmt.setString(10, entry.getRelationUno());
            pstmt.setString(11, entry.getRelationId());
            pstmt.setTimestamp(12, entry.getItemCreateDate() != null ? new Timestamp(entry.getItemCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));

            pstmt.setInt(13, entry.getDisplayOrder());
            pstmt.setInt(14, entry.getDisplayType().getValue());

            pstmt.setString(15, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);

            pstmt.setString(16, entry.getItemDesc());

            pstmt.setTimestamp(17, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));
            pstmt.setString(18, entry.getCreateUno());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public List<Integer> queryCategoryIds(QueryExpress queryExpress, Connection conn) throws DbException {
        List<Integer> returnValue = new ArrayList<Integer>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT CATEGORYID FROM " + KEY_TABLE_VIEWLINE_NAME + " WHERE LINEID IN " + "(SELECT LINEID FROM " + KEY_TABLE_NAME + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true) + ")";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            //
            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rs.getInt("CATEGORYID"));
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
    public List<ViewLineItem> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public ViewLineItem get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected ViewLineItem rsToObject(ResultSet rs) throws SQLException {
        ViewLineItem entry = new ViewLineItem();

        //ITEMID, LINEID, CATEGORYID, CATEGORYASPECT, DISPLAYINFO, DIRECTUNO, DIRECTID, PARENTUNO, PARENTID, RELATIONUNO, RELATIONID, ITEMCREATEDATE
        //DISPLAYORDER,DISPLAYTYPE, VALIDSTATUS, ITEMDESC, CREATEDATE, CREATEUNO
        entry.setItemId(rs.getLong("ITEMID"));

        entry.setLineId(rs.getInt("LINEID"));
        entry.setCategoryId(rs.getInt("CATEGORYID"));
        entry.setCategoryAspect(ViewCategoryAspect.getByCode(rs.getString("CATEGORYASPECT")));
        entry.setDisplayInfo(ViewLineItemDisplayInfo.parse(rs.getString("DISPLAYINFO")));

        entry.setDirectUno(rs.getString("DIRECTUNO"));
        entry.setDirectId(rs.getString("DIRECTID"));
        entry.setParentUno(rs.getString("PARENTUNO"));
        entry.setParentId(rs.getString("PARENTID"));
        entry.setRelationUno(rs.getString("RELATIONUNO"));
        entry.setRelationId(rs.getString("RELATIONID"));

        entry.setItemCreateDate(rs.getTimestamp("ITEMCREATEDATE") != null ? new Date(rs.getTimestamp("ITEMCREATEDATE").getTime()) : null);

        entry.setDisplayOrder(rs.getInt("DISPLAYORDER"));
        entry.setDisplayType(ViewLineItemDisplayType.getByValue(rs.getInt("DISPLAYTYPE")));

        entry.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));

        entry.setItemDesc(rs.getString("ITEMDESC"));

        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        entry.setCreateUno(rs.getString("CREATEUNO"));

        return entry;
    }

    @Override
    public List<ViewLineItem> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<ViewLineItem> query(QueryExpress queryExpress, Rangination range, Connection conn) throws DbException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        //ITEMID, LINEID, DISPLAYINFO, DIRECTUNO, DIRECTID, PARENTUNO, PARENTID, RELATIONUNO, RELATIONID, ITEMCREATEDATE
        //DISPLAYORDER,DISPLAYTYPE, VALIDSTATUS, ITEMDESC, CREATEDATE, CREATEUNO
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (ITEMID, LINEID, CATEGORYID, CATEGORYASPECT, DISPLAYINFO, DIRECTUNO, DIRECTID," +
                " PARENTUNO, PARENTID, RELATIONUNO, RELATIONID, ITEMCREATEDATE, DISPLAYORDER, DISPLAYTYPE, VALIDSTATUS, " +
                "ITEMDESC, CREATEDATE, CREATEUNO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ViewLineItem INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
