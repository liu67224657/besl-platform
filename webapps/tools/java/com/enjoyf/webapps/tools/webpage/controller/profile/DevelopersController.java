package com.enjoyf.webapps.tools.webpage.controller.profile;

import com.enjoyf.platform.service.profile.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.weblogic.dto.developers.ProfileDeveloperDTO;
import com.enjoyf.webapps.tools.weblogic.profile.DevelopersWebLogic;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-12-23
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/developers")
public class DevelopersController extends ToolsBaseController {

    @Resource(name = "developersWebLogic")
    private DevelopersWebLogic developersWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "category", required = false) Integer category,
                             @RequestParam(value = "status", required = false) String verifyStatus) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("category", category);
        mapMessage.put("status", verifyStatus);

        mapMessage.put("categoryCollection", DeveloperCategory.getAll());

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QuerySort.add(ProfileDeveloperField.CREATE_DATE, QuerySortOrder.DESC));
        if (category != null) {
            queryExpress.add(QueryCriterions.eq(ProfileDeveloperField.CATEGORY, category));
        }
        if (!StringUtil.isEmpty(verifyStatus)) {
            queryExpress.add(QueryCriterions.eq(ProfileDeveloperField.VERIFY_STATUS, verifyStatus));
        }

        try {
            PageRows<ProfileDeveloperDTO> pageRows = developersWebLogic.queryProfileDeveloperDTOs(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/developers/list", mapMessage);
    }

    @RequestMapping(value = "/detail")
    public ModelAndView detail(@RequestParam(value = "uno", required = false) String uno) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (StringUtil.isEmpty(uno)) {
            return new ModelAndView("redirect:/developers/list");
        }
        mapMessage.put("uno", uno);
        mapMessage.put("statusCollection", VerifyStatus.getAll());
        try {
            ProfileDeveloperDTO dto = developersWebLogic.getProfileDeveloperDTO(uno);
            mapMessage.put("dto", dto);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/developers/detail", mapMessage);
    }

    @RequestMapping(value = "/verify")
    public ModelAndView verify(@RequestParam(value = "uno", required = false) String uno,
                               @RequestParam(value = "status", required = false) String verifyStatus,
                               @RequestParam(value = "verifydesc", required = false) String verifyDesc,
                               @RequestParam(value = "verifytype", required = false) String verifyType,
                               @RequestParam(value = "reason", required = false) String reason) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        if (StringUtil.isEmpty(uno)) {
            return new ModelAndView("redirect:/developers/list");
        }
        if (StringUtil.isEmpty(verifyStatus)) {
            return new ModelAndView("redirect:/developers/list");
        }
        DeveloperCategory category = null;
        try {
            ProfileDeveloper developer = ProfileServiceSngl.get().getProfileDeveloper(new QueryExpress().add(QueryCriterions.eq(ProfileDeveloperField.UNO, uno)));
            if (developer == null) {
                return new ModelAndView("redirect:/developers/list");
            }
            category = developer.getCategory();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ProfileDeveloperField.VERIFY_STATUS, verifyStatus);
        updateExpress.set(ProfileDeveloperField.VERIFY_DATE, new Date());
        updateExpress.set(ProfileDeveloperField.VERIFY_IP, getIp());
        updateExpress.set(ProfileDeveloperField.VERIFY_REASON, reason);
        try {
            ProfileServiceSngl.get().modifyProfileDeveloper(updateExpress, new QueryExpress().add(QueryCriterions.eq(ProfileDeveloperField.UNO, uno)));
            VerifyType type = VerifyType.getByCode(verifyType);
            VerifyStatus status = VerifyStatus.getByCode(verifyStatus);
            if (VerifyStatus.ACCESS.equals(status) && VerifyType.N_VERIFY.equals(type)) {
                Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();
                if(DeveloperCategory.PERSONALDEV.equals(category)){
                    map.put(ProfileDetailField.VERIFYSTATUS, VerifyType.P_VERIFY.getCode());
                }else {
                    map.put(ProfileDetailField.VERIFYSTATUS, VerifyType.C_VERIFY.getCode());
                }
                map.put(ProfileDetailField.VERIFYDESC, verifyDesc);
                ProfileServiceSngl.get().updateProfileDetail(uno, map);
            } else {
                Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();
                map.put(ProfileDetailField.VERIFYSTATUS, VerifyType.N_VERIFY.getCode());
                map.put(ProfileDetailField.VERIFYDESC, verifyDesc);
                ProfileServiceSngl.get().updateProfileDetail(uno, map);
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("redirect:/developers/list");
    }

}
