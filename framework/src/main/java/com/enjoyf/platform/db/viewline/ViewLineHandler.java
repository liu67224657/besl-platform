/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class ViewLineHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private ViewLineAccessor viewLineAccessor;
    private ViewLineItemAccessor viewLineItemAccessor;
    private ViewCategoryAccessor viewCategoryAccessor;
    private ViewCategoryPrivacyAccessor viewCategoryPrivacyAccessor;
    private ViewLineSumDataAccessor viewLineSumDataAccessor;
    private ViewLineLogAccessor viewLineLogAccessor;


    //
    public ViewLineHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        viewLineItemAccessor = TableAccessorFactory.get().factoryAccessor(ViewLineItemAccessor.class, dataBaseType);
        viewLineAccessor = TableAccessorFactory.get().factoryAccessor(ViewLineAccessor.class, dataBaseType);
        viewCategoryAccessor = TableAccessorFactory.get().factoryAccessor(ViewCategoryAccessor.class, dataBaseType);
        viewCategoryPrivacyAccessor = TableAccessorFactory.get().factoryAccessor(ViewCategoryPrivacyAccessor.class, dataBaseType);
        viewLineSumDataAccessor = TableAccessorFactory.get().factoryAccessor(ViewLineSumDataAccessor.class, dataBaseType);
        viewLineLogAccessor = TableAccessorFactory.get().factoryAccessor(ViewLineLogAccessor.class, dataBaseType);
    }

    //###############################VIEWLINEITEM###################################
    //insert
    public ViewLineItem insertLineItem(ViewLineItem entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineItemAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public ViewLineItem getLineItem(QueryExpress queryExpress) throws DbException {
        ViewLineItem returnValue = null;

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = viewLineItemAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //query
    public List<ViewLineItem> queryLineItems(QueryExpress queryExpress) throws DbException {
        List<ViewLineItem> returnValue = null;

        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = viewLineItemAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //query
    public PageRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Pagination page) throws DbException {
        PageRows<ViewLineItem> returnValue = new PageRows<ViewLineItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(viewLineItemAccessor.query(queryExpress, page, conn));
            returnValue.setPage(page);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //query
    public RangeRows<ViewLineItem> queryLineItems(QueryExpress queryExpress, Rangination range) throws DbException {
        RangeRows<ViewLineItem> returnValue = new RangeRows<ViewLineItem>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(viewLineItemAccessor.query(queryExpress, range, conn));
            returnValue.setRange(range);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //update
    public int updateLineItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineItemAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //delete
    public int deleteLineItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineItemAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //###############################VIEWLINE###################################
    //insert
    public ViewLine insertLine(ViewLine entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //get by id
    public ViewLine getLine(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //query
    public List<ViewLine> queryLines(QueryExpress queryExpress) throws DbException {
        List<ViewLine> returnValue = new ArrayList<ViewLine>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = viewLineAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }


    public PageRows<ViewLine> queryLines(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<ViewLine> returnValue = new PageRows<ViewLine>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(viewLineAccessor.query(queryExpress, pagination, conn));
            returnValue.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public RangeRows<ViewLine> queryLines(QueryExpress queryExpress, Rangination range) throws DbException {
        RangeRows<ViewLine> returnValue = new RangeRows<ViewLine>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue.setRows(viewLineAccessor.query(queryExpress, range, conn));
            returnValue.setRange(range);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //update
    public int updateLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //delete
    public int deleteLine(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }


    //###############################VIEWCATEGORY###################################
    //insert
    public ViewCategory insertCategory(ViewCategory entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewCategoryAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //get by code
    public ViewCategory getCategory(QueryExpress getExpress) throws DbException {
        ViewCategory returnValue = null;
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = viewCategoryAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //query
    public List<ViewCategory> queryCategories(QueryExpress queryExpress) throws DbException {
        List<ViewCategory> returnValue = new ArrayList<ViewCategory>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = (viewCategoryAccessor.query(queryExpress, conn));
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //query by paging
    public PageRows<ViewCategory> queryCategories(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<ViewCategory> returnValue = new PageRows<ViewCategory>();
        List<ViewCategory> valueList = new ArrayList<ViewCategory>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            valueList = viewCategoryAccessor.query(queryExpress, pagination, conn);

            returnValue.setRows(valueList);
            returnValue.setPage(pagination);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //update
    public int updateCategory(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewCategoryAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //delete
    public int deleteCategory(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewCategoryAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    //
    public List<Integer> queryCategoryIdsByItem(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineItemAccessor.queryCategoryIds(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //###############################VIEW Category privacy##################################
    //insert
    public ViewCategoryPrivacy insertCategoryPrivacy(ViewCategoryPrivacy entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewCategoryPrivacyAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //get by id
    public ViewCategoryPrivacy getCategoryPrivacy(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewCategoryPrivacyAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //query
    public List<ViewCategoryPrivacy> queryCategoryPrivacies(QueryExpress queryExpress) throws DbException {
        List<ViewCategoryPrivacy> returnValue = new ArrayList<ViewCategoryPrivacy>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = viewCategoryPrivacyAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    //update
    public int updateCategoryPrivacy(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewCategoryPrivacyAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //delete
    public int deleteCategoryPrivacy(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewCategoryPrivacyAccessor.delete(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    //
    public List<Integer> queryCategoryIdsByPrivacy(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewCategoryPrivacyAccessor.queryCategoryIds(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //###############################VIEW LINE SUM DATA##################################
    //insert
    public ViewLineSumData insertViewLineSumData(ViewLineSumData entry) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineSumDataAccessor.insert(entry, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //get by id
    public ViewLineSumData getViewLineSumData(QueryExpress getExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineSumDataAccessor.get(getExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //update
    public int updateViewLineSumData(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineSumDataAccessor.update(updateExpress, queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //query
    public List<ViewLineSumData> queryViewLineSumDatas(QueryExpress queryExpress) throws DbException {
        List<ViewLineSumData> returnValue = new ArrayList<ViewLineSumData>();

        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnValue = viewLineSumDataAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return returnValue;
    }

    public ViewLineLog insertViewLinLog(ViewLineLog viewLineLog) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineLogAccessor.insert(viewLineLog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public ViewLineLog getViewLinLog(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineLogAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<ViewLineLog> queryViewLinLog(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return viewLineLogAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<ViewLineLog> queryViewLinLog(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;

        PageRows<ViewLineLog> reutrnObj = new PageRows<ViewLineLog>();

        try {
            conn = DbConnFactory.factory(dataSourceName);

            reutrnObj.setRows(viewLineLogAccessor.query(queryExpress, pagination, conn));
            reutrnObj.setPage(pagination);

            return reutrnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

}
