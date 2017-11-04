package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.CityRelation;
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
 * Time: 下午2:25
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCityRelationAccessor extends AbstractBaseTableAccessor<CityRelation> implements CityRelationAccessor{

    private static final Logger logger = LoggerFactory.getLogger(AbstractCityRelationAccessor.class);

    private static final String KEY_TABLE_NAME = "city_relation";

    @Override
    public CityRelation insert(CityRelation cityRelation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, cityRelation.getCityId());
            pstmt.setLong(2, cityRelation.getNewGameInfoId());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                cityRelation.setCityRelationId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert ");
        }
        return cityRelation;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public CityRelation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CityRelation> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CityRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected CityRelation rsToObject(ResultSet rs) throws SQLException {
        CityRelation cityRelation = new CityRelation();
        cityRelation.setCityRelationId(rs.getLong("city_relation_id"));
        cityRelation.setCityId(rs.getLong("city_id"));
        cityRelation.setNewGameInfoId(rs.getLong("new_game_info_id"));
        return cityRelation;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(city_id,new_game_info_id) VALUES(?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("insert cityRelation sql:" + insertSql);
        }
        return insertSql;
    }
}
