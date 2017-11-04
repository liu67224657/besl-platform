package com.enjoyf.webapps.joyme.weblogic.event;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.EventServiceSngl;
import com.enjoyf.platform.service.event.task.Task;
import com.enjoyf.platform.service.event.task.TaskGroup;
import com.enjoyf.platform.service.event.task.TaskGroupShowType;
import com.enjoyf.platform.service.event.task.TaskLog;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.webapps.joyme.dto.task.TaskDTO;
import com.enjoyf.webapps.joyme.dto.task.TaskGroupDTO;
import com.enjoyf.webapps.joyme.dto.task.TaskInfoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/8
 * Description:
 */
@Service(value = "taskWebLogic")
public class TaskWebLogic {

    public List<TaskDTO> getTaskDTO(List<Task> taskList) {
        List<TaskDTO> dtoList = new ArrayList<TaskDTO>();
        for (Task task : taskList) {
            TaskDTO dto = buildTaskDTO(task);
            dtoList.add(dto);
        }

        return dtoList;
    }

    public TaskDTO buildTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setGourpid(task.getTaskGroupId());
        taskDTO.setName(task.getTaskName());
        taskDTO.setOvertimes(task.getOverTimes());
        taskDTO.setTaskAward(task.getTaskAward());
        taskDTO.setTaskid(task.getTaskId());
        taskDTO.setType(task.getTaskType().getCode());
        return taskDTO;
    }

    public TaskInfoDTO getTaskInfo(Map<String, List<Task>> taskMap, Map<String, TaskLog> cTaskMap, Profile profile) throws ServiceException {
        TaskInfoDTO taskInfoDTO = new TaskInfoDTO();

        Date doTaskDate = new Date();
        List<TaskGroupDTO> list = new ArrayList<TaskGroupDTO>();

        for (Map.Entry<String, List<Task>> entry : taskMap.entrySet()) {
            //todo 禁止循环查询
            TaskGroup group = EventServiceSngl.get().getTaskGroup(entry.getKey());
            TaskGroupDTO taskGroupDTO = new TaskGroupDTO();
            taskGroupDTO.setGroupName(group.getTaskGroupName());
            taskGroupDTO.setGroupId(entry.getKey());
            taskGroupDTO.setTaskGroup(group);

            List<Task> taskGroupList = entry.getValue();
            for (int i = 0; i < taskGroupList.size(); i++) {
                Task task = taskGroupList.get(i);
                //根据beginTime 和endTime过滤掉当前已经失效的任务，
                if (task.getBeginTime() == null || task.getEndTime() == null
                        || task.getBeginTime().getTime() > doTaskDate.getTime()
                        || task.getEndTime().getTime() < doTaskDate.getTime()) {
                    taskGroupList.remove(i);
                    i--;
                    continue;
                }
                if (cTaskMap.containsKey(task.getTaskId())) {
                    TaskLog tempLog = cTaskMap.get(task.getTaskId());
                    if (tempLog.getOverStatus().equals(ActStatus.ACTED)) {
                        taskGroupDTO.setCompletedNum(taskGroupDTO.getCompletedNum() + 1);
                    } else if (tempLog.getOverStatus().equals(ActStatus.REJECTED)) {
                        taskGroupDTO.setGetAwardNum(taskGroupDTO.getGetAwardNum() + 1);
                    }
                }

            }
            taskGroupDTO.setTaskList(taskGroupList);
            list.add(taskGroupDTO);
        }

        int taskCount = 0;
        int completeCount = 0;
        int awardCount = 0;
        int allTemp = 0;
//        int completeTemp = 0;
        for (int i = 0; i < list.size(); i++) {
            allTemp = list.get(i).getTaskList().size();
//            completeTemp = list.get(i).getCompletedNum();
            TaskGroupShowType taskGroupShowType = list.get(i).getTaskGroup().getShowtype();

            if (list.get(i).getCompletedNum() == allTemp) {
                //todo 所有查询都循环做操作效率太差 放在完成任务的时候去坐
                EventServiceSngl.get().setCompleteGroupByProfileIdGroupId(profile.getProfileId(), list.get(i).getGroupId(), TaskGroupShowType.SUPER_HIDE);  //10188 mantis
            }

            //只有设置为超级隐藏(即新增也不显示，才有必要保存要redis或tasklog表中）
            if (taskGroupShowType.equals(TaskGroupShowType.HIDE) || taskGroupShowType.equals(TaskGroupShowType.SUPER_HIDE)) {

                //状态为隐藏，并且所有任务已领奖，那么不显示此任务组
                if (taskGroupShowType.equals(TaskGroupShowType.HIDE) && list.get(i).getCompletedNum() == allTemp) {
                    list.remove(i);
                    i--;
                    continue;
                }

                //状态为超级隐藏，并且曾经完成过此任务组的所有任务并领奖，那么不显示此任务组
                if (taskGroupShowType.equals(TaskGroupShowType.SUPER_HIDE)) {
                    //todo
                    TaskGroupShowType savedShowType = EventServiceSngl.get().queryCompleteGroupByProfileIdGroupId(profile.getProfileId(), list.get(i).getGroupId());
                    if (savedShowType.equals(TaskGroupShowType.SUPER_HIDE)) {
                        list.remove(i);
                        i--;
                        continue;
                    }
                }
            }

            taskCount += list.get(i).getTaskList().size();
            completeCount += list.get(i).getCompletedNum();
            awardCount += list.get(i).getGetAwardNum();

        }

        taskInfoDTO.setList(list);
        taskInfoDTO.setTaskCount(taskCount);
        taskInfoDTO.setCompleteCount(completeCount);
        taskInfoDTO.setAwardCount(awardCount);
        taskInfoDTO.setUnComplete(taskCount - completeCount - awardCount);
        return taskInfoDTO;
    }

}
