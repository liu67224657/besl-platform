package com.enjoyf.platform.service.viewline;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-6-7
 * Time: 下午5:57
 * To change this template use File | Settings | File Templates.
 */
public class LocationCode implements Serializable {

    private static Map<String, LocationCode> map = new HashMap<String, LocationCode>();

    public static final LocationCode DEFAULT = new LocationCode("def");
    public static final LocationCode TALK_BOARD = new LocationCode("talk");
    public static final LocationCode ESS_BOARD = new LocationCode("ess");
    public static final LocationCode GAME_LINK = new LocationCode("gamelink");

    public static final LocationCode DOWNLOAD_LINK = new LocationCode("downloadlink");
    public static final LocationCode GROUP_CONTENT_LINK = new LocationCode("groupcontentlink");
    public static final LocationCode COUSTOM_MODULE = new LocationCode("cmodule");

    private String code;

    private LocationCode(String code){
        this.code = code.toLowerCase();
        map.put(code, this);
    }

    public static LocationCode getByCode(String code){
        if(Strings.isNullOrEmpty(code)){
            return null;
        }
        return map.get(code.toLowerCase());
    }

    public static Collection<LocationCode> getAll(){
        return map.values();
    }

    public String getCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationCode that = (LocationCode) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LocationCode: code=" + code;
    }
}
