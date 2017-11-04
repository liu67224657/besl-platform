package com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.webview;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.TaskAwardEvent;
import com.enjoyf.platform.service.event.task.TaskAction;
import com.enjoyf.platform.service.gameclient.dto.GameClientPicDTO;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.gameclient.GameClientWebLogic;
import com.enjoyf.webapps.joyme.webpage.controller.joymeapp.gameclient.AbstractGameClientBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/19
 * Description:
 */
@Controller
@RequestMapping("/joymeapp/gameclient/webview/pic")
public class GameClientPicProfileWebViewController extends AbstractGameClientBaseController {

    @Resource(name = "gameClientWebLogic")
    private GameClientWebLogic gameClientWebLogic;

    @RequestMapping("/likepage")
    public ModelAndView likePicPage(HttpServletRequest request) {
        Map map = new HashMap();
        String uidParam = HTTPUtil.getParam(request, "uid");
        map.put("uid", uidParam);
        String logindomain = HTTPUtil.getParam(request, "logindomain");
        map.put("logindomain", logindomain);
        String appKey = HTTPUtil.getParam(request, "appkey");
        map.put("appkey", appKey);
        String platFormString = HTTPUtil.getParam(request, "platform");
        String clientId = StringUtil.isEmpty(HTTPUtil.getParam(request, "clientid")) ? request.getHeader("User-Agent") : HTTPUtil.getParam(request, "clientid"); //用于任务系统

        AppPlatform appPlatform = null;
        try {
            appPlatform = AppPlatform.getByCode(Integer.parseInt(platFormString));
        } catch (Exception e) {
        }
        map.put("platform", appPlatform == null ? "" : appPlatform.getCode());
        long uid = -1;
        try {
            uid = Long.parseLong(uidParam);
        } catch (Exception e) {
        }

        try {
            Profile profile = null;
            if (uid > 0) {
                profile = UserCenterServiceSngl.get().getProfileByUid(uid);
            }

            List<GameClientPicDTO> list = gameClientWebLogic.queryGameClientPicDTO(profile != null ? profile.getProfileId() : "");
            if (profile != null) {
                sendOutMiyouTask(profile, getIp(request), appPlatform, appKey, new Date(),clientId);
            }

            map.put("list", list);
            return new ModelAndView("/views/jsp/gameclient/webview/piclike-page", map);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e: ", e);
        }
        return null;
    }

    private void sendOutMiyouTask(Profile profile, String ip, AppPlatform appPlatform, String appKey, Date date,String clientId) {
        try {
            TaskAwardEvent taskEvent = new TaskAwardEvent();
            taskEvent.setDoTaskDate(date);
            taskEvent.setProfileId(profile.getProfileId());
            taskEvent.setTaskIp(ip);
            taskEvent.setTaskAction(TaskAction.VIEW_MIYOU);
//            taskEvent.setTaskId(getMiyouPageTaskId(appPlatform));
            taskEvent.setUno(profile.getUno());
            taskEvent.setAppkey(AppUtil.getAppKey(appKey));
            taskEvent.setProfileKey(profile.getProfileKey());
            taskEvent.setAppPlatform(appPlatform);
            taskEvent.setClientId(clientId);
            EventDispatchServiceSngl.get().dispatch(taskEvent);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
        }
    }
}
