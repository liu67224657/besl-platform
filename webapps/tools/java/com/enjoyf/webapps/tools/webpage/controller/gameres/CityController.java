package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.City;
import com.enjoyf.platform.service.gameres.CityField;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.gameres.NewReleaseWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping(value = "/gameresource/city")
public class CityController extends ToolsBaseController {

    private Logger logger = LoggerFactory.getLogger(NewReleaseController.class);

    @Resource(name = "newGameInfoWebLogic")
    private NewReleaseWebLogic newReleaseWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "ispreset", required = false) boolean isPreset) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(CityField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QuerySort.add(CityField.CREATE_DATE, QuerySortOrder.DESC));
        if (isPreset) {
            queryExpress.add(QueryCriterions.eq(CityField.IS_PRESET, isPreset));
        }
        try {
            List<City> cityList = GameResourceServiceSngl.get().queryCity(queryExpress);
            if (CollectionUtil.isEmpty(cityList)) {
                return new ModelAndView("/gameresource/citylist", mapMessage);
            }
            mapMessage.put("list", cityList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/citylist");
        }
        return new ModelAndView("/gameresource/citylist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {

        return new ModelAndView("/gameresource/createcitypage");
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "cityname", required = false) String cityName,
                               @RequestParam(value = "ispreset", required = false) boolean isPreset) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        City city = new City();
        city.setCityName(cityName);
        city.setIsPreset(isPreset);
        city.setCreateDate(new Date());
        city.setCreateIp(getIp());
        city.setCreateUserId(getCurrentUser().getUserid());
        city.setValidStatus(ValidStatus.VALID);
        try {
            GameResourceServiceSngl.get().createCity(city);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/createcitypage");
        }
        return new ModelAndView("redirect:/gameresource/city/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifypage(@RequestParam(value = "cityid", required = true) Long cityId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (cityId == null) {
            return new ModelAndView("redirect:/gameresource/city/list");
        }
        try {
            List<City> cityList = GameResourceServiceSngl.get().queryCity(new QueryExpress().add(QueryCriterions.eq(CityField.CITY_ID, cityId)));
            if (CollectionUtil.isEmpty(cityList) || cityList.get(0) == null) {
                return new ModelAndView("redirect:/gameresource/city/list");
            }
            mapMessage.put("city", cityList.get(0));
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/gameresource/citylist");
        }
        mapMessage.put("cityId", cityId);
        return new ModelAndView("/gameresource/modifycitypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "cityid", required = false) Long cityId,
                               @RequestParam(value = "cityname", required = false) String cityName,
                               @RequestParam(value = "ispreset", required = false) boolean isPreset) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (cityId == null) {
            return new ModelAndView("redirect:/gameresource/city/list");
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(CityField.CITY_NAME, cityName);
        updateExpress.set(CityField.IS_PRESET, isPreset);
        updateExpress.set(CityField.LAST_MODIFY_DATE, new Date());
        updateExpress.set(CityField.LAST_MODIFY_IP, getIp());
        updateExpress.set(CityField.LAST_MODIFY_USERID, getCurrentUser().getUserid());
        try {
            GameResourceServiceSngl.get().modifyCity(cityId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/gameresource/city/list");
        }
        return new ModelAndView("redirect:/gameresource/city/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "cityid", required = true) Long cityId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (cityId == null) {
            return new ModelAndView("redirect:/gameresource/city/list");
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(CityField.VALIDSTATUS, ValidStatus.REMOVED.getCode());

        try {
            GameResourceServiceSngl.get().modifyCity(cityId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/gameresource/city/list");
        }
        return new ModelAndView("redirect:/gameresource/city/list");
    }

}
