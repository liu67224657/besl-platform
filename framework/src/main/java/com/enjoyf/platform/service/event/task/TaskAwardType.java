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
public class TaskAwardType implements Serializable {
    private static Map<Integer, TaskAwardType> map = new HashMap<Integer, TaskAwardType>();
    //迷豆
    public static final TaskAwardType POINT = new TaskAwardType(0);             // 虚拟货币
  //  public static final TaskAwardType REAL_GOOD = new TaskAwardType(1);      //  实物商品  暂时无用
   // public static final TaskAwardType VIRTUAL_GOOD = new TaskAwardType(2);   //虚拟商品    暂时无用


    private int code;


    public TaskAwardType(int c) {
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
        return "TaskAwardType{" +
                "code=" + code +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TaskAwardType)) {
            return false;
        }

        return code == (((TaskAwardType) obj).getCode());
    }

    public static TaskAwardType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TaskAwardType> getAll() {
        return map.values();
    }
}
