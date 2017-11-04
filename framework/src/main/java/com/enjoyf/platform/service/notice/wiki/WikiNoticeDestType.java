/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.notice.wiki;

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
public class WikiNoticeDestType implements Serializable {
    private static Map<Integer, WikiNoticeDestType> map = new HashMap<Integer, WikiNoticeDestType>();


    //social client
    public static final WikiNoticeDestType REPLY = new WikiNoticeDestType(1); //发表评论
    public static final WikiNoticeDestType REPLY_ME = new WikiNoticeDestType(2);//回复我
    public static final WikiNoticeDestType REPLY_OTHER = new WikiNoticeDestType(3);//回复其他人
    public static final WikiNoticeDestType AGREE_WIKI= new WikiNoticeDestType(4);//赞了wiki
    public static final WikiNoticeDestType AGREE_ME= new WikiNoticeDestType(5);//赞了我
    public static final WikiNoticeDestType THANKS= new WikiNoticeDestType(6);//感谢
    public static final WikiNoticeDestType WORSHIP= new WikiNoticeDestType(7); //膜拜
    public static final WikiNoticeDestType PRESTIGE= new WikiNoticeDestType(8); //声望排行
    public static final WikiNoticeDestType CANCEL_FOLLOW= new WikiNoticeDestType(9); //取消关注
    public static final WikiNoticeDestType FOLLOW= new WikiNoticeDestType(10); //关注

    private int code;

    private WikiNoticeDestType(int c) {
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
        return "WikiNoticeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof WikiNoticeDestType)) {
            return false;
        }

        return code==(((WikiNoticeDestType) obj).getCode());
    }

    public static WikiNoticeDestType getByCode(int c) {


        return map.get(c);
    }

    public static Collection<WikiNoticeDestType> getAll() {
        return map.values();
    }
}
