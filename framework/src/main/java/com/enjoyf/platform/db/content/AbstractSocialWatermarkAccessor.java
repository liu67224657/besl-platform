package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.social.SocialWatermark;
import com.enjoyf.platform.service.content.social.Subscript;
import com.enjoyf.platform.service.content.social.SubscriptType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
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
 * Date: 14-5-27
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialWatermarkAccessor extends AbstractBaseTableAccessor<SocialWatermark> implements SocialWatermarkAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialWatermarkAccessor.class);
    private static final String KEY_TABLE_NAME = "social_watermark";

    @Override
    public SocialWatermark insert(SocialWatermark socialWaterMark, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            //title,description,ios_pic,android_pic,start_date,end_date,is_hot,is_new,remove_status,display_order,use_sum,create_date,create_ip,create_userid
            pstmt.setString(1, socialWaterMark.getTitle());
            pstmt.setString(2, socialWaterMark.getDescription());
            pstmt.setString(3, socialWaterMark.getIosPic());
            pstmt.setString(4, socialWaterMark.getAndroidPic());
            pstmt.setString(5, socialWaterMark.getSubscript().toJsonStr());
            pstmt.setString(6, socialWaterMark.getRemoveStatus() == null ? ValidStatus.INVALID.getCode() : socialWaterMark.getRemoveStatus().getCode());
            pstmt.setInt(7, socialWaterMark.getDisplayOrder());
            pstmt.setInt(8, socialWaterMark.getUseSum());
            pstmt.setTimestamp(9, new Timestamp(socialWaterMark.getCreateDate() == null ? System.currentTimeMillis() : socialWaterMark.getCreateDate().getTime()));
            pstmt.setString(10, socialWaterMark.getCreateIp() == null ? "" : socialWaterMark.getCreateIp());
            pstmt.setString(11, socialWaterMark.getCreateUserId() == null ? "" : socialWaterMark.getCreateUserId());
            pstmt.setLong(12, socialWaterMark.getActivityId());
            pstmt.setInt(13, socialWaterMark.getSubscriptType() == null ? SubscriptType.NULL.getCode() : socialWaterMark.getSubscriptType().getCode());
            pstmt.setString(14, socialWaterMark.getIosIcon() == null ? "" : socialWaterMark.getIosIcon());
            pstmt.setString(15, socialWaterMark.getAndroidIcon() == null ? "" : socialWaterMark.getAndroidIcon());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                socialWaterMark.setWatermarkId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On INSERT SocialWaterMark, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialWaterMark;
    }

    @Override
    public SocialWatermark get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialWatermark> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialWatermark> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
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

        String sql = "SELECT display_order FROM " + KEY_TABLE_NAME + " ORDER BY display_order DESC LIMIT 0,1";
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
    public List<SocialWatermark> query(NextPagination nextPagination, Connection conn) throws DbException {
        List<SocialWatermark> list = new ArrayList<SocialWatermark>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE remove_status=? AND display_order" + (nextPagination.isNext() ? "> ? ORDER BY display_order ASC" : "< ? ORDER BY display_order DESC") + "  LIMIT 0, ?";
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
    protected SocialWatermark rsToObject(ResultSet rs) throws SQLException {
        SocialWatermark socialWaterMark = new SocialWatermark();
        socialWaterMark.setWatermarkId(rs.getLong("watermark_id"));
        socialWaterMark.setTitle(rs.getString("title"));
        socialWaterMark.setDescription(rs.getString("description"));
        socialWaterMark.setIosIcon(rs.getString("ios_icon"));
        socialWaterMark.setAndroidIcon(rs.getString("android_icon"));
        socialWaterMark.setIosPic(rs.getString("ios_pic"));
        socialWaterMark.setAndroidPic(rs.getString("android_pic"));
        socialWaterMark.setSubscript(Subscript.parse(rs.getString("subscript")));
        socialWaterMark.setSubscriptType(SubscriptType.getByCode(rs.getInt("subscript_type")));
        socialWaterMark.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        socialWaterMark.setDisplayOrder(rs.getInt("display_order"));
        socialWaterMark.setUseSum(rs.getInt("use_sum"));
        socialWaterMark.setCreateDate(rs.getTimestamp("create_date"));
        socialWaterMark.setCreateIp(rs.getString("create_ip"));
        socialWaterMark.setCreateUserId(rs.getString("create_userid"));
        socialWaterMark.setModifyDate(rs.getTimestamp("modify_date"));
        socialWaterMark.setModifyIp(rs.getString("modify_ip"));
        socialWaterMark.setModifyUserId(rs.getString("modify_userid"));
        socialWaterMark.setActivityId(rs.getLong("activity_id"));
        return socialWaterMark;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(title,description,ios_pic,android_pic,subscript," +
                "remove_status,display_order,use_sum,create_date,create_ip," +
                "create_userid,activity_id,subscript_type,ios_icon,android_icon) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("SocialWaterMark INSERT sql:" + sql);
        }
        return sql;
    }
}
