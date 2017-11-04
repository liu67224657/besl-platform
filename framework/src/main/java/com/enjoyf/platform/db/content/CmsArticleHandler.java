package com.enjoyf.platform.db.content;

import com.enjoyf.mcms.bean.DedeArchives;
import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.mcms.service.DedeArchivesService;
import com.enjoyf.mcms.service.DedeArctypeService;
import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

import java.sql.Connection;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-2-11 下午4:15
 * Description:
 */
public class CmsArticleHandler {

    private DataBaseType dataBaseType;
    private String dataSourceName;


    private DedeArchivesService archivesService = new DedeArchivesService();
    private DedeArctypeService arctypeService = new DedeArctypeService();

    public CmsArticleHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

    }

    public DedeArctype getDedeArcTypeByArchivesId(int archiveId) {
        Connection conn = null;
        DedeArctype arcType = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            DedeArchives archives = archivesService.queryDedeArchivesbyId(conn, archiveId);
            if (archives == null) {
                return arcType;
            }

            arcType = arctypeService.queryDedeArctype(conn, archives.getTypeid());

            return arcType;
        } catch (Exception e) {
            GAlerter.lab("On getDedeArcTypeByArchivesId, a SQLException occured.", e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return arcType;
    }
}
