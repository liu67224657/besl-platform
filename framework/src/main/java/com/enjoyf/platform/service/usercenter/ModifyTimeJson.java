package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zhimingli on 2015/2/8 0008.
 */
public class ModifyTimeJson implements Serializable {
    private long likedprofileModifyTime;//喜欢我的人更新时间
    private long miyouModifyTime;//我的帖子有更新


    public long getLikedprofileModifyTime() {
        return likedprofileModifyTime;
    }

    public void setLikedprofileModifyTime(long likedprofileModifyTime) {
        this.likedprofileModifyTime = likedprofileModifyTime;
    }

    public long getMiyouModifyTime() {
        return miyouModifyTime;
    }

    public void setMiyouModifyTime(long miyouModifyTime) {
        this.miyouModifyTime = miyouModifyTime;
    }

    public static ModifyTimeJson parse(String jsonStr) {
        ModifyTimeJson returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ModifyTimeJson>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ModifyTimeJson parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
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
