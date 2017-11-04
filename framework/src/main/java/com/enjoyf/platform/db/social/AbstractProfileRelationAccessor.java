package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.social.ObjectRelationType;
import com.enjoyf.platform.service.social.ProfileRelation;
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
public abstract class AbstractProfileRelationAccessor extends AbstractBaseTableAccessor<ProfileRelation> implements ProfileRelationAccessor {

    private static final String KEY_TABLE_NAME = "profile_relation";

    private static Logger logger = LoggerFactory.getLogger(AbstractProfileRelationAccessor.class);

    @Override
    public ProfileRelation insert(ProfileRelation relation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            //src_profileid,dest_profileid,src_status,dest_status,modifytime,modifyip
            pstmt = conn.prepareStatement(getInsertSql());
            pstmt.setString(1, relation.getRelationId());
            pstmt.setString(2, relation.getSrcProfileId());
            pstmt.setString(3, relation.getDestProfileId());
            pstmt.setInt(4, relation.getSrcStatus().getCode());
            pstmt.setInt(5, relation.getDestStatus().getCode());
            pstmt.setTimestamp(6, new Timestamp(relation.getModifyTime() == null ? System.currentTimeMillis() : relation.getModifyTime().getTime()));
            pstmt.setString(7, relation.getModifyIp());
            pstmt.setString(8, relation.getProfileKey());
            pstmt.setString(9, relation.getExtString());
            pstmt.setInt(10, relation.getType().getCode());

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
    public ProfileRelation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<ProfileRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(profile_relation_id,src_profileid,dest_profileid,src_status,dest_status,modifytime,modifyip,profilekey,extstring,relationtype) VALUES(?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("profile insert sql" + sql);
        }
        return sql;
    }

    @Override
    protected ProfileRelation rsToObject(ResultSet rs) throws SQLException {
        ProfileRelation relation = new ProfileRelation();
        //profile_relation_id,src_profileid,dest_profileid,src_status,dest_status,modifytime,modifyip
        relation.setRelationId(rs.getString("profile_relation_id"));
        relation.setSrcProfileId(rs.getString("src_profileid"));
        relation.setDestProfileId(rs.getString("dest_profileid"));
        relation.setSrcStatus(IntValidStatus.getByCode(rs.getInt("src_status")));
        relation.setDestStatus(IntValidStatus.getByCode(rs.getInt("dest_status")));
        relation.setModifyTime(rs.getTimestamp("modifytime"));
        relation.setModifyIp(rs.getString("modifyip"));
        relation.setProfileKey(rs.getString("profilekey"));
        relation.setExtString(rs.getString("extstring"));
        relation.setType(ObjectRelationType.getByCode(rs.getInt("relationtype")));
        return relation;
    }
}
