package com.enjoyf.platform.service.event.task.test;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.oauth.weibo4j.http.HttpClient;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/7
 * Description:
 */
public class TaskMain {

    //http://passport.joyme.dev/api/auth?sign=F92D01A199C7688C12B634DD5B0D6E5C&appkey=17yfn24TFexGybOF0PqjdYA&logindomain=client&time=1419409520345&otherid=355066064716352

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sign();
                }
            };
            t.start();
        }
    }

    private static void sign() {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult result = httpClient.post("http://api.joyme.beta/joymeapp/gameclient/api/task/sign?uid=1741339&platform=0&appkey=17yfn24TFexGybOF0PqjdYI", null, null);
        System.out.println(result);
    }

    private static void getSignList() {
        HttpClientManager httpClient = new HttpClientManager();
        HttpResult result = httpClient.post("http://api.joyme.test/joymeapp/gameclient/api/task/signinfo?uid=20003", null, null);
        System.out.println(result);
    }

    private void testCreateSignTask() {

        TaskGroup group = new TaskGroup();
        group.setTaskGroupId("gameclient.sign");
        group.setAppKey("default");
        group.setRemoveStatus(ActStatus.UNACT);
        group.setAppPlatform(AppPlatform.IOS);
     //   group.setTaskCount(7);
        group.setTaskGroupName("签到！！！");


        try {
            EventServiceSngl.get().createTaskGroup(group);
        } catch (ServiceException e) {
            e.printStackTrace();
        }


//        for (int i = 0; i < group.getTaskCount(); i++) {
//            Task task = new Task();
//            task.setTaskId(group.getTaskGroupId() + "." + i);
//            task.setPlatform(AppPlatform.IOS);
//            task.setTaskType(TaskType.ONCE_BY_DAY);
//            task.setOverTimes(1);
//            task.setAppKey("default");
//            task.setTaskGroupId(group.getTaskGroupId());
//            task.setTaskType(TaskType.ONCE_BY_DAY);
//            task.setOverTimes(1);
//            task.setTaskName("签到第" + i + "天");
//            task.setRemoveStatus(ActStatus.UNACT);
//            TaskAward award = new TaskAward();
//            award.setType(TaskAwardType.POINT.getCode());
//            award.setValue(String.valueOf(10 * (1 + i)));
//            task.setTaskAward(award);
//            try {
//                EventServiceSngl.get().createTask(task);
//            } catch (ServiceException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
