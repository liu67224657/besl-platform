package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.PrivilegeUser;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午3:04
 * Desc:
 */
class ToolsPrivilegeUserAccessorMySql extends AbstractToolsPrivilegeUserAccessor {
    private static final Logger logger = LoggerFactory.getLogger(ToolsPrivilegeUserAccessorMySql.class);

    @Override
    public List<PrivilegeUser> queryUserByParam(PrivilegeUser param, Pagination p, Connection conn) throws DbException {
        List<PrivilegeUser> returnValue = new ArrayList<PrivilegeUser>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ").append(getUserTableName()).append(" WHERE 1=1");


        if (param != null && !Strings.isNullOrEmpty(param.getUserid())) {
            sql.append(" AND ( USERID LIKE ?");
        }
        if (param != null && !Strings.isNullOrEmpty(param.getUsername())) {
            sql.append(" OR USERNAME LIKE ? )");
        }
        if (param != null && param.getUstatus() != null) {
            sql.append(" AND USTATUS = ?");
        }
        sql.append(" ORDER BY UNO ASC LIMIT ?, ?");

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege queryUserByParam script:" + sql);
        }
        try {

            p.setTotalRows(viewQueryRowSize(param, conn));

            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());


            if (param != null && !Strings.isNullOrEmpty(param.getUserid())) {
                pstmt.setString(i++, "%" + param.getUserid() + "%");
            }
            if (param != null && !Strings.isNullOrEmpty(param.getUsername())) {
                pstmt.setString(i++, "%" + param.getUsername() + "%");
            }
            if (param != null && param.getUstatus() != null) {
                pstmt.setString(i++, param.getUstatus().getCode());
            }

            pstmt.setInt(i++, p.getStartRowIdx());
            pstmt.setInt(i++, p.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query privilege user list, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnValue;
    }



    private int viewQueryRowSize(PrivilegeUser param, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(getUserTableName()).append(" WHERE 1=1");

        if (param != null && !Strings.isNullOrEmpty(param.getUserid())) {
            sql.append(" AND USERID = ?");
        }
        if (param != null && !Strings.isNullOrEmpty(param.getUsername())) {
            sql.append(" AND USERNAME LIKE ?");
        }
        if (param != null && param.getUstatus() != null) {
            sql.append(" AND USTATUS = ?");
        }

        try {
            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());

            if (param != null && !Strings.isNullOrEmpty(param.getUserid())) {
                pstmt.setString(i++, param.getUserid());
            }
            if (param != null && !Strings.isNullOrEmpty(param.getUsername())) {
                pstmt.setString(i++, "%" + param.getUsername() + "%");
            }
            if (param != null && param.getUstatus() != null) {
                pstmt.setString(i++, param.getUstatus().getCode());
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On privilegeuser queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }
}
