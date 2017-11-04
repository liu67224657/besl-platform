package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.tools.PrivilegeResource;
import com.enjoyf.platform.service.tools.PrivilegeResourceLevel;
import com.enjoyf.platform.service.tools.PrivilegeResourceType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-11-2
 * Time: 上午11:39
 * Desc:
 */
class AbstractToolsPrivilegeResAccessor implements ToolsPrivilegeResAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractToolsPrivilegeResAccessor.class);

    //
    private static final String KEY_TABLE_NAME_RESOURCE = "PRIVILEGE_RESOURCE";

    private static final String KEY_SEQUENCE_NAME = "SEQ_RES_ID";

    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    @Override
    public PrivilegeResource findResById(int id, Connection conn) throws DbException {

        PrivilegeResource returnValue = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM  " + getResTableName() + " WHERE RSID = ? AND RSSTATUS = ? ";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege res findResById script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            pstmt.setString(2, ActStatus.ACTED.getCode());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On  tools privilege res findResById, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<PrivilegeResource> queryAllResByStatus(ActStatus status, Connection conn) throws DbException {
        List<PrivilegeResource> returnValue = new ArrayList<PrivilegeResource>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        if (status != null) {
            sql = "SELECT * FROM  " + getResTableName() + " WHERE RSSTATUS = ? ORDER BY ORDERFIELD ASC, RSTYPE ASC";
        } else {
            sql = "SELECT * FROM  " + getResTableName() + " ORDER BY ORDERFIELD ASC,RSTYPE ASC ";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("findResById script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            if (status != null) {
                pstmt.setString(1, status.getCode());
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryAllResByStatus, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    protected String getResTableName() throws DbException {
        return KEY_TABLE_NAME_RESOURCE;
    }

    protected PrivilegeResource rsToObject(ResultSet rs) throws SQLException {
        PrivilegeResource entity = new PrivilegeResource();

        //rsid,RSNAME,RSURL,RSLEVEL,STATUS,PARENTID,ORDERFIELD,ICONURL,RSTYPE,ISMENU),DESCRIPTION
        entity.setRsid(rs.getInt("RSID"));
        entity.setRsname(rs.getString("RSNAME"));
        entity.setRsurl(rs.getString("RSURL"));
        entity.setRslevel(PrivilegeResourceLevel.getByCode(rs.getInt("RSLEVEL")));
        entity.setStatus(ActStatus.getByCode(rs.getString("RSSTATUS")));
        entity.setParentid(rs.getInt("PARENTID"));
        entity.setOrderfield(rs.getInt("ORDERFIELD"));
        entity.setIconurl(rs.getString("ICONURL"));
        entity.setRstype(PrivilegeResourceType.getByCode(rs.getInt("RSTYPE")));
        entity.setIsmenu(ActStatus.getByCode(rs.getString("ISMENU")));
        entity.setDescription(rs.getString("DESCRIPTION"));

        return entity;
    }

    @Override
    public List<PrivilegeResource> queryResByParam(PrivilegeResource param, Pagination p, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    @Override
    public boolean deleteRes(String status, int rsid, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        boolean bool = false;

        sql.append("UPDATE ").append(getResTableName()).append(" SET RSSTATUS = ? WHERE RSID = ?");

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege deleteRes delete resource script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql.toString());

            pstmt.setString(1, status);
            pstmt.setInt(2, rsid);

            pstmt.execute();

            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod deleteRes delete a resource, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean updateRes(PrivilegeResource entity, Map<ObjectField, Object> map, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        boolean bool = false;

        //修改角色
        sql = "UPDATE " + getResTableName() + " SET " + ObjectFieldUtil.generateMapSetClause(map) + " WHERE RSID = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege updateRes update res script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            int index = ObjectFieldUtil.setStmtParams(pstmt, 1, map);
            pstmt.setInt(index, entity.getRsid());

            pstmt.execute();

            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod updateRes update a res, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public boolean insertRes(PrivilegeResource entity, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;
        boolean bool = false;

        sql = "INSERT INTO " + getResTableName() + " (RSNAME, RSURL, RSLEVEL, RSSTATUS, PARENTID, ORDERFIELD, ICONURL, RSTYPE, ISMENU, DESCRIPTION) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("Privilege insertRes script:" + sql);
        }

        try {
            int i = 1;
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            if (entity != null) {
               // pstmt.setInt(i++, (int) getSeqNo(conn));
                pstmt.setString(i++, entity.getRsname());
                pstmt.setString(i++, entity.getRsurl());
                pstmt.setInt(i++, entity.getRslevel() != null ? entity.getRslevel().getCode() : null);
                pstmt.setString(i++, entity.getStatus() != null ? entity.getStatus().getCode() : null);
                pstmt.setInt(i++, entity.getParentid());
                pstmt.setInt(i++, entity.getOrderfield());
                pstmt.setString(i++, entity.getIconurl());
                pstmt.setInt(i++, entity.getRstype() != null ? entity.getRstype().getCode() : null);
                pstmt.setString(i++, entity.getIsmenu() != null ? entity.getIsmenu().getCode() : null);
                pstmt.setString(i++, entity.getDescription());
            }

            pstmt.execute();

            bool = true;
        } catch (SQLException e) {
            GAlerter.lab("On mothod insertRes add a res, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return bool;
    }

    @Override
    public PrivilegeResource getResByRsurl(String rsurl, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrivilegeResource privilegeRes = null;

        String sql = "SELECT * FROM " + getResTableName() + " WHERE RSURL = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege roles getResByRsurl  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, rsurl);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                privilegeRes = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getResByRsurl , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return privilegeRes;
    }

    @Override
    public List<PrivilegeResource> queryResMenu(String rstype, String status, Connection conn) throws DbException {
        List<PrivilegeResource> returnValue = new ArrayList<PrivilegeResource>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + getResTableName() + " WHERE RSSTATUS = ?" + " ORDER BY ORDERFIELD ASC";

        if (logger.isDebugEnabled()) {
            logger.debug("tools privilege roles getRoleByRoleName  Script:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, status);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryResMenu , a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    public PrivilegeResource getResourceByRsid(int rsid, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PrivilegeResource privilegeResource = null;

        String sql = "SELECT * FROM  " + getResTableName() + " WHERE RSID = ? ";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, rsid);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                privilegeResource = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("An Exception occurred", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return privilegeResource;

    }
}
