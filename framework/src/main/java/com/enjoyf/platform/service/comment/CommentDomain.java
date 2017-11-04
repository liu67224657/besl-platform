/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@fivewh.com">Eric Liu</a>
 * Create time: 11-8-17 下午5:21
 * Description:  外部文章表
 */
public class CommentDomain implements Serializable {
    private static Map<Integer, CommentDomain> map = new HashMap<Integer, CommentDomain>();

    //the original type
    public static final CommentDomain DEFAULT = new CommentDomain(0);
    public static final CommentDomain DIGITAL_COMMENT = new CommentDomain(1);  //数字站评论
    public static final CommentDomain CMS_COMMENT = new CommentDomain(2);    //CMS评论

    //3—短评 4-吐槽
    public static final CommentDomain MOBILE_GAME_COMMENT = new CommentDomain(3);   //手游排行榜评论
    public static final CommentDomain MOBILE_GAME_GAG = new CommentDomain(4);    //手游排行榜吐槽

    //5--数字站评分
    public static final CommentDomain DIGITAL_SCORE = new CommentDomain(5);   //数字站评分

    //6--wiki评论
    public static final CommentDomain UGCWIKI_COMMENT = new CommentDomain(6);    //ugc wiki 评论

    //7-PROFILE_PIC
    public static final CommentDomain GAMECLIENT_MIYOU = new CommentDomain(7);    //玩霸迷友

    public static final CommentDomain WIKI_VOTE = new CommentDomain(8);    //wiki 投票

    public static final CommentDomain ZONGYI_MI = new CommentDomain(9); //综艺迷

    public static final CommentDomain JOYME_LIVE = new CommentDomain(10);//直播间

    public static final CommentDomain GAME_VIDEO = new CommentDomain(11);//游戏录制视频

    public static final CommentDomain UGCWIKI_IMPROVE = new CommentDomain(12);//ugc wiki 完善

    public static final CommentDomain GAME_DETIAL = new CommentDomain(13);//游戏详情页

    public static final CommentDomain WAN_ASK_COMMENT = new CommentDomain(14);//玩霸问答-答案的评论
    public static final CommentDomain WIKIAPP_COMMENT = new CommentDomain(15);//wikiapp里的点评

    private int code;

    private CommentDomain(int c) {
        code = c;

        map.put(code, this);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof CommentDomain)) {
            return false;
        }

        return code == ((CommentDomain) obj).getCode();
    }

    public static CommentDomain getByCode(Integer c) {
        if (c == null) {
            return CommentDomain.DEFAULT;
        }

        return map.get(c) == null ? CommentDomain.DEFAULT : map.get(c);
    }

    public static Collection<CommentDomain> getAll() {
        return map.values();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
