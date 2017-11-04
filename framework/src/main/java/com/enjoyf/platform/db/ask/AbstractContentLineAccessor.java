package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.wiki.ContentLine;
import com.enjoyf.platform.service.ask.wiki.ContentLineOwn;
import com.enjoyf.platform.service.ask.wiki.ContentLineType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractContentLineAccessor extends AbstractBaseTableAccessor<ContentLine> implements ContentLineAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractContentLineAccessor.class);

    private static final String KEY_TABLE_NAME = "content_line";

    @Override
    public ContentLine insert(ContentLine contentLine, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, contentLine.getLinekey());
            pstmt.setInt(2, contentLine.getOwnId().getCode());
            pstmt.setInt(3, contentLine.getLineType().getCode());
            pstmt.setLong(4, contentLine.getDestId());
            pstmt.setDouble(5, contentLine.getScore());
            pstmt.setTimestamp(6, new Timestamp(contentLine.getCreateTime().getTime()));
            pstmt.setString(7, contentLine.getValidStatus().getCode());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                contentLine.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return contentLine;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(line_key,own_id,line_type,dest_id,score,create_time,valid_status) VALUES (?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("ContentLine insert sql" + sql);
        }
        return sql;
    }

    @Override
    public ContentLine get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ContentLine> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ContentLine> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected ContentLine rsToObject(ResultSet rs) throws SQLException {

        ContentLine returnObject = new ContentLine();

        returnObject.setId(rs.getLong("id"));
        returnObject.setLinekey(rs.getString("line_key"));
        returnObject.setOwnId(ContentLineOwn.getByCode(rs.getInt("own_id")));
        returnObject.setLineType(ContentLineType.getByCode(rs.getInt("line_type")));
        returnObject.setDestId(rs.getLong("dest_id"));
        returnObject.setScore(rs.getDouble("score"));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));


        return returnObject;
    }
}