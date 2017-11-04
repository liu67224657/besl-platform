/**
 * (C) 2010 Fivewh platform platform.com
 */
package com.enjoyf.platform.db.sequence;

import java.sql.Connection;

import com.enjoyf.platform.db.DbException;

public interface SequenceAccessor {
    //get the sequence no
    public long getSeqNo(Connection conn) throws DbException;
}
