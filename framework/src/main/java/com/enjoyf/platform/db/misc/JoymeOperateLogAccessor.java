package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.JoymeOperateLog;
import com.enjoyf.platform.service.misc.JoymeOperateType;

import java.sql.Connection;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 下午5:23
 * Description:
 */
public interface JoymeOperateLogAccessor {

    public JoymeOperateLog insert(JoymeOperateLog log, Connection conn) throws DbException;

    public long getMaxJoymeOperateId(String serviceId, JoymeOperateType operateType, Connection conn) throws DbException;


}
