package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryAspect;
import com.enjoyf.platform.service.viewline.ViewCategoryDisplaySetting;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public abstract class AbstractViewCategoryAccessor extends AbstractSequenceBaseTableAccessor<ViewCategory> implements ViewCategoryAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractViewCategoryAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "VIEWLINE_CATEGORY";
    protected static final String KEY_SEQUENCE_NAME = "SEQ_VIEWLINE_CATEGORY_ID";

    @Override
    public ViewCategory insert(ViewCategory entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            entry.setCategoryId((int) getSeqNo(KEY_SEQUENCE_NAME, conn));

            if (Strings.isNullOrEmpty(entry.getCategoryCode())) {
                entry.setCategoryCode(String.valueOf(entry.getCategoryId()));
            }

            // CATEGORYID, CATEGORYDOMAIN, CATEGORYASPECT, CATEGORYCODE, LOCATIONCODE, CATEGORYNAME, CATEGORYDESC, SEOKEYWORD, SEODESC,
            // PARENTCATEGORYID, DISPLAYSETTING, DISPLAYORDER, VALIDSTATUS, PUBLISHSTATUS,
            // CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID

            pstmt.setInt(1, entry.getCategoryId());

            pstmt.setString(2, entry.getCategoryAspect().getItemType().getCode());
            pstmt.setString(3, entry.getCategoryAspect().getCode());

            pstmt.setString(4, !StringUtil.isEmpty(entry.getCategoryCode())?entry.getCategoryCode():String.valueOf(entry.getCategoryId()));
            pstmt.setString(5, entry.getLocationCode());

            pstmt.setString(6, entry.getCategoryName());
            pstmt.setString(7, entry.getCategoryDesc());

            pstmt.setString(8, entry.getSeoKeyWord());
            pstmt.setString(9, entry.getSeoDesc());

            pstmt.setInt(10, entry.getParentCategoryId());

            pstmt.setString(11, entry.getDisplaySetting() != null ? entry.getDisplaySetting().toJson() : null);
            pstmt.setInt(12, entry.getDisplayOrder());

            pstmt.setString(13, entry.getValidStatus() != null ? entry.getValidStatus().getCode() : null);
            pstmt.setString(14, entry.getPublishStatus() != null ? entry.getPublishStatus().getCode() : null);

            pstmt.setTimestamp(15, entry.getCreateDate() != null ? new Timestamp(entry.getCreateDate().getTime()) : null);
            pstmt.setString(16, entry.getCreateUserid());

            pstmt.setTimestamp(17, entry.getUpdateDate() != null ? new Timestamp(entry.getUpdateDate().getTime()) : null);
            pstmt.setString(18, entry.getUpdateUserid());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ViewCategory, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public ViewCategory get(QueryExpress getExpress, Connection conn) throws DbException {
        //
        return get(KEY_TABLE_NAME, getExpress, conn);
    }

    @Override
    protected ViewCategory rsToObject(ResultSet rs) throws SQLException {
        ViewCategory entry = new ViewCategory();

        // CATEGORYID, CATEGORYDOMAIN, CATEGORYASPECT, CATEGORYCODE, LOCATIONCODE, CATEGORYNAME, CATEGORYDESC, SEOKEYWORD, SEODESC,
        // PARENTCATEGORYID, DISPLAYSETTING, DISPLAYORDER, VALIDSTATUS, PUBLISHSTATUS,
        // CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID
        entry.setCategoryId(rs.getInt("CATEGORYID"));

        entry.setCategoryAspect(ViewCategoryAspect.getByCode(rs.getString("CATEGORYASPECT")));
        entry.setCategoryCode(rs.getString("CATEGORYCODE"));
        entry.setLocationCode(rs.getString("LOCATIONCODE"));

        entry.setCategoryName(rs.getString("CATEGORYNAME"));
        entry.setCategoryDesc(rs.getString("CATEGORYDESC"));

        entry.setSeoKeyWord(rs.getString("SEOKEYWORD"));
        entry.setSeoDesc(rs.getString("SEODESC"));

        entry.setParentCategoryId(rs.getInt("PARENTCATEGORYID"));

        entry.setDisplaySetting(ViewCategoryDisplaySetting.parse(rs.getString("DISPLAYSETTING")));
        entry.setDisplayOrder(rs.getInt("DISPLAYORDER"));

        entry.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));
        entry.setPublishStatus(ActStatus.getByCode(rs.getString("PUBLISHSTATUS")));

        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);
        entry.setCreateUserid(rs.getString("CREATEUSERID"));

        entry.setUpdateDate(rs.getTimestamp("UPDATEDATE") != null ? new Date(rs.getTimestamp("UPDATEDATE").getTime()) : null);
        entry.setUpdateUserid(rs.getString("UPDATEUSERID"));

        return entry;
    }

    @Override
    public List<ViewCategory> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ViewCategory> query (QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (CATEGORYID, CATEGORYDOMAIN, CATEGORYASPECT, CATEGORYCODE, LOCATIONCODE, CATEGORYNAME, CATEGORYDESC, SEOKEYWORD, SEODESC, " +
                "PARENTCATEGORYID, DISPLAYSETTING, DISPLAYORDER, VALIDSTATUS, PUBLISHSTATUS, " +
                "CREATEDATE, CREATEUSERID, UPDATEDATE, UPDATEUSERID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ViewCategory INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
