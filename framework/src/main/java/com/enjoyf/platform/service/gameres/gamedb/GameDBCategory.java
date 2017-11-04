package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-12-3
 * Time: 上午10:37
 * To change this template use File | Settings | File Templates.
 */
public class GameDBCategory implements Serializable {
    private long gameDbCategoryId;
    private boolean gameDbCategoryStatus;

    public long getGameDbCategoryId() {
        return gameDbCategoryId;
    }

    public void setGameDbCategoryId(long gameDbCategoryId) {
        this.gameDbCategoryId = gameDbCategoryId;
    }

    public boolean isGameDbCategoryStatus() {
        return gameDbCategoryStatus;
    }

    public void setGameDbCategoryStatus(boolean gameDbCategoryStatus) {
        this.gameDbCategoryStatus = gameDbCategoryStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
