package com.enjoyf.platform.service.point;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.content.MobileExclusive;
import com.enjoyf.platform.text.TextJsonItem;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import net.sf.json.JSONObject;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-5-4
 * Time: 下午4:47
 * To change this template use File | Settings | File Templates.
 */
public class SecKill implements Serializable {
    private String title;//标题
    private String desc; //描述

    private String supscript; //角标
    private String color;     //角标颜色
    private String price;//

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSupscript() {
        return supscript;
    }

    public void setSupscript(String supscript) {
        this.supscript = supscript;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String toJson() {
        return JsonBinder.buildNonNullBinder().toJson(this);
    }

    public static SecKill fromJson(String jsonString) {
        if (StringUtil.isEmpty(jsonString)) {
            return null;
        }
        SecKill resultList = null;
        try {
            JSONObject jsonObject = JSONObject.fromObject(jsonString);
            if(jsonObject != null && !jsonObject.isNullObject()){
                resultList = new SecKill();
                if(jsonObject.containsKey("title")){
                    resultList.setTitle(jsonObject.getString("title"));
                }
                if(jsonObject.containsKey("desc")){
                    resultList.setTitle(jsonObject.getString("desc"));
                }
                if(jsonObject.containsKey("supscript")){
                    resultList.setTitle(jsonObject.getString("supscript"));
                }
                if(jsonObject.containsKey("color")){
                    resultList.setTitle(jsonObject.getString("color"));
                }
                if(jsonObject.containsKey("price")){
                    resultList.setTitle(jsonObject.getString("price"));
                }
            }
        } catch (Exception e) {
            GAlerter.lab("SecKill occur Exception.e", e);
        }
        return resultList;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
