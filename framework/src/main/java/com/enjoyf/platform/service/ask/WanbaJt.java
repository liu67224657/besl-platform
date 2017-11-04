package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2016/9/18 0018.
 */
public class WanbaJt implements Serializable {
    private static Map<Integer, WanbaJt> map = new HashMap<Integer, WanbaJt>();

    private static Map<Integer, WanbaJt> toolsmap = new HashMap<Integer, WanbaJt>();
//http://wiki.enjoyf.com/wiki/Gameclient_client

    //    -1		✓	打开应用
//    -2	url	✕	推送通知到webview页面(导航栏为默认)
//    -3		✓	消息中心页面（系统通知）
//            -4	appid/itunes url	✕	应用内打开AppStore 下载链接(appid为应用id)
//    50	活动id	✓	活动详情页
//    51	profileid	✓	个人主页
//    52	web url	✓	详情页面
//    53		✕	问答通知
//    54		✕	回答我的
//    55	问题id	✓	评论页面(详情页面通过js跳转)
    // wap  native:个人主页、活动列表、活动页、问答详情页
    public static final WanbaJt OPEN_APP = new WanbaJt(-1, "打开应用", true);    //wap
    public static final WanbaJt WAP = new WanbaJt(-2, "推送通知到webview页面(导航栏为默认)", true);    //wap
    public static final WanbaJt SYSTEM_MESSGAE = new WanbaJt(-3, "消息中心页面（系统通知）", true);    //wap
    public static final WanbaJt WAP_ADVERTISE = new WanbaJt(-4, "应用内打开AppStore 下载链接(appid为应用id)", true);    //wap

    // public static final WanbaJt ACTIVITY_PAGE = new WanbaJt(50, "活动详情页");
    public static final WanbaJt PERSONAL_PAGE = new WanbaJt(51, "个人主页____填写profileid", true);
    public static final WanbaJt ASK_QUESTION_PAGE = new WanbaJt(52, "问题详情页面____填写问题ID", true);//ji 只需要问题id


    //
    public static final WanbaJt ASK_QUESTION = new WanbaJt(53, "问答通知____不用填", false);
    public static final WanbaJt ANSWER_ME = new WanbaJt(54, "回答我的____不用填", false);
    public static final WanbaJt REPLY_PAGE = new WanbaJt(55, "评论页面____填写答案ID", false);
    public static final WanbaJt ASK_WHO = new WanbaJt(56, "问他（问题详情页通过js跳转）", false);
    public static final WanbaJt TAG_PAGE = new WanbaJt(57, "跳转游戏圈子（问题详情页通过js跳转）", false);
    public static final WanbaJt QUESTION_INVITE = new WanbaJt(58, "邀请回答（问题详情页通过js跳转）", false);


    public static final WanbaJt ANSWER_DETAIL = new WanbaJt(59, "答案详情页____填写答案ID", true);//只需要传答案id


    public static final WanbaJt ACTIVITY_LIST = new WanbaJt(60, "活动列表____不用填", true);//
    public static final WanbaJt ACTIVITY_DETAIL = new WanbaJt(61, "活动详情页____填写活动ID", true);//只需要传活动ID


    //跳转到wiki页面
    public static final WanbaJt WAP_WIKI = new WanbaJt(66, "wiki页面", true);

    private int code;
    private String name;
    private boolean toolsShow;

    public WanbaJt(int code, String name, boolean toolsShow) {
        this.code = code;
        this.name = name;
        this.toolsShow = toolsShow;
        if (toolsShow) {
            toolsmap.put(code, this);
        }
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
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


        if (code != ((WanbaJt) o).code) return false;

        return true;
    }


    public boolean getToolsShow() {
        return toolsShow;
    }

    public void setToolsShow(boolean toolsShow) {
        this.toolsShow = toolsShow;
    }

    public static WanbaJt getByCode(int c) {
        return map.get(c);
    }

    public static Collection<WanbaJt> getAll() {
        return map.values();
    }


    public static Collection<WanbaJt> getToolsAll() {
        return toolsmap.values();
    }

    public static void main(String[] args) {
        Collection<WanbaJt> all = WanbaJt.getAll();
        for (WanbaJt jt : all) {
            System.out.println(jt);
        }

    }


}
