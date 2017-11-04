package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.social.SocialBlack;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-29
 * Time: 下午4:16
 * To change this template use File | Settings | File Templates.
 */
public class AbstractSocialBlackAccessor extends AbstractSequenceBaseTableAccessor<SocialBlack> implements SocialBlackAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractSocialBlackAccessor.class);

    //
    private static final String KEY_TABLE_NAME_PREFIX = "social_black_";
    private static final String KEY_SEQUENCE_NAME = "SEQ_SOCIAL_BLACK_ID";
    private static final int TABLE_NUM = 10;

    @Override
    public SocialBlack insert(SocialBlack socialBlack, Connection conn) throws DbException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            socialBlack.setBlack_id(getSeqNo(KEY_SEQUENCE_NAME, conn));

            pstmt = conn.prepareStatement(getInsertSql(socialBlack.getSrcUno()));

            //black_id,srcuno,desuno,black_type,create_time,remove_status
            pstmt.setLong(1, socialBlack.getBlack_id());
            pstmt.setString(2, socialBlack.getSrcUno());
            pstmt.setString(3, socialBlack.getDesUno());
            pstmt.setTimestamp(4, new Timestamp(socialBlack.getCreateTime() == null ? System.currentTimeMillis() : socialBlack.getCreateTime().getTime()));
            pstmt.setString(5, socialBlack.getRemoveStatus().getCode());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return socialBlack;
    }

    @Override
    public SocialBlack getSocialBlack(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return get(getTableName(ownUno), queryExpress, conn);
    }

    @Override
    public int updateSocialBlack(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(getTableName(ownUno), updateExpress, queryExpress, conn);
    }

    @Override
    public List<SocialBlack> querySocialBlackList(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(getTableName(ownUno), queryExpress, page, conn);
    }

    @Override
    public List<SocialBlack> queryAllSocialBlackList(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException {
        return query(getTableName(ownUno), queryExpress, conn);
    }

    @Override
    protected SocialBlack rsToObject(ResultSet rs) throws SQLException {
        SocialBlack socialBlack = new SocialBlack();
        // black_id srcuno,desuno,black_type,create_time, update_time remove_status
        socialBlack.setBlack_id(rs.getLong("black_id"));
        socialBlack.setSrcUno(rs.getString("srcuno"));
        socialBlack.setDesUno(rs.getString("desuno"));
        socialBlack.setCreateTime(rs.getTimestamp("create_time") != null ? new java.util.Date(rs.getTimestamp("create_time").getTime()) : null);
        socialBlack.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        socialBlack.setUpdateTime(rs.getTimestamp("update_time"));
        return socialBlack;
    }

    private String getInsertSql(String uno) throws DbException {
        String sql = "INSERT INTO " + getTableName(uno) + "(black_id,srcuno,desuno,create_time,remove_status)VALUES(?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }

    private String getTableName(String uno) throws DbException {
        return KEY_TABLE_NAME_PREFIX + TableUtil.getTableNumSuffix(uno.hashCode(), TABLE_NUM);
    }
}
