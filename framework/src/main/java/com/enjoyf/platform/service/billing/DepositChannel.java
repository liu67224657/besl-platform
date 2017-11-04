/**
 * (C) 2009 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.billing;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class DepositChannel implements Serializable {

    private static Map<String, DepositChannel> map = new LinkedHashMap<String, DepositChannel>();

    //the initialize status   未审核n
    public static DepositChannel IAPPPAY = new DepositChannel("iapppay");      //爱贝

    public static DepositChannel APPSTORE = new DepositChannel("appstore");      //appstore

    public static DepositChannel AISI = new DepositChannel("i4");      //爱思
    public static DepositChannel HAIMA = new DepositChannel("hm");      //海马
    public static DepositChannel XY = new DepositChannel("xy");//xy
    public static DepositChannel KUAIYONG = new DepositChannel("kuaiyong");//快用
    public static DepositChannel GUOPAN = new DepositChannel("guopan");//果盘
    public static DepositChannel PP = new DepositChannel("pp");//pp助手
    public static DepositChannel TB = new DepositChannel("tb");      //同步推

    public static final DepositChannel C360 = new DepositChannel("c360");//360
    public static final DepositChannel UC = new DepositChannel("uc");//uc
    public static final DepositChannel XIAOMI = new DepositChannel("xiaomi");//小米
    public static final DepositChannel BAIDU = new DepositChannel("baidu");//百度
    public static final DepositChannel HUAWEI = new DepositChannel("huawei");//华为
    public static final DepositChannel OPPO = new DepositChannel("oppo");//oppo
    public static final DepositChannel IIAPPLE = new DepositChannel("iiapple");//爱苹果

    public static final DepositChannel ITOOLS = new DepositChannel("itools");//itools
    public static final DepositChannel YIJIE = new DepositChannel("yijie");//itools

    public static final DepositChannel JOYME = new DepositChannel("joyme");//joyme
    public static final DepositChannel YOUKU = new DepositChannel("youku");//youku

    public static final DepositChannel APPCHINA = new DepositChannel("appchina");//应用汇
    public static final DepositChannel C9377 = new DepositChannel("c9377");//c9377
    public static final DepositChannel C7881 = new DepositChannel("c7881");//猎宝网

    public static final DepositChannel ANZHI = new DepositChannel("anzhi");//anzhi
    public static final DepositChannel YYB = new DepositChannel("yyb");//应用宝
    public static final DepositChannel WANDOUJIA = new DepositChannel("wandoujia");//豌豆荚
    public static final DepositChannel GIONEE = new DepositChannel("gionee");//金立
    public static final DepositChannel VIVO = new DepositChannel("vivo");//步步高


    public static final DepositChannel YUMI = new DepositChannel("yumi");//玉米渠道
    public static final DepositChannel CC = new DepositChannel("cc");//虫虫助手
    public static final DepositChannel DANGLE = new DepositChannel("dangle");//当乐
    public static final DepositChannel LENOVO = new DepositChannel("lenovo");//联想
    public static final DepositChannel OUWAN = new DepositChannel("ouwan");//有米
    public static final DepositChannel PPTV = new DepositChannel("pptv");//pptv
    public static final DepositChannel YOULONG = new DepositChannel("youlong");//游龙
    public static final DepositChannel NDUO = new DepositChannel("nduo");// N多
    public static final DepositChannel GFAN = new DepositChannel("gfan");// 机锋
    public static final DepositChannel COOL = new DepositChannel("cool");// 酷派
    public static final DepositChannel C49YOU = new DepositChannel("c49you");// 49you
    public static final DepositChannel TIEXUE = new DepositChannel("tiexue");// tiexue
    public static final DepositChannel JRTT = new DepositChannel("jrtt");// 今日头条
    public static final DepositChannel SINA = new DepositChannel("sina");// 新浪
    public static final DepositChannel KAOPU = new DepositChannel("kaopu");// 靠谱
    public static final DepositChannel MZW = new DepositChannel("mzw");// 拇指玩
    public static final DepositChannel MMY = new DepositChannel("mmy");// 木蚂蚁

    public static final DepositChannel LETV = new DepositChannel("letv");// 乐视
    public static final DepositChannel MEIZU = new DepositChannel("meizu");// 魅族
    public static final DepositChannel SOGOU = new DepositChannel("sogou");// 搜狗
    public static final DepositChannel PAOJIAO = new DepositChannel("paojiao");// 泡椒
    public static final DepositChannel PYW = new DepositChannel("pyw");// 朋友玩
    public static final DepositChannel LIULIAN = new DepositChannel("liulian");// 榴莲
    public static final DepositChannel NHDZ = new DepositChannel("nhdz");//内涵段子
    public static final DepositChannel TT = new DepositChannel("tt");//tt语音
    private String code;

    private DepositChannel(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "DepositChannel code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof DepositChannel) {
            return code.equalsIgnoreCase(((DepositChannel) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static DepositChannel getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<DepositChannel> getAll() {
        return map.values();
    }
}
