package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDisplyType;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli
 * Date: 2014/12/18
 * Time: 16:42
 */
public abstract class AbstractTagDedearchivesAccessor extends AbstractBaseTableAccessor<TagDedearchives> implements TagDedearchivesAccessor {
    private static final String KEY_TABLE_NAME = "tag_dede_archives";

    private static Logger logger = LoggerFactory.getLogger(AbstractTagDedearchivesAccessor.class);

    @Override
    public TagDedearchives insert(TagDedearchives tag, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //id,tagid,dede_archives_id,type,dede_archives_title,dede_archives_description,dede_archives_litpic,
            //dede_archives_pubdate,dede_archives_showios,dede_archives_showandroid,extstring,display_order,remove_status
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, tag.getId());
            pstmt.setLong(2, tag.getTagid());
            pstmt.setString(3, tag.getDede_archives_id());
            pstmt.setString(4, tag.getDede_archives_title());
            pstmt.setString(5, tag.getDede_archives_description());
            pstmt.setString(6, tag.getDede_archives_litpic());
            pstmt.setLong(7, tag.getDede_archives_pubdate());
            pstmt.setInt(8, tag.getDede_archives_showios());
            pstmt.setInt(9, tag.getDede_archives_showandroid());
            pstmt.setInt(10, tag.getTagDisplyType().getCode());
            pstmt.setLong(11, tag.getDisplay_order());
            pstmt.setString(12, tag.getRemove_status().getCode());
            pstmt.setLong(13, tag.getDisplay_tag());
            pstmt.setString(14, tag.getDede_archives_htlistimg());
            pstmt.setString(15, tag.getDede_archives_url());
            pstmt.setInt(16, tag.getDede_redirect_type().getCode());
            pstmt.setString(17, tag.getDede_redirect_url());
            pstmt.setInt(18, tag.getArchiveRelationType() == null ? 0 : tag.getArchiveRelationType().getCode());
            pstmt.setInt(19, tag.getArchiveContentType() == null ? 0 : tag.getArchiveContentType().getCode());
            pstmt.setString(20, tag.getProfileId() == null ? "" : tag.getProfileId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return tag;
    }

    @Override
    protected TagDedearchives rsToObject(ResultSet rs) throws SQLException {
        TagDedearchives dto = new TagDedearchives();
        dto.setId(rs.getString("id"));
        dto.setTagid(rs.getLong("tagid"));
        dto.setDede_archives_id(rs.getString("dede_archives_id"));
        dto.setDede_archives_title(rs.getString("dede_archives_title"));
        dto.setDede_archives_description(rs.getString("dede_archives_description"));
        dto.setDede_archives_litpic(rs.getString("dede_archives_litpic"));
        dto.setDisplay_order(rs.getLong("display_order"));
        dto.setDede_archives_pubdate(rs.getLong("dede_archives_pubdate"));
        dto.setDede_archives_showios(rs.getInt("dede_archives_showios"));
        dto.setDede_archives_showandroid(rs.getInt("dede_archives_showandroid"));
        dto.setTagDisplyType(TagDisplyType.getByCode(rs.getInt("display_type")));
        dto.setRemove_status(ValidStatus.getByCode(rs.getString("remove_status")));
        dto.setDisplay_tag(rs.getLong("display_tag"));
        dto.setDede_archives_htlistimg(rs.getString("dede_archives_htlistimg"));
        dto.setDede_archives_url(rs.getString("dede_archives_url"));
        dto.setDede_redirect_type(AppRedirectType.getByCode(rs.getInt("dede_redirect_type")));
        dto.setDede_redirect_url(rs.getString("dede_redirect_url"));
        dto.setArchiveRelationType(ArchiveRelationType.getByCode(rs.getInt("relation_type")));
        dto.setArchiveContentType(ArchiveContentType.getByCode(rs.getInt("content_type")));
        dto.setProfileId(rs.getString("profile_id"));
        return dto;
    }

    @Override
    public TagDedearchives get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<TagDedearchives> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<TagDedearchives> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public List<Integer> queryPostNum(QueryExpress queryExpress, Connection conn) throws DbException {
        List<Integer> returnValue = new ArrayList<Integer>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(*) as c FROM " + KEY_TABLE_NAME + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true)+"  GROUP BY profile_id HAVING COUNT(profile_id)>0";
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
                returnValue.add(rs.getInt("c"));
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
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public PageRows<Long> queryByDistinct(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        PageRows<Long> pageRows = null;
        List<Long> list = new ArrayList<Long>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT DISTINCT(dede_archives_id) FROM " + KEY_TABLE_NAME + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true)+" LIMIT ?, ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            //
            int index = 1;
            index = ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
            pstmt.setInt(index++, page.getStartRowIdx());
            pstmt.setInt(index++, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(Long.valueOf(rs.getString(1)));
            }

            String countSql = "SELECT COUNT(DISTINCT(dede_archives_id)) FROM " + KEY_TABLE_NAME + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true);
            pstmt = conn.prepareStatement(countSql);
            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
            rs = pstmt.executeQuery();
            if(rs.next()){
                page.setTotalRows(rs.getInt(1));
            }

            pageRows = new PageRows<Long>();
            pageRows.setPage(page);
            pageRows.setRows(list);
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return pageRows;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(id,tagid,dede_archives_id,dede_archives_title," +
                "dede_archives_description,dede_archives_litpic,dede_archives_pubdate,dede_archives_showios," +
                "dede_archives_showandroid,display_type,display_order,remove_status,display_tag," +
                "dede_archives_htlistimg,dede_archives_url,dede_redirect_type,dede_redirect_url," +
                "relation_type,content_type,profile_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("profile insert sql" + sql);
        }
        return sql;
    }
}
