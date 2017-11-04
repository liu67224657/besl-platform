package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.RelationStatus;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.UserRelation;
import com.enjoyf.platform.service.social.UserRelationField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public abstract class AbstractUserRelationAccessor extends AbstractBaseTableAccessor<UserRelation> implements UserRelationAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserRelationAccessor.class);

    private static final String KEY_TABLE_NAME = "user_relation";

    @Override
    public UserRelation insert(UserRelation userRelation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, userRelation.getRelationId());
            pstmt.setString(2, userRelation.getSrcProfileid());
            pstmt.setString(3, userRelation.getDestProfileid());
            pstmt.setInt(4, userRelation.getSrcStatus().getCode());
            pstmt.setInt(5, userRelation.getDestStatus().getCode());
            pstmt.setString(6, userRelation.getProfilekey());
            pstmt.setInt(7, userRelation.getRelationType().getCode());
            pstmt.setString(8, userRelation.getExtstring());
            pstmt.setString(9, userRelation.getModifyIp());
            pstmt.setTimestamp(10, new Timestamp(userRelation.getModifyTime().getTime()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return userRelation;
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(relation_id,src_profileid,dest_profileid,src_status,dest_status,profilekey,relation_type,extstring,modify_ip,modify_time) VALUES (?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("UserRelation insert sql" + sql);
        }
        return sql;
    }

    @Override
    public UserRelation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<UserRelation> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<UserRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public UserRelation get(String srcUno, String destUno, ObjectRelationType type, Connection conn) throws DbException {
        UserRelation returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + KEY_TABLE_NAME + " WHERE src_profileid = ? AND dest_profileid = ? AND relation_type = ?");

            pstmt.setString(1, srcUno);
            pstmt.setString(2, destUno);
            pstmt.setInt(3, type.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On select a relation, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    //todo 这俩个方法???
    @Override
    public List<UserRelation> queryByDestProfileId(String srcUno, ObjectRelationType type, Pagination page, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(UserRelationField.SRC_PROFILEID, srcUno))
                .add(QueryCriterions.eq(UserRelationField.RELATION_TYPE, type.getCode()));
        return  this.query(queryExpress,page,conn);
    }

    //todo 这俩个方法???
    @Override
    public List<UserRelation> queryBySrcProfileId(String srcUno, ObjectRelationType type, Pagination page, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(UserRelationField.SRC_PROFILEID, srcUno))
                .add(QueryCriterions.eq(UserRelationField.RELATION_TYPE, type.getCode()));
        return  this.query(queryExpress,page,conn);
    }

    @Override
    protected UserRelation rsToObject(ResultSet rs) throws SQLException {

        UserRelation returnObject = new UserRelation();

        returnObject.setRelationId(rs.getString("relation_id"));
        returnObject.setSrcProfileid(rs.getString("src_profileid"));
        returnObject.setDestProfileid(rs.getString("dest_profileid"));
        returnObject.setSrcStatus(RelationStatus.getByCode(rs.getInt("src_status")));
        returnObject.setDestStatus(RelationStatus.getByCode(rs.getInt("dest_status")));
        returnObject.setProfilekey(rs.getString("profilekey"));
        returnObject.setRelationType(ObjectRelationType.getByCode(rs.getInt("relation_type")));
        returnObject.setExtstring(rs.getString("extstring"));
        returnObject.setModifyIp(rs.getString("modify_ip"));
        returnObject.setModifyTime(rs.getTimestamp("modify_time"));


        return returnObject;
    }
}