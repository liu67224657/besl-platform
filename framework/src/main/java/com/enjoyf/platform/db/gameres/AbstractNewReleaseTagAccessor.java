package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.NewReleaseTag;
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
 * Time: 下午12:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractNewReleaseTagAccessor extends AbstractBaseTableAccessor<NewReleaseTag> implements NewReleaseTagAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractNewReleaseTagAccessor.class);

    private static final String KEY_TABLE_NAME = "new_game_tag";

    @Override
    public NewReleaseTag insert(NewReleaseTag newGameTag, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, newGameTag.getTagName());
            pstmt.setBoolean(2, newGameTag.getIsHot());
            pstmt.setBoolean(3, newGameTag.getIsTop());
            pstmt.setTimestamp(4, new Timestamp(newGameTag.getCreateDate()==null?System.currentTimeMillis():newGameTag.getCreateDate().getTime()));
            pstmt.setString(5, newGameTag.getCreateIp());
            pstmt.setString(6, newGameTag.getCreateUserId());
            pstmt.setString(7, newGameTag.getValidStatus()==null? ValidStatus.VALID.getCode():newGameTag.getValidStatus().getCode());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                newGameTag.setNewReleaseTagId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On Insert NewGameTag,SQLException:"+e);
            throw  new DbException(e);
        }
        return newGameTag;
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public NewReleaseTag get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<NewReleaseTag> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<NewReleaseTag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected NewReleaseTag rsToObject(ResultSet rs) throws SQLException {
        NewReleaseTag newGameTag = new NewReleaseTag();
        newGameTag.setNewReleaseTagId(rs.getLong("new_game_tag_id"));
        newGameTag.setTagName(rs.getString("tag_name"));
        newGameTag.setIsHot(rs.getBoolean("is_hot"));
        newGameTag.setIsTop(rs.getBoolean("is_top"));
        newGameTag.setCreateDate(rs.getTimestamp("create_date"));
        newGameTag.setCreateIp(rs.getString("create_ip"));
        newGameTag.setCreateUserId(rs.getString("create_userid"));
        newGameTag.setLastModifyDate(rs.getTimestamp("last_modify_date"));
        newGameTag.setLastModifyIp(rs.getString("last_modify_ip"));
        newGameTag.setLastModifyUserId(rs.getString("last_modify_userid"));
        newGameTag.setValidStatus(ValidStatus.getByCode(rs.getString("validstatus")));
        return newGameTag;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO "+KEY_TABLE_NAME+"(tag_name,is_hot,is_top,create_date,create_ip,create_userid,validstatus) VALUES(?,?,?,?,?,?,?)";
        if(logger.isDebugEnabled()){
            logger.debug("Insert NewGameTag sql:"+insertSql);
        }
        return insertSql;
    }
}
