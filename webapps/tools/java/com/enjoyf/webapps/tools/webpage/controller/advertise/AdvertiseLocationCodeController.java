package com.enjoyf.webapps.tools.webpage.controller.advertise;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.advertise.AdvertisePublishField;
import com.enjoyf.platform.service.advertise.AdvertisePublishLocation;
import com.enjoyf.platform.service.advertise.AdvertisePublishLocationField;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
@Controller
@RequestMapping(value = "/advertise/location")
public class AdvertiseLocationCodeController extends AdvertiseBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "publishid") String publishId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(AdvertisePublishLocationField.PUBLISHID, publishId))
                .add(QueryCriterions.eq(AdvertisePublishField.VALIDSTATUS, ValidStatus.VALID.getCode()))
                .add(QuerySort.add(AdvertisePublishField.CREATEDATE, QuerySortOrder.DESC));

        List<AdvertisePublishLocation> locationList = new ArrayList<AdvertisePublishLocation>();
        try {

            locationList = AdvertiseServiceSngl.get().queryPublishLocations(queryExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/tab_locationlist", mapMessage);
        }

        mapMessage.put("locationList", locationList);
        mapMessage.put("publishid", publishId);

        return new ModelAndView("/advertise/tab_locationlist", mapMessage);
    }

    @RequestMapping(value = "/addpage")
    public ModelAndView addPage(@RequestParam(value = "publishid") String publishId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("publishid", publishId);

        return new ModelAndView("/advertise/tab_locationaddpage", mapMessage);
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(@RequestParam(value = "publishid") String publishId,
                            @RequestParam(value = "locationcode") String locationCode,
                            @RequestParam(value = "locationname") String locationName,
                            @RequestParam(value = "locationdesc", required = false) String locationDesc) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertisePublishLocation location = new AdvertisePublishLocation();
        location.setLocationCode(locationCode);
        location.setLocationDesc(locationDesc);
        location.setLocationName(locationName);
        location.setCreateDate(new Date());
        location.setCreateIp(getIp());
        location.setCreateUserid(getCurrentUser().getUsername());
        location.setPublishId(publishId);

        try {
            AdvertiseServiceSngl.get().createPublishLocation(location);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/tab_locationaddpage", mapMessage);
        }

        return new ModelAndView("redirect:/advertise/location/list?publishid=" + publishId);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "locationcode") String locationCode,
                                   @RequestParam(value = "publishid") String publishId) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertisePublishLocation location = null;
        try {
            location = AdvertiseServiceSngl.get().getPublishLocation(publishId, locationCode);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifypage occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/tab_locationlist", mapMessage);
        }

        if (location == null) {
            mapMessage.put("errorMsgMap", putErrorMap("error", "error.advertise.location.notexists"));
            return new ModelAndView("/advertise/tab_locationlist", mapMessage);
        }

        mapMessage.put("location", location);
        return new ModelAndView("/advertise/tab_locationmodifypage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "publishid") String publishId,
                               @RequestParam(value = "locationcode") String locationCode,
                               @RequestParam(value = "locationname") String locationName,
                               @RequestParam(value = "locationdesc", required = false) String locationDesc,
                               @RequestParam(value = "validstatus") String validStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        AdvertisePublishLocation location = null;
        try {
            UpdateExpress updateExpress = new UpdateExpress()
                    .set(AdvertisePublishLocationField.LOCATIONCODE, locationCode)
                    .set(AdvertisePublishLocationField.LOCATIONNAME, locationName)
                    .set(AdvertisePublishLocationField.LOCATIONDESC, locationDesc)
                    .set(AdvertisePublishLocationField.VALIDSTATUS, validStatus);

            AdvertiseServiceSngl.get().modifyPublishLocation(updateExpress, publishId, locationCode);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " modifypage occured Service Exception.e:", e);

            mapMessage.put("errorMsgMap", putErrorMap("error", "error.exception"));
            return new ModelAndView("/advertise/tab_locationlist", mapMessage);
        }

        return new ModelAndView("redirect:/advertise/location/list?publishid=" + publishId);
    }

}
