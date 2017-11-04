/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db;

import com.enjoyf.platform.db.sequence.TableSequenceException;
import com.enjoyf.platform.db.sequence.TableSequenceFetcher;

import java.sql.Connection;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-30 下午1:04
 * Description:
 */
public abstract class AbstractSequenceBaseTableAccessor<T> extends AbstractBaseTableAccessor<T> {
    //
    public long getSeqNo(String sequenceName, Connection conn) throws DbException {
        try {
            return TableSequenceFetcher.get().generate(sequenceName, conn);
        } catch (TableSequenceException e) {
            throw new DbException(DbException.TABLE_SEQUENCE_ERROR, "Fetch sequence value error, sequence:" + sequenceName);
        }
    }
}
