package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.AbstractSequenceBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewLineSumData;
import com.enjoyf.platform.service.viewline.ViewLineSumDomain;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public abstract class AbstractViewLineSumDataAccessor extends AbstractSequenceBaseTableAccessor<ViewLineSumData> implements ViewLineSumDataAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractViewLineSumDataAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "VIEWLINE_CATEGORY_SUM";

    @Override
    public ViewLineSumData insert(ViewLineSumData entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(getInsertSql());

            //SRCID, SUMDOMAIN, SUMTYPECODE, SUMNUM01, SUMNUM02, SUMNUM03, SUMNUM04, SUMNUM05, SUMNUM06, SUMNUM07, SUMNUM08
            pstmt.setInt(1, entry.getSrcId());

            pstmt.setString(2, entry.getSumDomain().getCode());
            pstmt.setString(3, entry.getSumTypeCode());

            pstmt.setInt(4, entry.getViewTimes());
            pstmt.setInt(5, entry.getPostTimes());
            pstmt.setInt(6, entry.getReplyTimes());
            pstmt.setInt(7, entry.getFavorTimes());
            pstmt.setInt(8, entry.getForwardTimes());
            pstmt.setInt(9, 0);
            pstmt.setInt(10, 0);
            pstmt.setInt(11, 0);


            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert ViewLineSumData, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public ViewLineSumData get(QueryExpress getExpress, Connection conn) throws DbException {
        //
        return get(KEY_TABLE_NAME, getExpress, conn);
    }

    @Override
    protected ViewLineSumData rsToObject(ResultSet rs) throws SQLException {
        ViewLineSumData entry = new ViewLineSumData();

        //SRCID, SUMDOMAIN, SUMTYPECODE, SUMNUM01, SUMNUM02, SUMNUM03, SUMNUM04, SUMNUM05, SUMNUM06, SUMNUM07, SUMNUM08
        entry.setSrcId(rs.getInt("SRCID"));

        entry.setSumDomain(ViewLineSumDomain.getByCode(rs.getString("SUMDOMAIN")));
        entry.setSumTypeCode(rs.getString("SUMTYPECODE"));

        entry.setViewTimes(rs.getInt("SUMNUM01"));
        entry.setPostTimes(rs.getInt("SUMNUM02"));
        entry.setReplyTimes(rs.getInt("SUMNUM03"));
        entry.setFavorTimes(rs.getInt("SUMNUM04"));
        entry.setForwardTimes(rs.getInt("SUMNUM05"));

        return entry;
    }

    @Override
    public List<ViewLineSumData> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    private String getInsertSql() throws DbException {
        String insertSql = "INSERT INTO " + KEY_TABLE_NAME + " (SRCID, SUMDOMAIN, SUMTYPECODE, SUMNUM01, SUMNUM02, SUMNUM03, SUMNUM04, SUMNUM05, SUMNUM06, SUMNUM07, SUMNUM08) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ViewLineSumData INSERT Script:" + insertSql);
        }

        return insertSql;
    }
}
