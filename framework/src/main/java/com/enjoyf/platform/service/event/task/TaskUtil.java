package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/6
 * Description:
 */
public class TaskUtil {

    public static String getTaskLogId(String profileId, String taskId, TaskType taskType, TaskAction taskAction) {
        String taskLogId = null;

        //签到任务的taskid会重复
        if (taskAction != null && taskAction.equals(TaskAction.SIGN)) {
            taskLogId = Md5Utils.md5(profileId + taskId + DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT)).toLowerCase();
        } else if (TaskType.ONCE_BY_DAY.equals(taskType) || TaskType.ONCE.equals(taskType)) {
            taskLogId = Md5Utils.md5(profileId + taskId).toLowerCase();    //logid 只有profileid和taskid决定
        } else if (TaskType.NO_LIMIT.equals(taskType)) {
            taskLogId = Md5Utils.md5(UUID.randomUUID().toString()).toLowerCase();
        } else {
            GAlerter.lab(TaskUtil.class.getName() + " logid error.profileId " + profileId + " taskId: " + taskId);
        }

        return taskLogId;
    }

    public static String getTaskId(String groupId, String code) {
        return groupId + "." + code;
    }


    public static String getTaskGroupId(String groupId, AppPlatform appPlatform) {
        return groupId + "." + appPlatform.getCode();
    }

    //2015-05-04 modified by tony
    public static Date getDoTaskDate(Date taskDate, Date taskBeginTime) {
        Date date = taskDate;

        //数据库默认begin_time字段为空，是这次的新加字段，历史数据中存在null值，新增的应该不会有begin_time为null的情况，这个判断是为了兼容历史数据
        if (taskBeginTime != null) {
            Calendar origin = Calendar.getInstance();
            origin.setTime(date);
            Calendar offset = Calendar.getInstance();
            offset.setTime(taskBeginTime);
            int hour = offset.get(Calendar.HOUR_OF_DAY);
            int minutes = offset.get(Calendar.MINUTE);

            origin.add(Calendar.HOUR_OF_DAY, -hour);
            origin.add(Calendar.MINUTE, -minutes);
            date = origin.getTime();
        }
        return date;
    }
}
