package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.InteractionType;
import com.enjoyf.platform.service.content.stats.TopInteractionContent;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ContentInteractionStatAccessorMySql extends AbstractContentInteractionStatAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractContentInteractionStatAccessor.class);

    //
    public List<TopInteractionContent> queryTopInteractionContents(InteractionType type, Date from, Date to, int size, Connection conn) throws DbException {
        //
        List<TopInteractionContent> returnValue = new ArrayList<TopInteractionContent>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = null;
        if (type == null) {
            sql = "SELECT COUNT(1) AS V, CONTENTID, CONTENTUNO FROM " + KEY_TABLE_NAME + " WHERE CREATEDATE >=? AND CREATEDATE < ? AND REMOVESTATUS=? GROUP BY CONTENTID ORDER BY V DESC LIMIT ?";
        } else {
            sql = "SELECT COUNT(1) AS V, CONTENTID, CONTENTUNO FROM " + KEY_TABLE_NAME + " WHERE CREATEDATE >=? AND CREATEDATE < ? AND INTERACTIONTYPE = ? AND REMOVESTATUS=? GROUP BY CONTENTID ORDER BY V DESC LIMIT ?";
        }
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            pstmt.setTimestamp(1, new Timestamp(from.getTime()));
            pstmt.setTimestamp(2, new Timestamp(to.getTime()));
            if (type == null) {
                pstmt.setString(3, ActStatus.UNACT.getCode());
                pstmt.setInt(4, size);

            } else {
                pstmt.setString(3, type.getCode());
                pstmt.setString(4, ActStatus.UNACT.getCode());
                pstmt.setInt(5, size);
            }

            //
            rs = pstmt.executeQuery();

            //
            while (rs.next()) {
                returnValue.add(rsToTopInteractionContent(rs));
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
}
