package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zhaoxin
 * Date: 11-8-22
 * Time: 下午5:28
 * Desc: 图片实体类
 */
public class ResourceImage implements Serializable {

    private int id;//排序使用
    private String desc;//描述
    private String url;//原图
    private String m;  //中图
    private String s;  //小图
    private String ss; //发现墙图
    private String ll; //
    private String sl; //

    private boolean validStatus = true; //默认可以显示

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public String getLl() {
        return ll;
    }

    public void setLl(String ll) {
        this.ll = ll;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public Boolean getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Boolean validStatus) {
        this.validStatus = validStatus;
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
