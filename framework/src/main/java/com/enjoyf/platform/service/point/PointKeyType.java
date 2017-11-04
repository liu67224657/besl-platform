package com.enjoyf.platform.service.point;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-20 上午9:55
 * Description:
 */
public class PointKeyType implements Serializable {
    private static Map<String, PointKeyType> map = new LinkedHashMap<String, PointKeyType>();

    /*
    * www:主站
    * default：默认共用的
    * 可继续添加 key:appkey value:pointkey
    * 比如手游画报和海贼迷的积分需要共用  可新添加 两个常量 key为appkey  value为相同的pointkey即可
    * */
    public static final PointKeyType WWW = new PointKeyType("default", "newsyhb", "主站积分");  //主站
    public static final PointKeyType DEFAULT = new PointKeyType("khddefault", "default", "客户端通用积分"); //我的
    public static final PointKeyType SYHB = new PointKeyType("17yfn24TFexGybOF0PqjdY", "syhb", "手游画报积分");    //手游画报
    public static final PointKeyType YKHZ = new PointKeyType("08pkvrWvx5ArJNvhYf19kN", "ykhz", "优酷合作积分");    //优酷游戏中心
    public static final PointKeyType NEWSYHB = new PointKeyType("3iiv7VWfx84pmHgCUqRwun", "newsyhb", "新玩霸积分");    //新玩霸

    //
    private String code;
    private String value;

    //存放中文名称
    private String name;

    public PointKeyType(String appkey, String value, String name) {
        this.code = appkey;
        this.value = value;
        this.name = name;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "PointKeyType: code=" + code + " value=" + value + " name=" + name;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof PointKeyType)) {
            return false;
        }

        return code.equalsIgnoreCase(((PointKeyType) obj).getCode());
    }

    public static PointKeyType getByCode(String c) {
        return map.get(c) == null ? PointKeyType.DEFAULT : map.get(c);
    }

    public static Collection<PointKeyType> getAll() {
        return map.values();
    }
}
