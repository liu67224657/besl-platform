/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.event.pageview.PageViewEvent;
import com.enjoyf.platform.service.event.pageview.PageViewLocation;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public interface EventService extends EventReceiver {
    //report the user event
    public boolean reportUserEvent(UserEvent event, boolean immediate) throws ServiceException;

    //report the pv event.
    public boolean reportPageViewEvent(PageViewEvent event) throws ServiceException;

    //pageview location apis
    public PageViewLocation getPageViewLocationById(Integer locationId) throws ServiceException;

    public List<PageViewLocation> queryAllPageViewLocations() throws ServiceException;

    //the pageview data stats apis.
    public long statsPageView(Date from, Date to, QueryExpress queryExpress) throws ServiceException;

    public long statsUniqueUsers(Date from, Date to, QueryExpress queryExpress) throws ServiceException;


    public Task createTask(Task task) throws ServiceException;

    public Task getTask(String taskId) throws ServiceException;

    public List<Task> queryTask(QueryExpress queryExpress) throws ServiceException;

    public PageRows<Task> queryTaskByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyTask(UpdateExpress updateExpress, String taskId) throws ServiceException;

    public TaskLog createTaskLog(TaskLog taskLog) throws ServiceException;

    public TaskLog getTaskLog(String taskLogId) throws ServiceException;

    public List<TaskLog> queryTaskLog(QueryExpress queryExpress) throws ServiceException;

    public PageRows<TaskLog> queryTaskLogByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException;

    public boolean modifyTaskLog(UpdateExpress updateExpress, String taskLogId) throws ServiceException;

    public TaskGroup createTaskGroup(TaskGroup taskGroup) throws ServiceException;

    public TaskGroup getTaskGroup(String taskGroupId) throws ServiceException;

    public List<TaskGroup> queryTaskGroup(QueryExpress queryExpress) throws ServiceException;

    public boolean modifyTaskGroup(UpdateExpress updateExpress, String groupId) throws ServiceException;

    //返回某个appkey,某个平台下所有的groupid为key, group下的任务list是value,如果为空，list的size为0
    public Map<String, List<Task>> queryTaskByGroupIds(String appKey, AppPlatform appPlatform, TaskGroupType taskGroupType) throws ServiceException;


    // 用于判断特定用户今天的签到任务是否完成
    public TaskLog getTaskLogByGroupIdProfileId(String groupId, String profileId, Date taskDate) throws ServiceException;

    //用于获取签到任务得到是否是今天昨天
    public List<Task> queryTaskByGroupIdProfileId(String groupId, String profileId) throws ServiceException;

    //获取clientid/profileid 在某个组的总签到次数
    public int querySignSumByProfileIdGroupId(String profileId, String groupId) throws ServiceException;

    // 查询某个用户是否完成某个任务组的任务
    public TaskGroupShowType queryCompleteGroupByProfileIdGroupId(String profileId, String groupId) throws ServiceException;

    //设置某个用户是否完成某个任务组的任务
    public TaskGroupShowType setCompleteGroupByProfileIdGroupId(String profileId, String groupId, TaskGroupShowType showType) throws ServiceException;

    // 用于获取用户 在ios/android 上的特定app 上某一天的任务完成情况
    public Map<String, TaskLog> checkCompleteTask(String profileId, String appKey, AppPlatform appPlatform, Date taskDate) throws ServiceException;

    public TaskAward getAward(String profileId, String taskId, Date awardDate, String appkey, String uno, String profileKey) throws ServiceException;

    //通过taskgroupid 获取此组下的所有任务
    public List<Task> getTaskGroupList(String groupId) throws ServiceException;
}
