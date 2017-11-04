package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.util.sql.ObjectField;

import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
@Deprecated
public class GameSumIncreaseEvent extends SystemEvent {
    private String gameResourceId;
    private Map<ObjectField, Object> sumFieldMap;

    public GameSumIncreaseEvent() {
        super(SystemEventType.GAME_SUM_INCREASE);
    }

    public String getGameResourceId() {
        return gameResourceId;
    }

    public void setGameResourceId(String gameResourceId) {
        this.gameResourceId = gameResourceId;
    }

    public Map<ObjectField, Object> getSumFieldMap() {
        return sumFieldMap;
    }

    public void setSumFieldMap(Map<ObjectField, Object> sumFieldMap) {
        this.sumFieldMap = sumFieldMap;
    }

}
