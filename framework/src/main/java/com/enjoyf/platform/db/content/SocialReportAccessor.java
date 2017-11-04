package com.enjoyf.platform.db.content;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.content.social.SocialContentReply;
import com.enjoyf.platform.service.content.social.SocialReport;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import javax.management.QueryExp;
import java.sql.Connection;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-4-14 下午8:46
 * Description:
 */
public interface SocialReportAccessor {

    public SocialReport insert(SocialReport socialReport, Connection conn) throws DbException;

    public List<SocialReport> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<SocialReport> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public SocialReport get(QueryExpress queryExpress, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;
}
