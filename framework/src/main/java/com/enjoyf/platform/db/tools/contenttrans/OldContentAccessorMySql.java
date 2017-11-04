package com.enjoyf.platform.db.tools.contenttrans;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.tools.contenttrans.OldContent;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class OldContentAccessorMySql implements OldContentAccessor {
    private static final String KEY_TABLE_NAME = "tbl_content_blog";

    @Override
    public List<OldContent> query(Pagination page, Connection conn) throws DbException {
        List<OldContent> returnValue = new ArrayList<OldContent>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(queryRowSize(conn));

            pstmt = conn.prepareStatement("SELECT * FROM " + KEY_TABLE_NAME + " ORDER BY publishdate LIMIT ?, ?");

            pstmt.setInt(1, page.getStartRowIdx());
            pstmt.setInt(2, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                OldContent content = new OldContent();

                content.setBlogid(rs.getInt("blogid"));
                content.setBlogrange(rs.getString("blogrange"));
                content.setBlogsubject(rs.getString("blogsubject"));
                content.setBlogtype(rs.getString("blogtype"));
                content.setContent(rs.getString("content"));
                content.setContentnum(rs.getInt("contentnum"));
                content.setFeedbacknum(rs.getInt("feedbacknum"));
                content.setIsreblog(rs.getString("isreblog"));
                content.setLovenum(rs.getInt("lovenum"));
                content.setOrginblogid(rs.getInt("orginblogid"));
                content.setOrginuno(rs.getString("orginuno"));
                content.setPublishdate(rs.getTimestamp("publishdate"));
                content.setReblognum(rs.getInt("reblognum"));
                content.setTag(rs.getString("tag"));
                content.setThumbimglink(rs.getString("thumbimglink"));
                content.setUno(rs.getString("uno"));
                content.setUrllink(rs.getString("urllink"));

                returnValue.add(content);
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

    private int queryRowSize(Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + KEY_TABLE_NAME;

        try {
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }
}
