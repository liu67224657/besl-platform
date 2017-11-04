/**
 *
 */
package com.enjoyf.webapps.joyme.weblogic.search;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.dto.joymeapp.wikiapp.WikiPageDTO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 搜索服务
 *
 * @author xinzhao
 */
@Service(value = "searchWebLogic")
public class SearchWebLogic {

    private Logger logger = LoggerFactory.getLogger(SearchWebLogic.class);

    //    private static final String SEARCH_URL = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchUrl();
    private static final String SEARCH_CORE_GAMEDB = "gamedb";
    private static final String SEARCH_KEY_PARAM_NAME = "name";
    private static final String SEARCH_KEY_PARAM_GAMEID = "gameid";

    private static final String SEARCH_CORE_WIKIPAGE = "wiki-page";
    private static final String SEARCH_WIKI_KEY_PARAM_WIKIKEY = "wikikey";
    private static final String SEARCH_WIKI_KEY_PARAM_TITLE = "title";
    private static final String SEARCH_WIKI_KEY_PARAM_ID = "id";

    //c:gamedb
    //q:(searchtext:熊)
    //sort:age:desc
    //p:1
    //ps:20
    public List<Long> searchGame(String[] texts, int curPage, int pageSize) {
        if (texts == null || texts.length == 0) {
            return Collections.EMPTY_LIST;
        }
        StringBuffer stringBuffer = new StringBuffer();

        for (String text : texts) {
            stringBuffer.append("(").append(SEARCH_KEY_PARAM_NAME).append(":").append(text).append(")");
        }
        HttpResult result = null;
        try {
            HttpClientManager httpClientManager = new HttpClientManager();
            result = httpClientManager.post(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchUrl(), new HttpParameter[]{
                    new HttpParameter("c", SEARCH_CORE_GAMEDB),
                    new HttpParameter("q", stringBuffer.toString()),
                    new HttpParameter("page", curPage),
                    new HttpParameter("ps", pageSize),
            }, null);

            //返回不是200
            if (result.getReponseCode() != 200) {
                GAlerter.lab(this.getClass().getName() + " call searchapi http error.:" + result);
                return Collections.EMPTY_LIST;
            }

            //{"rs":1,"result":[{"gameid":0,"name":"0VsYSLLsN8CrbBSMUOlLNx"}]}
            JSONObject jsonObject = new JSONObject().fromObject(result.getResult());
            //返回值是错误
            if (jsonObject.getInt("rs") != 1) {
                logger.info(this.getClass().getName() + "call searchapi response not success." + result);
                return Collections.EMPTY_LIST;
            }

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //返回值为空
            if (jsonArray == null) {
                return Collections.EMPTY_LIST;
            }
            Object[] resultObject = jsonArray.toArray();
            List<Long> gameIdList = new ArrayList<Long>();
            for (Object object : resultObject) {
                JSONObject jsonobj = (JSONObject) object;
                try {
                    gameIdList.add(jsonobj.getLong(SEARCH_KEY_PARAM_GAMEID));
                } catch (Exception e) {
                    logger.info(this.getClass().getName() + "call searchapi gameid is not long." + jsonobj.get("gameid"));
                }
            }

            return gameIdList;
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "call searchapi error." + result + " e,", e);
        }

        return Collections.EMPTY_LIST;
    }

    public PageRows<WikiPageDTO> searchWikipage(String wikiKey, String[] texts, int curPage, int pageSize) {
        PageRows<WikiPageDTO> returnRows = new PageRows<WikiPageDTO>();
        returnRows.setRows(new ArrayList<WikiPageDTO>());
        returnRows.setPage(new Pagination(pageSize, curPage, pageSize));

        if (texts == null || texts.length == 0) {
            return returnRows;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(").append(SEARCH_WIKI_KEY_PARAM_WIKIKEY).append(":").append(wikiKey).append(")");
        for (String text : texts) {
            stringBuffer.append("(").append(SEARCH_WIKI_KEY_PARAM_TITLE).append(":").append(text).append(")");
        }
        HttpResult result = null;
        try {
            HttpClientManager httpClientManager = new HttpClientManager();
            result = httpClientManager.post(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchUrl(), new HttpParameter[]{
                    new HttpParameter("c", SEARCH_CORE_WIKIPAGE),
                    new HttpParameter("q", stringBuffer.toString()),
                    new HttpParameter("p", curPage),
                    new HttpParameter("ps", pageSize),
            }, null);

            //返回不是200
            if (result.getReponseCode() != 200) {
                GAlerter.lab(this.getClass().getName() + " call searchapi http error.:" + result);
                return returnRows;
            }

            //{"rs":1,"result":[{"gameid":0,"name":"0VsYSLLsN8CrbBSMUOlLNx"}]}
            JSONObject jsonObject = new JSONObject().fromObject(result.getResult());
            //返回值是错误
            if (jsonObject.getInt("rs") != 1) {
                logger.info(this.getClass().getName() + "call searchapi response not success." + result);
                return returnRows;
            }

            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //返回值为空
            if (jsonArray == null) {
                return returnRows;
            }
            Object[] resultObject = jsonArray.toArray();

            for (Object object : resultObject) {
                JSONObject jsonobj = (JSONObject) object;
                try {
                    WikiPageDTO dto = new WikiPageDTO();
                    dto.setTitle(jsonobj.getString(SEARCH_WIKI_KEY_PARAM_TITLE));
                    dto.setWikiKey(jsonobj.getString(SEARCH_WIKI_KEY_PARAM_WIKIKEY));
                    dto.setWikiPageId(jsonobj.getLong(SEARCH_WIKI_KEY_PARAM_ID));
                    returnRows.getRows().add(dto);
                } catch (Exception e) {
                    logger.info(this.getClass().getName() + "call searchapi wikiid is not long." + jsonobj.get("gameid"), e);
                }
            }

            //build page
            returnRows.setPage(getPageByJsonStr(jsonObject));

            return returnRows;
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "call searchapi error." + result + " e,", e);
        }

        return returnRows;
    }


    private Pagination getPageByJsonStr(JSONObject jsonObject) {
        if (!jsonObject.containsKey("page")) {
            return null;
        }
        JSONObject pageObj = JSONObject.fromObject(jsonObject.get("page"));
        //{"total":22,"curpage":1,"pagesize":20,"maxpage":2}
        return new Pagination(pageObj.getInt("total"), pageObj.getInt("curpage"), pageObj.getInt("pagesize"));
    }

    public static void main(String[] args) {
        SearchWebLogic searchWebLogic = new SearchWebLogic();
        System.out.println(searchWebLogic.searchWikipage("wzzj", new String[]{"*"}, 1, 20));


    }


}