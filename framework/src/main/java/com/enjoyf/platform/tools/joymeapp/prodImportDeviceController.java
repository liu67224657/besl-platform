package com.enjoyf.platform.tools.joymeapp;

import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.comment.CommentHandler;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.db.message.MessageHandler;
import com.enjoyf.platform.db.oauth.OAuthHandler;
import com.enjoyf.platform.db.tools.ToolsHandler;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.serv.oauth.AuthAppCache;
import com.enjoyf.platform.serv.oauth.OAuthConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentBeanField;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.service.comment.CommentServiceSngl;
import com.enjoyf.platform.service.joymeapp.AppSecret;
import com.enjoyf.platform.service.joymeapp.Archive;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigField;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class prodImportDeviceController {
    @Resource(name = "i18nSource")
    private ResourceBundleMessageSource i18nSource;

    private static final Logger logger = LoggerFactory.getLogger(prodImportDeviceController.class);

    private static JoymeAppHandler joymeAppHandler;
    private static CommentHandler commentHandler;
    private static OAuthHandler oAuthHandler;

    private static ToolsHandler toolsHandler;

    private static MessageHandler messageHandler;
    private static RedisManager manager;

    public static void main(String[] args) {

        FiveProps servProps = Props.instance().getServProps();
        manager = new RedisManager(servProps);
        try {
            //UserCenterConfig centerConfig = new UserCenterConfig(servProps);
            joymeAppHandler = new JoymeAppHandler("joymeapp", servProps);
            //commentHandler = new CommentHandler("comment", servProps);
            //  oAuthHandler = new OAuthHandler("oauth", servProps);
            // toolsHandler = new ToolsHandler("TOOLS", servProps);
            // messageHandler = new MessageHandler("message", servProps, manager);
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }
        //updateDede_archives_pubdate();
        //updateCommentBeanStatus();
        //updateToolspassword();
        //  getActivityById();
        //importOauthAppsecrt(servProps);
        //  delete(servProps);
        //  updateDede();
        updateDede();

    }

    public static String getArticleUrl(DedeArctype dedeArctype, long time, int archiveId) {
        String typedir = dedeArctype.getTypedir().replaceAll("\\{cmspath\\}/", "");
        String namerule = dedeArctype.getNamerule();
        // {typedir}/{Y}{M}{D}/{aid}.html
        namerule = namerule.replaceAll("\\{typedir\\}", typedir);
        String date = DateUtil.convert2String(time, DateUtil.YYYYMMDD_FORMAT);
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        namerule = namerule.replaceAll("\\{Y\\}", year);
        namerule = namerule.replaceAll("\\{M\\}", month);
        namerule = namerule.replaceAll("\\{D\\}", day);
        namerule = namerule.replaceAll("\\{aid\\}", archiveId + "");

        int position = namerule.lastIndexOf("/");
        String[] paths = new String[2];
        if (position > 0) {
            paths[0] = namerule.substring(0, position);
            paths[1] = namerule.substring(position + 1, namerule.length());
        }
        String returnURL = "";
        String domain = WebappConfig.get().getDomain();
        if (domain.contains("joyme.dev") || domain.contains("joyme.test")) {
            returnURL = "http://marticle.joyme.dev/syhb4/" + paths[0] + "/" + paths[1];
            ;
        } else {
            returnURL = "http://marticle." + domain + "/syhb4/" + paths[0] + "/" + paths[1];
        }
        return returnURL;
    }

    public static void updateDede() {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_URL, "http://marticle.joyme.beta/syhb4/syhbao/pingguoduan/201501/2267881.html"));

        int i = 0;
        try {
            List<TagDedearchives> list = joymeAppHandler.queryTagDedearchives(queryExpress);
            System.out.println("list size:" + list.size());

            for (TagDedearchives dedearchives : list) {
                // System.out.println(dedearchives.getDede_archives_id() + "" + dedearchives.getDede_archives_url());
                Archive archive = JoymeAppServiceSngl.get().getArchiveById(Integer.valueOf(dedearchives.getDede_archives_id()));
                if (archive != null) {
                    DedeArctype arctype = JoymeAppServiceSngl.get().getqueryDedeArctype(archive.getTypeid());
                    if (arctype != null) {
                        String dedearchiv_url = getArticleUrl(arctype, archive.getCreateTime().getTime(), archive.getArchiveId());
                        System.out.println(archive.getArchiveId() + "=====webviwe url====" + dedearchiv_url);
                        QueryExpress upqu = new QueryExpress();
                        upqu.add(QueryCriterions.eq(TagDedearchivesFiled.ID, dedearchives.getId()));


                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(TagDedearchivesFiled.DEDE_ARCHIVES_URL, dedearchiv_url);
                        JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(dedearchives.getTagid()), dedearchives.getDede_archives_id(), upqu, updateExpress, ArchiveRelationType.TAG_RELATION);
                    }

                } else {
                    i++;
                }
//                String url = dedearchives.getDede_archives_url();
//                System.out.println(dedearchives);
                //
            }
            //JoymeAppServiceSngl.get().modifyTagDedearchives(Long.valueOf(tagid), 70336, queryExpress, updateExpress);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        UpdateExpress up = new UpdateExpress();
