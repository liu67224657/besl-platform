package com.enjoyf.webapps.tools.webpage.controller.gameres;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.NewReleaseTag;
import com.enjoyf.platform.service.gameres.NewReleaseTagField;
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
@Deprecated
@Controller
@RequestMapping(value = "/gameresource/newreleasetag")
public class NewReleaseTagController extends ToolsBaseController {

    private Logger logger = LoggerFactory.getLogger(NewReleaseController.class);

    @Resource(name = "newGameInfoWebLogic")
    private NewReleaseWebLogic newReleaseWebLogic;

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "ishot", required = false) boolean isHot,
                             @RequestParam(value = "istop", required = false) boolean isTop) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(NewReleaseTagField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QuerySort.add(NewReleaseTagField.CREATE_DATE, QuerySortOrder.DESC));
        if (isHot) {
            queryExpress.add(QueryCriterions.eq(NewReleaseTagField.IS_HOT, isHot));
        }
        if (isTop) {
            queryExpress.add(QueryCriterions.eq(NewReleaseTagField.IS_TOP, isTop));
        }
        try {
            List<NewReleaseTag> newGameTagList = GameResourceServiceSngl.get().queryNewGameTag(queryExpress);
            if (CollectionUtil.isEmpty(newGameTagList)) {
                return new ModelAndView("/gameresource/newreleasetaglist");
            }
            mapMessage.put("list", newGameTagList);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/newreleasetaglist");
        }
        return new ModelAndView("/gameresource/newreleasetaglist", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage() {

        return new ModelAndView("/gameresource/createnewreleasetagpage");
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "tagname", required = false) String tagName,
                               @RequestParam(value = "ishot", required = false) boolean isHot,
                               @RequestParam(value = "istop", required = false) boolean isTop) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        NewReleaseTag tag = new NewReleaseTag();
        tag.setTagName(tagName);
        tag.setIsHot(isHot);
        tag.setIsTop(isTop);
        tag.setCreateDate(new Date());
        tag.setCreateIp(getIp());
        tag.setCreateUserId(getCurrentUser().getUserid());
        tag.setValidStatus(ValidStatus.VALID);
        try {
            GameResourceServiceSngl.get().createNewGameTag(tag);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/createnewreleasetagpage");
        }
        return new ModelAndView("redirect:/gameresource/newreleasetag/list");
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifypage(@RequestParam(value = "tagid", required = true) Long tagId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (tagId == null) {
            return new ModelAndView("redirect:/gameresource/newreleasetag/list");
        }
        try {
            NewReleaseTag tag = GameResourceServiceSngl.get().getNewGameTag(tagId);
            if (tag == null) {
                return new ModelAndView("redirect:/gameresource/newreleasetag/list");
            }
            mapMessage.put("tag", tag);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/gameresource/newreleasetaglist");
        }
        mapMessage.put("tagId", tagId);
        return new ModelAndView("/gameresource/modifynewreleasetagpage", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "tagid", required = false) Long tagId,
                               @RequestParam(value = "tagname", required = false) String tagName,
                               @RequestParam(value = "ishot", required = false) boolean isHot,
                               @RequestParam(value = "istop", required = false) boolean isTop) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (tagId == null) {
            return new ModelAndView("redirect:/gameresource/newreleasetag/list");
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(NewReleaseTagField.TAG_NAME, tagName);
        updateExpress.set(NewReleaseTagField.IS_HOT, isHot);
        updateExpress.set(NewReleaseTagField.IS_TOP, isTop);
        updateExpress.set(NewReleaseTagField.LAST_MODIFY_DATE, new Date());
        updateExpress.set(NewReleaseTagField.LAST_MODIFY_IP, getIp());
        updateExpress.set(NewReleaseTagField.LAST_MODIFY_USERID, getCurrentUser().getUserid());

        try {
            GameResourceServiceSngl.get().modifyNewGameTag(tagId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/gameresource/newreleasetag/list");
        }
        return new ModelAndView("redirect:/gameresource/newreleasetag/list");
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam(value = "tagid", required = true) Long tagId) {

        Map<String, Object> mapMessage = new HashMap<String, Object>();
        if (tagId == null) {
            return new ModelAndView("redirect:/gameresource/newreleasetag/list");
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(NewReleaseTagField.VALIDSTATUS, ValidStatus.REMOVED.getCode());
        try {
            GameResourceServiceSngl.get().modifyNewGameTag(tagId, updateExpress);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " ServiceException.e:" + e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/gameresource/newreleasetag/list");
        }
        return new ModelAndView("redirect:/gameresource/newreleasetag/list");
    }
}
