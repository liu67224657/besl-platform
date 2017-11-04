package com.enjoyf.webapps.tools.weblogic.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.*;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.joymeApp.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-1
 * Time: 上午1:33
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "jomyeAppWebLogic")
public class JoymeAppWebLogic {

    private static final String CUREENT_DATE_FISRT_NUM = "001";

    public PageRows<PushMessageDTO> queryPushMessageDTO(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        PageRows<PushMessageDTO> returnObj = new PageRows<PushMessageDTO>();

        PageRows<PushMessage> pageRows = MessageServiceSngl.get().queryPushMessage(queryExpress, pagination);

        List<PushMessageDTO> list = new ArrayList<PushMessageDTO>();
        for (PushMessage pushMessage : pageRows.getRows()) {
            PushMessageDTO dto = new PushMessageDTO();

            AuthApp authApp = OAuthServiceSngl.get().getApp(pushMessage.getAppKey());
            dto.setPushMessage(pushMessage);
            if (authApp != null && !StringUtil.isEmpty(authApp.getAppName())) {
                dto.setAppName(authApp.getAppName());
            }

            list.add(dto);
        }
        returnObj.setPage(pageRows.getPage());
        returnObj.setRows(list);

        return returnObj;

    }

    public PushMessageDTO getPushMessageDTO(QueryExpress queryExpress) throws ServiceException {
        PushMessageDTO returnObj = null;

        PushMessage message = MessageServiceSngl.get().getPushMessage(queryExpress);
        if (message == null || StringUtil.isEmpty(message.getAppKey())) {
            return null;
        }

        AuthApp authApp = OAuthServiceSngl.get().getApp(message.getAppKey());
        if (authApp == null) {
            return null;
        }

        returnObj = new PushMessageDTO();
        returnObj.setPushMessage(message);
        returnObj.setAppName(authApp.getAppName());

        return returnObj;

    }

    public boolean sendKaDaMessage(PushMessage pushMessage) throws ServiceException {
        boolean returnBoolean = false;
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(PushMessageField.PUSHSTATUS, ActStatus.ACTING.getCode());
        updateExpress.set(PushMessageField.SENDDATE, new Date());

        returnBoolean = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMessage.getPushMsgId())), pushMessage.getAppKey());


        MessageServiceSngl.get().sendSociailPushMessage(pushMessage);

        return returnBoolean;

    }

    public boolean sendMessage(PushMessage pushMessage) throws ServiceException {
        boolean returnBoolean = false;

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(PushMessageField.PUSHSTATUS, ActStatus.ACTING.getCode());
        updateExpress.set(PushMessageField.SENDDATE, new Date());

        returnBoolean = MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, pushMessage.getPushMsgId())), pushMessage.getAppKey());


//        if (pushMessage.getAcccoutPlatform().equals(AppPlatform.IOS)) {
        MessageServiceSngl.get().sendPushMessage(pushMessage);
//        }

        return returnBoolean;
    }

    public long generatorApp(String appKey) throws ServiceException {
        long returnVersionNum = 0;

        long lastVersionNum = JoymeAppConfigServiceSngl.get().getLastAppContentVersionByAppKey(appKey);

        if (lastVersionNum <= 0) {
            returnVersionNum = Long.parseLong(DateUtil.getCurrentDate(DateUtil.PATTERN_DATE_DAY) + CUREENT_DATE_FISRT_NUM);
        } else {
            String dateString = String.valueOf(lastVersionNum).substring(0, 8);
            if (DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_DAY).equals(dateString)) {
                returnVersionNum = lastVersionNum + 1;
            } else {
                returnVersionNum = Long.parseLong(DateUtil.getCurrentDate(DateUtil.PATTERN_DATE_DAY) + CUREENT_DATE_FISRT_NUM);
            }
        }

        return returnVersionNum;
    }




    public PageRows<ActivityTopMenuDTO> queryActivityTopMenuDTO(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        PageRows<ActivityTopMenu> activityTopMenuPageRows = JoymeAppConfigServiceSngl.get().queryActivityTopMenuPage(queryExpress, pagination);
        if (activityTopMenuPageRows == null || CollectionUtil.isEmpty(activityTopMenuPageRows.getRows())) {
            return null;
        }
        List<ActivityTopMenu> activityTopMenuList = activityTopMenuPageRows.getRows();
        List<ActivityTopMenuDTO> list = new ArrayList<ActivityTopMenuDTO>();

        for (ActivityTopMenu menu : activityTopMenuList) {
            AppChannel appChannel = new AppChannel();
            AuthApp authApp = new AuthApp();
            if (menu.getChannelId() != null && menu.getChannelId() > 0) {
                appChannel = JoymeAppConfigServiceSngl.get().getAppChannel(menu.getChannelId());
            }
            if (!StringUtil.isEmpty(menu.getAppKey())) {
                List<AuthApp> authAppList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress().add(QueryCriterions.eq(AuthAppField.APPID, menu.getAppKey())));
                if (!CollectionUtil.isEmpty(authAppList)) {
                    authApp = authAppList.get(0);
                }
            }
            ActivityTopMenuDTO dto = new ActivityTopMenuDTO();
            dto.setAppChannel(appChannel);
            dto.setActivityTopMenu(menu);
            dto.setAuthApp(authApp);
            list.add(dto);
        }
        PageRows<ActivityTopMenuDTO> pageRows = new PageRows<ActivityTopMenuDTO>();
        pageRows.setRows(list);
        pageRows.setPage(pagination);
        return pageRows;
    }

    public static Long sortActivityTopMenu(String sort, String appkey, long activityTopMenuId, int category) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, category));
        //step1 get goodsId,get display order
        ActivityTopMenu activityTopMenu = JoymeAppConfigServiceSngl.get().getActivityTopMenuById(activityTopMenuId);
        if (!StringUtil.isEmpty(appkey)) {
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.APP_KEY, appkey));
        }
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.lt(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.DESC));

        } else {
            queryExpress.add(QueryCriterions.gt(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
        }

        List<ActivityTopMenu> list = JoymeAppConfigServiceSngl.get().queryActivityTopMenu(queryExpress);
        if (!CollectionUtil.isEmpty(list)) {
            updateExpress1.set(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder());
            JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(new QueryExpress().add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, list.get(0).getActivityTopMenuId())), updateExpress1);

            updateExpress2.set(ActivityTopMenuField.DISPLAY_ORDER, list.get(0).getDisplayOrder());
            JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(new QueryExpress().add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, activityTopMenu.getActivityTopMenuId())), updateExpress2);
        }
