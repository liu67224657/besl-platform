package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.AdvertiseAppUrl;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class AbstractAdvertiseAppUrlAccessor extends AbstractBaseTableAccessor<AdvertiseAppUrl> implements AdvertiseAppUrlAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractAdvertiseAppUrlAccessor.class);
    protected static final String KEY_TABLE_NAME = "ADVERTISE_APPURL";

    @Override
    public AdvertiseAppUrl insert(AdvertiseAppUrl advertiseAppUrl, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql(), java.sql.Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, advertiseAppUrl.getCode());
            pstmt.setString(2, advertiseAppUrl.getIosUrl());
            pstmt.setString(3, advertiseAppUrl.getAndroidUrl());
            pstmt.setString(4, advertiseAppUrl.getDefaultUrl());
            pstmt.setString(5, advertiseAppUrl.getRemoveStatus().getCode());
            pstmt.setString(6, advertiseAppUrl.getCreateIp());
            pstmt.setString(7, advertiseAppUrl.getCreateId());
            pstmt.setTimestamp(8, new Timestamp(advertiseAppUrl.getCreateTime() == null ? System.currentTimeMillis() : advertiseAppUrl.getCreateTime().getTime()));
            pstmt.setString(9, advertiseAppUrl.getLastModifyIp());
            pstmt.setString(10, advertiseAppUrl.getLastModifyId());
            if (advertiseAppUrl.getLastModifyTime() != null) {
                pstmt.setTimestamp(11, new Timestamp(advertiseAppUrl.getLastModifyTime().getTime()));
            } else {
                pstmt.setNull(11, Types.TIMESTAMP);
            }
            pstmt.setString(12, advertiseAppUrl.getCodeName());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                advertiseAppUrl.setClientUrlId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On insert advertiseAppUrl, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeConnection(conn);
        }

        return advertiseAppUrl;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected AdvertiseAppUrl rsToObject(ResultSet rs) throws SQLException {
        AdvertiseAppUrl advertiseAppUrl = new AdvertiseAppUrl();
        advertiseAppUrl.setClientUrlId(rs.getLong("CLIENTURLID"));
        advertiseAppUrl.setCode(rs.getString("CODE"));
        advertiseAppUrl.setIosUrl(rs.getString("IOSURL"));
        advertiseAppUrl.setAndroidUrl(rs.getString("ANDROIDURL"));
        advertiseAppUrl.setDefaultUrl(rs.getString("DEFAULTURL"));
        advertiseAppUrl.setRemoveStatus(ActStatus.getByCode(rs.getString("REMOVESTATUS")));
        advertiseAppUrl.setCreateId(rs.getString("CREATEID"));
        advertiseAppUrl.setCreateIp(rs.getString("CREATEIP"));
        advertiseAppUrl.setCreateTime(rs.getTimestamp("CREATETIME"));
        advertiseAppUrl.setLastModifyId(rs.getString("LASTMODIFYID"));
        advertiseAppUrl.setLastModifyIp(rs.getString("LASTMODIFYIP"));
        advertiseAppUrl.setLastModifyTime(rs.getTimestamp("LASTMODIFYTIME"));
        advertiseAppUrl.setCodeName(rs.getString("CODE_NAME"));
        return advertiseAppUrl;
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public AdvertiseAppUrl get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AdvertiseAppUrl> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AdvertiseAppUrl> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {

        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + "(CODE,IOSURL,ANDROIDURL,DEFAULTURL,REMOVESTATUS,CREATEID,CREATEIP,CREATETIME,LASTMODIFYID,LASTMODIFYIP,LASTMODIFYTIME,CODE_NAME) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("INSERT SCRIPT:" + insertSql);
        }

        return insertSql;
    }
}
