package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.PushMessageDevice;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-22
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPushMessageDeviceAccessor extends AbstractSequenceBaseTableAccessor<PushMessageDevice> implements PushMessageDeviceAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPushMessageDeviceAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "SEQ_PUSHMESSAGE_DEVICE_ID";
    protected static final String KEY_TABLE_NAME = "push_message_device";


    @Override
    public PushMessageDevice insert(PushMessageDevice device, Connection conn) throws DbException {

        PreparedStatement pstmt = null;

        try {
            device.setDeviceId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());

            //deviceid, clientid, clienttoken, platform, appkey,lastmsgid
            pstmt.setLong(1, device.getDeviceId());
            pstmt.setString(2, device.getClientId());
            pstmt.setString(3, device.getClientToken());
            pstmt.setInt(4, device.getPlatform().getCode());
            pstmt.setString(5, device.getAppKey());
            pstmt.setLong(6, device.getLastMsgId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert PushMessage, a SQLException occured." + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return device;
    }

    @Override
    protected PushMessageDevice rsToObject(ResultSet rs) throws SQLException {
        PushMessageDevice returnObj = new PushMessageDevice();

        returnObj.setDeviceId(rs.getLong("deviceid"));
        returnObj.setAppKey(rs.getString("appkey"));
        returnObj.setClientId(rs.getString("clientid"));
        returnObj.setClientToken(rs.getString("clienttoken"));

        returnObj.setLastMsgId(rs.getLong("lastmsgid"));
        returnObj.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));

        return returnObj;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (deviceid, clientid, clienttoken, platform, appkey, lastmsgid) VALUES (?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PushMessageAccessor INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    public PushMessageDevice get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<PushMessageDevice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
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


}
