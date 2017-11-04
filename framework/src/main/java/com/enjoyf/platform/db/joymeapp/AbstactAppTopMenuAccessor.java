package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.ChannelTopMenuSet;
import com.enjoyf.platform.service.joymeapp.JoymeAppTopMenu;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-2
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstactAppTopMenuAccessor extends AbstractSequenceBaseTableAccessor<JoymeAppTopMenu> implements AppTopMenuAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractContentVersionInfoAccessor.class);


    private static final String KEY_SEQUENCE_NAME = "SEQ_TOP_MENU_ID";
    protected static final String KEY_TABLE_NAME = "app_top_menu";

    @Override
    public JoymeAppTopMenu insert(JoymeAppTopMenu joymeAppTopMenu, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {

            joymeAppTopMenu.setMenuId(getSeqNo(KEY_SEQUENCE_NAME, conn));
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, joymeAppTopMenu.getMenuId());
            pstmt.setString(2, joymeAppTopMenu.getAppkey());
            pstmt.setString(3, joymeAppTopMenu.getPicUrl1());
            pstmt.setString(4, joymeAppTopMenu.getPicUrl2());
            pstmt.setString(5, joymeAppTopMenu.getMenuName());
            pstmt.setString(6, joymeAppTopMenu.getUrl());
            pstmt.setInt(7, joymeAppTopMenu.getMenuType());
            pstmt.setString(8, joymeAppTopMenu.getMenuDesc());
            pstmt.setBoolean(9, joymeAppTopMenu.isHot());
            pstmt.setBoolean(10, joymeAppTopMenu.isNew());
            pstmt.setString(11, joymeAppTopMenu.getCreateUserId());
            pstmt.setTimestamp(12, new Timestamp(joymeAppTopMenu.getCreateDate() == null ? System.currentTimeMillis() : joymeAppTopMenu.getCreateDate().getTime()));
            pstmt.setString(13, joymeAppTopMenu.getCreateIp());
            pstmt.setString(14, joymeAppTopMenu.getLastModifyUserId());
            pstmt.setString(15, joymeAppTopMenu.getLastModifyIp());
            pstmt.setString(16, joymeAppTopMenu.getRemoveStatus().getCode());
            if (joymeAppTopMenu.getLastModifyDate() != null) {
                pstmt.setTimestamp(17, new Timestamp(joymeAppTopMenu.getLastModifyDate().getTime()));
            } else {
                pstmt.setNull(17, Types.TIMESTAMP);
            }
            pstmt.setInt(18, joymeAppTopMenu.getDisplay_order());

            pstmt.setString(19, joymeAppTopMenu.getChannelTopMenuSet() != null ? joymeAppTopMenu.getChannelTopMenuSet().toJson() : null);
            pstmt.setString(20, joymeAppTopMenu.getGameId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert joymeAppTopMenu, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return joymeAppTopMenu;
    }


    @Override
    protected JoymeAppTopMenu rsToObject(ResultSet rs) throws SQLException {
        JoymeAppTopMenu returnObj = new JoymeAppTopMenu();
        returnObj.setMenuId(rs.getLong("top_menu_id"));
        returnObj.setAppkey(rs.getString("appkey"));
        returnObj.setPicUrl1(rs.getString("pic_url"));
        returnObj.setPicUrl2(rs.getString("pic_url2"));
        returnObj.setMenuName(rs.getString("menu_name"));
        returnObj.setUrl(rs.getString("link_url"));
        returnObj.setMenuType(rs.getInt("menu_type"));
        returnObj.setMenuDesc(rs.getString("menu_desc"));
        returnObj.setNew(rs.getBoolean("menu_isnew"));
        returnObj.setHot(rs.getBoolean("menu_ishot"));
        returnObj.setCreateUserId(rs.getString("create_userid"));
        returnObj.setCreateDate(rs.getDate("createdate"));
        returnObj.setCreateIp(rs.getString("create_ip"));
        returnObj.setLastModifyUserId(rs.getString("lastmodify_userid"));
        returnObj.setLastModifyIp(rs.getString("lastmodify_ip"));
        if (rs.getTimestamp("lastmodifydate") != null) {
            returnObj.setLastModifyDate(rs.getTimestamp("lastmodifydate"));
        }
        returnObj.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        returnObj.setDisplay_order(rs.getInt("display_order"));

        if (!StringUtil.isEmpty(rs.getString("channel_topmenu"))) {
            returnObj.setChannelTopMenuSet(ChannelTopMenuSet.fromJson(rs.getString("channel_topmenu")));
        }
        returnObj.setGameId(rs.getString("gameid"));

        return returnObj;
    }

    @Override
    public JoymeAppTopMenu get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<JoymeAppTopMenu> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<JoymeAppTopMenu> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (top_menu_id, appkey, pic_url,pic_url2, menu_name," +
                " link_url, menu_type, menu_desc,menu_isnew,menu_ishot,create_userid,createdate,create_ip," +
                "lastmodify_userid,lastmodify_ip,removestatus,lastmodifydate,display_order,channel_topmenu,gameid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("AppErrorInfo INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
