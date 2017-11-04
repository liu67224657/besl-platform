package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午7:04
 * To change this template use File | Settings | File Templates.
 */
public class PushMessageOptions implements Serializable {

    private int template;

    private List<PushMessageOption> list = new ArrayList<PushMessageOption>();


    public static PushMessageOptions parse(String jsonStr) {

        PushMessageOptions options = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {


            try {
                options = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<PushMessageOptions>() {
                });
            } catch (IOException e) {
                GAlerter.lab("PushMessageOptions parse error, jsonStr:" + jsonStr, e);
            }
        }

        return options;
    }


    public int getTemplate() {
        return template;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public List<PushMessageOption> getList() {
        return list;
    }

    public void setList(List<PushMessageOption> list) {
        this.list = list;
    }

    /**
     * to json
     */
    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
