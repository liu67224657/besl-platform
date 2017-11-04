package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppInfo;
import com.enjoyf.platform.service.joymeapp.AppInfoType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.MobileGameArticle;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-2
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMobileGameArticleAccessor extends AbstractBaseTableAccessor<MobileGameArticle> implements MobileGameArticleAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMobileGameArticleAccessor.class);


    protected static final String KEY_TABLE_NAME = "mobile_game_article";

    @Override
    public MobileGameArticle insert(MobileGameArticle mobileGameArticle, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, mobileGameArticle.getDesc());
            pstmt.setString(2, mobileGameArticle.getArticleUrl());
            pstmt.setString(3, mobileGameArticle.getAuthorName());
            pstmt.setTimestamp(4, new Timestamp(mobileGameArticle.getCreateTime() == null ? System.currentTimeMillis() : mobileGameArticle.getCreateTime().getTime()));
            pstmt.setString(5, mobileGameArticle.getValidStatus().getCode());
            pstmt.setInt(6, mobileGameArticle.getDisplayOrder());
            pstmt.setLong(7, mobileGameArticle.getGameDbId());
            pstmt.setString(8, mobileGameArticle.getTitle());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                mobileGameArticle.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert MobileGameArticle, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return mobileGameArticle;
    }


    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<MobileGameArticle> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<MobileGameArticle> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }


    @Override
    public MobileGameArticle get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected MobileGameArticle rsToObject(ResultSet rs) throws SQLException {
        MobileGameArticle mobileGameArticle = new MobileGameArticle();
        mobileGameArticle.setId(rs.getLong("id"));
        mobileGameArticle.setTitle(rs.getString("article_title"));
        mobileGameArticle.setDesc(rs.getString("article_desc"));
        mobileGameArticle.setArticleUrl(rs.getString("article_url"));
        mobileGameArticle.setAuthorName(rs.getString("author_name"));
        mobileGameArticle.setCreateTime(rs.getTimestamp("create_time"));
        mobileGameArticle.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        mobileGameArticle.setDisplayOrder(rs.getInt("display_order"));
        mobileGameArticle.setGameDbId(rs.getLong("game_db_id"));

        return mobileGameArticle;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(article_desc,article_url,author_name,create_time,valid_status,display_order,game_db_id,article_title) VALUES(?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("MobileGameArticle INSERT Script:" + insertSql);
        }

        return insertSql;
    }


}
