package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.service.event.EventConstants;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.*;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  14-1-15 下午4:19
 * Description:
 */
public class TaskCache {
    private static final long TIME_OUT_SEC = 60l * 60l * 2l;

    private static final long TIME_OUT_SEC_FIVEMINUTE = 60l * 5l;

    private static final String PREFIX_TASK = "_task_";
    private static final String PREFIX_TASKLOG = "_tasklog_";
    private static final String PREFIX_TASKGROUP = "_taskgroup_";
    private static final String PREFIX_TASK_APP_PLATFORM = "_taskapplist_";
    private static final String PREFIX_TASKGROUPLIST = "_taskgrouplist_";
    private static final String PREFIX_TASKGROUP_ID_LIST = "_taskgroupidlist_";    //added by tony ,存储某个app,在某个平台下的所有groupid的list


    private static final String PREFIX_COMPLETETASK = "_completetask_";

    private static final String PREFIX_TASKLIST_BY_ACTION = "_tlistbyaction_";
    private static final String KEY_TASK_COUNTER = "_taskcounter_";
    private static final String KEY_TASK_OVER_COUNTER = "_taskovercounter_";
    private static final String KEY_TASK_SIGN_COUNTER = "_tasksigncounter_";
    private static final String KEY_TASK_AWARD_COUNTER = "_taskawardcounter_";

    private static final String KEY_TASK_LOCK = "_tasklock_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    TaskCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    //////////////////////////////////////////////////////////////////////taskid ---> task
    public void putTask(Task task) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_TASK + task.getTaskId(), task, TIME_OUT_SEC);
    }

    public Task getTask(String taskId) {
        Object object = manager.get(EventConstants.SERVICE_SECTION + PREFIX_TASK + taskId);
        if (object != null) {
            return (Task) object;
        }
        return null;
    }

    public boolean removeTask(String taskId) {
        return manager.remove(EventConstants.SERVICE_SECTION + PREFIX_TASK + taskId);
    }

    //////////////////////////////////////////////////////////////////////tasklog ---> tasklog
    public void putTaskLog(TaskLog taskLog) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_TASKLOG + taskLog.getLogId(), taskLog, TIME_OUT_SEC);
    }

    public TaskLog getTaskLog(String logId) {
        Object object = manager.get(EventConstants.SERVICE_SECTION + PREFIX_TASKLOG + logId);
        if (object != null) {
            return (TaskLog) object;
        }
        return null;
    }

    public boolean removeTaskLog(String logId) {
        return manager.remove(EventConstants.SERVICE_SECTION + PREFIX_TASKLOG + logId);
    }

    //////////////////////////////////////////////////////////////////////taskgroup --> taskgroup
    public void putTaskGroup(TaskGroup taskGroup) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_TASKGROUP + taskGroup.getTaskGroupId(), taskGroup, TIME_OUT_SEC);
    }

    public TaskGroup getTaskGroup(String groupId) {
        Object object = manager.get(EventConstants.SERVICE_SECTION + PREFIX_TASKGROUP + groupId);
        if (object != null) {
            return (TaskGroup) object;
        }
        return null;
    }

    public boolean removeTaskGroup(String groupId) {
        return manager.remove(EventConstants.SERVICE_SECTION + PREFIX_TASKGROUP + groupId);
    }


    //////////////////////////////////////////////////////////////////////taskgroup ---> taskList
    public void putTaskGroupList(String groupId, List<Task> taskList) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_TASKGROUPLIST + groupId, taskList, TIME_OUT_SEC);
    }

    public List<Task> getTaskGroupList(String groupId) {
        Object object = manager.get(EventConstants.SERVICE_SECTION + PREFIX_TASKGROUPLIST + groupId);
        if (object != null) {
            return (List<Task>) object;
        }
        return null;
    }


