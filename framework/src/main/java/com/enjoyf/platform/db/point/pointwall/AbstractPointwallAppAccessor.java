package com.enjoyf.platform.db.point.pointwall;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
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
 * User: Tony Diao
 * Date: 28-11-14
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPointwallAppAccessor extends AbstractBaseTableAccessor<PointwallApp> implements PointwallAppAccessor{

    private Logger logger = LoggerFactory.getLogger(AbstractPointwallAppAccessor.class);

    protected static final String KEY_TABLE_NAME = "pw_app";



    @Override
    public PointwallApp insert(PointwallApp pointwallApp, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, pointwallApp.getPackageName());
            pstmt.setString(2, pointwallApp.getAppName());
            pstmt.setString(3, pointwallApp.getVerName());
            pstmt.setInt(4, pointwallApp.getPlatform());
            pstmt.setString(5, pointwallApp.getAppIcon());
            pstmt.setString(6, pointwallApp.getAppDesc());
            pstmt.setString(7, pointwallApp.getSponsorName()==null?"":pointwallApp.getSponsorName());
            pstmt.setString(8, pointwallApp.getDownloadUrl());
            pstmt.setString(9, pointwallApp.getReportUrl()==null?"":pointwallApp.getReportUrl());
            pstmt.setInt(10, pointwallApp.getInitScore());
//            pstmt.setString(11,pointwallApp.getStatus());

         //   pstmt.setTimestamp(11, new Timestamp(pointwallApp.getCreateTime()==null?System.currentTimeMillis():pointwallApp.getCreateTime().getTime()));


            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                pointwallApp.setAppId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert PointwallApp, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return pointwallApp;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    //select * from goods order by displayourder asc
    public List<PointwallApp> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }


    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public PointwallApp get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME,queryExpress , conn);
    }



    @Override
    protected PointwallApp rsToObject(ResultSet rs) throws SQLException {

        PointwallApp pointwallApp = new PointwallApp();
        pointwallApp.setAppId(rs.getLong("app_id"));
        pointwallApp.setPackageName(rs.getString("package_name"));
        pointwallApp.setAppName(rs.getString("app_name"));
        pointwallApp.setVerName(rs.getString("ver_name"));
        pointwallApp.setPlatform(rs.getInt("platform"));
        pointwallApp.setAppIcon(rs.getString("app_icon"));
        pointwallApp.setAppDesc(rs.getString("app_desc"));
        pointwallApp.setSponsorName(rs.getString("sponsor_name"));
        pointwallApp.setDownloadUrl(rs.getString("download_url")) ;
        pointwallApp.setReportUrl(rs.getString("report_url"));
        pointwallApp.setInitScore(rs.getInt("init_score"));
        pointwallApp.setCreateTime(rs.getTimestamp("create_time"));
        pointwallApp.setRemoveStatus(rs.getString("remove_status"));
        return pointwallApp;
    }

    public String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(package_name,app_name,ver_name,platform,app_icon,app_desc," +
                "sponsor_name,download_url,report_url,init_score)" +
                " values(?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PointwallApp insert sql:" + sql);
        }
        return sql;
    }

}
