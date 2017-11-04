package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
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
public abstract class AbstractClientLineItemAccessor extends AbstractBaseTableAccessor<ClientLineItem> implements ClientLineItemAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractClientLineItemAccessor.class);

    private static final String KEY_TABLE_NAME = "client_line_item";

    @Override
    public ClientLineItem insert(ClientLineItem clientLineitem, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //code,line_name,item_type,create_date,create_userid,modify_date,modify_userid,valid_status
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, clientLineitem.getLineId());
            pstmt.setString(2, clientLineitem.getTitle());
            pstmt.setString(3, clientLineitem.getDesc());
            pstmt.setString(4, clientLineitem.getPicUrl());
            pstmt.setString(5, clientLineitem.getUrl());
            pstmt.setString(6, clientLineitem.getDirectId() == null ? "" : clientLineitem.getDirectId());
            pstmt.setInt(7, clientLineitem.getItemType().getCode());
            pstmt.setInt(8, clientLineitem.getItemDomain().getCode());
            pstmt.setTimestamp(9, new Timestamp(clientLineitem.getItemCreateDate() == null ? System.currentTimeMillis() : clientLineitem.getItemCreateDate().getTime()));
            pstmt.setInt(10, clientLineitem.getDisplayOrder());
            pstmt.setString(11, clientLineitem.getValidStatus().getCode());
            pstmt.setInt(12, clientLineitem.getRedirectType() == null ? -1 : clientLineitem.getRedirectType().getCode());
            pstmt.setString(13, clientLineitem.getRate() == null ? "" : clientLineitem.getRate());
            pstmt.setString(14, clientLineitem.getCategory() == null ? "" : clientLineitem.getCategory());
            pstmt.setString(15, clientLineitem.getCategoryColor() == null ? "" : clientLineitem.getCategoryColor());
            pstmt.setInt(16, clientLineitem.getAppDisplayType() == null ? 0 : clientLineitem.getAppDisplayType().getCode());
            pstmt.setString(17, clientLineitem.getAuthor() == null ? "" : clientLineitem.getAuthor());
            pstmt.setString(18, clientLineitem.getParam() == null ? "" : clientLineitem.getParam().toJson());
            pstmt.setTimestamp(19, new Timestamp(clientLineitem.getStateDate() == null ? System.currentTimeMillis() : clientLineitem.getStateDate().getTime()));
            pstmt.setInt(20, clientLineitem.getDisplayType());
            pstmt.setLong(21, clientLineitem.getContentid());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                clientLineitem.setItemId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert ClientLine,SQLException:" + e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return clientLineitem;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<ClientLineItem> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ClientLineItem> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public ClientLineItem get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(line_id,title,linedesc,pic_url,url,direct_id," +
                "item_type,item_domain,item_create_date,display_order,valid_status,app_redirect_type,rate," +
                "category,category_color,app_display_type,author,item_param,state_date,is_hot,contentid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert ClientLine sql:" + insertSql);
        }
        return insertSql;
    }

    @Override
    protected ClientLineItem rsToObject(ResultSet rs) throws SQLException {
        ClientLineItem clientLineItem = new ClientLineItem();
        clientLineItem.setItemId(rs.getLong("item_id"));
        clientLineItem.setLineId(rs.getLong("line_id"));
        clientLineItem.setTitle(rs.getString("title"));
        clientLineItem.setDesc(rs.getString("linedesc"));
        clientLineItem.setPicUrl(URLUtils.getJoymeDnUrl(rs.getString("pic_url")));
        clientLineItem.setUrl(rs.getString("url"));
        clientLineItem.setDirectId(rs.getString("direct_id"));
        clientLineItem.setItemType(ClientItemType.getByCode(rs.getInt("item_type")));
        clientLineItem.setItemDomain(ClientItemDomain.getByCode(rs.getInt("item_domain")));
        clientLineItem.setItemCreateDate(rs.getTimestamp("item_create_date"));
        clientLineItem.setDisplayOrder(rs.getInt("display_order"));
        clientLineItem.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        clientLineItem.setRedirectType(AppRedirectType.getByCode(rs.getInt("app_redirect_type")));
        clientLineItem.setRate(rs.getString("rate"));
        clientLineItem.setCategory(rs.getString("category"));
        clientLineItem.setCategoryColor(rs.getString("category_color"));
        clientLineItem.setAppDisplayType(AppDisplayType.getByCode(rs.getInt("app_display_type")));
        clientLineItem.setAuthor(rs.getString("author"));
        if (!StringUtil.isEmpty(rs.getString("item_param"))) {
            clientLineItem.setParam(ParamTextJson.fromJson(rs.getString("item_param")));
        }
        clientLineItem.setStateDate(rs.getTimestamp("state_date"));
        clientLineItem.setDisplayType(rs.getInt("is_hot"));
        clientLineItem.setContentid(rs.getLong("contentid"));
        return clientLineItem;
    }
}
