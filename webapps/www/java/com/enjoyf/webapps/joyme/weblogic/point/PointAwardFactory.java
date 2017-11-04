package com.enjoyf.webapps.joyme.weblogic.point;

import com.enjoyf.platform.service.point.PointActionType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-12
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public class PointAwardFactory {

    private static Map<PointActionType, PointAwardStragy> map = new HashMap<PointActionType, PointAwardStragy>();
    private static Map<PointActionType, Integer> pointMap = new HashMap<PointActionType, Integer>();//积分
    private static Map<PointActionType, Integer> prestigeMap = new HashMap<PointActionType, Integer>();//声望

    static {
        map.put(PointActionType.SHARE, new SharePointAwardStragy());
        map.put(PointActionType.CONTENT_ADMIN_ADJUST_POINT, new GroupContentPointAwardStragy());
        ///玩霸/////
        map.put(PointActionType.PHONE_LOGIN_AND_BIND, new WanbaLoginAndBindPointAwardStragy()); //login和绑定
        map.put(PointActionType.OTHER_LOGIN_AND_BIND, new WanbaLoginAndBindPointAwardStragy());
        map.put(PointActionType.WANBA_SIGN, new WanbaOtherPointAwardStragy());//其他加积分
        map.put(PointActionType.WANBA_REPLY, new WanbaOtherPointAwardStragy());
        map.put(PointActionType.WANBA_AGREE, new WanbaOtherPointAwardStragy());
        map.put(PointActionType.WANBA_SHARE, new WanbaOtherPointAwardStragy());
        map.put(PointActionType.WIKI_CREATE_PAGE, new WanbaOtherPointAwardStragy());
        map.put(PointActionType.WIKI_MODIFY_PAGE, new WanbaOtherPointAwardStragy());
        map.put(PointActionType.WIKI_CREATE_SHORT_COMMENTS, new WanbaOtherPointAwardStragy());
        map.put(PointActionType.WIKI_THANKS_AUTHOR, new WanbaOtherPointAwardStragy());
        map.put(PointActionType.FOLLOW_USER, new WanbaOtherPointAwardStragy());
        map.put(PointActionType.FOLLOW_WIKI, new WanbaOtherPointAwardStragy());

        map.put(PointActionType.WANBA_REDUCE_POINT, new WanbaReducePointAwardStragy()); //加减积分
        map.put(PointActionType.ANSWER, new WanbaReducePointAwardStragy());
        ////////////wiki//////////////
        map.put(PointActionType.FANS, new PrestigeAwardStragy()); //粉丝
        map.put(PointActionType.WIKI_PAGE_COLLECT, new PrestigeAwardStragy());//被收藏
        map.put(PointActionType.WIKI_PAGE_AGREE, new PrestigeAwardStragy());//被点赞
        map.put(PointActionType.WIKI_PAGE_SHORT_COMMENTS, new PrestigeAwardStragy());//被短评
        map.put(PointActionType.WIKI_PAGE_COMMENT, new PrestigeAwardStragy()); //被评论
        map.put(PointActionType.CANCEL_WIKI_PAGE_COLLECT, new PrestigeAwardStragy()); //取消收藏
        map.put(PointActionType.CANCEL_FOLLOW, new PrestigeAwardStragy()); //减少粉丝

    }

    //积分
    static {
        pointMap.put(PointActionType.PHONE_LOGIN_AND_BIND, 500);
        pointMap.put(PointActionType.OTHER_LOGIN_AND_BIND, 100);
        pointMap.put(PointActionType.WANBA_SIGN, 40);
        pointMap.put(PointActionType.WANBA_REPLY, 5);
        pointMap.put(PointActionType.WANBA_AGREE, 1);
        pointMap.put(PointActionType.WANBA_SHARE, 10);
        pointMap.put(PointActionType.WIKI_CREATE_PAGE, 100);
        pointMap.put(PointActionType.WIKI_MODIFY_PAGE, 50);
        pointMap.put(PointActionType.WIKI_CREATE_SHORT_COMMENTS, 5);
        pointMap.put(PointActionType.WIKI_THANKS_AUTHOR, 5);
        pointMap.put(PointActionType.FOLLOW_USER, 5);
        pointMap.put(PointActionType.FOLLOW_WIKI, 5);
        pointMap.put(PointActionType.WORSHIP, 50);//膜拜1次 减50分
    }

    ///声望
    static {
        prestigeMap.put(PointActionType.WORSHIP, 20);//膜拜
        prestigeMap.put(PointActionType.THANKS, 10); //感谢
        prestigeMap.put(PointActionType.FANS, 5); //粉丝
        prestigeMap.put(PointActionType.WIKI_PAGE_COLLECT, 5);//被收藏
        prestigeMap.put(PointActionType.WIKI_PAGE_AGREE, 2);//被点赞
        prestigeMap.put(PointActionType.WIKI_PAGE_SHORT_COMMENTS, 2);//被短评
        prestigeMap.put(PointActionType.WIKI_PAGE_COMMENT, 2); //被评论
        prestigeMap.put(PointActionType.WIKI_PAGE_READ, 1); //被阅读
        prestigeMap.put(PointActionType.CANCEL_WIKI_PAGE_COLLECT, -5); //wiki被收藏
        prestigeMap.put(PointActionType.CANCEL_FOLLOW, -5); //减少粉丝


    }


    public static PointAwardStragy getStragyByPointActionType(PointActionType type) {
        return map.get(type);
    }

    public static Integer getPointAwardNum(PointActionType type) {
        return pointMap.get(type);
    }

    public static Integer getPrestigeNum(PointActionType type) {
        return prestigeMap.get(type);
    }


}
