package com.enjoyf.platform.service.point;

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
public class PointActionType implements Serializable {
    private static Map<Integer, PointActionType> map = new HashMap<Integer, PointActionType>();
    //手动调整
    public static final PointActionType ADJUST = new PointActionType(0, HistoryActionType.POINT.getCode(), "管理员调整");
    //分享
    public static final PointActionType SHARE = new PointActionType(1, HistoryActionType.POINT.getCode(), "分享");
    //兑换
    public static final PointActionType EXCHANGE_GIFT = new PointActionType(2, HistoryActionType.POINT.getCode(), "兑换");
    //抽奖
    public static final PointActionType LOTTERY_AWARD = new PointActionType(3, HistoryActionType.POINT.getCode(), "抽奖");
    //
    public static final PointActionType CONTENT_ADJUST_POINT = new PointActionType(4, HistoryActionType.POINT.getCode(), "");
    //
    public static final PointActionType CONTENT_ADMIN_ADJUST_POINT = new PointActionType(5, HistoryActionType.POINT.getCode(), "");

    public static final PointActionType DISCUZ_SYNC = new PointActionType(6, HistoryActionType.POINT.getCode(), "论坛获得");

    public static final PointActionType APPKEY = new PointActionType(7, HistoryActionType.POINT.getCode(), "");

    public static final PointActionType DOWNLOAD_APP_FROM_POINT_WALL = new PointActionType(8, HistoryActionType.POINT.getCode(), "下载积分墙");
    //签到 没用了 任务
    public static final PointActionType SIGN = new PointActionType(9, HistoryActionType.POINT.getCode(), "手游画报签到");

    //=======================玩霸3.0=======================================

    public static final PointActionType PHONE_LOGIN_AND_BIND = new PointActionType(10, HistoryActionType.POINT.getCode(), "首次登录或绑定手机号");//手机号首次登录或绑定

    public static final PointActionType OTHER_LOGIN_AND_BIND = new PointActionType(11, HistoryActionType.POINT.getCode(), "第三方首次登录或绑定");//第三方首次登录或绑定

    public static final PointActionType WANBA_SIGN = new PointActionType(12, HistoryActionType.POINT.getCode(), "签到");//签到

    public static final PointActionType WANBA_REPLY = new PointActionType(13, HistoryActionType.POINT.getCode(), "评论");//评论

    public static final PointActionType WANBA_AGREE = new PointActionType(14, HistoryActionType.POINT.getCode(), "赞");//赞

    public static final PointActionType WANBA_SHARE = new PointActionType(15, HistoryActionType.POINT.getCode(), "分享");//分享

    public static final PointActionType ANSWER = new PointActionType(16, HistoryActionType.POINT.getCode(), "回答问题");//回答

    public static final PointActionType ACCEPT_ANSWER = new PointActionType(17, HistoryActionType.POINT.getCode(), "答案被采纳");//采纳回答

    public static final PointActionType WANBA_REDUCE_POINT = new PointActionType(18, HistoryActionType.POINT.getCode(), "提问");//减积分


    //==========================end============================================

    //===========================wiki==============================================
    public static final PointActionType WORSHIP = new PointActionType(19, HistoryActionType.PRESTIGE.getCode(), "膜拜");//膜拜
    public static final PointActionType THANKS = new PointActionType(20, HistoryActionType.PRESTIGE.getCode(), "感谢");//感谢
    public static final PointActionType FANS = new PointActionType(21, HistoryActionType.PRESTIGE.getCode(), "新增粉丝");//加减粉丝
    public static final PointActionType WIKI_PAGE_COLLECT = new PointActionType(22, HistoryActionType.PRESTIGE.getCode(), "词条被收藏");//词条被收藏
    public static final PointActionType WIKI_PAGE_AGREE = new PointActionType(23, HistoryActionType.PRESTIGE.getCode(), "词条被点赞");//词条被点赞
    public static final PointActionType WIKI_PAGE_SHORT_COMMENTS = new PointActionType(24, HistoryActionType.PRESTIGE.getCode(), "词条被短评");//词条被短评
    public static final PointActionType WIKI_PAGE_COMMENT = new PointActionType(25, HistoryActionType.PRESTIGE.getCode(), "词条被评论");//词条被评论
    public static final PointActionType WIKI_PAGE_READ = new PointActionType(26, HistoryActionType.PRESTIGE.getCode(), "词条被阅读");//词条被阅读


    public static final PointActionType WIKI_CREATE_PAGE = new PointActionType(27, HistoryActionType.POINT.getCode(), "创建页面");//创建页面
    public static final PointActionType WIKI_MODIFY_PAGE = new PointActionType(28, HistoryActionType.POINT.getCode(), "修改页面");//修改页面
    public static final PointActionType WIKI_CREATE_SHORT_COMMENTS = new PointActionType(30, HistoryActionType.POINT.getCode(), "发表短评");//发表短评
    public static final PointActionType WIKI_THANKS_AUTHOR = new PointActionType(31, HistoryActionType.POINT.getCode(), "感谢作者");//感谢作者
    public static final PointActionType FOLLOW_USER = new PointActionType(32, HistoryActionType.POINT.getCode(), "关注用户");//关注用户
    public static final PointActionType FOLLOW_WIKI = new PointActionType(33, HistoryActionType.POINT.getCode(), "关注WIKI");//关注WIKI

    public static final PointActionType GIFT_LOTTERY = new PointActionType(34, HistoryActionType.POINT.getCode(), "宝箱操作");//宝箱抽奖
    public static final PointActionType CANCEL_WIKI_PAGE_COLLECT = new PointActionType(35, HistoryActionType.PRESTIGE.getCode(), "取消收藏词条");//词条被阅读
    public static final PointActionType CANCEL_FOLLOW = new PointActionType(36, HistoryActionType.PRESTIGE.getCode(), "减少粉丝");//粉丝减少



    private int code;
    private int value;
    private String name;

    //code action_type  value= 0=积分 ，1=声望
    public PointActionType(int code, int value, String name) {
        this.code = code;
        this.value = value;
        this.name = name;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ActionType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((PointActionType) o).code) return false;

        return true;
    }

    public static PointActionType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<PointActionType> getAll() {
        return map.values();
    }
}
