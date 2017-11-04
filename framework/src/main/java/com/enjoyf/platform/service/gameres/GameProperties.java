package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-18
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class GameProperties implements Serializable{
    private Long resourceId;
    private List<GameProperty> channels =new ArrayList<GameProperty>();
    private GameProperty updateInfo;

    public List<GameProperty> getChannels() {
        return channels;
    }

    public void setChannels(List<GameProperty> channels) {
        this.channels = channels;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public GameProperty getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(GameProperty updateInfo) {
        this.updateInfo = updateInfo;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
