package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-29
 * Time: 下午6:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAppDeploymentAccessor extends AbstractBaseTableAccessor<AppDeployment> implements AppDeploymentAccessor {
	private static final Logger logger = LoggerFactory.getLogger(AbstractAppDeploymentAccessor.class);

	private static final String KEY_TABLE_NAME = "app_deployment";

	@Override
	public AppDeployment insert(AppDeployment appDeployment, Connection conn) throws DbException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
//            app_key,platform,path_url,product_pwd,is_product," +
//                            "title,description,deployment_type,update_type,remove_status,create_date,create_ip," +
//                            "create_userid
			pstmt.setString(1, appDeployment.getAppkey() == null ? "" : appDeployment.getAppkey());
			pstmt.setInt(2, appDeployment.getAppPlatform() == null ? AppPlatform.IOS.getCode() : appDeployment.getAppPlatform().getCode());
			pstmt.setString(3, appDeployment.getPath() == null ? "" : appDeployment.getPath());
			pstmt.setString(4, appDeployment.getPassword() == null ? "" : appDeployment.getPassword());
			pstmt.setBoolean(5, appDeployment.getIsProduct());
			pstmt.setString(6, appDeployment.getTitle() == null ? "" : appDeployment.getTitle());
			pstmt.setString(7, appDeployment.getDescription() == null ? "" : appDeployment.getDescription());
			pstmt.setInt(8, appDeployment.getAppDeploymentType() == null ? 0 : appDeployment.getAppDeploymentType().getCode());
			pstmt.setInt(9, appDeployment.getAppVersionUpdateType() == null ? 0 : appDeployment.getAppVersionUpdateType().getCode());
			pstmt.setString(10, appDeployment.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : appDeployment.getRemoveStatus().getCode());
			pstmt.setTimestamp(11, new Timestamp(appDeployment.getCreateDate() == null ? System.currentTimeMillis() : appDeployment.getCreateDate().getTime()));
			pstmt.setString(12, appDeployment.getCreateIp() == null ? "" : appDeployment.getCreateIp());
			pstmt.setString(13, appDeployment.getCreateUserId() == null ? "" : appDeployment.getCreateUserId());
			pstmt.setString(14, appDeployment.getChannel() == null ? "" : appDeployment.getChannel().getCode());
			pstmt.setInt(15, appDeployment.getAppEnterpriserType() == null ? 0 : appDeployment.getAppEnterpriserType().getCode());

			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				appDeployment.setDeploymentId(rs.getLong(1));
			}
		} catch (SQLException e) {
			GAlerter.lab("On insert AppChannel, a SQLException occured:", e);
			throw new DbException(e);
		} finally {
			DataBaseUtil.closeResultSet(rs);
			DataBaseUtil.closeStatment(pstmt);
		}
		return appDeployment;
	}

	@Override
	public AppDeployment get(QueryExpress queryExpress, Connection conn) throws DbException {
		return super.get(KEY_TABLE_NAME, queryExpress, conn);
	}

	@Override
	public List<AppDeployment> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
		return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
	}

	@Override
	public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
		return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
	}

	@Override
	protected AppDeployment rsToObject(ResultSet rs) throws SQLException {
		AppDeployment appDeployment = new AppDeployment();
		appDeployment.setDeploymentId(rs.getLong("deployment_id"));
		appDeployment.setAppkey(rs.getString("app_key"));
		appDeployment.setAppPlatform(AppPlatform.getByCode(rs.getInt("platform")));
		appDeployment.setPath(rs.getString("path_url"));
		appDeployment.setPassword(rs.getString("product_pwd"));
		appDeployment.setIsProduct(rs.getBoolean("is_product"));
		appDeployment.setTitle(rs.getString("title"));
		appDeployment.setAppVersionUpdateType(AppVersionUpdateType.getByCode(rs.getInt("update_type")));
		appDeployment.setDescription(rs.getString("description"));
		appDeployment.setAppDeploymentType(AppDeploymentType.getByCode(rs.getInt("deployment_type")));
		appDeployment.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
		appDeployment.setCreateDate(rs.getTimestamp("create_date"));
		appDeployment.setCreateIp(rs.getString("create_ip"));
		appDeployment.setCreateUserId(rs.getString("create_userid"));
		appDeployment.setModifyDate(rs.getTimestamp("modify_date"));
		appDeployment.setModifyIp(rs.getString("modify_ip"));
		appDeployment.setModifyUserId(rs.getString("modify_userid"));
		appDeployment.setChannel(AppChannelType.getByCode(rs.getString("channel")));
		appDeployment.setAppEnterpriserType(AppEnterpriserType.getByCode(rs.getInt("enterpriser")));
		return appDeployment;
	}

	private String getInsertSql() {
		String sql = "INSERT INTO " + KEY_TABLE_NAME + "(app_key,platform,path_url,product_pwd,is_product," +
				"title,description,deployment_type,update_type,remove_status,create_date,create_ip," +
				"create_userid,channel,enterpriser) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (logger.isDebugEnabled()) {
			logger.debug(this.getClass().getName() + " AppDeployment insert sql:" + sql);
		}
		return sql;
	}
}
