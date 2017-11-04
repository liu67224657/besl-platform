package com.enjoyf.platform.db.message;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.message.SocialMessage;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午3:51
 * To change this template use File | Settings | File Templates.
 */
public interface SocialMessageAccessor {
     //insert
    public SocialMessage insert(SocialMessage entry, Connection conn) throws DbException;

    //get by id
    public SocialMessage getSocialMessage(String ownUno, QueryExpress queryExpress, Connection conn) throws DbException;

     //update
    public int updateSocialMessage(String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;

     public List<SocialMessage> querySocialMessageList(String ownUno, QueryExpress queryExpress, Pagination page, Connection conn) throws DbException;
}