//    public void addTaskGroupList(String groupId, Task task) {
//        List<Task> list = getTaskGroupList(groupId);
//        if (CollectionUtil.isEmpty(list)) {
//            list = new ArrayList<Task>();
//        }
//        list.add(task);
//        putTaskGroupList(groupId, list);
//    }


    public boolean removeTaskGroupList(String groupId) {
        return manager.remove(EventConstants.SERVICE_SECTION + PREFIX_TASKGROUPLIST + groupId);
    }
        //////////////////////////////

    public void putTaskGroupIdList(String appKey, AppPlatform appPlatform,TaskGroupType taskGroupType,List<String> groupIds) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_TASKGROUP_ID_LIST + appKey + appPlatform.getCode()+taskGroupType.getCode(), groupIds, TIME_OUT_SEC);
    }

    public List<String> getTaskGroupIdList(String appKey, AppPlatform appPlatform,TaskGroupType taskGroupType) {
        Object object = manager.get(EventConstants.SERVICE_SECTION + PREFIX_TASKGROUP_ID_LIST+ appKey + appPlatform.getCode()+taskGroupType.getCode());
        if (object != null) {
            return (List<String>) object;
        }
        return null;
    }

    public boolean removeTaskGroupIdList(String appKey, AppPlatform appPlatform,TaskGroupType taskGroupType) {
        return manager.remove(EventConstants.SERVICE_SECTION  + PREFIX_TASKGROUP_ID_LIST+ appKey + appPlatform.getCode()+taskGroupType.getCode());
    }

    //////////////////////////////

     //按日期存储 某个平台的app在某一天的所有任务---start  --added by tony 2015-04-30
    public void putTaskAppPlatformList(String appKey, AppPlatform appPlatform, String dateStr, List<Task> taskList) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_TASK_APP_PLATFORM + appKey + appPlatform.getCode() + dateStr, taskList, TIME_OUT_SEC);
    }

    public List<Task> getTaskAppPlatformList(String appKey, AppPlatform appPlatform, String dateStr) {
        Object object = manager.get(EventConstants.SERVICE_SECTION + PREFIX_TASK_APP_PLATFORM + appKey + appPlatform.getCode() + dateStr);
        if (object != null) {
            return (List<Task>) object;
        }
        return null;
    }

    public boolean removeTaskAppPlatformList(String appKey, AppPlatform appPlatform, String dateStr) {
        return manager.remove(EventConstants.SERVICE_SECTION + PREFIX_TASK_APP_PLATFORM + appKey + appPlatform.getCode() + dateStr);
    }
    //按日期存储 某个平台的app在某一天的所有任务---end  --added by tony 2015-04-30

    /////////////////////////////
    public void putCompleteTask(String profileId, Date taskDate, Map<String, TaskLog> taskIds, String appKey, AppPlatform appPlatform) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_COMPLETETASK + profileId + DateUtil.formatDateToString(taskDate, DateUtil.DATE_FORMAT) + appKey + appPlatform.getCode(), taskIds, TIME_OUT_SEC_FIVEMINUTE);
    }


    public Map<String, TaskLog> getCompleteTask(String profileId, Date taskDate, String appKey, AppPlatform appPlatform) {
        Object obj = manager.get(EventConstants.SERVICE_SECTION + PREFIX_COMPLETETASK + profileId + DateUtil.formatDateToString(taskDate, DateUtil.DATE_FORMAT) + appKey + appPlatform.getCode());
        if (obj != null) {
            return (Map<String, TaskLog>) obj;
        }
        return null;
    }

    public boolean removeCompleteTask(String profileId, Date taskDate, String appKey, AppPlatform appPlatform) {
        return manager.remove(EventConstants.SERVICE_SECTION + PREFIX_COMPLETETASK + profileId + DateUtil.formatDateToString(taskDate, DateUtil.DATE_FORMAT) + appKey + appPlatform.getCode());
    }

    ////////////////////////////
    public void putTaskListByAction(TaskAction taskAction, String appkey, AppPlatform platform, List<Task> taskList) {
        manager.put(EventConstants.SERVICE_SECTION + PREFIX_TASKLIST_BY_ACTION + taskAction.getCode() + appkey + platform.getCode(), taskList, TIME_OUT_SEC);
    }

    public List<Task> getTaskListByAction(TaskAction taskAction, String appkey, AppPlatform platform) {
        Object obj = manager.get(EventConstants.SERVICE_SECTION + PREFIX_TASKLIST_BY_ACTION + taskAction.getCode() + appkey + platform.getCode());
        if (obj != null) {
            return (List<Task>) obj;
        }
        return null;
    }

    public boolean removeTaskListByAction(TaskAction action, String appKey, AppPlatform platform) {
        return manager.remove(EventConstants.SERVICE_SECTION + PREFIX_TASKLIST_BY_ACTION + action.getCode() + appKey + platform.getCode());
    }

    public void addTaskCounter(Date date) {
        manager.addOrIncr(EventConstants.SERVICE_SECTION + KEY_TASK_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    public void addTaskOverCounter(Date date) {
        manager.addOrIncr(EventConstants.SERVICE_SECTION + KEY_TASK_OVER_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    public void addTaskSignCounter(Date date) {
        manager.addOrIncr(EventConstants.SERVICE_SECTION + KEY_TASK_SIGN_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    public void addTaskAwardCounter(Date date) {
        manager.addOrIncr(EventConstants.SERVICE_SECTION + KEY_TASK_AWARD_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    /**
     * key=taskid(如果是签到taskaction)+profileid
     * <p/>
     * vlue=lockobj
     *
     * @param lockkey
     * @param profileId
     * @param lockObj
     */
    public void lockTask(String lockkey, String profileId, String lockObj) {
        manager.put(EventConstants.SERVICE_SECTION + KEY_TASK_LOCK + profileId + lockkey, lockObj, 60);
    }

    /**
     * 得到锁
     *
     * @param lockkey
     * @param profileId
     * @return
     */
    public String getTaskLock(String lockkey, String profileId) {
        Object obj = manager.get(EventConstants.SERVICE_SECTION + KEY_TASK_LOCK + profileId + lockkey);
        if (obj != null) {
            return String.valueOf(obj);
        }
        return null;
    }

    /**
     * 去掉锁
     *
     * @param lockkey
     * @param profileId
     */
    public void unlockTask(String lockkey, String profileId) {
        manager.remove(EventConstants.SERVICE_SECTION + KEY_TASK_LOCK + profileId + lockkey);
    }

}
