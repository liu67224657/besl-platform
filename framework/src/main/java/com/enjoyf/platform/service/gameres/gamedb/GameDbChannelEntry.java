package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-21 下午4:23
 * Description:
 */
public class GameDbChannelEntry implements Serializable {
    private GameDBChannel gameDBChannel;
    private GameDBChannelInfo gameDBChannelInfo;

    public GameDBChannel getGameDBChannel() {
        return gameDBChannel;
    }

    public void setGameDBChannel(GameDBChannel gameDBChannel) {
        this.gameDBChannel = gameDBChannel;
    }

    public GameDBChannelInfo getGameDBChannelInfo() {
        return gameDBChannelInfo;
    }

    public void setGameDBChannelInfo(GameDBChannelInfo gameDBChannelInfo) {
        this.gameDBChannelInfo = gameDBChannelInfo;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
