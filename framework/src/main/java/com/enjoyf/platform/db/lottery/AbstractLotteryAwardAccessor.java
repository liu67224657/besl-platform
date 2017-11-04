package com.enjoyf.platform.db.lottery;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.LotteryAward;
import com.enjoyf.platform.service.lottery.LotteryAwardField;
import com.enjoyf.platform.service.lottery.LotteryAwardRule;
import com.enjoyf.platform.service.lottery.LotteryAwardType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-24
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLotteryAwardAccessor extends AbstractSequenceBaseTableAccessor<LotteryAward> implements LotteryAwardAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractLotteryAwardAccessor.class);

    private static final String KEY_SEQUENCE_NAME = "SEQ_LOTTERY_AWARD_ID";
    protected static final String KEY_TABLE_NAME = "lottery_award";

    @Override
    public LotteryAward insert(LotteryAward lotteryAward, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            lotteryAward.setLotteryAwardId(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setLong(1, lotteryAward.getLotteryAwardId());
            pstmt.setLong(2, lotteryAward.getLotteryId());
            pstmt.setInt(3, lotteryAward.getLotteryAwardLevel());
            pstmt.setString(4, lotteryAward.getLotteryAwardName());
            pstmt.setString(5, lotteryAward.getLotteryAwardDesc());
            pstmt.setString(6, lotteryAward.getLotteryAwardPic());
            pstmt.setInt(7, lotteryAward.getLotteryAwardAmount());
            pstmt.setInt(8, lotteryAward.getLotteryAwardRestAmount());
            pstmt.setInt(9, lotteryAward.getLotteryAwardMinRate());
            pstmt.setInt(10, lotteryAward.getLotteryAwardMaxRate());
            pstmt.setTimestamp(11, new Timestamp(lotteryAward.getCreateDate() == null ? System.currentTimeMillis() : lotteryAward.getCreateDate().getTime()));
            pstmt.setString(12, lotteryAward.getCreateIp());
            pstmt.setTimestamp(13, lotteryAward.getLastModifyDate() == null ? null : new Timestamp(lotteryAward.getLastModifyDate().getTime()));
            pstmt.setString(14, lotteryAward.getLastModifyIp() == null ? null : lotteryAward.getLastModifyIp());
            pstmt.setInt(15, lotteryAward.getLotteryAwardType().getCode());
            pstmt.setString(16, lotteryAward.getValidStatus().getCode());
            if (!CollectionUtil.isEmpty(lotteryAward.getLotteryCode())) {
                Set<Integer> set = lotteryAward.getLotteryCode();
                StringBuffer stringBuffer = new StringBuffer();
                for (Integer i : set) {
                    stringBuffer.append(i + ",");
                }
                pstmt.setString(17, stringBuffer.substring(0, stringBuffer.toString().length() - 1).toString());
            } else {
                pstmt.setString(17, null);
            }

            pstmt.setString(18, lotteryAward.getAwardRule() == null ? null : lotteryAward.getAwardRule().toJson());


            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert AppErrorInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return lotteryAward;
    }

    @Override
    public int update(UpdateExpress updateExpress, long lotteryAwardId, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, new QueryExpress().add(QueryCriterions.eq(LotteryAwardField.LOTTERY_AWARD_ID, lotteryAwardId)), conn);
    }

    @Override
    public List<LotteryAward> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<LotteryAward> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public LotteryAward getById(long lotteryAwardId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(LotteryAwardField.LOTTERY_AWARD_ID, lotteryAwardId)), conn);
    }

    @Override
    public LotteryAward getByRate(long lotteryId, int randomNum, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress()
                .add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId))
                .add(QueryCriterions.leq(LotteryAwardField.AWARD_MIN_RATE, randomNum))
                .add(QueryCriterions.geq(LotteryAwardField.AWARD_MAX_RATE, randomNum))
                .add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode())), conn);
    }

    @Override
    public LotteryAward getByLevel(int awardLevel, long lotteryId, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, new QueryExpress()
                .add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId))
                .add(QueryCriterions.eq(LotteryAwardField.LOTTERY_AWARD_LEVEL, awardLevel))
                .add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode())), conn);
    }

    @Override
    public LotteryAward getByRestAmount(long lotteryId, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE lottery_id=? AND lottery_award_rest_amount > 0 AND valid_status=? ORDER BY lottery_award_level DESC limit 1";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setLong(1, lotteryId);
            pstmt.setString(2, ValidStatus.VALID.getCode());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return null;
    }

    @Override
    protected LotteryAward rsToObject(ResultSet rs) throws SQLException {
        LotteryAward lotteryAward = new LotteryAward();
        lotteryAward.setLotteryAwardId(rs.getLong("lottery_award_id"));
        lotteryAward.setLotteryId(rs.getLong("lottery_id"));
        lotteryAward.setLotteryAwardLevel(rs.getInt("lottery_award_level"));
        lotteryAward.setLotteryAwardName(rs.getString("lottery_award_name"));
        lotteryAward.setLotteryAwardDesc(rs.getString("lottery_award_description"));
        lotteryAward.setLotteryAwardPic(rs.getString("lottery_award_pic"));
        lotteryAward.setLotteryAwardAmount(rs.getInt("lottery_award_amount"));
        lotteryAward.setLotteryAwardRestAmount(rs.getInt("lottery_award_rest_amount"));
        lotteryAward.setLotteryAwardMinRate(rs.getInt("award_min_rate"));
        lotteryAward.setLotteryAwardMaxRate(rs.getInt("award_max_rate"));
        lotteryAward.setCreateDate(rs.getTimestamp("createdate"));
        lotteryAward.setCreateIp(rs.getString("createip"));
        lotteryAward.setLastModifyDate(rs.getTimestamp("lastmodifydate"));
        lotteryAward.setLastModifyIp(rs.getString("lastmodifyip"));
        lotteryAward.setLotteryAwardType(LotteryAwardType.getByCode(rs.getInt("lottery_award_type")));
        lotteryAward.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        String lottery_code = rs.getString("lottery_code");
        if (!StringUtil.isEmpty(lottery_code)) {
            String arr[] = lottery_code.split(",");
            Set<Integer> lottery_code_set = new HashSet<Integer>();
            for (String a : arr) {
                lottery_code_set.add(Integer.valueOf(a));
            }
            lotteryAward.setLotteryCode(lottery_code_set);
        }
        String lottery_award_rule = rs.getString("lottery_award_rule");
        if (!StringUtil.isEmpty(lottery_award_rule)) {
            lotteryAward.setAwardRule(LotteryAwardRule.fromJson(lottery_award_rule));
        }

        return lotteryAward;
    }

    //lottery_award_id,lottery_id,lottery_award_level,lottery_award_name,lottery_award_description,lottery_award_pic,lottery_award_amount,lottery_award_rest_amount,award_min_rate,award_max_rate,createdate,createip,lastmodifydate,lastmodifyip
    public String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(lottery_award_id,lottery_id,lottery_award_level,lottery_award_name,lottery_award_description,lottery_award_pic,lottery_award_amount,lottery_award_rest_amount,award_min_rate,award_max_rate,createdate,createip,lastmodifydate,lastmodifyip,lottery_award_type,valid_status,lottery_code,lottery_award_rule) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("lotteryAward insert sql" + sql);
        }
        return sql;
    }
}
