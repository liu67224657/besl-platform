package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-3
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
public class GameLayoutSetting implements Serializable{
    private String layoutCode;  //暂时没用

    private List<GameViewLayout> viewList; //没一行的View的属性

    public String getLayoutCode() {
        return layoutCode;
    }

    public void setLayoutCode(String layoutCode) {
        this.layoutCode = layoutCode;
    }

    public List<GameViewLayout> getViewList() {
        return viewList;
    }

    public void setViewList(List<GameViewLayout> viewList) {
        this.viewList = viewList;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static GameLayoutSetting parse(String jsonStr) {
        GameLayoutSetting returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {

            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<GameLayoutSetting>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


}
