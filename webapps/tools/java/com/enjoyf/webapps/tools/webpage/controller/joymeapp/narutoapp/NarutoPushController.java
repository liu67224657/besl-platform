package com.enjoyf.webapps.tools.webpage.controller.joymeapp.narutoapp;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.PushMessageDTO;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-1
 * Time: 上午9:17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/narutoapp/push")
public class NarutoPushController extends ToolsBaseController {

    private static final String TV_SERIES_APP_KEY = "1wVnuqR5R0PFRk0K7Jj1ho";

    @Resource(name = "jomyeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    private static Set<Integer> platformSet = new HashSet<Integer>();

    private static Set<Integer> redirectTypes = new HashSet<Integer>();

    static {
        platformSet.add(AppPlatform.ANDROID.getCode());
        platformSet.add(AppPlatform.IOS.getCode());
    }

    static {
        redirectTypes.add(0);
        redirectTypes.add(1);
        redirectTypes.add(2);
        redirectTypes.add(3);
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("appId", TV_SERIES_APP_KEY);

        int curPage = pageStartIndex / pageSize + 1;

        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(PushMessageField.APPKEY, TV_SERIES_APP_KEY))
                .add(QueryCriterions.eq(PushMessageField.PUSHLISTTYPE, PushListType.PUSH_MESSAGE.getCode()))
                .add(QueryCriterions.ne(PushMessageField.PUSHSTATUS, ActStatus.ACTED.getCode()))
                .add(QuerySort.add(PushMessageField.PUSHMSGID, QuerySortOrder.DESC));
        try {

            PageRows<PushMessageDTO> pageRows = joymeAppWebLogic.queryPushMessageDTO(queryExpress, pagination);

            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/joymeapp/tv/push/pushlist", mapMessage);
    }


    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPushPage(@RequestParam(value = "icon", required = false) String icon,
                                       @RequestParam(value = "subject", required = false) String subject,
                                       @RequestParam(value = "shortmessage", required = false) String shortMessage,
                                       @RequestParam(value = "info", required = false) String info,
                                       @RequestParam(value = "platform", required = false) Integer platform,
                                       @RequestParam(value = "rtype", required = false) Integer redirectType,
                                       @RequestParam(value = "pushchannel", required = false) Integer pushChannel,
                                       @RequestParam(value = "channeltype", required = false) String channelType,
                                       @RequestParam(value = "version", required = false) String version,
                                       @RequestParam(value = "devices", required = false) String devices,
                                       @RequestParam(value = "tags", required = false) String tags,
                                       @RequestParam(value = "badge", required = false) Integer badge,
                                       @RequestParam(value = "sound", required = false) String sound) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("icon", icon);
        mapMessage.put("subject", subject);
        mapMessage.put("shortMessage", shortMessage);
        mapMessage.put("info", info);
        mapMessage.put("platform", platform);
        mapMessage.put("redirectType", redirectType);
        mapMessage.put("pushChannel", pushChannel);
        mapMessage.put("channelType", channelType);
        mapMessage.put("version", version);
        mapMessage.put("devices", devices);
        mapMessage.put("tags", tags);
        mapMessage.put("badge", badge == null ? 0 : badge);
        mapMessage.put("sound", sound);

        mapMessage.put("redirectTypes", AnimeRedirectType.getAll());

        mapMessage.put("platforms", platformSet);
        mapMessage.put("pushChannels", AppPushChannel.getAll());
        mapMessage.put("channelTypes", AppChannelType.getAll());

        return new ModelAndView("/joymeapp/tv/push/createpush", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView createMessage(@RequestParam(value = "icon", required = false) String icon,
                                      @RequestParam(value = "subject", required = false) String subject,
                                      @RequestParam(value = "shortmessage", required = false) String shortMessage,
                                      @RequestParam(value = "info", required = false) String info,
                                      @RequestParam(value = "platform", required = false) Integer platform,
                                      @RequestParam(value = "rtype", required = false) Integer redirectType,
                                      @RequestParam(value = "pushchannel", required = false, defaultValue = "0") Integer pushChannel,
                                      @RequestParam(value = "channeltype", required = false) String channelType,
                                      @RequestParam(value = "version", required = false) String version,
                                      @RequestParam(value = "devices", required = false) String devices,
                                      @RequestParam(value = "tags", required = false) String tags,
                                      @RequestParam(value = "badge", required = false) Integer badge,
                                      @RequestParam(value = "sound", required = false) String sound) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("icon", icon);
        mapMessage.put("subject", subject);
        mapMessage.put("shortMessage", shortMessage);
        mapMessage.put("info", info);
        mapMessage.put("platform", platform);
        mapMessage.put("redirectType", redirectType);
        mapMessage.put("pushChannel", pushChannel);
        mapMessage.put("channelType", channelType);
        mapMessage.put("version", version);
        mapMessage.put("devices", devices);
        mapMessage.put("tags", tags);
        mapMessage.put("badge", badge == null ? 0 : badge);
        mapMessage.put("sound", sound);

        try {
            AppPlatform appPlatform = AppPlatform.getByCode(platform);

            PushMessage pushMessage = new PushMessage();
            Date date = new Date();
            pushMessage.setAppKey(TV_SERIES_APP_KEY);
            pushMessage.setAppPlatform(appPlatform);
            pushMessage.setCreateDate(date);
            pushMessage.setCreateUserid(getCurrentUser().getUserid());
            pushMessage.setMsgIcon(icon);
            pushMessage.setMsgSubject(subject);
            pushMessage.setPushStatus(ActStatus.UNACT);
            pushMessage.setShortMessage(shortMessage);
            pushMessage.setPushListType(PushListType.PUSH_MESSAGE);
            pushMessage.setPushChannel(AppPushChannel.getByCode(pushChannel));
            pushMessage.setAppChannel(channelType);
            pushMessage.setAppVersion(version);
            pushMessage.setTags(tags);
            pushMessage.setDevices(devices);
            pushMessage.setSound(sound);
            pushMessage.setBadge(badge == null ? 0 : badge);
            pushMessage.setSendDate(date);
            PushMessageOptions options = new PushMessageOptions();

            PushMessageOption pushMessageOption = new PushMessageOption();
            pushMessageOption.setInfo(info);
            pushMessageOption.setType(redirectType);

            options.getList().add(pushMessageOption);
            options.setTemplate(0);
            pushMessage.setOptions(options);

            MessageServiceSngl.get().ceatePushMessage(pushMessage);

            //addLog
            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.CREATE_JOYMEAPP_PUSHMESSAGE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("create push message:" + pushMessage);

            addLog(log);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("iconError", "error.exception");
            return new ModelAndView("redirect:/tv/push/list", mapMessage);
        }
        return new ModelAndView("redirect:/tv/push/list");
    }

    @RequestMapping(value = "/send")
    public ModelAndView sendMessage(@RequestParam(value = "msgid", required = true) Long msgId,
                                    @RequestParam(value = "pushType", required = true) String pushType) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            PushMessage pushMessage = MessageServiceSngl.get().getPushMessage(new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)));
            if (pushMessage == null) {
                return new ModelAndView("redirect:/tv/push/list");
            }
            UpdateExpress updateExpress = new UpdateExpress();
            if (pushType.equals("E")) {
                if (pushMessage.getE_sendStatus().equals(ActStatus.ACTING)) {
                    return new ModelAndView("redirect:/tv/push/list");
                }
                updateExpress.set(PushMessageField.E_SEND_STATUS, ActStatus.ACTING.getCode());
            } else {
                if (pushMessage.getSendStatus().equals(ActStatus.ACTING)) {
                    return new ModelAndView("redirect:/tv/push/list");
                }
                updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.ACTING.getCode());
            }
            updateExpress.set(PushMessageField.SENDDATE, new Date());

            MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                    .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMessage.getPushMsgId())), pushMessage.getAppKey());

            pushMessage.setAppKey(pushMessage.getAppKey() + pushType);
