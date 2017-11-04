package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.social.SocialBackgroundAudio;
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
public abstract class AbstractSocialBackgroundAudioAccessor extends AbstractBaseTableAccessor<SocialBackgroundAudio> implements SocialBackgroundAudioAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialBackgroundAudioAccessor.class);
    private static final String KEY_TABLE_NAME = "social_bgaudio";

    @Override
    public SocialBackgroundAudio insert(SocialBackgroundAudio backgroundAudio, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            //title,description,ios_pic,android_pic,start_date,end_date,is_hot,is_new,remove_status,display_order,use_sum,create_date,create_ip,create_userid
            pstmt.setString(1, backgroundAudio.getAudioName());
            pstmt.setString(2, backgroundAudio.getAudioDescription());
            pstmt.setString(3, backgroundAudio.getSinger());
            pstmt.setString(4, backgroundAudio.getMp3Src());
            pstmt.setString(5, backgroundAudio.getWavSrc());
            pstmt.setString(6, backgroundAudio.getRemoveStatus() == null ? ValidStatus.INVALID.getCode() : backgroundAudio.getRemoveStatus().getCode());
            pstmt.setInt(7, backgroundAudio.getDisplayOrder());
            pstmt.setInt(8, backgroundAudio.getUseSum());
            pstmt.setTimestamp(9, new Timestamp(backgroundAudio.getCreateDate() == null ? System.currentTimeMillis() : backgroundAudio.getCreateDate().getTime()));
            if (backgroundAudio.getCreateIp() == null) {
                pstmt.setNull(10, Types.VARCHAR);
            } else {
                pstmt.setString(10, backgroundAudio.getCreateIp());
            }
            if (backgroundAudio.getCreateUserId() == null) {
                pstmt.setNull(11, Types.VARCHAR);
            } else {
                pstmt.setString(11, backgroundAudio.getCreateUserId());
            }
            pstmt.setString(12, backgroundAudio.getAudioPic() == null ? "" : backgroundAudio.getAudioPic());
            pstmt.setInt(13, backgroundAudio.getSubscriptType() == null ? SubscriptType.NULL.getCode() : backgroundAudio.getSubscriptType().getCode());
            pstmt.setString(14, backgroundAudio.getSubscript().toJsonStr());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                backgroundAudio.setAudioId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On INSERT SocialWaterMark, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return backgroundAudio;
    }

    @Override
    public SocialBackgroundAudio get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialBackgroundAudio> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialBackgroundAudio> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
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
    public List<SocialBackgroundAudio> query(NextPagination nextPagination, Connection conn) throws DbException {
        List<SocialBackgroundAudio> list = new ArrayList<SocialBackgroundAudio>();

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
    protected SocialBackgroundAudio rsToObject(ResultSet rs) throws SQLException {
        SocialBackgroundAudio backgroundAudio = new SocialBackgroundAudio();
        backgroundAudio.setAudioId(rs.getLong("audio_id"));
        backgroundAudio.setAudioName(rs.getString("audio_name"));
        backgroundAudio.setAudioPic(rs.getString("audio_pic"));
        backgroundAudio.setAudioDescription(rs.getString("audio_description"));
        backgroundAudio.setSinger(rs.getString("singer"));
        backgroundAudio.setMp3Src(rs.getString("mp3_src"));
        backgroundAudio.setWavSrc(rs.getString("wav_src"));
        backgroundAudio.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        backgroundAudio.setDisplayOrder(rs.getInt("display_order"));
        backgroundAudio.setUseSum(rs.getInt("use_sum"));
        backgroundAudio.setSubscriptType(SubscriptType.getByCode(rs.getInt("subscript_type")));
        backgroundAudio.setSubscript(Subscript.parse(rs.getString("subscript")));
        backgroundAudio.setCreateDate(rs.getTimestamp("create_date"));
        backgroundAudio.setCreateIp(rs.getString("create_ip"));
        backgroundAudio.setCreateUserId(rs.getString("create_userid"));
        backgroundAudio.setModifyDate(rs.getTimestamp("modify_date"));
        backgroundAudio.setModifyIp(rs.getString("modify_ip"));
        backgroundAudio.setModifyUserId(rs.getString("modify_userid"));
        return backgroundAudio;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(audio_name,audio_description,singer,mp3_src,wav_src," +
                "remove_status,display_order,use_sum,create_date,create_ip,create_userid,audio_pic,subscript_type," +
                "subscript) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("SocialWaterMark INSERT sql:" + sql);
        }
        return sql;
    }
}
