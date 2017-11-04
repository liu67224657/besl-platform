/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.advertise.app;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description:
 */
public class PublishParam implements Serializable {
    
    private int numberParam;

    private long longtime;

    public int getNumberParam() {
        return numberParam;
    }

    public void setNumberParam(int numberParam) {
        this.numberParam = numberParam;
    }

    public long getLongtime() {
        return longtime;
    }

    public void setLongtime(long longtime) {
        this.longtime = longtime;
    }

    public static PublishParam parse(String jsonStr) {
        PublishParam returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<PublishParam>() {
                });
            } catch (IOException e) {
                GAlerter.lab("PublishParam parse error, jsonStr:" + jsonStr, e);
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
