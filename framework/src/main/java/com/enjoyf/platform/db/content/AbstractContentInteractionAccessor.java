/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.content;


import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.tools.ContentReplyAuditStatus;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UUIDUtil;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract class AbstractContentInteractionAccessor extends AbstractSequenceBaseTableAccessor<ContentInteraction> implements ContentInteractionAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractContentInteractionAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "CONTENT_INTERACTION_";
    private static final int TABLE_NUM = 100;

    private static final String KEY_SEQ_FOLLOR_NUM = "SEQ_FLOOR_NUM";

    @Override
    public ContentInteraction insert(ContentInteraction interaction, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            interaction.setInteractionId(getInteractionId());
            interaction.setCreateDate(interaction.getCreateDate() == null ? new Date() : interaction.getCreateDate());
            interaction.setAuditStatus(new ContentReplyAuditStatus(0));

            pstmt = conn.prepareStatement(getInsertSql(interaction.getContentId()));

            //REPLYID,CONTENTID,CONTENTUNO, PARENTREPLYID, PARENTREPLYUNO, INTERACTIONCONTENT, INTERACTIONCONTENTTYPE, INTERACTIONIMAGES, REPLYUNO, REMOVED, REPLYDATE  REPLYIP,ROOTID,ROOTUNO,F
            pstmt.setString(1, interaction.getInteractionId());

            pstmt.setString(2, interaction.getContentId());
            pstmt.setString(3, interaction.getContentUno());

            pstmt.setString(4, interaction.getParentId());
            pstmt.setString(5, interaction.getParentUno());

            pstmt.setString(6, interaction.getInteractionContent());

            pstmt.setInt(7, interaction.getInteractionContentType().getValue());

            pstmt.setString(8, interaction.getInteractionImages() != null ? interaction.getInteractionImages().toJsonStr() : null);

            pstmt.setString(9, interaction.getInteractionUno());

            pstmt.setString(10, interaction.getRemoveStatus().getCode());
            pstmt.setString(11, interaction.getInteractionType().getCode());
            pstmt.setTimestamp(12, new Timestamp(interaction.getCreateDate().getTime()));
            pstmt.setString(13, interaction.getCreateIp());

            pstmt.setString(14, interaction.getRootId());
            pstmt.setString(15, interaction.getRootUno());

            pstmt.setLong(16, interaction.getFloorNo());

            pstmt.setInt(17, interaction.getReplyTimes());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ContentInteraction, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return interaction;
    }

    public long getFloorNum(Connection conn) throws DbException {
        return getSeqNo(KEY_SEQ_FOLLOR_NUM, conn);
    }

    public long getMaxFloorNum(String contentId, Connection conn) throws DbException {
        long returnValue = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(*) FROM " + getTableName(contentId) + " WHERE CONTENTID=? AND ROOTID IS NULL";
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            //
            pstmt.setString(1, contentId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rs.getLong(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On get, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }


    @Override
    public ContentInteraction getInteraction(String contentId, QueryExpress getExpress, Connection conn) throws DbException {
        return get(getTableName(contentId), getExpress, conn);
    }

    @Override
    public boolean updateInteraction(String contentId, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(getTableName(contentId), updateExpress, queryExpress, conn) > 0;
    }

    @Override
    public List<ContentInteraction> queryInteraction(String contentId, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(getTableName(contentId), queryExpress, page, conn);
    }

    @Override
    public List<ContentInteraction> queryInteraction(String contentId, QueryExpress queryExpress, Connection conn) throws DbException {
        return query(getTableName(contentId), queryExpress, conn);
    }

    @Override
    public List<ContentInteraction> queryByIIdsCid(List<String> iIds, String cid, Connection conn) throws DbException {
        List<ContentInteraction> returnValue = new ArrayList<ContentInteraction>();

        if (iIds == null || iIds.size() == 0) {
            return returnValue;
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + getTableName(cid) + " WHERE (" + getIidSelectSql(iIds.size()) + ") AND REMOVESTATUS = ?");

            int i = 1;
            for (String rid : iIds) {
                pstmt.setString(i++, rid);
            }
            pstmt.setString(i, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryByiIdsCid, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<ContentInteraction> queryContentReply(QueryExpress queryExpress, Pagination p, Connection conn) throws DbException {
        return query(getViewContentReplyName(), queryExpress, p, conn);
    }

    @Override
    public int countByQueryExpress(String contentId, QueryExpress queryExpress, Connection conn) throws DbException {
        return queryRowSize(getTableName(contentId), queryExpress, conn);
    }

    //
    public long queryReplyTimes(String contentId, Date from, Date to, Connection conn) throws DbException {
        long returnValue = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) AS VAL FROM " + getTableName(contentId) +
                " WHERE CONTENTID = ? AND CREATEDATE >= ? AND CREATEDATE < ? AND INTERACTIONTYPE = ? AND REMOVESTATUS = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The queryReplyTimes sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, contentId);
            pstmt.setTimestamp(2, new Timestamp(from.getTime()));
            pstmt.setTimestamp(3, new Timestamp(to.getTime()));
            pstmt.setString(4, InteractionType.REPLY.getCode());
            pstmt.setString(5, ActStatus.UNACT.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rs.getLong(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryReplyTimes, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    protected ContentInteraction rsToObject(ResultSet rs) throws SQLException {
        ContentInteraction entity = new ContentInteraction();

        //INTERACTIONID,CONTENTID,CONTENTUNO,PARENTID, PARENTUNO,
        //INTERACTIONCONTENT, INTERACTIONUNO, REMOVESTATUS, CREATEDATE  CREATEIP
        entity.setInteractionId(rs.getString("INTERACTIONID"));

        entity.setContentId(rs.getString("CONTENTID"));
        entity.setContentUno(rs.getString("CONTENTUNO"));

        entity.setParentId(rs.getString("PARENTID"));
        entity.setParentUno(rs.getString("PARENTUNO"));

        entity.setRootId(rs.getString("ROOTID"));
        entity.setRootUno(rs.getString("ROOTUNO"));

        entity.setReplyTimes(rs.getInt("REPLYTIMES"));
        entity.setFloorNo(rs.getInt("FLOORNUM"));

        entity.setInteractionContent(rs.getString("INTERACTIONCONTENT"));
        entity.setInteractionContentType(InteractionContentType.getByValue(rs.getInt("INTERACTIONCONTENTTYPE")));
        entity.setInteractionImages(ImageContentSet.parse(rs.getString("INTERACTIONIMAGES")));

        entity.setInteractionUno(rs.getString("INTERACTIONUNO"));

        entity.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));

        entity.setCreateDate(rs.getTimestamp("CREATEDATE"));
        entity.setCreateIp(rs.getString("CREATEIP"));

        entity.setAuditStatus(ContentReplyAuditStatus.getByValue(rs.getInt("AUDITSTATUS")));

        entity.setInteractionType(InteractionType.getByCode(rs.getString("INTERACTIONTYPE")));
        entity.setIntetractionDisplayType(IntetractionDisplayType.getByValue(rs.getInt("DISPLAYTYPE")));
        return entity;
    }

    protected String getTableName(String contentId) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(contentId.hashCode(), TABLE_NUM);
    }

    protected String getViewContentReplyName() throws DbException {
        return "VIEW_CONTENT_INTERACTION";
    }

    private String getInsertSql(String contentId) throws DbException {
        String insertSql = "INSERT INTO " + getTableName(contentId) + " (INTERACTIONID, CONTENTID, CONTENTUNO, PARENTID, PARENTUNO, INTERACTIONCONTENT, INTERACTIONCONTENTTYPE, INTERACTIONIMAGES, INTERACTIONUNO, REMOVESTATUS,INTERACTIONTYPE,CREATEDATE,CREATEIP,ROOTID,ROOTUNO,FLOORNUM,REPLYTIMES) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ContentReply INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    private String getIidSelectSql(int size) {
        StringBuffer returnValue = new StringBuffer();

        for (int i = 0; i < size; i++) {
            returnValue.append("INTERACTIONID = ? ");

            if (i < (size - 1)) {
                returnValue.append("OR ");
            }
        }

        return returnValue.toString();
    }


    private String getInteractionId() {
        return UUIDUtil.getShortUUID();
    }
}
