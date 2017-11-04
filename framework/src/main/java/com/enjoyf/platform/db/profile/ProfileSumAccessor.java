/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.profile;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.profile.ProfileSum;
import com.enjoyf.platform.util.sql.ObjectField;

import java.sql.Connection;
import java.util.Map;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface ProfileSumAccessor {
    //
    public ProfileSum insert(ProfileSum sum, Connection conn) throws DbException;

    //
    public boolean increase(String uno, Map<ObjectField, Object> keyDeltas, Connection conn) throws DbException;

    public boolean increase(String uno, ObjectField field, Integer value, Connection conn) throws DbException;

    //
    public boolean update(String uno, Map<ObjectField, Object> keyValues, Connection conn) throws DbException;

    //
    public ProfileSum get(String uno, Connection conn) throws DbException;
}
