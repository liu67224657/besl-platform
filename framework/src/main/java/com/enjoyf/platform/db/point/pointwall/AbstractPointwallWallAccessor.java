package com.enjoyf.platform.db.point.pointwall;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.point.GiftReserveAccessor;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.point.GiftReserve;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallWallField;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tony Diao
 * Date: 28-11-14
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPointwallWallAccessor extends AbstractBaseTableAccessor<PointwallWall> implements PointwallWallAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractPointwallWallAccessor.class);

    protected static final String KEY_TABLE_NAME = "pw_wall";

    @Override
    public PointwallWall insert(PointwallWall pointwallWall, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, pointwallWall.getAppkey());
            pstmt.setString(2, pointwallWall.getPointKey());
            pstmt.setString(3, pointwallWall.getWallMoneyName());
            pstmt.setString(4,pointwallWall.getTemplate());
            pstmt.setInt(5,pointwallWall.getShopKey());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert PointwallWall, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return pointwallWall;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    //select * from goods order by displayourder asc
    public List<PointwallWall> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    public List<PointwallWall> queryAll(QueryExpress queryExpress, Connection conn) throws DbException{

        return super.query(KEY_TABLE_NAME,queryExpress,conn);
    }

    @Override
    public int queryTotalWalls(QueryExpress queryExpress, Connection conn) throws DbException {

        return   queryRowSize(KEY_TABLE_NAME,queryExpress,conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public PointwallWall get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected PointwallWall rsToObject(ResultSet rs) throws SQLException {
        PointwallWall pointwallWall = new PointwallWall();
        pointwallWall.setAppkey(rs.getString("appkey"));
        pointwallWall.setPointKey(rs.getString("point_key"));
        pointwallWall.setWallMoneyName(rs.getString("wall_money_name"));
        pointwallWall.setTemplate(rs.getString("template"));
        pointwallWall.setShopKey(rs.getInt("shop_key"));

        return pointwallWall;
    }

    public String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(appkey,point_key,wall_money_name,template,shop_key) values(?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PointwallWall insert sql:" + sql);
        }
        return sql;
    }

}
