package com.enjoyf.platform.service.ask.search;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.service.misc.FiledValue;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhimingli on 2017-3-29 0029.
 */
public class WikiappSearchService {
    private static Logger logger = LoggerFactory.getLogger(WanbaSearchService.class);

    private static final String SEARCH_CORE_WIKIAPP = "wikiapp";
    private static final String SEARCH_KEY_PARAM_TITLE = "title";
    private static final String SEARCH_KEY_PARAM_TYPE = "type";
    private static final String SEARCH_KEY_PARAM_ID = "id";
    private static final String SEARCH_KEY_PARAM_CREATETIME = "createtime";

    private static HttpClientManager httpClientManager = new HttpClientManager();

    public PageRows<FiledValue> searchByType(WikiappSearchType type, int curPage, int pageSize, String... texts) {
        PageRows<FiledValue> pageRows = new PageRows<FiledValue>();
        pageRows.setPage(new Pagination(curPage * pageSize, curPage, pageSize));
        if (texts == null || texts.length == 0) {
            return pageRows;
        }
        StringBuffer stringBuffer = new StringBuffer();

        for (String text : texts) {
            stringBuffer.append("(").append(SEARCH_KEY_PARAM_TITLE).append(":").append(text).append(")");
        }
        if (type != null) {
            stringBuffer.append("(").append(SEARCH_KEY_PARAM_TYPE).append(":").append(type.getCode()).append(")");
        }

        HttpResult result = null;
        try {
            HttpClientManager httpClientManager = new HttpClientManager();
            result = httpClientManager.post(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchUrl(), new HttpParameter[]{
                    new HttpParameter("c", SEARCH_CORE_WIKIAPP),
                    new HttpParameter("q", stringBuffer.toString()),
                    new HttpParameter("p", curPage),
                    new HttpParameter("ps", pageSize),
            }, null);

            //返回不是200
            if (result.getReponseCode() != 200) {
                return pageRows;
            }

            JSONObject jsonObject = new JSONObject().fromObject(result.getResult());
            //返回值是错误
            if (jsonObject.getInt("rs") != 1) {
                return pageRows;
            }

            JSONObject pageObject = jsonObject.getJSONObject("page");
            int total = pageObject.getInt("total");
            pageRows.getPage().setTotalRows(total);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //返回值为空
            if (jsonArray == null) {
                return pageRows;
            }
            Object[] resultObject = jsonArray.toArray();
            List<FiledValue> returnList = new ArrayList<FiledValue>();
            for (Object object : resultObject) {
                JSONObject jsonobj = (JSONObject) object;
                try {
                    FiledValue filedValue = new FiledValue(jsonobj.getString(SEARCH_KEY_PARAM_ID), jsonobj.getString(SEARCH_KEY_PARAM_CREATETIME));
                    returnList.add(filedValue);
                } catch (Exception e) {
                    logger.info(this.getClass().getName() + "call searchapi search ." + jsonobj.get("id"));
                }
            }
            pageRows.setRows(returnList);
            return pageRows;
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "call searchapi error." + result + " e,", e);
        }
        return pageRows;
    }

    public boolean saveObject(WikiappSearchEntry entry) {
        try {

            HttpResult result = httpClientManager.post(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchSaveUrl(), new HttpParameter[]{
                    new HttpParameter("c", SEARCH_CORE_WIKIAPP),
                    new HttpParameter("field", new Gson().toJson(entry)),
            }, null);

            JSONObject jsonObject = JSONObject.fromObject(result.getResult());

            if (result.getReponseCode() != 200) {
                return false;
            }
            return jsonObject.getInt("rs") == 1;
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occured error.e:", e);
            return false;
        }
    }


    public boolean deleteObject(WikiappSearchDeleteEntry entry) {
        try {
            HttpResult result = httpClientManager.post(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchDeleteUrl(), new HttpParameter[]{
                    new HttpParameter("c", SEARCH_CORE_WIKIAPP),
                    new HttpParameter("q", entry.getDeleteId(entry)),
            }, null);
            //返回不是200
            if (result.getReponseCode() != 200) {
                GAlerter.lab(this.getClass().getName() + " call searchapi http error.:" + result);
                return false;
            }
            JSONObject jsonObject = new JSONObject().fromObject(result.getResult());

            return jsonObject.getInt("rs") == 1;
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occured error.e:", e);
            return false;
        }
    }

    public static void main(String[] args) {
        WikiappSearchService searchService = new WikiappSearchService();
        WikiappSearchEntry search = new WikiappSearchEntry();
        search.setEntryid(AskUtil.getWikiappSearchEntryId("101160", WikiappSearchType.GAME));
        search.setId("101160");
        search.setType(WikiappSearchType.GAME.getCode());
        search.setTitle("萌宠时代");
        search.setCreatetime(System.currentTimeMillis());
        searchService.saveObject(search);

//        WikiappSearchDeleteEntry dele = new WikiappSearchDeleteEntry();
//        dele.setId("1");
//        dele.setType(WikiappSearchType.GAME);
//        searchService.deleteObject(dele);
        //test merge

        System.out.println("end============");
    }
}
