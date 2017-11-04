package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.service.social.SocialRelation;
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
class SocialRelationAccessorMySql extends AbstractSocialRelationAccessor {

    @Override
    public List<SocialRelation> querySrcRelations(String srcUno, RelationType type, ActStatus status, Pagination page, Connection conn) throws DbException {
        List<SocialRelation> returnValue = new ArrayList<SocialRelation>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(querySrcRowSize(srcUno, type, status, conn));

            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND RELATIONTYPE = ? AND SRCSTATUS = ? ORDER BY SRCDATE DESC LIMIT ?, ?");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, status.getCode());
            pstmt.setInt(4, page.getStartRowIdx());
            pstmt.setInt(5, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select src relations, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int querySrcRowSize(String srcUno, RelationType type, ActStatus status, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND RELATIONTYPE = ? AND SRCSTATUS = ?";

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, srcUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, status.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On SocialRelationAccessorMySql querySrcRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    @Override
    public List<SocialRelation> querySrcCategoryRelations(String srcUno, RelationType type, ActStatus status, Long cateId, Pagination page, Connection conn) throws DbException {
        List<SocialRelation> returnValue = new ArrayList<SocialRelation>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(querySrcCategoryRowSize(srcUno, type, status, cateId, conn));

            if (cateId > 0) {
                String sql = "SELECT A.* FROM " + getTableName(type, srcUno) + " A, " + getRelationCategoryTableName(type, srcUno) +
                        " B WHERE A.SRCUNO = ? AND A.RELATIONTYPE = ? AND A.SRCSTATUS = ? AND B.SRCUNO = ? AND B.CATEGORYID = ? AND B.DESTUNO = A.DESTUNO AND A.RELATIONTYPE = ? ORDER BY A.SRCDATE DESC LIMIT ?, ?";

                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, srcUno);
                pstmt.setString(2, type.getCode());
                pstmt.setString(3, status.getCode());

                pstmt.setString(4, srcUno);
                pstmt.setLong(5, cateId);
                pstmt.setString(6, type.getCode());

                pstmt.setInt(7, page.getStartRowIdx());
                pstmt.setInt(8, page.getPageSize());
            } else {
                String sql = "SELECT A.* FROM " + getTableName(type, srcUno) +
                        " A WHERE SRCUNO = ? AND RELATIONTYPE = ? AND SRCSTATUS = ? AND DESTUNO NOT IN(SELECT DESTUNO FROM " +
                        getRelationCategoryTableName(type, srcUno) + " WHERE SRCUNO = ? AND RELATIONTYPE = ?) ORDER BY SRCDATE DESC LIMIT ?, ?";

                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, srcUno);
                pstmt.setString(2, type.getCode());
                pstmt.setString(3, status.getCode());

                pstmt.setString(4, srcUno);
                pstmt.setString(5, type.getCode());

                pstmt.setInt(6, page.getStartRowIdx());
                pstmt.setInt(7, page.getPageSize());
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select src relations, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;

    }

    private int querySrcCategoryRowSize(String srcUno, RelationType type, ActStatus status, Long cateId, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            if (cateId > 0) {
                String sql = "SELECT COUNT(1) FROM " + getTableName(type, srcUno) + " A, " + getRelationCategoryTableName(type, srcUno) +
                        " B WHERE A.SRCUNO = ? AND A.RELATIONTYPE = ? AND A.SRCSTATUS = ? AND B.SRCUNO = ? AND B.CATEGORYID = ? AND B.DESTUNO = A.DESTUNO AND A.RELATIONTYPE = ?";

                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, srcUno);
                pstmt.setString(2, type.getCode());
                pstmt.setString(3, status.getCode());

                pstmt.setString(4, srcUno);
                pstmt.setLong(5, cateId);
                pstmt.setString(6, type.getCode());
            } else {
                String sql = "SELECT COUNT(1) FROM " + getTableName(type, srcUno) +
                        " WHERE SRCUNO = ? AND RELATIONTYPE = ? AND SRCSTATUS = ? AND DESTUNO NOT IN(SELECT DESTUNO FROM " +
                        getRelationCategoryTableName(type, srcUno) + " WHERE SRCUNO = ? AND RELATIONTYPE = ?)";

                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, srcUno);
                pstmt.setString(2, type.getCode());
                pstmt.setString(3, status.getCode());

                pstmt.setString(4, srcUno);
                pstmt.setString(5, type.getCode());
            }


            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On SocialRelationAccessorMySql querySrcCategoryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

    @Override
    public List<SocialRelation> queryDestRelations(String srcUno, RelationType type, ActStatus status, Pagination page, Connection conn) throws DbException {
        List<SocialRelation> returnValue = new ArrayList<SocialRelation>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            page.setTotalRows(queryDestRowSize(srcUno, type, status, conn));

            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND RELATIONTYPE = ? AND DESTSTATUS = ? ORDER BY DESTDATE DESC LIMIT ?, ?");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, status.getCode());
            pstmt.setInt(4, page.getStartRowIdx());
            pstmt.setInt(5, page.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On select dest relations, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    private int queryDestRowSize(String srcUno, RelationType type, ActStatus status, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND RELATIONTYPE = ? AND DESTSTATUS = ?";

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, srcUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, status.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On SocialRelationAccessorMySql queryDestRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }
}
