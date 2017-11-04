/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.db.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.event.user.UserEventEntry;
import com.enjoyf.platform.service.event.user.UserEventType;

import java.sql.Connection;
import java.util.Date;

interface UserEventAccessor extends SequenceAccessor {
    //the player event entry.
    public UserEventEntry insert(UserEventEntry entry, Connection conn) throws DbException;

    //stats
    public long userEventTypeSum(UserEventType eventType, Date from, Date to, Connection conn) throws DbException;

    public long userEventTypeDistinct(UserEventType eventType, Date from, Date to, Connection conn) throws DbException;
}
