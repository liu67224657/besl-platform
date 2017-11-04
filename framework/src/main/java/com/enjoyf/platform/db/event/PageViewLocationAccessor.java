/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.event.pageview.PageViewLocation;

import java.sql.Connection;
import java.util.List;

interface PageViewLocationAccessor extends SequenceAccessor {
    //the pageview location entry.
    //get.
    public PageViewLocation getById(Integer locationId, Connection conn) throws DbException;

    //query
    public List<PageViewLocation> queryAll(Connection conn) throws DbException;

}
