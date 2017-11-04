package com.enjoyf.platform.db.viewline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.viewline.ViewLineLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;

import java.sql.Connection;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public interface ViewLineLogAccessor {
    public ViewLineLog insert(ViewLineLog log,Connection conn)throws DbException;

    public ViewLineLog get(QueryExpress queryExpress,Connection conn)throws DbException;

    public List<ViewLineLog> query(QueryExpress queryExpress,Connection conn)throws DbException;

    public List<ViewLineLog> query(QueryExpress queryExpress, Pagination pagination, Connection conn)throws DbException;
}
