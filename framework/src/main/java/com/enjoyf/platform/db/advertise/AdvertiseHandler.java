/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.advertise;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.advertise.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class AdvertiseHandler {
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private AdvertiseAgentAccessor agentAccessor;
    private AdvertiseProjectAccessor projectAccessor;
    private AdvertisePublishAccessor publishAccessor;
    private AdvertisePublishLocationAccessor publishLocationAccessor;
    private AdvertiseUserPublishRelationAccessor userPublishRelationAccessor;
    private AdvertiseEventAccessor eventAccessor;
    private AdvertiseAppUrlAccessor appUrlAccessor;

    private DeviceEventAccessor deviceEventAccessor;
    private ActivityDeviceAccessor activityDeviceAccessor;

    public AdvertiseHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        agentAccessor = TableAccessorFactory.get().factoryAccessor(AdvertiseAgentAccessor.class, dataBaseType);
        projectAccessor = TableAccessorFactory.get().factoryAccessor(AdvertiseProjectAccessor.class, dataBaseType);
        publishAccessor = TableAccessorFactory.get().factoryAccessor(AdvertisePublishAccessor.class, dataBaseType);
        publishLocationAccessor = TableAccessorFactory.get().factoryAccessor(AdvertisePublishLocationAccessor.class, dataBaseType);
        userPublishRelationAccessor = TableAccessorFactory.get().factoryAccessor(AdvertiseUserPublishRelationAccessor.class, dataBaseType);
        eventAccessor = TableAccessorFactory.get().factoryAccessor(AdvertiseEventAccessor.class, dataBaseType);
        appUrlAccessor = TableAccessorFactory.get().factoryAccessor(AdvertiseAppUrlAccessor.class, dataBaseType);
        deviceEventAccessor = TableAccessorFactory.get().factoryAccessor(DeviceEventAccessor.class, dataBaseType);
        activityDeviceAccessor = TableAccessorFactory.get().factoryAccessor(ActivityDeviceAccessor.class, dataBaseType);
    }

    //////////////////////////////////////////////////
    //the agent apis
    //////////////////////////////////////////////////
    public AdvertiseAgent insertAgent(AdvertiseAgent entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return agentAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AdvertiseAgent getAgent(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return agentAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AdvertiseAgent> queryAgents(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<AdvertiseAgent> returnValue = new PageRows<AdvertiseAgent>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(agentAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<AdvertiseAgent> queryAgents(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return agentAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int updateAgent(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return agentAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////
    //the project apis
    //////////////////////////////////////////////////
    public AdvertiseProject insertProject(AdvertiseProject entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return projectAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AdvertiseProject getProject(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return projectAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AdvertiseProject> queryProjects(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<AdvertiseProject> returnValue = new PageRows<AdvertiseProject>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(projectAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<AdvertiseProject> queryProjects(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return projectAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int updateProject(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return projectAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////
    //the publish apis
    //////////////////////////////////////////////////
    public AdvertisePublish insertPublish(AdvertisePublish entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return publishAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AdvertisePublish getPublish(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return publishAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AdvertisePublish> queryPublishs(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<AdvertisePublish> returnValue = new PageRows<AdvertisePublish>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(publishAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<AdvertisePublish> queryPublishs(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return publishAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int updatePublish(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return publishAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////
    //the publish location apis
    //////////////////////////////////////////////////
    public AdvertisePublishLocation insertPublishLocation(AdvertisePublishLocation entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return publishLocationAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AdvertisePublishLocation getPublishLocation(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return publishLocationAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AdvertisePublishLocation> queryPublishLocations(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<AdvertisePublishLocation> returnValue = new PageRows<AdvertisePublishLocation>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(publishLocationAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public List<AdvertisePublishLocation> queryPublishLocations(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return publishLocationAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public int updatePublishLocation(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return publishLocationAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////
    //the user publish relation apis
    //////////////////////////////////////////////////
    public AdvertiseUserPublishRelation insertUserPublishRelation(AdvertiseUserPublishRelation entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userPublishRelationAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AdvertiseUserPublishRelation getUserPublishRelation(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return userPublishRelationAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////////////////////////////////
    //the event apis
    //////////////////////////////////////////////////
    public AdvertiseEvent insertEvent(AdvertiseEvent entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return eventAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    //////////////////////////////////////////
    // the AdvertiseAppUrl method
    ////////////////////////////////

    public AdvertiseAppUrl insertAppUrl(AdvertiseAppUrl advertiseAppUrl) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appUrlAccessor.insert(advertiseAppUrl, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public AdvertiseAppUrl getAdvertiseAppUrl(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appUrlAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyAdvertiseAppUrl(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appUrlAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<AdvertiseAppUrl> listAdvertiseAppUrl(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return appUrlAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<AdvertiseAppUrl> pageAdvertiseAppUrl(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<AdvertiseAppUrl> pageRows = new PageRows<AdvertiseAppUrl>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<AdvertiseAppUrl> listAdvertiseAppUrl = appUrlAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(listAdvertiseAppUrl);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return pageRows;
    }


    /////////
    public DeviceEvent insertDeviceEvent(DeviceEvent deviceEvent) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return deviceEventAccessor.insert(deviceEvent, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyDeviceEvent(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return deviceEventAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public DeviceEvent getDeviceEvent(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return deviceEventAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<DeviceEvent> queryDeviceEvent(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return deviceEventAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<DeviceEvent> queryDeviceEventByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<DeviceEvent> pageRows = new PageRows<DeviceEvent>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<DeviceEvent> list = deviceEventAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////activity_device
    public ActivityDevice insertActivityDevice(ActivityDevice activityDevice) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityDeviceAccessor.insert(activityDevice, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyActivityDevice(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityDeviceAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ActivityDevice getActivityDevice(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityDeviceAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ActivityDevice> queryActivityDevice(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return activityDeviceAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ActivityDevice> queryActivityDeviceByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<ActivityDevice> pageRows = new PageRows<ActivityDevice>();
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<ActivityDevice> list = activityDeviceAccessor.query(queryExpress, pagination, conn);
            pageRows.setPage(pagination);
            pageRows.setRows(list);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

}
