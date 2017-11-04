package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.tools.ToolsLoginLog;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:58
 * Desc: 权限log
 */
interface ToolsLoginLogAccessor {

    public ToolsLoginLog getLoginLog(long id, Connection conn) throws DbException;

    public ToolsLoginLog insertLoginLog(ToolsLoginLog entity, Connection conn) throws DbException;

    public List<ToolsLoginLog> queryLoginLogs(ToolsLoginLog entity, Pagination p, Connection conn) throws DbException;


}
