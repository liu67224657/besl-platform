package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.WikiResource;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Author: ericliu
 * Date: 11-8-25
 * Time: 下午4:53
 * Desc:
 */
abstract class AbstractWikiResourceAccessor extends AbstractSequenceBaseTableAccessor<WikiResource> implements WikiResourceAccessor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TABLE_NAME = "WIKI_RESOURCE";
    private static final String KEY_SEQUENCE_NAME = "SEQ_GAME_RESOURCE_ID";

    @Override
    public WikiResource insert(WikiResource wikiResource, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        //RESOURCEID WIKINAME WIKICODE WIKIURL ICON WIKIDESC REMOVESTATUS CREATEUSERID CREATEDATE LASTMODIFYUSERID LASTMODIFYDATE
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            wikiResource.setResourceId(getSeqNo(KEY_SEQUENCE_NAME, conn));


            pstmt.setLong(1, wikiResource.getResourceId());
            pstmt.setString(2, wikiResource.getWikiName());
            pstmt.setString(3, wikiResource.getWikiCode());
            pstmt.setString(4, wikiResource.getWikiUrl());
            pstmt.setString(5, wikiResource.getIcon());
            pstmt.setString(6, wikiResource.getThumbIcon());
            pstmt.setString(7, wikiResource.getWikiDesc());
            pstmt.setString(8, wikiResource.getRemoveStatus().getCode());
            pstmt.setString(9, wikiResource.getCreateUserid());
            pstmt.setTimestamp(10, new Timestamp(wikiResource.getCreateDate() == null ? System.currentTimeMillis() : wikiResource.getCreateDate().getTime()));
            pstmt.setString(11, wikiResource.getLastModifyUserid());
            pstmt.setTimestamp(12, wikiResource.getLastModifyDate() == null ? null : new Timestamp(wikiResource.getLastModifyDate().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return wikiResource;
    }

    @Override
    public WikiResource get(QueryExpress getExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, getExpress, conn);
    }

    @Override
    public List<WikiResource> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<WikiResource> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected WikiResource rsToObject(ResultSet rs) throws SQLException {
        //RESOURCEID WIKINAME WIKICODE  WIKIURL  ICON   WIKIDESC  REMOVESTATUS   CREATEUSERID  CREATEDATE MODIFYUSERID  LASTMODIFYDATE
        WikiResource returnObj = new WikiResource();
        returnObj.setResourceId(rs.getLong("RESOURCEID"));
        returnObj.setWikiName(rs.getString("WIKINAME"));
        returnObj.setWikiCode(rs.getString("WIKICODE"));
        returnObj.setWikiUrl(rs.getString("WIKIURL"));
        returnObj.setIcon(rs.getString("ICON"));
        returnObj.setThumbIcon(rs.getString("THUMBICON"));
        returnObj.setWikiDesc(rs.getString("WIKIDESC"));
        returnObj.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));
        returnObj.setCreateUserid(rs.getString("CREATEUSERID"));
        returnObj.setCreateDate(new Date(rs.getTimestamp("CREATEDATE").getTime()));

        returnObj.setTotalPageNum(rs.getInt("TOTALPAGENUM"));
        if (rs.getTimestamp("LASTUPDATEPAGEDATE") != null) {
            returnObj.setLastUpdatePageDate(new Date(rs.getTimestamp("LASTUPDATEPAGEDATE").getTime()));
        }
        returnObj.setLastWeekUpdatePageNum(rs.getInt("LASTWEEKUPDATEPAGENUM"));

        returnObj.setLastModifyUserid(rs.getString("LASTMODIFYUSERID"));
        if (rs.getTimestamp("LASTMODIFYDATE") != null) {
            returnObj.setLastModifyDate(new Date(rs.getTimestamp("LASTMODIFYDATE").getTime()));
        }
        return returnObj;
    }

    private String getInsertSql() {
        String sqlScript = "INSERT INTO " + TABLE_NAME + "(RESOURCEID,WIKINAME,WIKICODE,WIKIURL,ICON,THUMBICON,WIKIDESC,REMOVESTATUS,CREATEUSERID,CREATEDATE,LASTMODIFYUSERID,LASTMODIFYDATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }
}
