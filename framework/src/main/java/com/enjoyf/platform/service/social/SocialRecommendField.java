/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:58
 * Description: 将各种相互关系放在一张表中。并把两个人间的相互关系用一条记录来记录，比方说关注和粉丝。
 */
public class SocialRecommendField extends AbstractObjectField {

    public static final SocialInviteDetailField SRCUNO = new SocialInviteDetailField("SRCUNO", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField RECOMMENDTYPE = new SocialInviteDetailField("RECOMMENDTYPE", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField DETAIL = new SocialInviteDetailField("DETAIL", ObjectFieldDBType.STRING, true, false);
    public static final SocialInviteDetailField CALDATE = new SocialInviteDetailField("CALDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialInviteDetailField CALSTATUS = new SocialInviteDetailField("CALSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final SocialInviteDetailField BLACKDETAIL = new SocialInviteDetailField("BLACKDETAIL", ObjectFieldDBType.STRING, true, false);

    //
    public SocialRecommendField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialRecommendField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
