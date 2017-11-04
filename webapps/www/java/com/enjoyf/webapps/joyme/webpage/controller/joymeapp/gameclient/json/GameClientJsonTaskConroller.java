package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.json;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.TaskAward;
import com.enjoyf.platform.service.event.task.TaskLog;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/2/8
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/json/task")
public class GameClientJsonTaskConroller extends AbstractGameClientBaseController {


    @RequestMapping(value = "/getaward")
    @ResponseBody
    public String taskList(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String appkey = HTTPUtil.getParam(request, "appkey");
        String platform = HTTPUtil.getParam(request, "platform");
        String profileId = HTTPUtil.getParam(request, "profileid");
        String taskId = HTTPUtil.getParam(request, "taskid");

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platform));
        } catch (Exception e) {
        }

        if (StringUtil.isEmpty(profileId) || appPlatform == null || StringUtil.isEmpty(appkey) || StringUtil.isEmpty(taskId)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        Date doTaskDate = new Date();
        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            TaskAward award = EventServiceSngl.get().getAward(profile.getProfileId(), taskId, doTaskDate, AppUtil.getAppKey(appkey), profile.getUno(), profile.getProfileKey());

            if (award == null) {
                return ResultCodeConstants.TASK_GETAWARD_FALIED.getJsonString();
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }

    @ResponseBody
    @RequestMapping(value = "/shareaward")
    public String shareAward(HttpServletRequest request) {
        String clientId = HTTPUtil.getParam(request, "clientid");
        String platform = HTTPUtil.getParam(request, "platform");
        String taskId = "dailytasks." + platform + ".qbdownload";
        String profileId = Md5Utils.md5(clientId);
        String taskLogId = Md5Utils.md5(profileId + taskId).toLowerCase();


        try {
            TaskLog taskLog = EventServiceSngl.get().getTaskLog(taskLogId);
            if (taskLog == null) {
                return new ResultCodeConstants(-1, "download.is.null").getJsonString();
            }
            taskId = "dailytasks." + platform + ".qbshare";
            taskLogId = Md5Utils.md5(profileId + taskId).toLowerCase();
            taskLog = EventServiceSngl.get().getTaskLog(taskLogId);
            if (taskLog == null) {
                return ResultCodeConstants.TASK_GETAWARD_FALIED.getJsonString();
            } else if (!taskLog.getOverStatus().equals(ActStatus.ACTED)) {
                return ResultCodeConstants.TASK_GETAWARD_FALIED.getJsonString();
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
        return ResultCodeConstants.SUCCESS.getJsonString();
    }
}
