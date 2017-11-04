package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.point.GoodsItem;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public interface GoodsItemAccessor {

    public int batchInsert(List<GoodsItem> goodsItemList, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, long goodsItemId, Connection conn) throws DbException;

    public List<GoodsItem> queryByGoodsIdExchangeStatus(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public GoodsItem getExchangeByStatus(long goodsId, ActStatus exchangeStatus, Connection conn) throws DbException;

    public GoodsItem get(long goodsItemId, Connection conn) throws DbException;


    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GoodsItem> queryGoodsItem(QueryExpress queryExpress, Connection conn) throws DbException;
}
