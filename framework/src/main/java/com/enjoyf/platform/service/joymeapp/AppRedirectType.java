package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class AppRedirectType implements Serializable {
    private static Map<Integer, AppRedirectType> map = new HashMap<Integer, AppRedirectType>();

    //    0	新游列表（native-list）
//    1	CMS文章单页（native-article）
//    2	攻略库专区首页（native）
//    3	攻略库专区列表(native-list)
//    4	WEBVIEW页面（WIKI首页文章页等）/（web-）
//    5	标签筛选页面（native-list）
//    6	礼包中心首页（native）
//    7	礼包详情页面（native）
//    8	攻略库wiki列表（native-list）
//    9 下载
//    10 新游专题（native-list）
//    11 着迷更多攻略
//    12 攻略库推荐列表
    //the content
    public static final AppRedirectType NEWGAMELIST = new AppRedirectType(0);
    public static final AppRedirectType CMSARTICLE = new AppRedirectType(1);
    public static final AppRedirectType CMSGAMEINDEX = new AppRedirectType(2);
    public static final AppRedirectType CMSGAMELIST = new AppRedirectType(3);
    public static final AppRedirectType WEBVIEW = new AppRedirectType(4);
    public static final AppRedirectType TAGLIST = new AppRedirectType(5);
    public static final AppRedirectType GIFTINDEX = new AppRedirectType(6);
    public static final AppRedirectType GIFTDETAIL = new AppRedirectType(7);
    public static final AppRedirectType WIKILIST = new AppRedirectType(8);
    public static final AppRedirectType DOWNLOAD = new AppRedirectType(9);
    public static final AppRedirectType NEWGAMESPECIAL = new AppRedirectType(10);
    public static final AppRedirectType MOREAPP = new AppRedirectType(11);
    public static final AppRedirectType RECOMLIST = new AppRedirectType(12);
    //

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //通用
    public static final AppRedirectType NOTHING = new AppRedirectType(-100);
    public static final AppRedirectType OPEN_APP = new AppRedirectType(-1); //打开应用
    public static final AppRedirectType DEFAULT_WEBVIEW = new AppRedirectType(-2);//默认的webview
    public static final AppRedirectType DEFAULT_NOTICE = new AppRedirectType(-3);//消息中心
    public static final AppRedirectType REDIRECT_DOWNLOAD = new AppRedirectType(-4);//下载

    //新版手游画报
    public static final AppRedirectType OPEN_LOGIN_DIALOG = new AppRedirectType(20);
    public static final AppRedirectType CLICK_LIKEGAME = new AppRedirectType(21);
    public static final AppRedirectType REDIRECT_PROFILE = new AppRedirectType(22);
    public static final AppRedirectType REDIRECT_WEBVIEW = new AppRedirectType(23);
    public static final AppRedirectType REDIRECT_GAMEDETAIL = new AppRedirectType(24);
    public static final AppRedirectType REDIRECT_MIYOU = new AppRedirectType(25);
    public static final AppRedirectType REDIRECT_HOTGAME = new AppRedirectType(26);
    public static final AppRedirectType REDIRECT_PAIHANGBANG = new AppRedirectType(27);
    public static final AppRedirectType REDIRECT_GIFTMARKET = new AppRedirectType(28);
    public static final AppRedirectType MIYOU_FOLLOW_NOTICE = new AppRedirectType(29);  //迷友喜欢之后通知客户端
    public static final AppRedirectType CLICK_LIKEGAME_ADD = new AppRedirectType(30);
    public static final AppRedirectType REDIRECT_TAGARTICLE = new AppRedirectType(31);
    public static final AppRedirectType REDIRECT_TAGARTICLE_OPEN = new AppRedirectType(34);//任务打开话题订阅
    public static final AppRedirectType TASK_OVER_NOTICE = new AppRedirectType(35); //完成任务通知客户端

    public static final AppRedirectType REDIRECT_WANBA_MESSAGELIST = new AppRedirectType(39); //迷友圈个人主页消息列表tab

    //wiki app
    public static final AppRedirectType USER_HOME = new AppRedirectType(100);  //个人中心
    public static final AppRedirectType WIKI_RANK = new AppRedirectType(101);  //wiki排行榜
    public static final AppRedirectType HOT_PIC = new AppRedirectType(102);  //热图墙
    public static final AppRedirectType HOT_PIC_DETAIL = new AppRedirectType(103);  //热图墙图片详情
    public static final AppRedirectType WIKI_ACTIVE_USERS = new AppRedirectType(104);  //wiki活跃用户
    public static final AppRedirectType INDEX_MORE = new AppRedirectType(105);   //首页更多
    public static final AppRedirectType INDEX = new AppRedirectType(106);   //首页
    public static final AppRedirectType DISCOVER = new AppRedirectType(107);  //发现
    public static final AppRedirectType MY_HOME = new AppRedirectType(108);  //我的
    public static final AppRedirectType WIKI_INDEX = new AppRedirectType(109);  //单wiki首页

    private int code;
    private AppRedirectType(int c) {
        this.code = c;
        map.put(code, this);
    }


    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppRedirectType) o).code) return false;

        return true;
    }

    public static AppRedirectType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppRedirectType> getAll() {
        return map.values();
    }
}
