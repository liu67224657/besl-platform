package com.enjoyf.platform.service.event.task;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-1
 * Time: 上午11:21
 */
public class TaskGroupShowType implements Serializable {
    private static Map<Integer, TaskGroupShowType> map = new LinkedHashMap<Integer, TaskGroupShowType>();

    //显示此任务组  显示
    public static final TaskGroupShowType SHOW = new TaskGroupShowType(1);
    //  完成此组的所有任务后 不显示
    public static final TaskGroupShowType HIDE = new TaskGroupShowType(2);
    //  完成此组的所有任务后 不显示
    public static final TaskGroupShowType SUPER_HIDE = new TaskGroupShowType(3);

    private int code;


    public TaskGroupShowType(int c) {
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
        if ((obj == null) || !(obj instanceof TaskGroupShowType)) {
            return false;
        }

        return code == (((TaskGroupShowType) obj).getCode());
    }

    public static TaskGroupShowType getByCode(int c) {
        return map.get(c) == null ? TaskGroupShowType.SHOW : map.get(c);
    }

    public static Collection<TaskGroupShowType> getAll() {
        return map.values();
    }
}
