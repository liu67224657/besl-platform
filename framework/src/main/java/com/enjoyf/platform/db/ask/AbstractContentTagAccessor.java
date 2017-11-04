package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.wiki.ContentTag;
import com.enjoyf.platform.service.ask.wiki.ContentTagLine;
import com.enjoyf.platform.service.ask.wiki.ContentTagType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractContentTagAccessor extends AbstractBaseTableAccessor<ContentTag> implements ContentTagAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractContentTagAccessor.class);

    private static final String KEY_TABLE_NAME = "content_tag";

    @Override
    public ContentTag insert(ContentTag contentTag, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, contentTag.getName());
            pstmt.setString(2, contentTag.getTarget());
            pstmt.setInt(3, contentTag.getTagType().getCode());
            pstmt.setString(4, contentTag.getTagLine().getCode());
            pstmt.setLong(5, contentTag.getDisplayOrder());
            pstmt.setString(6, contentTag.getValidStatus().getCode());
            pstmt.setTimestamp(7, contentTag.getCreateDate() == null ? new Timestamp(new Date().getTime()) : new Timestamp(contentTag.getCreateDate().getTime()));

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                contentTag.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return contentTag;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(name,target,tag_type,tag_line,display_order,valid_status,create_date) VALUES (?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("ContentTag insert sql" + sql);
        }
        return sql;
    }

    @Override
    public ContentTag get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ContentTag> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ContentTag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected ContentTag rsToObject(ResultSet rs) throws SQLException {
        ContentTag returnObject = new ContentTag();
        returnObject.setId(rs.getLong("id"));
        returnObject.setName(rs.getString("name"));
        returnObject.setTarget(rs.getString("target"));
        returnObject.setTagType(ContentTagType.getByCode(rs.getInt("tag_type")));
        returnObject.setTagLine(ContentTagLine.getByCode(rs.getString("tag_line")));
        returnObject.setDisplayOrder(rs.getLong("display_order"));
        returnObject.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        returnObject.setCreateDate(new Date(rs.getTimestamp("create_date").getTime()));
        return returnObject;
    }
}