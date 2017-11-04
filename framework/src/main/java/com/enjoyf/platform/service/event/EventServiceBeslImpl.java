/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.event;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.service.event.pageview.PageViewLocation;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConfig;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.collection.QueueList;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class EventServiceBeslImpl implements EventService {
    private ReqProcessor reqProcessor = null;
    private QueueThreadN eventQueueThreadN = null;

    public EventServiceBeslImpl(ServiceConfig scfg) {
        if (scfg == null) {
            throw new RuntimeException("EventServiceBeslImpl.ctor: ServiceConfig is null!");
        }

        reqProcessor = scfg.getReqProcessor();

        eventQueueThreadN = new QueueThreadN(64, new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    queueProcess((Event) obj);
                } else {
                    GAlerter.lab("In eventQueueThreadN, there is an unknown obj.");
                }
            }
        }, new QueueList());
    }

    private boolean queueProcess(Event event) {
        try {
            reportEvent(event, false);
        } catch (Exception e) {
            //
            GAlerter.lab("EventServiceBeslImpl queueProcess error.", e);
        }

        return true;
    }

    // recieve the player event
    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        eventQueueThreadN.add(event);

        return true;
    }

    //
    @Override
    public boolean reportUserEvent(UserEvent event, boolean immediate) throws ServiceException {
        if (immediate) {
            return reportEvent(event, immediate);
        } else {
            eventQueueThreadN.add(event);

            return true;
        }
    }

    private boolean reportEvent(Event event, boolean immediate) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(event);
        wPacket.writeBooleanNx(immediate);

        Request req = new Request(EventConstants.EVENT_WRITE, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readBooleanNx();
    }

    @Override
    public boolean reportPageViewEvent(PageViewEvent event) throws ServiceException {
        eventQueueThreadN.add(event);

        return true;
    }

    @Override
    public PageViewLocation getPageViewLocationById(Integer locationId) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(locationId);

        Request req = new Request(EventConstants.PAGEVIEW_LOCATION_GET, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (PageViewLocation) rPacket.readSerializable();
    }

    @Override
    public List<PageViewLocation> queryAllPageViewLocations() throws ServiceException {
        WPacket wPacket = new WPacket();

        Request req = new Request(EventConstants.PAGEVIEW_LOCATION_QUERY, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return (List<PageViewLocation>) rPacket.readSerializable();
    }

    @Override
    public long statsPageView(Date from, Date to, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(from);
        wPacket.writeSerializable(to);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(EventConstants.PAGEVIEW_STATS_PAGEVIEW, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readLongNx();
    }

    @Override
    public long statsUniqueUsers(Date from, Date to, QueryExpress queryExpress) throws ServiceException {
        WPacket wPacket = new WPacket();

        wPacket.writeSerializable(from);
        wPacket.writeSerializable(to);
        wPacket.writeSerializable(queryExpress);

        Request req = new Request(EventConstants.PAGEVIEW_STATS_UNIQUEUSER, wPacket);
        RPacket rPacket = reqProcessor.process(req);

        return rPacket.readLongNx();
    }


    @Override
    public Task createTask(Task task) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(task);
        Request req = new Request(EventConstants.CREATE_TASK, wp);
        RPacket rp = reqProcessor.process(req);
        return (Task) rp.readSerializable();
    }

    @Override
    public Task getTask(String taskId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(taskId);
        Request req = new Request(EventConstants.GET_TASK, wp);
        RPacket rp = reqProcessor.process(req);
        return (Task) rp.readSerializable();
    }

    @Override
    public List<Task> queryTask(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(EventConstants.QUERY_TASK, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<Task>) rp.readSerializable();
    }

    @Override
    public PageRows<Task> queryTaskByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(EventConstants.QUERY_TASK_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<Task>) rp.readSerializable();
    }

    @Override
    public boolean modifyTask(UpdateExpress updateExpress, String taskId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(taskId);
        Request req = new Request(EventConstants.MODIFY_TASK, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public TaskLog createTaskLog(TaskLog taskLog) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(taskLog);
        Request req = new Request(EventConstants.CREATE_TASK_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (TaskLog) rp.readSerializable();
    }

    @Override
    public TaskLog getTaskLog(String taskLogId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(taskLogId);
        Request req = new Request(EventConstants.GET_TASK_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (TaskLog) rp.readSerializable();

    }

    @Override
    public List<TaskLog> queryTaskLog(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(EventConstants.QUERY_TASK_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<TaskLog>) rp.readSerializable();
    }

    @Override
    public PageRows<TaskLog> queryTaskLogByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        wp.writeSerializable(pagination);
        Request req = new Request(EventConstants.QUERY_TASK_LOG_BY_PAGE, wp);
        RPacket rp = reqProcessor.process(req);
        return (PageRows<TaskLog>) rp.readSerializable();
    }

    @Override
    public boolean modifyTaskLog(UpdateExpress updateExpress, String taskLogId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(taskLogId);
        Request req = new Request(EventConstants.MODIFY_TASK_LOG, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }


    @Override
    public TaskGroup createTaskGroup(TaskGroup taskGroup) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(taskGroup);
        Request req = new Request(EventConstants.CREATE_TASK_GROUP, wp);
        RPacket rp = reqProcessor.process(req);
        return (TaskGroup) rp.readSerializable();
    }

    @Override
    public TaskGroup getTaskGroup(String taskGroupId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(taskGroupId);
        Request req = new Request(EventConstants.GET_TASK_GROUP, wp);
        RPacket rp = reqProcessor.process(req);
        return (TaskGroup) rp.readSerializable();
    }

    @Override
    public List<TaskGroup> queryTaskGroup(QueryExpress queryExpress) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(queryExpress);
        Request req = new Request(EventConstants.QUERY_TASK_GROUP, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<TaskGroup>) rp.readSerializable();
    }

    @Override
    public boolean modifyTaskGroup(UpdateExpress updateExpress, String groupId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(updateExpress);
        wp.writeStringUTF(groupId);
        Request req = new Request(EventConstants.MODIFY_TASK_GROUP, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readBooleanNx();
    }

    @Override
    public Map<String, List<Task>> queryTaskByGroupIds(String appKey, AppPlatform appPlatform,TaskGroupType taskGroupType) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(appKey);
        wp.writeSerializable(appPlatform);
        wp.writeSerializable(taskGroupType);
        Request req = new Request(EventConstants.QUERY_TASK_BY_GROUPIDS, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<String, List<Task>>) rp.readSerializable();
    }

    @Override
    public TaskLog getTaskLogByGroupIdProfileId(String groupId, String profileId, Date taskDate) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(groupId);
        wp.writeStringUTF(profileId);
        wp.writeSerializable(taskDate);
        Request req = new Request(EventConstants.GET_TASKLOG_BY_GROUPID_PROFILEID, wp);
        RPacket rp = reqProcessor.process(req);
        return (TaskLog) rp.readSerializable();
    }

    @Override
    public List<Task> queryTaskByGroupIdProfileId(String groupId, String profileId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(groupId);
        wp.writeStringUTF(profileId);
        Request req = new Request(EventConstants.QUERY_TASK_BY_GROUPID_PROFILEID, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<Task>) rp.readSerializable();
    }

    @Override
    public int querySignSumByProfileIdGroupId(String profileId,String groupId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(groupId);
        Request req = new Request(EventConstants.QUERY_TASKLOG_SIGN_SUM_BY_PROFILEID_GROUPID, wp);
        RPacket rp = reqProcessor.process(req);
        return rp.readIntNx();
    }


    @Override
    public TaskGroupShowType queryCompleteGroupByProfileIdGroupId(String profileId,String groupId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(groupId);
        Request req = new Request(EventConstants.QUERY_COMPLETE_GROUP_BY_PROFILEID_GROUPID, wp);
        RPacket rp = reqProcessor.process(req);
        return (TaskGroupShowType) rp.readSerializable();
    }

    @Override
    public TaskGroupShowType setCompleteGroupByProfileIdGroupId(String profileId,String groupId,TaskGroupShowType showType) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(groupId);
        wp.writeSerializable(showType);
        Request req = new Request(EventConstants.SET_COMPLETE_GROUP_BY_PROFILEID_GROUPID, wp);
        RPacket rp = reqProcessor.process(req);
        return (TaskGroupShowType)rp.readSerializable();
    }

    @Override
    public Map<String, TaskLog> checkCompleteTask(String profileId, String appKey, AppPlatform appPlatform, Date taskDate) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(appKey);
        wp.writeSerializable(appPlatform);
        wp.writeSerializable(taskDate);
        Request req = new Request(EventConstants.CHECK_COMPLETE_TASK, wp);
        RPacket rp = reqProcessor.process(req);
        return (Map<String, TaskLog>) rp.readSerializable();
    }

    @Override
    public TaskAward getAward(String profileId, String taskId, Date awardDate, String appkey, String uno, String profileKey) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(profileId);
        wp.writeStringUTF(taskId);
        wp.writeSerializable(awardDate);
        wp.writeStringUTF(appkey);
        wp.writeStringUTF(uno);
        wp.writeStringUTF(profileKey);
        Request req = new Request(EventConstants.GET_TASK_AWARD, wp);
        RPacket rp = reqProcessor.process(req);
        return (TaskAward) rp.readSerializable();
    }

    @Override
    public List<Task> getTaskGroupList(String groupId) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeStringUTF(groupId);
        Request req = new Request(EventConstants.QUERY_TASK_LIST_BY_GROUPID, wp);
        RPacket rp = reqProcessor.process(req);
        return (List<Task>) rp.readSerializable();
    }
}
