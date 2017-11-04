/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db.timeline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.sequence.SequenceAccessor;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.timeline.TimeLineDomain;
import com.enjoyf.platform.service.timeline.TimeLineFilterType;
import com.enjoyf.platform.service.timeline.TimeLineItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import javax.management.QueryExp;
import java.sql.Connection;
import java.util.List;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
interface TimeLineItemAccessor extends SequenceAccessor {
    //insert
    public TimeLineItem insert(TimeLineItem item, Connection conn) throws DbException;

    //get the time line item
    public TimeLineItem get(String ownUno, TimeLineDomain domain, String directId, Connection conn) throws DbException;

    //query by own uno, domain and page.
    public List<TimeLineItem> query(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException;

    //query by own uno, domain and page.
    public List<TimeLineItem> queryOnlyFocus(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException;

    //query by own uno, domain, before time, size
    public List<TimeLineItem> queryBefore(String ownUno, TimeLineDomain domain, Long before, Integer size, Connection conn) throws DbException;

    //query by own uno, domain, before time, size
    public List<TimeLineItem> queryAfter(String ownUno, TimeLineDomain domain, Long after, Integer size, Connection conn) throws DbException;

    //query by own uno, domain and page.
    public List<TimeLineItem> query(String ownUno, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page, Connection conn) throws DbException;

    //query by own uno, domain and page.
    public List<TimeLineItem> queryRelationID(String ownUno, String relationId, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page, Connection conn) throws DbException;
    
    //query by own uno, domain and page and org
    public List<TimeLineItem> queryOrg(String ownUno, TimeLineDomain domain, Pagination page, Connection conn) throws DbException;

    //update  the time line item's status
    public boolean updateStatus(String ownUno, TimeLineDomain domain, String directId, ActStatus removeStatus, Connection conn) throws DbException;

    //remove the time line item
    public boolean remove(String ownUno, TimeLineDomain domain, String directId, Connection conn) throws DbException;

    public boolean removeByDirectUno(String ownUno, TimeLineDomain domain, String directUno, Connection conn) throws DbException;

    public int update(String ownUno, TimeLineDomain domain, UpdateExpress updateExpress,QueryExpress queryExpress, Connection conn) throws DbException;

    public TimeLineItem getByExpress(String ownUno, TimeLineDomain domain,QueryExpress getExpress, Connection conn)throws DbException;
}