//        JoymeAppServiceSngl.get().queryActivityTopMenu(new QueryExpress().add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC)).add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.DEFAULT.getCode())));
        return list.get(0).getActivityTopMenuId();
    }

    public static void sortClientTopMenu(String sort, long activityTopMenuId, int category, int clientPlatform) throws ServiceException {
        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, category));
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, clientPlatform));
        //step1 get goodsId,get display order
        ActivityTopMenu activityTopMenu = JoymeAppConfigServiceSngl.get().getActivityTopMenuById(activityTopMenuId);
        if (sort.equals("up")) {
            queryExpress.add(QueryCriterions.lt(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.DESC));
        } else {
            queryExpress.add(QueryCriterions.gt(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder()));
            queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
        }

        List<ActivityTopMenu> list = JoymeAppConfigServiceSngl.get().queryActivityTopMenu(queryExpress);
        if (!CollectionUtil.isEmpty(list)) {
            updateExpress1.set(ActivityTopMenuField.DISPLAY_ORDER, activityTopMenu.getDisplayOrder());
            JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(new QueryExpress().add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, list.get(0).getActivityTopMenuId())), updateExpress1);

            updateExpress2.set(ActivityTopMenuField.DISPLAY_ORDER, list.get(0).getDisplayOrder());
            JoymeAppConfigServiceSngl.get().modifyActivityTopMenu(new QueryExpress().add(QueryCriterions.eq(ActivityTopMenuField.ACTIVITY_MENU_ID, activityTopMenu.getActivityTopMenuId())), updateExpress2);
        }
        JoymeAppConfigServiceSngl.get().queryActivityTopMenu(new QueryExpress().add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC)).add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, AppTopMenuCategory.DEFAULT.getCode())).add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, clientPlatform)));
    }

    public PageRows<ClientTopMenuDTO> queryClientTopMenuDTO(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        PageRows<ActivityTopMenu> activityTopMenuPageRows = JoymeAppConfigServiceSngl.get().queryActivityTopMenuPage(queryExpress, pagination);
        if (activityTopMenuPageRows == null || CollectionUtil.isEmpty(activityTopMenuPageRows.getRows())) {
            return null;
        }
        List<ActivityTopMenu> activityTopMenuList = activityTopMenuPageRows.getRows();
        List<ClientTopMenuDTO> list = new ArrayList<ClientTopMenuDTO>();

        for (ActivityTopMenu menu : activityTopMenuList) {
            AppChannel appChannel = new AppChannel();
            if (menu.getChannelId() != null && menu.getChannelId() > 0) {
                appChannel = JoymeAppConfigServiceSngl.get().getAppChannel(menu.getChannelId());
            }
            ClientTopMenuDTO dto = new ClientTopMenuDTO();
            dto.setAppChannel(appChannel);
            dto.setActivityTopMenu(menu);
            list.add(dto);
        }
        PageRows<ClientTopMenuDTO> pageRows = new PageRows<ClientTopMenuDTO>();
        pageRows.setRows(list);
        pageRows.setPage(pagination);
        return pageRows;
    }
}
