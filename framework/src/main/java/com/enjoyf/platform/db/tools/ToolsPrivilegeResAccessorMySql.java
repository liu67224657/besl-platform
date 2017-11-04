package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.PrivilegeResource;
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
class ToolsPrivilegeResAccessorMySql extends AbstractToolsPrivilegeResAccessor {
    private static final Logger logger = LoggerFactory.getLogger(ToolsPrivilegeResAccessorMySql.class);

    @Override
    public List<PrivilegeResource> queryResByParam(PrivilegeResource param, Pagination p, Connection conn) throws DbException {
        List<PrivilegeResource> returnValue = new ArrayList<PrivilegeResource>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM " + getResTableName() + " WHERE 1=1");

        if (param != null && !Strings.isNullOrEmpty(param.getRsname())) {
            sql.append(" AND RSNAME LIKE ?");
        }
        if (param != null && param.getRstype() != null) {
            sql.append(" AND RSTYPE = ?");
        }
        if (param != null && param.getStatus() != null) {
            sql.append(" AND RSSTATUS = ?");
        }
        sql.append(" ORDER BY RSID ASC LIMIT ?, ?");

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege queryRsByParam script:" + sql);
        }
        try {

            p.setTotalRows(viewQueryRowSize(param, conn));

            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());


            if (param != null && !Strings.isNullOrEmpty(param.getRsname())) {
                pstmt.setString(i++, "%" + param.getRsname() + "%");
            }
            if (param != null && param.getRstype() != null) {
                pstmt.setInt(i++, param.getRstype().getCode());
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
            GAlerter.lab("On query privilege rs list, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnValue;
    }

    private int viewQueryRowSize(PrivilegeResource param, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(1) FROM " + getResTableName() + " WHERE 1=1");

        if (param != null && !Strings.isNullOrEmpty(param.getRsname())) {
            sql.append(" AND RSNAME LIKE ?");
        }
        if (param != null && param.getRstype() != null) {
            sql.append(" AND RSTYPE = ?");
        }
        if (param != null && param.getStatus() != null) {
            sql.append(" AND RSSTATUS = ?");
        }

        try {
            int i = 1;
            pstmt = conn.prepareStatement(sql.toString());

            if (param != null && !Strings.isNullOrEmpty(param.getRsname())) {
                pstmt.setString(i++, "%" + param.getRsname() + "%");
            }
            if (param != null && param.getRstype() != null) {
                pstmt.setInt(i++, param.getRstype().getCode());
            }
            if (param != null && param.getStatus() != null) {
                pstmt.setString(i++, param.getStatus().getCode());
            }
            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On privilegeuser viewQueryRowSize rs, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

}
