package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractWanbaItemAccessor extends AbstractBaseTableAccessor<WanbaItem> implements WanbaItemAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWanbaItemAccessor.class);

    private static final String KEY_TABLE_NAME = "wanba_item";

    @Override
    public WanbaItem insert(WanbaItem askItem, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            askItem.setItemId(AskUtil.getAskItemId(askItem.getDestId(), askItem.getLineKey(), askItem.getItemType()));
            pstmt.setString(1, askItem.getItemId());
            pstmt.setString(2, askItem.getLineKey());
            pstmt.setString(3, askItem.getOwnProfileId());
            pstmt.setInt(4, askItem.getItemType().getCode());
            pstmt.setString(5, askItem.getDestId());
            pstmt.setString(6, askItem.getDestProfileId());
            pstmt.setDouble(7, askItem.getScore());
            pstmt.setInt(8, askItem.getItemDomain().getCode());
            pstmt.setTimestamp(9, new Timestamp(askItem.getCreateTime().getTime()));
            pstmt.setString(10, askItem.getValidStatus().getCode());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return askItem;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(item_id,line_key,own_profile_id,item_type,dest_id,dest_profile_id,score,item_domain,create_time,valid_status) VALUES (?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("WanbaItem insert sql" + sql);
        }
        return sql;
    }

    @Override
    public WanbaItem get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<WanbaItem> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<WanbaItem> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected WanbaItem rsToObject(ResultSet rs) throws SQLException {
        WanbaItem returnObject = new WanbaItem();
        returnObject.setItemId(rs.getString("item_id"));
        returnObject.setLineKey(rs.getString("line_key"));
        returnObject.setOwnProfileId(rs.getString("own_profile_id"));
        returnObject.setItemType(ItemType.getByCode(rs.getInt("item_type")));
        returnObject.setDestId(rs.getString("dest_id"));
        returnObject.setDestProfileId(rs.getString("dest_profile_id"));
        returnObject.setScore(rs.getDouble("score"));
        returnObject.setItemDomain(WanbaItemDomain.getByCode(rs.getInt("item_domain")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        return returnObject;
    }
}