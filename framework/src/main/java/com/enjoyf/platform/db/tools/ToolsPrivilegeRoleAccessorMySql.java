package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.PrivilegeRole;
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
class ToolsPrivilegeRoleAccessorMySql extends AbstractToolsPrivilegeRoleAccessor {
    private static final Logger logger = LoggerFactory.getLogger(ToolsPrivilegeRoleAccessorMySql.class);

    @Override
    public List<PrivilegeRole> queryRoleByParam(PrivilegeRole param, Pagination p, Connection conn) throws DbException {
        List<PrivilegeRole> returnValue = new ArrayList<PrivilegeRole>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ").append(getRolesTableName()).append(" WHERE 1=1");


        if (param != null && !Strings.isNullOrEmpty(param.getRoleName())) {
            sql.append(" AND ROLENAME LIKE ?");
        }
        if (param != null && param.getStatus() != null) {
            sql.append(" AND RSTATUS = ?");
        }
        sql.append(" ORDER BY RID ASC LIMIT ?, ?");

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege queryRoleByParam script:" + sql);
        }
        try {

            p.setTotalRows(viewQueryRowSize(param, conn));

            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());


            if (param != null && !Strings.isNullOrEmpty(param.getRoleName())) {
                pstmt.setString(i++, "%" + param.getRoleName() + "%");
            }
            if (param != null && param.getStatus() != null) {
                pstmt.setString(i++, param.getStatus().getCode());
            }

            pstmt.setInt(i++, p.getStartRowIdx());
            pstmt.setInt(i++, p.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query privilege roles list, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnValue;
    }


    private int viewQueryRowSize(PrivilegeRole param, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(1) FROM ").append(getRolesTableName()).append(" WHERE 1=1");


        if (param != null && !Strings.isNullOrEmpty(param.getRoleName())) {
            sql.append(" AND ROLENAME LIKE ?");
        }
        if (param != null && param.getStatus() != null) {
            sql.append(" AND RSTATUS = ?");
        }

        try {
            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());

            if (param != null && !Strings.isNullOrEmpty(param.getRoleName())) {
                pstmt.setString(i++, "%" + param.getRoleName() + "%");
            }
            if (param != null && param.getStatus() != null) {
                pstmt.setString(i++, param.getStatus().getCode());
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
