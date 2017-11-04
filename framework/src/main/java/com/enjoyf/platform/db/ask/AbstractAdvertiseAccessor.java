package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.wiki.Advertise;
import com.enjoyf.platform.service.ask.wiki.AdvertiseDomain;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

public abstract class AbstractAdvertiseAccessor extends AbstractBaseTableAccessor<Advertise> implements AdvertiseAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAdvertiseAccessor.class);

    private static final String KEY_TABLE_NAME = "advertise";

    @Override
    public Advertise insert(Advertise advertise, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, advertise.getAppkey());
            pstmt.setString(2, advertise.getTitle());
            pstmt.setString(3, advertise.getDescription());
            pstmt.setInt(4, advertise.getDomain().getCode());
            pstmt.setString(5, advertise.getTarget());
            pstmt.setString(6, advertise.getPic());
            pstmt.setString(7, advertise.getExtend());
            pstmt.setString(8, advertise.getRemoveStatus().getCode());
            pstmt.setLong(9, advertise.getDisplayOrder());
            pstmt.setTimestamp(10, new Timestamp(advertise.getCreateDate().getTime()));
            pstmt.setInt(11, advertise.getPlatform());
            pstmt.setInt(12, advertise.getType());
            pstmt.setTimestamp(13, new Timestamp(advertise.getStartTime().getTime()));
            pstmt.setTimestamp(14, new Timestamp(advertise.getEndTime().getTime()));
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                advertise.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return advertise;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(appkey,title,description,domain,target,pic,extend,remove_status,display_order,create_date,platform,type,start_time,end_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("Advertise insert sql" + sql);
        }
        return sql;
    }

    @Override
    public Advertise get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<Advertise> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Advertise> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected Advertise rsToObject(ResultSet rs) throws SQLException {

        Advertise returnObject = new Advertise();

        returnObject.setId(rs.getLong("id"));
        returnObject.setAppkey(rs.getString("appkey"));
        returnObject.setTitle(rs.getString("title"));
        returnObject.setDescription(rs.getString("description"));
        returnObject.setDomain(AdvertiseDomain.getByCode(rs.getInt("domain")));
        returnObject.setTarget(rs.getString("target"));
        returnObject.setPic(rs.getString("pic"));
        returnObject.setExtend(rs.getString("extend"));
        returnObject.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        returnObject.setDisplayOrder(rs.getLong("display_order"));
        returnObject.setCreateDate(new Date(rs.getTimestamp("create_date").getTime()));
        returnObject.setPlatform(rs.getInt("platform"));
        returnObject.setType(rs.getInt("type"));

        returnObject.setStartTime(rs.getTimestamp("start_time"));
        returnObject.setEndTime(rs.getTimestamp("end_time"));
        return returnObject;
    }
}