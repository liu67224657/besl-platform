package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;



//为接口http://api.joyme.test/json/mood  ，把Mood类中的imgUrl改成pic,这样没有大写字符
public class MoodJson {
    private String code;
    private String pic;

    public MoodJson(String code, String pic) {
        this.code = code;
        this.pic = pic;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
