package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ContentRelation;
import com.enjoyf.platform.service.content.ContentRelationType;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-7
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */
public class AbstractContentRelationAccessor extends AbstractBaseTableAccessor<ContentRelation> implements ContentRelationAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractContentRelationAccessor.class);

    private static final String TABLE_NAME = "CONTENT_RELATION";

    @Override
    public ContentRelation insert(ContentRelation contentRelation, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        ContentRelation returnObj = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //CONTENTID,RELATIONTYPE,RELATIONID,CREATEDATE,REMOVESTATUS,RELATIONVALUE
            pstmt.setString(1, contentRelation.getContentId());
            pstmt.setString(2, contentRelation.getRelationType().getCode());
            pstmt.setString(3, contentRelation.getRelationId());
            pstmt.setTimestamp(4, new Timestamp((contentRelation.getCreateDate() != null ? contentRelation.getCreateDate().getTime() : System.currentTimeMillis())));
            pstmt.setString(5, contentRelation.getRemoveStatus().getCode());
            pstmt.setString(6, contentRelation.getRelationValue());
            pstmt.executeUpdate();
            returnObj = contentRelation;
        } catch (SQLException e) {
            GAlerter.lab("On insert ContentRelation, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnObj;
    }

    @Override
    public ContentRelation get(QueryExpress getExpress, Connection connection) throws DbException {
        return super.get(TABLE_NAME, getExpress, connection);
    }

    @Override
    public List<ContentRelation> query(QueryExpress queryExpress, Connection connection) throws DbException {
        return super.query(TABLE_NAME, queryExpress, connection);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
    }


    @Override
    protected ContentRelation rsToObject(ResultSet rs) throws SQLException {
        ContentRelation returnObj = new ContentRelation();

        returnObj.setContentId(rs.getString("CONTENTID"));
        returnObj.setRelationType(ContentRelationType.getByCode(rs.getString("RELATIONTYPE")));
        returnObj.setRelationId(rs.getString("RELATIONID"));
        returnObj.setCreateDate(new Date(rs.getTimestamp("CREATEDATE").getTime()));
        returnObj.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));
        returnObj.setRelationValue(rs.getString("RELATIONVALUE"));

        return returnObj;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO CONTENT_RELATION(CONTENTID,RELATIONTYPE,RELATIONID,CREATEDATE,REMOVESTATUS,RELATIONVALUE) VALUES(?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ContentRelation INSERT Script:" + insertSql);
        }

        return insertSql;

    }
}
