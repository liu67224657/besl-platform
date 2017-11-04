package com.enjoyf.platform.service.joymeapp.config;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/3/19
 * Description:
 */
public class ShakeItem implements Serializable {

//    private String shakeItemId;   //appkey+platform+channel+type+directid

    private int shakeType;

    private String directId;

    private int weight = 0;

//    public String getShakeItemId() {
//        return shakeItemId;
//    }
//
//    public void setShakeItemId(String shakeItemId) {
//        this.shakeItemId = shakeItemId;
//    }

    public String getDirectId() {
        return directId;
    }

    public void setDirectId(String directId) {
        this.directId = directId;
    }

    public int getShakeType() {
        return shakeType;
    }

    public void setShakeType(int shakeType) {
        this.shakeType = shakeType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static ShakeItem parse(String jsonStr) {
        ShakeItem item = null;
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                item = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ShakeItem>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ShakeItem parse error, jsonStr:" + jsonStr, e);
            }
        }
        return item;
    }

}