//        if (removestaus.equals("removed") || removestaus.equals("invalid")) {
//            up.increase(AnimeTagField.TOTAL_SUM, -1);
//        } else {
//            up.increase(AnimeTagField.TOTAL_SUM, 1);
//        }
//        try {
//            JoymeAppServiceSngl.get().modifyAnimeTag(tagid, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagid)), up);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
        System.out.println("end==============" + i);
    }

    public static void updateUser(FiveProps servProps) {
        try {
            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.ne(AppConfigField.APPKEY, "aaa"));
            Pagination pagination = new Pagination(100, 1, 100);
            //    ToolsServiceSngl.get().modifyPwd(DES.encrypt("wedcfr"), "200540")
            PageRows<AppConfig> pageRows = JoymeAppConfigServiceSngl.get().queryAppConfigByPage(qu, pagination);
            List<AppConfig> list = pageRows.getRows();
            for (AppConfig config : list) {
                System.out.println(config.getAppKey());
            }
        } catch (Exception e) {

        }
        System.out.println("end=====================");
    }

    public static void delete(FiveProps servProps) {
        try {
            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.ne(AppConfigField.APPKEY, "aaa"));
            Pagination pagination = new Pagination(100, 1, 100);
            PageRows<AppConfig> pageRows = JoymeAppConfigServiceSngl.get().queryAppConfigByPage(qu, pagination);
            List<AppConfig> list = pageRows.getRows();
            for (AppConfig config : list) {
                System.out.println(config.getAppKey());
            }
        } catch (Exception e) {

        }
        System.out.println("end=====================");
    }


    public static void importOauthAppsecrt(FiveProps servProps) {
        try {
            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.ne(AuthAppField.VALIDSTATUS, "aaa"));
            List<AuthApp> queryAuthAppList = oAuthHandler.queryAuthApp(qu);
            AuthAppCache cache = new AuthAppCache(new OAuthConfig(servProps).getMemCachedConfig());
            for (AuthApp authApp : queryAuthAppList) {
                AppSecret appSecret = new AppSecret();
                appSecret.setIos(authApp.getAppKey());
                appSecret.setAndroid(authApp.getAppKey());

                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(AuthAppField.APPSECRET, appSecret.toJson());
                //  OAuthServiceSngl.get().modifyAuthApp(authApp.getAppId(), updateExpress);

                QueryExpress queryExpress = new QueryExpress();
                System.out.println(authApp.getAppKey() + "--" + appSecret.toJson());
                queryExpress.add(QueryCriterions.eq(AuthAppField.APPID, authApp.getAppId()));

                boolean val = oAuthHandler.updateAuthApp(updateExpress, queryExpress);
                System.out.println(val);
                cache.remove(authApp.getAppId());
            }
            //  AuthAppCache cache = new AuthAppCache(new OAuthConfig(servProps).getMemCachedConfig());

        } catch (Exception e) {

        }
        System.out.println("end=====================");
    }

    public static void updateToolspassword() {
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()));

            //modifyUser(PrivilegeUser entity, Map<ObjectField, Object> map)
        } catch (Exception e) {

        }
        System.out.println("end=====================");
    }

    public static void updateCommentBeanStatus() {
        try {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(CommentBeanField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.eq(CommentBeanField.DOMAIN, CommentDomain.GAMECLIENT_MIYOU.getCode()));
            List<CommentBean> list = commentHandler.queryCommentBean(queryExpress);
            for (CommentBean commentBean : list) {
                System.out.println(commentBean.getCommentId() + "---" + commentBean.getUri());
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(CommentBeanField.REMOVE_STATUS, ActStatus.ACTING.getCode());
                CommentServiceSngl.get().modifyCommentBeanById(commentBean.getCommentId(), updateExpress);
            }
        } catch (Exception e) {

        }
        System.out.println("end=====================");
    }

    public static void updateDede_archives_pubdate() {
        try {
            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.ne(AnimeTagField.TAG_ID, -37L));
            List<AnimeTag> animeTagList = JoymeAppServiceSngl.get().queryAnimeTag(qu);
            for (AnimeTag tag : animeTagList) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, tag.getTag_id()));
                queryExpress.add(QuerySort.add(TagDedearchivesFiled.DISPLAY_ORDER, QuerySortOrder.DESC));
                List<TagDedearchives> list = JoymeAppServiceSngl.get().queryTagDedearchives(queryExpress);
                Date date = new Date();
                int i = 0;
                int j = 0;
                long time = date.getTime();
                for (TagDedearchives tagDedearchives : list) {
                    System.out.println(tagDedearchives.getDede_archives_title() + "=======" + tagDedearchives.getTagid() + "----" + tagDedearchives.getDede_archives_id() + "----" + tagDedearchives.getDede_archives_pubdate());
                    QueryExpress q = new QueryExpress();
                    q.add(QueryCriterions.eq(TagDedearchivesFiled.ID, tagDedearchives.getId()));
                    UpdateExpress u = new UpdateExpress();
                    if (i > j + 2) {
                        j = i;
                        time = time - 24 * 60 * 60 * 1000;
                    }
                    u.set(TagDedearchivesFiled.DEDE_ARCHIVES_PUBDATE, time);
                    JoymeAppServiceSngl.get().modifyTagDedearchives(tag.getTag_id(), tagDedearchives.getDede_archives_id(), q, u, ArchiveRelationType.TAG_RELATION);
                    i++;
                }

            }
            System.out.println("============================================");
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }


}
