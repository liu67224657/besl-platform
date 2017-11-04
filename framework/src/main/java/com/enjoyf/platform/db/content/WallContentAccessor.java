/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.DiscoveryWallContent;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface WallContentAccessor {
    //insert
    public DiscoveryWallContent insert(DiscoveryWallContent entry, Connection conn) throws DbException;

    //query wall wall content
    public List<DiscoveryWallContent> query(Pagination page, Connection conn) throws DbException;
}
