package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.props.hotdeploy.ToolsHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;

/**
 * * Author: taijunli
 * Date: 12-1-10
 * Time: 下午6:27
 */
public class GameCategory implements Serializable {
    private static ToolsHotdeployConfig hotdeployConfig = HotdeployConfigFactory.get().getConfig(ToolsHotdeployConfig.class);
    private String code;

    public GameCategory() {
    }

    public GameCategory(String c) {
        this.code = c;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GameCategory getByCode(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return null;
        }
        return hotdeployConfig.getCategoryMap().get(code);
    }

    public static Collection<GameCategory> getAll() {
        return hotdeployConfig.getCategoryMap().values();
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GameCategory)) {
            return false;
        }

        return code.equalsIgnoreCase(((GameCategory) obj).getCode());
    }

}
