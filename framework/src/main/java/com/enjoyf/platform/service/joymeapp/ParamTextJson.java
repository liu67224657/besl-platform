package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.gameres.gamedb.RedMessageType;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-2-26
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class ParamTextJson implements Serializable {
    private String createUserId;
    private long createDate;
    private String modifyUserId;
    private long modifyDate;

    private String bgcolor;

    //图片主色调
    private String picrgb;

    //应用大小
    private String size;
    //活动时间
    private String startDate;
    private String endDate;
    //热门
    private boolean isHot;
    //最新
    private boolean isNew;

    //游戏资料
    private String redMessageType = RedMessageType.DEFAULT.getCode() + "";
    private String redMessageText = "";

    public ParamTextJson() {

    }

    public ParamTextJson(String bgcolor, String picrgb, String size, String startDate, String endDate) {
        this.bgcolor = bgcolor;
        this.picrgb = picrgb;
        this.size = size;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ParamTextJson(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public static String toJson(ParamTextJson paramTextJson) {
        return JsonBinder.buildNonDefaultBinder().toJson(paramTextJson);
    }

    public static ParamTextJson fromJson(String jsonString) {
        ParamTextJson resultList = new ParamTextJson();
        try {
            resultList = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<ParamTextJson>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    public String toJson() {
        return JsonBinder.buildNonNullBinder().toJson(this);
    }


    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public String getPicrgb() {
        return picrgb;
    }

    public void setPicrgb(String picrgb) {
        this.picrgb = picrgb;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(boolean hot) {
        isHot = hot;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    public String getRedMessageText() {
        return redMessageText;
    }

    public void setRedMessageText(String redMessageText) {
        this.redMessageText = redMessageText;
    }

    public String getRedMessageType() {
        return redMessageType;
    }

    public void setRedMessageType(String redMessageType) {
        this.redMessageType = redMessageType;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }
}
