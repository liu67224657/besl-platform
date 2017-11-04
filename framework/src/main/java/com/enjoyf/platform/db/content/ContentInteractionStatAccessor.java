package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.InteractionType;
import com.enjoyf.platform.service.content.stats.TopInteractionContent;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * <p/>
 * Description: the accessor for stat of contentinteractions.
 * </p>
 */
public interface ContentInteractionStatAccessor {
    //
    public List<TopInteractionContent> queryTopInteractionContents(InteractionType type, Date from, Date to, int size, Connection conn) throws DbException;
}
