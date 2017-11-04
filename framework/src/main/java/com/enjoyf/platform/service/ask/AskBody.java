package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/9/12
 */
public class AskBody implements Serializable {
    private String text;
    private List<String> pic;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public static AskBody toObject(String jsonStr) {
        return new Gson().fromJson(jsonStr, AskBody.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
