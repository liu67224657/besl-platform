package com.enjoyf.platform.db.point.pointwall;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.pointwall.PointwallWall;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Tony Diao
 * Date: 28-11-14
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPointwallWallAppAccessor extends AbstractBaseTableAccessor<PointwallWallApp> implements PointwallWallAppAccessor{

    private Logger logger = LoggerFactory.getLogger(AbstractPointwallWallAppAccessor.class);

    protected static final String KEY_TABLE_NAME = "pw_r_wall_app";


    //查询一个积分墙内拥有的app的数量
    @Override
    public int queryTotalOfAppsOfOneWall(QueryExpress queryExpress, Connection conn) throws DbException{

     return   queryRowSize(KEY_TABLE_NAME,queryExpress,conn);
    }


    @Override
    public int   queryTotalOfApps(QueryExpress queryExpress, Connection conn)  throws DbException{

        return   queryRowSize("pw_app",queryExpress,conn);
    }

    //用于指定积分墙增加一个app,从app表查询显示分页时,先过滤掉已经被添加到这个积分墙的app
    @Override
    public List<PointwallWallApp> queryAll(QueryExpress queryExpress, Connection conn) throws DbException{

             return super.query(KEY_TABLE_NAME, queryExpress, conn);

    }

    @Override
    public PointwallWallApp insert(PointwallWallApp pointwallWallApp, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, pointwallWallApp.getAppId());
            pstmt.setString(2, pointwallWallApp.getAppkey());
            pstmt.setInt(3, pointwallWallApp.getPlatform());
            pstmt.setInt(4, pointwallWallApp.getDisplayOrder());
            pstmt.setInt(5,pointwallWallApp.getHotStatus());
            pstmt.setInt(6, pointwallWallApp.getPointAmount());
            pstmt.setString(7,pointwallWallApp.getStatus());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                pointwallWallApp.setWallAppId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert PointwallWallApp, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return pointwallWallApp;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    //用于查询某个积分墙的内容时的分页
    @Override
    public List<PointwallWallApp> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
          //
          if (page == null) {
              return query(KEY_TABLE_NAME, queryExpress, conn);
          }

          //
          List<PointwallWallApp> returnValue = new ArrayList<PointwallWallApp>();

          PreparedStatement pstmt = null;
          ResultSet rs = null;

        //  String sql = "SELECT * FROM " + KEY_TABLE_NAME + " " + ObjectFieldUtil.generateQueryClause(queryExpress, true) + " LIMIT ?, ?";
          String sql="select pw_r_wall_app.*, k.package_name package_name,k.app_name app_name,k.app_icon app_icon ,k.app_desc app_desc,k.download_url download_url from pw_r_wall_app ,pw_app k "+ ObjectFieldUtil.generateQueryClause(queryExpress, true) +"   limit ?,?";

             String toInsert="pw_r_wall_app.app_id=k.app_id ";



         String result="";
        Matcher mWhere=Pattern.compile("(?i)where").matcher(sql);
        Matcher mOrder=Pattern.compile("(?i)order").matcher(sql);

        if(mWhere.find()){
            result=mWhere.replaceFirst(" where " +toInsert+" and ");

        } else if(mOrder.find()){
            result=mOrder.replaceFirst(" where "+toInsert+" order ");

        } else {
            result=sql.replaceFirst("(?i)limit"," where "+toInsert+" limit ");

        }

       /* if (queryExpress.getQueryCriterions().hasCriterion()) {

                    QueryCriterions criterions = queryExpress.getQueryCriterions();

                if (criterions.isMultiCriterion()) {
                    for (QueryCriterions next : criterions.getCriterionsList()) {

                        if(!next.isMultiCriterion()) {

                            QueryCriterion criterion=      next.getCriterion();
                           if( criterion.getField().getColumn().equalsIgnoreCase("platform")) {

                                result.replaceFirst("platform","pw_r_wall_app.platform") ;

                           }


                        }


                    }
                }
            }*/


      result=  result.replaceFirst("platform","pw_r_wall_app.platform") ;

          if (logger.isDebugEnabled()) {
              logger.debug("The query sql:" + result);
          }

          try {
              //
              page.setTotalRows(queryRowSize(KEY_TABLE_NAME, queryExpress, conn));

              //
              pstmt = conn.prepareStatement(result);

              int index = 1;
              index = ObjectFieldUtil.setStmtParams(pstmt, 1, queryExpress);
              pstmt.setInt(index++, page.getStartRowIdx());
              pstmt.setInt(index++, page.getPageSize());

              rs = pstmt.executeQuery();

              while (rs.next()) {
                  returnValue.add(rsToObjectInnerJoin(rs));
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
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public PointwallWallApp get(QueryExpress queryExpress, Connection conn) throws DbException {
            return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected PointwallWallApp rsToObject(ResultSet rs) throws SQLException {

        PointwallWallApp pointwallWallApp = new PointwallWallApp();
        pointwallWallApp.setAppId(rs.getLong("app_id"));
        pointwallWallApp.setAppkey(rs.getString("appkey"));
        pointwallWallApp.setPlatform(rs.getInt("platform"));
        pointwallWallApp.setDisplayOrder(rs.getInt("display_order"));
        pointwallWallApp.setHotStatus(rs.getInt("hot_status"));
        pointwallWallApp.setPointAmount(rs.getInt("point_amount"));
        pointwallWallApp.setStatus(rs.getString("status"));

        return pointwallWallApp;
    }


    protected PointwallWallApp rsToObjectInnerJoin(ResultSet rs) throws SQLException {

        PointwallWallApp pointwallWallApp = new PointwallWallApp();
        pointwallWallApp.setWallAppId(rs.getLong("wall_app_id"));
        pointwallWallApp.setAppId(rs.getLong("app_id"));
        pointwallWallApp.setAppkey(rs.getString("appkey"));
        pointwallWallApp.setPlatform(rs.getInt("platform"));
        pointwallWallApp.setDisplayOrder(rs.getInt("display_order"));
        pointwallWallApp.setHotStatus(rs.getInt("hot_status"));
        pointwallWallApp.setPointAmount(rs.getInt("point_amount"));
        pointwallWallApp.setStatus(rs.getString("status"));

        pointwallWallApp.setPackageName(rs.getString("package_name"));
        pointwallWallApp.setAppName(rs.getString("app_name"));
        pointwallWallApp.setAppDesc(rs.getString("app_desc"));
        pointwallWallApp.setAppIcon(rs.getString("app_icon"));
        pointwallWallApp.setDownloadUrl(rs.getString("download_url"));

        return pointwallWallApp;
    }

    public String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(app_id,appkey,platform,display_order,hot_status,point_amount,status) values(?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("PointwallWallApp insert sql:" + sql);
        }
        return sql;
    }

}
