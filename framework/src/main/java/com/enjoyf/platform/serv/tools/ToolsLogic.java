package com.enjoyf.platform.serv.tools;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.tools.ToolsHandler;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.QueueList;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:21
 * Desc:
 */
public class ToolsLogic implements ToolsService {

    //
    private static final Logger logger = LoggerFactory.getLogger(ToolsLogic.class);

    //the handler's
    private ToolsHandler writeAbleToolsHandler;
    private HandlerPool<ToolsHandler> readonlyToolsHandlersPool;

    ToolsLogic(ToolsConfig cfg) {

        //initialize the handler.
        try {
            writeAbleToolsHandler = new ToolsHandler(cfg.getWriteableDataSourceName(), cfg.getProps());
            readonlyToolsHandlersPool = new HandlerPool<ToolsHandler>();
            for (String dsn : cfg.getReadonlyDataSourceNames()) {
                readonlyToolsHandlersPool.add(new ToolsHandler(dsn, cfg.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //the queue thread n initialize.
        QueueThreadN eventProcessQueueThreadN;
        eventProcessQueueThreadN = new QueueThreadN(cfg.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new QueueList());

    }

    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processQueuedEvent:" + event);
        }
    }


    @Override
    public AuditUser createAuditUser(AuditUser auditUser) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler to createAuditUser, auditUser is " + auditUser);
        }

        //write the audituser into the db.
        return writeAbleToolsHandler.insertAuditUser(auditUser);
    }

