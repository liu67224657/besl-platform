package com.enjoyf.webapps.tools.webpage.controller.editor;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResource;
import com.enjoyf.platform.service.gameres.GameResourceField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.regex.RegexUtil;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-3-28
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/editor/game")
public class EditorGameController extends EditorBaseController {

    private Pattern GAME_URL_PATTERN = Pattern.compile("/game/([^/?]+)");


    @RequestMapping(value = "/page")
    public ModelAndView contentReportList(
            @RequestParam(value = "startDate", required = false) Date startDate,
            @RequestParam(value = "adminuno", required = false) Integer adminUno,
            @RequestParam(value = "endDate", required = false) Date endDate,
            @RequestParam(value = "status", required = false) String validStatusCode,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer maxPageItems) {

        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put("statusList",ValidStatus.getAll());

        PrivilegeUser currentUser = getCurrentUser();

        int queryUno = adminUno != null ? adminUno : currentUser.getUno();
        StatsEditor statsEditor = null;
        try {
            statsEditor = ToolsServiceSngl.get().getStatsEditor(new QueryExpress()
                    .add(QueryCriterions.eq(StatsEditorField.ADMINUNO, queryUno))
                    .add(QueryCriterions.eq(StatsEditorField.VALIDSTATUS, ValidStatus.VALID.getCode())));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        //todo
        if (statsEditor == null) {
            messageMap.put("rurl", "/editor/game/page");
            return new ModelAndView("/editor/activepage", messageMap);
        }

        messageMap.put("editor", statsEditor);

        //startDate<=endDate
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(StatsEditorItemField.ADMINUNO, statsEditor.getAdminUno()))
                .add(QueryCriterions.eq(StatsEditorItemField.ITEMTYPE, EditorStatsItemType.GAME.getCode()));
        if (endDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.DAY_OF_YEAR, 1);
            endDate = cal.getTime();
        }

