package com.enjoyf.webapps.joyme.webpage.quartz;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.service.point.ActivityGoods;
import com.enjoyf.platform.service.point.ActivityGoodsField;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.util.FileUtil;
import com.enjoyf.util.StringUtil;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-10-25 下午2:18
 * Description:
 */
public class SiteMapQuartzJob implements Job {
    private static final int PAGE_SIZE = 200;

    private static RedisManager redisManager = new RedisManager(WebappConfig.get().getProps());
    private static String GIFT_URL = WebappConfig.get().getGiftSitemapUrl();
    private static String GAME_URL = WebappConfig.get().getGameSitemapUrl();
    private static String SITEMAP_FOLDER = WebappConfig.get().getSitemapFolder();
    private static final String JOYME_SITEMAP_GIFT_URLS = "joyme_sitemap_gift_urls";
    private static final String JOYME_SITEMAP_GAME_URLS = "joyme_sitemap_game_urls";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //礼包
        try {
            genGiftSitemap();
        } catch (Exception e) {
            GAlerter.lab("=====================gift sitemap failed========================");
        }
        //游戏
        try {
            genGameSitemap();
        } catch (Exception e) {
            GAlerter.lab("=====================collection sitemap failed========================");
        }
    }

    private void genGameSitemap() {
        GAlerter.lan("=====================game collection sitemap start========================");
        redisManager.sadd(JOYME_SITEMAP_GAME_URLS, WebappConfig.get().getUrlWww() + GAME_URL);
        int cp = 0;
        Pagination page = null;
        do {
            cp += 1;
            page = new Pagination(PAGE_SIZE * cp, cp, PAGE_SIZE);
            PageRows<GameDB> gamePageRows = null;
            try {
                MongoQueryExpress queryExpress = new MongoQueryExpress();
                queryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
                gamePageRows = GameResourceServiceSngl.get().queryGameDbByPage(queryExpress, page);
                page = (gamePageRows == null ? page : gamePageRows.getPage());
                if (gamePageRows != null && !CollectionUtil.isEmpty(gamePageRows.getRows())) {
                    for (GameDB game : gamePageRows.getRows()) {
                        if (game != null) {
                            if (!StringUtil.isEmpty(game.getAnotherName())) {
                                String url = WebappConfig.get().getUrlWww() + GAME_URL + "/" + game.getGameDbId();
                                redisManager.sadd(JOYME_SITEMAP_GAME_URLS, url);
                            }
                        }
                    }
                }
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " occur ServiceException.e", e);
            }
        } while (!page.isLastPage());
        //生成www
        generatorSitemap(JOYME_SITEMAP_GAME_URLS, GAME_URL, WebappConfig.get().getUrlWww());

        //生成m
        generatorSitemap(JOYME_SITEMAP_GAME_URLS, GAME_URL, WebappConfig.get().getUrlM());
        GAlerter.lan("=====================game collection sitemap end========================");
    }

    private void genGiftSitemap() {
        GAlerter.lan("=====================gift sitemap start========================");
        redisManager.sadd(JOYME_SITEMAP_GIFT_URLS, WebappConfig.get().getUrlWww() + GIFT_URL);
        redisManager.sadd(JOYME_SITEMAP_GIFT_URLS, WebappConfig.get().getUrlWww() + GIFT_URL + "/more");
        int cp = 0;
        Pagination page = null;
        do {
            cp += 1;
            page = new Pagination(PAGE_SIZE * cp, cp, PAGE_SIZE);
            PageRows<ActivityGoods> goodsPageRows = null;
            try {
                goodsPageRows = PointServiceSngl.get().queryActivityGoodsByPage(new QueryExpress()
                        .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                        .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode())), page);

                page = (goodsPageRows == null ? page : goodsPageRows.getPage());

                if (goodsPageRows != null && !CollectionUtil.isEmpty(goodsPageRows.getRows())) {
                    for (ActivityGoods goods : goodsPageRows.getRows()) {
                        if (goods != null) {
                            String url = WebappConfig.get().getUrlWww() + GIFT_URL + "/" + goods.getActivityGoodsId();
                            redisManager.sadd(JOYME_SITEMAP_GIFT_URLS, url);
                        }
                    }
                }
            } catch (ServiceException e) {
                GAlerter.lab(this.getClass().getName() + " occur ServiceException.e", e);
            }
        } while (!page.isLastPage());

        //生成www
        generatorSitemap(JOYME_SITEMAP_GIFT_URLS, GIFT_URL, WebappConfig.get().getUrlWww());

        //生成m
        generatorSitemap(JOYME_SITEMAP_GIFT_URLS, GIFT_URL, WebappConfig.get().getUrlM());
        GAlerter.lan("=====================gift sitemap end========================");
    }

    private void generatorSitemap(String key, String folder, String domain) {
        GAlerter.lan("=====================generator sitemap xml start========================");
        org.dom4j.Document document = DocumentHelper.createDocument();
        org.dom4j.Element root = document.addElement("urlset", "http://www.sitemaps.org/schemas/sitemap/0.9");
        try {
            Set<String> urlSet = redisManager.smembers(key);
            if (!CollectionUtil.isEmpty(urlSet)) {
                for (String url : urlSet) {
                    if (StringUtil.isEmpty(url)) {
                        continue;
                    }
                    org.dom4j.Element urlElement = root.addElement("url"); //添加root的子节点
                    org.dom4j.Element locElement = urlElement.addElement("loc");
                    url = url.replace(WebappConfig.get().getUrlWww(), domain);
                    locElement.addText(url);
                    org.dom4j.Element priorityElement = urlElement.addElement("priority");
                    priorityElement.addText(String.valueOf(genPriority(url)));
                }
            }
            //输出全部原始数据，在编译器中显示
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");//根据需要设置编码

            String path = getPath(folder, domain);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }

            String xmlPath = path;
            // 输出全部原始数据，并用它生成新的我们需要的XML文件
            if (FileUtil.isFileOrDirExist(xmlPath)) {
                FileUtil.createDirectory(xmlPath);
            }

            File xmlFile = new File(xmlPath + "/sitemap.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(xmlFile);
            XMLWriter writer2 = new XMLWriter(fileOutputStream, format);
            writer2.write(document); //输出到文件
            writer2.close();
            GAlerter.lan("=====================generator sitemap xml end========================");
        } catch (UnsupportedEncodingException e) {
            GAlerter.lab(this.getClass().getName() + " occur UnsupportedEncodingException.e", e);
        } catch (IOException e) {
            GAlerter.lab(this.getClass().getName() + " occur IOException.e", e);
        }
    }

    private String getPath(String path, String domain) {
        return SITEMAP_FOLDER + "/" + domain.replace("http://", "") + path;
    }

    private double genPriority(String pageUrl) {
        double priority = 0.5;
        try {
            URL url = new URL(pageUrl);
            String path = url.getPath();
            if (path.startsWith("/")) {
                path.substring(1, path.length());
            }
            if (path.endsWith("/")) {
                path.substring(0, path.length() - 1);
            }
            if (path.contains("/")) {
                String[] paths = url.getPath().split("/");
                if (paths.length == 1) {
                    priority = 0.8;
                } else if (paths.length == 2) {
                    priority = 0.6;
                } else {
                    priority = 0.5;
                }
            } else {
                priority = 0.8;
            }
        } catch (MalformedURLException e) {
            GAlerter.lab(this.getClass().getName() + " occur MalformedURLException.e", e);
        }
        return priority;
    }
}
