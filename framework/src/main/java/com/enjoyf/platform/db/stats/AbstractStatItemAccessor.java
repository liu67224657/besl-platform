package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableUtil;
import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;
import com.enjoyf.platform.service.stats.*;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
class AbstractStatItemAccessor extends AbstractBaseTableAccessor<StatItem> implements StatItemAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractStatItemAccessor.class);

    //
    private static final String KEY_SEQUENCE_NAME = "SEQ_STAT_ITEM_ID";
    private static String KEY_TABLE_NAME_PREFIX = "STAT_ITEM_";
    private static String KEY_TABLE_SUFFIX_FMT = "yyyyMM";
    private static String KEY_UNDERLINE = "_";

    //
    public long getSeqNo(Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(KEY_SEQUENCE_NAME, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + KEY_SEQUENCE_NAME);
        }
    }

    public StatItem get(StatItem entry, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(StatItemField.DOMAIN, entry.getStatDomain().getCode()));
        queryExpress.add(QueryCriterions.eq(StatItemField.SECTION, entry.getStatSection().getCode()));
        queryExpress.add(QueryCriterions.eq(StatItemField.DATETYPE, entry.getDateType().getCode()));
        queryExpress.add(QueryCriterions.eq(StatItemField.STATDATE, entry.getStatDate()));
        queryExpress.add(QueryCriterions.eq(StatItemField.PUBLISHID, entry.getPublishId()));
        return super.get(getTableName(entry.getStatDomain().getDomainPrefix(), entry.getStatDate()), queryExpress, conn);
    }

    public StatItem insert(StatItem entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        try {
            entry.setItemId(getSeqNo(conn));

            pstmt = conn.prepareStatement(getInsertSql(entry));

            pstmt.setLong(1, entry.getItemId());
            pstmt.setString(2, entry.getStatDomain().getCode());
            pstmt.setString(3, entry.getDomainName());

            pstmt.setString(4, entry.getStatSection().getCode());
            pstmt.setString(5, entry.getSectionName());

            pstmt.setString(6, entry.getDateType().getCode());

            pstmt.setTimestamp(7, new Timestamp(entry.getStatDate().getTime()));
            pstmt.setLong(8, entry.getStatValue());
            pstmt.setString(9, entry.getExtData().toJson());

            pstmt.setTimestamp(10, new Timestamp(entry.getReportDate().getTime()));

            pstmt.setString(11, entry.getPublishId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert StatItem, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    public StatItem update(StatItem entry, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String updateSql = "UPDATE " + this.getTableName(entry.getStatDomain().getDomainPrefix(), entry.getStatDate())
                + " SET STATVALUE = ?, REPORTDATE = ? WHERE ITEMID = ?";
        try {
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setLong(1, entry.getStatValue());
            pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            pstmt.setLong(3, entry.getItemId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On update StatItem, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    public StatItem increase(StatItem entry, long delta, Connection conn) throws DbException {
        PreparedStatement pstmt = null;

        String updateSql = "UPDATE " + this.getTableName(entry.getStatDomain().getDomainPrefix(), entry.getStatDate())
                + " SET STATVALUE = STATVALUE + ? WHERE ITEMID = ?";
        try {
            pstmt = conn.prepareStatement(updateSql);

            pstmt.setLong(1, delta);
            pstmt.setLong(2, entry.getItemId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On increase StatItem, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    public List<StatItem> query(StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to, Connection conn) throws DbException {
        List<StatItem> entry = new ArrayList<StatItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String querySql = null;

        try {
            for (Date date : DateUtil.getMonthList(from, to)) {
                querySql = "SELECT * FROM " + this.getTableName(domain.getDomainPrefix(), date)
                        + " WHERE STATDATE >= ? AND STATDATE <= ? AND DOMAIN = ? AND SECTION = ? AND DATETYPE = ? ORDER BY STATDATE";

                pstmt = conn.prepareStatement(querySql);

                pstmt.setTimestamp(1, new Timestamp(from.getTime()));
                pstmt.setTimestamp(2, new Timestamp(to.getTime()));
                pstmt.setString(3, domain.getCode());
                pstmt.setString(4, section.getCode());
                pstmt.setString(5, dateType.getCode());

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    entry.add(rsToObject(rs));
                }

            }
        } catch (SQLException e) {
            GAlerter.lab("On query StatItem, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }

    @Override
    public List<StatItem> query(String advertiseId, StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to, Connection conn) throws DbException {
        List<StatItem> entry = new ArrayList<StatItem>();

        for (Date date : DateUtil.getMonthList(from, to)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(StatItemField.DOMAIN, domain.getCode()));
            queryExpress.add(QueryCriterions.eq(StatItemField.SECTION, section.getCode()));
            queryExpress.add(QueryCriterions.eq(StatItemField.DATETYPE, dateType.getCode()));
            queryExpress.add(QueryCriterions.geq(StatItemField.STATDATE, new Timestamp(from.getTime())));
            queryExpress.add(QueryCriterions.leq(StatItemField.STATDATE, new Timestamp(to.getTime())));
            queryExpress.add(QueryCriterions.eq(StatItemField.PUBLISHID, StringUtil.isEmpty(advertiseId) ? null : advertiseId));
            queryExpress.add(QuerySort.add(StatItemField.STATDATE, QuerySortOrder.ASC));

            List<StatItem> list = super.query(getTableName(domain.getDomainPrefix(), date), queryExpress, conn);

            for (StatItem item : list) {
                entry.add(item);
            }
        }

        return entry;
    }

    public List<StatItem> query(StatDomain domain, StatDateType dateType, Date statDate, Connection conn) throws DbException {
        List<StatItem> entry = new LinkedList<StatItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String querySql = "SELECT * FROM " + this.getTableName(domain.getDomainPrefix(), statDate)
                + " WHERE STATDATE = ? AND DOMAIN = ? AND DATETYPE = ?  ORDER BY STATVALUE DESC";
        if (logger.isDebugEnabled()) {
            logger.debug("The query sql:" + querySql);
        }

        try {

            pstmt = conn.prepareStatement(querySql);

            pstmt.setTimestamp(1, new Timestamp(statDate.getTime()));
            pstmt.setString(2, domain.getCode());
            pstmt.setString(3, dateType.getCode());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                entry.add(rsToObject(rs));
            }

        } catch (SQLException e) {
            GAlerter.lab("On query StatItem, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

//        // sort by section
//        Collections.sort(entry, new Comparator<StatItem>() {
//            public int compare(StatItem item1, StatItem item2) {
//                return item1.getStatSection().getCode().compareTo(item2.getStatSection().getCode());
//            }
//        });

        return entry;
    }

    @Override
    public List<StatItem> query(StatDomain domain, StatDateType dateType, Date statDate, Pagination p, Connection conn) throws DbException {
        List<StatItem> entries = new LinkedList<StatItem>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String querySql = null;

        try {
            querySql = "SELECT * FROM " + this.getTableName(domain.getDomainPrefix(), statDate)
                    + " WHERE STATDATE = ? AND DOMAIN = ? AND DATETYPE = ?  ORDER BY STATVALUE DESC";

            pstmt = conn.prepareStatement(querySql);

            pstmt.setTimestamp(1, new Timestamp(statDate.getTime()));
            pstmt.setString(2, domain.getCode());
            pstmt.setString(3, dateType.getCode());

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

    // ////////////////////////////////////////////////////////////////////////////
    protected String getTableName(StatDomainPrefix prefix, Date d) {
        return KEY_TABLE_NAME_PREFIX + prefix.getPartitionCode() + KEY_UNDERLINE + TableUtil.getTableDateSuffix(d, KEY_TABLE_SUFFIX_FMT);
    }

    private String getInsertSql(StatItem entry) throws DbException {
        String tableName = this.getTableName(entry.getStatDomain().getDomainPrefix(), entry.getStatDate());

        if (tableName == null) {
            throw new DbException(DbException.SQL_GENERIC, "The StatItem's partition table is null.");
        }

        String insertSql = "INSERT INTO " + tableName + " (ITEMID, DOMAIN, DOMAINNAME, SECTION, SECTIONNAME, DATETYPE, STATDATE, STATVALUE, EXTDATA, REPORTDATE,PUBLISHID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("StatItem INSERT Script:" + insertSql);
        }

        return insertSql;
    }

    protected StatItem rsToObject(ResultSet rs) throws SQLException {
        StatItem entry = new StatItem();

        entry.setItemId(rs.getLong("ITEMID"));

        entry.setStatDomain(rs.getString("DOMAIN") == null ? null : new StatDomainDefault(rs.getString("DOMAIN")));
        entry.setDomainName(rs.getString("DOMAINNAME"));

        entry.setStatSection(new StatSectionDefault(rs.getString("SECTION")));
        entry.setSectionName(rs.getString("SECTIONNAME"));

        entry.setDateType(StatDateType.getByCode(rs.getString("DATETYPE")));
        entry.setStatDate(new Date(rs.getTimestamp("STATDATE").getTime()));

        entry.setStatValue(rs.getLong("STATVALUE"));
        entry.setExtData(StatItemExtData.parse(rs.getString("EXTDATA")));

        entry.setReportDate(new Date(rs.getTimestamp("REPORTDATE").getTime()));
        entry.setPublishId(rs.getString("PUBLISHID"));
        return entry;
    }

    @Override
    public StatItem sumStatsValue(StatSection section, StatDomain domain, StatDateType dateType, Date startDate, Date endDate, Connection conn) throws DbException {

        StatItem entry = new StatItem();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String querySql = null;

        try {
            if (section == null) {
                querySql = "SELECT SUM(STATVALUE) AS V,DOMAINNAME,SECTIONNAME,REPORTDATE FROM "
                        + this.getTableName(domain.getDomainPrefix(), startDate)
                        + " WHERE DOMAIN = ? AND STATDATE >= ? AND STATDATE<? AND DATETYPE = ? GROUP BY DOMAINNAME";
                pstmt = conn.prepareStatement(querySql);

                pstmt.setString(1, domain.getCode());
                pstmt.setTimestamp(2, new Timestamp(startDate.getTime()));
                pstmt.setTimestamp(3, new Timestamp(endDate.getTime()));
                pstmt.setString(4, dateType.getCode());

            } else {
                querySql = "SELECT SUM(STATVALUE) AS V,DOMAINNAME,SECTIONNAME,REPORTDATE FROM "
                        + this.getTableName(domain.getDomainPrefix(), startDate)
                        + " WHERE SECTION=? AND DOMAIN = ? AND STATDATE >= ? AND STATDATE<? AND DATETYPE = ? GROUP BY DOMAINNAME,SECTIONNAME";

                pstmt = conn.prepareStatement(querySql);

                pstmt.setString(1, section.getCode());
                pstmt.setString(2, domain.getCode());
                pstmt.setTimestamp(3, new Timestamp(startDate.getTime()));
                pstmt.setTimestamp(4, new Timestamp(endDate.getTime()));
                pstmt.setString(5, dateType.getCode());
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                entry.setStatDomain(domain);
                entry.setDomainName(rs.getString("DOMAINNAME"));

                entry.setStatSection(section);
                entry.setSectionName(rs.getString("SECTIONNAME"));

                entry.setDateType(StatDateType.MONTH);
                entry.setStatDate(new Timestamp(startDate.getTime()));

                entry.setStatValue(rs.getLong("V"));
                entry.setReportDate(new Date(rs.getTimestamp("REPORTDATE").getTime()));
            }
        } catch (SQLException e) {
            GAlerter.lab("On query StatItems by page, a SQLException occured.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }

        return entry;
    }
}
