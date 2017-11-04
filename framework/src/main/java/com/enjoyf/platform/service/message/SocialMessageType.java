package com.enjoyf.platform.service.message;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午3:43
 * To change this template use File | Settings | File Templates.
 */
public class SocialMessageType implements Serializable {
    private static Map<Integer, SocialMessageType> map = new HashMap<Integer, SocialMessageType>();
    //打开应用（纯文本等）不做跳转
    public static final SocialMessageType DEFAULT = new SocialMessageType(0);
    //agree message
    public static final SocialMessageType AGREE = new SocialMessageType(1);

    //reply message
    public static final SocialMessageType REPLY = new SocialMessageType(2);

    //hot content
    public static final SocialMessageType NOTICE = new SocialMessageType(3);

    //open conetnt
    public static final SocialMessageType OPEN_CONETNT = new SocialMessageType(4);

    //open activity
    public static final SocialMessageType OPEN_ACTIVITY = new SocialMessageType(5);

    //open wap html
    public static final SocialMessageType OPEN_WAP_HTML = new SocialMessageType(6);

    public static final SocialMessageType HOT = new SocialMessageType(7);

    public static final SocialMessageType FOCUS = new SocialMessageType(8);

    //玩霸消息列表
    public static final SocialMessageType WANBA_MESSAGE_LIST = new SocialMessageType(9);


    private int code;

    public SocialMessageType(int c) {
        this.code = c;

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
        return "NoticeType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SocialMessageType)) {
            return false;
        }

        return code == (((SocialMessageType) obj).getCode());
    }

    public static SocialMessageType getByCode(Integer c) {
        if (c == null) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<SocialMessageType> getAll() {
        return map.values();
    }
}
