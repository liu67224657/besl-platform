package com.enjoyf.webapps.tools.webpage.controller.editor;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.stats.StatDateType;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.webapps.tools.weblogic.dto.EditorStatDTO;
import com.enjoyf.webapps.tools.weblogic.dto.EditorStatsContentDTO;
import com.enjoyf.webapps.tools.weblogic.dto.EditorStatsGameDTO;
import com.enjoyf.webapps.tools.weblogic.editor.EditorStatsWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.management.QueryExp;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-3-28
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/editor/user/stat")
public class EditorStatsController extends EditorBaseController {

    @Resource(name = "editorStatsWebLogic")
    private EditorStatsWebLogic editorStatsWebLogic;

    @RequestMapping
    public ModelAndView statUser(
            @RequestParam(value = "statdate", required = false) Date statDate,
            @RequestParam(value = "datetype", required = false) String dateType) {

        Map<String, Object> messageMap = new HashMap<String, Object>();

        //判断是否是编辑
        StatsEditor curentEditor = null;
        PrivilegeUser currentUser = getCurrentUser();
        try {
            curentEditor = ToolsServiceSngl.get().getStatsEditor(new QueryExpress()
                    .add(QueryCriterions.eq(StatsEditorField.ADMINUNO, currentUser.getUno()))
                    .add(QueryCriterions.eq(StatsEditorField.VALIDSTATUS, ValidStatus.VALID.getCode())));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        if (curentEditor == null) {
            messageMap.put("rurl", "/editor/user/stats/content");
            return new ModelAndView("/editor/activepage", messageMap);
        }

        List<StatDateType> dateTypeList = new ArrayList<StatDateType>();
        dateTypeList.add(StatDateType.WEEK);
        dateTypeList.add(StatDateType.MONTH);
        messageMap.put("dateTypeList", dateTypeList);

        if (statDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            statDate = cal.getTime();
        }

        StatDateType statDateType = StatDateType.getByCode(dateType);
        if (statDateType == null) {
            statDateType = StatDateType.WEEK;
            dateType = statDateType.getCode();
        }
        statDate = statDateType.getStartDateByType(statDate);
        messageMap.put("dateType", dateType);
        messageMap.put("statDate", statDate);

        try {
            List<EditorStatDTO> statsList = editorStatsWebLogic.statsEditor(statDateType, statDate);

            messageMap.put("list", statsList);
        } catch (ServiceException e) {
            putErrorMessage(messageMap, "system.error");
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("/editor/userstat", messageMap);
        }

        return new ModelAndView("/editor/userstat", messageMap);
    }

    @RequestMapping(value = "/content")
    public ModelAndView statContent(
            @RequestParam(value = "adminuno", required = false) Integer adminUno,
            @RequestParam(value = "statdate", required = false) Date statDate,
            @RequestParam(value = "datetype", required = false) String dateType) {

        Map<String, Object> messageMap = new HashMap<String, Object>();

        //判断是否是编辑
        StatsEditor curentEditor = null;
        PrivilegeUser currentUser = getCurrentUser();
        try {
            curentEditor = ToolsServiceSngl.get().getStatsEditor(new QueryExpress()
                    .add(QueryCriterions.eq(StatsEditorField.ADMINUNO, currentUser.getUno()))
                    .add(QueryCriterions.eq(StatsEditorField.VALIDSTATUS, ValidStatus.VALID.getCode())));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        if (curentEditor == null) {
            messageMap.put("rurl", "/editor/user/stats/content");
            return new ModelAndView("/editor/activepage", messageMap);
        }

        StatsEditor statsEditor = null;
        try {
            statsEditor = ToolsServiceSngl.get().getStatsEditor(new QueryExpress().add(QueryCriterions.eq(StatsEditorField.ADMINUNO, adminUno)));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            putErrorMessage(messageMap, "system.error");
            return new ModelAndView("/editor/userstatcontent", messageMap);
        }
        if (statsEditor == null) {
            putErrorMessage(messageMap, "stats.editor.not.exists");
            return new ModelAndView("/editor/userstatcontent", messageMap);
        }
        messageMap.put("editor", statsEditor);

        List<StatDateType> dateTypeList = new ArrayList<StatDateType>();
        dateTypeList.add(StatDateType.WEEK);
        dateTypeList.add(StatDateType.MONTH);
        messageMap.put("dateTypeList", dateTypeList);

        if (statDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            statDate = cal.getTime();
        }

        StatDateType statDateType = StatDateType.getByCode(dateType);
        if (statDateType == null) {
            statDateType = StatDateType.WEEK;
            dateType = statDateType.getCode();
        }
        statDate = statDateType.getStartDateByType(statDate);
        messageMap.put("dateType", dateType);
        messageMap.put("statDate", statDate);

        try {
            List<EditorStatsContentDTO> statsList = editorStatsWebLogic.statsEditorContent(statsEditor.getAdminUno(), statDateType, statDate);

            messageMap.put("list", statsList);
        } catch (ServiceException e) {
            putErrorMessage(messageMap, "system.error");
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("/editor/userstatcontent", messageMap);
        }

        return new ModelAndView("/editor/userstatcontent", messageMap);
    }

    @RequestMapping(value = "/game")
    public ModelAndView statGame(
            @RequestParam(value = "adminuno", required = false) Integer adminUno,
            @RequestParam(value = "statdate", required = false) Date statDate,
            @RequestParam(value = "datetype", required = false) String dateType) {

        Map<String, Object> messageMap = new HashMap<String, Object>();

        //判断是否是编辑
        StatsEditor curentEditor = null;
        PrivilegeUser currentUser = getCurrentUser();
        try {
            curentEditor = ToolsServiceSngl.get().getStatsEditor(new QueryExpress()
                    .add(QueryCriterions.eq(StatsEditorField.ADMINUNO, currentUser.getUno()))
                    .add(QueryCriterions.eq(StatsEditorField.VALIDSTATUS, ValidStatus.VALID.getCode())));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }
        if (curentEditor == null) {
            messageMap.put("rurl", "/editor/user/stats/content");
            return new ModelAndView("/editor/activepage", messageMap);
        }

        StatsEditor statsEditor = null;
        try {
            statsEditor = ToolsServiceSngl.get().getStatsEditor(new QueryExpress().add(QueryCriterions.eq(StatsEditorField.ADMINUNO, adminUno)));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            putErrorMessage(messageMap, "system.error");
            return new ModelAndView("/editor/userstatgame", messageMap);
        }
        if (statsEditor == null) {
            putErrorMessage(messageMap, "stats.editor.not.exists");
            return new ModelAndView("/editor/userstatgame", messageMap);
        }
        messageMap.put("editor", statsEditor);

        List<StatDateType> dateTypeList = new ArrayList<StatDateType>();
        dateTypeList.add(StatDateType.WEEK);
        dateTypeList.add(StatDateType.MONTH);
        messageMap.put("dateTypeList", dateTypeList);

        if (statDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            statDate = cal.getTime();
        }

        StatDateType statDateType = StatDateType.getByCode(dateType);
        if (statDateType == null) {
            statDateType = StatDateType.WEEK;
            dateType = statDateType.getCode();
        }
        statDate = statDateType.getStartDateByType(statDate);
        messageMap.put("dateType", dateType);
        messageMap.put("statDate", statDate);

        try {
            List<EditorStatsGameDTO> statsList = editorStatsWebLogic.statsEditorGame(statsEditor.getAdminUno(), statDateType, statDate);

            messageMap.put("list", statsList);
        } catch (ServiceException e) {
            putErrorMessage(messageMap, "system.error");
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("/editor/userstatgame", messageMap);
        }

        return new ModelAndView("/editor/userstatgame", messageMap);
    }

}
