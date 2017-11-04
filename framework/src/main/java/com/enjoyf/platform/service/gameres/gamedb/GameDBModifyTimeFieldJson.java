package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zhimingli
 * Date: 2015/01/16
 * Time: 11:44
 */
public class GameDBModifyTimeFieldJson implements Serializable {
    private long lastModifyTime;          //最新更新的时间，因为它的优先级不一定在今天最高，所以不一定显示
    private long giftlastModifyTime;
    private long redMessageTime = 0l;      //优先级最高的 字的 更新时间
    private String redMessageType = RedMessageType.DEFAULT.getCode() + "";
    private String redMessageText = "";

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public long getGiftlastModifyTime() {
        return giftlastModifyTime;
    }

    public void setGiftlastModifyTime(long giftlastModifyTime) {
        this.giftlastModifyTime = giftlastModifyTime;
    }

    public static GameDBModifyTimeFieldJson parse(String jsonStr) {
        GameDBModifyTimeFieldJson returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<GameDBModifyTimeFieldJson>() {
                });
            } catch (IOException e) {
                GAlerter.lab("PublishParam parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }

    public String getRedMessageType() {
        return redMessageType;
    }

    public void setRedMessageType(String redMessageType) {
        this.redMessageType = redMessageType;
    }

    public String getRedMessageText() {
        return redMessageText;
    }

    public void setRedMessageText(String redMessageText) {
        this.redMessageText = redMessageText;
    }

    public long getRedMessageTime() {
        return redMessageTime;
    }

    public void setRedMessageTime(long redMessageTime) {
        this.redMessageTime = redMessageTime;
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

