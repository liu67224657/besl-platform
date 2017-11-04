package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPushMessageAccessor extends AbstractBaseTableAccessor<PushMessage> implements PushMessageAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPushMessageAccessor.class);

    //    private static final String KEY_SEQUENCE_NAME = "SEQ_PUSHMESSAGE_ID";
    protected static final String KEY_TABLE_NAME = "push_message";

    @Override
    public PushMessage insert(PushMessage pushMessage, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            //appkey, msgicon, msgsubject, shortmessage, options, removestatus, createuserid,createdate
//            pstmt.setLong(1, pushMessage.getPushMsgId());
            pstmt.setString(1, pushMessage.getAppKey());
            pstmt.setString(2, pushMessage.getMsgIcon());
            pstmt.setString(3, pushMessage.getMsgSubject());
            pstmt.setString(4, pushMessage.getShortMessage());
            pstmt.setString(5, pushMessage.getOptions() == null ? "" : pushMessage.getOptions().toJson());
            pstmt.setString(6, pushMessage.getPushStatus().getCode());
            pstmt.setString(7, pushMessage.getCreateUserid());
            pstmt.setTimestamp(8, new Timestamp(pushMessage.getCreateDate() == null ? System.currentTimeMillis() : pushMessage.getCreateDate().getTime()));
            pstmt.setInt(9, pushMessage.getAppPlatform().getCode());
            pstmt.setInt(10, pushMessage.getPushListType().getCode());
            pstmt.setString(11, pushMessage.getUnos() == null ? "" : pushMessage.getUnos());
            pstmt.setInt(12, pushMessage.getPushChannel() == null ? -1 : pushMessage.getPushChannel().getCode());
            pstmt.setString(13, pushMessage.getAppChannel() == null ? "" : pushMessage.getAppChannel());
            pstmt.setString(14, pushMessage.getAppVersion() == null ? "" : pushMessage.getAppVersion());
            pstmt.setString(15, pushMessage.getTags() == null ? "" : pushMessage.getTags());
            pstmt.setString(16, pushMessage.getDevices() == null ? "" : pushMessage.getDevices());
            pstmt.setString(17, pushMessage.getSound() == null ? "" : pushMessage.getSound());
            pstmt.setInt(18, pushMessage.getBadge());
            pstmt.setTimestamp(19, pushMessage.getSendDate() == null ? null : new Timestamp(pushMessage.getSendDate().getTime()));
            pstmt.setString(20, pushMessage.getSendStatus() == null ? ActStatus.UNACT.getCode() : pushMessage.getSendStatus().getCode());
            pstmt.setString(21, pushMessage.getE_sendStatus() == null ? ActStatus.UNACT.getCode() : pushMessage.getE_sendStatus().getCode());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                pushMessage.setPushMsgId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert PushMessage, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return pushMessage;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (appkey, msgicon, msgsubject, shortmessage, " +
                "options, pushstatus, createuserid,createdate,platform,pushlisttype," +
                "unos,pushchannel,appchannel,appversion,tags,devices,msgsound,msgbadge,senddate,sendstatus,e_sendstatus) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PushMessage INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    public PushMessage get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<PushMessage> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<PushMessage> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Long> queryLastastMsgIdByPlatform(AppPlatform appPlatform, Connection conn) throws DbException {

        List<Long> returnValue = new ArrayList<Long>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT MAX(pushmsgid) as lastid,appkey FROM push_message WHERE platform= ? AND pushstatus= ? GROUP BY APPKEY";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            //pushmsgid, appkey, msgicon, msgsubject, shortmessage, options, pushstatus, createuserid,createdate
            pstmt.setInt(1, appPlatform.getCode());
            pstmt.setString(2, ActStatus.ACTING.getCode());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rs.getLong("lastid"));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryLastastMsgIdByPlatform, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }


    @Override
    public Long getLastestPushMessageByAppKeyPlatform(String appKey, AppPlatform appPlatform, String version, Connection conn) throws DbException {
        Long returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT MAX(pushmsgid) as lastid FROM push_message WHERE appkey=? AND platform= ? AND appversion=? AND pushstatus= ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, appKey);
            pstmt.setInt(2, appPlatform.getCode());
            pstmt.setString(3, version);
            pstmt.setString(4, ActStatus.ACTING.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rs.getLong("lastid");
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryLastastMsgIdByPlatform, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public boolean remove(Long msgId, Connection conn) throws DbException {
        if (msgId == null) {
            return false;
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM " + KEY_TABLE_NAME + " WHERE pushmsgid=?";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, msgId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On queryLastastMsgIdByPlatform, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return true;
    }

    @Override
    protected PushMessage rsToObject(ResultSet rs) throws SQLException {
        //pushmsgid, appkey, msgicon, msgsubject, shortmessage, options, pushstatus,removeDate, createuserid,createdate
        PushMessage returnObj = new PushMessage();
        returnObj.setPushMsgId(rs.getLong("pushmsgid"));
        returnObj.setAppKey(rs.getString("appkey"));
        returnObj.setMsgIcon(rs.getString("msgicon"));
        returnObj.setMsgSubject(rs.getString("msgsubject"));
        returnObj.setShortMessage(rs.getString("shortmessage"));
        String option = rs.getString("options");
        if (!StringUtil.isEmpty(option)) {
            returnObj.setOptions(PushMessageOptions.parse(rs.getString("options")));
        }
        returnObj.setPushStatus(ActStatus.getByCode(rs.getString("pushstatus")));
        returnObj.setSendDate(rs.getTimestamp("senddate"));
        returnObj.setAppPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        returnObj.setCreateUserid(rs.getString("createuserid"));
        returnObj.setCreateDate(new Date(rs.getTimestamp("createdate").getTime()));
        returnObj.setPushListType(PushListType.getByCode(rs.getInt("pushlisttype")));
        //unos,pushchannel,appchannel,appversion,tags,devices,msgsound
        returnObj.setPushChannel(AppPushChannel.getByCode(rs.getInt("pushchannel")));
        returnObj.setAppChannel(rs.getString("appchannel"));
        returnObj.setAppVersion(rs.getString("appversion"));
        returnObj.setUnos(rs.getString("unos"));
        returnObj.setDevices(rs.getString("devices"));
        returnObj.setTags(rs.getString("tags"));
        returnObj.setSound(rs.getString("msgsound"));
        returnObj.setBadge(rs.getInt("msgbadge"));
        returnObj.setModifyDate(rs.getTimestamp("modifydate"));
        returnObj.setModifyUserId(rs.getString("modifyuserid"));
        returnObj.setSendStatus(ActStatus.getByCode(rs.getString("sendstatus")));
        returnObj.setE_sendStatus(ActStatus.getByCode(rs.getString("e_sendstatus")));
        returnObj.setEnterpriseType(AppEnterpriserType.getByCode(rs.getInt("enterprisetype")));

        return returnObj;
    }

}
