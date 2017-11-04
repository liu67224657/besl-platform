/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
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
public class ClientItemType implements Serializable {
    //content, profile, gameres
    private static Map<Integer, ClientItemType> map = new HashMap<Integer, ClientItemType>();

    //the content
    public static final ClientItemType ARTICLE = new ClientItemType(0);
    public static final ClientItemType GAME = new ClientItemType(1);

    //推荐墙 - 精选应用
    public static final ClientItemType APPS = new ClientItemType(2);
    //推荐墙 - 热门活动
    public static final ClientItemType EVENTS = new ClientItemType(3);

    //手游排行榜
    public static final ClientItemType GAMETOP = new ClientItemType(4);

    //微信公众号 - 轮播图
    public static final ClientItemType WECHAT_BBSAC_TOP = new ClientItemType(5);
    //微信公众号 - 论坛活动
    public static final ClientItemType WECHAT_BBSAC_LIST = new ClientItemType(6);

    //推广
    public static final ClientItemType ADVERT = new ClientItemType(7);
    //人
    public static final ClientItemType PROFILE = new ClientItemType(8);
    //着迷玩霸 轮播图 自定义管理
    public static final ClientItemType CUSTOM = new ClientItemType(9);

    //着迷玩霸2.0.3 今日推荐
    public static final ClientItemType TODAY_RECOMMEND = new ClientItemType(10);

    //着迷玩霸2.0.3 热门页自定义推荐楼层
    public static final ClientItemType HOT_FLOOR = new ClientItemType(11);

    //着迷玩霸2.0.3 游戏分类之第一分类类别
    public static final ClientItemType HOT_PAGE_FIRST_CATEGORY = new ClientItemType(12);

    //着迷玩霸2.0.3 游戏分类之第二分类类别
    public static final ClientItemType HOT_PAGE_SECOND_CATEGORY = new ClientItemType(13);
    //WIKIAPP
    public static final ClientItemType WIKIAPP_HEADINFO = new ClientItemType(14);    //首页轮播图
    public static final ClientItemType WIKIAPP_WIKI = new ClientItemType(15);    //WIKI
    public static final ClientItemType WIKIAPP_SPECIAL = new ClientItemType(16);     //专题
    public static final ClientItemType WIKIAPP_WIKI_RANK = new ClientItemType(17);   //排行榜
    public static final ClientItemType PC_EXCLUSIVE = new ClientItemType(18);   // pc独家礼包列表


    //网站-游戏库
    public static final ClientItemType GAME_FOCUS = new ClientItemType(19);            //焦点游戏
    public static final ClientItemType GAME_HOT_MAJOR = new ClientItemType(20);         //热门大作
    public static final ClientItemType GAME_HOT_CATEGORY = new ClientItemType(21);      //热门类型
    public static final ClientItemType GAME_NEWS = new ClientItemType(22);           //最新入库
    public static final ClientItemType GAME_POPULAR_RANK = new ClientItemType(23);       //人气排行
    public static final ClientItemType GAME_RECOMMEND = new ClientItemType(24);          //着迷推荐
    public static final ClientItemType GAME_MOBILE = new ClientItemType(26);          //手机游戏
    public static final ClientItemType GAME_PC = new ClientItemType(27);          //电脑游戏
    public static final ClientItemType GAME_PSP = new ClientItemType(28);          //掌机游戏
    public static final ClientItemType GAME_TV = new ClientItemType(29);          //电视游戏

    //
    public static final ClientItemType WEIXIN_HOT_SEARCH_LIST=new ClientItemType(25); //微信礼包搜索页面热词推荐


    private int code;

    public ClientItemType() {
    }

    public ClientItemType(int c) {
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
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ClientItemType) o).code) return false;

        return true;
    }

    public static ClientItemType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ClientItemType> getAll() {
        return map.values();
    }
}
