package com.enjoyf.webapps.tools.weblogic.log;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.tools.ToolsLoginLog;
import com.enjoyf.platform.service.tools.ToolsServiceSngl;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Author: taijunli
 * Date: 2012-1-18 15:27:48
 * Desc: 登录日志与操作日志
 */
@Service(value = "logWebLogic")
public class LogWebLogic {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    public PageRows<ToolsLog> queryLogs(ToolsLog entity, Pagination p) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ToolsLog:" + entity);
        }

        return ToolsServiceSngl.get().queryLogs(entity, p);
    }

    public PageRows<ToolsLoginLog> queryLoginLogs(ToolsLoginLog entity, Pagination p) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ToolsLoginLog:" + entity);
        }

        return ToolsServiceSngl.get().queryLoginLogs(entity, p);
    }

    public ToolsLog saveLog(ToolsLog entity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("ToolsLog:" + entity);
        }

        return ToolsServiceSngl.get().saveLog(entity);
    }

    public ToolsLog queryLog(long id, Date date) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryLog id:" + id + "date :" + date);
        }

        return ToolsServiceSngl.get().getLog(id, date);
    }

    public ToolsLoginLog queryLoginLog(long id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryLoginLog id:" + id);
        }

        return ToolsServiceSngl.get().getLoginLog(id);
    }

}
