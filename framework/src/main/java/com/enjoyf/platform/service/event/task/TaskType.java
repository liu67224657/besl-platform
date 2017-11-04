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
public class TaskType implements Serializable {
    private static Map<Integer, TaskType> map = new HashMap<Integer, TaskType>();
    //一天一次
    public static final TaskType ONCE_BY_DAY = new TaskType(1);
    //永久一次
    public static final TaskType ONCE = new TaskType(2);
    //不限次数
    public static final TaskType NO_LIMIT = new TaskType(3);

    private int code;

    public TaskType(int c) {
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
        return "TaskTimesType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TaskType)) {
            return false;
        }

        return code == (((TaskType) obj).getCode());
    }

    public static TaskType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TaskType> getAll() {
        return map.values();
    }
}
