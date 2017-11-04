package com.enjoyf.webapps.tools.webpage.controller.gameclient;

import com.enjoyf.framework.mongodb.MongoDBDao;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameDB;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDbStatus;
import com.enjoyf.platform.service.joymeapp.ClientLineItem;
import com.enjoyf.platform.service.joymeapp.ClientLineItemField;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.PointwallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallAppField;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.service.point.pointwall.PointwallWallAppField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by tonydiao on 2014/12/19.
 */

@Controller
@RequestMapping(value = "/json/gameclient/clientline")
public class JsonClientLineGameController extends ToolsBaseController {


    @RequestMapping(value = "/toaddgame")
    @ResponseBody
    public String jsonapplist(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                              @RequestParam(value = "rows", required = false, defaultValue = "20") int rows,
                              @RequestParam(value = "lineId", required = false) String lineId,
                              @RequestParam(value = "gameName", required = false) String gameName) {   //repeatFlag  值是0 是不允许重复，1时允许重复，默认是0          @RequestParam(value = "repeatFlag", required = false,defaultValue = "0") String repeatFlag
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {
              //查询mongodb ,game库下面的game_db表 ,要剔除已经包含 在这个client_line内的数据
            MongoQueryExpress queryExpress = new MongoQueryExpress();


            //查询当前此clientline中已经含有的mongodb ,game库下面的game_db表的_id列表
           if(!StringUtil.isEmpty(lineId)) {
               QueryExpress queryExpressLineItem = new QueryExpress();
               queryExpressLineItem.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, Long.valueOf(lineId)));
               queryExpressLineItem.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
               List<ClientLineItem> listItems = JoymeAppServiceSngl.get().queryClientLineItemByQueryExpress(queryExpressLineItem);

               if (listItems != null && listItems.size() > 0) {
                   Pattern pattern = Pattern.compile("([1-9][0-9]*)||0");
                   List<Long> ids = new ArrayList<Long>();
                   for (int i = 0; i < listItems.size(); i++) {
                       if (pattern.matcher(listItems.get(i).getDirectId()).matches()) {
                           ids.add(Long.valueOf(listItems.get(i).getDirectId()));
                       }
                   }
                   queryExpress.add(MongoQueryCriterions.notIn(GameDBField.ID, ids.toArray()));
               }
           }
            //如果gameName参数不为空,加入这个过滤条件
            if (!StringUtil.isEmpty(gameName)) {
                queryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME,gameName));
            }

            //按mongodb,game库下面的game_db表的_id的逆序来排序
            queryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
            queryExpress.add(new MongoSort[]{new MongoSort(GameDBField.ID, MongoSortOrder.DESC)});
            int pageStartIndex = (page - 1) * rows;
            int pageSize = rows;

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

            PageRows<GameDB> pageRows = GameResourceServiceSngl.get().queryGameDbByPage(queryExpress, pagination);
            int total = GameResourceServiceSngl.get().countGameDB(queryExpress);
            if (pageRows != null) {
                mapMessage.put("rows", pageRows.getRows());
                mapMessage.put("total", total);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return binder.toJson(mapMessage);
    }


    @RequestMapping(value="/wikiaddgame")
    @ResponseBody
       public String jsonAddGame(@RequestParam(value = "page", required = false, defaultValue = "1") int page,      //数据库记录索引
                                 @RequestParam(value = "rows", required = false, defaultValue = "20") int rows,
                                 @RequestParam(value = "gameName", required = false) String gameName) {   //repeatFlag  值是0 是不允许重复，1时允许重复，默认是0          @RequestParam(value = "repeatFlag", required = false,defaultValue = "0") String repeatFlag
           Map<String, Object> mapMessage = new HashMap<String, Object>();
           JsonBinder binder = JsonBinder.buildNormalBinder();
           try {
                 //查询mongodb ,game库下面的game_db表 ,要剔除已经包含 在这个client_line内的数据
               MongoQueryExpress queryExpress = new MongoQueryExpress();

               //如果gameName参数不为空,加入这个过滤条件
               if (!StringUtil.isEmpty(gameName)) {
                   queryExpress.add(MongoQueryCriterions.like(GameDBField.GAMENAME,gameName));
               }

               //按mongodb,game库下面的game_db表的_id的逆序来排序
               queryExpress.add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, GameDbStatus.VALID.getCode()));
               queryExpress.add(MongoQueryCriterions.ne(GameDBField.WIKIKEY,null));
               queryExpress.add(new MongoSort[]{new MongoSort(GameDBField.ID, MongoSortOrder.DESC)});
               int pageStartIndex = (page - 1) * rows;
               int pageSize = rows;

               int curPage = (pageStartIndex / pageSize) + 1;
               Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

               PageRows<GameDB> pageRows = GameResourceServiceSngl.get().queryGameDbByPage(queryExpress, pagination);
               int total = GameResourceServiceSngl.get().countGameDB(queryExpress);
               if (pageRows != null) {
                   mapMessage.put("rows", pageRows.getRows());
                   mapMessage.put("total", total);
               }
           } catch (ServiceException e) {
               GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
               mapMessage = putErrorMessage(mapMessage, "system.error");
           }
           return binder.toJson(mapMessage);
       }
}
