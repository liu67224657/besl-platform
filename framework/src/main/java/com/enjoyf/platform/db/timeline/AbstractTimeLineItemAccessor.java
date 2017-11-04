/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.CommentType;
import com.enjoyf.platform.service.timeline.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractTimeLineItemAccessor  extends AbstractBaseTableAccessor<TimeLineItem> implements TimeLineItemAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractTimeLineItemAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "TIMELINE_ITEM_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_TIMELINE_ITEM_ID";
    private static final int TABLE_NUM = 100;

    @Override
    public TimeLineItem insert(TimeLineItem item, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            item.setTlId(getSeqNo(conn));

            pstmt = conn.prepareStatement(getInsertSql(item.getDomain(), item.getOwnUno()));

            //TLID,OWNUNO,TLDOMAIN,TLCONTENTTYPE, DIRECTUNO, DIRECTID, PARENTUNO, PARENTID,RELATIONUNO,RELATIONID,FILTERTYPE,TLDESCRIPTION,CREATEDATE,REMOVESTATUS,REMOVEDATE,ROOTID, ROOTUNO, COMMENTTYPE
            pstmt.setLong(1, item.getTlId());

            pstmt.setString(2, item.getOwnUno());
            pstmt.setString(3, item.getDomain().getCode());
            pstmt.setString(4, item.getType().getCode());

            pstmt.setString(5, item.getDirectUno());
            pstmt.setString(6, item.getDirectId());

            pstmt.setString(7, item.getParentUno());
            pstmt.setString(8, item.getParentId());

            pstmt.setString(9, item.getRelationUno());
            pstmt.setString(10, item.getRelationId());

            pstmt.setInt(11, item.getFilterType() == null ? 0 : item.getFilterType().getValue());

            pstmt.setString(12, item.getDescription());

            pstmt.setTimestamp(13, item.getCreateDate() != null ? new Timestamp(item.getCreateDate().getTime()) : new Timestamp(System.currentTimeMillis()));

            pstmt.setString(14, item.getRemoveStatus().getCode());
            pstmt.setTimestamp(15, item.getRemoveDate() != null ? new Timestamp(item.getRemoveDate().getTime()) : null);

            pstmt.setInt(16, item.getSpreadType().getValue());
            pstmt.setInt(17, item.getFavSum());
            pstmt.setString(18,item.getRootId());
            pstmt.setString(19,item.getRootUno());
            pstmt.setInt(20,item.getSource().getCode());
            //
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);

            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return item;
    }

    @Override
    public TimeLineItem get(String ownUno, TimeLineDomain domain, String directId, Connection conn) throws DbException {
        TimeLineItem returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND DIRECTID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, directId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On get a TimeLineItem, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<TimeLineItem> query(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<TimeLineItem> queryOnlyFocus(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<TimeLineItem> queryBefore(String ownUno, TimeLineDomain domain, Long before, Integer size, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<TimeLineItem> queryAfter(String ownUno, TimeLineDomain domain, Long after, Integer size, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<TimeLineItem> queryOrg(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public boolean updateStatus(String ownUno, TimeLineDomain domain, String directId, ActStatus removeStatus, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "UPDATE " + getTableName(domain, ownUno) + " SET REMOVESTATUS = ?, REMOVEDATE = ? WHERE OWNUNO = ? AND DIRECTID = ? AND REMOVESTATUS <> ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The updateStatus sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, removeStatus.getCode());
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            pstmt.setString(3, ownUno);
            pstmt.setString(4, directId);
            pstmt.setString(5, removeStatus.getCode());

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On updateStatus, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public List<TimeLineItem> query(String ownUno, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public List<TimeLineItem> queryRelationID(String ownUno, String relationId, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public boolean remove(String ownUno, TimeLineDomain domain, String directId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM " + getTableName(domain, ownUno) + "  WHERE OWNUNO = ? AND DIRECTID = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The remove sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, directId);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On remove timelineitem, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public boolean removeByDirectUno(String ownUno, TimeLineDomain domain, String directUno, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM " + getTableName(domain, ownUno) + " WHERE OWNUNO = ? AND DIRECTUNO = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The removeByDirectUno sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ownUno);
            pstmt.setString(2, directUno);

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            GAlerter.lab("On removeByDirectUno, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public int update(String ownUno, TimeLineDomain domain, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(getTableName(domain,ownUno),updateExpress,queryExpress,conn);
    }

    @Override
    public TimeLineItem getByExpress(String ownUno, TimeLineDomain domain, QueryExpress getExpress, Connection conn) throws DbException {
        return super.get(getTableName(domain,ownUno),getExpress,conn);
    }

    @Override
    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    ///private methods.
    private String getInsertSql(TimeLineDomain domain, String ownUno) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(domain, ownUno)
                + " (TLID, OWNUNO, TLDOMAIN, TLCONTENTTYPE, DIRECTUNO, DIRECTID, PARENTUNO, PARENTID, RELATIONUNO, RELATIONID,FILTERTYPE, TLDESCRIPTION, CREATEDATE, REMOVESTATUS, REMOVEDATE,SPREADTYPE,ITEMFAVSUM,ROOTID,ROOTUNO,COMMENTTYPE)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineItem INSERT Script:" + insertSql);
        }


        return insertSql;
    }

    protected String getTableName(TimeLineDomain domain, String ownUno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + domain.getCode().toUpperCase() + "_" + TableUtil.getTableNumSuffix(ownUno.hashCode(), TABLE_NUM);
    }

    protected String getUnoRemoveCreateDateIndexName(TimeLineDomain domain, String ownUno) throws DbException {
        return "IDX_TLI_" + domain.getCode().toUpperCase() + "_" + TableUtil.getTableNumSuffix(ownUno.hashCode(), TABLE_NUM) + "_REMOVE_UNO";
    }

    protected TimeLineItem rsToObject(ResultSet rs) throws SQLException {
        TimeLineItem entry = new TimeLineItem();

        //TLID,OWNUNO,TLDOMAIN, TLCONTENTTYPE, DIRECTUNO,DIRECTID,RELATIONUNO,RELATIONID,FILTERTYPE,DESCRIPTION,CREATEDATE,REMOVESTATUS,REMOVEDATE
        entry.setTlId(rs.getLong("TLID"));

        entry.setOwnUno(rs.getString("OWNUNO"));
        entry.setDomain(TimeLineDomain.getByCode(rs.getString("TLDOMAIN")));
        entry.setType(TimeLineContentType.getByCode(rs.getString("TLCONTENTTYPE")));

        entry.setDirectUno(rs.getString("DIRECTUNO"));
        entry.setDirectId(rs.getString("DIRECTID"));

        entry.setParentUno(rs.getString("PARENTUNO"));
        entry.setParentId(rs.getString("PARENTID"));

        entry.setRelationUno(rs.getString("RELATIONUNO"));
        entry.setRelationId(rs.getString("RELATIONID"));

        entry.setFilterType(TimeLineFilterType.getByValue(rs.getInt("FILTERTYPE")));

        entry.setDescription(rs.getString("TLDESCRIPTION"));

        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);

        entry.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));
        entry.setRemoveDate(rs.getTimestamp("REMOVEDATE") != null ? new Date(rs.getTimestamp("REMOVEDATE").getTime()) : null);

        entry.setFavSum(rs.getInt("ITEMFAVSUM"));
        entry.setSpreadType(ItemSpreadType.getByValue(rs.getInt("SPREADTYPE")));

        entry.setRootId(rs.getString("ROOTID"));
        entry.setRootUno(rs.getString("ROOTUNO"));

        entry.setSource(0 == rs.getInt("COMMENTTYPE") ? CommentType.LOCALSOURCE : CommentType.FOREIGNSOURCE);
        return entry;
    }
}