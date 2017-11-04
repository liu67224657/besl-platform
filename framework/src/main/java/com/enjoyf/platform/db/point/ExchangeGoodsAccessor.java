package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.ExchangeGoods;
import com.enjoyf.platform.service.point.Goods;
import com.enjoyf.platform.service.point.GoodsField;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-20 下午1:52
 * Description:
 */
public interface ExchangeGoodsAccessor {

    public ExchangeGoods insert(ExchangeGoods goods, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<ExchangeGoods> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public List<ExchangeGoods> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public ExchangeGoods get(long goodsId, Connection conn) throws DbException;
}
