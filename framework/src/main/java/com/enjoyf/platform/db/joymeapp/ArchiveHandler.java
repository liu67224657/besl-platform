package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.mcms.bean.DedeAddonarticle;
import com.enjoyf.mcms.bean.DedeArchives;
import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.mcms.bean.DedeTaglist;
import com.enjoyf.mcms.service.*;
import com.enjoyf.platform.db.DataBaseType;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DataSourceProps;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.joymeapp.Archive;
import com.enjoyf.platform.service.joymeapp.ArchiveTag;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.util.StringUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-10 上午10:07
 * Description:
 */
public class ArchiveHandler {
    private String urlDomain = "http://article.joyme.com";
    private String dev_urlDomain = "http://article.enjoyf.com";

    private DataBaseType dataBaseType;
    private String dataSourceName;


    private DedeArchivesService archivesService = new DedeArchivesService();
    private DedeTaglistService taglistService = new DedeTaglistService();

    private static DedeChanneltypeService dedeChanneltypeService = new DedeChanneltypeService();
    private static DedeAddonarticleService dedeAddonarticleService = new DedeAddonarticleService();
    private static DedeArctypeService dedeArctypeService = new DedeArctypeService();

    public ArchiveHandler(String dsn, FiveProps props) throws DbException {
        dataSourceName = dsn.toLowerCase();

        //create the catasource
        DataSourceManager.get().append(dataSourceName, props);

        dataBaseType = DataSourceProps.getDataSourceProps(dataSourceName).getDataBaseType();

    }

    public Archive getArchive(String archiveId) throws Exception {
        Connection conn = null;
        Archive archive = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            DedeArchives archives = archivesService.queryDedeArchivesbyId(conn, Integer.parseInt(archiveId));
            //如果文章不存在或者文章没开放浏览或者文章没生成静态页面返回null
            if (archives == null ) {
                //if (archives == null) {
                return null;
            }
            //get tag
            List<DedeTaglist> tagList = null;
            if (archives != null) {
                tagList = taglistService.queryDedeTagByAid(conn, Integer.valueOf(archiveId));
            }
            List<ArchiveTag> archiveTagList = new ArrayList<ArchiveTag>();
            if (!CollectionUtil.isEmpty(tagList)) {
                for (DedeTaglist dedeTag : tagList) {
                    archiveTagList.add(new ArchiveTag(dedeTag.getTid(), dedeTag.getTag()));
                }
            }

            archive = new Archive();
            archive.setTypeid(archives.getTypeid());
            archive.setArchiveId(Integer.parseInt(archiveId));
            archive.setArchiveTagList(archiveTagList);
            long createTimeMills = (long) archives.getSenddate();
            archive.setCreateTime(new Date(createTimeMills * 1000L));
            archive.setTitle(archives.getTitle());
            archive.setDesc(archives.getDescription());
            archive.setTypeName(archives.getTypeName());
            archive.setTypeColor(archives.getTypeColor());
            //if(Constant.DOMAIN)
            if (archives.getLitpic().startsWith("http://")) {
                archive.setIcon(archives.getLitpic());
            } else {
                archive.setIcon("http://article.joyme.com" + archives.getLitpic());
            }

            archive.setAuthor(archives.getWriter());
            archive.setShowios(archives.getShowios());
            archive.setShowandroid(archives.getShowandroid());

            String tableName = dedeChanneltypeService.queryDedeChanneltypebyId(conn, archives.getChannel());
            DedeAddonarticle dedeAddonarticle = dedeAddonarticleService.queryDedeAddonarticlebyId(conn, tableName, archives.getId());
            //webview跳转
            if (archives.getFlag() != null) {
                String flag = StringUtil.isEmpty(archives.getFlag().toString()) ? "" : archives.getFlag().toString();
                if (flag.contains("j")) {
                    // 查到内容在哪个table中
                    archive.setWebviewUrl(dedeAddonarticle.getRedirecturl());
                }
            }
            if (!StringUtil.isEmpty(dedeAddonarticle.getHtlistimg())) {
                if (dedeAddonarticle.getHtlistimg().startsWith("http://")) {
                    archive.setHtlistimg(dedeAddonarticle.getHtlistimg());
                } else {
                    archive.setHtlistimg("http://article.joyme.com" + dedeAddonarticle.getHtlistimg());
                }
            }

            archive.setArcrank(archives.getArcrank());
            archive.setIsMake(archives.getIsmake());

        } catch (Exception e) {
            GAlerter.lab("On insert AppInstallInfo, a SQLException occured.", e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return archive;
    }

    public boolean modifyDedeArchivePubdateById(DedeArchives dedeArchives) throws Exception {
        Connection conn = null;
        boolean ok = false;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            ok = archivesService.updateDedearchivesPubdate(conn, dedeArchives) > 0;
            GAlerter.lab("ArchiveHandler modifyDedeArchivePubdateById ok=" + ok + ",conn=" + conn + ",id=" + dedeArchives.getId() + ",pubdate=" + dedeArchives.getPubdate());
        } catch (Exception e) {
            GAlerter.lab("On update modifyDedeArchivePubdateById, a SQLException occured.", e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return ok;
    }

    public DedeArctype getqueryDedeArctype(Integer archiveTypeId) {
        Connection conn = null;
        DedeArctype dedeArctype = null;
        try {
            conn = DbConnFactory.factory(dataSourceName);
            dedeArctype = dedeArctypeService.queryDedeArctype(conn, archiveTypeId);
            if (dedeArctype == null) {
                return null;
            }
        } catch (Exception e) {
            GAlerter.lab("On update getqueryDedeArctype, a SQLException occured.", e);
        } finally {
            DataBaseUtil.closeConnection(conn);
        }
        return dedeArctype;
    }
}
