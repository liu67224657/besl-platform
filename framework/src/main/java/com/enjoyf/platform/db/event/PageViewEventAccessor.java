/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;

import java.sql.Connection;

interface PageViewEventAccessor extends SequenceAccessor {
    //the pageview event entry.
    public PageViewEvent insert(PageViewEvent entry, Connection conn) throws DbException;
}
