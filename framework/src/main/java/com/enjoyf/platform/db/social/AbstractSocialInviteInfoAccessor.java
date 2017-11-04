package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.InviteDomain;
import com.enjoyf.platform.service.social.SocialInviteInfo;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-20
 * Time: 下午5:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialInviteInfoAccessor extends AbstractSequenceBaseTableAccessor<SocialInviteInfo> implements SocialInviteInfoAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialInviteDetailAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "SOCIAL_INVITE_INFO";
    protected static final String KEY_SEQUENCE_NAME = "SEQ_SOCIAL_INVITE_ID";

    @Override
    public SocialInviteInfo insert(SocialInviteInfo socialInviteInfo, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSqlScript());

            socialInviteInfo.setInviteId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt.setLong(1, socialInviteInfo.getInviteId());
            pstmt.setString(2, socialInviteInfo.getSrcUno());
            pstmt.setString(3, socialInviteInfo.getInviteDomain().getCode());
            pstmt.setString(4, socialInviteInfo.getDestId());
            pstmt.setTimestamp(5, new Timestamp(socialInviteInfo.getCreateDate() == null ? System.currentTimeMillis() : socialInviteInfo.getCreateDate().getTime()));
            pstmt.setString(6, socialInviteInfo.getCreateIp());

            pstmt.executeUpdate();

            return socialInviteInfo;
        } catch (SQLException e) {
            GAlerter.lab("On insert SocialInviteInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public SocialInviteInfo get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected SocialInviteInfo rsToObject(ResultSet rs) throws SQLException {
        SocialInviteInfo socialInviteInfo = new SocialInviteInfo();
        socialInviteInfo.setInviteId(rs.getLong("INVITEID"));
        socialInviteInfo.setSrcUno(rs.getString("SRCUNO"));
        socialInviteInfo.setDestId(rs.getString("DESTID"));
        socialInviteInfo.setInviteDomain(InviteDomain.getByCode(rs.getString("INVITEDOMAIN")));
        socialInviteInfo.setCreateDate(rs.getTimestamp("CREATEDATE"));
        socialInviteInfo.setCreateIp(rs.getString("CREATEIP"));

        return socialInviteInfo;
    }

    private String getInsertSqlScript() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + " (INVITEID ,SRCUNO ,INVITEDOMAIN , DESTID ,CREATEDATE ,CREATEIP) VALUES(?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug(" insert sql script:" + sql);
        }

        return sql;
    }
}
