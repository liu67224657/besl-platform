package com.enjoyf.platform.service.point.pointwall;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class AppInstallType implements Serializable {
    private static Map<Integer, AppInstallType> map = new HashMap<Integer, AppInstallType>();
    //已经下载   ---适用于获取
    public static final AppInstallType DOWNLOADED = new AppInstallType(1);
    //已经获得积分
    public static final AppInstallType SCORE_AWARDED = new AppInstallType(2);

    //在5分钟内仍然显示 下载按钮，过了5分钟才显示 获取。
    public static final AppInstallType DOWNLOADED_IN_FIVE = new AppInstallType(3);

    private int code;

    public AppInstallType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ActionType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppInstallType) o).code) return false;

        return true;
    }

    public static AppInstallType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppInstallType> getAll() {
        return map.values();
    }
}
