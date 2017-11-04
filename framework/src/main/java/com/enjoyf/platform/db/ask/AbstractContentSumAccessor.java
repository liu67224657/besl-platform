package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ask.wiki.ContentSum;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractContentSumAccessor extends AbstractBaseTableAccessor<ContentSum> implements ContentSumAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractContentSumAccessor.class);

    private static final String KEY_TABLE_NAME = "content_sum";

    @Override
    public ContentSum insert(ContentSum contentSum, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setInt(1, contentSum.getAgree_num());
            pstmt.setTimestamp(2, new Timestamp(contentSum.getCreateDate().getTime()));
            pstmt.setLong(3,contentSum.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return contentSum;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(agree_num,create_date,id) VALUES (?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("ContentSum insert sql" + sql);
        }
        return sql;
    }

    @Override
    public ContentSum get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ContentSum> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ContentSum> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected ContentSum rsToObject(ResultSet rs) throws SQLException {
        ContentSum returnObject = new ContentSum();
        returnObject.setId(rs.getLong("id"));
        returnObject.setAgree_num(rs.getInt("agree_num"));
        returnObject.setCreateDate(new Date(rs.getTimestamp("create_date").getTime()));
        return returnObject;
    }
}