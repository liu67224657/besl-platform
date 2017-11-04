package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.ClientItemType;
import com.enjoyf.platform.service.joymeapp.ClientLine;
import com.enjoyf.platform.service.joymeapp.ClientLineAngular;
import com.enjoyf.platform.service.joymeapp.ClientLineType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.URLUtils;
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
public abstract class AbstractClientLineAccessor extends AbstractBaseTableAccessor<ClientLine> implements ClientLineAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractClientLineAccessor.class);

    private static final String KEY_TABLE_NAME = "client_line";

    @Override
    public ClientLine insert(ClientLine clientLine, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //code,line_name,item_type,create_date,create_userid,modify_date,modify_userid,valid_status
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, clientLine.getCode());
            pstmt.setString(2, clientLine.getLineName());
            pstmt.setInt(3, clientLine.getItemType().getCode());
            pstmt.setTimestamp(4, new Timestamp(clientLine.getCreateDate() == null ? System.currentTimeMillis() : clientLine.getCreateDate().getTime()));
            pstmt.setString(5, clientLine.getCreateUserid());
            pstmt.setString(6, clientLine.getValidStatus() == null ? ValidStatus.INVALID.getCode() : clientLine.getValidStatus().getCode());
            pstmt.setInt(7, clientLine.getPlatform());
            pstmt.setInt(8, clientLine.getLineType().getCode());
            pstmt.setInt(9, clientLine.getLineAngle() == null ? -1 : clientLine.getLineAngle().getCode());
            pstmt.setString(10, clientLine.getBigpic() == null ? "" : clientLine.getBigpic());
            pstmt.setString(11, clientLine.getSmallpic() == null ? "" : clientLine.getSmallpic());
            pstmt.setString(12, clientLine.getLine_desc() == null ? "" : clientLine.getLine_desc());
            pstmt.setInt(13, clientLine.getDisplay_order());
            pstmt.setString(14,clientLine.getLine_intro());
            pstmt.setInt(15,clientLine.getHot());
            pstmt.setString(16,clientLine.getSharePic());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                clientLine.setLineId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert ClientLine,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return clientLine;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ClientLine> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ClientLine> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public ClientLine get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(code,line_name,item_type,create_date,create_userid,valid_status,platform,line_type,angular,bigpic,smallpic,line_desc,display_order,line_intro,is_hot,share_pic) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert ClientLine sql:" + insertSql);
        }
        return insertSql;
    }

    @Override
    protected ClientLine rsToObject(ResultSet rs) throws SQLException {
        ClientLine clienLine = new ClientLine();
        clienLine.setLineId(rs.getLong("line_id"));
        clienLine.setCode(rs.getString("code"));
        clienLine.setLineName(rs.getString("line_name"));
        clienLine.setItemType(ClientItemType.getByCode(rs.getInt("item_type")));
        clienLine.setCreateDate(rs.getTimestamp("create_date"));
        clienLine.setCreateUserid(rs.getString("create_userid"));
        clienLine.setUpdateDate(rs.getTimestamp("modify_date"));
        clienLine.setUpdateUserid(rs.getString("modify_userid"));
        clienLine.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        clienLine.setPlatform(rs.getInt("platform"));
        clienLine.setLineType(ClientLineType.getByCode(rs.getInt("line_type")));
        clienLine.setLineAngle(ClientLineAngular.getByCode(rs.getInt("angular")));
        clienLine.setBigpic(URLUtils.getJoymeDnUrl(rs.getString("bigpic")));
        clienLine.setSmallpic(URLUtils.getJoymeDnUrl(rs.getString("smallpic")));
        clienLine.setLine_desc(rs.getString("line_desc"));
        clienLine.setLine_intro(rs.getString("line_intro"));
        clienLine.setHot(rs.getInt("is_hot"));
        clienLine.setDisplay_order(rs.getInt("display_order"));
        clienLine.setSharePic(URLUtils.getJoymeDnUrl(rs.getString("share_pic")));
        return clienLine;
    }
}
