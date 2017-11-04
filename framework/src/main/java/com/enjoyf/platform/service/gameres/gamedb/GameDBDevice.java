package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-12-3
 * Time: 下午9:33
 * To change this template use File | Settings | File Templates.
 */
public class GameDBDevice implements Serializable {
    private Long gameDbDeviceId;     //设备ID  1 iphone  2ipad  3android
    private boolean gameDbDeviceStatus;

    public Long getGameDbDeviceId() {
        return gameDbDeviceId;
    }

    public void setGameDbDeviceId(Long gameDbDeviceId) {
        this.gameDbDeviceId = gameDbDeviceId;
    }

    public boolean isGameDbDeviceStatus() {
        return gameDbDeviceStatus;
    }

    public void setGameDbDeviceStatus(boolean gameDbDeviceStatus) {
        this.gameDbDeviceStatus = gameDbDeviceStatus;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
