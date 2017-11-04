package com.enjoyf.platform.db.social;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.social.SocialBlack;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-29
 * Time: 下午3:48
 * To change this template use File | Settings | File Templates.
 */
public interface SocialBlackAccessor {
    
    public SocialBlack insert(SocialBlack socialBlack, Connection conn) throws DbException;

    //get
    public SocialBlack getSocialBlack(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException;

    //update
    public int updateSocialBlack(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

    public List<SocialBlack> querySocialBlackList(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;

    public List<SocialBlack> queryAllSocialBlackList(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException;
}
