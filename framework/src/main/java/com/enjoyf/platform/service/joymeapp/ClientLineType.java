/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: 元素类型
 */
public class ClientLineType implements Serializable {

    private static Map<Integer, ClientLineType> map = new HashMap<Integer, ClientLineType>();

    //the content
    //大端
    public static final ClientLineType BIGITEM = new ClientLineType(0);
    //新闻端
    public static final ClientLineType NEWSITEM = new ClientLineType(1);

    public static final ClientLineType GUIDEITEM = new ClientLineType(3);

    public static final ClientLineType GUIDEITEMFOCUS = new ClientLineType(4);

    //App推荐墙
    public static final ClientLineType PLATINUM = new ClientLineType(5);

    //手游排行榜
    public static final ClientLineType MobileGame = new ClientLineType(6);


    //微信 论坛活动
    public static final ClientLineType WECHAT_BBSAC = new ClientLineType(7);

    //
    public static final ClientLineType SHAKEGAME = new ClientLineType(8);

    //大端
    public static final ClientLineType GAMECLIENT = new ClientLineType(9);

    public static final ClientLineType GAMENEWSTAB = new ClientLineType(10);
    //wikiapp
    public static final ClientLineType WIKIAPP_WIKIRANK = new ClientLineType(11);  //wiki排行榜
    public static final ClientLineType WIKIAPP_INDEX = new ClientLineType(12);   //首页
    public static final ClientLineType PC_EXCLUSIVE = new ClientLineType(13);   // pc独家礼包列表
    public static final ClientLineType WEIXIN_HOT_SEARCH_LIST=new ClientLineType(15); //微信礼包搜索页面热词推荐

    public static final ClientLineType GAME_COLLECTION = new ClientLineType(14);    //网站-游戏库


    private int code;

    public ClientLineType() {
    }

    public ClientLineType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ClientLineType) o).code) return false;

        return true;
    }

    public static ClientLineType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ClientLineType> getAll() {
        return map.values();
    }
}
