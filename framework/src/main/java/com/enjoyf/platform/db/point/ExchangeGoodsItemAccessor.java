package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.point.ExchangeGoods;
import com.enjoyf.platform.service.point.ExchangeGoodsItem;
import com.enjoyf.platform.service.point.Goods;
import com.enjoyf.platform.service.point.GoodsItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-20 下午1:52
 * Description:
 */
public interface ExchangeGoodsItemAccessor {

    public int batchInsert(List<ExchangeGoodsItem> goodsItemList, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ExchangeGoodsItem> queryByGoodsIdExchangeStatus(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<ExchangeGoodsItem> queryExchangeGoodsItem(QueryExpress queryExpress, Connection conn) throws DbException;

    public ExchangeGoodsItem getExchangeByStatus(long goodsId, ActStatus exchangeStatus, Connection conn) throws DbException;

    public ExchangeGoodsItem get(long goodsItemId, Connection conn) throws DbException;
}
