package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.service.event.EventConstants;
import com.enjoyf.platform.service.event.task.TaskGroupShowType;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.redis.RedisManager;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/7
 * Description:
 */
public class TaskRedisManager {

    private static final String KEY_PREFIX = EventConstants.SERVICE_SECTION + "_task";

    private static final String KEY_TASK_PROFILEFLAG = "_pf_";
    private static final String KEY_TASK_SIGN_SUM = "_sign_sum_";    //历史签到总数
    private static final String KEY_TASK_GROUP_COMPLETE = "_complete_";

    private RedisManager manager;

    public TaskRedisManager(FiveProps p) {
        manager = new RedisManager(p);
    }

    /**
     * 存的是当前签到任务ID的后缀
     *
     * @param profileId
     * @param taskId
     * @param award
     */
    public void putFLag(String profileId, String taskId, int award) {
        manager.set(KEY_PREFIX + KEY_TASK_PROFILEFLAG + profileId + taskId, String.valueOf(award));
    }

    /**
     * 获取当前签到走到第几次了  0到6
     *
     * @param profileId
     * @param taskId
     * @return
     */
    public String getFLag(String profileId, String taskId) {
        return manager.get(KEY_PREFIX + KEY_TASK_PROFILEFLAG + profileId + taskId);
    }

    //profileid/clientid 在某个groupid下的总次数
    public void putSignSum(String profileId, String taskGroupId, int num) {
        manager.set(KEY_PREFIX + KEY_TASK_SIGN_SUM + profileId + taskGroupId, String.valueOf(num));
    }

    public void incrSignSum(String profileId, String taskGroupId, int num) {
        manager.incr(KEY_PREFIX + KEY_TASK_SIGN_SUM + profileId + taskGroupId, num, -1);
    }



    //profileid/clientid 在某个groupid下的总次数
    public String getSignSum(String profileId, String taskGroupId) {
        return manager.get(KEY_PREFIX + KEY_TASK_SIGN_SUM + profileId + taskGroupId);
    }

    //profileid/clientid 在某个groupid下 是否全部完成   0,未完成 1：已完成
    public void putCompleteGroup(String profileId, String taskGroupId, TaskGroupShowType showType) {
        manager.set(KEY_PREFIX + KEY_TASK_GROUP_COMPLETE + profileId + taskGroupId, String.valueOf(showType.getCode()));
    }

    //profileid/clientid 在某个groupid下  是否全部完成     0,未完成 1：已完成
    public String getCompleteGroup(String profileId, String taskGroupId) {
        return manager.get(KEY_PREFIX + KEY_TASK_GROUP_COMPLETE + profileId + taskGroupId);
    }


    public static void main(String[] args) {
        System.out.println(KEY_PREFIX + KEY_TASK_PROFILEFLAG + "99d93ddf22430a783b50d89ffaea64c8" + "gameclient.sign.0");
    }

}
