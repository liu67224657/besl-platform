package com.enjoyf.platform.tools.gamedb;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;

import java.util.Date;

/**
 * 将游戏库导入到渠道后台PHP
 */
public class ImportGameDBSendPhp {

    // private static GameDBHandler gameDBHandler = null;

    private static Date defaultDate = null;

    public static void main(String[] args) {
        //FiveProps fiveProps = Props.instance().getServProps();
        try {
            // gameDBHandler = new GameDBHandler("gamedb", fiveProps);

            defaultDate = DateUtil.formatStringToDate("2011-04-02 9:00:00", DateUtil.DEFAULT_DATE_FORMAT2);

            importGameDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void importGameDB() {
        System.out.println("====start");
        int PAGE_SIZE = 400;
        int cp = 0;
        Pagination page = null;
        do {
            cp += 1;
            page = new Pagination(PAGE_SIZE * cp, cp, PAGE_SIZE);
            PageRows<GameDB> gamePageRows = null;
            try {
                MongoQueryExpress queryExpress = new MongoQueryExpress();
                //queryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
                // queryExpress.add(MongoQueryCriterions.eq(GameDBField.ID, 101193L));

                queryExpress.add(MongoQueryCriterions.ne(GameDBField.ID, "-1000L"));
                gamePageRows = GameResourceServiceSngl.get().queryGameDbByPage(queryExpress, page);
                System.out.println(gamePageRows.getPage());
                page = (gamePageRows == null ? page : gamePageRows.getPage());
                if (gamePageRows != null && !CollectionUtil.isEmpty(gamePageRows.getRows())) {
                    for (GameDB game : gamePageRows.getRows()) {

                        //创建时间和上市时间不能为空，如果为空需要给一个默认日期
                        if (game.getGamePublicTime() == null || game.getCreateDate() == null) {

                            BasicDBObject queryDBObject = new BasicDBObject();
                            BasicDBObject updateDBObject = new BasicDBObject();
                            queryDBObject.put(GameDBField.ID.getColumn(), game.getGameDbId());

                            if (game.getGamePublicTime() == null) {
                                game.setGamePublicTime(defaultDate);
                                updateDBObject.put(GameDBField.GAMEPUBLICTIME.getColumn(), defaultDate);
                            }

                            if (game.getCreateDate() == null) {
                                game.setCreateDate(defaultDate);
                                updateDBObject.put(GameDBField.CREATE_DATE.getColumn(), defaultDate);
                            }
                            GameResourceServiceSngl.get().updateGameDB(queryDBObject, updateDBObject);

                        }


                        //审核通过 往PHP发送
                        if (game != null && game.getValidStatus().getCode().equals(GameDbStatus.VALID.getCode())) {
                            sendPhpGamedb(game);
                        }
                    }
                }
            } catch (Exception e) {
                //GAlerter.lab(this.getClass().getName() + " occur ServiceException.e", e);
            }
        } while (!page.isLastPage());

        System.out.println("====end");
    }


    private static HttpClientManager httpClient = new HttpClientManager();


    public static void sendPhpGamedb(GameDB gameDB) {
        if (gameDB == null) {
            return;
        }
        //http://channel.joyme.dev/?c=source&a=savegame
        try {
            HttpParameter httpParameter[] = new HttpParameter[]{
                    new HttpParameter("gid", gameDB.getGameDbId()),
                    new HttpParameter("extra", new Gson().toJson(gameDB))
            };

            String domain = WebappConfig.get().getDomain();
            if (WebappConfig.get().getDomain().equals("joyme.test") || WebappConfig.get().getDomain().equals("joyme.dev")) {
                domain = "joyme.alpha";
            }

            HttpResult httpResult = httpClient.post("http://channel." + domain + "?c=source&a=savegame", httpParameter, null);
            if (httpResult.getReponseCode() != 200) {
                System.out.printf(new Gson().toJson(httpResult));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
