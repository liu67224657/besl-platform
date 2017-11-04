package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.WikiGameres;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractWikiGameresAccessor extends AbstractBaseTableAccessor<WikiGameres> implements WikiGameresAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWikiGameresAccessor.class);

    private static final String KEY_TABLE_NAME = "joymewiki_gameres";

    @Override
    public WikiGameres insert(WikiGameres wikiGameres, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, wikiGameres.getGameId());
            pstmt.setString(2, wikiGameres.getGameName());
            pstmt.setString(3, wikiGameres.getHeadPic());
            pstmt.setString(4, wikiGameres.getValidStatus().getCode());
            pstmt.setInt(5, wikiGameres.getRecommend());
            pstmt.setInt(6, wikiGameres.getDisplayOrder());
            pstmt.setTimestamp(7, new Timestamp(wikiGameres.getCreateTime().getTime()));
            pstmt.setTimestamp(8, new Timestamp(wikiGameres.getUpdateTime().getTime()));
            pstmt.setString(9, wikiGameres.getUpdateUser());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                wikiGameres.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return wikiGameres;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(game_id,game_name,head_pic,valid_status,recommend,display_order,create_time,update_time,update_user) VALUES (?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("WikiGameres insert sql" + sql);
        }
        return sql;
    }

    @Override
    public WikiGameres get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<WikiGameres> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<WikiGameres> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected WikiGameres rsToObject(ResultSet rs) throws SQLException {

        WikiGameres returnObject = new WikiGameres();

        returnObject.setId(rs.getLong("id"));
        returnObject.setGameId(rs.getLong("game_id"));
        returnObject.setGameName(rs.getString("game_name"));
        returnObject.setHeadPic(rs.getString("head_pic"));
        returnObject.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        returnObject.setRecommend(rs.getInt("recommend"));
        returnObject.setDisplayOrder(rs.getInt("display_order"));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setUpdateTime(new Date(rs.getTimestamp("update_time").getTime()));
        returnObject.setUpdateUser(rs.getString("update_user"));


        return returnObject;
    }
}