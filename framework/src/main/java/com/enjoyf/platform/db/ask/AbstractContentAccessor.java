package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.wiki.Content;
import com.enjoyf.platform.service.ask.wiki.ContentSource;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractContentAccessor extends AbstractBaseTableAccessor<Content> implements ContentAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractContentAccessor.class);

    private static final String KEY_TABLE_NAME = "content";

    @Override
    public Content insert(Content content, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            //comment_id,title,DESCRIBE,pic,author,game_id,release_time,web_url,source,remove_status,display_order,create_date
            pstmt.setString(1, content.getCommentId());
            pstmt.setString(2, content.getTitle());
            pstmt.setString(3, content.getDescription());
            pstmt.setString(4, content.getPic());
            pstmt.setString(5, content.getAuthor());
            pstmt.setString(6, content.getGameId());
            pstmt.setTimestamp(7, new Timestamp(content.getPublishTime().getTime()));
            pstmt.setString(8, content.getWebUrl());
            pstmt.setInt(9, content.getSource().getCode());
            pstmt.setString(10, content.getRemoveStatus().getCode());
            pstmt.setTimestamp(11, new Timestamp(content.getCreateDate().getTime()));

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                content.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return content;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(comment_id,title,description,pic,author,game_id,publish_time,web_url," +
                "source,remove_status,create_date) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("Content insert sql" + sql);
        }
        return sql;
    }

    @Override
    public Content get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<Content> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Content> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected Content rsToObject(ResultSet rs) throws SQLException {

        Content returnObject = new Content();

        returnObject.setId(rs.getLong("id"));
        returnObject.setCommentId(rs.getString("comment_id"));
        returnObject.setTitle(rs.getString("title"));
        returnObject.setDescription(rs.getString("description"));
        returnObject.setPic(rs.getString("pic"));
        returnObject.setAuthor(rs.getString("author"));
        returnObject.setGameId(rs.getString("game_id"));
        returnObject.setPublishTime(new Date(rs.getTimestamp("publish_time").getTime()));
        returnObject.setWebUrl(rs.getString("web_url"));
        returnObject.setSource(ContentSource.getByCode(rs.getInt("source")));
        returnObject.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        returnObject.setCreateDate(new Date(rs.getTimestamp("create_date").getTime()));


        return returnObject;
    }
}