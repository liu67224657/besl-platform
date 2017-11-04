package com.enjoyf.webapps.tools.webpage.controller.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.ask.WanbaJt;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.AppPushEvent;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigField;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.message.MessageConstants;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.PushMessageDTO;
import com.enjoyf.webapps.tools.weblogic.joymeapp.JoymeAppWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping(value = "/joymeapp/push")
public class AppPushController extends ToolsBaseController {

    @Resource(name = "jomyeAppWebLogic")
    private JoymeAppWebLogic joymeAppWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "appid", required = false) String appId,
                             @RequestParam(value = "pushlisttype", required = false) String pushListType,
                             @RequestParam(value = "ordertype", required = false) String orderType,
                             @RequestParam(value = "searchtext", required = false) String searchText,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "app", required = false) String app) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("errorMsg", errorMsg);
        mapMessage.put("searchText", StringUtil.isEmpty(searchText) ? "" : searchText);
        mapMessage.put("appId", appId);
        mapMessage.put("pushListType", pushListType);
        mapMessage.put("app", app);

        int curPage = pageStartIndex / pageSize + 1;

        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(PushMessageField.APPKEY, appId));
        if (!StringUtil.isEmpty(pushListType)) {
            if (pushListType.equals("1") || pushListType.equals("5")) {
                queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHLISTTYPE, Integer.valueOf(pushListType)));
            } else if (pushListType.equals("0")) {
                queryExpress.add(QueryCriterions.notIn(PushMessageField.PUSHLISTTYPE, new Integer[]{1, 2}));
            }
            if ("senddate".equals(orderType)) {
                queryExpress.add(QuerySort.add(PushMessageField.SENDDATE, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QuerySort.add(PushMessageField.PUSHMSGID, QuerySortOrder.DESC));
            }
        }
        if (!StringUtil.isEmpty(searchText)) {
            queryExpress.add(QueryCriterions.like(PushMessageField.SHORTMESSAGE, "%" + searchText + "%"));
        }

        queryExpress.add(QueryCriterions.ne(PushMessageField.PUSHSTATUS, ActStatus.ACTED.getCode()));


        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                            .add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC))
            );
            mapMessage.put("appList", appList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        try {
            PageRows<PushMessageDTO> pageRows = joymeAppWebLogic.queryPushMessageDTO(queryExpress, pagination);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        //玩霸-问答
        if (appId.equals("3iiv7VWfx84pmHgCUqRwun") && !StringUtil.isEmpty(app) && "wanbaask".equals(app)) {
            return new ModelAndView("/wanba/pushmessage/pushlist", mapMessage);
        } else if (appId.equals("17yfn24TFexGybOF0PqjdY")) {
            //画报
            return new ModelAndView("/joymeapp/newsclient/pushmessage/pushlist", mapMessage);
        } else if (appId.equals("0VsYSLLsN8CrbBSMUOlLNx")) {
            //咔哒
            return new ModelAndView("/joymeapp/socialclient/pushmessage/pushlist", mapMessage);
        } else if (appId.equals("1wVnuqR5R0PFRk0K7Jj1ho")) {
            //美剧
            return new ModelAndView("/joymeapp/meijuclient/pushmessage/pushlist", mapMessage);
        } else if (appId.equals("1zBwYvQpt3AE6JsykiA2es")) {
            //火影
            return new ModelAndView("/joymeapp/huoyingclient/pushmessage/pushlist", mapMessage);
        } else if (appId.equals("0G30ZtEkZ4vFBhAfN7Bx4v")) {
            //海贼
            return new ModelAndView("/joymeapp/anime/push/pushlist", mapMessage);
        } else if (appId.equals("25AQWaK997Po2x300CQeP0")) {
            //着迷新画报
            return new ModelAndView("/gameclient/pushmessage/pushlist", mapMessage);
        } else {
            return new ModelAndView("/joymeapp/pushmessage/pushlist", mapMessage);
        }
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPushPage(@RequestParam(value = "appid", required = true) String appId,
                                       @RequestParam(value = "pushlisttype", required = false) String pushListType,
                                       @RequestParam(value = "icon", required = false) String icon,
                                       @RequestParam(value = "subject", required = false) String subject,
                                       @RequestParam(value = "shortmessage", required = false) String shortMessage,
                                       @RequestParam(value = "info", required = false) String info,
                                       @RequestParam(value = "platform", required = false) Integer platform,
                                       @RequestParam(value = "rtype", required = false) Integer redirectType,
                                       @RequestParam(value = "screennames", required = false) String screenNames,
                                       @RequestParam(value = "channel", required = false) String channel,
                                       @RequestParam(value = "version", required = false) String version,
                                       @RequestParam(value = "tags", required = false) String tags,
                                       @RequestParam(value = "badge", required = false) Integer badge,
                                       @RequestParam(value = "sound", required = false) String sound,
                                       @RequestParam(value = "senddate", required = false) String sendDate,
                                       @RequestParam(value = "enterprise", required = false) Integer enterprise,
                                       @RequestParam(value = "app", required = false) String app) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appId", appId);
        mapMessage.put("pushListType", pushListType);
        mapMessage.put("app", app);
        mapMessage.put("icon", icon);
        mapMessage.put("subject", subject);
        mapMessage.put("shortMessage", shortMessage);
        mapMessage.put("info", info);
        mapMessage.put("platform", platform);
        mapMessage.put("redirectType", redirectType);
        mapMessage.put("screenNames", screenNames);
        mapMessage.put("channel", channel);
        mapMessage.put("version", version);
        mapMessage.put("tags", tags);
        mapMessage.put("badge", badge == null ? 0 : badge);
        mapMessage.put("sound", sound);
        mapMessage.put("sendDate", sendDate);
        mapMessage.put("enterprise", enterprise);

        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                            .add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC))
            );
            mapMessage.put("appList", appList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        if (appId.equals("3iiv7VWfx84pmHgCUqRwun") && !StringUtil.isEmpty(app) && "wanbaask".equals(app)) {
            mapMessage.put("WanbaJt", WanbaJt.getToolsAll());
            return new ModelAndView("/wanba/pushmessage/createpush", mapMessage);
        } else if (appId.equals("17yfn24TFexGybOF0PqjdY")) {
            return new ModelAndView("/joymeapp/newsclient/pushmessage/createpush", mapMessage);
        } else if (appId.equals("0VsYSLLsN8CrbBSMUOlLNx")) {
            return new ModelAndView("/joymeapp/socialclient/pushmessage/createpush", mapMessage);
        } else if (appId.equals("1wVnuqR5R0PFRk0K7Jj1ho")) {
            return new ModelAndView("/joymeapp/meijuclient/pushmessage/createpush", mapMessage);
        } else if (appId.equals("1zBwYvQpt3AE6JsykiA2es")) {
            return new ModelAndView("/joymeapp/huoyingclient/pushmessage/createpush", mapMessage);
        } else if (appId.equals("0G30ZtEkZ4vFBhAfN7Bx4v")) {
            return new ModelAndView("/joymeapp/anime/push/createpush", mapMessage);
        } else if (appId.equals("25AQWaK997Po2x300CQeP0")) {
            //着迷新画报
            return new ModelAndView("/gameclient/pushmessage/createpush", mapMessage);
        } else {
            return new ModelAndView("/joymeapp/pushmessage/createpush", mapMessage);
        }
    }

    @RequestMapping(value = "/create")
    public ModelAndView createMessage(@RequestParam(value = "appid", required = true) String appId,
                                      @RequestParam(value = "pushlisttype", required = false) Integer pushListType,
                                      @RequestParam(value = "icon", required = false) String icon,
                                      @RequestParam(value = "subject", required = false) String subject,
                                      @RequestParam(value = "shortmessage", required = false) String shortMessage,
                                      @RequestParam(value = "info", required = false) String info,
                                      @RequestParam(value = "platform", required = false) Integer platform,
                                      @RequestParam(value = "rtype", required = false) Integer redirectType,
                                      @RequestParam(value = "screennames", required = false) String screenNames,
                                      @RequestParam(value = "pushchannel", required = false) Integer pushChannel,
                                      @RequestParam(value = "channel", required = false) String channel,
                                      @RequestParam(value = "version", required = false) String version,
                                      @RequestParam(value = "tags", required = false) String tags,
                                      @RequestParam(value = "badge", required = false) Integer badge,
                                      @RequestParam(value = "sound", required = false) String sound,
                                      @RequestParam(value = "senddate", required = false) String sendDate,
                                      @RequestParam(value = "enterprise", required = false) Integer enterprise,
                                      @RequestParam(value = "app", required = false) String app) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appid", appId);
        mapMessage.put("pushlisttype", pushListType);
        mapMessage.put("icon", icon);
        mapMessage.put("subject", subject);
        mapMessage.put("shortmessage", shortMessage);
        mapMessage.put("info", info);
        mapMessage.put("platform", platform);
        mapMessage.put("rtype", redirectType);
        mapMessage.put("screennames", screenNames);
        mapMessage.put("pushchannel", pushChannel);
        mapMessage.put("channel", channel);
        mapMessage.put("version", version);
        mapMessage.put("tags", tags);
        mapMessage.put("badge", badge == null ? 0 : badge);
        mapMessage.put("sound", sound);
        mapMessage.put("senddate", sendDate);
        mapMessage.put("enterprise", enterprise);
        mapMessage.put("app", app);
        try {
            AppPlatform appPlatform = AppPlatform.getByCode(platform);
            String profileIds = "";
            if (!StringUtil.isEmpty(screenNames)) {
                String[] nameArr = screenNames.split(" ");
                for (String name : nameArr) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByNick(name.trim());
                    if (profile != null) {
                        profileIds = profileIds + profile.getProfileId() + " ";
                    }
                }
            }

            if (PushListType.SYSTEM_MESSAGE.equals(PushListType.getByCode(pushListType)) || PushListType.WANB_ASK_SYSTEM_MESSAGE.equals(PushListType.getByCode(pushListType))) {
                PushMessage message = new PushMessage();
                message.setAppKey(appId);
                message.setAppPlatform(appPlatform);
                message.setCreateDate(new Date());
                message.setCreateUserid(getCurrentUser().getUserid());
                message.setMsgIcon(icon);
                message.setMsgSubject(subject);
                message.setPushStatus(ActStatus.UNACT);
                message.setShortMessage(shortMessage);
                message.setPushListType(PushListType.getByCode(pushListType));

                PushMessageOptions options = new PushMessageOptions();
                PushMessageOption pushMessageOption = new PushMessageOption();
                pushMessageOption.setInfo(info);
                pushMessageOption.setType(redirectType);

                options.getList().add(pushMessageOption);
                options.setTemplate(0);
                message.setOptions(options);
                MessageServiceSngl.get().ceatePushMessage(message);
            } else {
                PushMessage pushMessage = new PushMessage();
                pushMessage.setAppKey(appId);
                pushMessage.setAppPlatform(appPlatform);
                pushMessage.setCreateDate(new Date());
                pushMessage.setCreateUserid(getCurrentUser().getUserid());
                pushMessage.setMsgIcon(icon);
                pushMessage.setMsgSubject(subject);
                pushMessage.setPushStatus(ActStatus.UNACT);
                pushMessage.setShortMessage(shortMessage);
                pushMessage.setPushListType(PushListType.getByCode(pushListType));
                pushMessage.setAppChannel(channel);
                pushMessage.setAppVersion(version);
                pushMessage.setTags(tags);
                pushMessage.setSound(sound);
                pushMessage.setBadge(badge == null ? 0 : badge);
                pushMessage.setUnos(profileIds);
                pushMessage.setEnterpriseType(AppEnterpriserType.getByCode(enterprise));
                if (!StringUtil.isEmpty(sendDate)) {
                    Date date = DateUtil.StringTodate(sendDate, DateUtil.PATTERN_DATE_TIME);
                    pushMessage.setSendDate(date);
                }
                pushMessage.setSendStatus(ActStatus.UNACT);

                PushMessageOptions options2 = new PushMessageOptions();

                PushMessageOption pushMessageOption2 = new PushMessageOption();
                pushMessageOption2.setInfo(info);
                pushMessageOption2.setType(redirectType);

                options2.getList().add(pushMessageOption2);
                options2.setTemplate(0);
                pushMessage.setOptions(options2);

                MessageServiceSngl.get().ceatePushMessage(pushMessage);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&errorMsg=system.error" + "&app=" + app);
        }
        return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&app=" + app);
    }

    @RequestMapping(value = "/send")
    public ModelAndView sendMessage(@RequestParam(value = "appid", required = true) String appId,
                                    @RequestParam(value = "pushlisttype", required = false) String pushListType,
                                    @RequestParam(value = "searchtext", required = false) String searchText,
                                    @RequestParam(value = "msgid", required = true) Long msgId,
                                    @RequestParam(value = "app", required = false) String app) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("app", app);
        try {
            PushMessage pushMessage = MessageServiceSngl.get().getPushMessage(new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)));
            if (pushMessage == null || !ActStatus.UNACT.equals(pushMessage.getPushStatus())) {
                return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&app=" + app);
            }
            //准备
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.REJECTED.getCode());
            boolean bool = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                    .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMessage.getPushMsgId())), pushMessage.getAppKey());
            if (bool) {
                //事件  队列
                AppPushEvent appPushEvent = new AppPushEvent();
                appPushEvent.setPushMsgId(pushMessage.getPushMsgId());

                appPushEvent.setAppKey(pushMessage.getAppKey());     //25AQWaK997Po2x300CQeP0   17yfn24TFexGybOF0PqjdY
                appPushEvent.setPlatform(pushMessage.getAppPlatform());
                appPushEvent.setEnterpriseType(pushMessage.getEnterpriseType());
                appPushEvent.setIcon("");
                appPushEvent.setSubject(pushMessage.getMsgSubject());
                appPushEvent.setContext(pushMessage.getShortMessage());
                appPushEvent.setChannel(pushMessage.getAppChannel());
                appPushEvent.setVersion(pushMessage.getAppVersion());
                appPushEvent.setTags(pushMessage.getTags());
                appPushEvent.setSound(pushMessage.getSound());
                appPushEvent.setBadge(0);
                appPushEvent.setProfileIds(pushMessage.getUnos());
                appPushEvent.setSendDate(pushMessage.getSendDate());
                appPushEvent.setJt(pushMessage.getOptions().getList().get(0).getType());
                appPushEvent.setJi(pushMessage.getOptions().getList().get(0).getInfo());
                appPushEvent.setPushListType(pushMessage.getPushListType());
                EventDispatchServiceSngl.get().dispatch(appPushEvent);
            }
            writeToolsLog(LogOperType.SEND_JOYMEAPP_PUSHMESSAGE, "消息推送:" + pushMessage.getAppKey() + "," + pushMessage.getPushMsgId());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + "occured ServiceExcpetion.e:", e);
            new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&errorMsg=system.error" + "&app=" + app);
        }
        return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&app=" + app);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "appid", required = true) String appId,
                                   @RequestParam(value = "pushlisttype", required = false) String pushListType,
                                   @RequestParam(value = "searchtext", required = false) String searchText,
                                   @RequestParam(value = "msgid", required = true) Long pushMsgId,
                                   @RequestParam(value = "app", required = false) String app) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("searchText", searchText);
        mapMessage.put("appId", appId);
        mapMessage.put("pushListType", pushListType);
        mapMessage.put("app", app);
        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                            .add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC))
            );
            mapMessage.put("appList", appList);

            PushMessageDTO pushMessageDTO = joymeAppWebLogic.getPushMessageDTO(new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMsgId)));
            if (pushMessageDTO == null || pushMessageDTO.getPushMessage() == null) {
                return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&app=" + app);
            }
            mapMessage.put("msgDTO", pushMessageDTO);

            String appKey = appId;
            if (appId.equals("25AQWaK997Po2x300CQeP0")) {
                appKey = "17yfn24TFexGybOF0PqjdY";
            }
            int platform = pushMessageDTO.getPushMessage().getAppPlatform().getCode();
            int enterprise = pushMessageDTO.getPushMessage().getEnterpriseType() == null ? AppEnterpriserType.DEFAULT.getCode() : pushMessageDTO.getPushMessage().getEnterpriseType().getCode();
            if (platform == 2) {
                platform = 0;
                enterprise = AppEnterpriserType.ENTERPRISE.getCode();
            }

            Set<String> versionList = new HashSet<String>();
            Set<AppChannelType> channelList = new HashSet<AppChannelType>();

            PushListType pushType = PushListType.getByCode(Integer.valueOf(pushListType));
            if (pushType.equals(PushListType.PUSH_MESSAGE)) {
                int i = 0;
                Pagination pagination = null;
                do {
                    i = i + 1;
                    pagination = new Pagination(200 * i, i, 200);
                    PageRows<AppConfig> appConfigList = JoymeAppConfigServiceSngl.get().queryAppConfigByPage(new QueryExpress()
                            .add(QueryCriterions.eq(AppConfigField.APPKEY, appKey))
                            .add(QueryCriterions.eq(AppConfigField.PLATFORM, platform))
                            .add(QueryCriterions.eq(AppConfigField.ENTERPRISE, enterprise)), pagination);
                    if (appConfigList != null && !CollectionUtil.isEmpty(appConfigList.getRows())) {
                        pagination = appConfigList.getPage();
                        for (AppConfig appConfig : appConfigList.getRows()) {
                            if (appConfig != null) {
                                if (appId.equals("17yfn24TFexGybOF0PqjdY") && !app.equals("wanbaask")) {
                                    if (!StringUtil.isEmpty(appConfig.getVersion()) && (appConfig.getVersion().equals("1.4.0") || appConfig.getVersion().equals("1.4.1") || appConfig.getVersion().equals("1.4.2") || appConfig.getVersion().equals("1.4.3"))) {
                                        versionList.add(appConfig.getVersion());
                                        if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                            if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                                channelList.add(AppChannelType.getByCode(appConfig.getChannel()));
                                            }
                                        }
                                    }
                                    //玩霸
                                } else if (appId.equals("25AQWaK997Po2x300CQeP0")) {
                                    if (!StringUtil.isEmpty(appConfig.getVersion()) && !appConfig.getVersion().equals("1.4.0") && !appConfig.getVersion().equals("1.4.1") && !appConfig.getVersion().equals("1.4.2") && !appConfig.getVersion().equals("1.4.3")) {
                                        versionList.add(appConfig.getVersion());
                                        if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                            if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                                channelList.add(AppChannelType.getByCode(appConfig.getChannel()));
                                            }
                                        }
                                    }
                                } else {
                                    if (!StringUtil.isEmpty(appConfig.getVersion())) {
                                        versionList.add(appConfig.getVersion());
                                    }
                                    if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                        if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                            channelList.add(AppChannelType.getByCode(appConfig.getChannel()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } while (!pagination.isLastPage());
            }
            mapMessage.put("versionList", versionList);
            mapMessage.put("channelList", channelList);

            Set<String> versionSet = new HashSet<String>();
            if (!StringUtil.isEmpty(pushMessageDTO.getPushMessage().getAppVersion())) {
                String[] checkVersionList = pushMessageDTO.getPushMessage().getAppVersion().split("\\|");
                for (String version : checkVersionList) {
                    if (!StringUtil.isEmpty(version)) {
                        versionSet.add(version);
                    }
                }
            }
            mapMessage.put("versionSet", versionSet);

            Set<AppChannelType> channelSet = new HashSet<AppChannelType>();
            if (!StringUtil.isEmpty(pushMessageDTO.getPushMessage().getAppChannel())) {
                String[] checkChannelList = pushMessageDTO.getPushMessage().getAppChannel().split("\\|");
                for (String channel : checkChannelList) {
                    if (!StringUtil.isEmpty(channel)) {
                        channelSet.add(AppChannelType.getByCode(channel));
                    }
                }
            } else {
                channelSet.addAll(channelList);
            }
            mapMessage.put("channelSet", channelSet);

            String screenNames = "";
            if (!StringUtil.isEmpty(pushMessageDTO.getPushMessage().getUnos())) {
                String[] profileIds = pushMessageDTO.getPushMessage().getUnos().split(" ");
                for (String profileId : profileIds) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
                    if (profile != null) {
                        screenNames += (profile.getNick() + " ");
                    }
                }
            }
            mapMessage.put("screenNames", screenNames);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&errorMsg=system.error" + "&app=" + app);
        }
        if (appId.equals("3iiv7VWfx84pmHgCUqRwun") && !StringUtil.isEmpty(app) && "wanbaask".equals(app)) {
            mapMessage.put("WanbaJt", WanbaJt.getToolsAll());
            return new ModelAndView("/wanba/pushmessage/modifypush", mapMessage);
        } else if (appId.equals("17yfn24TFexGybOF0PqjdY")) {
            return new ModelAndView("/joymeapp/newsclient/pushmessage/modifypush", mapMessage);
        } else if (appId.equals("0VsYSLLsN8CrbBSMUOlLNx")) {
            return new ModelAndView("/joymeapp/socialclient/pushmessage/modifypush", mapMessage);
        } else if (appId.equals("1wVnuqR5R0PFRk0K7Jj1ho")) {
            return new ModelAndView("/joymeapp/meijuclient/pushmessage/modifypush", mapMessage);
        } else if (appId.equals("1zBwYvQpt3AE6JsykiA2es")) {
            return new ModelAndView("/joymeapp/huoyingclient/pushmessage/modifypush", mapMessage);
        } else if (appId.equals("0G30ZtEkZ4vFBhAfN7Bx4v")) {
            return new ModelAndView("/joymeapp/anime/push/modifypush", mapMessage);
        } else if (appId.equals("25AQWaK997Po2x300CQeP0")) {
            //着迷新画报
            return new ModelAndView("/gameclient/pushmessage/modifypush", mapMessage);
        } else {
            return new ModelAndView("/joymeapp/pushmessage/modifypush", mapMessage);
        }
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "msgid", required = true) Long pushMsgId,
                               @RequestParam(value = "appid", required = true) String appId,
                               @RequestParam(value = "pushlisttype", required = false) Integer pushListType,
                               @RequestParam(value = "searchtext", required = false) String searchText,
                               @RequestParam(value = "icon", required = false) String icon,
                               @RequestParam(value = "subject", required = false) String subject,
                               @RequestParam(value = "shortmessage", required = false) String shortMessage,
                               @RequestParam(value = "info", required = false) String info,
                               @RequestParam(value = "platform", required = false) Integer platform,
                               @RequestParam(value = "rtype", required = false) Integer redirectType,
                               @RequestParam(value = "screennames", required = false) String screenNames,
                               @RequestParam(value = "channel", required = false) String channel,
                               @RequestParam(value = "version", required = false) String version,
                               @RequestParam(value = "tags", required = false) String tags,
                               @RequestParam(value = "badge", required = false) Integer badge,
                               @RequestParam(value = "sound", required = false) String sound,
                               @RequestParam(value = "senddate", required = false) String sendDate,
                               @RequestParam(value = "enterprise", required = false) Integer enterprise,
                               @RequestParam(value = "app", required = false) String app) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("app", app);
        try {
            String profileIds = "";
            if (!StringUtil.isEmpty(screenNames)) {
                String[] nameArr = screenNames.split(" ");
                for (String name : nameArr) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByNick(name.trim());
                    if (profile != null) {
                        profileIds = profileIds + profile.getProfileId() + " ";
                    }
                }
            }

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
                    .set(PushMessageField.ENTERPRISE_TYPE, enterprise)
                    .set(PushMessageField.OPTIONS, options.toJson())
                    .set(PushMessageField.CHANNEL_TYPE, channel)
                    .set(PushMessageField.VERSION, version)
                    .set(PushMessageField.UNOS, profileIds)
                    .set(PushMessageField.TAGS, tags)
                    .set(PushMessageField.BADGE, badge)
                    .set(PushMessageField.SOUND, sound)
                    .set(PushMessageField.MODIFY_DATE, new Date())
                    .set(PushMessageField.MODIFY_USER_ID, getCurrentUser().getUserid());
            if (!StringUtil.isEmpty(sendDate)) {
                Date date = DateUtil.StringTodate(sendDate, DateUtil.PATTERN_DATE_TIME);
                updateExpress.set(PushMessageField.SENDDATE, date);
            }

            boolean bVal = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMsgId)), appId);

            if (bVal) {
                writeToolsLog(LogOperType.MODIFY_JOYMEAPP_PUSHMESSAGE, "消息通知修改:" + appId + "," + pushMsgId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&errorMsg=system.error" + "&app=" + app);
        }
        return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&app=" + app);
    }

    //
    @RequestMapping(value = "/remove")
    public ModelAndView deleteMessage(@RequestParam(value = "appid", required = true) String appId,
                                      @RequestParam(value = "pushlisttype", required = false) Integer pushListType,
                                      @RequestParam(value = "searchtext", required = false) String searchText,
                                      @RequestParam(value = "msgid", required = true) Long msgId,
                                      @RequestParam(value = "app", required = false) String app) {
        try {
            boolean bVal = MessageServiceSngl.get().removePushMessage(msgId);
            if (bVal) {
                writeToolsLog(LogOperType.DELETE_JOYMEAPP_PUSHMESSAGE, "消息通知删除:" + appId + "," + msgId);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&errorMsg=system.error" + "&app=" + app);
        }
        return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&app=" + app);
    }

    @RequestMapping(value = "/publish")
    public ModelAndView publish(@RequestParam(value = "appid", required = true) String appId,
                                @RequestParam(value = "pushlisttype", required = false) Integer pushListType,
                                @RequestParam(value = "searchtext", required = false) String searchText,
                                @RequestParam(value = "platform", required = false) String platform,
                                @RequestParam(value = "msgid", required = true) Long msgId,
                                @RequestParam(value = "app", required = false) String app) {
        Date date = new Date();
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.ACTING.getCode());
        updateExpress.set(PushMessageField.SENDDATE, date);
        updateExpress.set(PushMessageField.MODIFY_DATE, new Date());
        updateExpress.set(PushMessageField.MODIFY_USER_ID, getCurrentUser().getUserid());
        try {
            boolean bVal = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, msgId)), appId);
            //将消息中心的修改时间保存到redis
            if (bVal && pushListType == PushListType.SYSTEM_MESSAGE.getCode() && !StringUtil.isEmpty(platform)) {
                MessageServiceSngl.get().pushMessageNoticeTime(MessageConstants.WANBA_KEY_MESSAGE_NOTICETIME + platform, date.getTime() + "");
                writeToolsLog(LogOperType.MODIFY_JOYMEAPP_PUSHMESSAGE, "通知发布:" + appId + "," + msgId);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&errorMsg=system.error" + "&app=" + app);
        }
        return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&searchtext=" + searchText + "&app=" + app);
    }

    @ResponseBody
    @RequestMapping(value = "/getversion")
    public String getVersion(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "appid", required = true) String appId,
                             @RequestParam(value = "pushlisttype", required = false) Integer pushListType,
                             @RequestParam(value = "platform", required = false) Integer platform,
                             @RequestParam(value = "enterprise", required = false) Integer enterprise
    ) {
        if (StringUtil.isEmpty(appId) || platform == null || pushListType == null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", ResultCodeConstants.ERROR.getCode());
            jsonObject.put("msg", ResultCodeConstants.ERROR.getMsg());
            jsonObject.put("result", new JSONObject());
            return jsonObject.toString();
        }
        String appKey = appId;
        if (appId.equals("25AQWaK997Po2x300CQeP0")) {
            appKey = "17yfn24TFexGybOF0PqjdY";
        }
        try {
            Set<String> versionSet = new HashSet<String>();
            Set<AppChannelType> channelSet = new HashSet<AppChannelType>();

            PushListType pushType = PushListType.getByCode(pushListType);
            if (pushType.equals(PushListType.PUSH_MESSAGE) || pushType.equals(PushListType.WANB_ASK_SYSTEM_MESSAGE)) {
                int i = 0;
                Pagination pagination = null;
                do {
                    i = i + 1;
                    pagination = new Pagination(200 * i, i, 200);
                    PageRows<AppConfig> appConfigList = JoymeAppConfigServiceSngl.get().queryAppConfigByPage(new QueryExpress()
                            .add(QueryCriterions.eq(AppConfigField.APPKEY, appKey))
                            .add(QueryCriterions.eq(AppConfigField.PLATFORM, platform))
                            .add(QueryCriterions.eq(AppConfigField.ENTERPRISE, enterprise)), pagination);
                    if (appConfigList != null && !CollectionUtil.isEmpty(appConfigList.getRows())) {
                        pagination = appConfigList.getPage();
                        for (AppConfig appConfig : appConfigList.getRows()) {
                            if (appConfig != null) {
                                //手游画报
                                if (appId.equals("17yfn24TFexGybOF0PqjdY") && !pushType.equals(PushListType.WANB_ASK_SYSTEM_MESSAGE)) {
                                    if (!StringUtil.isEmpty(appConfig.getVersion()) && (appConfig.getVersion().equals("1.4.0") || appConfig.getVersion().equals("1.4.1") || appConfig.getVersion().equals("1.4.2") || appConfig.getVersion().equals("1.4.3"))) {
                                        versionSet.add(appConfig.getVersion());
                                        if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                            if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                                channelSet.add(AppChannelType.getByCode(appConfig.getChannel()));
                                            }
                                        }
                                    }
                                    //玩霸
                                } else if (appId.equals("25AQWaK997Po2x300CQeP0")) {
                                    if (!StringUtil.isEmpty(appConfig.getVersion()) && !appConfig.getVersion().equals("1.4.0") && !appConfig.getVersion().equals("1.4.1") && !appConfig.getVersion().equals("1.4.2") && !appConfig.getVersion().equals("1.4.3")) {
                                        versionSet.add(appConfig.getVersion());
                                        if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                            if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                                channelSet.add(AppChannelType.getByCode(appConfig.getChannel()));
                                            }
                                        }
                                    }
                                } else {
                                    if (!StringUtil.isEmpty(appConfig.getVersion())) {
                                        versionSet.add(appConfig.getVersion());
                                    }
                                    if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                        if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                            channelSet.add(AppChannelType.getByCode(appConfig.getChannel()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } while (!pagination.isLastPage());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
                jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
                JSONObject result = new JSONObject();
                result.put("versionList", versionSet);
                result.put("channelList", channelSet);
                jsonObject.put("result", result);
                return jsonObject.toString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur Exception.e", e);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultCodeConstants.SUCCESS.getCode());
        jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());
        jsonObject.put("result", new JSONObject());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "appid", required = true) String appId,
                               @RequestParam(value = "pushlisttype", required = false) String pushListType,
                               @RequestParam(value = "msgid", required = true) Long pushMsgId,
                               @RequestParam(value = "app", required = false) String app) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appId", appId);
        mapMessage.put("pushListType", pushListType);
        mapMessage.put("app", app);
        try {
            List<AuthApp> appList = OAuthServiceSngl.get().queryAuthApp(
                    new QueryExpress()
                            .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.INTERNAL_CLIENT.getCode()))
                            .add(QueryCriterions.in(AuthAppField.PLATOFRM, new Integer[]{AppPlatform.ANDROID.getCode(), AppPlatform.CLIENT.getCode(), AppPlatform.IOS.getCode()}))
                            .add(QueryCriterions.eq(AuthAppField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                            .add(QuerySort.add(AuthAppField.CREATEDATE, QuerySortOrder.DESC))
            );
            mapMessage.put("appList", appList);

            PushMessageDTO pushMessageDTO = joymeAppWebLogic.getPushMessageDTO(new QueryExpress().add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMsgId)));
            if (pushMessageDTO == null) {
                return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType + "&app=" + app);
            }
            mapMessage.put("msgDTO", pushMessageDTO);

            String appKey = appId;
            if (appId.equals("25AQWaK997Po2x300CQeP0")) {
                appKey = "17yfn24TFexGybOF0PqjdY";
            }
            int platform = pushMessageDTO.getPushMessage().getAppPlatform().getCode();
            int enterprise = pushMessageDTO.getPushMessage().getEnterpriseType() == null ? AppEnterpriserType.DEFAULT.getCode() : pushMessageDTO.getPushMessage().getEnterpriseType().getCode();
            if (platform == 2) {
                platform = 0;
                enterprise = AppEnterpriserType.ENTERPRISE.getCode();
            }

            Set<String> versionList = new HashSet<String>();
            Set<AppChannelType> channelList = new HashSet<AppChannelType>();

            PushListType pushType = PushListType.getByCode(Integer.valueOf(pushListType));
            if (pushType.equals(PushListType.PUSH_MESSAGE)) {
                int i = 0;
                Pagination pagination = null;
                do {
                    i = i + 1;
                    pagination = new Pagination(200 * i, i, 200);
                    PageRows<AppConfig> appConfigList = JoymeAppConfigServiceSngl.get().queryAppConfigByPage(new QueryExpress()
                            .add(QueryCriterions.eq(AppConfigField.APPKEY, appKey))
                            .add(QueryCriterions.eq(AppConfigField.PLATFORM, platform))
                            .add(QueryCriterions.eq(AppConfigField.ENTERPRISE, enterprise)), pagination);
                    if (appConfigList != null && !CollectionUtil.isEmpty(appConfigList.getRows())) {
                        pagination = appConfigList.getPage();
                        for (AppConfig appConfig : appConfigList.getRows()) {
                            if (appConfig != null) {
                                if (appId.equals("17yfn24TFexGybOF0PqjdY")) {
                                    if (!StringUtil.isEmpty(appConfig.getVersion()) && (appConfig.getVersion().equals("1.4.0") || appConfig.getVersion().equals("1.4.1") || appConfig.getVersion().equals("1.4.2") || appConfig.getVersion().equals("1.4.3"))) {
                                        versionList.add(appConfig.getVersion());
                                        if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                            if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                                channelList.add(AppChannelType.getByCode(appConfig.getChannel()));
                                            }
                                        }
                                    }
                                    //玩霸
                                } else if (appId.equals("25AQWaK997Po2x300CQeP0")) {
                                    if (!StringUtil.isEmpty(appConfig.getVersion()) && !appConfig.getVersion().equals("1.4.0") && !appConfig.getVersion().equals("1.4.1") && !appConfig.getVersion().equals("1.4.2") && !appConfig.getVersion().equals("1.4.3")) {
                                        versionList.add(appConfig.getVersion());
                                        if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                            if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                                channelList.add(AppChannelType.getByCode(appConfig.getChannel()));
                                            }
                                        }
                                    }
                                } else {
                                    if (!StringUtil.isEmpty(appConfig.getVersion())) {
                                        versionList.add(appConfig.getVersion());
                                    }
                                    if (!StringUtil.isEmpty(appConfig.getChannel())) {
                                        if (AppChannelType.getByCode(appConfig.getChannel()) != null) {
                                            channelList.add(AppChannelType.getByCode(appConfig.getChannel()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                } while (!pagination.isLastPage());
            }
            mapMessage.put("versionList", versionList);
            mapMessage.put("channelList", channelList);

            Set<String> versionSet = new HashSet<String>();
            if (!StringUtil.isEmpty(pushMessageDTO.getPushMessage().getAppVersion())) {
                String[] checkVersionList = pushMessageDTO.getPushMessage().getAppVersion().split("\\|");
                for (String version : checkVersionList) {
                    if (!StringUtil.isEmpty(version)) {
                        versionSet.add(version);
                    }
                }
            }
            mapMessage.put("versionSet", versionSet);

            Set<AppChannelType> channelSet = new HashSet<AppChannelType>();
            if (!StringUtil.isEmpty(pushMessageDTO.getPushMessage().getAppChannel())) {
                String[] checkChannelList = pushMessageDTO.getPushMessage().getAppChannel().split("\\|");
                for (String channel : checkChannelList) {
                    if (!StringUtil.isEmpty(channel)) {
                        if (AppChannelType.getByCode(channel) != null) {
                            channelSet.add(AppChannelType.getByCode(channel));
                        }
                    }
                }
            } else {
                channelSet.addAll(channelList);
            }
            mapMessage.put("channelSet", channelSet);

            String screenNames = "";
            if (!StringUtil.isEmpty(pushMessageDTO.getPushMessage().getUnos())) {
                String[] profileIds = pushMessageDTO.getPushMessage().getUnos().split(" ");
                for (String profileId : profileIds) {
                    Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
                    if (profile != null) {
                        screenNames += (profile.getNick() + " ");
                    }
                }
            }
            mapMessage.put("screenNames", screenNames);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage.put("errorMsg", "error.exception");
            return new ModelAndView("redirect:/joymeapp/push/list?appid=" + appId + "&pushlisttype=" + pushListType);
        }
        if (appId.equals("3iiv7VWfx84pmHgCUqRwun") && !StringUtil.isEmpty(app) && "wanbaask".equals(app)) {
            mapMessage.put("WanbaJt", WanbaJt.getToolsAll());
            return new ModelAndView("/wanba/pushmessage/pushdetail", mapMessage);
        } else if (appId.equals("17yfn24TFexGybOF0PqjdY")) {
            return new ModelAndView("/joymeapp/newsclient/pushmessage/pushdetail", mapMessage);
        } else if (appId.equals("0VsYSLLsN8CrbBSMUOlLNx")) {
            return new ModelAndView("/joymeapp/socialclient/pushmessage/pushdetail", mapMessage);
        } else if (appId.equals("1wVnuqR5R0PFRk0K7Jj1ho")) {
            return new ModelAndView("/joymeapp/meijuclient/pushmessage/pushdetail", mapMessage);
        } else if (appId.equals("1zBwYvQpt3AE6JsykiA2es")) {
            return new ModelAndView("/joymeapp/huoyingclient/pushmessage/pushdetail", mapMessage);
        } else if (appId.equals("0G30ZtEkZ4vFBhAfN7Bx4v")) {
            return new ModelAndView("/joymeapp/anime/push/pushdetail", mapMessage);
        } else if (appId.equals("25AQWaK997Po2x300CQeP0")) {
            return new ModelAndView("/gameclient/pushmessage/pushdetail", mapMessage);
        } else {
            return new ModelAndView("/joymeapp/pushmessage/pushdetail", mapMessage);
        }
    }
}
