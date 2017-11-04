package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.advertise.ActivityDevice;
import com.enjoyf.platform.service.advertise.AgentCode;
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
public abstract class AbstractActivityDeviceAccessor extends AbstractBaseTableAccessor<ActivityDevice> implements ActivityDeviceAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractActivityDeviceAccessor.class);

    private static final String KEY_TABLE_NAME = "activity_device";

//    private String deviceId;
//    //    private IntValidStatus status = IntValidStatus.VALIDING;//ing--is init valid--is actvity device
//    private String agentId;
//    private AgentCode agentCode;
//    private Date createTime;
//    private String createIp;
//    private Date modifyTime;
//    private String modifyIp;

    @Override
    public ActivityDevice insert(ActivityDevice activityDevice, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, activityDevice.getDeviceId());
            pstmt.setString(2, activityDevice.getAgentId());
            pstmt.setString(3, activityDevice.getAgentCode().getCode());
            pstmt.setTimestamp(4, new Timestamp(activityDevice.getCreateTime().getTime()));
            pstmt.setString(5, activityDevice.getCreateIp());
            pstmt.setTimestamp(6, new Timestamp(activityDevice.getModifyTime().getTime()));
            pstmt.setString(7, activityDevice.getModifyIp());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return activityDevice;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "device_id,agent_id,agent_code,create_time,create_ip,modify_time,modify_ip)VALUES(?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("DeviceEvent insert sql" + sql);
        }
        return sql;
    }

    @Override
    public ActivityDevice get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ActivityDevice> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ActivityDevice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected ActivityDevice rsToObject(ResultSet rs) throws SQLException {

        ActivityDevice returnObject = new ActivityDevice();

        returnObject.setDeviceId(rs.getString("device_id"));
        returnObject.setAgentId(rs.getString("agent_id"));
        returnObject.setAgentCode(AgentCode.getByCode(rs.getString("agent_code")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setCreateIp(rs.getString("create_ip"));
        returnObject.setModifyTime(new Date(rs.getTimestamp("modify_time").getTime()));
        returnObject.setModifyIp(rs.getString("modify_ip"));

        return returnObject;
    }
}

