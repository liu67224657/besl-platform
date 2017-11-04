package com.enjoyf.platform.text;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午7:04
 * To change this template use File | Settings | File Templates.
 */
public class TextJsonItem implements Serializable {

    public static final int IMAGE_TYPE = 2;
    public static final int TEXT_TYPE = 1;

    private int type;
    private String item;
    private Integer width;
    private Integer height;

    public TextJsonItem() {
    }

    public TextJsonItem(int type, String item) {
        this.type = type;
        this.item = item;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public static String toJson(List<TextJsonItem> jsonItemList) {
        return JsonBinder.buildNonDefaultBinder().toJson(jsonItemList);
    }

    public static List<TextJsonItem> fromJson(String jsonString) {
        List<TextJsonItem> resultList = new ArrayList<TextJsonItem>();
        try {
            resultList = JsonBinder.buildNonNullBinder().getMapper().readValue(jsonString, new TypeReference<List<TextJsonItem>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * to json
     */
    public String toJson() {
        return JsonBinder.buildNonNullBinder().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
