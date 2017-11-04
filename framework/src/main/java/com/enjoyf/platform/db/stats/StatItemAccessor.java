package com.enjoyf.platform.db.stats;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.stats.StatDomain;
import com.enjoyf.platform.service.stats.StatItem;
import com.enjoyf.platform.service.stats.StatSection;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

interface StatItemAccessor extends SequenceAccessor {

    public StatItem get(StatItem entry, Connection conn) throws DbException;

    public StatItem insert(StatItem entry, Connection conn) throws DbException;

    public StatItem update(StatItem entry, Connection conn) throws DbException;

    public StatItem increase(StatItem entry, long delta, Connection conn) throws DbException;

    public StatItem sumStatsValue(StatSection section, StatDomain domain, StatDateType dateType, Date startDate, Date endDate, Connection conn) throws DbException;

    public List<StatItem> query(StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to, Connection conn) throws DbException;

    public List<StatItem> query(String advertiseId, StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to, Connection conn) throws DbException;

    public List<StatItem> query(StatDomain domain, StatDateType dateType, Date statDate, Connection conn) throws DbException;

    public List<StatItem> query(StatDomain domain, StatDateType dateType, Date statDate, Pagination p, Connection conn) throws DbException;
}
