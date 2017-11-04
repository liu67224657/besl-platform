package com.enjoyf.platform.db.point.pointwall;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.pointwall.PointwallTasklog;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tony Diao
 * Date: 28-11-14
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPointwallTasklogAccessor extends AbstractBaseTableAccessor<PointwallTasklog> implements PointwallTasklogAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractPointwallTasklogAccessor.class);

    protected static final String KEY_TABLE_NAME = "pw_tasklog";


    @Override
    //分页查询
    public List<PointwallTasklog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    //根据MD5值查询 下载记录是否存在
    @Override
    public PointwallTasklog get(QueryExpress queryExpress, Connection conn) throws DbException {

        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    //查询符合查询条件的所有记录的数量
       @Override
    public int countTotal(QueryExpress queryExpress, Connection conn) throws DbException{

           return   queryRowSize(KEY_TABLE_NAME,queryExpress,conn);

    }

    //查询一个符合一定条件的所有记录,非分页,用于数据导出
    @Override
    public List<PointwallTasklog> queryAll(QueryExpress queryExpress,int startIndex,int size, Connection conn) throws DbException {

        List<PointwallTasklog> returnValue = new ArrayList<PointwallTasklog>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true)+" limit "+startIndex+", "+size;
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            //
            ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    protected PointwallTasklog rsToObject(ResultSet rs) throws SQLException {
        PointwallTasklog p = new PointwallTasklog();
        p.setTaskId(rs.getString("task_id"));
        p.setClientId(rs.getString("clientid"));
        p.setProfileid(rs.getString("profileid"));
        p.setAppId(rs.getLong("app_id"));
        p.setPackageName(rs.getString("package_name"));
        p.setAppkey(rs.getString("appkey"));
        p.setStatus(rs.getInt("status"));
        p.setCreateTime(rs.getTimestamp("create_time"));
        p.setCreateIp(rs.getString("create_ip"));
        p.setPointAmount(rs.getInt("point_amount"));
        p.setPlatform(rs.getInt("platform"));
        return p;
    }

    @Override
    public PointwallTasklog insert(PointwallTasklog pointwallTasklog, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, pointwallTasklog.getTaskId());
            pstmt.setString(2, pointwallTasklog.getClientId());
            pstmt.setString(3, pointwallTasklog.getProfileid());
            pstmt.setLong(4, pointwallTasklog.getAppId());
            pstmt.setString(5, pointwallTasklog.getPackageName());
            pstmt.setString(6, pointwallTasklog.getAppkey());
            pstmt.setInt(7, pointwallTasklog.getStatus());
            pstmt.setString(8, pointwallTasklog.getCreateIp());
            pstmt.setInt(9, pointwallTasklog.getPointAmount());
            pstmt.setInt(10, pointwallTasklog.getPlatform());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            GAlerter.lab("On insert PointwallTasklog, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return pointwallTasklog;
    }

    public String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(task_id,clientid,profileid,app_id,package_name,appkey,status,create_ip,point_amount,platform) values(?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PointwallTasklog insert sql:" + sql);
        }
        return sql;
    }
}
