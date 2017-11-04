package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.NewRelease;
import com.enjoyf.platform.service.gameres.NewReleaseField;
import com.enjoyf.platform.service.message.Message;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.message.MessageType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareBaseInfoField;
import com.enjoyf.platform.service.sync.ShareType;
import com.enjoyf.platform.service.sync.SyncServiceSngl;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.game.NewReleaseDTO;
import com.enjoyf.webapps.tools.weblogic.gameres.NewReleaseWebLogic;
import com.enjoyf.webapps.tools.weblogic.mail.MailEngine;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-22
 * Time: 上午9:04
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
@Controller
@RequestMapping(value = "/gameresource/newrelease")
public class NewReleaseController extends ToolsBaseController {

    private Logger logger = LoggerFactory.getLogger(NewReleaseController.class);

    @Resource(name = "newGameInfoWebLogic")
    private NewReleaseWebLogic newReleaseWebLogic;

    @Resource(name = "mailEngine")
    private MailEngine mailEngine;

    private TemplateHotdeployConfig config = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "status", required = false) String validStatus) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(NewReleaseField.DISPLAY_ORDER, QuerySortOrder.ASC));
        if (validStatus != null && !StringUtil.isEmpty(validStatus)) {
            queryExpress.add(QueryCriterions.eq(NewReleaseField.VALID_STATUS, validStatus));
        }
        mapMessage.put("validStatus", validStatus);
        try {
            PageRows<NewReleaseDTO> pageRows = newReleaseWebLogic.queryNewGameInfoDTO(queryExpress, pagination);
            if (pageRows == null) {
                return new ModelAndView("/gameresource/newreleaselist");
            }

            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("list", pageRows.getRows());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/newreleaselist");
        }
        return new ModelAndView("/gameresource/newreleaselist", mapMessage);
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "infoid", required = true) Long newGameInfoId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (newGameInfoId == null) {
            return new ModelAndView("redirect:/gameresource/newrelease/list");
        }
        try {
            List<ShareBaseInfo> shareBaseInfoList = SyncServiceSngl.get().queryShareInfo(new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHARETYPE, ShareType.NEW_GAME.getCode())));
            if (!CollectionUtil.isEmpty(shareBaseInfoList)) {
                mapMessage.put("shareBaseInfoList", shareBaseInfoList);
            }

            NewReleaseDTO dto = newReleaseWebLogic.getNewGameInfoDTO(newGameInfoId);
            if (dto == null) {
                return new ModelAndView("redirect:/gameresource/newrelease/list");
            }
            mapMessage.put("dto", dto);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/newreleaselist");
        }
        return new ModelAndView("/gameresource/newreleasedetail", mapMessage);
    }

    @RequestMapping(value = "/verify")
    public ModelAndView verify(@RequestParam(value = "infoid", required = true) Long newGameInfoId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (newGameInfoId == null) {
            return new ModelAndView("redirect:/gameresource/newrelease/list");
        }
        try {
            NewRelease info = GameResourceServiceSngl.get().getNewGameInfo(newGameInfoId);
            if (info == null) {
                return new ModelAndView("redirect:/gameresource/newrelease/list");
            }

            TemplateHotdeployConfig templateConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);
            String notice = templateConfig.getGameNewReleasePassTemplate();

            Date date = new Date();
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(NewReleaseField.VALID_STATUS, ValidStatus.VALID.getCode());
            updateExpress.set(NewReleaseField.VERIFY_DATE, date);
            updateExpress.set(NewReleaseField.NOTICE, notice);

            Boolean bool = GameResourceServiceSngl.get().modifyNewGameInfo(newGameInfoId, updateExpress);

            if (bool) {
                info = GameResourceServiceSngl.get().getNewGameInfo(newGameInfoId);
                //站内通知
                pushNotice(info);
                //发邮件
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/newreleaselist");
        }

        return new ModelAndView("redirect:/gameresource/newrelease/list", mapMessage);
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "infoid", required = true) Long newGameInfoId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (newGameInfoId == null) {
            return new ModelAndView("redirect:/gameresource/newrelease/list");
        }

        try {
            Date date = new Date();
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(NewReleaseField.VALID_STATUS, ValidStatus.REMOVED.getCode());

            Boolean bool = GameResourceServiceSngl.get().modifyNewGameInfo(newGameInfoId, updateExpress);

            if (bool) {
                ToolsLog log = new ToolsLog();
                log.setOperType(LogOperType.REMOVE_NEW_GAME_INFO);
                log.setOpTime(date);
                log.setOpIp(getIp());
                log.setOpAfter("newGameInfo.newGameInfoId:" + newGameInfoId);
                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/newreleaselist");
        }

        return new ModelAndView("redirect:/gameresource/newrelease/list", mapMessage);
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "infoid", required = true) Long newGameInfoId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (newGameInfoId == null) {
            return new ModelAndView("redirect:/gameresource/newrelease/list?status=valid");
        }
        try {
            newReleaseWebLogic.newGameInfoSort(newGameInfoId, sort);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/gameresource/newrelease/list?status=valid");
        }
        return new ModelAndView("redirect:/gameresource/newrelease/list?status=valid");
    }

    private void pushNotice(NewRelease info) throws ServiceException {
        Map<String, String> paramMap = new HashMap<String, String>();

        String noticeTemplate = info.getNotice();
        paramMap.put("newgamename", info.getNewGameName());
        paramMap.put("infoid", ""+info.getNewReleaseId());
        String messageBody = NamedTemplate.parse(noticeTemplate).format(paramMap);

        Message message = new Message();
        message.setBody(messageBody);
        message.setMsgType(MessageType.OPERATION);
        message.setOwnUno(info.getCreateUno());
        message.setRecieverUno(info.getCreateUno());
        message.setSendDate(new Date());

        MessageServiceSngl.get().postMessage(info.getCreateUno(), message);
    }

}
