package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.NewReleaseTagRelation;
import com.enjoyf.platform.util.Pagination;
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
 * Date: 13-8-21
 * Time: 下午12:43
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractNewReleaseTagRelationAccessor extends AbstractBaseTableAccessor<NewReleaseTagRelation> implements NewReleaseTagRelationAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractNewReleaseTagRelationAccessor.class);

    private static final String KEY_TABLE_NAME = "new_tag_relation";

    @Override
    public NewReleaseTagRelation insert(NewReleaseTagRelation newTagRelation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, newTagRelation.getNewGameInfoId());
            pstmt.setLong(2, newTagRelation.getNewGameTagId());
            pstmt.setString(3, newTagRelation.getStatus().getCode());

            pstmt.executeUpdate();
            rs =  pstmt.getGeneratedKeys();
            if(rs.next()){
                newTagRelation.setNewTagRelationId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert newTagRelation,SQLException:" + e);
            throw new DbException(e);
        }
        return newTagRelation;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public NewReleaseTagRelation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<NewReleaseTagRelation> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<NewReleaseTagRelation> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected NewReleaseTagRelation rsToObject(ResultSet rs) throws SQLException {
        NewReleaseTagRelation newTagRelation = new NewReleaseTagRelation();
        newTagRelation.setNewTagRelationId(rs.getLong("new_tag_relation_id"));
        newTagRelation.setNewGameInfoId(rs.getLong("new_game_info_id"));
        newTagRelation.setNewGameTagId(rs.getLong("new_game_tag_id"));
        newTagRelation.setStatus(ActStatus.getByCode(rs.getString("status")));
        return newTagRelation;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(new_game_info_id,new_game_tag_id,status) VALUES(?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("Insert newTagRelation sql:" + insertSql);
        }
        return insertSql;
    }
}
