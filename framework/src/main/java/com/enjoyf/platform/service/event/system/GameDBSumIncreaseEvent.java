package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.gameres.gamedb.GameDBModifyTimeFieldJson;

/**
 * Created by zhimingli
 * Date: 2015/01/16
 * Time: 11:58
 */
public class GameDBSumIncreaseEvent extends SystemEvent {
    private long gameId;
    private GameDBField gameDBField;
    private int increateValue;

    public GameDBSumIncreaseEvent() {
        super(SystemEventType.GAMEDB_SUM_INCREASE_EVENT);
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public int getIncreateValue() {
        return increateValue;
    }

    public void setIncreateValue(int increateValue) {
        this.increateValue = increateValue;
    }

    public GameDBField getGameDBField() {
        return gameDBField;
    }

    public void setGameDBField(GameDBField gameDBField) {
        this.gameDBField = gameDBField;
    }
}
