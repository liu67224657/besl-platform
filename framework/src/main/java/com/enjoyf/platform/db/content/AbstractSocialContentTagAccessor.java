package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.social.SocialContentTag;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-12
 * Time: 上午11:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSocialContentTagAccessor extends AbstractBaseTableAccessor<SocialContentTag> implements SocialContentTagAccessor{

    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialContentTagAccessor.class);
    private static final String KEY_TABLE_NAME = "social_content_tag";

    @Override
    public SocialContentTag insert(SocialContentTag socialContentTag, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, socialContentTag.getContentId());
            pstmt.setString(2, socialContentTag.getContentUno());
            pstmt.setLong(3, socialContentTag.getTagId());
            pstmt.setString(4, socialContentTag.getRemoveStatus() == null ? ValidStatus.VALID.getCode() : socialContentTag.getRemoveStatus().getCode());
            pstmt.setTimestamp(5, new Timestamp(socialContentTag.getCreateDate() == null ? System.currentTimeMillis() : socialContentTag.getCreateDate().getTime()));
            pstmt.setString(6, socialContentTag.getCreateIp());
            pstmt.setString(7, socialContentTag.getCreateUserId());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                socialContentTag.setContentTagId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialContentTag;
    }

    @Override
    public SocialContentTag get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialContentTag> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<SocialContentTag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    protected SocialContentTag rsToObject(ResultSet rs) throws SQLException {
        SocialContentTag socialContentTag = new SocialContentTag();
        socialContentTag.setContentTagId(rs.getLong("content_tag_id"));
        socialContentTag.setContentId(rs.getLong("content_id"));
        socialContentTag.setContentUno(rs.getString("content_uno"));
        socialContentTag.setTagId(rs.getLong("tag_id"));
        socialContentTag.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        socialContentTag.setCreateDate(rs.getTimestamp("create_date"));
        socialContentTag.setCreateIp(rs.getString("create_ip"));
        socialContentTag.setCreateUserId(rs.getString("create_userid"));
        socialContentTag.setLastModifyDate(rs.getTimestamp("modify_date"));
        socialContentTag.setLastModifyIp(rs.getString("modify_ip"));
        socialContentTag.setLastModifyUserId(rs.getString("modify_userid"));
        return socialContentTag;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(content_id,content_uno,tag_id,remove_status,create_date,create_ip,create_userid) VALUES(?,?,?,?,?,?,?)";
        if(logger.isDebugEnabled()){
            logger.debug("SocialContentTag insert sql:" + sql);
        }
        return sql;
    }
}
