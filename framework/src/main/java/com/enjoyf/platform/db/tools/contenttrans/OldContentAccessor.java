/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.tools.contenttrans;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.tools.contenttrans.OldContent;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface OldContentAccessor {
    //query
    public List<OldContent> query(Pagination page, Connection conn) throws DbException;
}
