package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.JoymeAppMenuTag;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-3
 * Time: 下午10:58
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJoymeAppMenuTagAccessor extends AbstractBaseTableAccessor<JoymeAppMenuTag> implements JoymeAppMenuTagAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractPageViewInfoAccessor.class);

    protected static final String KEY_TABLE_NAME = "app_menu_tag";


    @Override
    public JoymeAppMenuTag insert(JoymeAppMenuTag tag, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs=null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            //tagname, removestatus, topmeunid, createid, createip, createdate
            pstmt.setString(1, tag.getTagName());
            pstmt.setString(2, tag.getRemoveStatus().getCode());
            pstmt.setLong(3, tag.getTopMenuId());
            pstmt.setString(4, tag.getCreateId());
            pstmt.setString(5, tag.getCreateIp());
            pstmt.setTimestamp(6, new Timestamp(tag.getCreateDate() != null ? tag.getCreateDate().getTime() : System.currentTimeMillis()));
            pstmt.setInt(7, tag.getDisplayOrder());


            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                tag.setTagId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert PushMessage, a SQLException occured." + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }


        return tag;
    }

    @Override
    protected JoymeAppMenuTag rsToObject(ResultSet rs) throws SQLException {
        JoymeAppMenuTag returnObj=new JoymeAppMenuTag();

        //tagname, removestatus, topmeunid, createid, createip, createdate
        returnObj.setTagId(rs.getLong("tagid"));
        returnObj.setTagName(rs.getString("tagname"));
        returnObj.setRemoveStatus(ActStatus.getByCode(rs.getString("removestatus")));
        returnObj.setTopMenuId(rs.getLong("topmenuid"));
        returnObj.setCreateId(rs.getString("createid"));
        returnObj.setCreateIp(rs.getString("createip"));
        returnObj.setCreateDate(rs.getTimestamp("createdate"));
        returnObj.setDisplayOrder(rs.getInt("displayorder"));

        return returnObj;
    }

    @Override
    public JoymeAppMenuTag get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME,queryExpress,conn);
    }

    @Override
    public List<JoymeAppMenuTag> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME,queryExpress,conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME,updateExpress,queryExpress,conn);
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (tagname, removestatus, topmenuid, createid, createip, createdate,displayorder) VALUES (?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppMenuTag INSERT Script:" + insertSql);
        }
        return insertSql;
    }
}