//            AppPushFactory.get().factory(pushMessage.getPushChannel(), pushMessage.getAppPlatform()).sendPushMessage(pushMessage);


            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.SEND_JOYMEAPP_PUSHMESSAGE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("create push message:" + pushMessage);

            addLog(log);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e:", e);
            mapMessage.put("errorMsg", "error.exception");
            new ModelAndView("redirect:/tv/push/list");
        }

        return new ModelAndView("redirect:/tv/push/list");
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "msgid", required = true) Long pushMsgId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("platforms", platformSet);
        mapMessage.put("pushChannels", AppPushChannel.getAll());
        mapMessage.put("channelTypes", AppChannelType.getAll());
        try {
            PushMessageDTO pushMessageDTO = joymeAppWebLogic.getPushMessageDTO(new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMsgId)));
            mapMessage.put("msgDTO", pushMessageDTO);

            mapMessage.put("redirectTypes", AnimeRedirectType.getAll());

            String appId = TV_SERIES_APP_KEY;
            if (AppPlatform.IOS.equals(pushMessageDTO.getPushMessage().getAppPlatform())) {
                appId += "I";
            } else if (AppPlatform.ANDROID.equals(pushMessageDTO.getPushMessage().getAppPlatform())) {
                appId += "A";
            }
            //todo
