package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.gameres.gamedb.GameDBModifyTimeFieldJson;
import com.enjoyf.platform.util.sql.ObjectField;

import java.util.Map;

/**
 * Created by zhimingli
 * Date: 2015/01/16
 * Time: 11:58
 */
public class GameDBModifyTimeEvent extends SystemEvent {
    private long gamedbFileId;
    private GameDBModifyTimeFieldJson modifyTimeFieldJson;

    public GameDBModifyTimeEvent() {
        super(SystemEventType.GAMEDB_MODIFYTIME_EVENT);
    }

    public long getGamedbFileId() {
        return gamedbFileId;
    }

    public void setGamedbFileId(long gamedbFileId) {
        this.gamedbFileId = gamedbFileId;
    }

    public GameDBModifyTimeFieldJson getModifyTimeFieldJson() {
        return modifyTimeFieldJson;
    }

    public void setModifyTimeFieldJson(GameDBModifyTimeFieldJson modifyTimeFieldJson) {
        this.modifyTimeFieldJson = modifyTimeFieldJson;
    }
}
