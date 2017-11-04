package com.enjoyf.platform.service.joymeapp.anime;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */
public class AnimeTagPicParam implements Serializable {
    private String ios;
    private String android;
    private String url;
    private String type = GameClientTagType.DEFAULT.getCode();


    public AnimeTagPicParam() {

    }

    public AnimeTagPicParam(String ios, String android) {
        this.ios = ios;
        this.android = android;
    }

    public static String toJson(AnimeTagPicParam paramTextJson) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ios", paramTextJson.getIos());
        jsonObject.put("android", paramTextJson.getAndroid());
        jsonObject.put("url", StringUtil.isEmpty(paramTextJson.getUrl()) ? "" : paramTextJson.getUrl());
        jsonObject.put("type", StringUtil.isEmpty(paramTextJson.getType()) ? "" : paramTextJson.getType());
        return jsonObject.toString();
    }

    public static AnimeTagPicParam fromJson(String jsonString) {
        if (StringUtil.isEmpty(jsonString)) {
            return null;
        }
        AnimeTagPicParam param = new AnimeTagPicParam();
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        if (!jsonObject.isNullObject()) {
            if (jsonObject.containsKey("ios")) {
                param.setIos(jsonObject.getString("ios"));
            }
            if (jsonObject.containsKey("android")) {
                param.setAndroid(jsonObject.getString("android"));
            }
            if (jsonObject.containsKey("url")) {
                param.setUrl(jsonObject.getString("url"));
            }
            if (jsonObject.containsKey("type")) {
                param.setType(jsonObject.getString("type"));
            }
        }
        return param;
    }

    public String toJson() {
        return JsonBinder.buildNonNullBinder().toJson(this);
    }

    public String getIos() {
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
