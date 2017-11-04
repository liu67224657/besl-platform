package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/6/15.
 */
public class GameBrand implements Serializable {

    private int id;
    private String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static GameBrand parse(String jsonStr) {
        GameBrand gameBrand = null;
        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                gameBrand = JsonBinder.buildNormalBinder().getMapper().enableDefaultTyping().readValue(jsonStr, new TypeReference<GameBrand>() {
                });
                gameBrand.setIcon(gameBrand.getIcon());
            } catch (IOException e) {
                GAlerter.lab("GameBrand parse occur Exception.e", e);
            }
        }
        return gameBrand;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

}
