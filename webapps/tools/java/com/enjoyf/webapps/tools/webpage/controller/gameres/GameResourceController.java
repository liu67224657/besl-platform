package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.props.hotdeploy.GameResourceHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ToolsHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ImageContent;
import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.PrivilegeUser;
import com.enjoyf.platform.service.tools.ToolsConstants;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.webapps.common.JoymeResultMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.dto.game.GameRelationDTO;
import com.enjoyf.webapps.tools.weblogic.gameres.GameResourceWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * User: taijunli
 * Date: 11-1-4
 * Time: 上午9:37
 */
@Deprecated
@Controller
@RequestMapping(value = "/gameresource")
public class GameResourceController extends ToolsBaseController {
    //
    private Logger logger = LoggerFactory.getLogger(GameResourceController.class);

    //
    private ToolsHotdeployConfig toolsHotdeployConfig = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class);

    //
    @Resource(name = "gameResourceWebLogic")
    private GameResourceWebLogic gameResourceWebLogic;

    //
    private JsonBinder jsonbinder = JsonBinder.buildNormalBinder();

    private static Set<String> gameResourceTypes = new LinkedHashSet<String>();

    static {
        gameResourceTypes.add("new.jpg");
        gameResourceTypes.add("hot.jpg");
        gameResourceTypes.add("ess.gif");
    }

    //
    @RequestMapping(value = "/precreategameresource")
    public ModelAndView preCreateGameResource() {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        //
        Map<String, GameDevice> deviceMap = toolsHotdeployConfig.getDeviceMap();
        Map<String, GameCategory> categoryMap = toolsHotdeployConfig.getCategoryMap();
        Map<String, GameStyle> styleMap = toolsHotdeployConfig.getStyleMap();

        mapMsg.put("deviceMap", deviceMap);
        mapMsg.put("styleMap", styleMap);
        mapMsg.put("categoryMap", categoryMap);
        mapMsg.put("gameResourceTypes", gameResourceTypes);

        mapMsg.put("gameChannelList", HotdeployConfigFactory.get().getConfig(GameResourceHotdeployConfig.class).getChannelMap().values());

        //
        return new ModelAndView("/gameresource/precreategameresource", mapMsg);
    }

    @RequestMapping(value = "/checkname")
    @ResponseBody
    public String checkName(
            @RequestParam(value = "resourceName", required = true) String resourceName,
            @RequestParam(value = "resourceDomain", required = false) String resourceDomain,
            @RequestParam(value = "resourceId", required = false, defaultValue = "0") long resourceIdItself) {
        //供ajax使用，存在返回1 ;不存在返回0
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);

        if (this.isGameResourceNameExist(resourceName, resourceDomain, resourceIdItself)) {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
        } else {
            resultMsg.setStatus_code(JoymeResultMsg.CODE_E);
        }

        return jsonbinder.toJson(resultMsg);
    }

    @RequestMapping(value = "/checksynonyms")
    @ResponseBody
    public String checkSynonyms(
            @RequestParam(value = "synonyms", required = false) String synonyms,
            @RequestParam(value = "resourceId", required = false, defaultValue = "0") long resourceId) {
        //供ajax使用，存在返回1 ;不存在返回0
        JoymeResultMsg resultMsg = new JoymeResultMsg(JoymeResultMsg.CODE_E);
        List jsonList = new ArrayList();

        try {

            Set<String> synonymses = StringUtil.splitStringToSet(synonyms, ",");

            if (this.isSynonymExist(synonymses, resourceId)) {
                jsonList.add(this.getJsonListValue(gameResourceWebLogic.isSynonymsesExist(synonymses, resourceId)));
                jsonList.add(gameResourceWebLogic.getOtherGameResourceName(synonymses, resourceId));

                resultMsg.setStatus_code(JoymeResultMsg.CODE_S);
                resultMsg.setResult(jsonList);
            }

        } catch (ServiceException e) {
            GAlerter.lab("checkSynonyms a Exception :", e);
        }

        //
        return jsonbinder.toJson(resultMsg);
    }

    @RequestMapping(value = "/creategameresource")
    public ModelAndView createGameResource(
            @RequestParam(value = "resourceName", required = true) String resourceName,      //游戏条目主名称
            @RequestParam(value = "gameCode", required = false) String gameCode,            //游戏编码
            @RequestParam(value = "resourceDomain", required = false) String resourceDomain,//
            @RequestParam(value = "device", required = false) String device,                 //游戏平台
            @RequestParam(value = "logoSize", required = false) String logoSize,     //主图类别
            @RequestParam(value = "resourceCategory", required = false) String resourceCategory,  //类型
            @RequestParam(value = "resourceStyle", required = false) String resourceStyle,  //风格：无该字段
            @RequestParam(value = "resourceStyleValue", required = false) String resourceStyleValue,  //风格：无该字段
            @RequestParam(value = "publishCompany", required = false) String publishCompany,  //发行商/运营商
            @RequestParam(value = "publishCompanyValue", required = false) String publishCompanyValue,  //发行商/运营商
            @RequestParam(value = "develop", required = false) String develop,        //开发商
            @RequestParam(value = "developValue", required = false) String developValue,        //开发商
            @RequestParam(value = "playerNumber", required = false) String playerNumber,  //游戏人数
            @RequestParam(value = "playerNumberValue", required = false) String playerNumberValue,  //游戏人数
            @RequestParam(value = "fileSize", required = false) String fileSize,     //游戏大小
            @RequestParam(value = "fileSizeValue", required = false) String fileSizeValue,     //游戏大小
            @RequestParam(value = "price", required = false) String price,     //价格
            @RequestParam(value = "priceValue", required = false) String priceValue,     //价格小时
            @RequestParam(value = "fileType", required = false) String fileType,      //游戏大小类型
            @RequestParam(value = "publishDateValue", required = false) String publishDateValue, //发售日期
            @RequestParam(value = "publishDate", required = false) String publishDate, //发售日期
            @RequestParam(value = "lastUpdateDate", required = false) String lastUpdateDate, //更新日期
            @RequestParam(value = "lastUpdateDateValue", required = false) String lastUpdateDateValue, //更新日期
            @RequestParam(value = "resourceUrl", required = false) String resourceUrl,    //官网
            @RequestParam(value = "resourceUrlValue", required = false) String resourceUrlValue,    //官网
            @RequestParam(value = "language", required = false) String language,         //语言
            @RequestParam(value = "languageValue", required = false) String languageValue,         //语言
            @RequestParam(value = "resourceDesc", required = false) String resourceDesc,   //游戏简介
            @RequestParam(value = "updateInfo", required = false) String updateInfo,   //游戏简介
            @RequestParam(value = "resourceDescValue", required = false) String resourceDescValue,   //游戏简介
            @RequestParam(value = "seoKeyWords", required = false) String seoKeyWords,
            @RequestParam(value = "seoDescription", required = false) String seoDescription,
            @RequestParam(value = "synonyms", required = false) String synonyms,
            @RequestParam(value = "picurl_s", required = false) String[] picurl_s,
            @RequestParam(value = "picurl_m", required = false) String[] picurl_m,
            @RequestParam(value = "picurl_b", required = false) String[] picurl_b,
            @RequestParam(value = "picurl_ss", required = false) String[] picurl_ss,
            @RequestParam(value = "picurl_ll", required = false) String[] picurl_ll,
            @RequestParam(value = "picurl_sl", required = false) String[] picurl_sl,
            @RequestParam(value = "thumbimg", required = false) String thumbimg,
            @RequestParam(value = "eventdesc", required = false) String eventdesc,
            @RequestParam(value = "score1", required = false) String score1,
            @RequestParam(value = "from1", required = false) String from1,
            @RequestParam(value = "desc1", required = false) String desc1,
            @RequestParam(value = "score2", required = false) String score2,
            @RequestParam(value = "from2", required = false) String from2,
            @RequestParam(value = "desc2", required = false) String desc2,
            @RequestParam(value = "buyLinkValue", required = false) String buyLinkValue,
            @RequestParam(value = "channeltype1", required = false) String channelType1,
            @RequestParam(value = "channellink1", required = false) String channellink1,
            @RequestParam(value = "channelprice1", required = false) String channelprice1,
            @RequestParam(value = "channeltype2", required = false) String channelType2,
            @RequestParam(value = "channellink2", required = false) String channellink2,
            @RequestParam(value = "channelprice2", required = false) String channelprice2,
            @RequestParam(value = "channeltype3", required = false) String channelType3,
            @RequestParam(value = "channellink3", required = false) String channellink3,
            @RequestParam(value = "channelprice3", required = false) String channelprice3,
            @RequestParam(value = "channeltype4", required = false) String channelType4,
            @RequestParam(value = "channellink4", required = false) String channellink4,
            @RequestParam(value = "channelprice4", required = false) String channelprice4,
            @RequestParam(value = "channeltype5", required = false) String channelType5,
            @RequestParam(value = "channellink5", required = false) String channellink5,
            @RequestParam(value = "channelprice5", required = false) String channelprice5
    ) {
        //
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        long resourceId = 0;

        if (isGameResourceNameExist(resourceName, resourceDomain, resourceId)) {
            errorMsgMap.put("resourceName", "error.gameresource.resourcename.exist");
        }
        if (isSynonymExist(StringUtil.splitStringToSet(synonyms, ","), resourceId)) {
            errorMsgMap.put("synonyms", "error.gameresource.synonyms.exist");
        }
        mapMessage.put("errorMsgMap", errorMsgMap);

        GameResource entity = getGameResourceOnGame(resourceId, resourceName.trim(), gameCode.trim(), resourceDomain, device, logoSize, resourceCategory,
                resourceStyle, resourceStyleValue, publishCompany, publishCompanyValue, develop, developValue, playerNumber, playerNumberValue,
                fileSize, fileType, fileSizeValue, price, priceValue, publishDate, publishDateValue, resourceUrl, resourceUrlValue, language, languageValue,
                resourceDesc, resourceDescValue, seoKeyWords, synonyms, seoDescription, picurl_s, picurl_m, picurl_b, picurl_ss,
                picurl_ll, picurl_sl, eventdesc, thumbimg, score1, from1, desc1, score2, from2, desc2, lastUpdateDate, lastUpdateDateValue, buyLinkValue);

        Map<String, GameDevice> deviceMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getDeviceMap();
        Map<String, GameCategory> categoryMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getCategoryMap();
        Map<String, GameStyle> styleMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getStyleMap();

        mapMessage.put("deviceMap", deviceMap);
        mapMessage.put("styleMap", styleMap);
        mapMessage.put("categoryMap", categoryMap);
        mapMessage.put("gameResourceTypes", gameResourceTypes);

        mapMessage.put("entity", entity);
        mapMessage.put("fileType", fileType);

        if (StringUtil.isEmpty(entity.getResourceName())) {
            errorMsgMap.put("resourceNameNull", "error.gameresource.resourcename.null");
        }
        if (CollectionUtil.isEmpty(entity.getIcon().getImages())) {
            errorMsgMap.put("icon", "error.gameresource.icon.null");
        }

        if (!StringUtil.isEmpty(entity.getGameCode())) {
            GameResource existsGameResource = null;
            try {
                existsGameResource = GameResourceServiceSngl.get().getGameResource(
                        new QueryExpress().add(QueryCriterions.eq(GameResourceField.GAMECODE, entity.getGameCode()))
                                .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GAME.getCode())));
            } catch (ServiceException e) {
                GAlerter.lab("Caught an Exception when query GameResources : " + e);
            }
            if (existsGameResource != null) {
                errorMsgMap.put("gamecode", "error.gameresource.gamecode.duplicate");
            }
        }

        if (!errorMsgMap.isEmpty()) {
            return new ModelAndView("/gameresource/precreategameresource", mapMessage);
        }

        try {
            List<GameProperty> gamePropertyList = new ArrayList<GameProperty>();
            GameProperty channelGameProp1 = buildGameChannel(channelType1, channellink1, channelprice1, 1);
            GameProperty channelGameProp2 = buildGameChannel(channelType2, channellink2, channelprice2, 2);
            GameProperty channelGameProp3 = buildGameChannel(channelType3, channellink3, channelprice3, 3);
            GameProperty channelGameProp4 = buildGameChannel(channelType4, channellink4, channelprice4, 4);
            GameProperty channelGameProp5 = buildGameChannel(channelType5, channellink5, channelprice5, 5);
            if (channelGameProp1 != null) {
                gamePropertyList.add(channelGameProp1);
            }
            if (channelGameProp2 != null) {
                gamePropertyList.add(channelGameProp2);
            }
            if (channelGameProp3 != null) {
                gamePropertyList.add(channelGameProp3);
            }
            if (channelGameProp4 != null) {
                gamePropertyList.add(channelGameProp4);
            }
            if (channelGameProp5 != null) {
                gamePropertyList.add(channelGameProp5);
            }

            GameProperty updateInfoProperty = new GameProperty();
            updateInfoProperty.setGamePropertyDomain(GamePropertyDomain.UPDATEINFO);
            updateInfoProperty.setValue(updateInfo);
            gamePropertyList.add(updateInfoProperty);

            entity = gameResourceWebLogic.saveGameResource(entity, getCurrentUser(), gamePropertyList);

            //
            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.CJYXTM_SAVEGAMERESPAGE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter(StringUtil.truncate(entity.toString(), ToolsConstants.SPLIT_SIZE));

            addLog(log);

        } catch (ServiceException e) {

            GAlerter.lab("Caught an Exception when modify the GameResource :　" + e);

            errorMsgMap.put("system", "error.exception");
            return new ModelAndView("/gameresource/precreategameresource", mapMessage);
        }

        return new ModelAndView("redirect:/gameresource/gameresourcelist");

    }

    @RequestMapping(value = "/preeditgameresource")
    public ModelAndView preEditGameResPage(
            @RequestParam(value = "resourceId", required = false) Long resourceId) {  //游戏条目ID
        //
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        GameResource entity = new GameResource();

        Map<String, GameDevice> deviceMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getDeviceMap();
        Map<String, GameCategory> categoryMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getCategoryMap();
        Map<String, GameStyle> styleMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getStyleMap();

        mapMessage.put("deviceMap", deviceMap);
        mapMessage.put("styleMap", styleMap);
        mapMessage.put("categoryMap", categoryMap);
        mapMessage.put("gameResourceTypes", gameResourceTypes);
        mapMessage.put("gameChannelList", HotdeployConfigFactory.get().getConfig(GameResourceHotdeployConfig.class).getChannelMap().values());

        try {
            entity = gameResourceWebLogic.getGameResourceById(resourceId);
            if (entity.getGameProperties() != null) {
                List<GameProperty> channels = entity.getGameProperties().getChannels();
                if (!CollectionUtil.isEmpty(channels)) {
                    for (int i = 0; i < channels.size(); i++) {
                        mapMessage.put("channel" + (i + 1), channels.get(i));
                    }
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("preEditGameResPage a  Exception :", e);
        }
        mapMessage.put("entity", entity);

        return new ModelAndView("/gameresource/preeditgameresource", mapMessage);
    }


    @RequestMapping(value = "/editgameresource")
    public ModelAndView editGameResource(
            @RequestParam(value = "resourceId", required = true) Long resourceId,
            @RequestParam(value = "resourceName", required = true) String resourceName,      //游戏条目主名称
            @RequestParam(value = "gameCode", required = false) String gameCode,
            @RequestParam(value = "orggamecode", required = false) String orggamecode,
            @RequestParam(value = "resourceDomain", required = false) String resourceDomain,//
            @RequestParam(value = "device", required = false) String device,                 //游戏平台
            @RequestParam(value = "logoSize", required = false) String logoSize,     //主图类别
            @RequestParam(value = "resourceCategory", required = false) String resourceCategory,  //类型
            @RequestParam(value = "resourceStyle", required = false) String resourceStyle,  //风格：无该字段
            @RequestParam(value = "resourceStyleValue", required = false) String resourceStyleValue,  //风格：无该字段
            @RequestParam(value = "publishCompany", required = false) String publishCompany,  //发行商/运营商
            @RequestParam(value = "publishCompanyValue", required = false) String publishCompanyValue,  //发行商/运营商
            @RequestParam(value = "develop", required = false) String develop,        //开发商
            @RequestParam(value = "developValue", required = false) String developValue,        //开发商
            @RequestParam(value = "playerNumber", required = false) String playerNumber,  //游戏人数
            @RequestParam(value = "playerNumberValue", required = false) String playerNumberValue,  //游戏人数
            @RequestParam(value = "fileSize", required = false) String fileSize,     //游戏大小
            @RequestParam(value = "fileSizeValue", required = false) String fileSizeValue,     //游戏大小
            @RequestParam(value = "price", required = false) String price,     //价格
            @RequestParam(value = "priceValue", required = false) String priceValue,     //价格隐藏
            @RequestParam(value = "publishDate", required = false) String publishDate, //发售日期
            @RequestParam(value = "publishDateValue", required = false) String publishDateValue, //发售日期
            @RequestParam(value = "lastUpdateDate", required = false) String lastUpdateDate, //更新日期
            @RequestParam(value = "lastUpdateDateValue", required = false) String lastUpdateDateValue, //更新日期
            @RequestParam(value = "resourceUrl", required = false) String resourceUrl,    //官网
            @RequestParam(value = "resourceUrlValue", required = false) String resourceUrlValue,    //官网
            @RequestParam(value = "language", required = false) String language,         //语言
            @RequestParam(value = "languageValue", required = false) String languageValue,         //语言
            @RequestParam(value = "resourceDesc", required = false) String resourceDesc,   //游戏简介
            @RequestParam(value = "resourceDescValue", required = false) String resourceDescValue,   //游戏简介
            @RequestParam(value = "updateInfo", required = false) String updateInfo,   //游戏简介
            @RequestParam(value = "seoKeyWords", required = false) String seoKeyWords,
            @RequestParam(value = "synonyms", required = false) String synonyms,
            @RequestParam(value = "seoDescription", required = false) String seoDescription,
            @RequestParam(value = "picurl_s", required = false) String[] picurl_s,
            @RequestParam(value = "picurl_m", required = false) String[] picurl_m,
            @RequestParam(value = "picurl_b", required = false) String[] picurl_b,
            @RequestParam(value = "picurl_ss", required = false) String[] picurl_ss,
            @RequestParam(value = "picurl_ll", required = false) String[] picurl_ll,
            @RequestParam(value = "picurl_sl", required = false) String[] picurl_sl,
            @RequestParam(value = "thumbimg", required = false) String thumbimg,
            @RequestParam(value = "eventdesc", required = false) String eventDesc,
            @RequestParam(value = "score1", required = false) String score1,
            @RequestParam(value = "from1", required = false) String from1,
            @RequestParam(value = "desc1", required = false) String desc1,
            @RequestParam(value = "score2", required = false) String score2,
            @RequestParam(value = "from2", required = false) String from2,
            @RequestParam(value = "desc2", required = false) String desc2,
            @RequestParam(value = "buyLinkValue", required = false) String buyLinkValue,
            @RequestParam(value = "channeltype1", required = false) String channelType1,
            @RequestParam(value = "channellink1", required = false) String channellink1,
            @RequestParam(value = "channelprice1", required = false) String channelprice1,
            @RequestParam(value = "channeltype2", required = false) String channelType2,
            @RequestParam(value = "channellink2", required = false) String channellink2,
            @RequestParam(value = "channelprice2", required = false) String channelprice2,
            @RequestParam(value = "channeltype3", required = false) String channelType3,
            @RequestParam(value = "channellink3", required = false) String channellink3,
            @RequestParam(value = "channelprice3", required = false) String channelprice3,
            @RequestParam(value = "channeltype4", required = false) String channelType4,
            @RequestParam(value = "channellink4", required = false) String channellink4,
            @RequestParam(value = "channelprice4", required = false) String channelprice4,
            @RequestParam(value = "channeltype5", required = false) String channelType5,
            @RequestParam(value = "channellink5", required = false) String channellink5,
            @RequestParam(value = "channelprice5", required = false) String channelprice5) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        if (isGameResourceNameExist(resourceName, resourceDomain, resourceId)) {
            errorMsgMap.put("resourceName", "error.gameresource.resourcename.exist");
        }
        if (isSynonymExist(StringUtil.splitStringToSet(synonyms, ","), resourceId)) {
            errorMsgMap.put("synonyms", "error.gameresource.synonyms.exist");
        }
        if (StringUtil.isEmpty(resourceName.trim())) {
            errorMsgMap.put("resourceNameNull", "error.gameresource.resourcename.null");
        }
        if (picurl_s == null || picurl_s.length == 0) {
            errorMsgMap.put("icon", "error.gameresource.icon.null");
        }
        if (!StringUtil.isEmpty(gameCode) && !gameCode.equals(orggamecode)) {
            GameResource existsGameResource = null;
            try {
                existsGameResource = GameResourceServiceSngl.get().getGameResource(
                        new QueryExpress().add(QueryCriterions.eq(GameResourceField.GAMECODE, gameCode))
                                .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GAME.getCode())));
            } catch (ServiceException e) {
                GAlerter.lab("Caught an Exception when query GameResources : " + e);
            }

            if (existsGameResource != null) {
                errorMsgMap.put("gamecode", "error.gameresource.gamecode.duplicate");
            }
        }

        mapMsg.put("errorMsgMap", errorMsgMap);

        //
        GameResource entity = getGameResourceOnGame(resourceId, resourceName.trim(), gameCode.trim(), resourceDomain, device, logoSize, resourceCategory,
                resourceStyle, resourceStyleValue, publishCompany, publishCompanyValue, develop, developValue, playerNumber, playerNumberValue,
                fileSize, null, fileSizeValue, price, priceValue, publishDate, publishDateValue, resourceUrl, resourceUrlValue, language, languageValue,
                resourceDesc, resourceDescValue, seoKeyWords, synonyms, seoDescription, picurl_s, picurl_m, picurl_b, picurl_ss,
                picurl_ll, picurl_sl, eventDesc, thumbimg, score1, from1, desc1, score2, from2, desc2, lastUpdateDate, lastUpdateDateValue, buyLinkValue);

        Map<String, GameDevice> deviceMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getDeviceMap();
        Map<String, GameCategory> categoryMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getCategoryMap();
        Map<String, GameStyle> styleMap = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class).getStyleMap();

        mapMsg.put("deviceMap", deviceMap);
        mapMsg.put("styleMap", styleMap);
        mapMsg.put("categoryMap", categoryMap);
        mapMsg.put("gameResourceTypes", gameResourceTypes);

        //
        mapMsg.put("entity", entity);

        if (!errorMsgMap.isEmpty()) {
            return new ModelAndView("/gameresource/preeditgameresource", mapMsg);
        }

        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(GameResourceField.RESOURCENAME, resourceName);
        updateExpress.set(GameResourceField.GAMECODE, entity.getGameCode());
        updateExpress.set(GameResourceField.RESOURCEDOMAIN, resourceDomain);
        if (entity.getDeviceSet() != null) {
            updateExpress.set(GameResourceField.DEVICE, entity.getDeviceSet().toJsonStr());
        }
        updateExpress.set(GameResourceField.ICON, entity.getIcon().toJsonStr());
        if (entity.getCategorySet() != null) {
            updateExpress.set(GameResourceField.RESOURCECATEGORY, entity.getCategorySet().toJsonStr());
        }
        if (entity.getStyleSet() != null) {
            updateExpress.set(GameResourceField.RESOURCESTYLE, entity.getStyleSet().toJsonStr());
        }
        if (entity.getResourceThumbimg() == null) {
            updateExpress.set(GameResourceField.THUMBIMG, null);
        } else {
            updateExpress.set(GameResourceField.THUMBIMG, entity.getResourceThumbimg().toJsonStr());
        }
        updateExpress.set(GameResourceField.PUBLISHCOMPANY, publishCompany);
        updateExpress.set(GameResourceField.DEVELOP, develop);
        updateExpress.set(GameResourceField.PLAYERNUMBER, playerNumber);
        updateExpress.set(GameResourceField.FILESIZE, fileSize);
        updateExpress.set(GameResourceField.PRICE, price);
        updateExpress.set(GameResourceField.PUBLISHDATE, publishDate);
        updateExpress.set(GameResourceField.RESOURCEURL, resourceUrl);
        updateExpress.set(GameResourceField.LANGUAGE, language);
        updateExpress.set(GameResourceField.RESOURCEDESC, resourceDesc);
        updateExpress.set(GameResourceField.LOGOSIZE, logoSize);
        updateExpress.set(GameResourceField.SEOKEYWORDS, seoKeyWords);
        updateExpress.set(GameResourceField.SEODESCRIPTION, seoDescription);
        updateExpress.set(GameResourceField.SHOWVALUE, entity.getResourceStatus().getValue());
        updateExpress.set(GameResourceField.LASTMODIFYDATE, new Date());
        updateExpress.set(GameResourceField.LASTMODIFYUSERID, getCurrentUser().getUserid());
        updateExpress.set(GameResourceField.SYNONYMS, synonyms);
        updateExpress.set(GameResourceField.LASTUPDATEDATE, lastUpdateDate);
        if (entity.getEventDesc() != null) {
            updateExpress.set(GameResourceField.EVENTDESC, entity.getEventDesc());
        }
        if (entity.getGameMediaScoreSet() != null && entity.getGameMediaScoreSet().getMediaScores().size() > 0) {
            updateExpress.set(GameResourceField.SCORE, entity.getGameMediaScoreSet().toJsonStr());
        }

        try {
            List<GameProperty> gamePropertyList = new ArrayList<GameProperty>();
            GameProperty channelGameProp1 = buildGameChannel(channelType1, channellink1, channelprice1, 1);
            GameProperty channelGameProp2 = buildGameChannel(channelType2, channellink2, channelprice2, 2);
            GameProperty channelGameProp3 = buildGameChannel(channelType3, channellink3, channelprice3, 3);
            GameProperty channelGameProp4 = buildGameChannel(channelType4, channellink4, channelprice4, 4);
            GameProperty channelGameProp5 = buildGameChannel(channelType5, channellink5, channelprice5, 5);

            if (channelGameProp1 != null) {
                gamePropertyList.add(channelGameProp1);
            }
            if (channelGameProp2 != null) {
                gamePropertyList.add(channelGameProp2);
            }
            if (channelGameProp3 != null) {
                gamePropertyList.add(channelGameProp3);
            }
            if (channelGameProp4 != null) {
                gamePropertyList.add(channelGameProp4);
            }
            if (channelGameProp5 != null) {
                gamePropertyList.add(channelGameProp5);
            }

            GameProperty updateProperty = new GameProperty();
            updateProperty.setGamePropertyDomain(GamePropertyDomain.UPDATEINFO);
            updateProperty.setValue(updateInfo);
            gamePropertyList.add(updateProperty);


            Map<GamePropertyDomain, List<GameProperty>> properyDomainMap = new HashMap<GamePropertyDomain, List<GameProperty>>();
            for (GameProperty property : gamePropertyList) {
                if (!properyDomainMap.containsKey(property.getGamePropertyDomain())) {
                    properyDomainMap.put(property.getGamePropertyDomain(), new ArrayList<GameProperty>());
                }
                properyDomainMap.get(property.getGamePropertyDomain()).add(property);
            }
            boolean bool = gameResourceWebLogic.modifyGameResource(updateExpress, resourceId, properyDomainMap);

//            GameRelationSet gameRelationSet = gameResourceWebLogic.getGameResourceById(resourceId).getGameRelationSet();

//            if (gameRelationSet != null && gameRelationSet.getBoardRelation() != null) {
//                String categoryId = gameRelationSet.getBoardRelation().getRelationValue();

            //
//                QueryExpress categoryQueryExpress = new QueryExpress();
//                categoryQueryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYID, Integer.parseInt(categoryId)));
//                UpdateExpress categoryUpdateExpress = new UpdateExpress();
//                categoryUpdateExpress.set(ViewCategoryField.CATEGORYNAME, resourceName);
//                ViewLineServiceSngl.get().modifyCategory(categoryUpdateExpress, categoryQueryExpress);

            //
//                QueryExpress viewLineQueryExpress = new QueryExpress();
//                viewLineQueryExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, Integer.parseInt(categoryId)))
//                        .add(QueryCriterions.eq(ViewLineField.LOCATIONCODE, LocationCode.GAME_LINK.getCode()));
//                UpdateExpress viewLineUpdateExpress = new UpdateExpress();
//                viewLineUpdateExpress.set(ViewLineField.LINENAME, resourceName);
//                ViewLineServiceSngl.get().modifyViewLine(viewLineUpdateExpress, viewLineQueryExpress);
//            }

            if (bool) {
                //
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GLYXTM_MODIFYGAMERESPAGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter(StringUtil.truncate(entity.toString(), ToolsConstants.SPLIT_SIZE));

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab("Caught an Exception when modify the GameResource :　", e);
            errorMsgMap.put("system", "error.exception");
            return new ModelAndView("/gameresource/preeditgameresource", mapMsg);
        }

        //
        return new ModelAndView("redirect:/gameresource/gameresourcelist");
    }

    @RequestMapping(value = "/gameresourcelist")
    public ModelAndView queryGameResList(
            @RequestParam(value = "resourceName", required = false) String resourceName,
            @RequestParam(value = "removeStatusCode", required = false) String removeStatusCode,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int maxPageItems) {   //每页记录数
        //
        Pagination pagination = new Pagination(((pagerOffset / maxPageItems) + 1) * maxPageItems, (pagerOffset / maxPageItems) + 1, maxPageItems);

        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("resourceName", resourceName);
        mapMsg.put("removeStatusCode", removeStatusCode);

        //
        mapMsg.put("removeStatuses", ActStatus.getAll());

        //
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GAME.getCode()));

        if (ActStatus.getByCode(removeStatusCode) != null) {
            queryExpress.add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.getByCode(removeStatusCode).getCode()));
        }


        if (!Strings.isNullOrEmpty(resourceName)) {
            queryExpress.add(QueryCriterions.like(GameResourceField.RESOURCENAME, "%" + resourceName + "%"));
        }

        queryExpress.add(QuerySort.add(GameResourceField.CREATEDATE, QuerySortOrder.DESC));


        //
        try {
            PageRows<GameResource> pageRows = gameResourceWebLogic.queryGameResources(queryExpress, pagination);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab("queryUserList a Controller ServiceException :", e);
        }

        return new ModelAndView("/gameresource/gameresourcelist", mapMsg);
    }

    @RequestMapping(value = "/batchgameresourcestatus")
    public ModelAndView batchGameResourceStatus(
            @RequestParam(value = "resourceIds", required = false) String resourceIds,
            //
            @RequestParam(value = "resourceName", required = false) String resourceName,
            @RequestParam(value = "removeStatusCode", required = false) String removeStatusCode,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int maxPageItems,
            //
            @RequestParam(value = "updateRemoveStatusCode", required = false) String updateRemoveStatusCode) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("resourceName", resourceName);
        mapMsg.put("removeStatusCode", removeStatusCode);
        mapMsg.put("updateRemoveStatusCode", updateRemoveStatusCode);

        //
        mapMsg.put("removeStatuses", ActStatus.getAll());

        //
        ActStatus updateStatus = ActStatus.getByCode(updateRemoveStatusCode);
        try {
            if (!Strings.isNullOrEmpty(resourceIds) && updateStatus != null) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(GameResourceField.REMOVESTATUS, updateStatus.getCode());
                updateExpress.set(GameResourceField.LASTMODIFYDATE, new Date());
                updateExpress.set(GameResourceField.LASTMODIFYUSERID, String.valueOf(getCurrentUser().getUno()));

                //
                String[] resourceIdSplits = resourceIds.split(",");
                for (String resourceId : resourceIdSplits) {
                    //
                    GameResourceServiceSngl.get().modifyGameResource(updateExpress, new QueryExpress()
                            .add(QueryCriterions.eq(GameResourceField.RESOURCEID, Long.parseLong(resourceId))));
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batchStatusLine, ServiceException :", e);
        }

        return new ModelAndView("forward:/gameresource/gameresourcelist", mapMsg);
    }


    ////////////////////////////////话题////////////////////////////////////
    @RequestMapping(value = "/precreategametopic")
    public ModelAndView createGameTopicPage() {
        return new ModelAndView("/gameresource/precreategametopic");
    }

    @RequestMapping(value = "/creategametopic")
    public ModelAndView createGameTopic(
            @RequestParam(value = "resourceName", required = true) String resourceName,
            @RequestParam(value = "gameCode", required = false) String gameCode,
            @RequestParam(value = "resourceDesc", required = false) String resourceDesc,
            @RequestParam(value = "seokeywords", required = false) String seoKeywords,
            @RequestParam(value = "seodesc", required = false) String seoDesc,
            @RequestParam(value = "picurl_s", required = false) String[] picurl_s,
            @RequestParam(value = "picurl_m", required = false) String[] picurl_m,
            @RequestParam(value = "picurl_b", required = false) String[] picurl_b,
            @RequestParam(value = "picurl_ss", required = false) String[] picurl_ss,
            @RequestParam(value = "picurl_ll", required = false) String[] picurl_ll,
            @RequestParam(value = "picurl_sl", required = false) String[] picurl_sl,
            @RequestParam(value = "thumbimg", required = false) String thumbimg) {

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        long resourceId = 0;

        if (isGameResourceNameExist(resourceName, ResourceDomain.GROUP.getCode(), resourceId)) {
            errorMsgMap.put("resourceName", "error.gameresource.resourcename.exist");
        }

        mapMsg.put("errorMsgMap", errorMsgMap);

        GameResource entity = getGameResourceOnGroup(resourceId, resourceName, gameCode, resourceDesc, seoKeywords, seoDesc, picurl_s,
                picurl_m, picurl_b, picurl_ss, picurl_ll, picurl_sl, thumbimg);

        mapMsg.put("entity", entity);

        if (!StringUtil.isEmpty(entity.getGameCode())) {
            GameResource existsGameResource = null;
            try {
                existsGameResource = GameResourceServiceSngl.get().getGameResource(
                        new QueryExpress().add(QueryCriterions.eq(GameResourceField.GAMECODE, entity.getGameCode()))
                                .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GROUP.getCode())));
            } catch (ServiceException e) {
                GAlerter.lab("Caught an Exception when query GameResources : " + e);
            }

            if (existsGameResource != null) {
                errorMsgMap.put("gamecode", "error.gameresource.gamecode.duplicate");
            }
        }


        if (!errorMsgMap.isEmpty()) {

            return new ModelAndView("/gameresource/preeditgametopic", mapMsg);

        }

        try {
            entity = gameResourceWebLogic.saveGroupResource(entity, getCurrentUser(), null);

            //gameResourceWebLogic.createDefaultRole(entity, getIp(), getCurrentUser().getUserid());

            ToolsLog log = new ToolsLog();

            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.CJHTTM_SAVEGAMETOPICPAGE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter(StringUtil.truncate(entity.toString(), ToolsConstants.SPLIT_SIZE));

            addLog(log);

        } catch (Exception e) {
            GAlerter.lab("saveGameTopicPage a  Exception :", e);

            if (entity.getResourceId() != 0L) {
                try {
                    gameResourceWebLogic.removeGameResource(new QueryExpress()
                            .add(QueryCriterions.eq(GameResourceField.RESOURCEID, entity.getResourceId())));
                } catch (ServiceException e1) {
                    GAlerter.lab("Caught an Exception when delete the GameResource :　" + e);
                }
            }

            errorMsgMap.put("system", "error.exception");
            return new ModelAndView("/gameresource/preeditgametopice", mapMsg);
        }

        return new ModelAndView("redirect:/gameresource/gametopiclist");
    }

    @RequestMapping(value = "/preeditgametopic")
    public ModelAndView preEditGameTopic(
            @RequestParam(value = "resourceId", required = true) Long resourceId) {
        //
        GameResource gameResource = new GameResource();

        Map<String, Object> mapMsg = new HashMap<String, Object>();

        try {
            gameResource = gameResourceWebLogic.getGameResourceById(resourceId);
        } catch (ServiceException e) {
            GAlerter.lab("preEditGameTopic a  Exception :", e);
        }

        mapMsg.put("entity", gameResource);

        return new ModelAndView("/gameresource/preeditgametopic", mapMsg);
    }

    @RequestMapping(value = "/editgametopic")
    public ModelAndView modifyGameTopicPage(
            @RequestParam(value = "resourceId", required = true) Long resourceId,
            //
            @RequestParam(value = "resourceName", required = true) String resourceName,      //游戏条目主名称
            @RequestParam(value = "gameCode", required = false) String gameCode,
            @RequestParam(value = "orggamecode", required = false) String orggamecode,
            @RequestParam(value = "resourceDesc", required = false) String resourceDesc,
            @RequestParam(value = "seokeywords", required = false) String seoKeywords,
            @RequestParam(value = "seodesc", required = false) String seoDesc,
            //
            @RequestParam(value = "picurl_s", required = false) String[] picurl_s,
            @RequestParam(value = "picurl_m", required = false) String[] picurl_m,
            @RequestParam(value = "picurl_b", required = false) String[] picurl_b,
            @RequestParam(value = "picurl_ss", required = false) String[] picurl_ss,
            @RequestParam(value = "picurl_ll", required = false) String[] picurl_ll,
            @RequestParam(value = "picurl_sl", required = false) String[] picurl_sl,
            @RequestParam(value = "thumbimg", required = false) String thumbimg) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        Map<String, String> errorMsgMap = new HashMap<String, String>();

        if (isGameResourceNameExist(resourceName, ResourceDomain.GROUP.getCode(), resourceId)) {
            errorMsgMap.put("resourceName", "error.gameresource.resourcename.exist");
        }

        if (StringUtil.isEmpty(gameCode)) {
            errorMsgMap.put("gamecode", "error.gameresource.gamecode.null");
        } else if (!StringUtil.isEmpty(gameCode) && !orggamecode.equals(gameCode)) {
            GameResource existsGameResource = null;
            try {
                existsGameResource = GameResourceServiceSngl.get().getGameResource(
                        new QueryExpress().add(QueryCriterions.eq(GameResourceField.GAMECODE, gameCode))
                                .add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GROUP.getCode())));
            } catch (ServiceException e) {
                GAlerter.lab("Caught an Exception when query GameResources : " + e);
            }

            if (existsGameResource != null) {
                errorMsgMap.put("gamecode", "error.gameresource.gamecode.duplicate");
            }
        }
        mapMsg.put("errorMsgMap", errorMsgMap);

        //
        GameResource entity = getGameResourceOnGroup(resourceId, resourceName, gameCode, resourceDesc, seoKeywords, seoDesc, picurl_s,
                picurl_m, picurl_b, picurl_ss, picurl_ll, picurl_sl, thumbimg);

        mapMsg.put("entity", entity);

        if (!errorMsgMap.isEmpty()) {
            return new ModelAndView("/gameresource/preeditgametopic", mapMsg);
        }
        //
        UpdateExpress updateExpress = new UpdateExpress();

        updateExpress.set(GameResourceField.RESOURCENAME, resourceName);
        updateExpress.set(GameResourceField.GAMECODE, gameCode);

        updateExpress.set(GameResourceField.ICON, entity.getIcon().toJsonStr());
        updateExpress.set(GameResourceField.RESOURCEDESC, resourceDesc);

        updateExpress.set(GameResourceField.SEOKEYWORDS, seoKeywords);
        updateExpress.set(GameResourceField.SEODESCRIPTION, seoDesc);

        updateExpress.set(GameResourceField.LASTMODIFYUSERID, getCurrentUser().getUserid());
        updateExpress.set(GameResourceField.LASTMODIFYDATE, new Date());

        if (entity.getResourceThumbimg() == null) {
            updateExpress.set(GameResourceField.THUMBIMG, null);
        } else {
            updateExpress.set(GameResourceField.THUMBIMG, entity.getResourceThumbimg().toJsonStr());
        }


        try {
            boolean bool = gameResourceWebLogic.modifyGroupResource(updateExpress, resourceId);

            if (bool) {
                ToolsLog log = new ToolsLog();

                log.setOpUserId(getCurrentUser().getUserid());
                log.setOperType(LogOperType.GLHTTM_MODIFYGAMETOPICPAGE);
                log.setOpTime(new Date());
                log.setOpIp(getIp());
                log.setOpAfter(StringUtil.truncate(entity.toString(), ToolsConstants.SPLIT_SIZE));

                addLog(log);
            }
        } catch (ServiceException e) {
            GAlerter.lab("editResourcePage a resource ServiceException :", e);

            errorMsgMap.put("system", "error.exception");
            return new ModelAndView("/gameresource/preeditgametopice", mapMsg);
        }

        return new ModelAndView("redirect:/gameresource/gametopiclist");
    }


    @RequestMapping(value = "/gametopiclist")
    public ModelAndView queryGameTopicList(
            //
            @RequestParam(value = "resourceName", required = false) String resourceName,
            @RequestParam(value = "removeStatusCode", required = false) String removeStatusCode,
            //
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pagerOffset,      //数据库记录索引
            @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int maxPageItems) {   //每页记录数
        //
        Pagination page = new Pagination(((pagerOffset / maxPageItems) + 1) * maxPageItems, (pagerOffset / maxPageItems) + 1, maxPageItems);

        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("resourceName", resourceName);
        mapMsg.put("removeStatusCode", removeStatusCode);

        mapMsg.put("removeStatuses", ActStatus.getAll());

        //
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(GameResourceField.RESOURCEDOMAIN, ResourceDomain.GROUP.getCode()));
        if (ActStatus.getByCode(removeStatusCode) != null) {
            queryExpress.add(QueryCriterions.eq(GameResourceField.REMOVESTATUS, ActStatus.getByCode(removeStatusCode).getCode()));
        }

        if (!Strings.isNullOrEmpty(resourceName)) {
            queryExpress.add(QueryCriterions.like(GameResourceField.RESOURCENAME, "%" + resourceName + "%"));
        }

        queryExpress.add(QuerySort.add(GameResourceField.CREATEDATE, QuerySortOrder.DESC));

        try {
            PageRows<GameResource> pageRows = gameResourceWebLogic.queryGameResources(queryExpress, page);

            mapMsg.put("rows", pageRows.getRows());
            mapMsg.put("page", pageRows.getPage());
        } catch (ServiceException e) {
            GAlerter.lab("gametopiclist a Controller ServiceException :", e);
        }

        return new ModelAndView("/gameresource/gametopiclist", mapMsg);
    }

    @RequestMapping(value = "/batchgametopicstatus")
    public ModelAndView batchGameTopicStatus(
            @RequestParam(value = "resourceIds", required = false) String resourceIds,
            //
            @RequestParam(value = "resourceName", required = false) String resourceName,
            @RequestParam(value = "removeStatusCode", required = false) String removeStatusCode,
            @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
            //
            @RequestParam(value = "updateRemoveStatusCode", required = false) String updateRemoveStatusCode) {
        //
        Map<String, Object> mapMsg = new HashMap<String, Object>();

        mapMsg.put("resourceName", resourceName);
        mapMsg.put("removeStatusCode", removeStatusCode);
        mapMsg.put("updateRemoveStatusCode", updateRemoveStatusCode);

        //
        mapMsg.put("removeStatuses", ActStatus.getAll());

        //
        UpdateExpress updateExpress = new UpdateExpress();

        if (ActStatus.getByCode(updateRemoveStatusCode) != null) {
            updateExpress.set(GameResourceField.REMOVESTATUS, ActStatus.getByCode(updateRemoveStatusCode).getCode());
        }

        updateExpress.set(GameResourceField.LASTMODIFYDATE, new Date());
        updateExpress.set(GameResourceField.LASTMODIFYUSERID, String.valueOf(getCurrentUser().getUserid()));

        //
        try {
            if (!Strings.isNullOrEmpty(resourceIds) && ActStatus.getByCode(updateRemoveStatusCode) != null) {
                //
                String[] resourceIdSplits = resourceIds.split(",");
                for (String resourceId : resourceIdSplits) {
                    //
                    gameResourceWebLogic.modifyGroupResource(updateExpress, Long.valueOf(resourceId));
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batchStatusLine, ServiceException :", e);
        }

        return new ModelAndView("forward:/gameresource/gametopiclist", mapMsg);
    }

    @RequestMapping(value = "/redrtalk")
    public ModelAndView redrTalk(@RequestParam(value = "cateid") Integer categoryId,
                                 @RequestParam(value = "resourcedomain") String resourceDomain) {
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(ViewCategoryField.PARENTCATEGORYID, categoryId))
                .add(QueryCriterions.eq(ViewCategoryField.CATEGORYASPECT, ViewCategoryAspect.CONTENT_BOARD.getCode()));

        if (!StringUtil.isEmpty(resourceDomain) && resourceDomain.equals(ResourceDomain.GAME.getCode())) {
            queryExpress.add(QueryCriterions.eq(ViewCategoryField.LOCATIONCODE, LocationCode.DOWNLOAD_LINK.getCode()));
        } else if (resourceDomain.equals(ResourceDomain.GROUP.getCode())) {
            queryExpress.add(QueryCriterions.eq(ViewCategoryField.LOCATIONCODE, LocationCode.TALK_BOARD.getCode()));
        }
        try {
            ViewCategory category = ViewLineServiceSngl.get().getCategory(queryExpress);

            if (category != null) {
                return new ModelAndView("redirect:/viewline/categorydetail?categoryId=" + category.getCategoryId());
            }

        } catch (ServiceException e) {
            GAlerter.lab(" redr talk get category error.", e);
            return new ModelAndView("/jsp/common/500");
        }

        return new ModelAndView("/jsp/common/404");
    }


    @RequestMapping(value = "/resdetail")
    public ModelAndView resDetail(@RequestParam(value = "resid", defaultValue = "0") long resourceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId is =" + resourceId);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();

        if (resourceId != 0) {
            try {
                GameResource gameResource = gameResourceWebLogic.getGameResourceById(resourceId);

                msgMap.put("entity", gameResource);
            } catch (ServiceException e) {

                GAlerter.lab("query a GameResource caught an exception:", e);
            }
        }

        return new ModelAndView("/gameresource/resourcedetail", msgMap);

    }


    @RequestMapping(value = "/relationlist")
    public ModelAndView relationList(@RequestParam(value = "resid", defaultValue = "0") long resourceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId is =" + resourceId);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();

        if (resourceId != 0) {
            try {
//                GameResource gameResource = gameResourceWebLogic.getGameResourceById(resourceId);

                List<GameRelationDTO> relationList = gameResourceWebLogic.queryGameRelationDTObyResourceId(resourceId);

                msgMap.put("relationList", relationList);

//                msgMap.put("relationList", gameResource.getGameRelationSet() == null ? new ArrayList() : gameResource.getGameRelationSet().getGameRelationList());
                msgMap.put("resid", resourceId);
                msgMap.put("validStatuses", ValidStatus.getAll());
            } catch (ServiceException e) {

                GAlerter.lab("query a GameResource caught an exception:", e);
            }
        }

        return new ModelAndView("/gameresource/tab_relationlist", msgMap);
    }

    @RequestMapping(value = "/preaddresrelation")
    public ModelAndView preAddResRelation(@RequestParam(value = "resid", defaultValue = "0") long resourceId) {
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId is =" + resourceId);
        }

        Map<String, Object> msgMap = new HashMap<String, Object>();
        List<GameRelationType> relationTypes = new ArrayList<GameRelationType>();
        relationTypes.add(GameRelationType.GAME_RELATION_TYPE_LINK);
        relationTypes.add(GameRelationType.GAME_RELATION_TYPE_COVER);
        relationTypes.add(GameRelationType.GAME_RELATION_TYPE_INVITE);
        relationTypes.add(GameRelationType.GAME_RELATION_TYPE_RELATED_GAME);
        relationTypes.add(GameRelationType.GAME_RELATION_TYPE_RELATED_GROUP);
        relationTypes.add(GameRelationType.GAME_RELATION_TYPE_BAIKE);

        msgMap.put("resid", resourceId);
        msgMap.put("validStatus", ValidStatus.getAll());
        msgMap.put("relationTypes", relationTypes);

        return new ModelAndView("/gameresource/preaddresrelation", msgMap);
    }

    @RequestMapping(value = "/addresrelation")
    public ModelAndView addResRelation(@RequestParam(value = "resid", defaultValue = "0") long resourceId,
                                       @RequestParam(value = "relationType", required = false) String relationType,
                                       @RequestParam(value = "relationValue", required = false) String relationValue,
                                       @RequestParam(value = "relationName", required = false) String relationName,
                                       @RequestParam(value = "sortNum", required = false) Integer sortNum,
                                       @RequestParam(value = "validStatus", required = false) String validStatus) {
        if (logger.isDebugEnabled()) {
            logger.debug("resourceId is =" + resourceId);
        }

        relationValue = relationValue.trim();

        Map<String, Object> msgMap = new HashMap<String, Object>();

        msgMap.put("resid", resourceId);
        msgMap.put("relationType", relationType);
        msgMap.put("relationValue", relationValue);
        msgMap.put("sortNum", sortNum);
        msgMap.put("validStatus", validStatus);

        GameRelation gameRelation = new GameRelation();

        if (resourceId != 0) {
            gameRelation.setResourceId(resourceId);
        }
        if (!StringUtil.isEmpty(relationType)) {
            gameRelation.setGameRelationType(GameRelationType.getByCode(relationType));
        }
        if (!StringUtil.isEmpty(relationValue)) {
            gameRelation.setRelationValue(relationValue);
        }
        if (!StringUtil.isEmpty(relationName)) {
            gameRelation.setRelationName(relationName);
        }
        if (sortNum != null) {
            gameRelation.setSortNum(sortNum);
        }
        if (!StringUtil.isEmpty(validStatus)) {
            gameRelation.setValidStatus(ValidStatus.getByCode(validStatus));
        }

        try {
            gameResourceWebLogic.saveGameRelation(gameRelation);
        } catch (ServiceException e) {
            GAlerter.lab("create GameRelation occurred an exception:", e);
        }

        return new ModelAndView("forward:/gameresource/relationlist", msgMap);
    }


    @RequestMapping(value = "/batchupdaterelation")
    public ModelAndView batchUpdateRelationStatus(@RequestParam(value = "resid", defaultValue = "0") long resourceId,
                                                  @RequestParam(value = "relationids", required = false) String relationids,
                                                  @RequestParam(value = "updateValidStatusCode", required = false) String updateValidStatusCode) {

        Map<String, Object> mapMsg = new HashMap<String, Object>();
        mapMsg.put("resid", resourceId);

        try {
            if (!StringUtil.isEmpty(relationids)) {
                String[] relationIdArray = relationids.split(",");

                QueryExpress queryExpress = new QueryExpress();

                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(GameRelationField.VALIDSTATUS, updateValidStatusCode);

                for (String relationId : relationIdArray) {
                    queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONID, Long.parseLong(relationId)));

                    GameResourceServiceSngl.get().modifyGameRelation(updateExpress, queryExpress);

                }
            }
        } catch (ServiceException e) {
            GAlerter.lab("batch delete privacy, ServiceException :", e);
        }

        return new ModelAndView("forward:/gameresource/relationlist", mapMsg);

    }

    @RequestMapping(value = "/preupdaterelation")
    public ModelAndView preUpdateRelation(
            @RequestParam(value = "resid") Long resourceId,
            @RequestParam(value = "relationid") Long relationId) {
        Map<String, Object> msgMap = new HashMap<String, Object>();

        GameRelationDTO gameRelation = null;
        try {
            GameResource gameResource = gameResourceWebLogic.getGameResourceById(resourceId);

            gameRelation = gameResourceWebLogic.getGameRelationDTObyResourceId(gameResource, relationId);
        } catch (ServiceException e) {
            GAlerter.lab("get an GameRelation, ServiceException :", e);
        }
        List<GameRelationType> relationTypes = new ArrayList<GameRelationType>();
        relationTypes.add(GameRelationType.GAME_RELATION_TYPE_LINK);
        relationTypes.add(GameRelationType.GAME_RELATION_TYPE_COVER);

        msgMap.put("gameRelation", gameRelation);
        msgMap.put("validStatus", ValidStatus.getAll());
        msgMap.put("relationTypes", relationTypes);

        return new ModelAndView("/gameresource/preupdaterelation", msgMap);
    }

    @RequestMapping(value = "/updaterelation")
    public ModelAndView updateRelation(@RequestParam(value = "resid", defaultValue = "0") long resourceId,
                                       @RequestParam(value = "relationid", defaultValue = "0") long relationId,
                                       @RequestParam(value = "relationType", required = false) String relationType,
                                       @RequestParam(value = "relationValue", required = false) String relationValue,
                                       @RequestParam(value = "relationName", required = false) String relationName,
                                       @RequestParam(value = "sortNum", defaultValue = "0") int sortNum) {
        Map<String, Object> msgMap = new HashMap<String, Object>();

        msgMap.put("resid", resourceId);

//        GameRelationType type = GameRelationType.getByCode(relationType);
//        if (type == null) {
//            GAlerter.lan(this.getClass().getName() + " updaterelation relationType illegel.type: " + relationType);
//        }

        GameRelation gameRelation = new GameRelation();
        gameRelation.setRelationId(relationId);
        gameRelation.setRelationName(relationName.trim());
        gameRelation.setRelationValue(relationValue.trim());
//        gameRelation.setGameRelationType(type);
        gameRelation.setResourceId(resourceId);

        try {
            gameResourceWebLogic.modifyGameRelation(gameRelation);
        } catch (ServiceException e) {
            GAlerter.lab("modify an GameRelation, ServiceException :", e);
        }

//        QueryExpress queryExpress = new QueryExpress();
//        queryExpress.add(QueryCriterions.eq(GameRelationField.RELATIONID, relationId));
//
//        UpdateExpress updateExpress = new UpdateExpress();
////        updateExpress.set(GameRelationField.RELATIONTYPE, relationType);
//        updateExpress.set(GameRelationField.RELATIONVALUE, relationValue);
//        updateExpress.set(GameRelationField.RELATIONNAME, relationName);
//
//        updateExpress.set(GameRelationField.SORTNUM, sortNum);
//
//        try {
//            GameResourceServiceSngl.get().modifyGameRelation(updateExpress, queryExpress);
//        } catch (ServiceException e) {
//            GAlerter.lab("modify an GameRelation, ServiceException :", e);
//        }

        return new ModelAndView("forward:/gameresource/relationlist", msgMap);
    }

    private List<String> getJsonListValue(Map<String, Boolean> checkMap) {
        List<String> returnList = new ArrayList<String>();
        //过滤掉不存在的
        Iterator<Map.Entry<String, Boolean>> iterator = checkMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Boolean> entry = iterator.next();
            if (!entry.getValue()) {
                iterator.remove();
            }
        }

        if (!checkMap.isEmpty()) {
            for (Map.Entry<String, Boolean> object : checkMap.entrySet()) {
                returnList.add(object.getKey());
            }
        }

        return returnList;
    }

    private boolean isGameResourceNameExist(String resourceName, String resourceDomain, long resourceId) {
        //准备参数
        Set<String> set = new HashSet<String>();
        set.add(resourceName);

        try {
            //游戏条目名称是否存在于其他游戏条目名称字段中
            boolean bool = gameResourceWebLogic.isGameResourceNameExist(resourceName, resourceDomain, resourceId);
            //游戏条目名称是否存在于同义词组中
            Map<String, Boolean> map = gameResourceWebLogic.isSynonymsesExist(set, resourceId);

            return map.size() <= 1 && (map.size() == 0) ? bool : (map.get(resourceName) || bool);

        } catch (ServiceException e) {
            GAlerter.lab("GameResourceController caught an Exception :" + e);
        }

        return true;   //true or false?

    }

    private boolean isSynonymExist(Set<String> synonymses, long resourceId) {
        try {
            //同义词存在于游戏条目名称否？
            boolean isSynonymExistInGameResourceName = gameResourceWebLogic.isGameResourceNameExistBySynonym(synonymses);

            //同义词存在于其他同义词记录中否？
            List list = getJsonListValue(gameResourceWebLogic.isSynonymsesExist(synonymses, resourceId));

            return isSynonymExistInGameResourceName || !list.isEmpty();
        } catch (ServiceException e) {
            GAlerter.lab("isSynonymExist caught an Exception: " + e);
        }
        return true; //true or false?
    }

    private GameResource getGameResourceOnGame(
            Long resourceId,
            String resourceName,      //游戏条目主名称
            String gameCode,
            String resourceDomain,//
            String device,                 //游戏平台
            String logoSize,     //主图类别
            String resourceCategory,  //类型
            String resourceStyle,  //风格：无该字段
            String resourceStyleValue,  //风格：无该字段
            String publishCompany,  //发行商/运营商
            String publishCompanyValue,  //发行商/运营商
            String develop,        //开发商
            String developValue,        //开发商
            String playerNumber,  //游戏人数
            String playerNumberValue,  //游戏人数
            String fileSize,     //游戏大小
            String fileType,     //游戏大小单位
            String fileSizeValue,     //游戏大小
            String price,
            String priceValue,
            String publishDate, //发售日期
            String publishDateValue, //发售日期
            String resourceUrl,    //官网
            String resourceUrlValue,    //官网
            String language,         //语言
            String languageValue,         //语言
            String resourceDesc,   //游戏简介
            String resourceDescValue,   //游戏简介
            String seoKeyWords,
            String synonyms,
            String seoDescription,
            String[] picurl_s,
            String[] picurl_m,
            String[] picurl_b,
            String[] picurl_ss,
            String[] picurl_ll,
            String[] picurl_sl,
            String eventDesc,
            String thumbimg,
            String score1,
            String from1,
            String desc1,
            String score2,
            String from2,
            String desc2,
            String lastUpdateDate,
            String lastUpdateDateValue,
            String buyLinkValue) {
        GameResource entity = new GameResource();

        if (resourceId != 0) {
            entity.setResourceId(resourceId);
        } else {
            PrivilegeUser user = getCurrentUser();
            entity.setCreateDate(new Date());
            entity.setCreateUserid(user.getUserid());
        }

        if (!StringUtil.isEmpty(gameCode)) {
            entity.setGameCode(gameCode);
        }

        if (!Strings.isNullOrEmpty(resourceName)) {
            entity.setResourceName(resourceName);
        }
        if (!Strings.isNullOrEmpty(resourceDomain)) {
            entity.setResourceDomain(ResourceDomain.getByCode(resourceDomain));
        }
        if (!Strings.isNullOrEmpty(logoSize)) {
            entity.setLogoSize(logoSize);
        }
        if (!Strings.isNullOrEmpty(publishCompany)) {
            entity.setPublishCompany(publishCompany);
        }
        if (!Strings.isNullOrEmpty(develop)) {
            entity.setDevelop(develop);
        }
        if (!Strings.isNullOrEmpty(playerNumber)) {
            entity.setPlayerNumber(playerNumber);
        }
        if (!Strings.isNullOrEmpty(fileType) && !Strings.isNullOrEmpty(fileSize)) {
            entity.setFileSize(fileSize + fileType);
        } else if (Strings.isNullOrEmpty(fileType) && !Strings.isNullOrEmpty(fileSize)) {
            entity.setFileSize(fileSize);
        }
        if (!Strings.isNullOrEmpty(price)) {
            entity.setPrice(price);
        }
        if (!Strings.isNullOrEmpty(publishDate)) {
            entity.setPublishDate(publishDate);
        }
        if (!Strings.isNullOrEmpty(resourceUrl)) {
            entity.setResourceUrl(resourceUrl);
        }
        if (!Strings.isNullOrEmpty(language)) {
            entity.setLanguage(language);
        }
        if (!Strings.isNullOrEmpty(resourceDesc)) {
            entity.setResourceDesc(resourceDesc);
        }
        if (!Strings.isNullOrEmpty(seoDescription)) {
            entity.setSeoDescription(seoDescription);
        }
        if (!Strings.isNullOrEmpty(seoKeyWords)) {
            entity.setSeoKeyWords(seoKeyWords);
        }
        if (!Strings.isNullOrEmpty(synonyms)) {
            entity.setSynonyms(synonyms);
        }

        entity.setLastUpdateDate(lastUpdateDate);

        //获取是否隐藏字段信息
        GameResourceStatus resourceStatus = new GameResourceStatus(0);
        if (!Strings.isNullOrEmpty(resourceStyleValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_RESOURCE_STYLE);
        }
        if (!Strings.isNullOrEmpty(publishCompanyValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_PUBLISH_COMPANY);
        }
        if (!Strings.isNullOrEmpty(developValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_DEVELOP);
        }
        if (!Strings.isNullOrEmpty(playerNumberValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_PLAYER_NUMBER);
        }
        if (!Strings.isNullOrEmpty(fileSizeValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_FILE_SIZE);
        }
        if (!Strings.isNullOrEmpty(priceValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_RESOURCE_PRICE);
        }
        if (!Strings.isNullOrEmpty(publishDateValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_PUBLISH_DATE);
        }
        if (!Strings.isNullOrEmpty(resourceUrlValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_RESOURCE_URL);
        }
        if (!Strings.isNullOrEmpty(languageValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_LANGUAGE);
        }
        if (!Strings.isNullOrEmpty(resourceDescValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_RESOURCE_DESC);
        }
        if (!Strings.isNullOrEmpty(lastUpdateDateValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_RESOURCE_LASTUPDATEDATE);
        }
        if (!Strings.isNullOrEmpty(buyLinkValue)) {
            resourceStatus.hidden(GameResourceStatus.HIDDEN_RESOURCE_BUYLINK);
        }
        entity.setResourceStatus(resourceStatus);

        //
        ResourceImageSet set = new ResourceImageSet();
        if (picurl_s != null && picurl_m != null && picurl_ss != null && picurl_b != null && picurl_ll != null && picurl_sl != null) {
            for (int i = 0; i < picurl_b.length; i++) {
                ResourceImage resourceImage = new ResourceImage();
                resourceImage.setUrl(picurl_b[i] + "");
                resourceImage.setM(picurl_m[i] + "");
                resourceImage.setS(picurl_s[i] + "");
                resourceImage.setSs(picurl_ss[i] + "");
                resourceImage.setLl(picurl_ll[i] + "");
                resourceImage.setSl(picurl_sl[i] + "");
                resourceImage.setId(i);
                set.add(resourceImage);
            }
        }
        if (set.getImages().size() > 0) {
            entity.setIcon(set);
        }

        GameDeviceSet deviceSet = new GameDeviceSet();
        if (!StringUtil.isEmpty(device)) {
            String[] str = StringUtil.splitString(device, ",");
            for (String s : str) {
                deviceSet.add(GameDevice.getByCode(s));
            }
            entity.setDeviceSet(deviceSet);
        }

        ImageContentSet thumbimgSet = new ImageContentSet();
        if (!StringUtil.isEmpty(thumbimg)) {
            ImageContent imageContent = new ImageContent();
            imageContent.setUrl(thumbimg);
            thumbimgSet.add(imageContent);
        }
        entity.setResourceThumbimg(thumbimgSet);

        GameCategorySet categorySet = new GameCategorySet();
        if (!StringUtil.isEmpty(resourceCategory)) {

            String[] categoryStr = StringUtil.splitString(resourceCategory, ",");
            for (String s : categoryStr) {
                categorySet.add(GameCategory.getByCode(s));
            }
            entity.setCategorySet(categorySet);
        }

        GameStyleSet styleSet = new GameStyleSet();
        if (!StringUtil.isEmpty(resourceStyle)) {

            String[] styleStr = StringUtil.splitString(resourceStyle, ",");
            for (String s : styleStr) {
                styleSet.add(GameStyle.getByCode(s));
            }
            entity.setStyleSet(styleSet);
        }

        entity.setEventDesc(eventDesc);

        GameMediaScoreSet gameMediaScoreSet = new GameMediaScoreSet();

        if (!StringUtil.isEmpty(score1) || !StringUtil.isEmpty(from1) || !StringUtil.isEmpty(desc1)) {
            List<GameMediaScore> scores = new ArrayList<GameMediaScore>();
            GameMediaScore score = new GameMediaScore();
            score.setScore(score1);
            score.setFrom(from1);
            score.setDescription(desc1);
            scores.add(score);
            gameMediaScoreSet.add(scores);
        }
        if (!StringUtil.isEmpty(score2) || !StringUtil.isEmpty(from2) || !StringUtil.isEmpty(desc2)) {
            List<GameMediaScore> scores = new ArrayList<GameMediaScore>();
            GameMediaScore score = new GameMediaScore();
            score.setScore(score2);
            score.setFrom(from2);
            score.setDescription(desc2);
            scores.add(score);
            gameMediaScoreSet.add(scores);
        }

        if (gameMediaScoreSet.getMediaScores().size() > 0) {
            entity.setGameMediaScoreSet(gameMediaScoreSet);
        }
        return entity;
    }

    private GameResource getGameResourceOnGroup(long resourceId,
                                                String resourceName,
                                                String gameCode,
                                                String resourceDesc,
                                                String seoKeywords,
                                                String seoDesc,
                                                String[] picurl_s,
                                                String[] picurl_m,
                                                String[] picurl_b,
                                                String[] picurl_ss,
                                                String[] picurl_ll,
                                                String[] picurl_sl, String thumbimg) {

        GameResource entity = new GameResource();

        if (resourceId != 0) {
            entity.setResourceId(resourceId);
        } else {
            entity.setResourceDomain(ResourceDomain.GROUP);
            entity.setCreateDate(new Date());
        }

        if (!Strings.isNullOrEmpty(resourceName)) {
            entity.setResourceName(resourceName);
        }
        if (!Strings.isNullOrEmpty(gameCode)) {
            entity.setGameCode(gameCode);
        }
        if (!Strings.isNullOrEmpty(resourceDesc)) {
            entity.setResourceDesc(resourceDesc);
        }
        if (!Strings.isNullOrEmpty(seoDesc)) {
            entity.setSeoDescription(seoDesc);
        }
        if (!Strings.isNullOrEmpty(seoKeywords)) {
            entity.setSeoKeyWords(seoKeywords);
        }
        entity.setCreateUserid(getCurrentUser().getUserid());
        ResourceImageSet set = new ResourceImageSet();

        if (picurl_s != null && picurl_m != null && picurl_ss != null && picurl_b != null && picurl_ll != null && picurl_sl != null) {
            for (int i = 0; i < picurl_b.length; i++) {
                ResourceImage resourceImage = new ResourceImage();
                resourceImage.setUrl(picurl_b[i] + "");
                resourceImage.setM(picurl_m[i] + "");
                resourceImage.setS(picurl_s[i] + "");
                resourceImage.setSs(picurl_ss[i] + "");
                resourceImage.setLl(picurl_ll[i] + "");
                resourceImage.setSl(picurl_sl[i] + "");
                resourceImage.setId(i);

                set.add(resourceImage);
            }
        }

        ImageContentSet thumbimgSet = new ImageContentSet();
        if (!StringUtil.isEmpty(thumbimg)) {
            ImageContent imageContent = new ImageContent();
            imageContent.setUrl(thumbimg);
            thumbimgSet.add(imageContent);
        }
        entity.setResourceThumbimg(thumbimgSet);


        if (set.getImages().size() > 0) {
            entity.setIcon(set);
        }

        return entity;
    }

    private GameProperty buildGameChannel(String type, String link, String price, int sortNum) {
        if (StringUtil.isEmpty(type)) {
            return null;
        }

        GameProperty returnObj = new GameProperty();
        returnObj.setGamePropertyDomain(GamePropertyDomain.DOMAIN_CHANNEL);
        returnObj.setPropertyType(type);
        returnObj.setValue(link);
        returnObj.setValue2(price);
        returnObj.setSortNum(sortNum);
        return returnObj;
    }
}
