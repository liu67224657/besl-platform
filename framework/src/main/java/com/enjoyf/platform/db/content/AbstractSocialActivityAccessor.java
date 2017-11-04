package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.social.SocialActivity;
import com.enjoyf.platform.service.content.social.SocialAwardSet;
import com.enjoyf.platform.service.content.social.Subscript;
import com.enjoyf.platform.service.content.social.SubscriptType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-12
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialActivityAccessor extends AbstractBaseTableAccessor<SocialActivity> implements SocialActivityAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialActivityAccessor.class);
    private static final String KEY_TABLE_NAME = "social_activity";

    @Override
    public SocialActivity insert(SocialActivity socialActivity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, socialActivity.getTitle());
            pstmt.setString(2, socialActivity.getDescription());
            pstmt.setString(3, socialActivity.getIosIcon());
            pstmt.setString(4, socialActivity.getAndroidIcon());
            pstmt.setString(5, socialActivity.getIosSmallPic());
            pstmt.setString(6, socialActivity.getAndroidSmallPic());
            pstmt.setString(7, socialActivity.getIosBigPic());
            pstmt.setString(8, socialActivity.getAndroidBigPic());
            pstmt.setInt(9, socialActivity.getDisplayOrder());
            pstmt.setString(10, socialActivity.getRemoveStatus() == null ? ValidStatus.VALID.getCode() : socialActivity.getRemoveStatus().getCode());
            pstmt.setString(11, socialActivity.getSubscript().toJsonStr());
            pstmt.setLong(12, socialActivity.getShareId());
            pstmt.setString(13, socialActivity.getAwardSet().toJsonStr());
            pstmt.setTimestamp(14, new Timestamp(socialActivity.getCreateDate() == null ? System.currentTimeMillis() : socialActivity.getCreateDate().getTime()));
            pstmt.setString(15, socialActivity.getCreateIp() == null ? "" : socialActivity.getCreateIp());
            pstmt.setString(16, socialActivity.getCreateUserId() == null ? "" : socialActivity.getCreateUserId());
            pstmt.setInt(17, socialActivity.getSubscriptType() == null ? SubscriptType.NULL.getCode() : socialActivity.getSubscriptType().getCode());
            pstmt.setInt(18, socialActivity.getTotals());
            pstmt.setInt(19, socialActivity.getRedirectType());
            pstmt.setString(20, StringUtil.isEmpty(socialActivity.getRedirectUrl()) ? "" : socialActivity.getRedirectUrl());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                socialActivity.setActivityId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert SocialActivity, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialActivity;
    }

    @Override
    public SocialActivity get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialActivity> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialActivity> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int getMaxDisplayOrder(Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT MAX(display_order) FROM " + KEY_TABLE_NAME;
        if (logger.isDebugEnabled()) {
            logger.debug("The get sql:" + sql);
        }
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On get, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return 0;
    }

    @Override
    public List<SocialActivity> query(NextPagination nextPagination, Connection conn) throws DbException {
        List<SocialActivity> list = new ArrayList<SocialActivity>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE remove_status=? AND display_order" + (nextPagination.isNext() ? "< ? ORDER BY display_order DESC" : "> ? ORDER BY display_order ASC") + "  LIMIT 0, ?";
        if (nextPagination.getStartId() <= 0l) {
            sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE remove_status=? ORDER BY display_order DESC  LIMIT 0, ?";
        }
        String minSql = "SELECT display_order FROM " + KEY_TABLE_NAME + " WHERE remove_status=? ORDER BY display_order ASC LIMIT 0,1";
        int minOrder = 0;
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + sql);
        }

        try {
            //
            pstmt = conn.prepareStatement(sql);

            if (nextPagination.getStartId() <= 0l) {
                pstmt.setString(1, ValidStatus.VALID.getCode());
                pstmt.setInt(2, nextPagination.getPageSize());
            } else {
                pstmt.setString(1, ValidStatus.VALID.getCode());
                pstmt.setLong(2, nextPagination.getStartId());
                pstmt.setInt(3, nextPagination.getPageSize());
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rsToObject(rs));
            }

            if (!CollectionUtil.isEmpty(list)) {
                nextPagination.setNextId(list.get(list.size() - 1).getDisplayOrder());
            }

            pstmt = conn.prepareStatement(minSql);
            pstmt.setString(1, ValidStatus.VALID.getCode());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                minOrder = rs.getInt("display_order");
            }

            nextPagination.setQueryRowsNum(list.size());

            if ((list.get(list.size() - 1).getDisplayOrder()) == minOrder) {
                nextPagination.setLast(true);
            } else {
                nextPagination.setLast(false);
            }

        } catch (SQLException e) {
            GAlerter.lab("On query, a SQLException occured:", e);
            throw new DbException(DbException.SQL_GENERIC, e);

        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return list;
    }

    @Override
    protected SocialActivity rsToObject(ResultSet rs) throws SQLException {
        SocialActivity socialActivity = new SocialActivity();
        socialActivity.setActivityId(rs.getLong("activity_id"));
        socialActivity.setTitle(rs.getString("title"));
        socialActivity.setDescription(rs.getString("description"));
        socialActivity.setIosIcon(rs.getString("ios_icon"));
        socialActivity.setAndroidIcon(rs.getString("android_icon"));
        socialActivity.setIosBigPic(rs.getString("ios_big_pic"));
        socialActivity.setAndroidBigPic(rs.getString("android_big_pic"));
        socialActivity.setIosSmallPic(rs.getString("ios_small_pic"));
        socialActivity.setAndroidSmallPic(rs.getString("android_small_pic"));
        socialActivity.setDisplayOrder(rs.getInt("display_order"));
        socialActivity.setUseSum(rs.getInt("use_sum"));
        socialActivity.setReplySum(rs.getInt("reply_sum"));
        socialActivity.setAgreeSum(rs.getInt("agree_sum"));
        socialActivity.setGiftSum(rs.getInt("gift_sum"));
        socialActivity.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        socialActivity.setSubscript(Subscript.parse(rs.getString("subscript")));
        socialActivity.setShareId(rs.getLong("share_id"));
        socialActivity.setAwardSet(SocialAwardSet.parse(rs.getString("awards")));
        socialActivity.setCreateDate(rs.getTimestamp("create_date"));
        socialActivity.setCreateIp(rs.getString("create_ip"));
        socialActivity.setCreateUserId(rs.getString("create_userid"));
        socialActivity.setLastModifyDate(rs.getTimestamp("modify_date"));
        socialActivity.setLastModifyIp(rs.getString("modify_ip"));
        socialActivity.setLastModifyUserId(rs.getString("modify_userid"));
        socialActivity.setBindStatus(rs.getString("bind_status"));
        socialActivity.setSubscriptType(SubscriptType.getByCode(rs.getInt("subscript_type")));
        socialActivity.setTotals(rs.getInt("totals"));
        socialActivity.setRedirectType(rs.getInt("retype"));
        socialActivity.setRedirectUrl(rs.getString("reurl"));
        return socialActivity;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(title,description,ios_icon,android_icon,ios_small_pic,android_small_pic," +
                "ios_big_pic,android_big_pic,display_order,remove_status,subscript,share_id,awards," +
                "create_date,create_ip,create_userid,subscript_type,totals,retype,reurl) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("SocialActivity insert sql:" + sql);
        }
        return sql;
    }

}
