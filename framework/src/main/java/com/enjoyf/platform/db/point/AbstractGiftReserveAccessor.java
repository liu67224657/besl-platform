package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.usercenter.LoginDomain;
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
 * User: pengxu
 * Date: 13-6-17
 * Time: 下午4:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGiftReserveAccessor extends AbstractBaseTableAccessor<GiftReserve> implements GiftReserveAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractGiftReserveAccessor.class);

    protected static final String KEY_TABLE_NAME = "gift_reserve";


    @Override
    public GiftReserve insert(GiftReserve gift, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, gift.getUno());
            pstmt.setString(2, gift.getGiftName());
            pstmt.setString(3, gift.getCreateIp());
            pstmt.setTimestamp(4, new Timestamp(gift.getCreateTime() == null ? System.currentTimeMillis() : gift.getCreateTime().getTime()));
            pstmt.setString(5, gift.getValidStatus().getCode());
            pstmt.setLong(6, gift.getAid() == null ? 0 : gift.getAid());
            pstmt.setString(7, gift.getLoginDomain().getCode());
            pstmt.setString(8, gift.getAppkey());
            pstmt.setString(9,gift.getProfileId());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                gift.setGiftReserveId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert GiftReserve, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return gift;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    //select * from goods order by displayourder asc
    public List<GiftReserve> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    //select * from goods where valid_status=?
    public List<GiftReserve> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    //select * from goods where goods_id = ? and valid_status='valid'
    public GiftReserve get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected GiftReserve rsToObject(ResultSet rs) throws SQLException {

        GiftReserve giftReserve = new GiftReserve();
        giftReserve.setGiftReserveId(rs.getLong("gift_reserve_id"));
        giftReserve.setUno(rs.getString("uno"));
        giftReserve.setGiftName(rs.getString("gift_name"));
        giftReserve.setCreateIp(rs.getString("create_ip"));
        giftReserve.setCreateTime(rs.getTimestamp("create_time"));
        giftReserve.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        giftReserve.setAid(rs.getLong("aid"));
        giftReserve.setLoginDomain(LoginDomain.getByCode(rs.getString("account_domain")));
        giftReserve.setProfileId(rs.getString("profileid"));
        giftReserve.setAppkey(rs.getString("appkey"));
        return giftReserve;
    }

    public String getInsertSql() {
        String sql = "insert into " + KEY_TABLE_NAME + "(uno,gift_name,create_ip,create_time,valid_status,aid,account_domain,appkey,profileid) values(?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("GiftReserve insert sql:" + sql);
        }
        return sql;
    }

}
