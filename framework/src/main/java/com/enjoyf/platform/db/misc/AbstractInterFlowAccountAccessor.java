package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.misc.InterFlowAccount;
import com.enjoyf.platform.service.misc.InterFlowType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-28
 * Time: 上午11:38
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractInterFlowAccountAccessor extends AbstractBaseTableAccessor<InterFlowAccount> implements InterFlowAccountAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractInterFlowAccountAccessor.class);
    private static final String TABLE_NAME = "interflow_account";

    @Override
    public InterFlowAccount insert(InterFlowAccount interFlowAccount, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            interFlowAccount.setInterflowId(MD5Util.Md5(interFlowAccount.getAccount() + "-" + interFlowAccount.getType().getCode()));

            pstmt = conn.prepareStatement(getInsertSql());

            //interflow_id,interflowname,interflowaccount,lorduser,interflowtype,dutyuser,usernumber,level,manufacturer,
            //gamename,gamecategory,gametype,platform,theme,publisharea,createdate,createuserid,modifydate,modifyuserid,removestatus
            pstmt.setString(1, interFlowAccount.getInterflowId());
            pstmt.setString(2, interFlowAccount.getName() == null ? "" : interFlowAccount.getName());
            pstmt.setString(3, interFlowAccount.getAccount() == null ? "" : interFlowAccount.getAccount());
            pstmt.setString(4, interFlowAccount.getLord() == null ? "" : interFlowAccount.getLord());
            pstmt.setInt(5, interFlowAccount.getType() == null ? 0 : interFlowAccount.getType().getCode());
            pstmt.setString(6, interFlowAccount.getDuty() == null ? "" : interFlowAccount.getDuty());
            pstmt.setString(7, interFlowAccount.getUserNumber() == null ? "" : interFlowAccount.getUserNumber());
            pstmt.setString(8, interFlowAccount.getLevel() == null ? "" : interFlowAccount.getLevel());
            pstmt.setString(9, interFlowAccount.getManufacturer() == null ? "" : interFlowAccount.getManufacturer());
            pstmt.setString(10, interFlowAccount.getGameName() == null ? "" : interFlowAccount.getGameName());
            pstmt.setString(11, interFlowAccount.getGameCategory() == null ? "" : interFlowAccount.getGameCategory());
            pstmt.setString(12, interFlowAccount.getGameType() == null ? "" : interFlowAccount.getGameType());
            pstmt.setString(13, interFlowAccount.getPlatform() == null ? "" : interFlowAccount.getPlatform());
            pstmt.setString(14, interFlowAccount.getTheme() == null ? "" : interFlowAccount.getTheme());
            pstmt.setString(15, interFlowAccount.getPublishArea() == null ? "" : interFlowAccount.getPublishArea());
            pstmt.setTimestamp(16, new Timestamp(interFlowAccount.getCreateDate() == null ? System.currentTimeMillis() : interFlowAccount.getCreateDate().getTime()));
            pstmt.setString(17, interFlowAccount.getCreateUser() == null ? "" : interFlowAccount.getCreateUser());
            pstmt.setString(18, interFlowAccount.getRemoveStatus() == null ? ValidStatus.VALID.getCode() : interFlowAccount.getRemoveStatus().getCode());
            pstmt.setString(19, interFlowAccount.getLastPostDate() == null ? "" : interFlowAccount.getLastPostDate());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert InterFlowAccount, occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return null;
    }

    private String getInsertSql() {
        String insertSql = "INSERT INTO " + TABLE_NAME + " (interflow_id,interflowname,interflowaccount,lorduser," +
                "interflowtype,dutyuser,usernumber,level,manufacturer,gamename,gamecategory,gametype,platform," +
                "theme,publisharea,createdate,createuserid,removestatus,lastpost) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("Feedback INSERT Script:" + insertSql);
        }
        return insertSql;
    }

    @Override
    public List<InterFlowAccount> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<InterFlowAccount> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public InterFlowAccount get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected InterFlowAccount rsToObject(ResultSet rs) throws SQLException {
        //interflow_id,interflowname,interflowaccount,lorduser,interflowtype,dutyuser,usernumber,level,manufacturer,
        //gamename,gamecategory,gametype,platform,theme,publisharea,createdate,createuserid,modifydate,modifyuserid,removestatus
        InterFlowAccount interFlowAccount = new InterFlowAccount();
        interFlowAccount.setInterflowId(rs.getString("interflow_id"));
        interFlowAccount.setName(rs.getString("interflowname"));
        interFlowAccount.setAccount(rs.getString("interflowaccount"));
        interFlowAccount.setLord(rs.getString("lorduser"));
        interFlowAccount.setType(InterFlowType.getByCode(rs.getInt("interflowtype")));
        interFlowAccount.setDuty(rs.getString("dutyuser"));
        interFlowAccount.setUserNumber(rs.getString("usernumber"));
        interFlowAccount.setLevel(rs.getString("level"));
        interFlowAccount.setManufacturer(rs.getString("manufacturer"));
        interFlowAccount.setGameName(rs.getString("gamename"));
        interFlowAccount.setGameCategory(rs.getString("gamecategory"));
        interFlowAccount.setGameType(rs.getString("gametype"));
        interFlowAccount.setPlatform(rs.getString("platform"));
        interFlowAccount.setTheme(rs.getString("theme"));
        interFlowAccount.setPublishArea(rs.getString("publisharea"));
        interFlowAccount.setLastPostDate(rs.getString("lastpost"));
        interFlowAccount.setCreateDate(rs.getTimestamp("createdate"));
        interFlowAccount.setCreateUser(rs.getString("createuserid"));
        interFlowAccount.setModifyDate(rs.getTimestamp("modifydate"));
        interFlowAccount.setModifyUser(rs.getString("modifyuserid"));
        interFlowAccount.setRemoveStatus(ValidStatus.getByCode(rs.getString("removestatus")));

        return interFlowAccount;
    }
}
