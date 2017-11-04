package com.enjoyf.webapps.tools.webpage.controller.task;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/9
 * Description:
 */
@Controller
@RequestMapping(value = "/task/group")
public class TaskGroupController extends AbstracToolsTaskController {

    @RequestMapping(value = "/{appkey}")
    public ModelAndView list(@PathVariable(value = "appkey") String appkey,
                             @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "platform", required = false, defaultValue = "") String platform,
                             @RequestParam(value = "errorMsg", required = false) String errorMsg) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        //默认是ios平台的手游画报
        if (StringUtil.isEmpty(appkey)) {
            appkey = "17yfn24TFexGybOF0PqjdY";
        }
        if (StringUtil.isEmpty(platform)) {
            platform = "0";
        }

        mapMessage.put("appkey", appkey);
        mapMessage.put("platform", platform);

        mapMessage.put("appKeys", getTaskApps());
        mapMessage.put("platforms", getPlatforms());

        if (!StringUtil.isEmpty(errorMsg)) {
            mapMessage = putErrorMessage(mapMessage, errorMsg);
            return new ModelAndView("/task/group-list", mapMessage);
        }

        try {
            List<TaskGroup> list = new ArrayList<TaskGroup>();
            if (!StringUtil.isEmpty(appkey) && !StringUtil.isEmpty(platform)) {
                QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(TaskGroupField.APPKEY, appkey));
                queryExpress.add(QueryCriterions.eq(TaskGroupField.PLATFORM, Integer.valueOf(platform)));
                queryExpress.add(QuerySort.add(TaskGroupField.DISPLAY_ORDER, QuerySortOrder.ASC));
                list = EventServiceSngl.get().queryTaskGroup(queryExpress);
            }

            mapMessage.put("list", list);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("/task/group-list", mapMessage);
        }
        return new ModelAndView("/task/group-list", mapMessage);
    }


    @RequestMapping(value = "/createpage")
    public ModelAndView createpage(@RequestParam(value = "appkey", required = false) String appkey,
                                   @RequestParam(value = "platform", required = false) Integer platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("appkey", appkey);
        mapMessage.put("platform", platform);
        mapMessage.put("types", TaskGroupType.getAll());
        mapMessage.put("appKeys", getTaskApps());
        mapMessage.put("platforms", getPlatforms());
        mapMessage.put("showtypes", TaskGroupShowType.getAll());
       // mapMessage.put("taskCount", TaskGroupShowType.SHOW.getCode());
        mapMessage.put("showtype", TaskGroupShowType.SHOW.getCode());

        return new ModelAndView("/task/group-create", mapMessage);
    }

    @RequestMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "groupid", required = false) String groupid,
                               @RequestParam(value = "appkey", required = false) String appkey,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "platform", required = false) String platform,
                               @RequestParam(value = "count", required = false, defaultValue = "1") String taskCount,
                               @RequestParam(value = "showtype", required = false, defaultValue = "1") Integer showtype) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        TaskGroup taskGroup = new TaskGroup();

        groupid = groupid.trim();
        name = name.trim();

        int platformValue = -1;
        try {
            platformValue = Integer.parseInt(platform);
        } catch (NumberFormatException e) {
        }
        int taskCountValue = -1;
        try {
            taskCountValue = Integer.parseInt(taskCount);
        } catch (NumberFormatException e) {
        }
        try {
            AppPlatform appPlatform = AppPlatform.getByCode(platformValue);
            List<TaskGroup> list = null;

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TaskGroupField.TASK_GROUP_ID, TaskUtil.getTaskGroupId(groupid, appPlatform)));
            list = EventServiceSngl.get().queryTaskGroup(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                mapMessage.put("appkey", appkey);
                mapMessage.put("platform", platform);
                mapMessage.put("appKeys", getTaskApps());
                mapMessage.put("platforms", getPlatforms());
                mapMessage.put("type", type);
                mapMessage.put("types", TaskGroupType.getAll());
                mapMessage.put("groupid", groupid);
                mapMessage.put("name", name);
              //  mapMessage.put("taskCount", taskCount);
                mapMessage.put("showtype", String.valueOf(showtype));
                mapMessage.put("showtypes", TaskGroupShowType.getAll());
                mapMessage = putErrorMessage(mapMessage, "创建失败,该任务组id已经存在，请修改后再提交");
                return new ModelAndView("/task/group-create", mapMessage);
            }

            taskGroup.setTaskGroupId(TaskUtil.getTaskGroupId(groupid, appPlatform));
            taskGroup.setAppKey(appkey);
            taskGroup.setAppPlatform(appPlatform);
            taskGroup.setTaskGroupName(name);
            taskGroup.setType(TaskGroupType.getByCode(type));
          //  taskGroup.setTaskCount(taskCountValue);      //暂时未用到此字段
            taskGroup.setCreateTime(new Date());
            taskGroup.setCreateUserId(getCurrentUser().getUserid());
            taskGroup.setRemoveStatus(ActStatus.ACTING);
            taskGroup.setShowtype(TaskGroupShowType.getByCode(showtype));
            taskGroup.setDisplayOrder(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));

            EventServiceSngl.get().createTaskGroup(taskGroup);
            writeToolsLog(LogOperType.CREATE_TASK_GROUP, "创建任务组，taskgroupid:" + taskGroup.getTaskGroupId());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/group/"+appkey+"?errorMsg=system.error", mapMessage);
        }

        return new ModelAndView("redirect:/task/group/" + appkey + "?platform=" + platform);
    }


    @RequestMapping(value = "/modifypage")
    public ModelAndView modifyPage(@RequestParam(value = "groupid", required = false) String groupid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String appkey = "";
        try {
            TaskGroup taskGroup = EventServiceSngl.get().getTaskGroup(groupid);

            appkey = taskGroup.getAppKey();
            mapMessage.put("taskgroup", taskGroup);
            mapMessage.put("types", TaskGroupType.getAll());
            mapMessage.put("showtypes", TaskGroupShowType.getAll());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/group/"+appkey+"?errorMsg=system.error", mapMessage);
        }

        return new ModelAndView("/task/group-modify", mapMessage);
    }

    @RequestMapping(value = "/modify")
    public ModelAndView modify(@RequestParam(value = "groupid", required = false) String groupid,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "type", required = false) String type,
                               @RequestParam(value = "count", required = false, defaultValue = "1") String count,
                               @RequestParam(value = "displayOrder", required = false) Integer displayOrder,
                               @RequestParam(value = "showtype", required = false, defaultValue = "1") Integer showtype) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        String appkey = "";

        TaskGroup taskGroup = null;
        try {
            taskGroup = EventServiceSngl.get().getTaskGroup(groupid);
            appkey = taskGroup.getAppKey();

            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(TaskGroupField.TASK_GROUP_NAME, name);
            updateExpress.set(TaskGroupField.TYPE, type);
        //    updateExpress.set(TaskGroupField.TASK_COUNT, Integer.valueOf(count));          //暂时不用这个字段
            updateExpress.set(TaskGroupField.SHOW_TYPE, showtype);
            updateExpress.set(TaskGroupField.MODIFY_TIME, new Date());
            updateExpress.set(TaskGroupField.MODIFY_USERID, getCurrentUser().getUserid());

            //将过去版本数据库条目中display_order 为null的条目赋上一个值。
            if (displayOrder == null || displayOrder == 0) {
                updateExpress.set(TaskGroupField.DISPLAY_ORDER, Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000));
            }

            boolean result = EventServiceSngl.get().modifyTaskGroup(updateExpress, taskGroup.getTaskGroupId());
            if (result) {
                writeToolsLog(LogOperType.MODIFY_TASK_GROUP, "修改任务组，taskgroupid:" + taskGroup.getTaskGroupId());
            }
            return new ModelAndView("redirect:/task/group/" + taskGroup.getAppKey() + "?platform=" + taskGroup.getAppPlatform().getCode());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/group/"+taskGroup.getAppKey()+"?errorMsg=system.error&platform=" + taskGroup.getAppPlatform().getCode(), mapMessage);
        }
    }


    //acting-->unact      未启用到启用
    @RequestMapping(value = "/publish")
    public ModelAndView publish(@RequestParam(value = "groupid", required = false) String groupid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        TaskGroup taskGroup = null;
        try {
            taskGroup = EventServiceSngl.get().getTaskGroup(groupid);

            if (taskGroup == null) {
                return new ModelAndView("redirect:/task/group/" + taskGroup.getAppKey() + "?platform=" + taskGroup.getAppPlatform().getCode() + "&errorMsg=taskgroup.notexists");
            }

            boolean result = EventServiceSngl.get().modifyTaskGroup(new UpdateExpress()
                    .set(TaskGroupField.REMOVE_STATUS, ActStatus.UNACT.getCode())
                    .set(TaskGroupField.MODIFY_TIME, new Date())
                    .set(TaskGroupField.MODIFY_USERID, getCurrentUser().getUserid())
                    , taskGroup.getTaskGroupId());

            if (result) {
                writeToolsLog(LogOperType.PUBLISH_TASK_GROUP, "发布任务组，taskgroupid:" + taskGroup.getTaskGroupId());
            }
            return new ModelAndView("redirect:/task/group/" + taskGroup.getAppKey() + "?platform=" + taskGroup.getAppPlatform().getCode());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/group/"+taskGroup.getAppKey()+"?errorMsg=system.error&platform=" + taskGroup.getAppPlatform().getCode(), mapMessage);
        }
    }


    @RequestMapping(value = "/remove")
    public ModelAndView remove(@RequestParam(value = "groupid", required = false) String groupid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        TaskGroup taskGroup = null;
        try {
            taskGroup = EventServiceSngl.get().getTaskGroup(groupid);

            if (taskGroup == null) {
                return new ModelAndView("redirect:/task/group/" + taskGroup.getAppKey() + "?platform=" + taskGroup.getAppPlatform().getCode() + "&errorMsg=taskgroup.notexists");
            }

            boolean result = EventServiceSngl.get().modifyTaskGroup(new UpdateExpress()
                    .set(TaskGroupField.REMOVE_STATUS, ActStatus.ACTED.getCode())
                    .set(TaskGroupField.MODIFY_TIME, new Date())
                    .set(TaskGroupField.MODIFY_USERID, getCurrentUser().getUserid())
                    , taskGroup.getTaskGroupId());

            if (result) {
                writeToolsLog(LogOperType.REMOVE_TASK_GROUP, "删除任务组，taskgroupid:" + taskGroup.getTaskGroupId());
            }
            return new ModelAndView("redirect:/task/group/" + taskGroup.getAppKey() + "?platform=" + taskGroup.getAppPlatform().getCode());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/group/"+taskGroup.getAppKey()+"?errorMsg=system.error&platform=" + taskGroup.getAppPlatform().getCode(), mapMessage);
        }
    }

    @RequestMapping(value = "/recover")
    public ModelAndView recover(@RequestParam(value = "groupid", required = false) String groupid) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();


        TaskGroup taskGroup = null;
        try {
            taskGroup = EventServiceSngl.get().getTaskGroup(groupid);

            boolean result = EventServiceSngl.get().modifyTaskGroup(new UpdateExpress()
                    .set(TaskGroupField.REMOVE_STATUS, ActStatus.UNACT.getCode())
                    .set(TaskGroupField.MODIFY_TIME, new Date())
                    .set(TaskGroupField.MODIFY_USERID, getCurrentUser().getUserid())
                    , taskGroup.getTaskGroupId());

            if (result) {
                writeToolsLog(LogOperType.RECOVER_TASK_GROUP, "恢复任务组，taskgroupid:" + taskGroup.getTaskGroupId());
            }
            return new ModelAndView("redirect:/task/group/" + taskGroup.getAppKey() + "?platform=" + taskGroup.getAppPlatform().getCode());
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/group/"+taskGroup.getAppKey()+"?errorMsg=system.error&platform=" + taskGroup.getAppPlatform().getCode(), mapMessage);
        }
    }

    @RequestMapping(value = "/sort/{sort}")
    public ModelAndView sort(@PathVariable(value = "sort") String sort,
                             @RequestParam(value = "groupid", required = false) String groupid,
                             @RequestParam(value = "appkey", required = false, defaultValue = "") String appkey,
                             @RequestParam(value = "platform", required = false, defaultValue = "") String platform) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        UpdateExpress updateExpress1 = new UpdateExpress();
        UpdateExpress updateExpress2 = new UpdateExpress();
        QueryExpress queryExpress = new QueryExpress();
        try {
            TaskGroup taskGroup = EventServiceSngl.get().getTaskGroup(groupid);
            if (taskGroup == null) {
                mapMessage = putErrorMessage(mapMessage, "任务组id为空");
                return new ModelAndView("redirect:/task/group/"+appkey, mapMessage);
            }
            if (!StringUtil.isEmpty(appkey) && !StringUtil.isEmpty(platform)) {
                queryExpress.add(QueryCriterions.eq(TaskGroupField.APPKEY, appkey));
                queryExpress.add(QueryCriterions.eq(TaskGroupField.PLATFORM, Integer.valueOf(platform)));
            }

            if (sort.equals("up")) {
                queryExpress.add(QueryCriterions.lt(TaskGroupField.DISPLAY_ORDER, taskGroup.getDisplayOrder()));
                queryExpress.add(QuerySort.add(TaskGroupField.DISPLAY_ORDER, QuerySortOrder.DESC));
            } else {
                queryExpress.add(QueryCriterions.gt(TaskGroupField.DISPLAY_ORDER, taskGroup.getDisplayOrder()));
                queryExpress.add(QuerySort.add(TaskGroupField.DISPLAY_ORDER, QuerySortOrder.ASC));
            }

            List<TaskGroup> list = EventServiceSngl.get().queryTaskGroup(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                updateExpress1.set(TaskGroupField.DISPLAY_ORDER, taskGroup.getDisplayOrder());
                EventServiceSngl.get().modifyTaskGroup(updateExpress1, list.get(0).getTaskGroupId());

                updateExpress2.set(TaskGroupField.DISPLAY_ORDER, list.get(0).getDisplayOrder());
                EventServiceSngl.get().modifyTaskGroup(updateExpress2, taskGroup.getTaskGroupId());
                writeToolsLog(LogOperType.SORT_TASK_GROUP, "任务组排序，第一个groupid:" + taskGroup.getTaskGroupId() + "第二个taskid:" + list.get(0).getTaskGroupId());
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
            return new ModelAndView("redirect:/task/group/" + appkey + "?platform=" + platform, mapMessage);
        }
        return new ModelAndView("redirect:/task/group/" + appkey + "?platform=" + platform, mapMessage);
    }

}
