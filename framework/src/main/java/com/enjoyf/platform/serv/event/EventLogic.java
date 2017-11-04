package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.event.EventMongoHandler;
import com.enjoyf.platform.db.event.TaskHandler;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.serv.event.processor.ChainProcessor;
import com.enjoyf.platform.serv.event.processor.WriteToDBProcessor;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.EventService;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.service.event.pageview.PageViewLocation;
import com.enjoyf.platform.service.event.system.ProfileOnlineOnEvent;
import com.enjoyf.platform.service.event.system.TaskAwardEvent;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The UserLogic class holds the core logic for the server. This class is
 * expected to change almost completely from server to server.
 * <p/>
 * <p/>
 * UserLogic is called by ExamplePacketDecoder.
 */
class EventLogic implements EventService {
    //
    private static final Logger logger = LoggerFactory.getLogger(EventLogic.class);

    //
    private EventConfig config;

    //
    private QueueThreadN eventProcessQueueThreadN = null;
    private ChainProcessor chainProcessor = null;

    private TaskHandler writeAbleHandler = null;
    private HandlerPool<TaskHandler> readonlyHandlersPool = null;

    private TaskRedisManager taskRedisManager;
    private TaskCache taskCache;

    private EventMongoHandler mongoHandler;

    EventLogic(EventConfig cfg) {
        config = cfg;

        try {
            writeAbleHandler = new TaskHandler(config.getWriteableDataSourceName(), config.getProps());

            readonlyHandlersPool = new HandlerPool<TaskHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                //create the handler and put it into the map.

                readonlyHandlersPool.add(new TaskHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        try {
            mongoHandler = new EventMongoHandler(config.getMongoDbWriteAbleDateSourceName(), config.getProps());
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //get the config
        initUserEventProcessorChain();

        // init queue thread
        eventProcessQueueThreadN = new QueueThreadN(config.getEventProcessQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                processQueuedEvent(obj);
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        taskRedisManager = new TaskRedisManager(config.getProps());
        taskCache = new TaskCache(config.getMemCachedConfig());
    }

    //
    private void initUserEventProcessorChain() {
        // initialize
        chainProcessor = new ChainProcessor();

        // add the processor to the chain.
        // please pay attention to the processor order.
        chainProcessor.addProcessor(new WriteToDBProcessor(mongoHandler));
    }

    private void processQueuedEvent(Object event) {
        if (event instanceof TaskAwardEvent) {
            TaskAwardEvent taskAwardEvent = (TaskAwardEvent) event;
            if (taskAwardEvent.getTaskAction() == null ||
                    (StringUtil.isEmpty(taskAwardEvent.getProfileId()) && StringUtil.isEmpty(taskAwardEvent.getClientId()))) {
                GAlerter.lan(this.getClass().getName() + " processTaskAwardEvent errror.action and taskid is null or profileid is null! event:" + taskAwardEvent);
                return;
            }

            if (taskAwardEvent.getTaskAction().equals(TaskAction.SIGN)) {
                processTaskAwardSignEvent(taskAwardEvent);
            } else {
                processTaskAwardEvent(taskAwardEvent);
            }
        } else {
            GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj." + event);
        }
    }

    // recieve the player event
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event is recieved, event:" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }

    //
    public boolean reportUserEvent(UserEvent event, boolean immediate) throws ServiceException {
        //nothing
        return false;
    }

    @Override
    public boolean reportPageViewEvent(PageViewEvent event) throws ServiceException {
        //nothing
        return false;
    }

    //
    public boolean reportEvent(Event event, boolean immediate) throws ServiceException {
        if (immediate) {
            processQueuedEvent(event);

            return true;
        } else {
            eventProcessQueueThreadN.add(event);

            return true;
        }
    }

    @Override
    public PageViewLocation getPageViewLocationById(Integer locationId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event logic call the handler to getPageViewLocationById, locationId:" + locationId);
        }

        return null;
    }

    @Override
    public List<PageViewLocation> queryAllPageViewLocations() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event logic call the handler to queryAllPageViewLocations.");
        }

        return null;
    }

    @Override
    public long statsPageView(Date from, Date to, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event logic call the handler to statsPageView, from:" + from + ", to:" + to + ", queryExpress:" + queryExpress);
        }

        return 0;
    }

