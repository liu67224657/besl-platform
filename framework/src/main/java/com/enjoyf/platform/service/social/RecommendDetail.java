package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-8-29
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
public class RecommendDetail implements Serializable {
    private String uno;
    private List<String> destUnos = new ArrayList<String>();
    private String desc;
    private String recommendReason;
    private String verifyType;

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    public List<String> getDestUnos() {
        return destUnos;
    }

    public void setDestUnos(List<String> destUnos) {
        this.destUnos = destUnos;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }
}
