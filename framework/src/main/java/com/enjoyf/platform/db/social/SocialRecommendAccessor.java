package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.SocialRecommend;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;

/**
 * @Auther: <a mailto="ericLiu@stuff.enjoyfound.com">${User}</a>
 * Create time: 12-8-29
 * Description:
 */
public interface SocialRecommendAccessor {

    public SocialRecommend insert(SocialRecommend recommend, Connection conn) throws DbException;

    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn)throws DbException;

    public SocialRecommend get(QueryExpress queryExpress,Connection conn)throws DbException;
}
