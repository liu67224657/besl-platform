/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:58
 * Description: 将各种相互关系放在一张表中。并把两个人间的相互关系用一条记录来记录，比方说关注和粉丝。
 */
public class SocialRelationField extends AbstractObjectField {

    public static final SocialInviteDetailField RELATIONID = new SocialInviteDetailField("RELATIONID", ObjectFieldDBType.LONG, false, true);
    public static final SocialInviteDetailField RELATIONTYPE = new SocialInviteDetailField("RELATIONTYPE", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField SRCUNO = new SocialInviteDetailField("SRCUNO", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField DESTUNO = new SocialInviteDetailField("DESTUNO", ObjectFieldDBType.STRING, false, false);
    public static final SocialInviteDetailField DESCRIPTION = new SocialInviteDetailField("DESCRIPTION", ObjectFieldDBType.STRING, true, false);
    public static final SocialInviteDetailField RELATIONRATE = new SocialInviteDetailField("RELATIONRATE", ObjectFieldDBType.INT, true, false);
    public static final SocialInviteDetailField SRCSTATUS = new SocialInviteDetailField("SRCSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final SocialInviteDetailField DESTSTATUS = new SocialInviteDetailField("DESTSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final SocialInviteDetailField SRCDATE = new SocialInviteDetailField("SRCDATE", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final SocialInviteDetailField DESTDATE = new SocialInviteDetailField("DESTDATE", ObjectFieldDBType.TIMESTAMP, false, false);


    //
    public SocialRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
