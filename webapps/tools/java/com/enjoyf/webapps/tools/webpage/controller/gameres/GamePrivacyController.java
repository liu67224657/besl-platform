package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ToolsHotdeployConfig;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.profile.ProfileBlog;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsConstants;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import com.enjoyf.webapps.tools.weblogic.dto.game.GamePrivacyDTO;
import com.enjoyf.webapps.tools.weblogic.gameres.GameResourceWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-25
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
@Controller
@RequestMapping(value = "/gameresource/privacy")
public class GamePrivacyController extends ToolsBaseController {

    private Logger logger = LoggerFactory.getLogger(GamePrivacyController.class);

    //
    @Resource(name = "gameResourceWebLogic")
    private GameResourceWebLogic gameResourceWebLogic;

    //
    @RequestMapping(value = "/list")
    public ModelAndView privacyListPage(@RequestParam(value = "resid") Long resrouceId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("resid", resrouceId);
        try {
            List<GamePrivacyDTO> privacyList = gameResourceWebLogic.queryPrivacy(resrouceId, ResourceDomain.GAME);

            mapMessage.put("privacyList", privacyList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/gameresource/tab_privacy", mapMessage);
    }

    //
    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "resid") Long resrouceId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        mapMessage.put("privacyLevelList", GamePrivacyLevel.getAll());
        mapMessage.put("resid", resrouceId);

        return new ModelAndView("/gameresource/tab_createprivacy", mapMessage);
    }


    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "resid") Long resrouceId,
                               @RequestParam(value = "screenname") String screenName,
                               @RequestParam(value = "level") String level) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("privacyLevelList", GamePrivacyLevel.getAll());
        mapMessage.put("level", level);
        mapMessage.put("screename", screenName);
        mapMessage.put("resid", resrouceId);

        try {

            GamePrivacyLevel gamePrivacyLevel = GamePrivacyLevel.getByCode(level);
            if (gamePrivacyLevel == null) {
                mapMessage = putErrorMessage(mapMessage, "game.privacy.wrong");
                return new ModelAndView("/gameresource/tab_createprivacy", mapMessage);
            }

            if (gameResourceWebLogic.getGameResourceById(resrouceId) == null) {
                mapMessage = putErrorMessage(mapMessage, "game.not.exists");
                return new ModelAndView("/gameresource/tab_createprivacy", mapMessage);
            }

            ProfileBlog profileBlog = ProfileServiceSngl.get().getProfileBlogByScreenName(screenName);
            if (profileBlog == null) {
                mapMessage = putErrorMessage(mapMessage, "profile.has.notexists");
                return new ModelAndView("/gameresource/tab_createprivacy", mapMessage);
            }

            if (gameResourceWebLogic.privacyHasExists(resrouceId, profileBlog.getUno(), gamePrivacyLevel, ResourceDomain.GAME)) {
                mapMessage = putErrorMessage(mapMessage, "game.privacy.hasexists");
                return new ModelAndView("/gameresource/tab_createprivacy", mapMessage);
            }

            GamePrivacy gamePrivacy = new GamePrivacy();
            gamePrivacy.setUno(profileBlog.getUno());
            gamePrivacy.setResourceId(resrouceId);
            gamePrivacy.setPrivacyLevel(GamePrivacyLevel.getByCode(level));
            gamePrivacy.setResourceDomain(ResourceDomain.GAME);
            gamePrivacy.setCreateDate(new Date());
            gamePrivacy.setCreateUserid(getCurrentUser().getUserid());

            GameResourceServiceSngl.get().createGamePrivacy(gamePrivacy);

            //
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.GAMEREPRIVACY_CREATE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("create:" + gamePrivacy);

            addLog(log);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " cratePage occured ServiceException.e: ", e);
        }


        return new ModelAndView("redirect:/gameresource/privacy/list?resid=" + resrouceId);
    }


    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "resid") Long resrouceId,
                               @RequestParam(value = "uno") String uno,
                               @RequestParam(value = "level") String level) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        GamePrivacyLevel gamePrivacyLevel = GamePrivacyLevel.getByCode(level);
        if (gamePrivacyLevel == null) {
            return new ModelAndView("redirect:/gameresource/privacy/list?resid=" + resrouceId);
        }

        try {
            gameResourceWebLogic.deleteUserPrivacy(resrouceId, uno, gamePrivacyLevel, ResourceDomain.GAME);

            //todo addlog
            //
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.GAMEREPRIVACY_DELETE);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpBefore("delete: uno " + uno + " resourseId:" + resrouceId + " level:" + level);

            addLog(log);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " cratePage occured ServiceException.e: ", e);
        }

        return new ModelAndView("redirect:/gameresource/privacy/list?resid=" + resrouceId);
    }

    //
    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "resid") Long resrouceId,
                                   @RequestParam(value = "uno") String uno,
                                   @RequestParam(value = "level") String level) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("resid", resrouceId);

        mapMessage.put("privacyLevelList", GamePrivacyLevel.getAll());
        GamePrivacyLevel gamePrivacyLevel = GamePrivacyLevel.getByCode(level);
        if (gamePrivacyLevel == null) {
            logger.error("wrong privacy level" + level);
            return new ModelAndView("redirect:/gameresource/privacy/list?resid=" + resrouceId);
        }

        try {
            GamePrivacyDTO privactDTO = gameResourceWebLogic.getPrivacyDTO(resrouceId, uno, gamePrivacyLevel, ResourceDomain.GAME);

            mapMessage.put("privacy", privactDTO);
            mapMessage.put("level", level);
            mapMessage.put("screenname", privactDTO.getScreenName());
            mapMessage.put("uno", privactDTO.getGamePrivacy().getUno());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/gameresource/tab_modifyprivacy", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "resid") Long resrouceId,
                               @RequestParam(value = "screenname") String screenName,
                               @RequestParam(value = "uno") String uno,
                               @RequestParam(value = "level") String level) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("privacyLevelList", GamePrivacyLevel.getAll());
        mapMessage.put("level", level);
        mapMessage.put("resid", resrouceId);
        mapMessage.put("screenname", screenName);
        mapMessage.put("uno", uno);

        try {

            GamePrivacyLevel gamePrivacyLevel = GamePrivacyLevel.getByCode(level);
            if (gamePrivacyLevel == null) {
                mapMessage = putErrorMessage(mapMessage, "game.privacy.wrong");
                return new ModelAndView("/gameresource/tab_modifyprivacy", mapMessage);
            }


            GameResourceServiceSngl.get().modifyGamePrivacy(
                    new UpdateExpress().set(GamePrivacyField.PRIVCAYLEVEL, gamePrivacyLevel.getCode())
                            .set(GamePrivacyField.UPDATEDATE, new Date())
                            .set(GamePrivacyField.UPDATEUSERID, getCurrentUser().getUserid()),
                    new QueryExpress()
                            .add(QueryCriterions.eq(GamePrivacyField.RESOURCEID, resrouceId))
                            .add(QueryCriterions.eq(GamePrivacyField.UNO, uno))
                            .add(QueryCriterions.eq(GamePrivacyField.RESOURCEDOMAIN, ResourceDomain.GAME.getCode())));

            //todo addlog
            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());
            log.setOperType(LogOperType.GAMEREPRIVACY_MODIFY);
            log.setOpTime(new Date());
            log.setOpIp(getIp());
            log.setOpAfter("modify: uno " + uno + " resourseId:" + resrouceId + " level:" + level);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " cratePage occured ServiceException.e: ", e);
            return new ModelAndView("/gameresource/tab_modifyprivacy", mapMessage);
        }

        return new ModelAndView("redirect:/gameresource/privacy/list?resid=" + resrouceId);
    }

}
