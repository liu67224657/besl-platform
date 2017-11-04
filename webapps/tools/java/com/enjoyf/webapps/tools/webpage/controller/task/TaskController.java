package com.enjoyf.webapps.tools.webpage.controller.task;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppRedirectType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.json.JsonUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.MapMessage;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-4-19
 * Time: 下午8:33
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/task")
public class TaskController extends AbstracToolsTaskController {
    private static Set<AppRedirectType> appRedirectTypes = new HashSet<AppRedirectType>();

    static {
        appRedirectTypes.add(AppRedirectType.NOTHING);
        appRedirectTypes.add(AppRedirectType.OPEN_APP);
        appRedirectTypes.add(AppRedirectType.DEFAULT_WEBVIEW);
        appRedirectTypes.add(AppRedirectType.DEFAULT_NOTICE);
        appRedirectTypes.add(AppRedirectType.REDIRECT_DOWNLOAD);
        appRedirectTypes.add(AppRedirectType.OPEN_LOGIN_DIALOG);
        appRedirectTypes.add(AppRedirectType.CLICK_LIKEGAME);
        appRedirectTypes.add(AppRedirectType.REDIRECT_PROFILE);
        appRedirectTypes.add(AppRedirectType.REDIRECT_WEBVIEW);
        appRedirectTypes.add(AppRedirectType.REDIRECT_GAMEDETAIL);
        appRedirectTypes.add(AppRedirectType.REDIRECT_MIYOU);
        appRedirectTypes.add(AppRedirectType.REDIRECT_HOTGAME);
        appRedirectTypes.add(AppRedirectType.REDIRECT_PAIHANGBANG);
        appRedirectTypes.add(AppRedirectType.REDIRECT_GIFTMARKET);
        appRedirectTypes.add(AppRedirectType.REDIRECT_TAGARTICLE);
        appRedirectTypes.add(AppRedirectType.REDIRECT_TAGARTICLE_OPEN);

    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize,
                             @RequestParam(value = "groupid", required = true) String groupId,
                             @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                             @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                             @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                             @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("groupid", groupId);
        mapMessage.put("dateFilter", dateFilter);
        mapMessage.put("dateEndFilter", dateEndFilter);
        mapMessage.put("typeFilter", typeFilter);
        mapMessage.put("allFilter", allFilter);

        if (!StringUtil.isEmpty(errorMsg)) {
            mapMessage = putErrorMessage(mapMessage, errorMsg);
            return new ModelAndView("/task/task-list", mapMessage);
        }

        int curPage = (pageStartIndex / pageSize) + 1;
        Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
        try {
            TaskGroup taskGroup = EventServiceSngl.get().getTaskGroup(groupId);
            if (taskGroup == null) {
                mapMessage = putErrorMessage(mapMessage, "teskgroup.not.exists");
                return new ModelAndView("/task/task-list", mapMessage);
            }

            QueryExpress queryExpress = new QueryExpress();
            //仅签到任务 不可以按日期过滤
            if (taskGroup.getType().equals(TaskGroupType.COMMON) && !allFilter.equals("on") && !StringUtil.isEmpty(dateFilter) && !StringUtil.isEmpty(dateEndFilter)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date beginDate = DateUtil.ignoreTime(sdf.parse(dateFilter));
                Date endDate = DateUtil.ignoreTime(DateUtil.adjustDate(sdf.parse(dateEndFilter), Calendar.DAY_OF_MONTH, 1));

                //所有在两个日期的闭区间内有效的任务                 开始时间在两个参数之间  [ ) 左闭右开
                queryExpress.add(QueryCriterions.geq(TaskField.BEGIN_TIME, beginDate));
                queryExpress.add(QueryCriterions.lt(TaskField.BEGIN_TIME, endDate));
            }
            if (taskGroup.getType().equals(TaskGroupType.COMMON) && !typeFilter.equals("")) {
                queryExpress.add(QueryCriterions.eq(TaskField.REMOVE_STATUS, ActStatus.getByCode(typeFilter).getCode()));
            }

            queryExpress.add(QueryCriterions.eq(TaskField.TASK_GROUP_ID, groupId));
            queryExpress.add(QuerySort.add(TaskField.DISPLAY_ORDER, QuerySortOrder.ASC));
            PageRows<Task> pageRows = EventServiceSngl.get().queryTaskByPage(queryExpress, pagination);
            mapMessage.put("taskgroup", taskGroup);
            mapMessage.put("list", pageRows.getRows());
            mapMessage.put("page", pageRows.getPage());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/task/task-list", mapMessage);
        }
        return new ModelAndView("/task/task-list", mapMessage);
    }

