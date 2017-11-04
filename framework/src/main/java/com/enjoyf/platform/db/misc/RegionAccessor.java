/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.Region;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface RegionAccessor {
    //get all.
    public List<Region> getAllRegion(Connection conn) throws DbException;
}
