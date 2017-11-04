/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.misc.Feedback;

import java.sql.Connection;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface FeedbackAccessor extends SequenceAccessor{
    //insert a feedback
    public Feedback insert(Feedback feedback, Connection conn) throws DbException;

}
