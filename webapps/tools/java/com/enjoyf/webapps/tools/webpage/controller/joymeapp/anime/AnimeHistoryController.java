package com.enjoyf.webapps.tools.webpage.controller.joymeapp.anime;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.PushMessageDTO;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli
 * Date: 2014/12/23
 * Time: 15:15
 */
@Controller
@RequestMapping(value = "/joymeapp/anime/history")
public class AnimeHistoryController extends ToolsBaseController {
    private static final String PIRATES_FANS_APP_KEY = "0G30ZtEkZ4vFBhAfN7Bx4v";//海贼APPKEY
    private static String CODE_APPKEY = "1zBwYvQpt3AE6JsykiA2es"; //火影的APPKEY

    @Resource(name = "jomyeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String appkey,
                             @RequestParam(value = "pushstatus", required = false) String pushstatus,
                             HttpServletRequest request) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appId", appkey);
        int curPage = pageStartIndex / pageSize + 1;

        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(PushMessageField.APPKEY, appkey))
                .add(QueryCriterions.eq(PushMessageField.PUSHLISTTYPE, PushListType.ANIME_HISTORYDAY.getCode()))
                        //.add(QueryCriterions.ne(PushMessageField.PUSHSTATUS, ActStatus.ACTED.getCode()))
                .add(QuerySort.add(PushMessageField.PUSHMSGID, QuerySortOrder.DESC));
        if (!StringUtil.isEmpty(pushstatus)) {
            queryExpress.add(QueryCriterions.ne(PushMessageField.PUSHSTATUS, ActStatus.getByCode("pushstatus").getCode()));
        }
        try {

            PageRows<PushMessageDTO> pageRows = joymeAppWebLogic.queryPushMessageDTO(queryExpress, pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
            mapMessage.put("appkey", appkey);
            mapMessage.put("errorMsg", request.getParameter("errorMsg"));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/anime/history/historylist", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView createMessage(
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "shortmessage", required = false) String shortMessage,
            @RequestParam(value = "info", required = false) String info,
            @RequestParam(value = "rtype", required = false) Integer redirectType,
            @RequestParam(value = "senddate", required = false) String senddate,
            @RequestParam(value = "platform", required = false, defaultValue = "1") Integer platform,
            @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("subject", subject);
        mapMessage.put("shortMessage", shortMessage);
        mapMessage.put("info", info);
        mapMessage.put("redirectType", redirectType);
        mapMessage.put("badge", 0);

        try {
            Date sendDate = DateUtil.StringTodate(senddate, DateUtil.PATTERN_DATE);
            String tags = DateUtil.DateToString(sendDate, DateUtil.PATTERN_DATE);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PushMessageField.APPKEY, appkey));
            queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHLISTTYPE, PushListType.ANIME_HISTORYDAY.getCode()));
            queryExpress.add(QueryCriterions.eq(PushMessageField.TAGS, tags));
            PushMessage pu = MessageServiceSngl.get().getPushMessage(queryExpress);
            if (pu == null) {
                AppPlatform appPlatform = AppPlatform.getByCode(platform);
                PushMessage pushMessage = new PushMessage();
                Date date = new Date();
                pushMessage.setAppKey(appkey);
                pushMessage.setAppPlatform(appPlatform);
                pushMessage.setCreateDate(date);
                pushMessage.setCreateUserid(getCurrentUser().getUserid());
                pushMessage.setMsgSubject(subject);
                pushMessage.setPushStatus(ActStatus.UNACT);//是否删除 默认未删除
                pushMessage.setShortMessage(shortMessage);
                pushMessage.setPushListType(PushListType.ANIME_HISTORYDAY);
                pushMessage.setBadge(0);
                pushMessage.setSendDate(sendDate);
                pushMessage.setTags(tags);
                PushMessageOptions options = new PushMessageOptions();

                PushMessageOption pushMessageOption = new PushMessageOption();
                pushMessageOption.setInfo(info);
                pushMessageOption.setType(redirectType);

                options.getList().add(pushMessageOption);
                options.setTemplate(0);
                pushMessage.setOptions(options);

                pu = MessageServiceSngl.get().ceatePushMessage(pushMessage);
            } else {
                mapMessage.put("errorMsg", "已存在该日期");
            }


            writeToolsLog(LogOperType.ANIME_HISTORY_ADD, "messageid:" + pu.getPushMsgId());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("iconError", "error.exception");
            return new ModelAndView("redirect:/joymeapp/anime/history/list", mapMessage);
        }
        mapMessage.put("appkey", appkey);
        return new ModelAndView("redirect:/joymeapp/anime/history/list", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPushPage(
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "shortmessage", required = false) String shortMessage,
            @RequestParam(value = "info", required = false) String info,
            @RequestParam(value = "rtype", required = false) Integer redirectType,
            @RequestParam(value = "appkey", required = false) String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("subject", subject);
        mapMessage.put("shortMessage", shortMessage);
        mapMessage.put("info", info);
        mapMessage.put("redirectType", redirectType);
        mapMessage.put("badge", 0);

        mapMessage.put("redirectTypes", AnimeRedirectType.getAll());
        mapMessage.put("pushChannels", AppPushChannel.getAll());
        mapMessage.put("channelTypes", AppChannelType.getAll());
        mapMessage.put("appkey", appkey);

        return new ModelAndView("/joymeapp/anime/history/createhistory", mapMessage);
    }


    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "msgid", required = true) Long pushMsgId,
                                   @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("pushChannels", AppPushChannel.getAll());
        mapMessage.put("channelTypes", AppChannelType.getAll());
        mapMessage.put("appkey", appkey);
        try {
            PushMessageDTO pushMessageDTO = joymeAppWebLogic.getPushMessageDTO(new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMsgId)));
            mapMessage.put("msgDTO", pushMessageDTO);
            mapMessage.put("redirectTypes", AnimeRedirectType.getAll());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage.put("errorMsg", "error.exception");
            new ModelAndView("redirect:/joymeapp/anime/history/list", mapMessage);
        }

        return new ModelAndView("/joymeapp/anime/history/modifyhistory", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "msgid", required = true) Long msgId,
                               @RequestParam(value = "subject", required = false) String subject,
                               @RequestParam(value = "shortmessage", required = false) String shortMessage,
                               @RequestParam(value = "info", required = false) String info,
                               @RequestParam(value = "senddate", required = false) String senddate,
                               @RequestParam(value = "rtype", required = false) Integer redirectType,
                               @RequestParam(value = "appkey", required = false, defaultValue = "0G30ZtEkZ4vFBhAfN7Bx4v") String appkey) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            PushMessageOptions options = new PushMessageOptions();
            PushMessageOption pushMessageOption = new PushMessageOption();
            pushMessageOption.setInfo(info);
            pushMessageOption.setType(redirectType);
            options.getList().add(pushMessageOption);
            options.setTemplate(0);

            Date sendDate = DateUtil.StringTodate(senddate, DateUtil.PATTERN_DATE);
            String tags = DateUtil.DateToString(sendDate, DateUtil.PATTERN_DATE);

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PushMessageField.APPKEY, appkey));
            queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHLISTTYPE, PushListType.ANIME_HISTORYDAY.getCode()));
            queryExpress.add(QueryCriterions.eq(PushMessageField.TAGS, tags));
            queryExpress.add(QueryCriterions.ne(PushMessageField.PUSHMSGID, msgId));
            PushMessage pu = MessageServiceSngl.get().getPushMessage(queryExpress);
            if (pu == null) {
                UpdateExpress updateExpress = new UpdateExpress()
                        .set(PushMessageField.MSGSUBJECT, subject)
                        .set(PushMessageField.SHORTMESSAGE, shortMessage)
                        .set(PushMessageField.OPTIONS, options.toJson())
                        .set(PushMessageField.MODIFY_DATE, new Date())
                        .set(PushMessageField.SENDDATE, sendDate)
                        .set(PushMessageField.TAGS, tags)
                        .set(PushMessageField.MODIFY_USER_ID, getCurrentUser().getUserid());

                MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)), appkey);
            } else {
                mapMessage.put("errorMsg", "已存在该日期");
            }
            writeToolsLog(LogOperType.ANIME_HISTORY_UPDATE, "messageid:" + msgId);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("iconError", "error.exception");
            return new ModelAndView("redirect:/joymeapp/anime/history/list");
        }
        mapMessage.put("appkey", appkey);
        return new ModelAndView("redirect:/joymeapp/anime/history/list", mapMessage);
    }

    //
    @RequestMapping(value = "/remove")
    public ModelAndView deleteMessage(@RequestParam(value = "msgid", required = true) Long msgId,
                                      @RequestParam(value = "status", required = true) String status,
                                      @RequestParam(value = "appkey", required = false) String appkey) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(PushMessageField.PUSHSTATUS, ActStatus.getByCode(status).getCode());

        try {
            boolean bval = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)), appkey);
            if (bval) {
                writeToolsLog(LogOperType.ANIME_HISTORY_UPDATE_STATUS, "修改后状态:" + status);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("appkey", appkey);
        return new ModelAndView("redirect:/joymeapp/anime/history/list", map);
    }


}

