package com.enjoyf.platform.db.point;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.point.GiftLottery;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

public interface GiftLotteryAccessor {

    public GiftLottery insert(GiftLottery giftLottery, Connection conn) throws DbException;

    public GiftLottery get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GiftLottery> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GiftLottery> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;
}
