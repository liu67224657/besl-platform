package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.*;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class GameDeviceSet implements Serializable {
    private static final String SPLIT_STRING = ",";
    private Set<GameDevice> deviceSet = new HashSet<GameDevice>();

    public GameDeviceSet() {

    }

    public Set<GameDevice> getDeviceSet() {
        return deviceSet;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public void setDeviceSet(Set<GameDevice> deviceSet) {
        this.deviceSet = deviceSet;
    }

    public GameDeviceSet add(GameDevice device) {
        deviceSet.add(device);
        return this;
    }

    public String toJsonStr() {
        StringBuffer returnStr = new StringBuffer();
        if (CollectionUtil.isEmpty(deviceSet)) {
            return "";
        }
        int idx = 0;
        for (GameDevice gemeDevice : deviceSet) {
            if (idx > 0) {
                returnStr.append(SPLIT_STRING);
            }
            if (gemeDevice != null) {
                returnStr.append(gemeDevice.getCode());
            }
            idx++;
        }
        return returnStr.toString();
    }

    public static GameDeviceSet parse(String jsonStr) {
        if (StringUtil.isEmpty(jsonStr)) {
            return null;
        }

        GameDeviceSet returnValue = new GameDeviceSet();

        Set<GameDevice> deviceSet = new HashSet<GameDevice>();
        String[] codeArray = jsonStr.split(SPLIT_STRING);
        for (String code : codeArray) {
            GameDevice gameDeviceCode = GameDevice.getByCode(code);
            if (gameDeviceCode != null) {
                deviceSet.add(gameDeviceCode);
            }
        }

        if (CollectionUtil.isEmpty(deviceSet)) {
            return null;
        }
        returnValue.setDeviceSet(deviceSet);
        return returnValue;
    }

    public boolean isChecked(String code) {
        boolean isChecked = false;
        for (GameDevice gameDevice : deviceSet) {
            if (gameDevice.getCode().equals(code)) {
                isChecked = true;
                break;
            }
        }
        return isChecked;
    }
}
