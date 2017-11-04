package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.timeline.SocialTimeLineDomain;
import com.enjoyf.platform.service.timeline.SocialTimeLineItem;
import com.enjoyf.platform.util.NextPagination;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-15
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
public interface SocialTimeLineItemAccessor {
    public SocialTimeLineItem insertSocialTimeLineItem(SocialTimeLineDomain socialTimeLineDomain, String ownUno, SocialTimeLineItem socialTimeLineItem, Connection connection) throws DbException;

    public SocialTimeLineItem getBySId(SocialTimeLineDomain socialTimeLineDomain, String ownUno, long contentId, Connection connection) throws DbException;

    public int update(SocialTimeLineDomain socialTimeLineDomain, String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException;

    public List<SocialTimeLineItem> query(SocialTimeLineDomain socialTimeLineDomain, String ownUno, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialTimeLineItem> queryByPageRows(SocialTimeLineDomain socialTimeLineDomain, String ownUno,QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<SocialTimeLineItem> queryNextRows(SocialTimeLineDomain domain, String uno, NextPagination nextPagination, Connection conn) throws DbException;
}
