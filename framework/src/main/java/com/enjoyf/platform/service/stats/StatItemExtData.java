/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-4 下午4:11
 * Description:
 */
public class StatItemExtData implements Serializable {
    //
    private String extValue01;
    private String extValue02;
    private String extValue03;
    private String extValue04;

    ////
    public StatItemExtData() {
    }

    public String getExtValue01() {
        return extValue01;
    }

    public void setExtValue01(String extValue01) {
        this.extValue01 = extValue01;
    }

    public String getExtValue02() {
        return extValue02;
    }

    public void setExtValue02(String extValue02) {
        this.extValue02 = extValue02;
    }

    public String getExtValue03() {
        return extValue03;
    }

    public void setExtValue03(String extValue03) {
        this.extValue03 = extValue03;
    }

    public String getExtValue04() {
        return extValue04;
    }

    public void setExtValue04(String extValue04) {
        this.extValue04 = extValue04;
    }

    public static StatItemExtData parse(String jsonStr) {
        StatItemExtData returnValue = new StatItemExtData();

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<StatItemExtData>() {
                });

            } catch (IOException e) {
                GAlerter.lab("StatItemExtData parse error, jsonStr:" + jsonStr, e);
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

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