//            List<String> versionList = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getPushVersionList(appId, pushMessage.getAppPlatform(), AppPushChannel.getByCode(umengPushChannel));
//            mapMessage.put("versionList", versionList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage.put("errorMsg", "error.exception");
            new ModelAndView("redirect:/tv/push/list", mapMessage);
        }

        return new ModelAndView("/joymeapp/tv/push/modifypush", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "msgid", required = true) Long msgId,
                               @RequestParam(value = "icon", required = false) String icon,
                               @RequestParam(value = "subject", required = false) String subject,
                               @RequestParam(value = "shortmessage", required = false) String shortMessage,
                               @RequestParam(value = "info", required = false) String info,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "rtype", required = false) Integer redirectType,
                               @RequestParam(value = "screennames", required = false) String screenNames,
                               @RequestParam(value = "pushchannel", required = false) Integer pushChannel,
                               @RequestParam(value = "channeltype", required = false) String channelType,
                               @RequestParam(value = "version", required = false) String version,
                               @RequestParam(value = "devices", required = false) String devices,
                               @RequestParam(value = "tags", required = false) String tags,
                               @RequestParam(value = "badge", required = false) Integer badge,
                               @RequestParam(value = "sound", required = false) String sound) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            PushMessageOptions options = new PushMessageOptions();
            PushMessageOption pushMessageOption = new PushMessageOption();
            pushMessageOption.setInfo(info);
            pushMessageOption.setType(redirectType);
            options.getList().add(pushMessageOption);
            options.setTemplate(0);

            UpdateExpress updateExpress = new UpdateExpress().set(PushMessageField.MSGICON, icon)
                    .set(PushMessageField.MSGSUBJECT, subject)
                    .set(PushMessageField.SHORTMESSAGE, shortMessage)
                    .set(PushMessageField.PLATFORM, platform)
                    .set(PushMessageField.OPTIONS, options.toJson())
                    .set(PushMessageField.PUSH_CHANNEL, pushChannel)
                    .set(PushMessageField.CHANNEL_TYPE, channelType)
                    .set(PushMessageField.VERSION, version)
                    .set(PushMessageField.DEVICES, devices)
                    .set(PushMessageField.TAGS, tags)
                    .set(PushMessageField.BADGE, badge)
                    .set(PushMessageField.SOUND, sound)
                    .set(PushMessageField.MODIFY_DATE, new Date())
                    .set(PushMessageField.MODIFY_USER_ID, getCurrentUser().getUserid());

            boolean bVal = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)), TV_SERIES_APP_KEY);

            if (bVal) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.MODIFY_JOYMEAPP_PUSHMESSAGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify push msgId:" + msgId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage.put("iconError", "error.exception");
            return new ModelAndView("redirect:/tv/push/list");
        }
        return new ModelAndView("redirect:/tv/push/list");
    }

    //
    @RequestMapping(value = "/remove")
    public ModelAndView deleteMessage(@RequestParam(value = "msgid", required = true) Long msgId,
                                      @RequestParam(value = "status", required = true) String status) {
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(PushMessageField.PUSHSTATUS, ActStatus.getByCode(status).getCode());

        try {
            boolean bVal = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)), TV_SERIES_APP_KEY);

            if (bVal) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.DELETE_JOYMEAPP_PUSHMESSAGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter("modify push msgId:" + msgId);

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        }

        return new ModelAndView("redirect:/tv/push/list");
    }

    @RequestMapping(value = "/getversion")
    public ModelAndView getVersion(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "platform", required = false) Integer platform
    ) {
        ResultObjectMsg resultObjectMsg = new ResultObjectMsg(ResultObjectMsg.CODE_S);
        JsonBinder binder = JsonBinder.buildNormalBinder();
        try {
            if (platform == null) {
                resultObjectMsg.setRs(ResultObjectMsg.CODE_E);
                HTTPUtil.writeJson(response, binder.toJson(resultObjectMsg));
                return null;
            }
            String appId = TV_SERIES_APP_KEY;
            if (platform == 0) {
                appId += "I";
            } else if (platform == 1) {
                appId += "A";
            }
            //todo
//            List<String> versionList = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getPushVersionList(appId, pushMessage.getAppPlatform(), AppPushChannel.getByCode(umengPushChannel));

            resultObjectMsg.setRs(ResultObjectMsg.CODE_S);
//            resultObjectMsg.setResult(versionList);
            HTTPUtil.writeJson(response, binder.toJson(resultObjectMsg));
            return null;
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        return null;
    }
}
