package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.message.SocialMessage;
import com.enjoyf.platform.service.message.SocialMessageCategory;
import com.enjoyf.platform.service.message.SocialMessageType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */
public class AbstractSocialMessageAccessor extends AbstractSequenceBaseTableAccessor<SocialMessage> implements SocialMessageAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageAccessor.class);
    //
    private static final String KEY_TABLE_NAME_PREFIX = "social_message_";
    private static final int TABLE_NUM = 10;

    @Override
    public SocialMessage insert(SocialMessage socialMessage, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(socialMessage.getOwnUno()), Statement.RETURN_GENERATED_KEYS);


            //msgbody msgtype  msgcategory  ownuno  senduno receiveuno contentid contentuno replyid replyuno parentid parentuno rootid rootuno removestatus  createdate createip createuserid
            pstmt.setString(1, socialMessage.getMsgBody() == null ? "" : socialMessage.getMsgBody());
            pstmt.setInt(2, socialMessage.getMsgType().getCode());
            pstmt.setInt(3, socialMessage.getMsgCategory().getCode());
            pstmt.setString(4, socialMessage.getOwnUno() == null ? "" : socialMessage.getOwnUno());
            pstmt.setString(5, socialMessage.getSendUno() == null ? "" : socialMessage.getSendUno());
            pstmt.setString(6, socialMessage.getReceiveUno() == null ? "" : socialMessage.getReceiveUno());

            pstmt.setLong(7, socialMessage.getContentId() == null ? 0l : socialMessage.getContentId());
            pstmt.setString(8, socialMessage.getContentUno() == null ? "" : socialMessage.getContentUno());
            pstmt.setLong(9, socialMessage.getReplyId() == null ? 0l : socialMessage.getReplyId());
            pstmt.setString(10, socialMessage.getReplyUno() == null ? "" : socialMessage.getReplyUno());
            pstmt.setLong(11, socialMessage.getParentId() == null ? 0l : socialMessage.getParentId());
            pstmt.setString(12, socialMessage.getParentUno() == null ? "" : socialMessage.getParentUno());
            pstmt.setLong(13, socialMessage.getRootId() == null ? 0l : socialMessage.getRootId());
            pstmt.setString(14, socialMessage.getRootUno() == null ? "" : socialMessage.getRootUno());

            pstmt.setString(15, socialMessage.getReadStatus() == null ? ActStatus.UNACT.getCode() : socialMessage.getReadStatus().getCode());
            pstmt.setString(16, socialMessage.getPublishStatus() == null ? ActStatus.UNACT.getCode() : socialMessage.getPublishStatus().getCode());
            pstmt.setString(17, socialMessage.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : socialMessage.getRemoveStatus().getCode());
            pstmt.setTimestamp(18, new Timestamp(socialMessage.getCreateDate() == null ? System.currentTimeMillis() : socialMessage.getCreateDate().getTime()));
            pstmt.setString(19, socialMessage.getCreateIp() == null ? "" : socialMessage.getCreateIp());
            pstmt.setString(20, socialMessage.getCreateUserId() == null ? "" : socialMessage.getCreateUserId());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                socialMessage.setMsgId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialMessage;
    }

    @Override
    public SocialMessage getSocialMessage(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return get(getTableName(ownUno), queryExpress, conn);
    }

    @Override
    public int updateSocialMessage(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(getTableName(ownUno), updateExpress, queryExpress, conn);
    }

    @Override
    public List<SocialMessage> querySocialMessageList(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(getTableName(ownUno), queryExpress, page, conn);
    }

    private String getInsertSql(String uno) throws DbException {
        //msgbody msgtype  msgcategory  ownuno  senduno receiveuno contentid contentuno replyid replyuno parentid parentuno rootid rootuno removestatus  createdate createip createuserid
        String sql = "INSERT INTO " + getTableName(uno) + "(msgbody,msgtype,msgcategory,ownuno,senduno,receiveuno," +
                "contentid,contentuno,replyid,replyuno,parentid,parentuno,rootid,rootuno,readstatus,pubstatus,removestatus,createdate," +
                "createip,createuserid)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }

    private String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }

    @Override
    protected SocialMessage rsToObject(ResultSet rs) throws SQLException {
        SocialMessage message = new SocialMessage();
        message.setMsgId(rs.getLong("msgid"));
        message.setMsgBody(rs.getString("msgbody"));
        message.setMsgType(SocialMessageType.getByCode(rs.getInt("msgtype")));
        message.setMsgCategory(SocialMessageCategory.getByCode(rs.getInt("msgcategory")));
        message.setOwnUno(rs.getString("ownuno"));
        message.setSendUno(rs.getString("senduno"));
        message.setReceiveUno(rs.getString("receiveuno"));
        message.setContentId(rs.getLong("contentid"));
        message.setContentUno(rs.getString("contentuno"));
        message.setReplyId(rs.getLong("replyid"));
        message.setReplyUno(rs.getString("replyuno"));
        message.setParentId(rs.getLong("parentid"));
        message.setParentUno(rs.getString("parentuno"));
        message.setRootId(rs.getLong("rootid"));
        message.setRootUno(rs.getString("rootuno"));
        message.setReadStatus(ActStatus.getByCode(rs.getString("readstatus")));
        message.setReadTime(rs.getTimestamp("readtime"));
        message.setPublishStatus(ActStatus.getByCode(rs.getString("pubstatus")));
        message.setPublishTime(rs.getTimestamp("pubtime"));
        message.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        message.setCreateDate(rs.getTimestamp("createdate"));
        message.setCreateIp(rs.getString("createip"));
        message.setCreateUserId(rs.getString("createuserid"));
        message.setModifyDate(rs.getTimestamp("modifydate"));
        message.setModifyIp(rs.getString("modifyip"));
        message.setModifyUserId(rs.getString("modifyuserid"));
        return message;

    }
}
