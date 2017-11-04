package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午7:04
 * To change this template use File | Settings | File Templates.
 */
public class PushMessageOptions implements Serializable {
    private String cuno;

    private String cid;

    private String domain;

    public PushMessageOptions() {

    }

    // getter and setter

    public String getCuno() {
        return cuno;
    }

    public void setCuno(String cuno) {
        this.cuno = cuno;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public static PushMessageOptions parse(String jsonStr) {
        PushMessageOptions returnValue = new PushMessageOptions();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<PushMessageOptions>() {
                });

            } catch (IOException e) {
                GAlerter.lab("PushMessageOptions parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    /**
     * to json
     */
    public String toJson() {
        return JsonBinder.buildNonNullBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
