package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.message.SocialNotice;
import com.enjoyf.platform.service.message.SocialNoticeField;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-6
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialNoticeAccessor extends AbstractBaseTableAccessor<SocialNotice> implements SocialNoticeAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialNoticeAccessor.class);

    private static final String KEY_TABLE_NAME = "social_notice_";
    private static final int TABLE_NUM = 10;

    @Override
    public SocialNotice insert(SocialNotice socialNotice, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(socialNotice.getOwnUno()), Statement.RETURN_GENERATED_KEYS);
            //ownuno,description,agreecount,replycount,noticecount,removestatus,createdate,readdate
            pstmt.setString(1, socialNotice.getOwnUno() == null ? "" : socialNotice.getOwnUno());
            pstmt.setString(2, socialNotice.getDescription() == null ? "" : socialNotice.getDescription());
            pstmt.setInt(3, socialNotice.getAgreeCount());
            pstmt.setInt(4, socialNotice.getReplyCount());
            pstmt.setInt(5, socialNotice.getNoticeCount());
            pstmt.setString(6, socialNotice.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : socialNotice.getRemoveStatus().getCode());
            pstmt.setTimestamp(7, new Timestamp(socialNotice.getCreateDate() == null ? System.currentTimeMillis() : socialNotice.getCreateDate().getTime()));
            pstmt.setTimestamp(8, new Timestamp(socialNotice.getReadAgreeDate() == null ? System.currentTimeMillis() : socialNotice.getReadAgreeDate().getTime()));
            pstmt.setTimestamp(9, new Timestamp(socialNotice.getReadReplyDate() == null ? System.currentTimeMillis() : socialNotice.getReadReplyDate().getTime()));
            pstmt.setTimestamp(10, new Timestamp(socialNotice.getReadNoticeDate() == null ? System.currentTimeMillis() : socialNotice.getReadNoticeDate().getTime()));
            pstmt.setInt(11, socialNotice.getHotCount());
            pstmt.setInt(12, socialNotice.getFocusCount());
            pstmt.setTimestamp(13, new Timestamp(socialNotice.getReadHotDate() == null ? System.currentTimeMillis() : socialNotice.getReadHotDate().getTime()));
            pstmt.setTimestamp(14, new Timestamp(socialNotice.getReadFocusDate() == null ? System.currentTimeMillis() : socialNotice.getReadFocusDate().getTime()));


            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                socialNotice.setNoticeId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + " insert occur SQLException.e", e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialNotice;
    }

    @Override
    public SocialNotice get(String ownUno, Connection conn) throws DbException {
        return super.get(getTableName(ownUno), new QueryExpress().add(QueryCriterions.eq(SocialNoticeField.OWN_UNO, ownUno)), conn);
    }

    @Override
    public int update(String ownUno, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(getTableName(ownUno), updateExpress, new QueryExpress().add(QueryCriterions.eq(SocialNoticeField.OWN_UNO, ownUno)), conn);
    }

    private String getInsertSql(String ownUno) {
        //ownuno,description,agreecount,replycount,noticecount,removestatus,createdate,readdate
        String sql = "INSERT INTO " + getTableName(ownUno) + "(ownuno,description,agreecount,replycount,noticecount,removestatus,createdate,readagreedate,readreplydate,readnoticedate,hotcount,focuscount,readhotdate,readfocusdate) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insert SocialNotice sql:" + sql);
        }
        return sql;
    }

    @Override
    protected SocialNotice rsToObject(ResultSet rs) throws SQLException {
        SocialNotice socialNotice = new SocialNotice();
        socialNotice.setNoticeId(rs.getLong("noticeid"));
        socialNotice.setOwnUno(rs.getString("ownuno"));
        socialNotice.setDescription(rs.getString("description"));
        socialNotice.setAgreeCount(rs.getInt("agreecount"));
        socialNotice.setReplyCount(rs.getInt("replycount"));
        socialNotice.setNoticeCount(rs.getInt("noticecount"));
        socialNotice.setHotCount(rs.getInt("hotcount"));
        socialNotice.setFocusCount(rs.getInt("focuscount"));
        socialNotice.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        socialNotice.setCreateDate(rs.getTimestamp("createdate"));
        socialNotice.setReadAgreeDate(rs.getTimestamp("readagreedate"));
        socialNotice.setReadReplyDate(rs.getTimestamp("readreplydate"));
        socialNotice.setReadNoticeDate(rs.getTimestamp("readnoticedate"));
        socialNotice.setReadHotDate(rs.getTimestamp("readhotdate"));
        socialNotice.setReadFocusDate(rs.getTimestamp("readfocusdate"));
        return socialNotice;
    }

    private String getTableName(String uno) {
        return KEY_TABLE_NAME + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }
}
