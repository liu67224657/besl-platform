package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.gameres.gamedb.GameDBRelation;
import com.enjoyf.platform.service.gameres.gamedb.GameDBRelationType;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/26
 * Description:
 */
public abstract class AbstractGameDbAccessor extends AbstractBaseTableAccessor<GameDBRelation> implements GameDBRelationAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGameDbAccessor.class);

    private static final String TABLE_NAME = "gamedb_relation";

    @Override
    public GameDBRelation insert(GameDBRelation gameDBRelation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //gamedbid,title,type,uri,displayorder,createtime,createip,modifyuserid
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setLong(1, gameDBRelation.getGamedbId());
            pstmt.setString(2, gameDBRelation.getTitle());
            pstmt.setInt(3, gameDBRelation.getType().getCode());
            pstmt.setString(4, gameDBRelation.getUri());
            pstmt.setInt(5, gameDBRelation.getDisplayOrder());
            pstmt.setTimestamp(6, new Timestamp(gameDBRelation.getModifyTime() == null ? System.currentTimeMillis() : gameDBRelation.getModifyTime().getTime()));
            pstmt.setString(7, gameDBRelation.getModifyIp());
            pstmt.setString(8, gameDBRelation.getModifyUserid());
            pstmt.setInt(9, gameDBRelation.getValidStatus().getCode());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                gameDBRelation.setRelationId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert occured error.e: ", e);
        }
        return gameDBRelation;
    }

    @Override
    protected GameDBRelation rsToObject(ResultSet rs) throws SQLException {
        GameDBRelation relation = new GameDBRelation();

        relation.setRelationId(rs.getLong("relationid"));
        relation.setGamedbId(rs.getLong("gamedbid"));
        relation.setTitle(rs.getString("title"));
        relation.setType(GameDBRelationType.getByCode(rs.getInt("type")));
        relation.setUri(rs.getString("uri"));
        relation.setDisplayOrder(rs.getInt("displayorder"));
        relation.setModifyTime(rs.getTimestamp("modifytime"));
        relation.setModifyIp(rs.getString("modifyip"));
        relation.setModifyUserid(rs.getString("modifyuserid"));
        relation.setValidStatus(IntValidStatus.getByCode(rs.getInt("validstatus")));

        return relation;
    }

    private String getInsertSql() {
        String sqlScript = "INSERT INTO " + TABLE_NAME + "(gamedbid,title,type,uri,displayorder,modifytime,modifyip,modifyuserid,validstatus) VALUES (?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }

    @Override
    public GameDBRelation get(QueryExpress getExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, getExpress, conn);
    }

    @Override
    public List<GameDBRelation> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(TABLE_NAME, queryExpress, conn);
    }
}
