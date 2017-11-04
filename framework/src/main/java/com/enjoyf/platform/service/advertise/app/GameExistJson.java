package com.enjoyf.platform.service.advertise.app;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-6-7
 * Time: 下午12:03
 * To change this template use File | Settings | File Templates.
 */
public class GameExistJson implements Serializable {

    private String title1;
    private String desc1;
    private String url1;
    private String picurl1;
    private String type1;

    private String title2;
    private String desc2;
    private String url2;
    private String picurl2;
    private String type2;

    private String title3;
    private String desc3;
    private String url3;
    private String picurl3;
    private String type3;

    private String title4;
    private String desc4;
    private String url4;
    private String picurl4;
    private String type4;

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getTitle4() {
        return title4;
    }

    public void setTitle4(String title4) {
        this.title4 = title4;
    }

    public String getDesc4() {
        return desc4;
    }

    public void setDesc4(String desc4) {
        this.desc4 = desc4;
    }

    public String getUrl4() {
        return url4;
    }

    public void setUrl4(String url4) {
        this.url4 = url4;
    }

    public String getType4() {
        return type4;
    }

    public void setType4(String type4) {
        this.type4 = type4;
    }

    public String getPicurl1() {
        return picurl1;
    }

    public void setPicurl1(String picurl1) {
        this.picurl1 = picurl1;
    }

    public String getPicurl2() {
        return picurl2;
    }

    public void setPicurl2(String picurl2) {
        this.picurl2 = picurl2;
    }

    public String getPicurl3() {
        return picurl3;
    }

    public void setPicurl3(String picurl3) {
        this.picurl3 = picurl3;
    }

    public String getPicurl4() {
        return picurl4;
    }

    public void setPicurl4(String picurl4) {
        this.picurl4 = picurl4;
    }

    public static GameExistJson parse(String jsonStr) {
        GameExistJson returnValue = null;

        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<GameExistJson>() {
                });
            } catch (IOException e) {
                GAlerter.lab("PublishParam parse error, jsonStr:" + jsonStr, e);
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
