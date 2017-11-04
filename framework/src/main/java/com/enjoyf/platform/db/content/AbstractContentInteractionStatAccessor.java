/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.content;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.InteractionType;
import com.enjoyf.platform.service.content.stats.TopInteractionContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

abstract class AbstractContentInteractionStatAccessor implements ContentInteractionStatAccessor {
    //
    private static final Logger logger = LoggerFactory.getLogger(AbstractContentInteractionStatAccessor.class);

    //
    protected static final String KEY_TABLE_NAME = "VIEW_CONTENT_INTERACTION";

    public List<TopInteractionContent> queryTopInteractionContents(InteractionType type, Date from, Date to, int size, Connection conn) throws DbException {
        return Collections.emptyList();
    }

    protected TopInteractionContent rsToTopInteractionContent(ResultSet rs) throws SQLException {
        TopInteractionContent entry = new TopInteractionContent();

        entry.setContentId(rs.getString("CONTENTID"));
        entry.setContentUno(rs.getString("CONTENTUNO"));

        entry.setValue(rs.getInt("V"));

        return entry;
    }
}
