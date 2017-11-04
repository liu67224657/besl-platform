package com.enjoyf.webapps.joyme.webpage.controller.wanba.api;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.ask.AskServiceSngl;
import com.enjoyf.platform.service.ask.AskUserAction;
import com.enjoyf.platform.service.ask.AskUserActionType;
import com.enjoyf.platform.service.ask.ItemType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.wordfilter.WanbaResultCodeConstants;
import com.enjoyf.webapps.joyme.dto.Wanba.WanbaFavoriteDTO;
import com.enjoyf.webapps.joyme.weblogic.wanba.WanbaFavoriteWebLogic;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2016/10/9 0009.
 */

@RequestMapping("/wanba/api/favorite")
@Controller
public class WanbaFavoriteController {

    @Resource(name = "wanbaFavoriteWebLogic")
    private WanbaFavoriteWebLogic wanbaFavoriteWebLogic;


    @RequestMapping("/list")
    @ResponseBody
    public String list(HttpServletRequest request,
                       @RequestParam(value = "pnum", required = false, defaultValue = "1") String pnum,
                       @RequestParam(value = "pcount", required = false, defaultValue = "10") String pcount,
                       @RequestParam(value = "pid", required = false) String pid) {

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        if (StringUtil.isEmpty(pid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        int pageNo = 1;
        int pageSize = 10;
        try {
            pageNo = Integer.parseInt(pnum);
            pageSize = Integer.parseInt(pcount);
        } catch (NumberFormatException e) {
        }

        Pagination page = new Pagination(pageNo * pageSize, pageNo, pageSize);
        try {
            PageRows<WanbaFavoriteDTO> pageRows = wanbaFavoriteWebLogic.queryFavoriteAskUserActionList(pid, page);

            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("rows", pageRows.getRows());
            resultMap.put("page", pageRows.getPage());

            jsonObject.put("result", JsonBinder.buildNonNullBinder(resultMap));
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return jsonObject.toString();
    }

    @RequestMapping("/add")
    @ResponseBody
    public String add(HttpServletRequest request,
                      @RequestParam(value = "questionid", required = false) String questionid,
                      @RequestParam(value = "answerid", required = false) String answerid,
                      @RequestParam(value = "pid", required = false) String pid) {

        JSONObject jsonObject = ResultCodeConstants.SUCCESS.getJsonObject();
        if (StringUtil.isEmpty(pid) || StringUtil.isEmpty(answerid) || StringUtil.isEmpty(questionid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        AskUserAction askUserAction = new AskUserAction();
        askUserAction.setActionType(AskUserActionType.FAVORITE);
        askUserAction.setCreateTime(new Date());
        askUserAction.setDestId(Long.valueOf(answerid));
        askUserAction.setItemType(ItemType.ANSWER);
        askUserAction.setProfileId(pid);
        askUserAction.setValue(questionid);
        try {
            boolean bval = AskServiceSngl.get().addAskUserAction(askUserAction);
            if (!bval) {
                jsonObject = WanbaResultCodeConstants.FAVORITE_EXISTS.getJsonObject();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }


    @RequestMapping("/remove")
    @ResponseBody
    public String remove(HttpServletRequest request,
                         @RequestParam(value = "questionid", required = false) String questionid,
                         @RequestParam(value = "answerid", required = false) String answerid,
                         @RequestParam(value = "pid", required = false) String pid) {

        JSONObject jsonObject = WanbaResultCodeConstants.SUCCESS.getJsonObject();
        if (StringUtil.isEmpty(pid) || StringUtil.isEmpty(answerid) || StringUtil.isEmpty(questionid)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        AskUserAction askUserAction = new AskUserAction();
        askUserAction.setActionType(AskUserActionType.FAVORITE);
        askUserAction.setCreateTime(new Date());
        askUserAction.setDestId(Long.valueOf(answerid));
        askUserAction.setItemType(ItemType.ANSWER);
        askUserAction.setProfileId(pid);
        askUserAction.setValue(questionid);
        try {
            boolean bval = AskServiceSngl.get().removeAskUserAction(askUserAction);
            if (!bval) {
                jsonObject = WanbaResultCodeConstants.REMOVE_FAVORITE_ERROR.getJsonObject();
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            return WanbaResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return jsonObject.toString();
    }

}
