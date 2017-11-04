package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * <p/>
 * Description:游戏渠道code数据来源于人不熟配置文件，不要new一个新对象
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GameDBChannel implements Serializable {

    private long channelId;
    private String channelName;
    private String channelCode;

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        if (channelId != ((GameDBChannel) obj).channelId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) channelId;
    }
}
