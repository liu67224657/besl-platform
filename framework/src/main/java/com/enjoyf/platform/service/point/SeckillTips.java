package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-5-4
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class SeckillTips implements Serializable {
    private String beforeTips;//开始前
    private String inTips; //进行中
    private String afterTips;  //结束后

    public String getBeforeTips() {
        return beforeTips;
    }

    public void setBeforeTips(String beforeTips) {
        this.beforeTips = beforeTips;
    }

    public String getInTips() {
        return inTips;
    }

    public void setInTips(String inTips) {
        this.inTips = inTips;
    }

    public String getAfterTips() {
        return afterTips;
    }

    public void setAfterTips(String afterTips) {
        this.afterTips = afterTips;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static SeckillTips parse(String jsonString) {
        if (StringUtil.isEmpty(jsonString)) {
            return null;
        }
        SeckillTips seckillTips = null;
        try {
            seckillTips = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<SeckillTips>() {
            });
        } catch (IOException e) {
            GAlerter.lab("SeckillTips parse IOException.e", e);
        }
        return seckillTips;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
