/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class GameChannelConfigField extends AbstractObjectField {

    public static final GameChannelConfigField CONFIGID = new GameChannelConfigField("config_id", ObjectFieldDBType.STRING);
    public static final GameChannelConfigField APPKEY = new GameChannelConfigField("appkey", ObjectFieldDBType.STRING);
    public static final GameChannelConfigField PLATFORM = new GameChannelConfigField("platform", ObjectFieldDBType.INT);
    public static final GameChannelConfigField CHANNEL = new GameChannelConfigField("channel", ObjectFieldDBType.STRING);
    public static final GameChannelConfigField VERSION = new GameChannelConfigField("version", ObjectFieldDBType.STRING);
    public static final GameChannelConfigField ISDEBUG = new GameChannelConfigField("isdebug", ObjectFieldDBType.BOOLEAN);


    public static final GameChannelConfigField LASTMODIFY_TIME = new GameChannelConfigField("lastmodify_time", ObjectFieldDBType.TIMESTAMP);
    public static final GameChannelConfigField LASTMODIFY_IP = new GameChannelConfigField("lastmodify_ip", ObjectFieldDBType.STRING);
    public static final GameChannelConfigField LASTMODIFY_USERID = new GameChannelConfigField("lastmodify_userid", ObjectFieldDBType.STRING);

    public GameChannelConfigField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
