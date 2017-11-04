package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by xupeng on 16/3/20.
 */
public class CommentVideoUrl implements Serializable {
    private String qnurl;
    private String ykurl;

    public String getQnurl() {
        return qnurl;
    }

    public void setQnurl(String qnurl) {
        this.qnurl = qnurl;
    }

    public String getYkurl() {
        return ykurl;
    }

    public void setYkurl(String ykurl) {
        this.ykurl = ykurl;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static CommentVideoUrl parse(String jsonStr) {
        CommentVideoUrl returnValue = null;
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<CommentVideoUrl>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ReplyBody parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}


