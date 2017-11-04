/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.userprops;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbConnException;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.DbTypeException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.userprops.UserPropKey;
import com.enjoyf.platform.service.userprops.UserProperty;
import com.enjoyf.platform.util.FiveProps;

import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class UserPropsHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;
    private UserPropsAccessor accessor;

    public UserPropsHandler(String ds, FiveProps props) throws DbException {
        dataSourceName = ds.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();
        accessor = UserPropsAccessorFactory.factory(dataBaseType);
    }

    public UserProperty getUserProps(UserPropKey key) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return accessor.select(key, conn);
        } catch (DataSourceException e) {
            throw e;
        } catch (DbConnException e) {
            throw e;
        } catch (DbTypeException e) {
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public List<UserProperty> queryUserProps(UserPropKey key) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return accessor.query(key, conn);
        } catch (DataSourceException e) {
            throw e;
        } catch (DbConnException e) {
            throw e;
        } catch (DbTypeException e) {
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public UserProperty increaseUserProps(UserPropKey key, long value, int maxTryTimes) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            UserProperty temp = null;
            int tryTimes = 0;

            do {
                temp = accessor.select(key, conn);

                //if no the user prop, create it.
                if (temp == null) {
                    UserProperty up = new UserProperty();

                    up.setUserPropKey(key);
                    up.setValue(Long.toString(value));

                    temp = accessor.insert(up, conn);
                } else {
                    //get the current value.
                    long orgValue = Long.parseLong(temp.getValue());

                    //increate it.
                    if (accessor.increase(key, orgValue + value, orgValue, conn)) {
                        temp.setValue(Long.toString(orgValue + value));
                    } else {
                        temp = null;
                    }
                }

                tryTimes++;

                if (tryTimes > 1) {
                    //GAlerterLogger.lm("Try to increase the user prop, times:" + tryTimes + ", key:" + key);
                }
            } while (tryTimes < maxTryTimes && temp == null);

            return temp;
        } catch (DataSourceException e) {
            throw e;
        } catch (DbConnException e) {
            throw e;
        } catch (DbTypeException e) {
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean setUserProps(UserProperty userProp) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            UserProperty temp = null;
            temp = accessor.select(userProp.getKey(), conn);

            if (temp == null) {
                if (accessor.insert(userProp, conn) != null) {
                    return true;
                }
            } else {
                return accessor.update(userProp, conn);
            }
        } catch (DataSourceException e) {
            throw e;
        } catch (DbConnException e) {
            throw e;
        } catch (DbTypeException e) {
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return false;
    }

    public boolean deleteUserProps(UserPropKey key) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return accessor.delete(key, conn);
        } catch (DataSourceException e) {
            throw e;
        } catch (DbConnException e) {
            throw e;
        } catch (DbTypeException e) {
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateUserProperty(UserProperty property) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return accessor.update(property, conn);
        } catch (DataSourceException e) {
            throw e;
        } catch (DbConnException e) {
            throw e;
        } catch (DbTypeException e) {
            throw e;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}