        if (startDate == null || endDate == null || endDate.after(startDate)) {
            if (startDate != null) {
                queryExpress.add(QueryCriterions.gt(StatsEditorItemField.CREATEDATE, startDate));
            }

            if (endDate != null) {
                queryExpress.add(QueryCriterions.lt(StatsEditorItemField.CREATEDATE, endDate));
            }
        }
        messageMap.put("startDate", startDate);
        messageMap.put("endDate", endDate);

        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, maxPageItems);
        //有查询的状态条件
        ValidStatus queryStatus = ValidStatus.getByCode(validStatusCode);
        if (queryStatus != null) {
            queryExpress.add(QueryCriterions.eq(StatsEditorField.VALIDSTATUS, validStatusCode));
            messageMap.put("currentStatusCode", queryStatus.getCode());
        }

        //查找列表
        try {
            PageRows<StatsEditorItem> statsEditorItemRows = ToolsServiceSngl.get().queryStatsEditorItemByPage(queryExpress, page);
            messageMap.put("list", statsEditorItemRows.getRows());
            messageMap.put("page", statsEditorItemRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }

        return new ModelAndView("/editor/gamepage", messageMap);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView contentCreatePage() {
        return new ModelAndView("/editor/gamecreatepage");
    }

    @RequestMapping(value = "/create")
    public ModelAndView createContent(@RequestParam(value = "link") String link) {
        PrivilegeUser currentUser = getCurrentUser();
        Map<String, Object> messageMap = new HashMap<String, Object>();

        List<Map<String, String>> result = RegexUtil.fetch(link, GAME_URL_PATTERN, 1);
        if (CollectionUtil.isEmpty(result)) {
            messageMap.put("link", link);
            messageMap = putErrorMessage(messageMap, "game.url.illegl");
            return new ModelAndView("/editor/gamecreatepage", messageMap);
        }


        GameResource gameResource = null;
        String gameCode = result.get(0).get("1");
        try {
            gameResource = GameResourceServiceSngl.get().getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.GAMECODE, gameCode)));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " getContentById occured ServiceExcpetion.e:", e);
        }

        if (gameResource == null) {
            messageMap.put("link", link);
            messageMap = putErrorMessage(messageMap, "game.not.exists");
            return new ModelAndView("/editor/gamecreatepage", messageMap);
        }

        try {
            StatsEditorItem statsEditorItem = ToolsServiceSngl.get().getStatsEditorItem(new QueryExpress()
                    .add(QueryCriterions.eq(StatsEditorItemField.SOURCEID, String.valueOf(gameResource.getResourceId())))
                    .add(QueryCriterions.eq(StatsEditorItemField.ADMINUNO, currentUser.getUno()))
            );

            //不为空同时为有效状态报错~
            if (statsEditorItem != null && statsEditorItem.getValidStatus().equals(ValidStatus.VALID)) {
                messageMap.put("link", link);
                messageMap = putErrorMessage(messageMap, "content.editor.has.exists");
                return new ModelAndView("/editor/gamecreatepage", messageMap);
            }

            if (statsEditorItem == null) {
                statsEditorItem = new StatsEditorItem();
                statsEditorItem.setSourceId(String.valueOf(gameResource.getResourceId()));
                statsEditorItem.setAdminUno(currentUser.getUno());
                statsEditorItem.setItemSrcNo(link);
                statsEditorItem.setItemType(EditorStatsItemType.GAME);
                statsEditorItem.setCreateDate(gameResource.getCreateDate());
                statsEditorItem.setCreateIp(getIp());
                statsEditorItem.setValidStatus(ValidStatus.VALID);
                ToolsServiceSngl.get().createStatsEditorItem(statsEditorItem);
            } else {
                UpdateExpress updateExpress = new UpdateExpress()
                        .set(StatsEditorItemField.VALIDSTATUS, ValidStatus.VALID.getCode());

                QueryExpress queryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(StatsEditorItemField.ADMINUNO, currentUser.getUno()))
                        .add(QueryCriterions.eq(StatsEditorItemField.SOURCEID, String.valueOf(gameResource.getResourceId())));
                ToolsServiceSngl.get().modifyStatsEditorItem(updateExpress, queryExpress);
            }
        } catch (ServiceException e) {
            GAlerter.lab(" insert modify editor Item occured ServiceException.e:", e);
            messageMap.put("link", link);
            messageMap = putErrorMessage(messageMap, "system.error");
            return new ModelAndView("/editor/gamecreatepage", messageMap);
        }


        return new ModelAndView("redirect:/editor/game/page");
    }

    @RequestMapping(value = "/del")
    public ModelAndView deleteContent(@RequestParam(value = "itemno") String itemno) {
        UpdateExpress updateExpress = new UpdateExpress()
                .set(StatsEditorItemField.VALIDSTATUS, ValidStatus.REMOVED.getCode());

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(StatsEditorItemField.ITEMNO, itemno));
        try {
            ToolsServiceSngl.get().modifyStatsEditorItem(updateExpress, queryExpress);
        } catch (ServiceException e) {
            GAlerter.lab(" insert modify editor Item occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/editor/game/page");
    }

//    @RequestMapping(value = "/modifypage")
//    public ModelAndView modifyContent(@RequestParam(value = "itemno") String itemno) {
//        Map<String, Object> messageMap = new HashMap<String, Object>();
//
//        QueryExpress queryExpress = new QueryExpress()
//                .add(QueryCriterions.eq(StatsEditorItemField.ITEMNO, itemno));
//
//        try {
//            StatsEditorItem editorItem = ToolsServiceSngl.get().getStatsEditorItem(queryExpress);
//            messageMap.put("editorItem", editorItem);
//        } catch (ServiceException e) {
//            GAlerter.lab(" insert modify editor Item occured ServiceException.e:", e);
//            messageMap = putErrorMessage(messageMap, "system.error");
//            return new ModelAndView("/editor/gamemodifypage", messageMap);
//        }
//
//        return new ModelAndView("/editor/gamemodifypage", messageMap);
//    }
//
//    @RequestMapping(value = "/modify")
//    public ModelAndView modifyContent(@RequestParam(value = "itemno") String itemno,
//                                      @RequestParam(value = "subtype", required = false) String subType) {
//        UpdateExpress updateExpress = new UpdateExpress()
//                .set(StatsEditorItemField.ITEMSUBTYPE, subType)
//                .set(StatsEditorItemField.VALIDSTATUS, ValidStatus.VALID.getCode());
//
//        QueryExpress queryExpress = new QueryExpress()
//                .add(QueryCriterions.eq(StatsEditorItemField.ITEMNO, itemno));
//        try {
//            ToolsServiceSngl.get().modifyStatsEditorItem(updateExpress, queryExpress);
//        } catch (ServiceException e) {
//            GAlerter.lab(" insert modify editor Item occured ServiceException.e:", e);
//        }
//        return new ModelAndView("redirect:/editor/game/page");
//    }
}
