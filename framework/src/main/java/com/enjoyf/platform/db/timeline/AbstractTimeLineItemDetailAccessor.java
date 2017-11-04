package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.service.timeline.TimeLineDetailType;
import com.enjoyf.platform.service.timeline.TimeLineDomain;
import com.enjoyf.platform.service.timeline.TimeLineItemDetail;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public abstract class AbstractTimeLineItemDetailAccessor extends AbstractBaseTableAccessor<TimeLineItemDetail> implements TimeLineDetailAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTimeLineItemDetailAccessor.class);

    private static final String TABLEN_NAME_PREFIX = "TIMELINE_ITEM_DETAIL_";
    private static final int TABLE_NUM = 100;

    @Override
    public TimeLineItemDetail insert(TimeLineItemDetail detail, Connection conn) throws DbException {
        TimeLineItemDetail returnObj = null;

        PreparedStatement pstmt = null;

        try {
            //TLID, DETAILUNO,DIRECTID, DIRECTUNO, DETAILTYPE, REMOVESTATUS, OWNUNO, TLDOMAIN, CREATEDATE
            pstmt = conn.prepareStatement(getInsertSql(detail.getOwnUno()));
            pstmt.setLong(1, detail.getTlId());
            pstmt.setString(2, detail.getDetailUno());
            pstmt.setString(3, detail.getDirectId());
            pstmt.setString(4, detail.getDirectUno());
            pstmt.setString(5, detail.getTimeLineDetailType().getCode());
            pstmt.setString(6, detail.getRemoveStatus().getCode());
            pstmt.setString(7, detail.getOwnUno());
            pstmt.setString(8, detail.getDomain().getCode());
            pstmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));

            returnObj = pstmt.executeUpdate() > 0 ? detail : null;
        } catch (SQLException e) {
            GAlerter.lab("On insert ContentInteraction, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return returnObj;
    }

    @Override
    public List<TimeLineItemDetail> queryBySize(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return super.query(getTableName(ownUno),queryExpress,page,conn);
    }

    public int updateDetail(String ownUno, UpdateExpress updateExpress,QueryExpress queryExpress,Connection conn) throws DbException{
        return super.update(getTableName(ownUno),updateExpress,queryExpress,conn);
    }

    public int deleteDetail(String ownUno,QueryExpress queryExpress,Connection conn) throws DbException{
        return super.delete(getTableName(ownUno),queryExpress,conn);
    }


    @Override
    protected TimeLineItemDetail rsToObject(ResultSet rs) throws SQLException {
        TimeLineItemDetail entry = new TimeLineItemDetail();

        //TLID,OWNUNO,TLDOMAIN, TLCONTENTTYPE, DIRECTUNO,DIRECTID,RELATIONUNO,RELATIONID,FILTERTYPE,DESCRIPTION,CREATEDATE,REMOVESTATUS,REMOVEDATE
        entry.setTlId(rs.getLong("TLID"));
        entry.setDetailUno(rs.getString("DETAILUNO"));
        entry.setOwnUno(rs.getString("OWNUNO"));
        entry.setDomain(TimeLineDomain.getByCode(rs.getString("TLDOMAIN")));


        entry.setDirectUno(rs.getString("DIRECTUNO"));
        entry.setDirectId(rs.getString("DIRECTID"));
        entry.setTimeLineDetailType(TimeLineDetailType.getByCode(rs.getString("DETAILTYPE")));

        entry.setCreateDate(rs.getTimestamp("CREATEDATE") != null ? new Date(rs.getTimestamp("CREATEDATE").getTime()) : null);


        entry.setRemoveDate(rs.getTimestamp("REMOVEDATE") != null ? new Date(rs.getTimestamp("REMOVEDATE").getTime()) : null);

        return entry;
    }

    private String getTableName(String ownUno) {
        return TABLEN_NAME_PREFIX + TableUtil.getTableNumSuffix(ownUno.hashCode(), TABLE_NUM);
    }

    private String getInsertSql(String ownUno) {
        String insertSql = "INSERT INTO " + getTableName(ownUno) + " (TLID,DETAILUNO, DIRECTID, DIRECTUNO, DETAILTYPE, REMOVESTATUS, OWNUNO, TLDOMAIN, CREATEDATE) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("ContentReply INSERT Script:" + insertSql);
        }

        return insertSql;
    }

}
