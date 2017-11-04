/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.Date;

interface PageViewEventStatsAccessor {
    //the pageview event stats apis.
    //the page view.
    public long statsPageView(Date month, QueryExpress queryExpress, Connection conn) throws DbException;

    //the unique user.
    public long statsUniqueUser(Date month, QueryExpress queryExpress, Connection conn) throws DbException;
}
