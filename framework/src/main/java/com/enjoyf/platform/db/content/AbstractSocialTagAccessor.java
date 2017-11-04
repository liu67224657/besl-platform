package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.social.SocialTag;
import com.enjoyf.platform.service.content.social.SocialTagCategory;
import com.enjoyf.platform.service.content.social.SocialTagType;
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
 * User: zhitaoshi
 * Date: 14-5-9
 * Time: 下午3:39
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialTagAccessor extends AbstractBaseTableAccessor<SocialTag> implements SocialTagAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialTagAccessor.class);

    private static final String KEY_TABLE_NAME = "social_tag";

    @Override
    public SocialTag insert(SocialTag socialTag, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            // tagName,tagDescription,tagType,tagCategory,displayOrder,isHot,removeStatus,createDate,createIp,createUserId,lastModifyDate,lastModifyIp,lastModifyUserId,shareId;

            pstmt.setString(1, socialTag.getTitle());
            pstmt.setString(2, socialTag.getDescription());
            pstmt.setString(3, socialTag.getIcon());
            pstmt.setInt(4, socialTag.getTagType().getCode());
            pstmt.setInt(5, socialTag.getTagCategory().getCode());
            pstmt.setInt(6, socialTag.getDisplayOrder());
            pstmt.setBoolean(7, socialTag.isHot());
            pstmt.setInt(8, socialTag.getUseSum() > 0 ? socialTag.getUseSum() : 1);
            pstmt.setString(9, socialTag.getRemoveStatus() == null ? ValidStatus.VALID.getCode() : socialTag.getRemoveStatus().getCode());
            pstmt.setTimestamp(10, new Timestamp(socialTag.getCreateDate() == null ? System.currentTimeMillis() : socialTag.getCreateDate().getTime()));
            if(StringUtil.isEmpty(socialTag.getCreateIp())){
                pstmt.setNull(11, Types.VARCHAR);
            } else {
                pstmt.setString(11, socialTag.getCreateIp());
            }
            if(StringUtil.isEmpty(socialTag.getCreateUserId())){
                pstmt.setNull(12, Types.VARCHAR);
            } else {
                pstmt.setString(12, socialTag.getCreateUserId());
            }

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                socialTag.setTagId(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab("On insert SocialTag, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialTag;
    }

    @Override
    public SocialTag get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialTag> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialTag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected SocialTag rsToObject(ResultSet rs) throws SQLException {
        SocialTag socialTag = new SocialTag();
        socialTag.setTagId(rs.getLong("tag_id"));
        socialTag.setTitle(rs.getString("title"));
        socialTag.setDescription(rs.getString("description"));
        socialTag.setIcon(rs.getString("icon"));
        socialTag.setTagType(SocialTagType.getByCode(rs.getInt("tag_type")));
        socialTag.setTagCategory(SocialTagCategory.getByCode(rs.getInt("tag_category")));
        socialTag.setDisplayOrder(rs.getInt("display_order"));
        socialTag.setHot(rs.getBoolean("is_hot"));
        socialTag.setUseSum(rs.getInt("use_sum"));
        socialTag.setReplySum(rs.getInt("reply_sum"));
        socialTag.setAgreeSum(rs.getInt("agree_sum"));
        socialTag.setGiftSum(rs.getInt("gift_sum"));
        socialTag.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        socialTag.setCreateDate(rs.getTimestamp("create_date"));
        socialTag.setCreateIp(rs.getString("create_ip"));
        socialTag.setCreateUserId(rs.getString("create_userid"));
        socialTag.setLastModifyDate(rs.getTimestamp("modify_date"));
        socialTag.setLastModifyIp(rs.getString("modify_ip"));
        socialTag.setLastModifyUserId(rs.getString("modify_userid"));
        return socialTag;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(title,description,icon,tag_type,tag_category,display_order,is_hot,use_sum,remove_status,create_date,create_ip,create_userid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        if(logger.isDebugEnabled()) {
            logger.debug("SocialTag insert sql:" + sql);
        }
        return sql;
    }

}