    @Override
    public AuditContent createAuditContent(AuditContent auditContent) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler to createAuditContent, auditContent is " + auditContent);
        }
        //write the auditContent into the db.
        return writeAbleToolsHandler.insertAuditContent(auditContent);
    }

    @Override
    public List<AuditUser> queryAuditUserByUnos(List<AuditUserQueryEntity> auditUserQueryEntityList) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryAuditUserByUnos, auditUserQueryEntityList:" + auditUserQueryEntityList);
        }

        List<AuditUser> returnValue = new ArrayList<AuditUser>();

        for (AuditUserQueryEntity entry : auditUserQueryEntityList) {
            returnValue.add(readonlyToolsHandlersPool.getHandler().findAuditUserByUno(entry.getUno()));
        }

        return returnValue;
    }


    @Override
    public List<AuditContent> queryAuditContentByIds(List<AuditContentQueryEntity> auditContentQueryEntityList) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryAuditContentByIds, auditContentQueryEntityList:" + auditContentQueryEntityList);
        }

        List<AuditContent> returnValue = new ArrayList<AuditContent>();

        for (AuditContentQueryEntity entity : auditContentQueryEntityList) {
            returnValue.add(readonlyToolsHandlersPool.getHandler().findAuditContentByCIdCt(entity.getAuditContentId(), entity.getContentType(), entity.getUno()));
        }

        return returnValue;
    }

    // --- privilege -----

    //-- user ---
    @Override
    public PrivilegeUser getUserByLoginName(String loginName) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool getUserByLoginName loginName is " + loginName);
        }

        return readonlyToolsHandlersPool.getHandler().getUserByLoginName(loginName);
    }

    @Override
    public PrivilegeRole getRoleByRoleName(String rolesName) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool getRoleByRoleName rolesName is " + rolesName);
        }

        return readonlyToolsHandlersPool.getHandler().getRoleByRoleName(rolesName);
    }


    public PrivilegeRole getRoleByRid(Integer rid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool getRoleByRoleId, rid is" + rid);
        }

        return readonlyToolsHandlersPool.getHandler().getRoleByRoleId(rid);
    }

    @Override
    public boolean insertRoleMenu(Integer rid, String[] menuids) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler to insertRoleMenu,rid is " + rid + ",menuids is " + Arrays.toString(menuids));
        }

        return writeAbleToolsHandler.insertRoleMenu(rid, menuids);
    }

    @Override
    public List<PrivilegeRole> queryRoles() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryRoles");
        }

        return readonlyToolsHandlersPool.getHandler().queryRoles();
    }

    @Override
    public boolean insertUserRole(Integer uno, String[] rolseids) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler to insertUserRole,uno is " + uno + ",String[] is " + Arrays.toString(rolseids));
        }

        return writeAbleToolsHandler.insertUserRole(uno, rolseids);
    }

    @Override
    public boolean deleteUserRole(Integer uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler to deleteUserRole,uno is " + uno);
        }

        return writeAbleToolsHandler.deleteUserRole(uno);
    }

    @Override
    public List<PrivilegeResource> queryResMenu(String rstype, String status) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool queryResMenu rstype=" + rstype + ",status =" + status);
        }

        return readonlyToolsHandlersPool.getHandler().queryResMenu(rstype, status);
    }

    @Override
    public PrivilegeResource getResByRsurl(String rsurl) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool getResByRsurl rsurl is " + rsurl);
        }

        return readonlyToolsHandlersPool.getHandler().getResByRsurl(rsurl);
    }

    @Override
    public PrivilegeUser getByUno(Integer uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool getByUno, uno is " + uno);
        }

        return readonlyToolsHandlersPool.getHandler().getByUno(uno);
    }

    @Override
    public PageRows<PrivilegeUser> queryUserByParam(PrivilegeUser param, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryUserByParam by view, page:" + page + "   PrivilegeUser:" + param);
        }
        PageRows<PrivilegeUser> returnValue = new PageRows<PrivilegeUser>();
        List<PrivilegeUser> list = readonlyToolsHandlersPool.getHandler().queryUserByParam(param, page);

        returnValue.setPage(page);
        returnValue.setRows(list);

        return returnValue;
    }

    @Override
    public PageRows<PrivilegeRole> queryRoleByParam(PrivilegeRole param, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryRoleByParam by view, page:" + page + "   PrivilegeRole:" + param);
        }
        PageRows<PrivilegeRole> returnValue = new PageRows<PrivilegeRole>();
        List<PrivilegeRole> list = readonlyToolsHandlersPool.getHandler().queryRoleByParam(param, page);

        returnValue.setPage(page);
        returnValue.setRows(list);

        return returnValue;
    }

    @Override
    public PageRows<PrivilegeResource> queryResByParam(PrivilegeResource param, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryRsByParam by view, page:" + page + "   PrivilegeResource:" + param);
        }
        PageRows<PrivilegeResource> returnValue = new PageRows<PrivilegeResource>();
        List<PrivilegeResource> list = readonlyToolsHandlersPool.getHandler().queryResByParam(param, page);

        returnValue.setPage(page);
        returnValue.setRows(list);

        return returnValue;
    }

    @Override
    public PrivilegeUser getUserByUserIdPwd(String userId, String pwd) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool getUserByUserIdPwd userId is " + userId + " ,pwd:" + pwd);
        }

        return readonlyToolsHandlersPool.getHandler().getUserByUserIdPwd(userId, pwd);
    }

    @Override
    public Set<PrivilegeResource> queryResByRoleId(PrivilegeRole entity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool queryResByRoleId PrivilegeRole is " + entity);
        }

        return readonlyToolsHandlersPool.getHandler().queryResByRoleId(entity);
    }

    @Override
    public boolean modifyUser(PrivilegeUser entity, Map<ObjectField, Object> map) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler modifyUser, PrivilegeUser is " + entity);
        }

        return writeAbleToolsHandler.updateUser(entity, map);
    }

    @Override
    public boolean modifyPwd(String pwd, Integer uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler modifyPwd, password is " + pwd + "uno is" + uno);
        }

        return writeAbleToolsHandler.updatePwd(pwd, uno);
    }

    @Override
    public boolean deleteRole(Integer rid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the deleteRole , rid is " + rid);
        }

        return writeAbleToolsHandler.deleteRole(rid);
    }

    @Override
    public boolean deleteUser(Integer uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the deleteUser , uno is " + uno);
        }

        return writeAbleToolsHandler.deleteUser(uno);
    }

    @Override
    public boolean deleteRes(String status, Integer rsid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the deleteRes , status is " + status + "rsid is " + rsid);
        }

        return writeAbleToolsHandler.deleteRes(status, rsid);
    }

    @Override
    public boolean modifyRole(PrivilegeRole entity, Map<ObjectField, Object> map) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler modifyRole, PrivilegeRole is " + entity);
        }

        return writeAbleToolsHandler.updateRole(entity, map);
    }

    @Override
    public boolean modifyRes(PrivilegeResource entity, Map<ObjectField, Object> map) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler modifyRes, PrivilegeResource is " + entity);
        }

        return writeAbleToolsHandler.updateRes(entity, map);
    }

    @Override
    public boolean switchUser(String ustatus, Integer uno) throws ServiceException {

        if (logger.isDebugEnabled()) {
            logger.debug("Call the switchUser , PrivilegeUser is ustatus:" + ustatus + " ,uno :" + uno);
        }

        return writeAbleToolsHandler.switchUser(ustatus, uno);
    }


    @Override
    public boolean saveUser(PrivilegeUser entity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler saveUser, PrivilegeUser is " + entity);
        }

        return writeAbleToolsHandler.saveUser(entity);
    }

    @Override
    public boolean saveRole(PrivilegeRole entity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler saveRole, PrivilegeRole is " + entity);
        }

        return writeAbleToolsHandler.saveRole(entity);
    }

    @Override
    public boolean saveRes(PrivilegeResource entity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler saveRes, PrivilegeResource is " + entity);
        }

        return writeAbleToolsHandler.saveRes(entity);
    }

    // res
    public PrivilegeResource findResById(Integer id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool findResById id is " + id);
        }

        return readonlyToolsHandlersPool.getHandler().findResById(id);
    }

    //
    public List<PrivilegeResource> queryAllResByStatus(ActStatus status) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool queryAllResByStatus status is " + status);
        }

        return readonlyToolsHandlersPool.getHandler().queryAllResByStatus(status);
    }

    //
    public ToolsLog saveLog(ToolsLog entity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler saveLog entity is " + entity);
        }

        return writeAbleToolsHandler.insertLog(entity);
    }

    public ToolsLog getLog(long id, Date date) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler queryLog id is " + id + " date is" + date);
        }

        return writeAbleToolsHandler.getLog(id, date);
    }

    public ToolsLoginLog getLoginLog(long id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler queryLog id is " + id);
        }
        return writeAbleToolsHandler.getLoginLog(id);
    }

    @Override
    public PageRows<ToolsLog> queryLogs(ToolsLog param, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryLogs by view, page:" + page + "   ToolsLog:" + param);
        }
        PageRows<ToolsLog> returnValue = new PageRows<ToolsLog>();
        List<ToolsLog> list = readonlyToolsHandlersPool.getHandler().queryLogs(param, page);

        returnValue.setPage(page);
        returnValue.setRows(list);

        return returnValue;
    }

    public ToolsLoginLog saveLoginLog(ToolsLoginLog entity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler saveLoginLog entity is " + entity);
        }

        return writeAbleToolsHandler.insertLoginLog(entity);
    }

    @Override
    public PageRows<ToolsLoginLog> queryLoginLogs(ToolsLoginLog entity, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryLoginLogs by view, page:" + page + "   ToolsLoginLog:" + entity);
        }
        PageRows<ToolsLoginLog> returnValue = new PageRows<ToolsLoginLog>();

        List<ToolsLoginLog> list = readonlyToolsHandlersPool.getHandler().queryLoginLogs(entity, page);

        returnValue.setPage(page);
        returnValue.setRows(list);

        return returnValue;
    }

    public PrivilegeResource getResourceByRsid(int rsid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to getResouce by rsid , rsid=" + rsid);
        }
        return readonlyToolsHandlersPool.getHandler().getResourceByRsid(rsid);
    }

    //stats editor
    @Override
    public StatsEditor createStatsEditor(StatsEditor statsEditor) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler to createStatsEditor , statsEditor: " + statsEditor);
        }
        return writeAbleToolsHandler.insertStatsEditor(statsEditor);
    }

    @Override
    public StatsEditor getStatsEditor(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to getStatsEditor , getExpress: " + getExpress);
        }
        return readonlyToolsHandlersPool.getHandler().getStatsEditor(getExpress);
    }

    @Override
    public boolean modifyStatsEditor(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to modifyStatsEditor , updateExpress: " + updateExpress + " , queryExpress: " + queryExpress);
        }
        return writeAbleToolsHandler.updateStatsEditor(updateExpress, queryExpress) > 0;
    }

    @Override
    public PageRows<StatsEditor> queryStatsEditorByPage(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to modifyStatsEditor , queryExpress: " + queryExpress);
        }
        return readonlyToolsHandlersPool.getHandler().queryStatsEditorsByPage(queryExpress, page);
    }

    @Override
    public List<StatsEditor> queryStatsEditor(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to modifyStatsEditor , queryExpress: " + queryExpress);
        }
        return readonlyToolsHandlersPool.getHandler().queryStatsEditors(queryExpress);
    }

    @Override
    public StatsEditorItem createStatsEditorItem(StatsEditorItem editorItem) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleToolsHandler to createStatsEditorItem , editorItem: " + editorItem);
        }
        return writeAbleToolsHandler.insertStatsEditorItem(editorItem);
    }

    @Override
    public StatsEditorItem getStatsEditorItem(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to getStatsEditorItem , getExpress: " + getExpress);
        }
        return readonlyToolsHandlersPool.getHandler().getStatsEditorItem(getExpress);
    }

    @Override
    public boolean modifyStatsEditorItem(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to modifygetStatsEditorItem , updateExpress: " + updateExpress + " , queryExpress: " + queryExpress);
        }
        return writeAbleToolsHandler.updateStatsEditorItem(updateExpress, queryExpress) > 0;
    }

    @Override
    public PageRows<StatsEditorItem> queryStatsEditorItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryStatsEditorItem , queryExpress: " + queryExpress);
        }
        return readonlyToolsHandlersPool.getHandler().queryStatsEditorItemsByPage(queryExpress, pagination);
    }

    @Override
    public List<StatsEditorItem> queryStatsEditorItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyToolsHandlersPool to queryStatsEditorItem , queryExpress: " + queryExpress);
        }
        return readonlyToolsHandlersPool.getHandler().queryStatsEditorItems(queryExpress);
    }
}
