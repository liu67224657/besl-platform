package com.enjoyf.platform.service.ask.search;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ask.AskUtil;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/21
 */
public class WanbaSearchService {
    private static Logger logger = LoggerFactory.getLogger(WanbaSearchService.class);


    private static final String SEARCH_CORE_WANBA = "wanba";
    private static final String SEARCH_KEY_PARAM_TITLE = "title";
    private static final String SEARCH_KEY_PARAM_TYPE = "type";
    private static final String SEARCH_KEY_PARAM_GAMEID = "id";
    private static HttpClientManager httpClientManager = new HttpClientManager();

    //c:gamedb
    //q:(searchtext:熊)
    //sort:age:desc
    //p:1
    //ps:20
    public PageRows<String> searchByType(WanbaSearchType type, int curPage, int pageSize, String... texts) {
        PageRows<String> pageRows = new PageRows<String>();
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
                    new HttpParameter("c", SEARCH_CORE_WANBA),
                    new HttpParameter("q", stringBuffer.toString()),
                    new HttpParameter("p", curPage),
                    new HttpParameter("ps", pageSize),
            }, null);

            //返回不是200
            if (result.getReponseCode() != 200) {
                GAlerter.lab(this.getClass().getName() + " call searchapi http error.:" + result);
                return pageRows;
            }

            //{"rs":1,"result":[{"gameid":0,"name":"0VsYSLLsN8CrbBSMUOlLNx"}]}
            JSONObject jsonObject = new JSONObject().fromObject(result.getResult());
            //返回值是错误
            if (jsonObject.getInt("rs") != 1) {
                logger.info(this.getClass().getName() + "call searchapi response not success." + result);
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
            List<String> returnList = new ArrayList<String>();
            for (Object object : resultObject) {
                JSONObject jsonobj = (JSONObject) object;
                try {
                    returnList.add(jsonobj.getString(SEARCH_KEY_PARAM_GAMEID));
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


    //key=value,key1=value1,key2=value2
    public boolean saveObject(WanbaSearchEntry entry) {

        try {
            GAlerter.lan("WanbaSearchService-processOneOnOneAnswer---getSearchSaveUrl>" + HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchSaveUrl());

            HttpResult result = httpClientManager.post(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchSaveUrl(), new HttpParameter[]{
                    new HttpParameter("c", SEARCH_CORE_WANBA),
                    new HttpParameter("field", new Gson().toJson(entry)),
            },null);
            GAlerter.lan("WanbaSearchService-processOneOnOneAnswer---result>" + result + " ,entry:" + entry);
            JSONObject jsonObject = JSONObject.fromObject(result.getResult());
            //返回不是200
            if (result.getReponseCode() != 200) {
                GAlerter.lab(this.getClass().getName() + " call searchapi http error.:" + result);
                return false;
            }
            //{"rs":1,"result":[{"gameid":0,"name":"0VsYSLLsN8CrbBSMUOlLNx"}]}
            return jsonObject.getInt("rs") == 1;
        } catch (Exception e) {
            GAlerter.lan(this.getClass().getName() + " occured error.e:", e);
            return false;
        }
    }


    public boolean deleteObject(WanbaSearchDeleteEntry entry) {
        try {
            HttpResult result = httpClientManager.post(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getSearchDeleteUrl(), new HttpParameter[]{
                    new HttpParameter("c", SEARCH_CORE_WANBA),
                    new HttpParameter("q", entry.getDeleteId()),
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

    static String ASK_PROFILEID = "02d40db86df60181779d4d9fd9852ebf"; //口袋
    static String ANSWER_PROFILEID = "0287f091ec14aa1ce7b8936ea5745b3c";//越长大越孤独

    public static void main(String[] args) {
        WanbaSearchService searchService = new WanbaSearchService();

//        WanbaSearchEntry searchEntry = new WanbaSearchEntry();
//        searchEntry.setContent("this is profile");
//        searchEntry.setTitle("口袋");
//        searchEntry.setId(ASK_PROFILEID);
//        searchEntry.setTag("游戏");
//        searchEntry.setType(WanbaSearchType.PROFILE);
//        searchEntry.setEntryid(AskUtil.getWanbaSearchEntryId(ASK_PROFILEID, WanbaSearchType.PROFILE));
//        System.out.println("====" + searchService.saveObject(searchEntry));
//
//        WanbaSearchEntry searchEntry2 = new WanbaSearchEntry();
//        searchEntry2.setContent("this is profile2");
//        searchEntry2.setTitle("越长大越孤独");
//        searchEntry2.setId(ANSWER_PROFILEID);
//        searchEntry2.setTag("游戏");
//        searchEntry2.setType(WanbaSearchType.PROFILE);
//        searchEntry2.setEntryid(AskUtil.getWanbaSearchEntryId(ANSWER_PROFILEID, WanbaSearchType.PROFILE));
//        System.out.println("====" + searchService.saveObject(searchEntry2));
//
//
//        WanbaSearchEntry searchEntry3 = new WanbaSearchEntry();
//        searchEntry3.setContent("this is question");
//        searchEntry3.setTitle("口袋提问的2222");
//        searchEntry3.setId("1");
//        searchEntry3.setTag("游戏问题");
//        searchEntry3.setType(WanbaSearchType.QUESION);
//        searchEntry3.setEntryid(AskUtil.getWanbaSearchEntryId("1", WanbaSearchType.QUESION));
//        System.out.println("====" + searchService.saveObject(searchEntry3));

        WanbaSearchEntry searchEntry4 = new WanbaSearchEntry();
        searchEntry4.setContent("this is question");
        searchEntry4.setTitle("这是问题问为得为问我额外额外额我额");
        searchEntry4.setId("343243242");
        searchEntry4.setTag("游戏问题");
        searchEntry4.setType(WanbaSearchType.QUESION.getCode());
        searchEntry4.setEntryid(AskUtil.getWanbaSearchEntryId("2", WanbaSearchType.QUESION));
        System.out.println("====" + searchService.saveObject(searchEntry4));


        PageRows<String> pageRows = searchService.searchByType(WanbaSearchType.QUESION, 1, 10, "这是问题问为得为问我额外额外额我额");
        if (CollectionUtil.isEmpty(pageRows.getRows())) {
            System.out.println("searchByType is empty");
        } else {
            for (String str : pageRows.getRows()) {
                System.out.println(str);
            }
        }


        WanbaSearchDeleteEntry deleteEntry = new WanbaSearchDeleteEntry();
        deleteEntry.setQid("343243242");
        System.out.println("====" + searchService.deleteObject(deleteEntry));
        /// System.out.println();

    }

}
