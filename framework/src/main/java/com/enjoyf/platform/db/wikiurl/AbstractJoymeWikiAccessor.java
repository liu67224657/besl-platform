package com.enjoyf.platform.db.wikiurl;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.JoymeWiki;
import com.enjoyf.platform.service.joymeapp.JoymeWikiContextPath;
import com.enjoyf.platform.service.joymeapp.JoymeWikiParam;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zhitaoshi on 2015/4/14.
 */
public abstract class AbstractJoymeWikiAccessor extends AbstractBaseTableAccessor<JoymeWiki> implements JoymeWikiAccessor{

    private static final String TABLE_NAME = "joyme_wiki";

    @Override
    public JoymeWiki insert(JoymeWiki wiki, Connection conn) throws DbException {
        return null;
    }

    @Override
    protected JoymeWiki rsToObject(ResultSet rs) throws SQLException {
        JoymeWiki joymeWiki = new JoymeWiki();
        joymeWiki.setWikiId(rs.getInt("joyme_wiki_id"));
        joymeWiki.setWikiDomain(rs.getString("joyme_wiki_domain"));
        joymeWiki.setContextPath(JoymeWikiContextPath.getByCode(rs.getString("context_path")));
        joymeWiki.setParam(JoymeWikiParam.parse(rs.getString("exp_param")));
        joymeWiki.setSupportSubDomain(rs.getInt("support_subdomain"));
        joymeWiki.setWikiKey(rs.getString("joyme_wiki_key"));
        joymeWiki.setWikiName(rs.getString("joyme_wiki_name"));
        joymeWiki.setWikiPath(rs.getString("joyme_wiki_path"));
        return joymeWiki;
    }


    @Override
    public JoymeWiki get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<JoymeWiki> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<JoymeWiki> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, conn);
    }

}
