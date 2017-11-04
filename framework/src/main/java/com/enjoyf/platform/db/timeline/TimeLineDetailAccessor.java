package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.timeline.TimeLineItemDetail;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public interface TimeLineDetailAccessor {

    public TimeLineItemDetail insert(TimeLineItemDetail itemDetail, Connection conn) throws DbException;

    public List<TimeLineItemDetail> queryBySize(String ownUno, QueryExpress queryExpress, Pagination p, Connection conn) throws DbException;

    public int updateDetail(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int deleteDetail(String ownUno,QueryExpress queryExpress, Connection conn) throws DbException;
}
