package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityRelation;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
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
public abstract class AbstactActivityRelationAccessor extends AbstractSequenceBaseTableAccessor<ActivityRelation> implements ActivityRelationAccessor {

    Logger logger = LoggerFactory.getLogger(AbstactActivityRelationAccessor.class);

    private static final String KEY_TABLE_NAME = "activity_relation";
    private static final String SEQUENCE_NAME = "SEQ_ACTIVITY_RELATION_ID";

    @Override
    public ActivityRelation insert(ActivityRelation relation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            relation.setRelationId(getSeqNo(SEQUENCE_NAME, conn));
            //activity_relation_id,activity_id,activity_type,activity_directid,
            //activity_subject,activity_desc, activity_pic,activity_link,
            //activity_pic1,activity_pic2,display_order,createuserid,createtime,createip
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setLong(1, relation.getRelationId());
            pstmt.setLong(2, relation.getActivityId());
            pstmt.setInt(3, relation.getActivityType().getCode());
            pstmt.setLong(4, relation.getDirectId());
            pstmt.setString(5, relation.getSubject());
            pstmt.setString(6, relation.getDescription());
            pstmt.setString(7, relation.getPic());
            pstmt.setString(8, relation.getLink());
            pstmt.setString(9, relation.getPic1());
            pstmt.setString(10, relation.getPic2());
            pstmt.setInt(11, relation.getDisplayOrder());
            pstmt.setString(12, relation.getCreateUserid());
            pstmt.setTimestamp(13, new Timestamp(relation.getCreateTime() == null ? System.currentTimeMillis() : relation.getCreateTime().getTime()));
            pstmt.setString(14, relation.getCreateIp());
            pstmt.setString(15, relation.getRemoveStatus()==null?ActStatus.UNACT.getCode():relation.getRemoveStatus().getCode());


            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert Content, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return relation;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " " +
                "(activity_relation_id,activity_id,activity_type,activity_directid," +
                "activity_subject,activity_desc, activity_pic,activity_link," +
                "activity_pic1,activity_pic2,display_order,createuserid,createtime,createip,remove_status) " +
                " VALUES (? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        if (logger.isDebugEnabled()) {
            logger.debug("ActivityRelation INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    @Override
    protected ActivityRelation rsToObject(ResultSet rs) throws SQLException {
        ActivityRelation returnObj = new ActivityRelation();

        //activity_relation_id,activity_id,activity_type,activity_directid,
        //activity_subject,activity_desc, activity_pic,activity_link,
        //activity_pic1,activity_pic2,display_order,createuserid,createtime,createip
        returnObj.setRelationId(rs.getLong("activity_relation_id"));
        returnObj.setActivityId(rs.getLong("activity_id"));
        returnObj.setActivityType(ActivityType.getByCode(rs.getInt("activity_type")));
        returnObj.setDirectId(rs.getLong("activity_directid"));
        returnObj.setSubject(rs.getString("activity_subject"));
        returnObj.setDescription(rs.getString("activity_desc"));

        returnObj.setPic(rs.getString("activity_pic"));
        returnObj.setLink(rs.getString("activity_link"));
        returnObj.setPic1(rs.getString("activity_pic1"));
        returnObj.setPic2(rs.getString("activity_pic2"));

        returnObj.setDisplayOrder(rs.getInt("display_order"));

        returnObj.setCreateUserid(rs.getString("createuserid"));
        returnObj.setCreateTime(rs.getTimestamp("createtime"));
        returnObj.setCreateIp(rs.getString("createip"));

        returnObj.setLastModifyUserId(rs.getString("lastmodifyuserid"));
        returnObj.setLastModifyTime(rs.getTimestamp("lastmodifytime"));
        returnObj.setLastModifyIp(rs.getString("lastmodifyip"));

        returnObj.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));

        return returnObj;
    }

    @Override
    public ActivityRelation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ActivityRelation> select(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ActivityRelation> select(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }
}
