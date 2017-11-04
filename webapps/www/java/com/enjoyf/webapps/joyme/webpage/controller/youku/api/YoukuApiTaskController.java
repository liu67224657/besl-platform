package com.enjoyf.webapps.joyme.webpage.controller.youku.api;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.system.TaskAwardEvent;
import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.service.event.task.TaskAction;
import com.enjoyf.platform.service.event.task.TaskUtil;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.controller.youku.AbstractYoukuController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by tonydiao on 2015/6/2.
 */

@Controller
@RequestMapping("/youku/api/task")
public class YoukuApiTaskController extends AbstractYoukuController {

    //签到接口，一周7天为周期，每次给的分值不同
    @ResponseBody
    @RequestMapping(value = "/sign")
    public String sign(HttpServletRequest request) {

        String platFormString = HTTPUtil.getParam(request, "platform");

//        String uidParam = HTTPUtil.getParam(request, "uid");
        String appKey = APPKEY;

        String clientId = HTTPUtil.getParam(request, "clientid"); //用于任务系统

        String uidParam = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
        String profileId = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_PROFILEID);

        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(profileId) || StringUtil.isEmpty(appKey) || StringUtil.isEmpty(platFormString)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

//        long uid = -1;
//        try {
//            uid = Long.parseLong(uidParam);
//        } catch (NumberFormatException e) {
//        }
//        if (uid == -1) {
//            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
//        }

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platFormString));
        } catch (Exception e) {
        }

        if (appPlatform == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {

//            appKey = AppUtil.getAppKey(appKey);
            String groupId = TaskUtil.getTaskGroupId(PREFIX_YOUKU_SIGN_, appPlatform);

            List<Task> tasks = EventServiceSngl.get().getTaskGroupList(groupId);
            if (CollectionUtil.isEmpty(tasks)) {
                return ResultCodeConstants.TASK_NOT_EXISTS.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            Task FirstTask = tasks.get(0);
            int taskVerifyId = FirstTask.getTaskVerifyId();
            String userId = getIdBytaskVerifyId(taskVerifyId, profile.getProfileId(), clientId);

            if (StringUtil.isEmpty(userId)) {
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            Date doTaskDate = new Date();

            //按第一个签到任务是按设备还是按用户来决定整个组是按设备还是按用户
            if (EventServiceSngl.get().getTaskLogByGroupIdProfileId(FirstTask.getTaskGroupId(), userId, doTaskDate) != null) {
                return ResultCodeConstants.TASK_HAS_COMPLETE.getJsonString();
            }


            TaskAwardEvent taskEvent = new TaskAwardEvent();
            taskEvent.setTaskGroupId(FirstTask.getTaskGroupId());
            taskEvent.setTaskAction(TaskAction.SIGN);
            taskEvent.setDoTaskDate(doTaskDate);
            taskEvent.setProfileId(profile.getProfileId());
            taskEvent.setTaskIp(getIp(request));
            taskEvent.setUno(profile.getUno());
            taskEvent.setAppkey(AppUtil.getAppKey(appKey));
            taskEvent.setProfileKey(profile.getProfileKey());
            taskEvent.setAppPlatform(appPlatform);
            taskEvent.setClientId(clientId);

            EventDispatchServiceSngl.get().dispatch(taskEvent);

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }

        return ResultCodeConstants.SUCCESS.getJsonString();
    }
}
