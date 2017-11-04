package com.enjoyf.platform.db.notice;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.notice.wiki.WikiNotice;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/10
 * Description:
 */
public abstract class AbstractWikiNoticeAccessor extends AbstractBaseTableAccessor<WikiNotice> implements WikiNoticeAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWikiNoticeAccessor.class);

    private static final String KEY_TABLE_NAME = "wiki_notice";

    @Override
    public WikiNotice insert(WikiNotice wikiNotice, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, wikiNotice.getOwnProfileId());
            pstmt.setString(2, wikiNotice.getDestProfileId());
            pstmt.setString(3, wikiNotice.getDestId());
            pstmt.setString(4, wikiNotice.getOtherId());
            pstmt.setObject(5, wikiNotice.getNoticeType());
            pstmt.setString(6, wikiNotice.getMessageBody());
            pstmt.setObject(7, wikiNotice.getRemoveStatus());
            pstmt.setTimestamp(8, new Timestamp(wikiNotice.getCreateTime().getTime()));
            pstmt.setString(9, wikiNotice.getExpStr());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                wikiNotice.setNoticeId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("On insert WikiNotice, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return wikiNotice;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(own_profile_id,dest_profile_id,dest_id,wiki_id,notice_type,message_body,remove_status,create_time,expstr) VALUES (?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("WikiNotice insert sql" + sql);
        }
        return sql;
    }

    @Override
    public WikiNotice get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<WikiNotice> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<WikiNotice> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected WikiNotice rsToObject(ResultSet rs) throws SQLException {
        WikiNotice returnObject = new WikiNotice();
        returnObject.setNoticeId(rs.getLong("notice_id"));
        returnObject.setOwnProfileId(rs.getString("own_profile_id"));
        returnObject.setDestProfileId(rs.getString("dest_profile_id"));
        returnObject.setDestId(rs.getString("dest_id"));
        returnObject.setOtherId(rs.getString("other_id"));
        returnObject.setNoticeType(WikiNoticeType.getByCode(rs.getString("notice_type")));
        returnObject.setMessageBody(rs.getString("message_body"));
        returnObject.setRemoveStatus(IntValidStatus.getByCode(rs.getInt("remove_status")));
        returnObject.setCreateTime(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setExpStr(rs.getString("expstr"));
        return returnObject;
    }
}