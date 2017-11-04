package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.DeviceEvent;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/10
 * Description:
 */
public abstract class AbstractDeviceEventAccessor extends AbstractBaseTableAccessor<DeviceEvent> implements DeviceEventAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDeviceEventAccessor.class);

    private static final String KEY_TABLE_NAME = "_device_event";

    @Override
    public DeviceEvent insert(DeviceEvent deviceEvent, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, deviceEvent.getDeviceId());
            pstmt.setString(2, deviceEvent.getProfileId());
            pstmt.setString(3, deviceEvent.getAgentId());
            pstmt.setTimestamp(4, new Timestamp(deviceEvent.getCreateTime().getTime()));
            pstmt.setString(5, deviceEvent.getCreateIp());
            pstmt.setString(6, deviceEvent.getEventType());
            pstmt.setInt(7, deviceEvent.getEventValue());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return deviceEvent;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(device_id,profile_id,agent_id,create_time,create_ip,event_type,event_value,device_id,profile_id,agent_id,create_time,create_ip,event_type,event_value)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("DeviceEvent insert sql" + sql);
        }
        return sql;
    }

    @Override
    public DeviceEvent get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<DeviceEvent> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<DeviceEvent> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected DeviceEvent rsToObject(ResultSet rs) throws SQLException {

        DeviceEvent returnObject = new DeviceEvent();

        returnObject.setDeviceId(rs.getString("device_id"));
        returnObject.setProfileId(rs.getString("profile_id"));
        returnObject.setAgentId(rs.getString("agent_id"));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setCreateIp(rs.getString("create_ip"));
        returnObject.setEventType(rs.getString("event_type"));
        returnObject.setEventValue(rs.getInt("event_value"));


        return returnObject;
    }
}

