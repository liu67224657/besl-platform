package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameRelation;
import com.enjoyf.platform.service.gameres.GameRelationType;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
abstract class AbstractGameRelationAccessor extends AbstractSequenceBaseTableAccessor<GameRelation> implements GameRelationAccessor {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TABLE_NAME = "GAME_RESOURCE_RELATION";
    private static final String SEQ_GAME_RELATION_ID = "SEQ_GAME_RELATION_ID";

    @Override
    public GameRelation insert(GameRelation gameRelation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            gameRelation.setRelationId(getSeqNo(SEQ_GAME_RELATION_ID, conn));

            //RELATIONID RESOURCEID,RELATIONTYPE,SORTNUM,RELATIONVALUE,VALIDSTATUS
            pstmt.setLong(1, gameRelation.getRelationId());
            pstmt.setLong(2, gameRelation.getResourceId());
            pstmt.setString(3, gameRelation.getGameRelationType().getCode());
            pstmt.setInt(4, gameRelation.getSortNum());
            pstmt.setString(5, gameRelation.getRelationValue());
            pstmt.setString(6, gameRelation.getValidStatus().getCode());
            pstmt.setString(7, gameRelation.getRelationName());

            pstmt.execute();
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return gameRelation;
    }

    @Override
    public GameRelation get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<GameRelation> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    private String getInsertSql() {
        String sqlScript = "INSERT INTO " + TABLE_NAME + "(RELATIONID,RESOURCEID,RELATIONTYPE,SORTNUM,RELATIONVALUE,VALIDSTATUS,RELATIONNAME) VALUES (? ,? ,? ,? ,? ,? ,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }

    @Override
    protected GameRelation rsToObject(ResultSet rs) throws SQLException {
        GameRelation returnObj = new GameRelation();
        returnObj.setRelationId(rs.getLong("RELATIONID"));
        returnObj.setResourceId(rs.getLong("RESOURCEID"));
        returnObj.setSortNum(rs.getInt("SORTNUM"));
        returnObj.setGameRelationType(GameRelationType.getByCode(rs.getString("RELATIONTYPE")));
        returnObj.setRelationValue(rs.getString("RELATIONVALUE"));
        returnObj.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));
        returnObj.setRelationName(rs.getString("RELATIONNAME"));
        return returnObj;
    }

    @Override
    public long getSeqNo(String sequenceName, Connection conn) throws DbException {
        return super.getSeqNo(sequenceName, conn);
    }
}
