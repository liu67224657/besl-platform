package com.enjoyf.platform.service.joymeapp;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class AppChannelType implements Serializable {
    private static Map<String, AppChannelType> map = new HashMap<String, AppChannelType>();

    //默认 ""
    public static final AppChannelType DEF = new AppChannelType(2, "def", "默认");
    //苹果商店
    public static final AppChannelType APPSTORE = new AppChannelType(3, "appstore", "苹果商店");
    //pp助手
    public static final AppChannelType PP = new AppChannelType(4, "pp", "pp助手");
    //同步推
    public static final AppChannelType TBT = new AppChannelType(5, "tbt", "同步推");
    //91助手
    public static final AppChannelType C91 = new AppChannelType(6, "c91", "91助手");
    //360市场
    public static final AppChannelType C360 = new AppChannelType(7, "c360", "360市场");
    //91android
    public static final AppChannelType ANDROID91 = new AppChannelType(8, "91android", "91android");
    //木蚂蚁
    public static final AppChannelType MUMAYI = new AppChannelType(9, "mumayi", "木蚂蚁");
    //豌豆荚
    public static final AppChannelType WANDOUJIA = new AppChannelType(10, "wandoujia", "豌豆荚");
    //3g市场
    public static final AppChannelType G3 = new AppChannelType(11, "3g", "3g市场");
    //应用汇
    public static final AppChannelType APPCHINA = new AppChannelType(12, "appchina", "应用汇");
    //小米
    public static final AppChannelType XIAOMI = new AppChannelType(13, "xiaomi", "小米");
    //uc
    public static final AppChannelType UC = new AppChannelType(14, "uc", "uc应用市场");
    //机锋
    public static final AppChannelType GFAN = new AppChannelType(15, "gfan", "机锋");
    //百度
    public static final AppChannelType BAIDU = new AppChannelType(16, "baidu", "百度");
    //应用宝
    public static final AppChannelType YINGYONGBAO = new AppChannelType(17, "yingyongbao", "应用宝");
    //安卓市场
    public static final AppChannelType HIAPK = new AppChannelType(18, "hiapk", "安卓市场");
    //安智
    public static final AppChannelType ANZHI = new AppChannelType(19, "anzhi", "安智");
    //着迷官网
    public static final AppChannelType JOYME = new AppChannelType(20, "joyme", "着迷官网");
    //着迷企业版
    public static final AppChannelType JOYMEENT = new AppChannelType(21, "joymeent", "着迷企业版");
    //腾讯
    public static final AppChannelType TENCENT = new AppChannelType(22, "tencent", "腾讯");

    //腾讯广点通
    public static final AppChannelType TXGDT = new AppChannelType(23, "txgdt", "腾讯广点通");
    //微博大号
    public static final AppChannelType WBDH = new AppChannelType(24, "wbdh", "微博大号");
    //论坛
    public static final AppChannelType LUNTAN = new AppChannelType(25, "luntan", "论坛");
    //qq群
    public static final AppChannelType QQUN = new AppChannelType(26, "qqun", "qq群");
    //微信大号
    public static final AppChannelType WXDH = new AppChannelType(27, "wxdh", "微信大号");
    //百思不得姐
    public static final AppChannelType BSBDJIE = new AppChannelType(28, "bsbdj", "百思不得姐");
    //神机工场
    public static final AppChannelType LEGC = new AppChannelType(29, "legc", "神机工场");

    //搜狗手机助手
    public static final AppChannelType SOGOU = new AppChannelType(30, "sogou", "搜狗手机助手");
    //金立
    public static final AppChannelType JINLI = new AppChannelType(31, "jinli", "金立");
    //魅族
    public static final AppChannelType MEIZU = new AppChannelType(32, "meizu", "魅族");
    //N多
    public static final AppChannelType NDUO = new AppChannelType(33, "nduo", "N多");
    //泡椒
    public static final AppChannelType PAOJIAO = new AppChannelType(34, "paojiao", "泡椒");
    //联想
    public static final AppChannelType LENOVO = new AppChannelType(35, "lenovo", "联想");
    //海马手机助手
    public static final AppChannelType HMSJZS = new AppChannelType(36, "hmsjzs", "海马手机助手");

    //阿里应用分发平台
    public static final AppChannelType ALI = new AppChannelType(37, "ali", "阿里");

    //历趣开发者平台
    public static final AppChannelType LIQU = new AppChannelType(38, "liqu", "历趣");

    //oppo
    public static final AppChannelType OPPO = new AppChannelType(39, "oppo", "oppo");

    //vivo
    public static final AppChannelType VIVO = new AppChannelType(40, "vivo", "vivo");

    private long id;
    private String code;
    private String name;

    public AppChannelType(String c) {
        code = c.toLowerCase();
        map.put(code, this);
    }

    public AppChannelType(int id, String c, String name) {
        code = c.toLowerCase();
        this.id = id;
        this.name = name;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AppChannelType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return code.equalsIgnoreCase(((AppChannelType) o).getCode());
    }

    public static AppChannelType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

    public static Collection<AppChannelType> getAll() {
        return map.values();
    }


    public static void main(String[] args) {
        AppChannelType appChannelType = AppChannelType.getByCode("appstore");
        System.out.println(appChannelType);
    }
}
