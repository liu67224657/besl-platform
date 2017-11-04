package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.util.json.JsonUtil;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * creator: zhitaoshi
 * modified by tony
 * Date: 14-12-16
 * Time: 上午11:22
 * To change this template use File | Settings | File Templates.
 */
public class TaskAward implements Serializable {


    private int  type = TaskAwardType.POINT.getCode();
    private String value;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TaskAward{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }

}
