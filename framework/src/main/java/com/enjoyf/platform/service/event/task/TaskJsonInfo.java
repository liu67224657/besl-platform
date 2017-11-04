package com.enjoyf.platform.service.event.task;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/3/11
 * Description:
 */
public class TaskJsonInfo implements Serializable {
    private int displaytype;//0--无 1--new

    public int getDisplaytype() {
        return displaytype;
    }

    public void setDisplaytype(int displaytype) {
        this.displaytype = displaytype;
    }

    public static TaskJsonInfo parse(String jsonStr) {
        TaskJsonInfo returnValue = new TaskJsonInfo();
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<TaskJsonInfo>() {
                });
            } catch (IOException e) {
                GAlerter.lab("taskjsoninfo error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
