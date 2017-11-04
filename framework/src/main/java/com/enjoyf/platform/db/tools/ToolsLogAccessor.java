package com.enjoyf.platform.db.tools;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.tools.ToolsLoginLog;
import com.enjoyf.platform.util.Pagination;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Author: taijunli
 * Date: 2012-2-1
 * Time: 18:08:46
 * Desc: 权限log
 */
interface ToolsLogAccessor {

    public ToolsLog insertLog(ToolsLog entity, Connection conn) throws DbException;

    public ToolsLog getLog(long id, Date date, Connection conn) throws DbException;

    public List<ToolsLog> queryLogs(ToolsLog entity, Pagination p, Connection conn) throws DbException;

}
