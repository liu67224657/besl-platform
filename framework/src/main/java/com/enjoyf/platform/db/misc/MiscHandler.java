/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.TableAccessorFactory;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.misc.*;
import com.enjoyf.platform.service.misc.RefreshCMSTiming;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-16 下午10:56
 * Description:
 */
public class MiscHandler {
    //
    private DataBaseType dataBaseType;
    private String dataSourceName;

    //
    private RegionAccessor regionAccessor;
    private RegCodeAccessor regCodeAccessor;
    private RegCodeApplyAccessor regCodeApplyAccessor;
    private FeedbackAccessor feedbackAccessor;
    private IpForbiddenAccessor ipForbiddenAccessor;

    private GamePropDbAccessor gamePropDbAccessor;

    private JoymeOperateAccessor joymeOperateAccessor;
    private JoymeOperateLogAccessor joymeOperateLogAccessor;
    private IndexCacheAccessor indexCacheAccessor;

    private InterFlowAccountAccessor interFlowAccountAccessor;
    private RefreshCMSTimingReleaseAccessor refreshCMSTimingReleaseAccessor;

    public MiscHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);
        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

        //
        regionAccessor = TableAccessorFactory.get().factoryAccessor(RegionAccessor.class, dataBaseType);
        regCodeAccessor = TableAccessorFactory.get().factoryAccessor(RegCodeAccessor.class, dataBaseType);
        regCodeApplyAccessor = TableAccessorFactory.get().factoryAccessor(RegCodeApplyAccessor.class, dataBaseType);
        feedbackAccessor = TableAccessorFactory.get().factoryAccessor(FeedbackAccessor.class, dataBaseType);
        ipForbiddenAccessor = TableAccessorFactory.get().factoryAccessor(IpForbiddenAccessor.class, dataBaseType);

        gamePropDbAccessor = TableAccessorFactory.get().factoryAccessor(GamePropDbAccessor.class, dataBaseType);

        //
        joymeOperateAccessor = TableAccessorFactory.get().factoryAccessor(JoymeOperateAccessor.class, dataBaseType);
        joymeOperateLogAccessor = TableAccessorFactory.get().factoryAccessor(JoymeOperateLogAccessor.class, dataBaseType);
        indexCacheAccessor = TableAccessorFactory.get().factoryAccessor(IndexCacheAccessor.class, dataBaseType);

        interFlowAccountAccessor = TableAccessorFactory.get().factoryAccessor(InterFlowAccountAccessor.class, dataBaseType);

        refreshCMSTimingReleaseAccessor = TableAccessorFactory.get().factoryAccessor(RefreshCMSTimingReleaseAccessor.class, dataBaseType);
    }

    public List<Region> getAllRegion() throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return regionAccessor.getAllRegion(conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public RegCode getRegCode(String regCode) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return regCodeAccessor.get(regCode, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean updateApplyInfo(String regCode, String applyEmail, String applyReason) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return regCodeAccessor.updateApplyInfo(regCode, applyEmail, applyReason, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean useRegCode(String regCode, String useUno, String useUserid) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return regCodeAccessor.updateUseInfo(regCode, useUno, useUserid, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //reg code apply

    public RegCodeApply applyRegCode(RegCodeApply apply) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            RegCodeApply existApply = regCodeApplyAccessor.getByEmail(apply.getUserEmail(), conn);
            if (existApply == null) {
                apply = regCodeApplyAccessor.insert(apply, conn);
            } else {
                regCodeApplyAccessor.update(apply.getUserEmail(), apply, conn);
            }
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return apply;
    }


    public RegCodeApply getRegCodeApply(String email) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return regCodeApplyAccessor.getByEmail(email, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public boolean appendApplyRegCode(String email, String regCode) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return regCodeApplyAccessor.updateRegCode(email, regCode, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public Feedback postFeedback(Feedback feedback) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return feedbackAccessor.insert(feedback, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<IpForbidden> queryIpForbiddens(QueryExpress queryExpress, Pagination pagination) throws DbException {
        PageRows<IpForbidden> pageRows = new PageRows<IpForbidden>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            pageRows.setRows(ipForbiddenAccessor.query(queryExpress, pagination, conn));
            pageRows.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

        return pageRows;
    }

    public IpForbidden createIpForbidden(IpForbidden entry) throws DbException {

        Connection conn = null;

        try {

            conn = DbConnFactory.factory(dataSourceName);

            return ipForbiddenAccessor.insert(entry, conn);

        } finally {

            DataBaseUtil.closeConnection(conn);
        }

    }


    public IpForbidden getIpForbidden(QueryExpress queryExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return ipForbiddenAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<IpForbidden> queryIpForbiddens(QueryExpress queryExpress) throws DbException {
        List<IpForbidden> returnList = new ArrayList<IpForbidden>();
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return ipForbiddenAccessor.query(queryExpress, conn);

        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public int updateIpForbidden(QueryExpress queryExpress, UpdateExpress updateExpress) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return ipForbiddenAccessor.update(queryExpress, updateExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    /////////////////////////////////////////
    public long getGamePropDbSeqNo() throws DbException {
        Connection conn = null;

        PageRows<GamePropDb> returnObj = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gamePropDbAccessor.getSeqNo(conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    private int i = 0;

    public boolean batchInsertGamePropDb(String gamePropCode, List<GamePropDb> gamePropDbList) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            conn.setAutoCommit(false);

            gamePropDbAccessor.insertBatch(gamePropCode, gamePropDbList, conn);

            conn.commit();
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On batch insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return true;
    }

    public GamePropDb insertGamePropDb(String gamePropCode, GamePropDb gamePropDb) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            GamePropDb db = gamePropDbAccessor.insert(gamePropDb, gamePropCode, conn);
            return db;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<GamePropDb> queryGamePropDb(QueryExpress queryExpress, String gamePropCode, Pagination pagination) throws DbException {
        Connection conn = null;

        PageRows<GamePropDb> returnObj = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            returnObj = new PageRows<GamePropDb>();
            returnObj.setRows(gamePropDbAccessor.query(queryExpress, pagination, gamePropCode, conn));
            returnObj.setPage(pagination);

            return returnObj;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public GamePropDb getGamePropDb(QueryExpress getExpress, String gamePropCode) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gamePropDbAccessor.get(getExpress, gamePropCode, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GamePropDb> queryGamePropDb(QueryExpress queryExpress, String gamePropCode) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gamePropDbAccessor.query(queryExpress, gamePropCode, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<GamePropDb> queryGamePropKeyId(List<GamePropDbQueryParam> paramObjectList, String gamePropCode) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);

            return gamePropDbAccessor.getByParamList(paramObjectList, gamePropCode, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<JoymeOperate> getUndoOperate(String serviceid, JoymeOperateType operateType) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            long maxId = joymeOperateLogAccessor.getMaxJoymeOperateId(serviceid, operateType, conn);

            return joymeOperateAccessor.queryUndoOperate(maxId, operateType, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public JoymeOperateLog insertJoymeOperateLog(JoymeOperateLog joymeOperateLog) throws DbException {
        Connection conn = null;
        List<JoymeOperate> returnList = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return joymeOperateLogAccessor.insert(joymeOperateLog, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public JoymeOperate insertJoymeOperate(JoymeOperate joymeOperate) throws DbException {
        Connection conn = null;
        List<JoymeOperate> returnList = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return joymeOperateAccessor.insert(joymeOperate, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public IndexCache insertIndexCache(IndexCache indexCache) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);

            return indexCacheAccessor.insert(indexCache, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean updateIndexCache(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return indexCacheAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<IndexCache> queryIndexCache(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return indexCacheAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public PageRows<IndexCache> queryIndexCache(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<IndexCache> page = new PageRows<IndexCache>();

        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<IndexCache> list = indexCacheAccessor.query(queryExpress, pagination, conn);
            page.setRows(list);
            page.setPage(pagination);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return page;
    }

    public IndexCache getIndexCache(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return indexCacheAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }


    public long getMaxIndexCacheId(IndexCacheType type) throws DbException {
        Connection conn = null;

        try {
            conn = DbConnFactory.factory(dataSourceName);
            return indexCacheAccessor.getMaxId(conn, type);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public InterFlowAccount createInterFlowAccount(InterFlowAccount interflowAccount) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return interFlowAccountAccessor.insert(interflowAccount, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<InterFlowAccount> queryInterFlowAccount(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return interFlowAccountAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public PageRows<InterFlowAccount> queryInterFlowAccountByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<InterFlowAccount> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<InterFlowAccount> list = interFlowAccountAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<InterFlowAccount>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyInterFlowAccount(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return interFlowAccountAccessor.update(updateExpress, queryExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public InterFlowAccount getInterFlowAccount(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return interFlowAccountAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    //////////////////////refreshcmstiming
    public RefreshCMSTiming createRefreshCMSTiming(RefreshCMSTiming refreshCMSTiming) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return refreshCMSTimingReleaseAccessor.insert(refreshCMSTiming, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public List<RefreshCMSTiming> queryRefreshCMSTiming(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return refreshCMSTimingReleaseAccessor.query(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }

    }

    public PageRows<RefreshCMSTiming> queryRefreshCMSTimingByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        Connection conn = null;
        PageRows<RefreshCMSTiming> pageRows = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            List<RefreshCMSTiming> list = refreshCMSTimingReleaseAccessor.query(queryExpress, pagination, conn);
            pageRows = new PageRows<RefreshCMSTiming>();
            pageRows.setRows(list);
            pageRows.setPage(pagination);
            return pageRows;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public boolean modifyRefreshCMSTiming(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return refreshCMSTimingReleaseAccessor.update(queryExpress, updateExpress, conn) > 0;
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }

    public RefreshCMSTiming getRefreshCMSTiming(QueryExpress queryExpress) throws DbException {
        Connection conn = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            return refreshCMSTimingReleaseAccessor.get(queryExpress, conn);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
    }
}

