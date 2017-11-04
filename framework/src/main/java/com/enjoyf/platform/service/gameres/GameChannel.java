package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * <p/>
 * Description:游戏渠道code数据来源于人不熟配置文件，不要new一个新对象
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class GameChannel implements Serializable{
    private String code;

    public GameChannel(String c) {
        this.code = c.toLowerCase();
    }

    public String getCode() {
        return code;
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
        if ((obj == null) || !(obj instanceof GameChannel)) {
            return false;
        }

        return code.equalsIgnoreCase(((GameChannel) obj).getCode());
    }
}
