package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.profile.ProfileClientMobileDevice;
import com.enjoyf.platform.util.PageRows;
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
 * User: yongmingxu
 * Date: 12-6-2
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractProfileClientMobileDeviceAccessor extends AbstractBaseTableAccessor<ProfileClientMobileDevice> implements ProfileClientMobileDeviceAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractProfileClientMobileDeviceAccessor.class);

    protected static final String KEY_TABLE_NAME = "profile_client_mobile_device";

    @Override
    public ProfileClientMobileDevice insert(ProfileClientMobileDevice mobileDevice, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, mobileDevice.getUno());
            pstmt.setString(2, mobileDevice.getClientId());
            pstmt.setString(3, mobileDevice.getClientToken());
            pstmt.setInt(4, mobileDevice.getPlatform().getCode());
            pstmt.setString(5, mobileDevice.getAppId());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                mobileDevice.setDeviceId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert ClientDevice, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return mobileDevice;
    }

    @Override
    public ProfileClientMobileDevice get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ProfileClientMobileDevice> query(QueryExpress queryExpress, Pagination pagination, Connection connection) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, pagination, connection);
    }

    @Override
    public List<ProfileClientMobileDevice> query(QueryExpress queryExpress, Connection connection) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, connection);
    }

    @Override
    public int modify(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return modify(updateExpress, queryExpress, conn);
    }

    @Override
    protected ProfileClientMobileDevice rsToObject(ResultSet rs) throws SQLException {
        ProfileClientMobileDevice entry = new ProfileClientMobileDevice();
        entry.setDeviceId(rs.getLong("device_id"));
        entry.setUno(rs.getString("uno"));
        entry.setAppId(rs.getString("app_id"));
        entry.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        entry.setClientId(rs.getString("client_id"));
        entry.setClientToken(rs.getString("client_token"));
        entry.setLastMsgId(rs.getLong("last_msg_id"));
        return entry;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (uno, client_id, client_token, platform, app_id) VALUES (?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("ClientDevice insert sql:" + insertSql);
        }
        return insertSql;
    }
}
