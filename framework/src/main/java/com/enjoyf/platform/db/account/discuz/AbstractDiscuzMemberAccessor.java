/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.account.discuz;


import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.account.discuz.DiscuzMember;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public abstract class AbstractDiscuzMemberAccessor extends AbstractBaseTableAccessor<DiscuzMember> implements DiscuzMemberAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractDiscuzMemberAccessor.class);

    private static final String KEY_DISCUZ_TABLE_NAME = "pre_common_member";

    @Override
    public DiscuzMember insert(DiscuzMember discuzUser, Connection conn) throws DbException {
        return null;
    }

    @Override
    public DiscuzMember get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_DISCUZ_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected DiscuzMember rsToObject(ResultSet rs) throws SQLException {
        DiscuzMember discuzMember = new DiscuzMember();

        discuzMember.setUid(rs.getLong("uid"));
        discuzMember.setEmail(rs.getString("email"));
        discuzMember.setUserName(rs.getString("username"));
        discuzMember.setPassword(rs.getString("password"));
        discuzMember.setStatus(rs.getInt("status"));
        discuzMember.setEmailstatus(rs.getInt("emailstatus"));
        discuzMember.setVideophotostatus(rs.getInt("videophotostatus"));
        discuzMember.setAdminid(rs.getInt("adminid"));
        discuzMember.setGroupid(rs.getInt("groupid"));

        discuzMember.setGroupexpiry(rs.getInt("groupexpiry"));
        discuzMember.setExtgroupids(rs.getString("extgroupids"));
        discuzMember.setRegdate(rs.getInt("regdate"));
        discuzMember.setCredits(rs.getInt("credits"));
        discuzMember.setNotifysound(rs.getInt("notifysound"));
        discuzMember.setTimeoffset(rs.getString("timeoffset"));

        discuzMember.setNewpm(rs.getInt("newpm"));
        discuzMember.setNewprompt(rs.getInt("newprompt"));
        discuzMember.setAccessmasks(rs.getInt("accessmasks"));
        discuzMember.setAllowadmincp(rs.getInt("allowadmincp"));
        discuzMember.setOnlyacceptfriendpm(rs.getInt("onlyacceptfriendpm"));
        discuzMember.setConisbind(rs.getInt("conisbind"));
        discuzMember.setAccountUno(rs.getString("ACCOUNTUNO"));
        discuzMember.setUno(rs.getString("UNO"));

        return discuzMember;
    }
}
