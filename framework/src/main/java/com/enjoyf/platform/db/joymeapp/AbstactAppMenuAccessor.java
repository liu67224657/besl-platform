package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
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
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-2
 * Time: 下午6:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstactAppMenuAccessor extends AbstractSequenceBaseTableAccessor<JoymeAppMenu> implements AppMenuAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstactAppMenuAccessor.class);


    private static final String KEY_SEQUENCE_NAME = "SEQ_BUTTOM_MENU_ID";
    protected static final String KEY_TABLE_NAME = "app_buttom_menu";

    @Override
    public JoymeAppMenu insert(JoymeAppMenu joymeAppMenu, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, joymeAppMenu.getParentId());
            pstmt.setString(2, joymeAppMenu.getAppkey());
            pstmt.setString(3, joymeAppMenu.getPicUrl());
            pstmt.setString(4, joymeAppMenu.getMenuName());
            pstmt.setString(5, joymeAppMenu.getUrl());
            pstmt.setInt(6, joymeAppMenu.getMenuType());
            pstmt.setString(7, joymeAppMenu.getMenuDesc());
            pstmt.setBoolean(8, joymeAppMenu.isNew());
            pstmt.setBoolean(9, joymeAppMenu.isHot());
            pstmt.setString(10, joymeAppMenu.getCreateUserId());
            pstmt.setTimestamp(11, new Timestamp(joymeAppMenu.getCreateDate() == null ? System.currentTimeMillis() : joymeAppMenu.getCreateDate().getTime()));
            pstmt.setString(12, joymeAppMenu.getCreateIp());
            pstmt.setString(13, joymeAppMenu.getLastModifyUserId());
            pstmt.setString(14, joymeAppMenu.getLastModifyIp());
            if (joymeAppMenu.getLastModifyDate() != null) {
                pstmt.setTimestamp(15, new Timestamp(joymeAppMenu.getLastModifyDate().getTime()));
            } else {
                pstmt.setNull(15, Types.TIMESTAMP);
            }
            pstmt.setString(16, joymeAppMenu.getRemoveStatus().getCode());
            pstmt.setInt(17, joymeAppMenu.getDisplay_order());
            pstmt.setInt(18, joymeAppMenu.getDisplayType().getCode());

            pstmt.setInt(19, joymeAppMenu.getModuleType() == null ? JoymeAppMenuModuleType.DEFAULT.getCode() : joymeAppMenu.getModuleType().getCode());
            pstmt.setString(20, joymeAppMenu.getStatusDesc() == null ? "" : joymeAppMenu.getStatusDesc());
            pstmt.setInt(21, joymeAppMenu.getRecommendStar());
            pstmt.setInt(22, joymeAppMenu.getContentType() == null ? 0 : joymeAppMenu.getContentType().getCode());
            pstmt.setString(23, joymeAppMenu.getPic() == null ? "" : joymeAppMenu.getPic().toJsonStr());
            pstmt.setString(24, joymeAppMenu.getExpField() == null ? "" : joymeAppMenu.getExpField().toJsonStr());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                joymeAppMenu.setMenuId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert JoymeAppMenu, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return joymeAppMenu;
    }

    @Override
    public JoymeAppMenu get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<JoymeAppMenu> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<JoymeAppMenu> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected JoymeAppMenu rsToObject(ResultSet rs) throws SQLException {
        JoymeAppMenu returnObj = new JoymeAppMenu();
        returnObj.setMenuId(rs.getInt("buttom_menu_id"));
        returnObj.setParentId(rs.getInt("pid"));
        returnObj.setAppkey(rs.getString("appkey"));
        returnObj.setPicUrl(rs.getString("pic_url"));
        returnObj.setMenuName(rs.getString("menu_name"));
        returnObj.setUrl(rs.getString("link_url"));
        returnObj.setMenuType(rs.getInt("menu_type"));
        returnObj.setMenuDesc(rs.getString("menu_desc"));
        returnObj.setNew(rs.getBoolean("menu_isnew"));
        returnObj.setHot(rs.getBoolean("menu_ishot"));
        returnObj.setCreateUserId(rs.getString("create_userid"));
        returnObj.setCreateDate(new Date(rs.getTimestamp("createdate").getTime()));
        returnObj.setCreateIp(rs.getString("create_ip"));
        returnObj.setLastModifyUserId(rs.getString("lastmodify_userid"));
        returnObj.setLastModifyIp(rs.getString("lastmodify_ip"));
        if (rs.getTimestamp("lastmodifydate") != null) {
            returnObj.setLastModifyDate(rs.getTimestamp("lastmodifydate"));
        }
        returnObj.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        returnObj.setDisplay_order(rs.getInt("display_order"));
        returnObj.setTagId(rs.getLong("tag_id"));
        returnObj.setDisplayType(JoymeAppMenuDisplayType.getByCode(rs.getInt("display_type")));

        returnObj.setModuleType(JoymeAppMenuModuleType.getByCode(rs.getInt("menucategory")));
        returnObj.setStatusDesc(rs.getString("statusdescription"));
        returnObj.setRecommendStar(rs.getInt("recomstar"));
        returnObj.setContentType(JoymeAppMenuContentType.getByCode(rs.getInt("contenttype")));
        returnObj.setExpField(JoymeAppMenuExpField.parse(rs.getString("expfield")));
        returnObj.setPic(JoymeAppMenuPic.parse(rs.getString("pic")));
        return returnObj;
    }


    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (pid, appkey, pic_url, menu_name, " +
                "link_url, menu_type, menu_desc,menu_isnew,menu_ishot,create_userid,createdate,create_ip," +
                "lastmodify_userid,lastmodify_ip,lastmodifydate,removestatus,display_order,display_type," +
                "menucategory,statusdescription,recomstar,contenttype,pic,expfield) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AppErrorInfo INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
