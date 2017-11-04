package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.InviteDomain;
import com.enjoyf.platform.service.social.SocialInviteDetail;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-20
 * Time: 下午5:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialInviteDetailAccessor extends AbstractBaseTableAccessor<SocialInviteDetail> implements SocialInviteDetailAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialInviteDetailAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "SOCIAL_INVITE_DETAIL";

    @Override
    public SocialInviteDetail insert(SocialInviteDetail inviteDetail, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //INVITEID,GID,INVITEUNO,INVITEDOMAIN,DIRECTUNO,CREATEDATE,CREATEIP
            pstmt.setLong(1, inviteDetail.getInviteId());
            pstmt.setString(2, StringUtil.isEmpty(inviteDetail.getGid()) ? null : inviteDetail.getGid());
            pstmt.setString(3, inviteDetail.getInviteUno());
            pstmt.setString(4, inviteDetail.getInviteDomain().getCode());
            pstmt.setString(5, inviteDetail.getDirectUno());
            pstmt.setTimestamp(6, new Timestamp(inviteDetail.getCreateDate() == null ? System.currentTimeMillis() : inviteDetail.getCreateDate().getTime()));
            pstmt.setString(7, inviteDetail.getCreateIp());

            pstmt.executeUpdate();

            return inviteDetail;
        } catch (SQLException e) {
            GAlerter.lab("On insert SocialInviteInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public SocialInviteDetail get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialInviteDetail> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<SocialInviteDetail> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected SocialInviteDetail rsToObject(ResultSet rs) throws SQLException {
        SocialInviteDetail inviteDetail = new SocialInviteDetail();

        inviteDetail.setInviteId(rs.getLong("INVITEID"));
        inviteDetail.setInviteUno(rs.getString("INVITEUNO"));
        inviteDetail.setGid(rs.getString("GID"));
        inviteDetail.setInviteDomain(InviteDomain.getByCode(rs.getString("INVITEDOMAIN")));
        inviteDetail.setDirectUno(rs.getString("DIRECTUNO"));
        inviteDetail.setCreateDate(rs.getTimestamp("CREATEDATE"));
        inviteDetail.setCreateIp(rs.getString("CREATEIP"));

        return inviteDetail;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + " (INVITEID,GID,INVITEUNO,INVITEDOMAIN,DIRECTUNO,CREATEDATE,CREATEIP) VALUES(?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug(" insert sql script:" + sql);
        }

        return sql;
    }

}
