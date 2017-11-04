package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.apache.openjpa.jdbc.sql.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-24
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLotteryAccessor extends AbstractSequenceBaseTableAccessor<Lottery> implements LotteryAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractLotteryAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "SEQ_LOTTERY_ID";
    protected static final String KEY_TABLE_NAME = "lottery";

    @Override
    public Lottery insert(Lottery lottery, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            lottery.setLotteryId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, lottery.getLotteryId());
            pstmt.setString(2, lottery.getLotteryName());
            pstmt.setString(3, lottery.getLotteryDesc());
            pstmt.setInt(4, lottery.getBaseRate());
            pstmt.setInt(5, lottery.getAwardLevelCount());
            pstmt.setInt(6, lottery.getLotteryType().getCode());
            pstmt.setString(7, lottery.getValidStatus().getCode());
            pstmt.setTimestamp(8, new Timestamp(lottery.getCreateDate() == null ? System.currentTimeMillis() : lottery.getCreateDate().getTime()));
            pstmt.setString(9, lottery.getCreateIp());
            pstmt.setTimestamp(10, lottery.getLastModifyDate() == null ? null : new Timestamp(lottery.getLastModifyDate().getTime()));
            pstmt.setString(11, lottery.getLastModifyIp() == null ? null : lottery.getLastModifyIp());
            pstmt.setInt(12, lottery.getLotteryTimesType().getCode());
            pstmt.setLong(13, lottery.getShareId());

            pstmt.setString(14, lottery.getLotteryRule() == null ? "" : lottery.getLotteryRule().toJson());
            pstmt.setTimestamp(15, lottery.getStartDate() == null ? null : new Timestamp(lottery.getStartDate().getTime()));
            pstmt.setTimestamp(16, lottery.getEndDate() == null ? null : new Timestamp(lottery.getEndDate().getTime()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            GAlerter.lab("On insert AppErrorInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return lottery;
    }

    @Override
    public int update(UpdateExpress updateExpress, long lotteryId, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, new QueryExpress().add(QueryCriterions.eq(LotteryField.LOTTERY_ID, lotteryId)), conn);
    }

    @Override
    public List<Lottery> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<Lottery> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public Lottery get(long lotteryId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(LotteryField.LOTTERY_ID, lotteryId)).add(QueryCriterions.ne(LotteryField.VALID_STATUS, ValidStatus.REMOVED.getCode())), conn);
    }

    @Override
    protected Lottery rsToObject(ResultSet rs) throws SQLException {
        Lottery lottery = new Lottery();
        lottery.setLotteryId(rs.getLong("lottery_id"));
        lottery.setLotteryName(rs.getString("lottery_name"));
        lottery.setLotteryDesc(rs.getString("lottery_desc"));
        lottery.setBaseRate(rs.getInt("base_rate"));
        lottery.setAwardLevelCount(rs.getInt("award_level_count"));
        lottery.setLotteryType(LotteryType.getByCode(rs.getInt("lottery_type")));
        lottery.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        lottery.setCreateDate(rs.getTimestamp("createdate"));
        lottery.setCreateIp(rs.getString("createip"));
        lottery.setLastModifyDate(rs.getTimestamp("lastmodifydate"));
        lottery.setLastModifyIp(rs.getString("lastmodifyip"));
        lottery.setLotteryTimesType(LotteryTimesType.getByCode(rs.getInt("lottery_times_type")));
        lottery.setShareId(rs.getLong("share_id"));

        String lotter_rule = rs.getString("lottery_rule");
        if (!StringUtil.isEmpty(lotter_rule)) {
            lottery.setLotteryRule(LotteryRule.fromJson(lotter_rule));
        }

        Timestamp start_date = rs.getTimestamp("start_date");
        if (start_date != null) {
            lottery.setStartDate(start_date);
        }
        Timestamp end_date = rs.getTimestamp("end_date");
        if (end_date != null) {
            lottery.setEndDate(end_date);
        }
        return lottery;
    }

    //lottery_id,lottery_name,lottery_desc,base_rate,award_level_count,lottery_type,valid_status,createdate,createip,lastmodifydate,lastmodifyip,lottery_times_type,share_id
    public String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(lottery_id,lottery_name,lottery_desc,base_rate,award_level_count,lottery_type,valid_status,createdate,createip,lastmodifydate,lastmodifyip,lottery_times_type,share_id,lottery_rule,start_date,end_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("lottery insert sql" + sql);
        }
        return sql;
    }
}
