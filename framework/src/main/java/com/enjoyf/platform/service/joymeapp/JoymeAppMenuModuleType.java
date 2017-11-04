package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class JoymeAppMenuModuleType implements Serializable {

    private static Map<Integer, JoymeAppMenuModuleType> map = new HashMap<Integer, JoymeAppMenuModuleType>();

    public static final JoymeAppMenuModuleType DEFAULT = new JoymeAppMenuModuleType(0);   //底部普通菜单
    //怪物弹珠 精版
    public static final JoymeAppMenuModuleType TOP_ROLLING = new JoymeAppMenuModuleType(1);  //顶部轮播
    public static final JoymeAppMenuModuleType TOP_BELOW_ROLLING = new JoymeAppMenuModuleType(2);  //顶部下面的滚动条
    public static final JoymeAppMenuModuleType MIDDLE_ROLLING = new JoymeAppMenuModuleType(3);  //中间轮播
    //1.7.0之后 通版
    public static final JoymeAppMenuModuleType LEFT_ONE = new JoymeAppMenuModuleType(4);   //左边单一大图菜单
    public static final JoymeAppMenuModuleType RIGHT_FOUR = new JoymeAppMenuModuleType(5);   //右边四个小图菜单
    public static final JoymeAppMenuModuleType BOTTOM_LIST = new JoymeAppMenuModuleType(6);    //底部列表菜单
    public static final JoymeAppMenuModuleType HOT_RECOMMENDED = new JoymeAppMenuModuleType(7);    //热点推荐
    //2.0之后版本
    public static final JoymeAppMenuModuleType HEADLIST = new JoymeAppMenuModuleType(8);//顶部轮播图
    public static final JoymeAppMenuModuleType NEWSLIST = new JoymeAppMenuModuleType(9);//新闻滚动条
    public static final JoymeAppMenuModuleType OTHERINFO = new JoymeAppMenuModuleType(10);//其他信息
    public static final JoymeAppMenuModuleType MODULE1 = new JoymeAppMenuModuleType(11);//模块1
    public static final JoymeAppMenuModuleType MODULE2 = new JoymeAppMenuModuleType(12);//模块2
    public static final JoymeAppMenuModuleType MODULE3 = new JoymeAppMenuModuleType(13);//模块3

    private int code;

    private JoymeAppMenuModuleType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "JoymeAppMenuModuleType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((JoymeAppMenuModuleType) o).code) return false;

        return true;
    }

    public static JoymeAppMenuModuleType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<JoymeAppMenuModuleType> getAll() {
        return map.values();
    }
}
