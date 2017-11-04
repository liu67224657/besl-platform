package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.social.RecommendBlackSet;
import com.enjoyf.platform.service.social.RecommendDetailSet;
import com.enjoyf.platform.service.social.RecommendType;
import com.enjoyf.platform.service.social.SocialRecommend;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">${User}</a>
 * Create time: 12-8-29
 * Description:
 */
public class AbstractSoicalRecommendAccessor extends AbstractBaseTableAccessor<SocialRecommend> implements SocialRecommendAccessor {

    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractSoicalRecommendAccessor.class);

    protected static final String KEY_TABLE_NAME = "SOCIAL_RECOMMEND";

    @Override
    public SocialRecommend insert(SocialRecommend recommend, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //,UNO,RECOMMENDTYPE,DETAIL,CALDATE,CALSTATUS,
            pstmt.setString(1, recommend.getSrcUno());
            pstmt.setString(2, recommend.getRecommendType().getCode());
            pstmt.setString(3, recommend.getRecommendDetailSet() == null ? null : recommend.getRecommendDetailSet().toJsonStr());
            pstmt.setTimestamp(4, new Timestamp(recommend.getCalDate() == null ? System.currentTimeMillis() : recommend.getCalDate().getTime()));
            pstmt.setString(5, recommend.getCalStatus().getCode());
            pstmt.setString(6,recommend.getBlackSet()==null?null:recommend.getBlackSet().toJsonStr());

            pstmt.executeUpdate();

            return recommend;
        } catch (SQLException e) {
            GAlerter.lab("On insert SocialInviteInfo, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public SocialRecommend get(QueryExpress queryExpress, Connection conn) throws DbException {
        return get(KEY_TABLE_NAME, queryExpress, conn);
    }


    @Override
    protected SocialRecommend rsToObject(ResultSet rs) throws SQLException {
        SocialRecommend socialRecommend = new SocialRecommend();
        socialRecommend.setSrcUno(rs.getString("SRCUNO"));
        socialRecommend.setRecommendType(RecommendType.getByCode(rs.getString("RECOMMENDTYPE")));
        socialRecommend.setCalDate(new Date(rs.getTimestamp("CALDATE").getTime()));
        socialRecommend.setCalStatus(ActStatus.getByCode(rs.getString("CALSTATUS")));
        socialRecommend.setRecommendDetailSet(RecommendDetailSet.parse(rs.getString("DETAIL")));
        socialRecommend.setBlackSet(RecommendBlackSet.parse(rs.getString("BLACKDETAIL")));

        return socialRecommend;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(SRCUNO,RECOMMENDTYPE,DETAIL,CALDATE,CALSTATUS,BLACKDETAIL) VALUES (?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert sql:" + sql);
        }

        return sql;
    }

}
