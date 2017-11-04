package com.enjoyf.webapps.tools.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.AnimeIndex;
import com.enjoyf.platform.service.joymeapp.AnimeIndexField;
import com.enjoyf.platform.service.joymeapp.AnimeRedirectType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.mongodb.BasicDBObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-10-27
 * Time: 上午11:44
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/anime/index")
public class AnimeIndexController extends ToolsBaseController {
    private static String APPKEY = "0G30ZtEkZ4vFBhAfN7Bx4v"; //海贼迷的APPKEY
    private static String CODE = "ONEPIECE_";//海贼

    private static String CODE_NARUTO = "NARUTO_";//火影
    private static String CODE_APPKEY = "1zBwYvQpt3AE6JsykiA2es"; //火影的APPKEY

    private static Set<String> appkeyset = new HashSet<String>();

    static {
        appkeyset.add(APPKEY);
        appkeyset.add(CODE_APPKEY);
    }


    @RequestMapping(value = "list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String appkey,
                             @RequestParam(value = "platform", required = false) String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            MongoQueryExpress queryExpress = new MongoQueryExpress();
            if (!StringUtil.isEmpty(platform)) {
                queryExpress.add(MongoQueryCriterions.eq(AnimeIndexField.PLATFORM, Integer.parseInt(platform)));
            }
            queryExpress.add(MongoQueryCriterions.eq(AnimeIndexField.APPKEY, appkey));
            queryExpress.add(new MongoSort[]{new MongoSort(AnimeIndexField.ID, MongoSortOrder.DESC)});

            PageRows<AnimeIndex> pageRows = JoymeAppServiceSngl.get().queryAnimeIndexByPage(queryExpress, pagination);

            //TODO 七牛
            List<AnimeIndex> list = pageRows.getRows();
            for (int i = 0; i < list.size(); i++) {
                AnimeIndex index = list.get(i);
                index.setPic_url(URLUtils.getJoymeDnUrl(index.getPic_url()));
                list.set(i, index);
            }

            mapMessage.put("list", list);
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("platform", platform);
            mapMessage.put("appkeyset", appkeyset);
            mapMessage.put("appkey", appkey);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
        }

