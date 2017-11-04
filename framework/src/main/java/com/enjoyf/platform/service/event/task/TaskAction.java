package com.enjoyf.platform.service.event.task;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/2/28
 * Description:
 */
public class TaskAction implements Serializable {
    private static Map<Integer, TaskAction> map = new LinkedHashMap<Integer, TaskAction>();

    public static final TaskAction SIGN = new TaskAction(1,TaskGroupType.SIGN);//签到
    public static final TaskAction LIKE_ARTICLE = new TaskAction(2,TaskGroupType.COMMON);//点赞文章
    public static final TaskAction LIKE_PROFILE = new TaskAction(3,TaskGroupType.COMMON);//关注迷友
    public static final TaskAction LIKE_GAME = new TaskAction(4,TaskGroupType.COMMON);//喜欢游戏
    public static final TaskAction LIKE_TOPIC = new TaskAction(5,TaskGroupType.COMMON);//关注话题

    public static final TaskAction VIEW_TEST = new TaskAction(6,TaskGroupType.COMMON);//阅读开测
    public static final TaskAction VIEW_HOTGAME = new TaskAction(7,TaskGroupType.COMMON);//查看游戏封面或海报
    public static final TaskAction VIEW_ARTICLE = new TaskAction(8,TaskGroupType.COMMON);//阅读文章


    public static final TaskAction SHARE_ARTICLE = new TaskAction(9,TaskGroupType.COMMON);//分享文章
    public static final TaskAction SHARE_HOTGAME = new TaskAction(10,TaskGroupType.COMMON);//分享热门游戏
    public static final TaskAction SHARE_RANKING = new TaskAction(11,TaskGroupType.COMMON);//分享排行榜

    public static final TaskAction UPLOAD_IMAGE = new TaskAction(12,TaskGroupType.COMMON);//上传图片
    public static final TaskAction VIEW_MIYOU = new TaskAction(13,TaskGroupType.COMMON);//查看迷友


  //  public static final TaskAction NEWS_ACTIVITY_DOWNLOAD = new TaskAction(14);//新手下载APP活动
  //  public static final TaskAction NEWS_ACTIVITY_SHARE = new TaskAction(15);//新手分享APP分享活动

    private int code = 0;
    private TaskGroupType taskGroupType=TaskGroupType.COMMON;

    public TaskAction(int c,TaskGroupType taskGroupType) {
        this.code = c;
        this.taskGroupType=taskGroupType;
        map.put(code, this);
    }

    public Integer getCode() {
        return code;
    }


    public TaskGroupType getTaskGroupType() {
        return taskGroupType;
    }


    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "TaskAction: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TaskAction)) {
            return false;
        }

        return code == (((TaskAction) obj).getCode());
    }

    public static TaskAction getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TaskAction> getAll() {
        return map.values();
    }
}
