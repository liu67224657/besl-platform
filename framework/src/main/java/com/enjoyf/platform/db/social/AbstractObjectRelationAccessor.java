package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.social.ObjectRelation;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/10
 * Description:
 */
public abstract class AbstractObjectRelationAccessor extends AbstractBaseTableAccessor<ObjectRelation> implements ObjectRelationAccessor {

    private static final String KEY_TABLE_NAME = "object_relation";

    private static Logger logger = LoggerFactory.getLogger(AbstractObjectRelationAccessor.class);

    @Override
    public ObjectRelation insert(ObjectRelation relation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            //object_relation_id,profileid,objectid,objecttype,modifytime,modifyip,removestatus,extstring,appkey
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, relation.getRelationId());
            pstmt.setString(2, relation.getProfileId());
            pstmt.setString(3, relation.getObjectId());
            pstmt.setInt(4, relation.getType().getCode());
            pstmt.setTimestamp(5, new Timestamp(relation.getModifyTime() == null ? System.currentTimeMillis() : relation.getModifyTime().getTime()));
            pstmt.setString(6, relation.getModifyIp());
            pstmt.setInt(7, relation.getStatus().getCode());
            pstmt.setString(8, relation.getExtString());
            pstmt.setString(9, relation.getProfileKey());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("On insert profile, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return relation;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public ObjectRelation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ObjectRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public List<ObjectRelation> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(object_relation_id,profileid,objectid,objecttype,modifytime,modifyip,status,extstring,profilekey) VALUES(?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("objectrelation insert sql" + sql);
        }

        return sql;
    }

    @Override
    protected ObjectRelation rsToObject(ResultSet rs) throws SQLException {
        ObjectRelation relation = new ObjectRelation();
        //object_relation_id,profileid,objectid,objecttype,modifytime,modifyip,removestatus,extstring,appkey
        relation.setRelationId(rs.getString("object_relation_id"));
        relation.setProfileId(rs.getString("profileid"));
        relation.setObjectId(rs.getString("objectid"));
        relation.setType(ObjectRelationType.getByCode(rs.getInt("objecttype")));
        relation.setModifyTime(rs.getTimestamp("modifytime"));
        relation.setModifyIp(rs.getString("modifyip"));
        relation.setStatus(IntValidStatus.getByCode(rs.getInt("status")));
        relation.setExtString(rs.getString("extstring"));
        relation.setProfileKey(rs.getString("profilekey"));
        return relation;
    }
}
