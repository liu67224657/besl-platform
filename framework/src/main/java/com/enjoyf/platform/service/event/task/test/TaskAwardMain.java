package com.enjoyf.platform.service.event.task.test;

import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.system.TaskAwardEvent;
import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.service.event.task.TaskAction;
import com.enjoyf.platform.service.event.task.TaskType;
import com.enjoyf.platform.service.event.task.TaskUtil;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.http.AppUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/7
 * Description:
 */
public class TaskAwardMain {
    public static void main(String[] args) {
//        String taskId = "gameclient.sign";
        try {
//            Profile profile = UserCenterServiceSngl.get().getProfileByUid(300764);

//            Task task = EventServiceSngl.get().getTask("dailytasks.0.sharehotgame");

//            System.out.println(TaskUtil.getTaskLogId("b5e15606800461672b43e350b3296eb0","",new Date(),16));


//            Calendar cal = Calendar.getInstance();
//            cal.setTime(new Date());
//            cal.add(Calendar.DAY_OF_YEAR, 15);
            TaskAwardEvent taskEvent = new TaskAwardEvent();
            taskEvent.setTaskAction(TaskAction.VIEW_TEST);
//            taskEvent.setTaskId("dailytasks.0.Sharearticle");
            taskEvent.setTaskType(TaskType.ONCE_BY_DAY);
            taskEvent.setDoTaskDate(new Date());
            taskEvent.setProfileId("262cdbb42499623a20737da60672874d");
            taskEvent.setTaskIp("127.0.0.1");
            taskEvent.setUno("c47d75d2-8177-4db3-ade1-49eeb66907e6");
            taskEvent.setAppPlatform(AppPlatform.IOS);
            taskEvent.setAppkey(AppUtil.getAppKey("17yfn24TFexGybOF0PqjdY"));
            taskEvent.setProfileKey("www");
            //http://api.joyme.test/joymeapp/gameclient/webview/task/list?appkey=17yfn24TFexGybOF0PqjdYI&channelid=appstore&clientid=B8E29464-AB9E-45CD-8EDD-DF058B21F1B5&platform=0&token=5b728e4c-e497-4135-aa49-798c5ef90268&uid=300757&uno=c47d75d2-8177-4db3-ade1-49eeb66907e6&version=1.0
            EventDispatchServiceSngl.get().dispatch(taskEvent);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        while (true) {
        }
    }
}
