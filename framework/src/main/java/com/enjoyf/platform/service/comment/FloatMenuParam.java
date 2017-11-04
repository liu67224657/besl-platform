package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-1
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public class FloatMenuParam implements Serializable {
    private String uri;
    private String pic;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String toJson(FloatMenuParam param) {
        return JsonBinder.buildNormalBinder().toJson(param);
    }

    public static FloatMenuParam parse(String jsonStr) {
        FloatMenuParam returnValue = null;
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<FloatMenuParam>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ReplyBody parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }
}
