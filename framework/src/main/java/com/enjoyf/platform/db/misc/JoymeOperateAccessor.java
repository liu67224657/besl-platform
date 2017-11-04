package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.JoymeOperate;
import com.enjoyf.platform.service.misc.JoymeOperateType;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 下午5:23
 * Description:
 */
public interface JoymeOperateAccessor {

    public JoymeOperate insert(JoymeOperate joymeOperate, Connection conn) throws DbException;

    public List<JoymeOperate> queryUndoOperate(long operateId, JoymeOperateType operateType, Connection conn) throws DbException;

    public List<JoymeOperate> query(QueryExpress queryExpress, Connection conn) throws DbException;
}
