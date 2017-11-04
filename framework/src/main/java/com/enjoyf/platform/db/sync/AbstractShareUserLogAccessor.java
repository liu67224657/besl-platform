package com.enjoyf.platform.db.sync;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.sync.*;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-4
 * Time: 下午4:06
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractShareUserLogAccessor extends AbstractBaseTableAccessor<ShareUserLog> implements ShareUserLogAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractShareUserLogAccessor.class);
    private static final String KEY_TABLE_NAME = "share_user_log_";
    private static final String KEY_TABLE_SUFFIX_FMT = "yyyyMM";

    @Override
    public ShareUserLog insert(ShareUserLog shareUserLog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {

            long now = System.currentTimeMillis();

            //user_no, share_id, account_domain, sharedate, sharetime ,shareType
            pstmt = conn.prepareStatement(getInsertSql(shareUserLog.getShareDate()));
            pstmt.setString(1, shareUserLog.getProfileUno());
            pstmt.setLong(2, shareUserLog.getShareBaseInfoId());
            pstmt.setString(3, shareUserLog.getLoginDomain().getCode());
            pstmt.setTimestamp(4, new Timestamp(shareUserLog.getShareDate() == null ? now : shareUserLog.getShareDate().getTime()));
            pstmt.setDate(5, new java.sql.Date(shareUserLog.getShareTime() == null ? now : shareUserLog.getShareTime().getTime()));
            pstmt.setInt(6, shareUserLog.getShareType().getCode());
            pstmt.setString(7, shareUserLog.getShareurl());


            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ShareTopic, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return shareUserLog;
    }

    @Override
    protected ShareUserLog rsToObject(ResultSet rs) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getInsertSql(Date date) {
        String sql = "INSERT INTO " + getTableName(date) + " (uno, share_id, account_domain, sharedate, sharetime,sharetype,shareurl) VALUES(?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("ShareUserLog INSERT Script:" + sql);
        }
        return sql;
    }

    private String getTableName(Date date) {
        return KEY_TABLE_NAME + TableUtil.getTableDateSuffix(date, KEY_TABLE_SUFFIX_FMT);
    }

}
