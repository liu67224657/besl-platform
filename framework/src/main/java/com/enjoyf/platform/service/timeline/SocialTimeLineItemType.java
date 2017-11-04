/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class SocialTimeLineItemType implements Serializable {
    private static Map<Integer, SocialTimeLineItemType> map = new HashMap<Integer, SocialTimeLineItemType>();

    public static final SocialTimeLineItemType CREATE_WIKI = new SocialTimeLineItemType(0);//创建wiki
    public static final SocialTimeLineItemType CREATE_PAGE = new SocialTimeLineItemType(1);//创建页面
    public static final SocialTimeLineItemType UPDATE_PAGE = new SocialTimeLineItemType(2);//修改页面
    public static final SocialTimeLineItemType UPLOAD_FILE = new SocialTimeLineItemType(3);//上传图片
    public static final SocialTimeLineItemType FOLLOW_WIKI = new SocialTimeLineItemType(4);//关注wiki
    public static final SocialTimeLineItemType FOLLOW_PROFILE = new SocialTimeLineItemType(5);//关注人
    public static final SocialTimeLineItemType IMPROVE_PAGE = new SocialTimeLineItemType(6);//完善
    public static final SocialTimeLineItemType REPLY = new SocialTimeLineItemType(7);//回复
    public static final SocialTimeLineItemType COLLECT_PAGE = new SocialTimeLineItemType(8);//收藏
    public static final SocialTimeLineItemType AT = new SocialTimeLineItemType(9);//@
    public static final SocialTimeLineItemType AGREE = new SocialTimeLineItemType(10);//赞
    public static final SocialTimeLineItemType UNFOLLOW_PROFILE = new SocialTimeLineItemType(11);//取消关注人
    public static final SocialTimeLineItemType FOLLOW_PAGE = new SocialTimeLineItemType(12);//关注页面

    private int code;

    public SocialTimeLineItemType(int c) {
        code = c;

        map.put(code, this);
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
        return "SocialTimeLineItemType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SocialTimeLineItemType)) {
            return false;
        }

        return code==(((SocialTimeLineItemType) obj).getCode());
    }

    public static SocialTimeLineItemType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<SocialTimeLineItemType> getAll() {
        return map.values();
    }
}