    @RequestMapping(value = "/createpage")
    public ModelAndView createPage(@RequestParam(value = "groupid", required = true) String groupId,
                                   @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                                   @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                                   @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                                   @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("groupid", groupId);
        mapMessage.put("dateFilter", dateFilter);
        mapMessage.put("dateEndFilter", dateEndFilter);
        mapMessage.put("allFilter", allFilter);
        mapMessage.put("typeFilter", typeFilter);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        mapMessage.put("beginTime", df.format(new Date()));
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        mapMessage.put("endTime", df2.format(new Date()));
        try {
            TaskGroup taskGroup = EventServiceSngl.get().getTaskGroup(groupId);
            mapMessage.put("taskgroup", taskGroup);
            mapMessage.put("tasktypes", TaskType.getAll());
            mapMessage.put("awardtypes", TaskAwardType.getAll());
            mapMessage.put("redirectypes", appRedirectTypes);

            mapMessage.put("actions", TaskAction.getAll());

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/task/task-create", mapMessage);
        }
        return new ModelAndView("/task/task-create", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "taskid", required = false) String taskId,
                               @RequestParam(value = "action", required = false) Integer action,
                               @RequestParam(value = "beginhour", required = false, defaultValue = "0") Integer beginHour,
                               @RequestParam(value = "beginTime", required = false) String beginTime,
                               @RequestParam(value = "endTime", required = false) String endTime,
                               @RequestParam(value = "beginDate", required = false) String beginDate,
                               @RequestParam(value = "endDate", required = false) String endDate,
                               @RequestParam(value = "groupid", required = true) String groupId,
                               @RequestParam(value = "directid", required = false) String directId,
                               @RequestParam(value = "tasktype", required = false) Integer taskType,
                               @RequestParam(value = "overtimes", required = false) Integer overTimes,
                               @RequestParam(value = "taskname", required = false) String taskName,
                               @RequestParam(value = "taskdesc", required = false) String taskDesc,
                               @RequestParam(value = "taskpic", required = false) String taskPic,
                               @RequestParam(value = "awardtype", required = false) Integer awardType,
                               @RequestParam(value = "awardvalue", required = false) String awardValue,
                               @RequestParam(value = "redirecttype", required = false) Integer redirecttype,
                               @RequestParam(value = "redirecturi", required = false) String redirecturi,
                               @RequestParam(value = "autosendaward", required = false, defaultValue = "false") Boolean autoSendAward,
                               @RequestParam(value = "taskverityid", required = false) Integer taskVerityId,
                               @RequestParam(value = "taskdisplaytype", required = false) Integer taskDisplayType,
                               @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                               @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                               @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                               @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter,
                               @RequestParam(value = "taskplatform", required = false) String taskPlatformStr) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            Set<TaskGroup> groupSet = new HashSet<TaskGroup>();

            TaskGroup thisGroup = EventServiceSngl.get().getTaskGroup(groupId);
            //没有选择其他的平台
            if(taskPlatformStr.equals(String.valueOf(thisGroup.getAppPlatform().getCode()))){
            }else {
                String anotherPlatform = taskPlatformStr;
                if(taskPlatformStr.indexOf(",") > 0){
                    String[] platformArr = taskPlatformStr.split(",");
                    for(String plat:platformArr){
                        if(!plat.equals(String.valueOf(thisGroup.getAppPlatform().getCode()))){
                            anotherPlatform = plat;
                        }else {
                            groupSet.add(thisGroup);
                        }
                    }
                }else {
                    if(anotherPlatform.equals(String.valueOf(thisGroup.getAppPlatform().getCode()))){
                        groupSet.add(thisGroup);
                    }
                }

                //注意，这里是通过任务组的名称查询，两个平台的任务组名称要相同
                List<TaskGroup> otherGroupList = EventServiceSngl.get().queryTaskGroup(new QueryExpress()
                        .add(QueryCriterions.eq(TaskGroupField.TASK_GROUP_NAME, thisGroup.getTaskGroupName()))
                        .add(QueryCriterions.ne(TaskGroupField.TASK_GROUP_ID, groupId))
                        .add(QueryCriterions.eq(TaskGroupField.APPKEY, thisGroup.getAppKey()))
                        .add(QueryCriterions.eq(TaskGroupField.PLATFORM, Integer.valueOf(anotherPlatform)))
                        .add(QueryCriterions.ne(TaskGroupField.REMOVE_STATUS, ActStatus.ACTED.getCode())));
                if(!CollectionUtil.isEmpty(otherGroupList)){
                    for(TaskGroup anotherGroup:otherGroupList){
                        groupSet.add(anotherGroup);
                    }
                }
            }

