package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.point.Goods;
import com.enjoyf.platform.service.point.GoodsType;
import com.enjoyf.platform.service.point.PointActionHistory;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public interface GoodsAccessor {

    public Goods insert(Goods goods, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, long goodsId, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<Goods> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<Goods> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public Goods get(long goodsId, Connection conn) throws DbException;

}
