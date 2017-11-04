package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.Activity;
import com.enjoyf.platform.service.content.ActivityCategoryType;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.content.ActivityGoodsCategory;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午4:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstactActivityAccessor extends AbstractBaseTableAccessor<Activity> implements ActivityAccessor {

    Logger logger = LoggerFactory.getLogger(AbstactActivityAccessor.class);

    private static final String KEY_TABLE_NAME = "activity";

    @Override
    public Activity insert(Activity activity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, activity.getActivitySubject());
            pstmt.setString(2, activity.getActivityDesc());
            pstmt.setString(3, TextJsonItem.toJson(activity.getTextJsonItemsList()));
            pstmt.setString(4, activity.getActivityPicUrl());
            pstmt.setInt(5, activity.getActivityType().getCode());
            if (!StringUtil.isEmpty(activity.getGameName())) {
                pstmt.setString(6, activity.getGameName());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }
            if (!StringUtil.isEmpty(activity.getGameIconUrl())) {
                pstmt.setString(7, activity.getGameIconUrl());
            } else {
                pstmt.setNull(7, Types.VARCHAR);
            }
            if (!StringUtil.isEmpty(activity.getGameProduct())) {
                pstmt.setString(8, activity.getGameProduct());
            } else {
                pstmt.setNull(8, Types.VARCHAR);
            }
            if (!StringUtil.isEmpty(activity.getGameUrl())) {
                pstmt.setString(9, activity.getGameUrl());
            } else {
                pstmt.setNull(9, Types.VARCHAR);
            }
            if (!StringUtil.isEmpty(activity.getQrUrl())) {
                pstmt.setString(10, activity.getQrUrl());
            } else {
                pstmt.setNull(10, Types.VARCHAR);
            }
            if (!StringUtil.isEmpty(activity.getIosDownloadUrl())) {
                pstmt.setString(11, activity.getIosDownloadUrl());
            } else {
                pstmt.setNull(11, Types.VARCHAR);
            }
            if (activity.getIosSizeMB() > 0) {
                pstmt.setDouble(12, activity.getIosSizeMB());
            } else {
                pstmt.setNull(12, Types.DOUBLE);
            }
            if (!StringUtil.isEmpty(activity.getAndroidDownloadUrl())) {
                pstmt.setString(13, activity.getAndroidDownloadUrl());
            } else {
                pstmt.setNull(13, Types.VARCHAR);
            }
            if (activity.getAndroidSizeMB() > 0) {
                pstmt.setDouble(14, activity.getAndroidSizeMB());
            } else {
                pstmt.setNull(14, Types.DOUBLE);
            }
            pstmt.setTimestamp(15, activity.getStartTime() == null ? null : new Timestamp(activity.getStartTime().getTime()));
            pstmt.setTimestamp(16, activity.getEndTime() == null ? null : new Timestamp(activity.getEndTime().getTime()));
            pstmt.setString(17, activity.getRemoveStatus().getCode());

            pstmt.setString(18, activity.getCreateUserId());
            pstmt.setTimestamp(19, new Timestamp(activity.getCreateTime() == null ? System.currentTimeMillis() : activity.getCreateTime().getTime()));
            pstmt.setString(20, activity.getCreateIp());
            if (activity.getCategory() != null && activity.getCategory().getValue() > 0) {
                pstmt.setInt(21, activity.getCategory().getValue());
            } else {
                pstmt.setNull(21, Types.INTEGER);
            }
            pstmt.setInt(22, activity.getDisplayOrder());
            pstmt.setString(23, activity.getSubDesc());
            pstmt.setLong(24, activity.getShareId());

            pstmt.setString(25, activity.getFirstLetter() == null ? "" : activity.getFirstLetter());

            if (activity.getGoodsCategory() != null) {
                pstmt.setInt(26, activity.getGoodsCategory().getValue());
            } else {
                pstmt.setNull(26, Types.INTEGER);
            }
            if (activity.getActivityPlatform() != null) {
                pstmt.setInt(27, activity.getActivityPlatform().getValue());
            } else {
                pstmt.setNull(27, Types.INTEGER);
            }
            if (activity.getCooperation() != null) {
                pstmt.setInt(28, activity.getCooperation().getValue());
            } else {
                pstmt.setNull(28, Types.INTEGER);
            }
            pstmt.setLong(29, activity.getGameDbId());
            pstmt.setInt(30, activity.getWeixinExclusive().getCode());
            pstmt.setInt(31, activity.getHotActivity());
            pstmt.setInt(32, activity.getGoodsActionType().getCode());
            pstmt.setString(33, activity.getBgPic());
            pstmt.setInt(34, activity.getReserveType());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                activity.setActivityId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return activity;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (activity_subject,activity_desc,activity_descjson,activity_picurl,activity_type,activity_gamename,activity_gameicon,activity_gamedeveloper,activity_game_url,activity_qrurl,activity_iosurl,activity_iossizemb,activity_androidurl,activity_androidsizemb,start_time,end_time,remove_status,createuserid,createtime,createip,category,display_order,sub_desc,activity_share_id,first_letter,goods_category,activity_platform,activity_cooperation,game_db_id,is_exclusive,hot_activity,goods_action_type,bg_pic,reserve_type) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("Activity INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    protected Activity rsToObject(ResultSet rs) throws SQLException {
        Activity returnObj = new Activity();

        //activity_id,activity_subject,activity_desc,start_time,end_time,remove_status,createuserid,createtime,createip

        returnObj.setActivityId(rs.getLong("activity_id"));
        returnObj.setHotActivity(rs.getInt("hot_activity"));
        returnObj.setActivitySubject(rs.getString("activity_subject"));
        returnObj.setActivityDesc(rs.getString("activity_desc"));
        returnObj.setTextJsonItemsList(TextJsonItem.fromJson(rs.getString("activity_descjson")));
        returnObj.setActivityPicUrl(rs.getString("activity_picurl"));
        returnObj.setActivityType(ActivityType.getByCode(rs.getInt("activity_type")));
        returnObj.setGameName(rs.getString("activity_gamename"));
        returnObj.setGameIconUrl(rs.getString("activity_gameicon"));
        returnObj.setGameProduct(rs.getString("activity_gamedeveloper"));
        returnObj.setGameUrl(rs.getString("activity_game_url"));
        returnObj.setQrUrl(rs.getString("activity_qrurl"));
        returnObj.setIosDownloadUrl(rs.getString("activity_iosurl"));
        returnObj.setIosSizeMB(rs.getDouble("activity_iossizemb"));
        returnObj.setAndroidDownloadUrl(rs.getString("activity_androidurl"));
        returnObj.setAndroidSizeMB(rs.getDouble("activity_androidsizemb"));
        returnObj.setStartTime(rs.getDate("start_time"));
        returnObj.setEndTime(rs.getDate("end_time"));
        returnObj.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        returnObj.setCreateUserId(rs.getString("createuserid"));
        returnObj.setCreateIp(rs.getString("createip"));
        returnObj.setCreateTime(rs.getTimestamp("createtime"));
        returnObj.setLastModifyUserId(rs.getString("lastmodifyuserid"));
        returnObj.setLastModifyTime(rs.getTimestamp("lastmodifytime"));
        returnObj.setLastModifyIp(rs.getString("lastmodifyip"));
        returnObj.setCategory(ActivityCategoryType.getByValue(rs.getInt("category")));
        returnObj.setDisplayOrder(rs.getInt("display_order"));
        returnObj.setEventDate(rs.getTimestamp("event_time"));
        returnObj.setSubDesc(rs.getString("sub_desc"));
        returnObj.setShareId(rs.getLong("activity_share_id"));

        returnObj.setFirstLetter(rs.getString("first_letter"));

        returnObj.setActivityPlatform(ActivityPlatform.getByValue(rs.getInt("activity_platform")));
        returnObj.setCooperation(ActivityCooperation.getByValue(rs.getInt("activity_cooperation")));

        returnObj.setGoodsCategory(ActivityGoodsCategory.getByValue(rs.getInt("goods_category")));
        returnObj.setGameDbId(rs.getLong("game_db_id"));
        returnObj.setWeixinExclusive(MobileExclusive.getByCode(rs.getInt("is_exclusive")));
        returnObj.setGoodsActionType(GoodsActionType.getByCode(rs.getInt("goods_action_type")));
        returnObj.setBgPic(rs.getString("bg_pic"));
        returnObj.setReserveType(rs.getInt("reserve_type"));
        return returnObj;
    }

    @Override
    public Activity get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Activity> select(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<Activity> select(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int modify(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }
}
