/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.stats.StatDomain;
import com.enjoyf.platform.service.stats.StatItem;
import com.enjoyf.platform.service.stats.StatSection;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;
import java.util.*;

public class StatHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    private StatItemAccessor itemAccessor;

    public StatHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        // create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        itemAccessor = StatAccessorFactory.statFactory(dataBaseType);


    }

    public StatItem saveItem(StatItem entry) throws DbException {
        StatItem returnValue = null;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            StatItem oldItem = itemAccessor.get(entry, conn);

            //the item is not exist
            if (oldItem == null) {
                returnValue = itemAccessor.insert(entry, conn);
            } else {
                entry.setItemId(oldItem.getItemId());

                returnValue = itemAccessor.update(entry, conn);
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public StatItem getStatsItem(StatDomain domain, StatSection section, StatDateType dateType, Date statDate) throws DbException {
        StatItem returnValue = null;
        Connection conn = null;

        //
        StatItem queryStat = new StatItem();

        queryStat.setDateType(dateType);
        queryStat.setStatDomain(domain);
        queryStat.setStatSection(section);
        queryStat.setStatDate(statDate);

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = itemAccessor.get(queryStat, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<StatItem> query(StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to) throws DbException {
        List<StatItem> entries = new ArrayList<StatItem>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            entries.addAll(itemAccessor.query(domain, section, dateType, from, to, conn));
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return entries;
    }

    public List<StatItem> query(String advertiseId, StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to) throws DbException {
        List<StatItem> entries = new ArrayList<StatItem>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            entries.addAll(itemAccessor.query(advertiseId, domain, section, dateType, from, to, conn));
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return entries;
    }

    public Map<StatSection, StatItem> query(StatDomain domain, StatDateType dateType, Date statDate) throws DbException {
        Map<StatSection, StatItem> entries = new LinkedHashMap<StatSection, StatItem>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<StatItem> items = itemAccessor.query(domain, dateType, statDate, conn);

            if (!CollectionUtil.isEmpty(items)) {
                for (StatItem item : items) {
                    entries.put(item.getStatSection(), item);
                }
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return entries;
    }

    public Map<StatSection, StatItem> query(StatDomain domain, StatDateType dateType, Date statDate, Pagination p) throws DbException {
        Map<StatSection, StatItem> entries = new LinkedHashMap<StatSection, StatItem>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<StatItem> items = itemAccessor.query(domain, dateType, statDate, p, conn);

            if (!CollectionUtil.isEmpty(items)) {
                for (StatItem item : items) {
                    entries.put(item.getStatSection(), item);
                }
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return entries;
    }

    public StatItem sumStatsValue(StatSection section, StatDomain domain, StatDateType dateType, Date startDate,
                                  Date endDate) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return itemAccessor.sumStatsValue(section, domain, dateType, startDate, endDate, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


}
