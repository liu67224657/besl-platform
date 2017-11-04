package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.ActivityTopMenu;
import com.enjoyf.platform.service.joymeapp.ActivityTopMenuParam;
import com.enjoyf.platform.service.joymeapp.AppTopMenuCategory;
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
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 下午12:51
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractActivityTopMenuAccessor extends AbstractBaseTableAccessor<ActivityTopMenu> implements ActivityTopMenuAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractActivityTopMenuAccessor.class);

    private static final String KEY_TABLE_NAME = "activity_top_menu";

    @Override
    public ActivityTopMenu insert(ActivityTopMenu activityTopMenu, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            //activity_menu_id,appkey,channel_id,platform,pic_url,menu_name,link_url,menu_type,menu_desc,is_new,is_hot,display_order,create_userid,create_date,create_ip,lastmodify_userid,lastmodify_date,lastmodify_ip,valid_status
            pstmt.setString(1, activityTopMenu.getAppKey() == null ? "" : activityTopMenu.getAppKey());
            pstmt.setLong(2, activityTopMenu.getChannelId() == null ? 0l : activityTopMenu.getChannelId());
            pstmt.setInt(3, activityTopMenu.getPlatform());
            pstmt.setString(4, activityTopMenu.getPicUrl());
            pstmt.setString(5, activityTopMenu.getMenuName());
            pstmt.setString(6, activityTopMenu.getLinkUrl());
            pstmt.setInt(7, activityTopMenu.getMenuType() == null ? -1 : activityTopMenu.getMenuType());
            pstmt.setString(8, activityTopMenu.getMenuDesc());
            pstmt.setBoolean(9, activityTopMenu.getIsNew());
            pstmt.setBoolean(10, activityTopMenu.getIsHot());
            pstmt.setInt(11, activityTopMenu.getDisplayOrder());
            pstmt.setString(12, activityTopMenu.getCreateUserId());
            pstmt.setTimestamp(13, new Timestamp(activityTopMenu.getCreateDate() == null ? System.currentTimeMillis() : activityTopMenu.getCreateDate().getTime()));
            pstmt.setString(14, activityTopMenu.getCreateIp());
            pstmt.setString(15, activityTopMenu.getValidStatus() == null ? ValidStatus.VALID.getCode() : activityTopMenu.getValidStatus().getCode());
            pstmt.setInt(16, activityTopMenu.getCategory() == null ? AppTopMenuCategory.DEFAULT.getCode() : activityTopMenu.getCategory().getCode());
            pstmt.setInt(17, activityTopMenu.getRedirectType() == null ? -1 : activityTopMenu.getRedirectType());
            pstmt.setString(18, activityTopMenu.getParam() == null ? "" : activityTopMenu.getParam().toJson());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                activityTopMenu.setActivityTopMenuId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert ActivityTopMenu,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return activityTopMenu;
    }

    @Override
    public List<ActivityTopMenu> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ActivityTopMenu> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public ActivityTopMenu get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(appkey,channel_id,platform,pic_url,menu_name," +
                "link_url,menu_type,menu_desc,is_new,is_hot,display_order,create_userid,create_date,create_ip," +
                "valid_status,category,redirect,param_text) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert ActivityTopMenu sql:" + insertSql);
        }
        return insertSql;
    }

    @Override
    protected ActivityTopMenu rsToObject(ResultSet rs) throws SQLException {
        ActivityTopMenu activityTopMenu = new ActivityTopMenu();
        //activity_menu_id,appkey,channel_id,platform,pic_url,menu_name,link_url,menu_type,menu_desc,is_new,is_hot,display_order,create_userid,create_date,create_ip,lastmodify_userid,lastmodify_date,lastmodify_ip,valid_status
        activityTopMenu.setActivityTopMenuId(rs.getLong("activity_menu_id"));
        activityTopMenu.setAppKey(rs.getString("appkey"));
        activityTopMenu.setChannelId(rs.getLong("channel_id"));
        activityTopMenu.setPlatform(rs.getInt("platform"));
        activityTopMenu.setPicUrl(rs.getString("pic_url"));
        activityTopMenu.setMenuName(rs.getString("menu_name"));
        activityTopMenu.setLinkUrl(rs.getString("link_url"));
        activityTopMenu.setMenuType(rs.getInt("menu_type"));
        activityTopMenu.setMenuDesc(rs.getString("menu_desc"));
        activityTopMenu.setIsNew(rs.getBoolean("is_new"));
        activityTopMenu.setIsHot(rs.getBoolean("is_hot"));
        activityTopMenu.setDisplayOrder(rs.getInt("display_order"));
        activityTopMenu.setCreateUserId(rs.getString("create_userid"));
        activityTopMenu.setCreateDate(rs.getTimestamp("create_date"));
        activityTopMenu.setCreateIp(rs.getString("create_ip"));
        activityTopMenu.setLastModifyUserId(rs.getString("lastmodify_userid"));
        activityTopMenu.setLastModifyDate(rs.getTimestamp("lastmodify_date"));
        activityTopMenu.setLastModifyIp(rs.getString("lastmodify_ip"));
        activityTopMenu.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        activityTopMenu.setCategory(AppTopMenuCategory.getByCode(rs.getInt("category")));
        activityTopMenu.setRedirectType(rs.getInt("redirect"));
        activityTopMenu.setParam(ActivityTopMenuParam.fromJson(rs.getString("param_text")));
        return activityTopMenu;
    }
}
