package com.enjoyf.platform.service.content;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
public class GoodsActionType implements Serializable {
    private static Map<Integer, GoodsActionType> map = new LinkedHashMap<Integer, GoodsActionType>();
    //主站
    public static final GoodsActionType WWW = new GoodsActionType(0, "主站");
    //积分礼包
    public static final GoodsActionType GIFT = new GoodsActionType(1, "积分礼包");
    //迷豆商城
    public static final GoodsActionType MIDOU = new GoodsActionType(2, "迷豆商城");
    //糖果联盟商城
    public static final GoodsActionType TANGGUOLIANMENG = new GoodsActionType(3, "糖果联盟商城");
    public static final GoodsActionType MANHUAHEZI = new GoodsActionType(4, "漫画盒子商城");
    public static final GoodsActionType QUANQIU = new GoodsActionType(5, "全球奇闻趣事商城");
    public static final GoodsActionType QIUSHI = new GoodsActionType(6, "糗事趣闻大全商城");
    public static final GoodsActionType GAOXIAO = new GoodsActionType(7, "搞笑视频大全商城");
    public static final GoodsActionType MINGXING = new GoodsActionType(8, "明星那点事商城");
    public static final GoodsActionType MEINV = new GoodsActionType(9, "美女夜疯狂商城");
    public static final GoodsActionType FUQI = new GoodsActionType(10, "夫妻那点事商城");
    public static final GoodsActionType SYHB = new GoodsActionType(11, "手游画报商城");
    public static final GoodsActionType YKSC = new GoodsActionType(12, "优酷商城");

    private int code;
    private String name;

    public GoodsActionType(int code, String name) {
        this.code = code;
        this.name = name;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((GoodsActionType) o).code) return false;

        return true;
    }

    public static GoodsActionType getByCode(int c) {

        return map.get(c) == null ? GoodsActionType.WWW : map.get(c);

    }

    public static Collection<GoodsActionType> getAll() {
        return map.values();
    }
}
