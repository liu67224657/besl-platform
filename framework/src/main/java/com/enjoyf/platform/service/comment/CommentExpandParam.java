package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-12-1
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public class CommentExpandParam implements Serializable {
    private String number;//编号
    private String star;//星级
    private List<FloatMenuParam> floatMenuList = new ArrayList<FloatMenuParam>();

    public static String toJson(List<FloatMenuParam> paramList) {
        return JsonBinder.buildNormalBinder().toJson(paramList);
    }

    public static CommentExpandParam parse(String jsonStr) {
        CommentExpandParam returnValue = null;
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ArrayList<CommentExpandParam>>() {
                });
            } catch (IOException e) {
                GAlerter.lab("ReplyBody parse error, jsonStr:" + jsonStr, e);
            }
        }
        return returnValue;
    }

    private void add(List<FloatMenuParam> list) {
        this.floatMenuList.addAll(list);
    }

    public List<FloatMenuParam> getFloatMenuList() {
        return floatMenuList;
    }

    public void setFloatMenuList(List<FloatMenuParam> floatMenuList) {
        this.floatMenuList = floatMenuList;
    }
}