        return new ModelAndView("/joymeapp/anime/index/animeindexlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String apppkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(AnimeRedirectType.ZHUIFAN.getCode());
        list.add(AnimeRedirectType.SHIDIAN.getCode());
        list.add(AnimeRedirectType.HAIWAN.getCode());
        mapMessage.put("list", list);
        mapMessage.put("appkey", apppkey);

        return new ModelAndView("joymeapp/anime/index/animecreatepage", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "linename", required = false) String linename,
                               @RequestParam(value = "superscript", required = false) String superScript,
                               @RequestParam(value = "wikinum", required = false) String wikiNum,
                               @RequestParam(value = "picurl", required = false) String picUrl,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "linkurl", required = false) String linkUrl,
                               @RequestParam(value = "animeredirect", required = false) String animereDirect,
                               @RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String apppkey,
                               @RequestParam(value = "desc", required = false) String desc) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AnimeIndex animeIndex = new AnimeIndex();
            animeIndex.setLine_name(linename);
            animeIndex.setSuperScript(superScript);
            animeIndex.setWikiPageNum(wikiNum);
            animeIndex.setPic_url(picUrl);
            animeIndex.setPlatform(platform);
            animeIndex.setLinkUrl(linkUrl);
            animeIndex.setAnimeRedirectType(AnimeRedirectType.getByCode(Integer.parseInt(animereDirect)));
            animeIndex.setDesc(desc);
            animeIndex.setValidStatus(ValidStatus.INVALID);

            //TODO
            if (apppkey.equals(APPKEY)) {
                animeIndex.setCode(CODE + code);
            } else if (apppkey.equals(CODE_APPKEY)) {
                animeIndex.setCode(CODE_NARUTO + code);
            }
            animeIndex.setTitle(title);
            animeIndex.setAppkey(apppkey);
            animeIndex.setCreateUser(getCurrentUser().getUsername());
            animeIndex.setCreateDate(new Date());

            animeIndex = JoymeAppServiceSngl.get().createAnimeIndex(animeIndex);

            writeToolsLog(LogOperType.ANIME_INDEX_ADD, "新增:animeIndexId=" + animeIndex.getAnimeIndexId());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", apppkey);
        return new ModelAndView("redirect:/anime/index/list", map);
    }

    @RequestMapping(value = "modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            AnimeIndex animeIndex = JoymeAppServiceSngl.get().getAnimeIndex(new BasicDBObject().append("_id", id));

            //TODO 七牛
            animeIndex.setPic_url(URLUtils.getJoymeDnUrl(animeIndex.getPic_url()));

            mapMessage.put("animeIndex", animeIndex);
            List<Integer> list = new ArrayList<Integer>();
            list.add(AnimeRedirectType.ZHUIFAN.getCode());
            list.add(AnimeRedirectType.SHIDIAN.getCode());
            list.add(AnimeRedirectType.HAIWAN.getCode());
            mapMessage.put("list", list);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return new ModelAndView("joymeapp/anime/index/animemodifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "linename", required = false) String linename,
                               @RequestParam(value = "superscript", required = false) String superScript,
                               @RequestParam(value = "wikinum", required = false) String wikiNum,
                               @RequestParam(value = "picurl", required = false) String picUrlI,
                               @RequestParam(value = "platform", required = false) int platform,
                               @RequestParam(value = "linkurl", required = false) String linkUrl,
                               @RequestParam(value = "animeredirect", required = false) String animereDirect,
                               @RequestParam(value = "code", required = false) String code,
                               @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String apppkey,
                               @RequestParam(value = "desc", required = false) String desc) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(AnimeIndexField.ID.getColumn(), Long.parseLong(id));
            BasicDBObject updateDBObject = new BasicDBObject();
            updateDBObject.put(AnimeIndexField.SUPER_SCRIPT.getColumn(), superScript);
            updateDBObject.put(AnimeIndexField.LINE_NAME.getColumn(), linename);
            updateDBObject.put(AnimeIndexField.WIKI_PAGE_NUM.getColumn(), wikiNum);
            updateDBObject.put(AnimeIndexField.PIC_URL.getColumn(), picUrlI);
            updateDBObject.put(AnimeIndexField.PLATFORM.getColumn(), platform);
            updateDBObject.put(AnimeIndexField.LINK_URL.getColumn(), linkUrl);
            updateDBObject.put(AnimeIndexField.ANIME_REDIRECT.getColumn(), Integer.parseInt(animereDirect));
            updateDBObject.put(AnimeIndexField.DESC.getColumn(), desc);

            //TODO
            if (apppkey.equals(APPKEY)) {
                updateDBObject.put(AnimeIndexField.CODE.getColumn(), CODE + code);
            } else if (apppkey.equals(CODE_APPKEY)) {
                updateDBObject.put(AnimeIndexField.CODE.getColumn(), CODE_NARUTO + code);
            }

            updateDBObject.put(AnimeIndexField.TITLE.getColumn(), title);


            boolean bool = JoymeAppServiceSngl.get().modifyAnimeIndex(queryDBObject, updateDBObject);

            if (bool) {
                writeToolsLog(LogOperType.ANIME_INDEX_UPDATE, "修改:ID=" + id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", apppkey);
        return new ModelAndView("redirect:/anime/index/list", map);
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String apppkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        BasicDBObject queryDBObject = new BasicDBObject();
        queryDBObject.put(AnimeIndexField.ID.getColumn(), Long.parseLong(id));
        BasicDBObject updateDBObject = new BasicDBObject();
        updateDBObject.put(AnimeIndexField.VALID_STATUS.getColumn(), ValidStatus.REMOVED.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyAnimeIndex(queryDBObject, updateDBObject);
            if (bool) {
                writeToolsLog(LogOperType.ANIME_INDEX_DETETE, "删除:ID=" + id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", apppkey);
        return new ModelAndView("redirect:/anime/index/list", map);
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "id", required = false) String id,
                                @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String apppkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        BasicDBObject queryDBObject = new BasicDBObject();
        queryDBObject.put(AnimeIndexField.ID.getColumn(), Long.parseLong(id));
        BasicDBObject updateDBObject = new BasicDBObject();
        updateDBObject.put(AnimeIndexField.VALID_STATUS.getColumn(), ValidStatus.VALID.getCode());
        try {
            boolean bool = JoymeAppServiceSngl.get().modifyAnimeIndex(queryDBObject, updateDBObject);
            if (bool) {
                writeToolsLog(LogOperType.ANIME_INDEX_RECOVER, "恢复:ID=" + id);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMessage", "system.error");
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", apppkey);
        return new ModelAndView("redirect:/anime/index/list", map);
    }
}
