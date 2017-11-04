/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.userprops;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface UserPropsAccessor {
    public UserProperty select(UserPropKey key, Connection conn) throws DbException;

    public List<UserProperty> query(UserPropKey key, Connection conn) throws DbException;

    public UserProperty insert(UserProperty userProp, Connection conn) throws DbException;

    public boolean delete(UserPropKey key, Connection conn) throws DbException;

    public boolean update(UserProperty userProp, Connection conn) throws DbException;

    public boolean increase(UserPropKey key, long newValue, long orgValue, Connection conn) throws DbException;
}
