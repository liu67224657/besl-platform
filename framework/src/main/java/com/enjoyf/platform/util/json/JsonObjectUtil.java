package com.enjoyf.platform.util.json;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ImageContent;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;

/**
 * <p/>
 * Description:json obj
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class JsonObjectUtil<T> {

    public String toJsonStr(T tObj) {
        return JsonBinder.buildNormalBinder().toJson(tObj);
    }

    public T parse(String jsonStr, Class<T> clazz) {
        T returnValue = null;

        if (!StringUtil.isEmpty(jsonStr)) {
            returnValue = JsonBinder.buildNormalBinder().fromJson(jsonStr, clazz);
        }

        return returnValue;
    }
}
