package com.enjoyf.platform.serv.gameres.quartz;

import com.enjoyf.platform.db.gameres.GameResourceHandler;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.gameres.WikiResourceField;
import com.enjoyf.platform.service.gameres.WikiResource;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class WikiStatsLogic {
    Logger logger = LoggerFactory.getLogger(WikiStatsLogic.class);

    private GameResourceHandler gameResourceHandler;

    private static final String STAT_API = "/wiki_ext_api/index.php?getData=page_count";

    public WikiStatsLogic(GameResourceHandler gameResourceHandler) {
        this.gameResourceHandler = gameResourceHandler;
    }

    public void statsWiki() {
        GAlerter.lan(this.getClass().getName() + " start statWiki...");
        long now = System.currentTimeMillis();
        try {
            List<WikiResource> wikiResourceList = gameResourceHandler.queryWikiResource(new QueryExpress().add(QueryCriterions.eq(WikiResourceField.REMOVESTATUS, ActStatus.UNACT.getCode())));

            for (WikiResource wikiResource : wikiResourceList) {
                try {
                    statWiki(wikiResource);
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
        }

        GAlerter.lan(this.getClass().getName() + " end statWiki... spent second : " + ((System.currentTimeMillis() - now) / 1000));
    }


    public boolean statWiki(WikiResource wikiResource) throws Exception {

        String wikiUrl = wikiResource.getWikiUrl();

        if (StringUtil.isEmpty(wikiUrl)) {
            GAlerter.lan(this.getClass().getName() + " wikiurl is empty.wikiid: " + wikiResource.getResourceId());
            return false;
        }

        //取得数据 插入 统计数据
        HttpResult result = new HttpClientManager().post(wikiUrl + STAT_API, null, null);

        if (result.getReponseCode() != HttpClientManager.OK) {
            GAlerter.lan(this.getClass().getName() + " get stats http response not ok.responsecode:" + result.getReponseCode() + " wikiid:" + wikiResource.getResourceId());
            return false;
        }

        JSONObject resultObj = (JSONObject) JSONValue.parse(result.getResult());
        int totalPageNum = Integer.parseInt(String.valueOf(resultObj.get("page_num_count")));
        long lastUpdateTimestamp = Long.parseLong(String.valueOf(resultObj.get("page_lastup_time")));
        int lastWeekUpdateNum = Integer.parseInt(String.valueOf(resultObj.get("week_up_count")));
        System.out.println(result.getResult());

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(WikiResourceField.TOTALPAGENUM, totalPageNum);
        updateExpress.set(WikiResourceField.LASTWEEKUPDATEPAGENUM, lastWeekUpdateNum);
        updateExpress.set(WikiResourceField.LASTUPDATEPAGEDATE, new Date(lastUpdateTimestamp));

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(WikiResourceField.RESOURCEID, wikiResource.getResourceId()));


        return gameResourceHandler.updateWikiResource(updateExpress, queryExpress);
    }
}
