package com.enjoyf.webapps.tools.weblogic.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-21
 * Time: 下午5:50
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppSocialVirtualTimerConstant {
    public static Long DEFAULT = 1 * 2L * 1000L;
    public static Long MINUTES_5 = 5 * 60 * 1000L;
    public static Long MINUTES_10 = 10 * 60 * 1000L;
    public static Long MINUTES_15 = 15 * 60 * 1000L;
    public static Long MINUTES_30 = 30 * 60 * 1000L;

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
