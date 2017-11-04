package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-12
 * Time: 下午8:40
 * To change this template use File | Settings | File Templates.
 */
public class ReplyBody implements Serializable {

    private String text = "";
    private String pic = "";

    public ReplyBody() {
    }

    public static String toJson(ReplyBody replyBody) {
        return JsonBinder.buildNormalBinder().toJson(replyBody);
    }

    public static ReplyBody parse(String jsonStr) {
        ReplyBody returnValue = null;
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ReplyBody>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ReplyBody parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
