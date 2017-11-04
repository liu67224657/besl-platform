package com.enjoyf.platform.service.joymeapp.config;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-3-4
 * Time: 下午5:11
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(value = {"shake_type","shake_buttons"})
public class AppConfigInfo implements Serializable {
    private String defad_url;
    private String gift_open;

    private String shake_open;
    private ShakeTag shake_tag = new ShakeTag();
    private Map<Integer, ShakeConfig> shakeconfigs = new HashMap<Integer, ShakeConfig>(); //摇一摇页的配置

    private String reddot_interval = "30";//单位:s
    private String shake_version = "0";//摇一摇开关 0:native,1:wap

    public String toJsonStr() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static AppConfigInfo parse(String jsonStr) {
        AppConfigInfo info = null;
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                info = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<AppConfigInfo>() {
                });
            } catch (IOException e) {
                GAlerter.lab("AppConfigInfo parse error, jsonStr:" + jsonStr, e);
            }
        }
        return info;
    }

    public String getShake_open() {
        return shake_open;
    }

    public void setShake_open(String shake_open) {
        this.shake_open = shake_open;
    }

    public String getDefad_url() {
        return defad_url;
    }

    public void setDefad_url(String defad_url) {
        this.defad_url = defad_url;
    }

    public String getGift_open() {
        return gift_open;
    }

    public void setGift_open(String gift_open) {
        this.gift_open = gift_open;
    }

    public ShakeTag getShake_tag() {
        return shake_tag;
    }

    public void setShake_tag(ShakeTag shake_tag) {
        this.shake_tag = shake_tag;
    }

    public Map<Integer, ShakeConfig> getShakeconfigs() {
        return shakeconfigs;
    }

    public void setShakeconfigs(Map<Integer, ShakeConfig> shakeconfigs) {
        this.shakeconfigs = shakeconfigs;
    }

    public String getReddot_interval() {
        return reddot_interval;
    }

    public void setReddot_interval(String reddot_interval) {
        this.reddot_interval = reddot_interval;
    }

    public String getShake_version() {
        return shake_version;
    }

    public void setShake_version(String shake_version) {
        this.shake_version = shake_version;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }


    public static void main(String[] args) {
        AppConfigInfo info = new AppConfigInfo();
        info.setShake_open("true");
        info.setDefad_url("");
        info.setGift_open("true");


//        {"shake_open":"false","defad_url":"","gift_open":"false"，"shake_type":{"100":1},"shake_buttons":[{"tag":"","bgpic":"","title":"","tagcolor":"","shaketype":1,"buttontext":"游戏"}]}
//
//        info.setShake_type(new HashMap<Integer, ShakeRange>());
//        info.getShake_type().put(ShakeType.GAME.getCode(), new ShakeRange(0, 30));
//        info.getShake_type().put(ShakeType.POINT.getCode(), new ShakeRange(31, 100));
//
//        info.setShake_buttons(new HashMap<Integer, ShakeConfig>());
//
//        ShakeConfig config = new ShakeConfig();
//        config.setButtontext("游戏");
//        info.getShake_buttons().put(ShakeType.GAME.getCode(), config);
//
//        ShakeConfig config2 = new ShakeConfig();
//        config2.setButtontext("礼包");
////        ShakeExtProps extProps=new ShakeExtProps();
////        config2.setExtProps(extProps);
//
//        config2.setTimeLimit(true);
//        config2.setShaketimes(1);
//        List<ShakeItemConfig> configList = new ArrayList<ShakeItemConfig>();
//
//        ShakeItemConfig shakeItemConfig = new ShakeItemConfig();
//        shakeItemConfig.setAmount(String.valueOf(100));
//        shakeItemConfig.setCount(10);
//        configList.add(shakeItemConfig);
//        ShakeItemConfig shakeItemConfig2 = new ShakeItemConfig();
//        shakeItemConfig2.setAmount(String.valueOf(1));
//        shakeItemConfig2.setCount(1000);
//        configList.add(shakeItemConfig2);
//
//        config2.setItemConfigs(configList);

//        info.getShake_buttons().put(ShakeType.POINT.getCode(), config2);

        System.out.println(info.toJsonStr());

        String s = info.toJsonStr();
        //{"shake_open":"true","defad_url":"","gift_open":"true","shake_type":{"1":{"min":0,"max":30},"2":{"min":31,"max":100}},"shake_buttons":{"1":{"tag":"","bgpic":"","title":"","tagcolor":"","buttontext":"游戏","extProps":null},"2":{"tag":"","bgpic":"","title":"","tagcolor":"","buttontext":"礼包","extProps":{"itemConfigs":[{"count":10,"amount":"100"},{"count":1000,"amount":"1"}],"limitTime":true,"beginTime":0,"endTime":0}}}}
        //{"shake_open":"true","defad_url":"","gift_open":"true","shake_type":{"1":{"min":0,"max":30},"2":{"min":31,"max":100}},"shake_buttons":{"1":{"tag":"","bgpic":"","title":"","tagcolor":"","buttontext":"游戏","extProps":null},"2":{"tag":"","bgpic":"","title":"","tagcolor":"","buttontext":"礼包","extProps":{"itemConfigs":[{"count":10,"amount":"100"},{"count":1000,"amount":"1"}],"limitTime":true,"beginTime":0,"endTime":0}}}
        System.out.println(AppConfigInfo.parse(s));
    }
}
