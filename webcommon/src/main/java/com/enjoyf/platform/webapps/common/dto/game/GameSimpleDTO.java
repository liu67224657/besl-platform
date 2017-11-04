package com.enjoyf.platform.webapps.common.dto.game;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GameSimpleDTO {
    private long gameResouceId;
    private String gameName;
    private String boardId;

    public long getGameResouceId() {
        return gameResouceId;
    }

    public void setGameResouceId(long gameResouceId) {
        this.gameResouceId = gameResouceId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
