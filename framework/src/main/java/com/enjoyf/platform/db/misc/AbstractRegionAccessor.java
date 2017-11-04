/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;


import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.Region;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AbstractRegionAccessor implements RegionAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractRegionAccessor.class);

    //
    private static final String KEY_TABLE_NAME = "MISC_REGION";

    //
    @Override
    public List<Region> getAllRegion(Connection conn) throws DbException {
        List<Region> returnValue = new ArrayList<Region>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + KEY_TABLE_NAME);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On getAllRegions, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    protected Region rsToObject(ResultSet rs) throws SQLException {
        Region entry = new Region();

        //
        entry.setRegionId(rs.getInt("REGIONID"));
        entry.setRegionName(rs.getString("REGIONNAME"));
        entry.setParentRegionId(rs.getInt("PARENTREGIONID"));
        entry.setRegionLevel(rs.getInt("REGIONLEVEL"));

        return entry;
    }
}
