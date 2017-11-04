/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.tools.tablehashcode;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface HashCodeAccessor {
    //update
    public boolean updateHashCode(String tableName, String srcColumnName, String scrColumnValue, String destColumnName, Connection conn) throws DbException;

    //query
    public List<String> queryUniqueKeys(String tableName, String srcColumnName, Pagination page, Connection conn) throws DbException;
}
