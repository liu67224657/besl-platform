package com.enjoyf.platform.db.ask;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.ask.WanbaProfileClassify;
import com.enjoyf.platform.service.ask.WanbaProfileClassifyType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by zhimingli on 2016/11/15 0015.
 */
public class AbstractWanbaProfileClassifyAccessor extends AbstractBaseTableAccessor<WanbaProfileClassify> implements WanbaProfileClassifyAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAnswerSumAccessor.class);

    private static final String KEY_TABLE_NAME = "wanba_profile_classify";

    @Override
    public WanbaProfileClassify insert(WanbaProfileClassify wanbaProfileClassify, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //   profileid,create_time,classify_type,remove_status,ext
            pstmt.setString(1, AskUtil.getWanbaProfileClassifyId(wanbaProfileClassify.getProfileid(), wanbaProfileClassify.getClassify_type()));
            pstmt.setString(2, wanbaProfileClassify.getProfileid());
            pstmt.setTimestamp(3, new Timestamp(wanbaProfileClassify.getCreate_time().getTime()));
            pstmt.setInt(4, wanbaProfileClassify.getClassify_type().getCode());
            pstmt.setInt(5, wanbaProfileClassify.getRemoveStatus().getCode());
            pstmt.setString(6, wanbaProfileClassify.getExt());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return wanbaProfileClassify;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(classify_id,profileid,create_time,classify_type,remove_status,ext) VALUES (?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("wanbaProfileClassify insert sql" + sql);
        }
        return sql;
    }

    @Override
    public WanbaProfileClassify get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<WanbaProfileClassify> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<WanbaProfileClassify> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }


    @Override
    protected WanbaProfileClassify rsToObject(ResultSet rs) throws SQLException {

        WanbaProfileClassify returnObject = new WanbaProfileClassify();
        returnObject.setClassifyid(rs.getString("classify_id"));
        returnObject.setProfileid(rs.getString("profileid"));
        returnObject.setCreate_time(new Date(rs.getTimestamp("create_time").getTime()));
        returnObject.setClassify_type(WanbaProfileClassifyType.getByCode(rs.getInt("classify_type")));
        returnObject.setRemoveStatus(IntValidStatus.getByCode(rs.getInt("remove_status")));
        returnObject.setExt(rs.getString("ext"));
        return returnObject;
    }
}
