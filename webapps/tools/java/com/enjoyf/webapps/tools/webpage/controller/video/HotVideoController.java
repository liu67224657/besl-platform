package com.enjoyf.webapps.tools.webpage.controller.video;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.comment.*;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.AuthAppField;
import com.enjoyf.platform.service.oauth.AuthAppType;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by xupeng on 16/3/18.
 */
@Controller
@RequestMapping(value = "/hot/video")
public class HotVideoController extends ToolsBaseController {

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "gamesdk", required = false) String gameSdk) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        int curPage = (pageStartIndex / pageSize) + 1;

        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);


        try {
            List<AuthApp> appAuthList = OAuthServiceSngl.get().queryAuthApp(new QueryExpress()
                    .add(QueryCriterions.eq(AuthAppField.APPTYPE, AuthAppType.VIDEO_SDK.getCode())));
            mapMessage.put("gamelist", appAuthList);
            if (StringUtil.isEmpty(gameSdk) && !CollectionUtil.isEmpty(appAuthList)) {
                gameSdk = appAuthList.get(0).getAppId();
            }
            if (StringUtil.isEmpty(gameSdk)) {
                return new ModelAndView("/video/hotvideolist", mapMessage);
            }

            mapMessage.put("gamesdk", gameSdk);

            List<CommentVideo> list = CommentServiceSngl.get().queryHotVideoListByRedis(gameSdk, 1);
            mapMessage.put("list", list);

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }

        return new ModelAndView("/video/hotvideolist", mapMessage);
    }

    @RequestMapping(value = "/updatehotlist")
    @ResponseBody
    public String updateHotList(@RequestParam(value = "gamesdk", required = false) String gameSDK) {

        try {
            return CommentServiceSngl.get().updateHotList(gameSDK);
        } catch (ServiceException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(value = "/sort")
    public ModelAndView sort(@RequestParam(value = "oldindex", required = false) String oldIndex,
                             @RequestParam(value = "newindex", required = false) String newIndex,
                             @RequestParam(value = "gamesdk", required = false) String gameSdk) {

        if (StringUtil.isEmpty(oldIndex) || StringUtil.isEmpty(newIndex) || StringUtil.isEmpty(gameSdk)) {
            return new ModelAndView("/video/hotvideolist");
        }
        try {
            CommentServiceSngl.get().sortHotVideoListByRedis(gameSdk, oldIndex, newIndex);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/hot/video/list?gamesdk=" + gameSdk);
    }

}
