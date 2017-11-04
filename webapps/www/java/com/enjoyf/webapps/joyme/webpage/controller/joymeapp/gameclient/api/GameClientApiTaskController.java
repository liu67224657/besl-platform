package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.api;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.system.TaskAwardEvent;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.lottery.LotteryServiceSngl;
import com.enjoyf.platform.service.point.UserPoint;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.task.TaskInfoDTO;
import com.enjoyf.webapps.joyme.weblogic.event.TaskWebLogic;
import com.enjoyf.webapps.joyme.weblogic.giftmarket.GiftMarketWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/api/task")
public class GameClientApiTaskController extends AbstractGameClientBaseController {


    @Resource(name = "taskWebLogic")
    private TaskWebLogic taskWebLogic;

    @Resource(name = "giftMarketWebLogic")
    private GiftMarketWebLogic giftMarketWebLogic;


    //签到接口，一周7天为周期，每次给的分值不同
    @ResponseBody
    @RequestMapping(value = "/sign")
    public String sign(HttpServletRequest request) {

        String platFormString = HTTPUtil.getParam(request, "platform");

        String uidParam = HTTPUtil.getParam(request, "uid");
        String appKey = HTTPUtil.getParam(request, "appkey");

        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? request.getHeader("User-Agent") : HTTPUtil.getParam(request, "clientid"); //用于任务系统

        if (StringUtil.isEmpty(uidParam) || StringUtil.isEmpty(appKey) || StringUtil.isEmpty(platFormString)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platFormString));
        } catch (Exception e) {
        }

        if (appPlatform == null) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }
        try {

            appKey = AppUtil.getAppKey(appKey);
            List<Task> tasks = EventServiceSngl.get().getTaskGroupList(getSignGroup(appPlatform));
            if (CollectionUtil.isEmpty(tasks)) {
                return ResultCodeConstants.TASK_NOT_EXISTS.getJsonString();
            }

            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }

            Date doTaskDate = new Date();

            //按第一个签到任务是按设备还是按用户来决定整个组是按设备还是按用户
            Task FirstTask = tasks.get(0);
            int taskVerifyId = FirstTask.getTaskVerifyId();

            //todo singinfo抽象方法
            if (taskVerifyId == 0) {
                if (EventServiceSngl.get().getTaskLogByGroupIdProfileId(FirstTask.getTaskGroupId(), profile.getProfileId(), doTaskDate) != null) {
                    return ResultCodeConstants.TASK_HAS_COMPLETE.getJsonString();
                }
            } else {
                if (EventServiceSngl.get().getTaskLogByGroupIdProfileId(FirstTask.getTaskGroupId(), clientId, doTaskDate) != null) {
                    return ResultCodeConstants.TASK_HAS_COMPLETE.getJsonString();
                }
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


    //获取签到信息的接口，点击侧边栏就要调用这个
    @ResponseBody
    @RequestMapping(value = "/signinfo")
    public String signInfo(HttpServletRequest request) {

        String uidParam = HTTPUtil.getParam(request, "uid");
        String appkey = HTTPUtil.getParam(request, "appkey");
        String platformString = HTTPUtil.getParam(request, "platform");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? request.getHeader("User-Agent") : HTTPUtil.getParam(request, "clientid"); //用于任务系统
        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platformString));
        } catch (Exception e) {
        }

        if (StringUtil.isEmpty(uidParam) || appPlatform == null || StringUtil.isEmpty(appkey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            appkey = AppUtil.getAppKey(appkey);
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
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

            List<Task> tasks = EventServiceSngl.get().getTaskGroupList(getSignGroup(appPlatform));

            if (CollectionUtil.isEmpty(tasks)) {
                return ResultCodeConstants.TASK_NOT_EXISTS.getJsonString();
            }

            //按第一个签到任务是按设备还是按用户来决定整个组是按设备还是按用户
            Task FirstTask = tasks.get(0);
            int taskVerifyId = FirstTask.getTaskVerifyId();
            int signNum = 0;
            if (taskVerifyId == 0) {
                taskLog = EventServiceSngl.get().getTaskLogByGroupIdProfileId(FirstTask.getTaskGroupId(), profile.getProfileId(), doTaskDate);
                signNum = EventServiceSngl.get().querySignSumByProfileIdGroupId(profile.getProfileId(), FirstTask.getTaskGroupId());
            } else {
                taskLog = EventServiceSngl.get().getTaskLogByGroupIdProfileId(FirstTask.getTaskGroupId(), clientId, doTaskDate);
                signNum = EventServiceSngl.get().querySignSumByProfileIdGroupId(clientId, FirstTask.getTaskGroupId());
            }

            result.put("signcomplete", taskLog != null);
            result.put("signnum", signNum);        //总签到次数

            List<Task> taskList = EventServiceSngl.get().queryTaskByGroupIdProfileId(FirstTask.getTaskGroupId(), profile.getProfileId());
            result.put("tasklist", taskWebLogic.getTaskDTO(taskList));

            //获取这一appkey,在这一平台的所有任务
            Map<String, List<Task>> taskMap = EventServiceSngl.get().queryTaskByGroupIds(AppUtil.getAppKey(appkey), appPlatform, TaskGroupType.COMMON);

            //获取所有当前已经完成的任务
            Map<String, TaskLog> cTaskMap = EventServiceSngl.get().checkCompleteTask(profile.getProfileId(), AppUtil.getAppKey(appkey), appPlatform, doTaskDate);

            TaskInfoDTO taskInfoDTO = taskWebLogic.getTaskInfo(taskMap, cTaskMap, profile);

            result.put("taskcount", taskInfoDTO.getTaskCount());
            result.put("completecount", taskInfoDTO.getCompleteCount());
            result.put("awardcount", taskInfoDTO.getAwardCount());

            jsonObject.put("result", result);
            jsonObject.put("msg", ResultCodeConstants.SUCCESS.getMsg());

            return jsonObject.toString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }


    //任务提交接口
    @ResponseBody
    @RequestMapping(value = "/complete")
    public String complete(HttpServletRequest request) {

        String uidParam = HTTPUtil.getParam(request, "uid");
        String appkey = HTTPUtil.getParam(request, "appkey");
        String platformString = HTTPUtil.getParam(request, "platform");

        String taskId = HTTPUtil.getParam(request, "taskid");

        String taskActionCode = HTTPUtil.getParam(request, "taskid");  //兼容客户端的命名

        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? request.getHeader("User-Agent") : HTTPUtil.getParam(request, "clientid"); //用于任务系统
        String directid = HTTPUtil.getParam(request, "directid");

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platformString));
        } catch (Exception e) {
        }

        if (StringUtil.isEmpty(uidParam) || appPlatform == null || StringUtil.isEmpty(appkey)) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (NumberFormatException e) {
        }
        if (uid == -1) {
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }

        try {
            Profile profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            if (profile == null) {
                return ResultCodeConstants.USERCENTER_PROFILE_NOT_EXISTS.getJsonString();
            }
            //5月特殊活动，taskid== wanba_five_month_task
            if (taskId != null && "wanba_five_month_task".contains(taskId)) {
                String date_short = DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT);

                LotteryServiceSngl.get().setUserLotteryShareTime(profile.getProfileId(), date_short);
                return ResultCodeConstants.SUCCESS.getJsonString();
            }


            Date doTaskDate = new Date();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rs", String.valueOf(ResultCodeConstants.SUCCESS.getCode()));
            TaskAwardEvent taskEvent = new TaskAwardEvent();

            taskEvent.setDoTaskDate(doTaskDate);
            taskEvent.setProfileId(profile.getProfileId());

            TaskAction taskAction = checkTaskActionCode(taskActionCode);
            taskEvent.setTaskAction(taskAction);
            taskEvent.setTaskIp(getIp(request));
            taskEvent.setUno(profile.getUno());
            taskEvent.setAppkey(AppUtil.getAppKey(appkey));
            taskEvent.setProfileKey(profile.getProfileKey());
            taskEvent.setAppPlatform(appPlatform);
            taskEvent.setDirectId(directid);
            EventDispatchServiceSngl.get().dispatch(taskEvent);

            return ResultCodeConstants.SUCCESS.getJsonString();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }
    }

    private String checkTaskId(HttpServletRequest request, String taskId, String platformString) {
        if (!HTTPUtil.isNewVersion(request)) {
            return taskId;
        }
        //dailytasks.0.Sharearticle	分享1篇文章给小伙伴
        // dailytasks.0.sharehotgame	分享1个热门游戏
        // dailytasks.0.shareranking	分享1个排行榜
        if (taskId.equals("Sharearticle") || taskId.equals("sharehotgame") || taskId.equals("shareranking")) {
            return "dailytasks." + platformString + "." + taskId;
        }
        return taskId;
    }

    private TaskAction checkTaskActionCode(String taskActionCode) {
        if (StringUtil.isEmpty(taskActionCode)) {
            return null;
        }
        if (taskActionCode.contains("Sharearticle")) {
            return TaskAction.SHARE_ARTICLE;
        } else if (taskActionCode.contains("sharehotgame")) {
            return TaskAction.SHARE_HOTGAME;
        } else if (taskActionCode.contains("shareranking")) {
            return TaskAction.SHARE_RANKING;
        }

        return null;
    }


}