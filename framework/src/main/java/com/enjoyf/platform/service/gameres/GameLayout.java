package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-3
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
public class GameLayout implements Serializable {

    private long resourceId;
    private GameLayoutSetting gameLayoutSetting;

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public GameLayoutSetting getGameLayoutSetting() {
        return gameLayoutSetting;
    }

    public void setGameLayoutSetting(GameLayoutSetting gameLayoutSetting) {
        this.gameLayoutSetting = gameLayoutSetting;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
