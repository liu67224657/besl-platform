/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-7 下午12:02
 * Description:
 */
public class AuthAppDetail implements Serializable {
    //the app.
    private String appName;

    private String description;

    //
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static AuthAppDetail parse(String jsonStr) {
        AuthAppDetail returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<AuthAppDetail>() {
                });
            } catch (Exception e) {
                GAlerter.lab("AuthAppDetail parse error, jsonStr:" + jsonStr, e);
            }
        }

        return returnValue;
    }
}
