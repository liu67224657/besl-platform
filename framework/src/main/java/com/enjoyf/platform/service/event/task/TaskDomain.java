package com.enjoyf.platform.service.event.task;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:21
 */
public class TaskDomain implements Serializable {
    private static Map<Integer, TaskDomain> map = new HashMap<Integer, TaskDomain>();

    //签到
    public static final TaskDomain SIGN = new TaskDomain(1);
    //每日任务
    public static final TaskDomain DAILY = new TaskDomain(2);
    //新手任务
    public static final TaskDomain NOVICE = new TaskDomain(3);

    private int code = 0;

    public TaskDomain(int c) {
        this.code = c;
        map.put(code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "TaskDomain: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TaskDomain)) {
            return false;
        }

        return code == (((TaskDomain) obj).getCode());
    }

    public static TaskDomain getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TaskDomain> getAll() {
        return map.values();
    }
}
