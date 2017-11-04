package com.enjoyf.platform.service.gameres;

import java.io.Serializable;

/**
 * User: taijunli
 * Date: 12-1-11
 * Time: 下午6:07
 * To change this template use File | Settings | File Templates.
 */
public class GameResourceStatus implements Serializable {
    //隐藏风格
    public static final Integer HIDDEN_RESOURCE_STYLE = 1;
    //隐藏发行商/运营商
    public static final Integer HIDDEN_PUBLISH_COMPANY = 2;
    //隐藏开发商
    public static final Integer HIDDEN_DEVELOP = 4;
    //隐藏游戏人数
    public static final Integer HIDDEN_PLAYER_NUMBER = 8;
    //隐藏游戏大小
    public static final Integer HIDDEN_FILE_SIZE = 16;
    //隐藏发售日期
    public static final Integer HIDDEN_PUBLISH_DATE = 32;
    //隐藏官网
    public static final Integer HIDDEN_RESOURCE_URL = 64;
    //隐藏语言
    public static final Integer HIDDEN_LANGUAGE = 128;
    //隐藏游戏简介
    public static final Integer HIDDEN_RESOURCE_DESC = 256;

    //隐藏更新日期
    public static final Integer HIDDEN_RESOURCE_LASTUPDATEDATE = 512;

    //隐藏购买链接
    public static final Integer HIDDEN_RESOURCE_BUYLINK = 1024;

    public static final Integer HIDDEN_RESOURCE_PRICE = 2056;

    private Integer value;

    public GameResourceStatus(Integer value) {
        this.value = value;
    }

    public GameResourceStatus() {

    }

    public void hidden(int value) {
        this.value += value;
    }

    public boolean showResourceStyle() {
        return (value & HIDDEN_RESOURCE_STYLE) <= 0;
    }

    public boolean showPublishCompany() {
        return (value & HIDDEN_PUBLISH_COMPANY) <= 0;
    }

    public boolean showDevelop() {
        return (value & HIDDEN_DEVELOP) <= 0;
    }

    public boolean showPlayerNumber() {
        return (value & HIDDEN_PLAYER_NUMBER) <= 0;
    }

    public boolean showFileSize() {
        return (value & HIDDEN_FILE_SIZE) <= 0;
    }

    public boolean showPublishDate() {
        return (value & HIDDEN_PUBLISH_DATE) <= 0;
    }

    public boolean showResourceUrl() {
        return (value & HIDDEN_RESOURCE_URL) <= 0;
    }

    public boolean showLanguage() {
        return (value & HIDDEN_LANGUAGE) <= 0;
    }

    public boolean showResourceDesc() {
        return (value & HIDDEN_RESOURCE_DESC) <= 0;
    }

    public boolean showLastUpdateDate() {
        return (value & HIDDEN_RESOURCE_LASTUPDATEDATE) <= 0;
    }

    public boolean showBuyLink() {
        return (value & HIDDEN_RESOURCE_BUYLINK) <= 0;
    }

    public boolean showPrice() {
        return (value & HIDDEN_RESOURCE_PRICE) <= 0;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "GameResourceStatus value=" + value;
    }

    public static GameResourceStatus getByValue(Integer v) {
        return new GameResourceStatus(v);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof GameResourceStatus) {
            return value.equals(((GameResourceStatus) obj).getValue());
        } else {
            return false;
        }
    }

}
