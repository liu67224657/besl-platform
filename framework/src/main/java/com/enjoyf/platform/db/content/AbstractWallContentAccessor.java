/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.content;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ContentType;
import com.enjoyf.platform.service.content.DiscoveryWallContent;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

class AbstractWallContentAccessor implements WallContentAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractWallContentAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "CONTENT_DISCOVERY_WALL";

    ////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME +
                " (DISCOVERYID, DOMAINNAME, CONTENTUNO, CONTENTID, WALLTAG, WALLCONTENT, CONTENTTYPE, PUBLISHDATE, REMOVESTATUS)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("DiscoveryWallContent INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected DiscoveryWallContent rsToObject(ResultSet rs) throws SQLException {
        DiscoveryWallContent entity = new DiscoveryWallContent();

        //DISCOVERYID, DOMAINNAME, CONTENTUNO, CONTENTID, WALLTAG, WALLCONTENT, CONTENTTYPE, PUBLISHDATE, REMOVESTATUS

        entity.setDomainName(rs.getString("DOMAINNAME"));

        entity.setContentUno(rs.getString("CONTENTUNO"));
        entity.setContentId(rs.getString("CONTENTID"));

        entity.setWallContent(rs.getString("WALLCONTENT"));

        entity.setContentType(ContentType.getByValue(rs.getInt("CONTENTTYPE")));

        entity.setPublishDate(rs.getTimestamp("PUBLISHDATE"));

        return entity;
    }

    @Override
    public DiscoveryWallContent insert(DiscoveryWallContent entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //DISCOVERYID, DOMAINNAME, CONTENTUNO, CONTENTID, WALLTAG, WALLCONTENT, CONTENTTYPE, PUBLISHDATE, REMOVESTATUS

            pstmt.setString(2, entry.getDomainName())
            ;
            pstmt.setString(3, entry.getContentUno());
            pstmt.setString(4, entry.getContentId());
            pstmt.setString(6, entry.getWallContent());
            pstmt.setInt(7, entry.getContentType().getValue());
            pstmt.setTimestamp(8, entry.getPublishDate() != null ? new Timestamp(entry.getPublishDate().getTime()) : new Timestamp(System.currentTimeMillis()));

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
    public List<DiscoveryWallContent> query(Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }
}
