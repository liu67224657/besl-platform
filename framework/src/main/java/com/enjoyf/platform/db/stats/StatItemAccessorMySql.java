/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.stats.StatDomain;
import com.enjoyf.platform.service.stats.StatItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
class StatItemAccessorMySql extends AbstractStatItemAccessor {
    @Override
    public List<StatItem> query(StatDomain domain, StatDateType dateType, Date statDate, Pagination p, Connection conn) throws DbException {
        List<StatItem> entries = new LinkedList<StatItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String querySql = null;

        try {
            //
            p.setTotalRows(queryRowSize(domain, dateType, statDate, conn));

            //
            querySql = "SELECT * FROM " + this.getTableName(domain.getDomainPrefix(), statDate)
                    + " WHERE STATDATE = ? AND DOMAIN = ? AND DATETYPE = ?  ORDER BY STATVALUE DESC LIMIT ?, ?";

            pstmt = conn.prepareStatement(querySql);

            pstmt.setTimestamp(1, new Timestamp(statDate.getTime()));
            pstmt.setString(2, domain.getCode());
            pstmt.setString(3, dateType.getCode());
            pstmt.setInt(4, p.getStartRowIdx());
            pstmt.setInt(5, p.getPageSize());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                entries.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query StatItems by page, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return entries;
    }

    protected int queryRowSize(StatDomain domain, StatDateType dateType, Date statDate, Connection conn) throws DbException {
        int size = 0;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(1) FROM " + this.getTableName(domain.getDomainPrefix(), statDate)
                + " WHERE STATDATE = ? AND DOMAIN = ? AND DATETYPE = ?";

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setTimestamp(1, new Timestamp(statDate.getTime()));
            pstmt.setString(2, domain.getCode());
            pstmt.setString(3, dateType.getCode());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            GAlerter.lab("On userEventTypeSum queryRowSize, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return size;
    }

}
