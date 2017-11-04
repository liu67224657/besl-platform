package com.enjoyf.webapps.joyme.webpage.controller.youku;

import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.service.event.task.TaskGroupType;
import com.enjoyf.platform.service.event.task.TaskLog;
import com.enjoyf.platform.service.event.task.TaskUtil;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.task.TaskInfoDTO;
import com.enjoyf.webapps.joyme.weblogic.event.TaskWebLogic;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterSession;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tonydiao on 2014/12/23.
 */

@Controller
@RequestMapping("/youku/webview/task")
public class YoukuWebviewTaskViewController extends AbstractYoukuController {

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;


    @Resource(name = "taskWebLogic")
    private TaskWebLogic taskWebLogic;

    private static final String PREFIX_YOUKU_SIGN_ = "youku.sign";

    //优酷签到
    @RequestMapping(value = "/signpage")
    public ModelAndView signPage(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        String appkey = APPKEY;
        String platformString = HTTPUtil.getParam(request, "platform");
        String clientId = HTTPUtil.getParam(request, "clientid"); //用于任务系统

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platformString));
        } catch (Exception e) {
        }

        String uid = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
        String profileId = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_PROFILEID);
        if (StringUtil.isEmpty(uid) || appPlatform == null || StringUtil.isEmpty(appkey)) {
            return new ModelAndView("/views/jsp/youku/youku-sign", mapMessage);
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(profileId);
            if (profile == null) {
                return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("user.profile.notexists"));
            }
            mapMessage.put("profile", profile);

            //验证任务
            String groupId = TaskUtil.getTaskGroupId(PREFIX_YOUKU_SIGN_, appPlatform);
            List<Task> tasks = EventServiceSngl.get().getTaskGroupList(groupId);
            if (CollectionUtil.isEmpty(tasks)) {
                return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("task.nick.notexists"));
            }

            //按第一个签到任务是按设备还是按用户来决定整个组是按设备还是按用户
            Task FirstTask = tasks.get(0);
            int taskVerifyId = FirstTask.getTaskVerifyId();
            String userId = getIdBytaskVerifyId(taskVerifyId, profile.getProfileId(), clientId);
            if (StringUtil.isEmpty(userId)) {
                return new ModelAndView("/views/jsp/common/custompage-wap", putErrorMessage("param.empty"));
            }

            //今天是否完成签到
            Date doTaskDate = new Date();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));

            //build resultMap
            Map result = new HashMap();
            UserPoint userPoint = giftMarketWebLogic.getUserPoint(appkey, profile);
            result.put("point", String.valueOf(userPoint == null ? 0 : userPoint.getUserPoint()));
            TaskLog taskLog = null;

            taskLog = EventServiceSngl.get().getTaskLogByGroupIdProfileId(FirstTask.getTaskGroupId(), userId, doTaskDate);
            int signNum = EventServiceSngl.get().querySignSumByProfileIdGroupId(userId, FirstTask.getTaskGroupId());


            mapMessage.put("signcomplete", taskLog != null);
            mapMessage.put("signnum", signNum);        //总签到次数

            List<Task> taskList = EventServiceSngl.get().queryTaskByGroupIdProfileId(FirstTask.getTaskGroupId(), profile.getProfileId());
            mapMessage.put("tasklist", taskWebLogic.getTaskDTO(taskList));

            //获取这一appkey,在这一平台的所有任务
            Map<String, List<Task>> taskMap = EventServiceSngl.get().queryTaskByGroupIds(AppUtil.getAppKey(appkey), appPlatform, TaskGroupType.COMMON);

            //获取所有当前已经完成的任务
            Map<String, TaskLog> cTaskMap = EventServiceSngl.get().checkCompleteTask(profile.getProfileId(), AppUtil.getAppKey(appkey), appPlatform, doTaskDate);

            TaskInfoDTO taskInfoDTO = taskWebLogic.getTaskInfo(taskMap, cTaskMap, profile);

            mapMessage.put("taskcount", taskInfoDTO.getTaskCount());
            mapMessage.put("completecount", taskInfoDTO.getCompleteCount());
            mapMessage.put("awardcount", taskInfoDTO.getAwardCount());
            mapMessage.put("appkey", APPKEY);
            if (!StringUtil.isEmpty(clientId)) {
                mapMessage.put("clientid", clientId);
            }


        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e: ", e);
            mapMessage.put("errorMessage", "system.error");
            mapMessage.put("errorStatus", 1);
        }

        return new ModelAndView("/views/jsp/youku/youku-sign", mapMessage);

    }


}
