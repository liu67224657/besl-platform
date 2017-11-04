package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.City;
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
 * Date: 13-8-21
 * Time: 下午1:58
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCityAccessor extends AbstractBaseTableAccessor<City> implements CityAccessor{

    private static final Logger logger = LoggerFactory.getLogger(AbstractCityAccessor.class);

    private static final String KEY_TABLE_NAME = "city";
    @Override
    public City insert(City city, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, city.getCityName());
            pstmt.setBoolean(2, city.getIsPreset());
            pstmt.setTimestamp(3, new Timestamp(city.getCreateDate()==null?System.currentTimeMillis():city.getCreateDate().getTime()));
            pstmt.setString(4, city.getCreateIp());
            pstmt.setString(5, city.getCreateUserId());
            pstmt.setString(6, city.getValidStatus()==null?ValidStatus.VALID.getCode():city.getValidStatus().getCode());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                city.setCityId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert City,SQLException:"+e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return city;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public City get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME , queryExpress, conn);
    }

    @Override
    public List<City> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<City> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected City rsToObject(ResultSet rs) throws SQLException {
        City city = new City();
        city.setCityId(rs.getLong("city_id"));
        city.setCityName(rs.getString("city_name"));
        city.setIsPreset(rs.getBoolean("is_preset"));
        city.setCreateDate(rs.getTimestamp("create_date"));
        city.setCreateIp(rs.getString("create_ip"));
        city.setCreateUserId(rs.getString("create_userid"));
        city.setLastModifyDate(rs.getTimestamp("last_modify_date"));
        city.setLastModifyIp(rs.getString("last_modify_ip"));
        city.setLastModifyUserId(rs.getString("last_modify_userid"));
        city.setValidStatus(ValidStatus.getByCode(rs.getString("validstatus")));
        return city;
    }

    private String getInsertSql(){
        String insertSql = "INSERT INTO "+KEY_TABLE_NAME+"(city_name,is_preset,create_date,create_ip,create_userid,validstatus) VALUES(?,?,?,?,?,?)";
        if(logger.isDebugEnabled()){
            logger.debug("insert city sql:"+insertSql);
        }
        return insertSql;
    }
}
