package com.enjoyf.platform.db.advertise.app;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseField;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
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
public abstract class AbstractAdvertiseAccessor extends AbstractBaseTableAccessor<AppAdvertise> implements AppAdvertiseAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractAdvertiseAccessor.class);
    private static final String KEY_TABLE_NAME = "app_advertise";

    @Override
    public AppAdvertise insert(AppAdvertise appAdvertise, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            //advertise_id, advertise_name, advertise_desc,advertise_url, 
            // advertise_picurl1, advertise_picurl2, advertise_platform, create_time, create_user, create_ip
            pstmt.setLong(1, appAdvertise.getAdvertiseId());
            pstmt.setString(2, appAdvertise.getAdvertiseName());
            pstmt.setString(3, appAdvertise.getAdvertiseDesc());
            pstmt.setString(4, appAdvertise.getUrl());
            pstmt.setString(5, appAdvertise.getPicUrl1());
            pstmt.setString(6, appAdvertise.getPicUrl2());
            pstmt.setInt(7, appAdvertise.getAppPlatform().getCode());
            pstmt.setTimestamp(8, new Timestamp(appAdvertise.getCreateTime() == null ? System.currentTimeMillis() : appAdvertise.getCreateTime().getTime()));
            pstmt.setString(9, appAdvertise.getCreateUser());
            pstmt.setString(10, appAdvertise.getCreatIp());
            pstmt.setString(11, appAdvertise.getRemoveStatus().getCode());
            pstmt.setInt(12, appAdvertise.getAppAdvertiseRedirectType() == null ? 0 : appAdvertise.getAppAdvertiseRedirectType());
            pstmt.setInt(13, appAdvertise.getAppAdvertiseType() == null ? 0 : appAdvertise.getAppAdvertiseType().getCode());
            pstmt.setString(14, StringUtil.isEmpty(appAdvertise.getExtstring()) ? "" : appAdvertise.getExtstring());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                appAdvertise.setAdvertiseId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return appAdvertise;
    }

    @Override
    public List<AppAdvertise> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public AppAdvertise get(long appAdvertiseId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(AppAdvertiseField.ADVERTISE_ID, appAdvertiseId)), conn);
    }

    @Override
    public List<AppAdvertise> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    private String getInsertSql() {
        //advertise_id, advertise_name, advertise_desc,advertise_url,
        // advertise_picurl1, advertise_picurl2, advertise_platform, create_time, create_user, create_ip,remove_status
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (advertise_id, advertise_name, advertise_desc,advertise_url, advertise_picurl1, advertise_picurl2, advertise_platform, create_time, create_user, create_ip,remove_status,redirect_type,advertise_type,extstring) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;
    }

    @Override
    protected AppAdvertise rsToObject(ResultSet rs) throws SQLException {
        AppAdvertise advertise = new AppAdvertise();

        advertise.setAdvertiseId(rs.getLong("advertise_id"));
        advertise.setAdvertiseName(rs.getString("advertise_name"));
        advertise.setAdvertiseDesc(rs.getString("advertise_desc"));
        advertise.setUrl(rs.getString("advertise_url"));
        advertise.setPicUrl1(rs.getString("advertise_picurl1"));
        advertise.setPicUrl2(rs.getString("advertise_picurl2"));
        advertise.setAppPlatform(AppPlatform.getByCode(rs.getInt("advertise_platform")));
        advertise.setCreateTime(rs.getTimestamp("create_time"));
        advertise.setCreateUser(rs.getString("create_user"));
        advertise.setCreatIp(rs.getString("create_ip"));
        advertise.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        advertise.setAppAdvertiseRedirectType(rs.getInt("redirect_type"));


        advertise.setAppAdvertiseType(AppAdvertiseType.getByCode(rs.getInt("advertise_type")));
        advertise.setExtstring(rs.getString("extstring"));
        return advertise;
    }
}
