/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.social;


import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.service.social.SocialRelation;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
abstract class AbstractSocialRelationAccessor extends AbstractSequenceBaseTableAccessor<SocialRelation> implements SocialRelationAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialRelationAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "SOCIAL_RELATION_";
    private static final String KEY_CATEGORY_TABLE_NAME_PREFIX = "RELATION_CATEGORY_";

    private static final String KEY_SEQUENCE_NAME = "SEQ_SOCIAL_RELATION_ID";
    private static final int TABLE_NUM = 100;

    @Override
    public SocialRelation insert(SocialRelation relation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            relation.setRelationId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql(relation));

            //
            pstmt.setLong(1, relation.getRelationId());

            pstmt.setString(2, relation.getRelationType().getCode());

            pstmt.setString(3, relation.getSrcUno());
            pstmt.setString(4, relation.getDestUno());
            pstmt.setString(5, relation.getDescription());

            pstmt.setInt(6, relation.getRelationRate());

            pstmt.setString(7, relation.getSrcStatus().getCode());
            pstmt.setString(8, relation.getDestStatus().getCode());

            pstmt.setTimestamp(9, relation.getSrcDate() != null ? new Timestamp(relation.getSrcDate().getTime()) : null);
            pstmt.setTimestamp(10, relation.getDestDate() != null ? new Timestamp(relation.getDestDate().getTime()) : null);

            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return relation;
    }

    @Override
    public List<SocialRelation> queryAllSrcRelations(String srcUno, RelationType type, ActStatus status, Connection conn) throws DbException {
        List<SocialRelation> returnValue = new ArrayList<SocialRelation>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND RELATIONTYPE = ? AND SRCSTATUS = ? ORDER BY SRCDATE DESC");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, type.getCode());
            pstmt.setString(3, status.getCode());

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

    @Override
    public List<SocialRelation> querySrcRelations(String srcUno, RelationType type, ActStatus status, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<SocialRelation> querySrcCategoryRelations(String srcUno, RelationType type, ActStatus status, Long cateId, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public Map<String, SocialRelation> queryRelationsByDestUnos(String srcUno, RelationType type, Set<String> destUnos, Connection conn) throws DbException {
        Map<String, SocialRelation> returnValue = new HashMap<String, SocialRelation>();

        if (destUnos == null || destUnos.size() == 0) {
            return returnValue;
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND RELATIONTYPE = ? AND " + getDestUnoSelectSql(destUnos.size()));

            pstmt.setString(1, srcUno);
            pstmt.setString(2, type.getCode());

            int i = 0;
            for (String destUno : destUnos) {
                pstmt.setString(i + 3, destUno);
                i++;
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                SocialRelation relation = rsToObject(rs);

                returnValue.put(relation.getDestUno(), relation);
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryRelationsByDestUnos, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<SocialRelation> queryDestRelations(String srcUno, RelationType type, ActStatus status, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public SocialRelation get(String srcUno, String destUno, RelationType type, Connection conn) throws DbException {
        SocialRelation returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND DESTUNO = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, destUno);
            pstmt.setString(3, type.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On select a relation, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public boolean updateSrcDescription(String srcUno, String destUno, RelationType type, String description, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(type, srcUno) + " SET DESCRIPTION = ? WHERE SRCUNO = ? AND DESTUNO = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, description);

            pstmt.setString(2, srcUno);
            pstmt.setString(3, destUno);
            pstmt.setString(4, type.getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On updateSrcDescription, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean updateSrcStatus(String srcUno, String destUno, RelationType type, ActStatus status, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(type, srcUno) + " SET SRCSTATUS = ?, SRCDATE = ? WHERE SRCUNO = ? AND DESTUNO = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, status.getCode());
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            pstmt.setString(3, srcUno);
            pstmt.setString(4, destUno);
            pstmt.setString(5, type.getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update relation src status, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean updateDestStatus(String srcUno, String destUno, RelationType type, ActStatus status, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("UPDATE " + getTableName(type, srcUno) + " SET DESTSTATUS = ?, DESTDATE = ? WHERE SRCUNO = ? AND DESTUNO = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, status.getCode());
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            pstmt.setString(3, srcUno);
            pstmt.setString(4, destUno);
            pstmt.setString(5, type.getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On update relation dest status, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public List<SocialRelation> query(String srcUno, RelationType type, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(getTableName(type, srcUno), queryExpress, conn);
    }

    @Override
    public List<SocialRelation> querySocialRelationByRelationTypeAndTable_NO(RelationType relationType, int tablNo, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME_PREFIX + relationType.getCode().toUpperCase() + "_" + TableUtil.getTableNumSuffix(tablNo, TABLE_NUM), new QueryExpress(), conn);
    }

    @Override
    public boolean remove(String srcUno, String destUno, RelationType type, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("DELETE " + getTableName(type, srcUno) + " WHERE SRCUNO = ? AND DESTUNO = ? AND RELATIONTYPE = ?");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, destUno);
            pstmt.setString(3, type.getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On remove relation, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }


    ///private methods.
    private String getInsertSql(SocialRelation relation) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(relation.getRelationType(), relation.getSrcUno())
                + " (RELATIONID, RELATIONTYPE, SRCUNO, DESTUNO, DESCRIPTION, RELATIONRATE, SRCSTATUS, DESTSTATUS, SRCDATE, DESTDATE)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("SocialRelation INSERT Script:" + insertSql);
        }

        return insertSql;
    }


    private String getDestUnoSelectSql(int size) {
        StringBuffer returnValue = new StringBuffer();

        returnValue.append("DESTUNO in (");
        for (int i = 0; i < size; i++) {
            returnValue.append("?");
            if(i<size-1){
                returnValue.append(",");
            }
        }
        returnValue.append(")");
//        for (int i = 0; i < size; i++) {
//            returnValue.append("DESTUNO = ? ");
//
//            if (i < (size - 1)) {
//                returnValue.append("OR ");
//            }
//        }

        return returnValue.toString();
    }

    protected String getTableName(RelationType type, String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + type.getCode().toUpperCase() + "_" + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    protected String getRelationCategoryTableName(RelationType type, String uno) {
        if(type.equals(RelationType.SFOCUS)){
            type = RelationType.FOCUS;
        }
        return KEY_CATEGORY_TABLE_NAME_PREFIX + type.getCode().toUpperCase() + "_" + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    protected SocialRelation rsToObject(ResultSet rs) throws SQLException {
        SocialRelation entry = new SocialRelation();

        entry.setRelationId(rs.getLong("RELATIONID"));

        entry.setRelationType(RelationType.getByCode(rs.getString("RELATIONTYPE")));

        entry.setSrcUno(rs.getString("SRCUNO"));
        entry.setDestUno(rs.getString("DESTUNO"));
        entry.setDescription(rs.getString("DESCRIPTION"));

        entry.setRelationRate(rs.getInt("RELATIONRATE"));

        entry.setSrcStatus(ActStatus.getByCode(rs.getString("SRCSTATUS")));
        entry.setDestStatus(ActStatus.getByCode(rs.getString("DESTSTATUS")));

        entry.setSrcDate(rs.getTimestamp("SRCDATE") != null ? new Date(rs.getTimestamp("SRCDATE").getTime()) : null);
        entry.setDestDate(rs.getTimestamp("DESTDATE") != null ? new Date(rs.getTimestamp("DESTDATE").getTime()) : null);

        return entry;
    }
}
