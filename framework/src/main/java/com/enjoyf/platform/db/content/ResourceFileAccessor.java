/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.ResourceFile;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a> ,zx
 */
interface ResourceFileAccessor {
    //insert
    public ResourceFile insert(ResourceFile file, Connection conn) throws DbException;

    //increate the used times
    public boolean increaseUsedTimes(String fileId, int delta, Connection conn) throws DbException;

    //update resource file
    public boolean update(String fileId, Map<ObjectField, Object> map, Connection conn) throws DbException;

    //query resource files
}
