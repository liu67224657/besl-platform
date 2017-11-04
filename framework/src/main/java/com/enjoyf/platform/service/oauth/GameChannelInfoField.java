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
public class GameChannelInfoField extends AbstractObjectField {

    public static final GameChannelInfoField INFOID = new GameChannelInfoField("info_id", ObjectFieldDBType.STRING);

    public static final GameChannelInfoField APPKEY = new GameChannelInfoField("appkey", ObjectFieldDBType.STRING);
    public static final GameChannelInfoField PLATFORM = new GameChannelInfoField("platform", ObjectFieldDBType.INT);
    public static final GameChannelInfoField CHANNEL = new GameChannelInfoField("channel", ObjectFieldDBType.STRING);


    public static final GameChannelInfoField CHANNELKEY = new GameChannelInfoField("channelkey", ObjectFieldDBType.STRING);
    public static final GameChannelInfoField CHANNELAPPID = new GameChannelInfoField("channelappid", ObjectFieldDBType.STRING);
    public static final GameChannelInfoField CHANNELSECR = new GameChannelInfoField("channelsecr", ObjectFieldDBType.STRING);
    public static final GameChannelInfoField PUBLICKEY = new GameChannelInfoField("publickey", ObjectFieldDBType.STRING);
    public static final GameChannelInfoField PRIVATEKEY = new GameChannelInfoField("privatekey", ObjectFieldDBType.STRING);


    public static final GameChannelInfoField LASTMODIFY_TIME = new GameChannelInfoField("lastmodify_time", ObjectFieldDBType.TIMESTAMP);
    public static final GameChannelInfoField LASTMODIFY_IP = new GameChannelInfoField("lastmodify_ip", ObjectFieldDBType.STRING);
    public static final GameChannelInfoField LASTMODIFY_USERID = new GameChannelInfoField("lastmodify_userid", ObjectFieldDBType.STRING);

    public GameChannelInfoField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
