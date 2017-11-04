package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppPushChannel;
import com.enjoyf.platform.service.message.ClientDevice;
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
public abstract class AbstractClientDeviceAccessor extends AbstractBaseTableAccessor<ClientDevice> implements ClientDeviceAccessor {

	//
	private static final Logger logger = LoggerFactory.getLogger(AbstractClientDeviceAccessor.class);

	protected static final String KEY_TABLE_NAME = "profile_client_mobile_device";

	@Override
	public ClientDevice insert(ClientDevice mobileDevice, Connection conn) throws DbException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, mobileDevice.getUno());
			pstmt.setString(2, mobileDevice.getClientId());
			pstmt.setString(3, mobileDevice.getClientToken());
			pstmt.setInt(4, mobileDevice.getPlatform().getCode());
			pstmt.setString(5, mobileDevice.getAppId());
			//push_channel,app_version,app_channel,app_tags,adv_id
			pstmt.setInt(6, mobileDevice.getPushChannel() == null ? AppPushChannel.DEFAULT.getCode() : mobileDevice.getPushChannel().getCode());
			pstmt.setString(7, mobileDevice.getAppVersion() == null ? "" : mobileDevice.getAppVersion());
			pstmt.setString(8, mobileDevice.getAppChannel() == null ? "" : mobileDevice.getAppChannel());
			pstmt.setString(9, mobileDevice.getTags() == null ? "" : mobileDevice.getTags());
			pstmt.setString(10, mobileDevice.getAdvId() == null ? "" : mobileDevice.getAdvId());
			pstmt.setString(11, mobileDevice.getIp() == null ? "" : mobileDevice.getIp());
			pstmt.setInt(12, mobileDevice.getEnterpriserType().getCode());
            pstmt.setString(13, mobileDevice.getProfileId() == null ? "" : mobileDevice.getProfileId());

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
	public ClientDevice get(QueryExpress queryExpress, Connection conn) throws DbException {
		return get(KEY_TABLE_NAME, queryExpress, conn);
	}

	@Override
	public List<ClientDevice> query(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, conn);
	}

	@Override
	public List<ClientDevice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);

	}

	@Override
	public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
		return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
	}

	@Override
	protected ClientDevice rsToObject(ResultSet rs) throws SQLException {
		ClientDevice entry = new ClientDevice();
		entry.setDeviceId(rs.getLong("device_id"));
		entry.setUno(rs.getString("uno"));
		entry.setAppId(rs.getString("app_id"));
		entry.setPlatform(AppPlatform.getByCode(rs.getInt("platform")));
		entry.setClientId(rs.getString("client_id"));
		entry.setClientToken(rs.getString("client_token"));
		entry.setLastMsgId(rs.getLong("lastmsgid"));
		entry.setPushChannel(AppPushChannel.getByCode(rs.getInt("push_channel")));
		entry.setAppVersion(rs.getString("app_version"));
		entry.setAppChannel(rs.getString("app_channel"));
		entry.setTags(rs.getString("app_tags"));
		entry.setAdvId(rs.getString("adv_id"));
		entry.setIp(rs.getString("client_ip"));
		entry.setEnterpriserType(AppEnterpriserType.getByCode(rs.getInt("enterpriser")));
        entry.setProfileId(rs.getString("profileid"));
		return entry;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private String getInsertSql() throws DbException {
		String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (uno, client_id, client_token, platform, app_id," +
				"push_channel,app_version,app_channel,app_tags,adv_id,client_ip,enterpriser,profileid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (logger.isDebugEnabled()) {
			logger.debug("ClientDevice insert sql:" + insertSql);
		}
		return insertSql;
	}
}
