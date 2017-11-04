package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.GameBrand;
import com.enjoyf.platform.service.joymeapp.JoymeAppServiceSngl;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchivesFiled;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-06-25
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
@Controller
@RequestMapping(value = "/gamedb/brand")
public class GameBrandController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView lineList() {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            List<GameBrand> gameBrandList = GameResourceServiceSngl.get().queryGameBrand();
            mapMessage.put("list", gameBrandList);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/gameresource/gamedb/brand/brandlist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createpage() {
        return new ModelAndView("/gameresource/gamedb/brand/createbrand");
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "icon", required = false) String icon
    ) {
        try {

            GameBrand gameBrand = new GameBrand();
            gameBrand.setIcon(icon);
            gameBrand.setId(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            GameResourceServiceSngl.get().createGameBrand(gameBrand);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        return new ModelAndView("redirect:/gamedb/brand/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifypage(@RequestParam(value = "brandid", required = false) Integer brandId,
                                   @RequestParam(value = "icon", required = false) String icon) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("brandId", brandId);
        mapMessage.put("brandIcon", icon);
        return new ModelAndView("/gameresource/gamedb/brand/modifybrand", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "brandid", required = false) Integer brandId,
                               @RequestParam(value = "icon", required = false) String icon) {
        try {
            GameBrand gameBrand = new GameBrand();
            gameBrand.setId(brandId);
            gameBrand.setIcon(icon);
            GameResourceServiceSngl.get().modifyGameBrand(brandId, gameBrand);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + "ServiceException", e);
        }
        return new ModelAndView("redirect:/gamedb/brand/list");
    }

}
