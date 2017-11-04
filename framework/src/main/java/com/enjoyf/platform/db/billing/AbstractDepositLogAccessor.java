package com.enjoyf.platform.db.billing;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.billing.DepositLog;
import com.enjoyf.platform.service.billing.BillingUtil;
import com.enjoyf.platform.service.billing.DepositChannel;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-6
 * Time: 上午10:02
 * To change this template use File | Settings | File Templates.
 */
abstract class AbstractDepositLogAccessor extends AbstractBaseTableAccessor<DepositLog> implements DepositLogAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDepositLogAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "billing_deposit_log";


    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME_PREFIX + " (deposit_log_id, oriderid, profileid, appkey, zonekey, third_channel, amount, currency," +
                " paytype, status, feetype, transtype, transid, deposittime, depositip,info,appchannel,syncstatus,errormsg,productid,platform,productname,thirdorderid,synctimes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("DepositLog INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    public DepositLog insert(DepositLog log, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            log.setLogId(BillingUtil.getLogId(log.getOrderId(), log.getChannel()));

            //deposit_log_id, oriderid, profileid, appkey, zonekey, third_channel, amount, currency,paytype, status, feetype, transtype, transid, deposittime, depositip,info,synctimes
            pstmt.setString(1, log.getLogId());
            pstmt.setString(2, log.getOrderId());
            pstmt.setString(3, log.getProfileId());
            pstmt.setString(4, log.getAppKey());
            pstmt.setString(5, log.getZoneKey());
            pstmt.setString(6, log.getChannel().getCode());
            pstmt.setInt(7, log.getAmount());
            pstmt.setString(8, log.getCurrency());
            pstmt.setString(9, log.getPaytype());
            pstmt.setInt(10, log.getStatus().getCode());
            pstmt.setString(11, log.getFeeType());
            pstmt.setString(12, log.getTransType());
            pstmt.setString(13, log.getTransId());
            pstmt.setTimestamp(14, new Timestamp(log.getDepositTime() == null ? System.currentTimeMillis() : log.getDepositTime().getTime()));

            pstmt.setString(15, log.getDepositIp());
            pstmt.setString(16, log.getInfo());
            pstmt.setString(17, log.getAppChannel());
            pstmt.setInt(18, log.getSyncStatus().getCode());
            pstmt.setString(19, log.getErrorMsg());
            pstmt.setString(20, log.getProductId());
            pstmt.setInt(21, log.getAppPlatform() != null ? log.getAppPlatform().getCode() : -1);
            pstmt.setString(22, log.getProducetName());
            pstmt.setString(23, log.getThirdOrderid());
            pstmt.setInt(24, log.getSyncTimes());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() != 1062) {
                GAlerter.lab("On insert DepositLog, occured SQLException.e:", e);
                throw new DbException(e);
            }else{
                logger.error("On insert DepositLog, occured SQLException.e:", e);
            }
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return log;
    }

    @Override
    public DepositLog get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME_PREFIX, queryExpress, conn);
    }

    @Override
    public List<DepositLog> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME_PREFIX, queryExpress, conn);
    }

    @Override
    public List<DepositLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME_PREFIX, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME_PREFIX, updateExpress, queryExpress, conn);
    }

    @Override
    public String queryBySql(String sql, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd; //取得元数据
        StringBuffer resultStr = new StringBuffer("");
        int columnCount; //返回结果有多少列
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();

            resultStr.append("<table border=1>");
            resultStr.append("<tr>");
            for (int j = 1; j <= columnCount; j++) {
                resultStr.append("<td align=left>&nbsp;");
                resultStr.append(rsmd.getColumnName(j));
                resultStr.append("</td>");
            }
            resultStr.append("</tr>");
            while (rs.next()) {
                resultStr.append("<tr>");
                for (int j = 1; j <= columnCount; j++) {
                    resultStr.append("<td align=left>&nbsp;");
                    resultStr.append(rs.getString(j));
                    resultStr.append("</td>");
                }
                resultStr.append("</tr>");

            }
            resultStr.append("</table>");
            rs.last();
            resultStr.append("<table><tr><td align=left>&nbsp;本次查询一共有" + rs.getRow() + "条记录</td></tr></table>");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return resultStr.toString();
    }

    @Override
    protected DepositLog rsToObject(ResultSet rs) throws SQLException {
        DepositLog log = new DepositLog();
        log.setLogId(rs.getString("deposit_log_id"));
        log.setOrderId(rs.getString("oriderid"));
        log.setProfileId(rs.getString("profileid"));
        log.setAppKey(rs.getString("appkey"));
        log.setZoneKey(rs.getString("zonekey"));
        log.setChannel(DepositChannel.getByCode(rs.getString("third_channel")));
        log.setAmount(rs.getInt("amount"));
        log.setCurrency(rs.getString("currency"));
        log.setPaytype(rs.getString("paytype"));
        log.setStatus(IntValidStatus.getByCode(rs.getInt("status")));
        log.setFeeType(rs.getString("feetype"));
        log.setTransType(rs.getString("transtype"));
        log.setTransId(rs.getString("transid"));
        log.setDepositTime(new Date(rs.getTimestamp("deposittime").getTime()));
        log.setDepositIp(rs.getString("depositip"));
        log.setInfo(rs.getString("info"));
        log.setAppChannel(rs.getString("appchannel"));
        log.setSyncStatus(IntValidStatus.getByCode(rs.getInt("syncstatus")));
        log.setErrorMsg(rs.getString("errormsg"));
        log.setProductId(rs.getString("productid"));
        log.setAppPlatform(AppPlatform.getByCode(rs.getInt("platform")));
        log.setProducetName(rs.getString("productname"));
        log.setThirdOrderid(rs.getString("thirdorderid"));
        log.setSyncTimes(rs.getInt("synctimes"));
        return log;
    }

}
