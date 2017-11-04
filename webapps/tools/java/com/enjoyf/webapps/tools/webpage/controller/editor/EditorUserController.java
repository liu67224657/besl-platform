package com.enjoyf.webapps.tools.webpage.controller.editor;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.*;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-3-28
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/editor/user")
public class EditorUserController extends EditorBaseController {

    @RequestMapping(value = "/active")
    public ModelAndView activeUser(@RequestParam(value = "rurl", required = false) String rurl) {
        PrivilegeUser currentUser = getCurrentUser();

        Map<String, Object> messageMap = new HashMap<String, Object>();

        StatsEditor statsEditor = null;
        try {
            statsEditor = ToolsServiceSngl.get().getStatsEditor(new QueryExpress().add(QueryCriterions.eq(StatsEditorField.ADMINUNO, currentUser.getUno())));

            //不为空且无效 改状态 为空insert创建编辑管理员 如果为空且有效状态不需要激活
            if (statsEditor != null && !statsEditor.getValidStatus().equals(ValidStatus.VALID)) {
                UpdateExpress updateExpress = new UpdateExpress().set(StatsEditorField.VALIDSTATUS, ValidStatus.VALID.getCode());

                QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(StatsEditorField.ADMINUNO, currentUser.getUno()));

                ToolsServiceSngl.get().modifyStatsEditor(updateExpress, queryExpress);
            } else if (statsEditor == null) {
                statsEditor = new StatsEditor();
                statsEditor.setAdminUno(currentUser.getUno());
                statsEditor.setCreateDate(new Date());
                statsEditor.setCreateIp(getIp());
                statsEditor.setEditorDesc("");
                statsEditor.setEditorName(currentUser.getUsername());
                statsEditor.setValidStatus(ValidStatus.VALID);
                ToolsServiceSngl.get().createStatsEditor(statsEditor);
            } else {
                return new ModelAndView("/editor/activepage", putErrorMessage(messageMap, "editor.active.exists"));
            }


        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("/editor/activepage", putErrorMessage(messageMap, "system.error"));
        }

        return new ModelAndView("redirect:" + (StringUtil.isEmpty(rurl) ? "/editor/content/page" : rurl));
    }


    @RequestMapping(value = "/page")
    public ModelAndView userPage(
            @RequestParam(value = "username", required = false) String userName,
            @RequestParam(value = "status", required = false) String validStatusCode,
            @RequestParam(value = "items", required = false, defaultValue = "0") Integer items,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pagerOffset,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer maxPageItems) {

        Map<String, Object> messageMap = new HashMap<String, Object>();

        messageMap.put("statusList",ValidStatus.getAll());

        PrivilegeUser currentUser = getCurrentUser();

        StatsEditor statsEditor = null;
        try {
            statsEditor = ToolsServiceSngl.get().getStatsEditor(new QueryExpress()
                    .add(QueryCriterions.eq(StatsEditorField.ADMINUNO, currentUser.getUno()))
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


        QueryExpress queryExpress = new QueryExpress();
        if (!StringUtil.isEmpty(userName)) {
            queryExpress.add(QueryCriterions.eq(StatsEditorField.EDITORNAME, userName));
        }

        Pagination page = new Pagination(items, (pagerOffset / maxPageItems) + 1, maxPageItems);
       //有查询的状态条件
        ValidStatus queryStatus=ValidStatus.getByCode(validStatusCode);
        if(queryStatus!=null){
            queryExpress.add(QueryCriterions.eq(StatsEditorField.VALIDSTATUS, validStatusCode));
            messageMap.put("currentStatusCode", queryStatus.getCode());
        }

        messageMap.put("username",userName);

        //查找列表
        try {
            PageRows<StatsEditor> statsEditorItemRows= ToolsServiceSngl.get().queryStatsEditorByPage(queryExpress, page);
            messageMap.put("list", statsEditorItemRows.getRows());
            messageMap.put("page", statsEditorItemRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
        }

        return new ModelAndView("/editor/userpage", messageMap);
    }

    @RequestMapping(value = "/del")
    public ModelAndView delUser(
            @RequestParam(value = "adminuno", required = false) Integer adminUno) {
        UpdateExpress updateExpress = new UpdateExpress()
                .set(StatsEditorField.VALIDSTATUS, ValidStatus.REMOVED.getCode());

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(StatsEditorField.ADMINUNO, adminUno));
        try {
            ToolsServiceSngl.get().modifyStatsEditor(updateExpress, queryExpress);
        } catch (ServiceException e) {
            GAlerter.lab(" insert modify editor Item occured ServiceException.e:", e);
        }

        return new ModelAndView("redirect:/editor/user/page");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView moidfyUserPage(
            @RequestParam(value = "adminuno", required = false) Integer adminUno) {
        Map<String, Object> messageMap = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(StatsEditorField.ADMINUNO, adminUno));

        try {
            StatsEditor editor = ToolsServiceSngl.get().getStatsEditor(queryExpress);
            messageMap.put("editor", editor);
        } catch (ServiceException e) {
            GAlerter.lab(" insert modify editor Item occured ServiceException.e:", e);
            messageMap = putErrorMessage(messageMap, "system.error");
            return new ModelAndView("/editor/usermodifypage", messageMap);
        }

        return new ModelAndView("/editor/usermodifypage", messageMap);
    }


    @RequestMapping(value = "/modify")
    public ModelAndView modifyContent(@RequestParam(value = "adminuno") Integer adminUno,
                                      @RequestParam(value = "username", required = false) String userName,
                                      @RequestParam(value = "userdesc", required = false) String userDesc) {
        UpdateExpress updateExpress = new UpdateExpress()
                .set(StatsEditorField.EDITORNAME, userName)
                .set(StatsEditorField.EDITORDESC, userDesc);

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(StatsEditorField.ADMINUNO, adminUno));
        try {
            ToolsServiceSngl.get().modifyStatsEditor(updateExpress, queryExpress);
        } catch (ServiceException e) {
            GAlerter.lab(" insert modify editor Item occured ServiceException.e:", e);
        }
        return new ModelAndView("redirect:/editor/user/page");
    }
}
