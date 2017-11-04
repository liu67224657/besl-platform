package com.enjoyf.platform.db.advertise.app;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublish;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishField;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishType;
import com.enjoyf.platform.service.advertise.app.PublishParam;
import com.enjoyf.platform.service.joymeapp.AppChannelType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  2014/6/9 15:12
 * Description:
 */
public abstract class AbstractAdvertisePublishAccessor extends AbstractBaseTableAccessor<AppAdvertisePublish> implements AppAdvertisePublishAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractAdvertisePublishAccessor.class);
    private static final String KEY_TABLE_NAME = "app_publish";

    @Override
    public AppAdvertisePublish insert(AppAdvertisePublish publish, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            //publish_id, publish_name, publish_desc,advertise_id, start_time,
            //end_time, create_time, create_user, create_ip, publish_type,publish_param,remove_status
            pstmt.setLong(1, publish.getPublishId());
            pstmt.setString(2, publish.getPublishName());
            pstmt.setString(3, publish.getPublishDesc());
            pstmt.setLong(4, publish.getAdvertiseId());
            if (publish.getStartTime() == null) {
                pstmt.setNull(5, Types.TIMESTAMP);
            } else {
                pstmt.setTimestamp(5, new Timestamp(publish.getStartTime().getTime()));
            }
            if (publish.getEndTime() == null) {
                pstmt.setNull(6, Types.TIMESTAMP);
            } else {
                pstmt.setTimestamp(6, new Timestamp(publish.getEndTime().getTime()));
            }
            pstmt.setTimestamp(7, new Timestamp(publish.getCreateTime() == null ? System.currentTimeMillis() : publish.getCreateTime().getTime()));
            pstmt.setString(8, publish.getCreateUser());
            pstmt.setString(9, publish.getCreatIp());
            pstmt.setInt(10, publish.getPublishType().getCode());
            pstmt.setString(11, publish.getPublishParam() == null ? null : publish.getPublishParam().toJson());
            pstmt.setString(12, publish.getRemoveStatus().getCode());
            pstmt.setString(13, publish.getAppkey());
            pstmt.setString(14, publish.getChannel() == null ? "" : publish.getChannel().getCode());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                publish.setPublishId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return publish;
    }

    @Override
    public List<AppAdvertisePublish> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AppAdvertisePublish> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public AppAdvertisePublish get(long publishId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(AppAdvertisePublishField.PUBLISH_ID, publishId)), conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    private String getInsertSql() {
        //publish_id, publish_name, publish_desc,advertise_id, start_time,
        //end_time, create_time, create_user, create_ip, publish_type,publish_param,remove_status
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (publish_id, publish_name, publish_desc,advertise_id, start_time, end_time, create_time, create_user, create_ip, publish_type,publish_param,remove_status,appkey,channel) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;

    }

    @Override
    protected AppAdvertisePublish rsToObject(ResultSet rs) throws SQLException {
        AppAdvertisePublish publish = new AppAdvertisePublish();

        publish.setPublishId(rs.getLong("publish_id"));
        publish.setPublishName(rs.getString("publish_name"));
        publish.setPublishDesc(rs.getString("publish_desc"));
        publish.setAdvertiseId(rs.getLong("advertise_id"));
        publish.setStartTime(rs.getTimestamp("start_time"));
        publish.setEndTime(rs.getTimestamp("end_time"));
        publish.setCreateTime(rs.getTimestamp("create_time"));
        publish.setCreateUser(rs.getString("create_user"));
        publish.setCreatIp(rs.getString("create_ip"));
        publish.setPublishType(AppAdvertisePublishType.getByCode(rs.getInt("publish_type")));
        publish.setPublishParam(PublishParam.parse(rs.getString("publish_param")));
        publish.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        publish.setAppkey(rs.getString("appkey"));
        publish.setChannel(AppChannelType.getByCode(rs.getString("channel")));

        return publish;
    }
}
