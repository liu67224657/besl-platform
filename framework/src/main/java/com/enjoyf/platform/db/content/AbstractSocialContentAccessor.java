package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午4:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialContentAccessor extends AbstractBaseTableAccessor<SocialContent> implements SocialContentAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractForignContentAccessor.class);

    private static final String TABLE_NAME = "social_content";

    @Override
    public SocialContent insert(SocialContent socialContent, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            String pic = JsonBinder.buildNormalBinder().toJson(socialContent.getPic());
            String audio = JsonBinder.buildNormalBinder().toJson(socialContent.getAudio());
            //content_url,content_domain,reply_sum,content_title,content_desc,remove_status
            pstmt.setString(1, socialContent.getUno());
            pstmt.setString(2, socialContent.getTitle());
            pstmt.setString(3, socialContent.getBody());
            pstmt.setString(4, pic);
            pstmt.setString(5, audio);
            pstmt.setLong(6, socialContent.getAudioLen());

            pstmt.setInt(7, socialContent.getReplyNum());
            pstmt.setInt(8, socialContent.getAgreeNum());
            pstmt.setInt(9, socialContent.getReadNum());
            pstmt.setInt(10, socialContent.getPlayNum());

            pstmt.setTimestamp(11, new Timestamp(socialContent.getCreateTime() == null ? System.currentTimeMillis() : socialContent.getCreateTime().getTime()));

            pstmt.setTimestamp(12, new Timestamp(socialContent.getUpdateTime() == null ? System.currentTimeMillis() : socialContent.getUpdateTime().getTime()));
            pstmt.setString(13, socialContent.getRemoveStatus().getCode());
            if (socialContent.getLon() == null) {
                pstmt.setNull(14, Types.DOUBLE);
            } else {
                pstmt.setDouble(14, socialContent.getLon());
            }
            if (socialContent.getLat() == null) {
                pstmt.setNull(15, Types.DOUBLE);
            } else {
                pstmt.setDouble(15, socialContent.getLat());
            }
            pstmt.setInt(16, socialContent.getSocialContentPlatformDomain() == null ? SocialContentPlatformDomain.DEFAULT.getCode() : socialContent.getSocialContentPlatformDomain().getCode());
            pstmt.setDouble(17, socialContent.getActivityId());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                socialContent.setContentId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return socialContent;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + "(uno,title,body,pic,audio,audio_len,reply_num,agree_num," +
                "read_num,play_num,create_time,update_time,remove_status,lon,lat,platform,activity_id)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }

    @Override
    public SocialContent getByContentId(QueryExpress queryExpress, Connection connection) throws DbException {
        return super.get(TABLE_NAME, queryExpress, connection);
    }

    @Override
    public SocialContent getByContentId(long contentId, Connection connection) throws DbException {
        return super.get(TABLE_NAME, new QueryExpress().add(QueryCriterions.eq(SocialContentField.CONTENTID, contentId)), connection);
    }

    @Override
    public List<SocialContent> queryByUno(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, page, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
    }

    @Override
    public List<SocialContent> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected SocialContent rsToObject(ResultSet rs) throws SQLException {
        SocialContent socialContent = new SocialContent();
        socialContent.setContentId(rs.getLong("content_id"));
        socialContent.setUno(rs.getString("uno"));
        socialContent.setTitle(rs.getString("title"));
        socialContent.setBody(rs.getString("body"));
        String picStr = rs.getString("pic");
        String audioStr = rs.getString("audio");
        if (!StringUtil.isEmpty(picStr)) {
            SocialConetntAppImages pic = JsonBinder.buildNormalBinder().fromJson(picStr, SocialConetntAppImages.class);
            socialContent.setPic(pic);
        }
        if (!StringUtil.isEmpty(audioStr)) {
            SocialContentAppAudios audios = JsonBinder.buildNormalBinder().fromJson(audioStr, SocialContentAppAudios.class);
            socialContent.setAudio(audios);
        }
        socialContent.setAudioLen(rs.getLong("audio_len"));
        socialContent.setReplyNum(rs.getInt("reply_num"));
        socialContent.setAgreeNum(rs.getInt("agree_num"));
        socialContent.setReadNum(rs.getInt("read_num"));
        socialContent.setPlayNum(rs.getInt("play_num"));
        socialContent.setCreateTime(rs.getTimestamp("create_time"));
        socialContent.setUpdateTime(rs.getTimestamp("update_time"));
        socialContent.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        socialContent.setLon(rs.getFloat("lon"));
        socialContent.setLat(rs.getFloat("lat"));
        socialContent.setSocialContentPlatformDomain(SocialContentPlatformDomain.getByCode(rs.getInt("platform")));
        socialContent.setActivityId(rs.getLong("activity_id"));
        return socialContent;
    }
}