            for(TaskGroup taskGroup:groupSet){
                //是一般任务，并且是一天一次时，才可以一次添加多个
                if (taskGroup.getType().equals(TaskGroupType.COMMON) && taskType == 1) {

                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar beginDateCalendar = Calendar.getInstance();
                    beginDateCalendar.setTime(sdfDate.parse(beginDate));

                    Calendar endDateCalendar = Calendar.getInstance();
                    endDateCalendar.setTime(sdfDate.parse(endDate));


                    SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar beginTimeCalendar = Calendar.getInstance();
                    beginTimeCalendar.setTime(sdfTime.parse(beginTime));

                    Calendar endTimeCalendar = Calendar.getInstance();
                    endTimeCalendar.setTime(sdfTime.parse(endTime));

                    int displayOrderStepValue = 3;

                    String taskIdTemp = "";
                    boolean repeatFlag = false;  //判断批量添加后是否会造成重复的taskId todo 什么逻辑
                    for (; beginDateCalendar.compareTo(endDateCalendar) <= 0; displayOrderStepValue += 2) {
                        taskIdTemp = TaskUtil.getTaskId(taskGroup.getTaskGroupId(), taskId + "" + beginDateCalendar.get(Calendar.YEAR) % 100 + (beginDateCalendar.get(Calendar.MONTH) + 1) + beginDateCalendar.get(Calendar.DAY_OF_MONTH));
                        if (EventServiceSngl.get().getTask(taskIdTemp) != null) {
                            repeatFlag = true;
                            break;
                        }
                        //循环一次后增加相应参数,在开始日期和结束日期之间，每个任务的开始时间和结束时间都加上一天
                        beginDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    //如果taskId会重复返回页面，让用户重新添写
                    if (repeatFlag) {
                        mapMessage.put("groupid", groupId);
                        mapMessage.put("dateFilter", dateFilter);
                        mapMessage.put("dateEndFilter", dateEndFilter);
                        mapMessage.put("allFilter", allFilter);
                        mapMessage.put("typeFilter", typeFilter);

                        mapMessage.put("taskgroup", thisGroup);
                        mapMessage.put("tasktypes", TaskType.getAll());
                        mapMessage.put("awardtypes", TaskAwardType.getAll());
                        mapMessage.put("redirectypes", appRedirectTypes);
                        mapMessage.put("actions", TaskAction.getAll());

                        mapMessage.put("taskid", taskId);
                        mapMessage.put("action", action);
                        mapMessage.put("beginTime", beginTime);
                        mapMessage.put("endTime", endTime);
                        mapMessage.put("beginDate", beginDate);
                        mapMessage.put("endDate", endDate);
                        mapMessage.put("directid", directId);

                        mapMessage.put("tasktype", taskType);
                        mapMessage.put("overtimes", overTimes);
                        mapMessage.put("taskname", taskName);
                        mapMessage.put("taskdesc", taskDesc);
                        mapMessage.put("taskpic", taskPic);

                        mapMessage.put("awardtype", awardType);
                        mapMessage.put("awardvalue", awardValue);
                        mapMessage.put("redirecttype", redirecttype);
                        mapMessage.put("redirecturi", redirecturi);
                        mapMessage.put("autosendaward", autoSendAward);

                        mapMessage.put("taskverityid", taskVerityId);
                        mapMessage.put("taskdisplaytype", taskDisplayType);

                        //todo 国际化
                        mapMessage = putErrorMessage(mapMessage, "创建失败,任务id-->" + taskIdTemp + "已经存在，请修改后再提交");
                        return new ModelAndView("/task/task-create", mapMessage);
                    } else {
                        //不会发生重复，重置beginDateCalendar
                        beginDateCalendar.setTime(sdfDate.parse(beginDate));
                    }


                    //按日期拆分，一天添加一条记录
                    for (; beginDateCalendar.compareTo(endDateCalendar) <= 0; displayOrderStepValue += 2) {
                        Task task = new Task();
                        task.setTaskId(TaskUtil.getTaskId(taskGroup.getTaskGroupId(), taskId + "" + beginDateCalendar.get(Calendar.YEAR) % 100 + (beginDateCalendar.get(Calendar.MONTH) + 1) + beginDateCalendar.get(Calendar.DAY_OF_MONTH)));
                        task.setTaskGroupId(taskGroup.getTaskGroupId());
                        task.setPlatform(taskGroup.getAppPlatform());
                        task.setAppKey(taskGroup.getAppKey());
                        task.setDirectId(directId);
                        task.setTaskType(TaskType.getByCode(taskType));
                        task.setOverTimes(overTimes);
                        task.setTaskName(taskName);
                        task.setTaskDesc(taskDesc);
                        task.setTaskPic(taskPic);
                        task.setAutoSendAward(autoSendAward);
                        task.setBeginHour(0);    //用于兼容以前代码
                        //开始时间和结束时间  added by tony 2015-04-23

                        task.setBeginTime(beginTimeCalendar.getTime());
                        task.setEndTime(endTimeCalendar.getTime());

                        task.setTaskAction(TaskAction.getByCode(action));
                        task.setTaskVerifyId(taskVerityId);
                        if (awardType != null) {
                            TaskAwardType taskAwardType = TaskAwardType.getByCode(awardType);
                            if (taskAwardType != null && !StringUtil.isEmpty(awardValue)) {
                                task.setTaskAward(new TaskAward());
                                task.getTaskAward().setValue(awardValue);
                                task.getTaskAward().setType(taskAwardType.getCode());
                            }
                        }
                        task.setOverTimes(overTimes);
                        task.setCreateTime(new Date());
                        task.setCreateUserId(getCurrentUser().getUserid());
                        task.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000) - displayOrderStepValue);
                        //   task.setRemoveStatus(ActStatus.ACTING);
                        task.setRemoveStatus(ActStatus.UNACT); //日常任务 自动启用
                        task.setRedirectType(AppRedirectType.getByCode(redirecttype));
                        task.setRedirectUri(redirecturi);
                        task.setAutoSendAward(autoSendAward);

                        TaskJsonInfo taskJsonInfo = new TaskJsonInfo();
                        taskJsonInfo.setDisplaytype(taskDisplayType);
                        task.setTaskJsonInfo(taskJsonInfo);

                        EventServiceSngl.get().createTask(task);

                        writeToolsLog(LogOperType.CREATE_TASK, "创建任务，taskid:" + task.getTaskId());

                        //循环一次后增加相应参数,在开始日期和结束日期之间，每个任务的开始时间和结束时间都加上一天
                        beginDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        beginTimeCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        endTimeCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    }

                } else {    //一般任务中的永久一次或不限次数  或者签 到任务

                    boolean repeatFlag = false;  //判断批量添加后是否会造成重复的taskId

                    String taskIdTemp = TaskUtil.getTaskId(taskGroup.getTaskGroupId(), taskId);
                    if (EventServiceSngl.get().getTask(taskIdTemp) != null) {
                        repeatFlag = true;
                    }

                    //如果taskId会重复返回页面，让用户重新添写
                    if (repeatFlag) {
                        mapMessage.put("groupid", groupId);
                        mapMessage.put("dateFilter", dateFilter);
                        mapMessage.put("dateEndFilter", dateEndFilter);
                        mapMessage.put("allFilter", allFilter);
                        mapMessage.put("typeFilter", typeFilter);

                        mapMessage.put("taskgroup", thisGroup);
                        mapMessage.put("tasktypes", TaskType.getAll());
                        mapMessage.put("awardtypes", TaskAwardType.getAll());
                        mapMessage.put("redirectypes", appRedirectTypes);
                        mapMessage.put("actions", TaskAction.getAll());

                        mapMessage.put("taskid", taskId);
                        mapMessage.put("action", action);
                        mapMessage.put("beginTime", beginTime);
                        mapMessage.put("endTime", endTime);
                        mapMessage.put("beginDate", beginDate);
                        mapMessage.put("endDate", endDate);
                        mapMessage.put("directid", directId);

                        mapMessage.put("tasktype", taskType);
                        mapMessage.put("overtimes", overTimes);
                        mapMessage.put("taskname", taskName);
                        mapMessage.put("taskdesc", taskDesc);
                        mapMessage.put("taskpic", taskPic);

                        mapMessage.put("awardtype", awardType);
                        mapMessage.put("awardvalue", awardValue);
                        mapMessage.put("redirecttype", redirecttype);
                        mapMessage.put("redirecturi", redirecturi);
                        mapMessage.put("autosendaward", autoSendAward);

                        mapMessage.put("taskverityid", taskVerityId);
                        mapMessage.put("taskdisplaytype", taskDisplayType);

                        mapMessage = putErrorMessage(mapMessage, "创建失败,任务id-->" + taskIdTemp + "已经存在，请修改后再提交");
                        return new ModelAndView("/task/task-create", mapMessage);
                    }

                    Task task = new Task();
                    task.setTaskId(TaskUtil.getTaskId(taskGroup.getTaskGroupId(), taskId));
                    task.setTaskGroupId(taskGroup.getTaskGroupId());
                    task.setPlatform(taskGroup.getAppPlatform());
                    task.setAppKey(taskGroup.getAppKey());
                    task.setDirectId(directId);
                    task.setTaskType(TaskType.getByCode(taskType));
                    task.setOverTimes(overTimes);
                    task.setTaskName(taskName);
                    task.setTaskDesc(taskDesc);
                    task.setTaskPic(taskPic);
                    task.setAutoSendAward(autoSendAward);
                    task.setBeginHour(0);    //用于兼容以前代码

                    //不包括签到任务，其他任务都 有开始时间，结束时间
                    if (taskGroup.getType().equals(TaskGroupType.COMMON)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date beginTimeDate = sdf.parse(beginTime);
                        Date endTimeDate = sdf.parse(endTime);
                        task.setBeginTime(beginTimeDate);
                        task.setEndTime(endTimeDate);
                    }

                    task.setTaskAction(TaskAction.getByCode(action));
                    task.setTaskVerifyId(taskVerityId);
                    if (awardType != null) {
                        TaskAwardType taskAwardType = TaskAwardType.getByCode(awardType);
                        if (taskAwardType != null && !StringUtil.isEmpty(awardValue)) {
                            task.setTaskAward(new TaskAward());
                            task.getTaskAward().setValue(awardValue);
                            task.getTaskAward().setType(taskAwardType.getCode());
                        }
                    }
                    task.setOverTimes(overTimes);
                    task.setCreateTime(new Date());
                    task.setCreateUserId(getCurrentUser().getUserid());
                    task.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
                    task.setRemoveStatus(ActStatus.ACTING);
                    task.setRedirectType(AppRedirectType.getByCode(redirecttype));
                    task.setRedirectUri(redirecturi);
                    task.setAutoSendAward(autoSendAward);

                    TaskJsonInfo taskJsonInfo = new TaskJsonInfo();
                    taskJsonInfo.setDisplaytype(taskDisplayType);
                    task.setTaskJsonInfo(taskJsonInfo);

                    EventServiceSngl.get().createTask(task);

                    writeToolsLog(LogOperType.CREATE_TASK, "创建任务，taskid:" + task.getTaskId());
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/task/list?groupid=" + groupId + "&errorMsg=system.error.or.task.id.duplicated");
        }
        return new ModelAndView("redirect:/task/list?groupid=" + groupId + "&allFilter=" + allFilter + "&dateFilter=" + dateFilter + "&dateEndFilter=" + dateEndFilter + "&typeFilter=" + typeFilter, mapMessage);
    }

    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,      //数据库记录索引
                                   @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize,             // 加入这两个参数使修改后能回到当前页
                                   @RequestParam(value = "taskid", required = false) String taskId,
                                   @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                                   @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                                   @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                                   @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("dateFilter", dateFilter);
        mapMessage.put("dateEndFilter", dateEndFilter);
        mapMessage.put("allFilter", allFilter);
        mapMessage.put("typeFilter", typeFilter);
        TaskGroup taskGroup = null;
        try {
            Task task = EventServiceSngl.get().getTask(taskId);
            taskGroup = EventServiceSngl.get().getTaskGroup(task.getTaskGroupId());
            if (taskGroup == null) {
                return new ModelAndView();
            }

            if (task == null) {
                return new ModelAndView("redirect:/task/list?groupid=" + taskGroup.getTaskGroupId() + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize);
            }
            mapMessage.put("groupid", task.getTaskGroupId());
            mapMessage.put("taskgroup", taskGroup);
            mapMessage.put("tasktypes", TaskType.getAll());
            mapMessage.put("awardtypes", TaskAwardType.getAll());
            mapMessage.put("task", task);
            mapMessage.put("redirectypes", appRedirectTypes);
            mapMessage.put("actions", TaskAction.getAll());


            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            mapMessage.put("page", pagination);

            return new ModelAndView("/task/task-modify", mapMessage);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/list?groupid=" + taskGroup.getTaskGroupId() + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize, mapMessage);
        }

    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize,
                               @RequestParam(value = "groupid", required = true) String groupId,
                               @RequestParam(value = "tasktype", required = false) Integer taskType,
                               @RequestParam(value = "taskname", required = false) String taskName,
                               @RequestParam(value = "taskdesc", required = false) String taskDesc,
                               @RequestParam(value = "awardtype", required = false) Integer awardType,
                               @RequestParam(value = "awardvalue", required = false) String awardValue,
                               @RequestParam(value = "action", required = false) Integer action,
                               @RequestParam(value = "beginhour", required = false, defaultValue = "0") Integer beginHour,
                               @RequestParam(value = "beginTime", required = false) String beginTime,
                               @RequestParam(value = "endTime", required = false) String endTime,
                               @RequestParam(value = "taskid", required = false) String taskId,
                               @RequestParam(value = "directid", required = false) String directId,
                               @RequestParam(value = "overtimes", required = false) Integer overTimes,
                               @RequestParam(value = "redirecttype", required = false) Integer redirecttype,
                               @RequestParam(value = "redirecturi", required = false) String redirecturi,
                               @RequestParam(value = "autosendaward", required = false, defaultValue = "false") Boolean autoSendAward,
                               @RequestParam(value = "taskverityid", required = false) Integer taskVerityId,
                               @RequestParam(value = "taskdisplaytype", required = false) Integer taskDisplayType,
                               @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                               @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                               @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                               @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter,
                               @RequestParam(value = "displayOrder", required = false) Integer displayOrder) {     //如果某个任务以前版本没有display_order,编辑再保存后便会加上这个选项
        TaskType type = TaskType.getByCode(taskType);
        if (type == null) {
            //todo
            return new ModelAndView();
        }

        try {
            TaskGroup taskGroup = EventServiceSngl.get().getTaskGroup(groupId);
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TaskField.TASK_TYPE, type.getCode());
            updateExpress.set(TaskField.TASK_NAME, taskName);
            updateExpress.set(TaskField.TASK_DESC, taskDesc);
            updateExpress.set(TaskField.DIRECT_ID, directId);
            updateExpress.set(TaskField.MODIFY_TIME, new Date());
            updateExpress.set(TaskField.MODIFY_USERID, getCurrentUser().getUserid());
            updateExpress.set(TaskField.REDIRECT_TYPE, AppRedirectType.getByCode(redirecttype) == null ? AppRedirectType.NOTHING : redirecttype);
            updateExpress.set(TaskField.REDIRECT_URI, redirecturi);
            updateExpress.set(TaskField.AUTO_SENDAWARD, autoSendAward);
            updateExpress.set(TaskField.TASK_ACTION, action);
            updateExpress.set(TaskField.TASK_HOUR, 0);      //把数据库中的历史数据都改成0

            //不包括签到任务，其他任务都有 开始时间，结束时间
            if (taskGroup.getType().equals(TaskGroupType.COMMON)) {
                //开始时间和结束时间  added by tony 2015-04-23
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date beginTimeDate = sdf.parse(beginTime);
                Date endTimeDate = sdf.parse(endTime);
                updateExpress.set(TaskField.BEGIN_TIME, beginTimeDate);
                updateExpress.set(TaskField.END_TIME, endTimeDate);
            }

            updateExpress.set(TaskField.TASK_VERIFY_ID, taskVerityId);

            TaskJsonInfo taskJsonInfo = new TaskJsonInfo();
            taskJsonInfo.setDisplaytype(taskDisplayType);
            updateExpress.set(TaskField.TASK_JSONINFO, taskJsonInfo.toJsonStr());

            if (awardType != null) {
                TaskAwardType taskAwardType = TaskAwardType.getByCode(awardType);
                if (taskAwardType != null && !StringUtil.isEmpty(awardValue)) {
                    TaskAward award = new TaskAward();
                    award.setValue(awardValue);
                    award.setType(taskAwardType.getCode());
                    updateExpress.set(TaskField.TASK_AWARD, JsonUtil.beanToJson(award));
                }
            }
            updateExpress.set(TaskField.OVER_TIMES, overTimes);
            //将过去版本数据库条目中display_order 为null的条目赋上一个值。
            if (displayOrder == null || displayOrder == 0) {
                updateExpress.set(TaskGroupField.DISPLAY_ORDER, Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            }


            boolean bool = EventServiceSngl.get().modifyTask(updateExpress, taskId);
            if (bool) {
                writeToolsLog(LogOperType.MODIFY_TASK, "修改任务，taskid:" + taskId);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            Map<String, Object> mapMessage = new HashMap<String, Object>();
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/list?groupid=" + groupId + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize, mapMessage);
        }
        return new ModelAndView("redirect:/task/list?groupid=" + groupId + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&allFilter=" + allFilter + "&dateFilter=" + dateFilter + "&dateEndFilter=" + dateEndFilter + "&typeFilter=" + typeFilter);
    }

    @RequestMapping(value = "/remove")
    public ModelAndView modify(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize,
                               @RequestParam(value = "taskid", required = false) String taskId,
                               @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                               @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                               @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                               @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter) {
        Task task = null;
        try {
            task = EventServiceSngl.get().getTask(taskId);
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TaskField.REMOVE_STATUS, ActStatus.ACTED.getCode());
            updateExpress.set(TaskField.MODIFY_TIME, new Date());
            updateExpress.set(TaskField.MODIFY_USERID, getCurrentUser().getUserid());

            boolean bool = EventServiceSngl.get().modifyTask(updateExpress, taskId);
            if (bool) {
                writeToolsLog(LogOperType.REMOVE_TASK, "删除任务，taskid:" + task.getTaskId());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/task/list?groupid=" + task.getTaskGroupId() + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize);
        }
        return new ModelAndView("redirect:/task/list?groupid=" + task.getTaskGroupId() + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&allFilter=" + allFilter + "&dateFilter=" + dateFilter + "&dateEndFilter=" + dateEndFilter + "&typeFilter=" + typeFilter);
    }

    //acting-->unact      未启用到启用
    @RequestMapping(value = "/publish")
    public ModelAndView publish(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize,
                                @RequestParam(value = "taskid", required = false) String taskId,
                                @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                                @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                                @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                                @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter) {
        Task task = null;
        try {
            task = EventServiceSngl.get().getTask(taskId);
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TaskField.REMOVE_STATUS, ActStatus.UNACT.getCode());
            updateExpress.set(TaskField.MODIFY_TIME, new Date());
            updateExpress.set(TaskField.MODIFY_USERID, getCurrentUser().getUserid());

            boolean bool = EventServiceSngl.get().modifyTask(updateExpress, taskId);
            if (bool) {
                writeToolsLog(LogOperType.PUBLISH_TASK, "发布任务，taskid:" + task.getTaskId());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/task/list?groupid=" + task.getTaskGroupId() + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize);
        }
        return new ModelAndView("redirect:/task/list?groupid=" + task.getTaskGroupId() + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&allFilter=" + allFilter + "&dateFilter=" + dateFilter + "&dateEndFilter=" + dateEndFilter + "&typeFilter=" + typeFilter);
    }

    //acted-->unact     删除到启用
    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,
                                @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize,
                                @RequestParam(value = "taskid", required = false) String taskId,
                                @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                                @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                                @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                                @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter) {
        Task task = null;
        try {
            task = EventServiceSngl.get().getTask(taskId);
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TaskField.REMOVE_STATUS, ActStatus.UNACT.getCode());
            updateExpress.set(TaskField.MODIFY_TIME, new Date());
            updateExpress.set(TaskField.MODIFY_USERID, getCurrentUser().getUserid());

            boolean bool = EventServiceSngl.get().modifyTask(updateExpress, taskId);
            if (bool) {
                writeToolsLog(LogOperType.RECOVER_TASK, "恢复任务，taskid:" + task.getTaskId());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ModelAndView("redirect:/task/list?groupid=" + task.getTaskGroupId() + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize);
        }
        return new ModelAndView("redirect:/task/list?groupid=" + task.getTaskGroupId() + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&allFilter=" + allFilter + "&dateFilter=" + dateFilter + "&dateEndFilter=" + dateEndFilter + "&typeFilter=" + typeFilter);
    }


    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") Integer pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") Integer pageSize,
                             @RequestParam(value = "dateFilter", required = false, defaultValue = "") String dateFilter,
                             @RequestParam(value = "allFilter", required = false, defaultValue = "") String allFilter,
                             @RequestParam(value = "taskid", required = false) String taskId,
                             @RequestParam(value = "dateEndFilter", required = false, defaultValue = "") String dateEndFilter,
                             @RequestParam(value = "typeFilter", required = false, defaultValue = "") String typeFilter) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        String groupId = "";
        try {
            Task task = EventServiceSngl.get().getTask(taskId);
            if (task == null) {
                return new ModelAndView("redirect:/task/list");
            }
            groupId = task.getTaskGroupId();
            TaskGroup taskGroup = EventServiceSngl.get().getTaskGroup(groupId);

            //仅签到任务 不可以按日期过滤
            if (taskGroup.getType().equals(TaskGroupType.COMMON) && !allFilter.equals("on") && !StringUtil.isEmpty(dateFilter) && !StringUtil.isEmpty(dateEndFilter)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date beginDate = DateUtil.ignoreTime(sdf.parse(dateFilter));
                Date endDate = DateUtil.ignoreTime(DateUtil.adjustDate(sdf.parse(dateEndFilter), Calendar.DAY_OF_MONTH, 1));

                //所有在两个日期的闭区间内有效的任务                 开始时间在两个参数之间  [ ) 左闭右开
                queryExpress.add(QueryCriterions.geq(TaskField.BEGIN_TIME, beginDate));
                queryExpress.add(QueryCriterions.lt(TaskField.BEGIN_TIME, endDate));
            }

            if (taskGroup.getType().equals(TaskGroupType.COMMON) && !typeFilter.equals("")) {
                queryExpress.add(QueryCriterions.eq(TaskField.REMOVE_STATUS, ActStatus.getByCode(typeFilter).getCode()));
            }

            queryExpress.add(QueryCriterions.eq(TaskField.TASK_GROUP_ID, groupId));

            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.lt(TaskField.DISPLAY_ORDER, task.getDisplayOrder()));
                queryExpress.add(QuerySort.add(TaskField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.gt(TaskField.DISPLAY_ORDER, task.getDisplayOrder()));
                queryExpress.add(QuerySort.add(TaskField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }

            PageRows<Task> pageRows = EventServiceSngl.get().queryTaskByPage(queryExpress, new Pagination(1, 1, 1));
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                updateExpress1.set(TaskField.DISPLAY_ORDER, task.getDisplayOrder());
                EventServiceSngl.get().modifyTask(updateExpress1, pageRows.getRows().get(0).getTaskId());

                updateExpress2.set(TaskField.DISPLAY_ORDER, pageRows.getRows().get(0).getDisplayOrder());
                EventServiceSngl.get().modifyTask(updateExpress2, task.getTaskId());
                writeToolsLog(LogOperType.SORT_TASK, "任务排序，第一个taskid:" + task.getTaskId() + "第二个taskid:" + pageRows.getRows().get(0).getTaskId());
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/list?groupid=" + groupId + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&allFilter=" + allFilter + "&dateFilter=" + dateFilter + "&dateEndFilter=" + dateEndFilter + "&typeFilter=" + typeFilter, mapMessage);
        }
        return new ModelAndView("redirect:/task/list?groupid=" + groupId + "&pager.offset=" + pageStartIndex + "&maxPageItems=" + pageSize + "&allFilter=" + allFilter + "&dateFilter=" + dateFilter + "&dateEndFilter=" + dateEndFilter + "&typeFilter=" + typeFilter, mapMessage);
    }

}
