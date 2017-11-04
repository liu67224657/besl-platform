package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.event.pageview.PageViewLocation;
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
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class AbstractPageViewLocationAccessor implements PageViewLocationAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractPageViewLocationAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_PV_LOCATION_ID";
    private static final String KEY_TABLE_NAME = "PV_EVENT_LOCATION";

    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }


    @Override
    public PageViewLocation getById(Integer locationId, Connection conn) throws DbException {
        PageViewLocation returnValue = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT * FROM " + KEY_TABLE_NAME + " WHERE LOCATIONID = ? AND VALIDSTATUS = ?");

            pstmt.setInt(1, locationId);
            pstmt.setString(2, ValidStatus.VALID.getCode());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                returnValue = rsToObject(rs);
            }
        } catch (SQLException e) {
            GAlerter.lab("On getById, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    @Override
    public List<PageViewLocation> queryAll(Connection conn) throws DbException {
        List<PageViewLocation> returnValue = new ArrayList<PageViewLocation>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM " + KEY_TABLE_NAME + " WHERE VALIDSTATUS = ?";
        if (logger.isDebugEnabled()) {
            logger.debug("The location queryAll sql:" + sql);
        }

        try {
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, ValidStatus.VALID.getCode());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnValue.add(rsToObject(rs));
            }
        } catch (SQLException e) {
            GAlerter.lab("On queryAll, a SQLException occured:", e);

            throw new DbException(DbException.SQL_GENERIC, e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return returnValue;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    protected PageViewLocation rsToObject(ResultSet rs) throws SQLException {
        PageViewLocation entity = new PageViewLocation();

        //LOCATIONID, LOCATIONNAME, LOCATIONLEVEL, LOCATIONURLREGEX, VALIDSTATUS,
        entity.setLocationId(rs.getString("LOCATIONID"));

        entity.setLocationName(rs.getString("LOCATIONNAME"));
        entity.setLocationLevel(rs.getInt("LOCATIONLEVEL"));

        entity.setLocationUrlRegex(rs.getString("LOCATIONURLREGEX"));

        entity.setValidStatus(ValidStatus.getByCode(rs.getString("VALIDSTATUS")));

        return entity;
    }
}
