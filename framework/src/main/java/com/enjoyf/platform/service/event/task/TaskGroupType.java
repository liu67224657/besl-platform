package com.enjoyf.platform.service.event.task;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by tonydiao on 2015/5/8.
 */
public class TaskGroupType implements Serializable {


    private static Map<String, TaskGroupType> map = new LinkedHashMap<String, TaskGroupType>();
    //普通任务    非连续性任务
    public static final TaskGroupType COMMON = new TaskGroupType("common");
    //签到任务    连续性任务
    public static final TaskGroupType SIGN = new TaskGroupType("sign");

    private String code;

    public TaskGroupType(String code) {
        this.code = code;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }


    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "TaskGroupType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TaskGroupType)) {
            return false;
        }

        return code.equalsIgnoreCase(((TaskGroupType) obj).getCode());
    }

    public static TaskGroupType getByCode(String code) {
        return map.get(code) == null ? TaskGroupType.COMMON : map.get(code);
    }

    public static Collection<TaskGroupType> getAll() {
        return map.values();
    }
}
