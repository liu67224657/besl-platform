package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.gameres.gamedb.GameDBModifyTimeFieldJson;

/**
 * Created by zhimingli
 * Date: 2015/01/16
 * Time: 11:58
 */
public class GameAgreeEvent extends SystemEvent {
    private long gamedbId;
    private String key;

    public GameAgreeEvent() {
        super(SystemEventType.GAMEDB_PC_AGREE_EVENT);
    }

    public long getGamedbId() {
        return gamedbId;
    }

    public void setGamedbId(long gamedbId) {
        this.gamedbId = gamedbId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
