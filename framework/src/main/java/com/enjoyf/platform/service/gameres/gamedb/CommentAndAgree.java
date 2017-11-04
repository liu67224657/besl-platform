package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 15-9-30
 * Time: 下午3:21
 * To change this template use File | Settings | File Templates.
 */
public class CommentAndAgree  implements Serializable {
    private String comment1;
    private String agree1;
    private String comment2;
    private String agree2;
    private String comment3;
    private String agree3;

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getAgree1() {
        return agree1;
    }

    public void setAgree1(String agree1) {
        this.agree1 = agree1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getAgree2() {
        return agree2;
    }

    public void setAgree2(String agree2) {
        this.agree2 = agree2;
    }

    public String getComment3() {
        return comment3;
    }

    public void setComment3(String comment3) {
        this.comment3 = comment3;
    }

    public String getAgree3() {
        return agree3;
    }

    public void setAgree3(String agree3) {
        this.agree3 = agree3;
    }


      public static CommentAndAgree parse(String jsonStr) {
        CommentAndAgree returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<CommentAndAgree>() {
                });
            } catch (IOException e) {
                GAlerter.lab("CommentAndAgree parse error, jsonStr:" + jsonStr, e);
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
