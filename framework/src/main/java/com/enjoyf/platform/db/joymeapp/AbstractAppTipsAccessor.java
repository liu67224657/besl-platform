package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.AppTips;
import com.enjoyf.platform.service.joymeapp.AppTipsType;
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
 * Date: 14-8-19
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAppTipsAccessor extends AbstractBaseTableAccessor<AppTips> implements AppTipsAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAppTipsAccessor.class);
    private static final String KEY_TABLE_NAME = "app_tips";

    @Override
    public AppTips insert(AppTips appTips, Connection conn) throws DbException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, appTips.getTipsTitle() == null ? "" : appTips.getTipsTitle());
            pstmt.setString(2, appTips.getTipsDescription() == null ? "" : appTips.getTipsDescription());
            pstmt.setString(3, appTips.getTipsPic() == null ? "" : appTips.getTipsPic());
            pstmt.setString(4, appTips.getTipsUrl() == null ? "" : appTips.getTipsUrl());
            pstmt.setInt(5, appTips.getTipsType() == null ? AppTipsType.DEFAULT.getCode() : appTips.getTipsType().getCode());
            pstmt.setString(6, appTips.getAppId());
            pstmt.setInt(7, appTips.getPlatform());
            pstmt.setString(8, appTips.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : appTips.getRemoveStatus().getCode());
            pstmt.setTimestamp(9, new Timestamp(appTips.getCreateDate() == null ? System.currentTimeMillis() : appTips.getCreateDate().getTime()));
            pstmt.setString(10, appTips.getCreateIp() == null ? "" : appTips.getCreateIp());
            pstmt.setString(11, appTips.getCreateUserId() == null ? "" : appTips.getCreateUserId());
            pstmt.setTimestamp(12, appTips.getUpdateTime() == null ? null : new Timestamp(appTips.getUpdateTime().getTime()));

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                appTips.setTipsId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + " insert occur Exception.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return appTips;
    }

    @Override
    public AppTips get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AppTips> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AppTips> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected AppTips rsToObject(ResultSet rs) throws SQLException {
        AppTips appTips = new AppTips();
        appTips.setTipsId(rs.getLong("tipsid"));
        appTips.setTipsTitle(rs.getString("tipstitle"));
        appTips.setTipsDescription(rs.getString("tipsdescription"));
        appTips.setTipsPic(rs.getString("tipspic"));
        appTips.setTipsUrl(rs.getString("tipsurl"));
        appTips.setTipsType(AppTipsType.getByCode(rs.getInt("tipstype")));
        appTips.setAppId(rs.getString("appid"));
        appTips.setPlatform(rs.getInt("platform"));
        appTips.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        appTips.setCreateDate(rs.getTimestamp("createdate"));
        appTips.setCreateIp(rs.getString("createip"));
        appTips.setCreateUserId(rs.getString("createuserid"));
        appTips.setModifyDate(rs.getTimestamp("modifydate"));
        appTips.setModifyIp(rs.getString("modifyip"));
        appTips.setModifyUserId(rs.getString("modifyuserid"));
        appTips.setUpdateTime(rs.getTimestamp("updatetime"));
        return appTips;
    }

    private String getSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(tipstitle,tipsdescription,tipspic,tipsurl," +
                "tipstype,appid,platform,removestatus,createdate,createip,createuserid,updatetime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insert sql:" + sql);
        }
        return sql;
    }
}