    @Override
    public long statsUniqueUsers(Date from, Date to, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event logic call the handler to statsUniqueUser, from:" + from + ", to:" + to + ", queryExpress:" + queryExpress);
        }
        return 0;
    }


    @Override
    public Task createTask(Task task) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic createTask.task:" + task.toString());
        }
        task = writeAbleHandler.createTask(task);

        if (task != null) {
            taskCache.removeTaskGroupList(task.getTaskGroupId());
            //   taskCache.removeTaskAppPlatformList(task.getAppKey(), task.getPlatform());
            String todayDateStr = DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT);
            taskCache.removeTaskAppPlatformList(task.getAppKey(), task.getPlatform(), todayDateStr);   //modified by tony 2015-05-04
            taskCache.removeTaskListByAction(task.getTaskAction(), task.getAppKey(), task.getPlatform());
        }

        return task;
    }

    @Override
    public Task getTask(String taskId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic createTask.taskId:" + taskId);
        }

        Task task = taskCache.getTask(taskId);
        if (task == null) {
            task = readonlyHandlersPool.getHandler().getTask(new QueryExpress().add(QueryCriterions.eq(TaskField.TASK_ID, taskId)));
            if (task != null) {
                taskCache.putTask(task);
            }
        }
        return task;
    }

    @Override
    public List<Task> queryTask(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic createTask.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryTask(queryExpress);
    }

    @Override
    public PageRows<Task> queryTaskByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic createTask.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryTaskByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyTask(UpdateExpress updateExpress, String taskId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic modifyTask.updateExpress:" + updateExpress + " taskId:" + taskId);
        }

        boolean returnBoolean = writeAbleHandler.modifyTask(updateExpress, new QueryExpress().add(QueryCriterions.eq(TaskField.TASK_ID, taskId)));
        if (returnBoolean) {
            Task task = getTask(taskId);
            if (task != null) {
                taskCache.removeTaskGroupList(task.getTaskGroupId());
                //  taskCache.removeTaskAppPlatformList(task.getAppKey(), task.getPlatform());
                String todayDateStr = DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT);
                taskCache.removeTaskAppPlatformList(task.getAppKey(), task.getPlatform(), todayDateStr);   //modified by tony 2015-05-04
                taskCache.removeTaskListByAction(task.getTaskAction(), task.getAppKey(), task.getPlatform());
            }
            taskCache.removeTask(taskId);
        }
        return returnBoolean;
    }

    @Override
    public TaskLog createTaskLog(TaskLog taskLog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic createTaskLog.taskLog:" + taskLog);
        }
        taskLog = writeAbleHandler.createTaskLog(taskLog);
        if (taskLog != null) {
            taskCache.addTaskCounter(taskLog.getOverTime());
            if (taskLog.getOverStatus().equals(ActStatus.ACTED)) {
                taskCache.addTaskOverCounter(taskLog.getOverTime());
            }
        }
        taskCache.removeCompleteTask(taskLog.getProfileId(), taskLog.getOverTime(), taskLog.getAppKey(), taskLog.getPlatform());
        return taskLog;
    }

    @Override
    public TaskLog getTaskLog(String taskLogId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic getTaskLog.taskLogId:" + taskLogId);
        }

        TaskLog taskLog = taskCache.getTaskLog(taskLogId);
        if (taskLog == null) {
            taskLog = readonlyHandlersPool.getHandler().getTaskLog(new QueryExpress().add(QueryCriterions.eq(TaskLogField.LOG_ID, taskLogId)));
            if (taskLog != null) {
                taskCache.putTaskLog(taskLog);
            }
        }
        return taskLog;
    }

    @Override
    public List<TaskLog> queryTaskLog(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic queryTaskLog.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryTaskLog(queryExpress);
    }

    @Override
    public PageRows<TaskLog> queryTaskLogByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic queryTaskLog.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryTaskLogByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyTaskLog(UpdateExpress updateExpress, String taskLogId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic queryTaskLog.queryExpress:" + taskLogId);
        }
        TaskLog taskLog = getTaskLog(taskLogId);
        if (taskLog == null) {
            return false;
        }

        boolean bool = writeAbleHandler.modifyTaskLog(updateExpress, new QueryExpress().add(QueryCriterions.eq(TaskLogField.LOG_ID, taskLogId)));

        if (bool) {
            List<UpdateAttribute> updateAttributes = updateExpress.getUpdateAttributes();
            for (UpdateAttribute updateAttribute : updateAttributes) {
                if (updateAttribute != null) {
                    if (updateAttribute.getField().getColumn().equals(TaskLogField.OVER_STATUS.getColumn()) && updateAttribute.getValue().equals(ActStatus.ACTED.getCode())) {
                        taskCache.addTaskOverCounter((Date) updateExpress.getUpdateValueByField(TaskLogField.OVER_DATE));
                    }
                    if (updateAttribute.getField().getColumn().equals(TaskLogField.OVER_TIMES.getColumn()) && updateAttribute.getUpdateType().equals(UpdateType.INCREASE) && 1 == (Integer) updateAttribute.getValue()) {
                        taskCache.addTaskCounter((Date) updateExpress.getUpdateValueByField(TaskLogField.OVER_DATE));
                    }
                }

            }
            taskCache.removeTaskLog(taskLogId);
            taskCache.removeCompleteTask(taskLog.getProfileId(), taskLog.getOverTime(), taskLog.getAppKey(), taskLog.getPlatform());
        }

        return bool;
    }


    @Override
    public TaskGroup createTaskGroup(TaskGroup taskGroup) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic createTaskGroup.taskGroup:" + taskGroup);
        }

        taskCache.removeTaskGroupIdList(taskGroup.getAppKey(), taskGroup.getAppPlatform(), taskGroup.getType());   //清空 groupid 的list 缓存
        return writeAbleHandler.insertTaskGroup(taskGroup);
    }

    @Override
    public TaskGroup getTaskGroup(String taskGroupId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic createTaskGroup.taskGroupId:" + taskGroupId);
        }

        TaskGroup group = taskCache.getTaskGroup(taskGroupId);
        if (group == null) {
            group = readonlyHandlersPool.getHandler().getTaskGroup(new QueryExpress().add(QueryCriterions.eq(TaskGroupField.TASK_GROUP_ID, taskGroupId)));
            if (group != null) {
                taskCache.putTaskGroup(group);
            }
        }

        return group;
    }

    @Override
    public List<TaskGroup> queryTaskGroup(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic queryTaskGroup.queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryTaskGroup(queryExpress);
    }

    @Override
    public boolean modifyTaskGroup(UpdateExpress updateExpress, String groupId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic modifyTaskGroup.updateExpress:" + updateExpress + " groupId:" + groupId);
        }

        boolean bval = writeAbleHandler.updateTaskGroup(updateExpress, new QueryExpress().add(QueryCriterions.eq(TaskGroupField.TASK_GROUP_ID, groupId)));
        if (bval) {
            taskCache.removeTaskGroup(groupId);
            TaskGroup group = getTaskGroup(groupId);
            taskCache.removeTaskGroupIdList(group.getAppKey(), group.getAppPlatform(), group.getType());   //清空 groupid 的list 缓存
        }
        return bval;
    }

    //存储某个appkey在某个平台下所有有效的任务 ，任务组类型为非关联性任务
    @Override
    public Map<String, List<Task>> queryTaskByGroupIds(String appKey, AppPlatform appPlatform, TaskGroupType taskGroupType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic queryTaskByGroupIds.appKey:" + appKey + ",appPlatform :" + appPlatform.getCode());
        }

        Map<String, List<Task>> map = new LinkedHashMap<String, List<Task>>();

        List<String> groupIds = taskCache.getTaskGroupIdList(appKey, appPlatform, taskGroupType);
        if (CollectionUtil.isEmpty(groupIds)) {
            groupIds = new ArrayList<String>();
            List<TaskGroup> groupList = readonlyHandlersPool.getHandler().queryTaskGroup(new QueryExpress().add(QueryCriterions.eq(TaskGroupField.APPKEY, appKey))
                    .add(QueryCriterions.eq(TaskGroupField.PLATFORM, appPlatform.getCode()))
                    .add(QueryCriterions.eq(TaskGroupField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QueryCriterions.eq(TaskGroupField.TYPE, taskGroupType.getCode()))
                    .add(QuerySort.add(TaskGroupField.DISPLAY_ORDER, QuerySortOrder.ASC)));
            if (!CollectionUtil.isEmpty(groupList)) {

                for (TaskGroup group : groupList) {
                    groupIds.add(group.getTaskGroupId());
                }
                taskCache.putTaskGroupIdList(appKey, appPlatform, taskGroupType, groupIds);
            }
        }

        if (CollectionUtil.isEmpty(groupIds)) {
            return map;
        }


        for (String groupId : groupIds) {
            List<Task> taskList = getTaskGroupList(groupId);

            if (!CollectionUtil.isEmpty(taskList)) {
                map.put(groupId, taskList);
            }
        }

        return map;
    }


    @Override
    public TaskLog getTaskLogByGroupIdProfileId(String groupId, String profileId, Date taskDate) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TaskLogic getTaskLogByGroupId.groupId:" + profileId + " profileId: " + groupId + " taskDate:" + taskDate);
        }

        //todo 临时方法 用于判断签到 new一个空的log???!!!!
        if (!StringUtil.isEmpty(taskCache.getTaskLock(groupId, profileId))) {
            return null;
        }

        String times = taskRedisManager.getFLag(profileId, groupId);

        if (StringUtil.isEmpty(times)) {
            times = "0";
        }

        String taskId = TaskUtil.getTaskId(groupId, times);

        Task task = getTask(taskId);

        return getTaskLog(TaskUtil.getTaskLogId(profileId, taskId, task.getTaskType(), task.getTaskAction()));

    }

    //获取clientid/profileid 在某个组的总签到次数
    @Override
    public int querySignSumByProfileIdGroupId(String profileId, String groupId) throws ServiceException {
        String times = taskRedisManager.getSignSum(profileId, groupId);
        int timesInt = 0;
        if (StringUtil.isEmpty(times)) {
            List<Task> tasks = getTaskGroupList(groupId);
            for (int i = 0; i < tasks.size(); i++) {
                //todo 直接取count
                List<TaskLog> logs = readonlyHandlersPool.getHandler().queryTaskLog(new QueryExpress().add(QueryCriterions.eq(TaskLogField.PROFILE_ID, profileId))
                        .add(QueryCriterions.eq(TaskLogField.TASK_ID, tasks.get(i).getTaskId())));
                if (!CollectionUtil.isEmpty(logs)) {
//                    timesInt += logs.size();
                    taskRedisManager.incrSignSum(profileId, groupId, logs.size());
                }
            }
//            taskRedisManager.putSignSum(profileId, groupId, timesInt);
        } else {
            try {
                timesInt = Integer.valueOf(times);
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException.profileId:" + profileId + " groupId:" + groupId);
            }
        }
        return timesInt;
    }


    //获取clientid/profileid 在某个组的是否已经全部完成
    @Override
    public TaskGroupShowType queryCompleteGroupByProfileIdGroupId(String profileId, String groupId) throws ServiceException {
        String value = taskRedisManager.getCompleteGroup(profileId, groupId);
        TaskGroupShowType showType = null;

        //缓存为空
        if (StringUtil.isEmpty(value)) {
            TaskLog taskLog = getTaskLog(TaskUtil.getTaskLogId(profileId, groupId, TaskType.ONCE, null));
            if (taskLog != null) {
                showType = TaskGroupShowType.getByCode(taskLog.getOverTimes());
                taskRedisManager.putCompleteGroup(profileId, groupId, showType);
            }
        } else {
            try {
                showType = TaskGroupShowType.getByCode(Integer.valueOf(value));
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured ServiceException: profileid" + profileId + " groupid " + groupId);
            }
        }

        //
        if (showType == null) {
            showType = TaskGroupShowType.SHOW;
        }

        return showType;
    }

    //根clientid/profileid +groupid 设置是否已经全部完成
    @Override
    public TaskGroupShowType setCompleteGroupByProfileIdGroupId(String profileId, String groupId, TaskGroupShowType showType) throws ServiceException {

        taskRedisManager.putCompleteGroup(profileId, groupId, showType);

        String taskLogId = TaskUtil.getTaskLogId(profileId, groupId, TaskType.ONCE, null);
        TaskLog taskLog = getTaskLog(taskLogId);

        if (taskLog != null) {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TaskLogField.OVER_STATUS, ActStatus.ACTED.getCode());
            updateExpress.set(TaskLogField.OVER_TIMES, showType.getCode());
            updateExpress.set(TaskLogField.OVER_DATE, new Date());
            modifyTaskLog(updateExpress, taskLogId);

        } else {
            taskLog = new TaskLog();
            taskLog.setLogId(taskLogId);
            taskLog.setProfileId(profileId);
            taskLog.setTaskType(TaskType.ONCE);
            taskLog.setTaskId(groupId);
            taskLog.setOverIp("127.0.0.1");
            taskLog.setOverTime(new Date());
            taskLog.setOverTimes(showType.getCode());

            //是否能透传过来减少一次查询
            TaskGroup taskGroup = getTaskGroup(groupId);
            taskLog.setAppKey(taskGroup.getAppKey());
            taskLog.setPlatform(taskGroup.getAppPlatform());

            taskLog.setOverStatus(ActStatus.ACTED);

            try {
                createTaskLog(taskLog);
            } catch (ServiceException e) {
                logger.info(this.getClass().getName() + " occured Exception.e:" + e);
            }
        }
        return showType;
    }

    @Override
    public List<Task> queryTaskByGroupIdProfileId(String groupId, String profileId) throws ServiceException {
        //这个签到任务组里含有的任务数量，也是循环周期
        List<Task> tasks = getTaskGroupList(groupId);
        int groupSize = 0;
        if (!CollectionUtil.isEmpty(tasks)) {
            groupSize = tasks.size();
        }

        //todo profileId 如果错误纠错机制是什么？待开发
        String times = taskRedisManager.getFLag(profileId, groupId);
        int todayTaskIdSuffix = -1;
        if (!StringUtil.isEmpty(times)) {
            try {
                todayTaskIdSuffix = Integer.parseInt(times);
            } catch (NumberFormatException e) {
                GAlerter.lab(this.getClass().getName() + " occured times error.e:", e);
            }
        }

        if (todayTaskIdSuffix < 0) {
            todayTaskIdSuffix = 0;
        } else {
            if (getTaskLogByGroupIdProfileId(groupId, profileId, new Date()) == null) {
                todayTaskIdSuffix = todayTaskIdSuffix + 1;
                if (todayTaskIdSuffix >= groupSize) {
                    todayTaskIdSuffix = 0;
                }
            }
        }


        int yesterdayTaskIdSuffix = 0;
        int tomorrowYaskIdSuffix = 0;
        if (todayTaskIdSuffix == 0) {
            yesterdayTaskIdSuffix = groupSize - 1;
            tomorrowYaskIdSuffix = todayTaskIdSuffix + 1;
        } else if (todayTaskIdSuffix >= groupSize - 1) {
            yesterdayTaskIdSuffix = todayTaskIdSuffix - 1;
            tomorrowYaskIdSuffix = 0;
        } else {
            yesterdayTaskIdSuffix = todayTaskIdSuffix - 1;
            tomorrowYaskIdSuffix = todayTaskIdSuffix + 1;
        }
        List<Task> taskList = new ArrayList<Task>();

        taskList.add(getTask(TaskUtil.getTaskId(groupId, String.valueOf(yesterdayTaskIdSuffix))));
        taskList.add(getTask(TaskUtil.getTaskId(groupId, String.valueOf(todayTaskIdSuffix))));
        taskList.add(getTask(TaskUtil.getTaskId(groupId, String.valueOf(tomorrowYaskIdSuffix))));

        return taskList;
    }

    //返回所有今天已经完成的任务
    @Override
    public Map<String, TaskLog> checkCompleteTask(String profileId, String appKey, AppPlatform appPlatform, Date taskDate) throws ServiceException {

        Map<String, TaskLog> returnTaskIds = taskCache.getCompleteTask(profileId, taskDate, appKey, appPlatform);
        if (returnTaskIds == null) {
            returnTaskIds = new HashMap<String, TaskLog>();
            List<Task> tasks = queryTaskByAppKeyPlatform(appKey, appPlatform);   //包括所有今天可能有效的任务,不一定是当前这一时刻有效的任务
            if (CollectionUtil.isEmpty(tasks)) {
                return null;
            }

            for (Task task : tasks) {

                String taskLogId = TaskUtil.getTaskLogId(profileId, task.getTaskId(), task.getTaskType(), task.getTaskAction());
                //
                TaskLog tasklog = getTaskLog(taskLogId);
                if (tasklog != null) {
                    returnTaskIds.put(task.getTaskId(), tasklog);
                }
            }
            taskCache.putCompleteTask(profileId, taskDate, returnTaskIds, appKey, appPlatform);
        }

        //从已完成的任务中过滤掉当前已经失效的任务 亮点
        Iterator<Map.Entry<String, TaskLog>> it = returnTaskIds.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TaskLog> entry = it.next();
            Task task = getTask(entry.getKey());
            if (task.getBeginTime() == null || task.getEndTime() == null
                    || task.getBeginTime().getTime() > taskDate.getTime() || task.getEndTime().getTime() < taskDate.getTime()) {
                it.remove();
            }
        }
        return returnTaskIds;
    }

    //返回今日有效的除签到任务外的所有任务
    //今天有效的任务不一定在 今天的某一特定时刻 依然有效
    private List<Task> queryTaskByAppKeyPlatform(String appKey, AppPlatform appPlatform) throws ServiceException {
        String todayDateStr = DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT);
        List<Task> tasks = taskCache.getTaskAppPlatformList(appKey, appPlatform, todayDateStr);

        Date todayBeginDate = DateUtil.ignoreTime(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayBeginDate);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayEndDate = calendar.getTime();

        if (tasks == null) {
            tasks = queryTask(new QueryExpress()
                    .add(QueryCriterions.eq(TaskField.APPKEY, appKey))
                    .add(QueryCriterions.eq(TaskField.PLATFORM, appPlatform.getCode()))
                    .add(QueryCriterions.eq(TaskField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QueryCriterions.leq(TaskField.BEGIN_TIME, todayEndDate))
                    .add(QueryCriterions.gt(TaskField.END_TIME, todayBeginDate)));
            taskCache.putTaskAppPlatformList(appKey, appPlatform, todayDateStr, tasks);
        }
        return tasks;
    }

    @Override
    public TaskAward getAward(String profileId, String taskId, Date awardDate, String appkey, String uno, String profileKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" getaward profileId:" + profileId + " taskId:" + taskId + " awardDate:" + awardDate);
        }

        Task task = getTask(taskId);
        if (task == null || task.getTaskAward() == null) {
            return null;
        }

        String taskLogId = TaskUtil.getTaskLogId(profileId, taskId, task.getTaskType(), task.getTaskAction());

        TaskLog taskLog = getTaskLog(taskLogId);

        if (!taskLog.getOverStatus().equals(ActStatus.REJECTED)) {
            return null;
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(TaskLogField.OVER_STATUS, ActStatus.ACTED.getCode());
        updateExpress.set(TaskLogField.OVER_DATE, awardDate);
        boolean modifyResult = this.modifyTaskLog(updateExpress, taskLogId);

        if (modifyResult) {
            sendAward(task, task.getTaskAward(), profileId, appkey, awardDate, uno);
            return task.getTaskAward();
        }

        return null;
    }

    private void processTaskAwardEvent(TaskAwardEvent taskAwardEvent) {
        try {
            List<Task> taskList = new ArrayList<Task>();

            // 通过action得到该action下所有的task
            taskList = getTaskListByAction(taskAwardEvent.getTaskAction(), AppUtil.getAppKey(taskAwardEvent.getAppkey()), taskAwardEvent.getAppPlatform());

            if (CollectionUtil.isEmpty(taskList)) {
                return;
            }

            for (Task task : taskList) {
                //get lock
                String userId = getIdBytaskVerifyId(taskAwardEvent, task.getTaskVerifyId());
                String lockObj = taskCache.getTaskLock(String.valueOf(taskAwardEvent.getTaskAction().getCode()), userId);

                if (!StringUtil.isEmpty(lockObj)) {
                    GAlerter.lan(this.getClass().getName() + " task has locked: event" + taskAwardEvent);
                    return;
                }
                taskCache.lockTask(String.valueOf(taskAwardEvent.getTaskAction().getCode()), userId, String.valueOf(taskAwardEvent.getTaskAction().getCode()));

                try {
                    //如果是规则完成某个具体任务
                    if (!StringUtil.isEmpty(task.getDirectId()) && !task.getDirectId().equals(taskAwardEvent.getDirectId())) {
                        continue;
                    }

                    if (task.getBeginTime() != null && task.getBeginTime().getTime() > taskAwardEvent.getDoTaskDate().getTime()) {
                        continue;
                    }

                    if (task.getEndTime() != null && task.getEndTime().getTime() < taskAwardEvent.getDoTaskDate().getTime()) {
                        continue;
                    }

                    String taskLogId = TaskUtil.getTaskLogId(userId, task.getTaskId(), task.getTaskType(), task.getTaskAction());

                    TaskLog taskLog = null;
                    try {
                        taskLog = getTaskLog(taskLogId);
                    } catch (ServiceException e) {
                        logger.info(this.getClass().getName() + " occured Exception.e:" + e);
                    }

                    boolean canAwardLog = false;  //只有完成任务，并且任务发奖类型是自动发奖才是true

                    //判断任务的完成次数
                    if (task.getOverTimes() == 1) {
                        if (taskLog != null) {
                            continue;  //如果任务已经完成过了，则直接返回
                        }

                        taskLog = new TaskLog();
                        taskLog.setLogId(taskLogId);

                        taskLog.setProfileId(userId);
                        taskLog.setTaskType(task.getTaskType());
                        taskLog.setTaskId(task.getTaskId());
                        taskLog.setOverIp(taskAwardEvent.getTaskIp());
                        taskLog.setOverTime(taskAwardEvent.getDoTaskDate());
                        taskLog.setOverTimes(1);
                        taskLog.setAppKey(taskAwardEvent.getAppkey());
                        taskLog.setPlatform(taskAwardEvent.getAppPlatform());

                        //自动领取所以直接发奖
                        if (task.isAutoSendAward()) {
                            taskLog.setOverStatus(ActStatus.ACTED);
                            canAwardLog = true;
                        } else {
                            taskLog.setOverStatus(ActStatus.REJECTED);
                        }

                        try {
                            createTaskLog(taskLog);
                        } catch (ServiceException e) {
                            logger.info(this.getClass().getName() + " occured Exception.e:" + e);
                            canAwardLog = false;
                        }
                    } else {
                        if (taskLog != null && !taskLog.getOverStatus().equals(ActStatus.ACTING)) {
                            continue;
                        }

                        if (taskLog == null) {
                            taskLog = new TaskLog();
                            taskLog.setLogId(taskLogId);

                            taskLog.setProfileId(userId);
                            taskLog.setTaskType(task.getTaskType());
                            taskLog.setTaskId(task.getTaskId());
                            taskLog.setOverIp(taskAwardEvent.getTaskIp());
                            taskLog.setOverStatus(ActStatus.ACTING);
                            taskLog.setOverTime(taskAwardEvent.getDoTaskDate());
                            taskLog.setOverTimes(1);
                            taskLog.setAppKey(taskAwardEvent.getAppkey());
                            taskLog.setPlatform(taskAwardEvent.getAppPlatform());
                            try {
                                createTaskLog(taskLog);
                            } catch (ServiceException e) {
                                logger.info(this.getClass().getName() + " occured Exception.e:" + e);
                            }
                        } else {
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(TaskLogField.OVER_DATE, taskAwardEvent.getDoTaskDate());
                            if (taskLog.getOverTimes() + 1 >= task.getOverTimes()) {
                                updateExpress.set(TaskLogField.OVER_TIMES, task.getOverTimes());
                                //自动领取所以直接发奖
                                if (task.isAutoSendAward()) {
                                    updateExpress.set(TaskLogField.OVER_STATUS, ActStatus.ACTED.getCode());
                                    canAwardLog = true;
                                } else {
                                    updateExpress.set(TaskLogField.OVER_STATUS, ActStatus.REJECTED.getCode());
                                }

                                boolean modifyResult = this.modifyTaskLog(updateExpress, taskLogId);
                                canAwardLog = canAwardLog && modifyResult;
                            } else {
                                updateExpress.increase(TaskLogField.OVER_TIMES, 1);
                                this.modifyTaskLog(updateExpress, taskLogId);
                            }
                        }
                    }

                    if (canAwardLog) {
                        sendAward(task, task.getTaskAward(), taskAwardEvent.getProfileId(), taskAwardEvent.getAppkey(), taskAwardEvent.getDoTaskDate(), taskAwardEvent.getUno());
                    }
                } finally {
                    try {
                        if (taskAwardEvent.getTaskAction() == null || (StringUtil.isEmpty(taskAwardEvent.getProfileId()) && StringUtil.isEmpty(taskAwardEvent.getClientId()))) {
                            return;
                        }
                        taskCache.unlockTask(String.valueOf(taskAwardEvent.getTaskAction().getCode()), userId);
                    } catch (Exception e) {
                        GAlerter.lan(this.getClass().getName() + " unlock faild.e: ", e);
                    }
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException");
        }
    }

    //如果是签到任务
    private void processTaskAwardSignEvent(TaskAwardEvent taskAwardEvent) {

        //签到
        int flag = -1;
        int taskVerifyId = 0;
        String userId = "";
        try {
            String lockObj = null;

            //这个签到任务组里含有的任务数量，也是循环周期
            List<Task> tasks = getTaskGroupList(taskAwardEvent.getTaskGroupId());
            int groupSize = 0;
            if (!CollectionUtil.isEmpty(tasks)) {
                groupSize = tasks.size();
            }

            if (CollectionUtil.isEmpty(tasks)) {
                return;
            }

            //按第一个签到任务是按设备还是按用户来决定整个组是按设备还是按用户
            Task FirstTask = tasks.get(0);
            taskVerifyId = FirstTask.getTaskVerifyId();
            userId = getIdBytaskVerifyId(taskAwardEvent, taskVerifyId);

            lockObj = taskCache.getTaskLock(taskAwardEvent.getTaskGroupId(), userId);
            if (!StringUtil.isEmpty(lockObj)) {
                GAlerter.lab(this.getClass().getName() + " task has locked: event" + taskAwardEvent);
                return;
            }
            taskCache.lockTask(taskAwardEvent.getTaskGroupId(), userId, taskAwardEvent.getTaskGroupId());

            TaskGroup taskGroup = getTaskGroup(taskAwardEvent.getTaskGroupId());
            if (taskGroup == null) {
                return;
            }
            // 得到其签到任务的id---> redis得到的值+1
            flag = getSignFlag(userId, taskGroup.getTaskGroupId());


            //取当前签到到第几天了
            String taskId = "";
            if (flag < 0) {
                flag = 0;
                taskId = TaskUtil.getTaskId(taskGroup.getTaskGroupId(), String.valueOf(flag));
            } else if (flag < groupSize - 1) {
                flag = flag + 1;
                taskId = TaskUtil.getTaskId(taskGroup.getTaskGroupId(), String.valueOf(flag));
            } else {
                flag = 0;
                taskId = TaskUtil.getTaskId(taskGroup.getTaskGroupId(), String.valueOf(flag));
            }

            Task task = getTask(taskId);
            //该消失签到多少次
            if (task != null) {
                taskCache.addTaskSignCounter(taskAwardEvent.getDoTaskDate());
            } else {
                return;
            }

            String taskLogId = TaskUtil.getTaskLogId(userId, task.getTaskId(), task.getTaskType(), task.getTaskAction());


            boolean canAwardLog = false;  //只有完成任务，并且任务发奖类型是自动发奖才是true


            TaskLog taskLog = new TaskLog();
            taskLog.setLogId(taskLogId);
            taskLog.setProfileId(userId);
            taskLog.setTaskType(task.getTaskType());
            taskLog.setTaskId(task.getTaskId());
            taskLog.setOverIp(taskAwardEvent.getTaskIp());
            taskLog.setOverTime(taskAwardEvent.getDoTaskDate());
            taskLog.setOverTimes(1);
            taskLog.setAppKey(taskAwardEvent.getAppkey());
            taskLog.setPlatform(taskAwardEvent.getAppPlatform());

            //自动领取所以直接发奖
            if (task.isAutoSendAward()) {
                taskLog.setOverStatus(ActStatus.ACTED);
                canAwardLog = true;
            } else {
                taskLog.setOverStatus(ActStatus.REJECTED);
            }

            try {
                createTaskLog(taskLog);
            } catch (ServiceException e) {
                logger.info(this.getClass().getName() + " occured Exception.e:" + e);
                canAwardLog = false;
            }
            if (canAwardLog) {
                sendAward(task, task.getTaskAward(), taskAwardEvent.getProfileId(), taskAwardEvent.getAppkey(), taskAwardEvent.getDoTaskDate(), taskAwardEvent.getUno());
            }
            //签到任务
            if (canAwardLog && flag >= 0 && !StringUtil.isEmpty(taskAwardEvent.getTaskGroupId())) {
                taskRedisManager.putFLag(userId, taskAwardEvent.getTaskGroupId(), flag);
                taskRedisManager.incrSignSum(userId, task.getTaskGroupId(), 1);
            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException");
        } finally {
            try {
                if (taskAwardEvent.getTaskAction() == null || (StringUtil.isEmpty(taskAwardEvent.getProfileId()) && StringUtil.isEmpty(taskAwardEvent.getClientId()))) {
                    return;
                }
                taskCache.unlockTask(taskAwardEvent.getTaskGroupId(), userId);
            } catch (Exception e) {
                GAlerter.lan(this.getClass().getName() + " unlock faild.e: ", e);
            }
        }

    }


    private List<Task> getTaskListByAction(TaskAction action, String appKey, AppPlatform appPlatform) throws DbException {
        List<Task> taskList = taskCache.getTaskListByAction(action, appKey, appPlatform);

        if (CollectionUtil.isEmpty(taskList)) {
            taskList = readonlyHandlersPool.getHandler().queryTask(new QueryExpress()
                            .add(QueryCriterions.eq(TaskField.TASK_ACTION, action.getCode()))
                            .add(QueryCriterions.eq(TaskField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                            .add(QueryCriterions.eq(TaskField.APPKEY, appKey))
                            .add(QueryCriterions.eq(TaskField.PLATFORM, appPlatform.getCode()))
            );

            if (!CollectionUtil.isEmpty(taskList)) {
                taskCache.putTaskListByAction(action, appKey, appPlatform, taskList);
            }
        }

        return taskList;
    }


    private String getIdBytaskVerifyId(TaskAwardEvent event, int taskVerifyId) {
        return taskVerifyId == 0 ? event.getProfileId() : event.getClientId();
    }


    private int getSignFlag(String profileId, String groupId) {
        String times = taskRedisManager.getFLag(profileId, groupId);

        int t = -1;
        if (!StringUtil.isEmpty(times)) {
            try {
                t = Integer.parseInt(times);
            } catch (NumberFormatException e) {
            }
        }
        return t;
    }

    @Override
    public List<Task> getTaskGroupList(String taskGroupId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" call handler getTaskGroupList: taskGroupId:" + taskGroupId);
        }

        List<Task> taskList = taskCache.getTaskGroupList(taskGroupId);

        if (CollectionUtil.isEmpty(taskList)) {
            taskList = readonlyHandlersPool.getHandler().queryTask(new QueryExpress().add(QueryCriterions.eq(TaskField.TASK_GROUP_ID, taskGroupId))
                    .add(QueryCriterions.eq(TaskField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                    .add(QuerySort.add(TaskField.DISPLAY_ORDER, QuerySortOrder.ASC)));

            if (!CollectionUtil.isEmpty(taskList)) {
                taskCache.putTaskGroupList(taskGroupId, taskList);
            }
        }
        return taskList;
    }

    private void sendAward(Task task, TaskAward taskAward, String profileId, String appkey, Date date, String uno) {
        TaskAwardStrategy strategy = new TaskAwardPointStrategy();
        taskCache.addTaskAwardCounter(date);
        strategy.award(task.getTaskId(), taskAward, profileId, appkey, date, uno);
    }
}
