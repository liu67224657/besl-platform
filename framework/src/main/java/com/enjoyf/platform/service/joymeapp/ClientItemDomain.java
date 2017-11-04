/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.JsonBinder;
import com.google.common.base.Strings;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: 元素类型
 */
public class ClientItemDomain implements Serializable {
    //content, profile, gameres
    private static Map<Integer, ClientItemDomain> map = new HashMap<Integer, ClientItemDomain>();

    //the content
    public static final ClientItemDomain DEFAULT = new ClientItemDomain(0);    //自定义
    public static final ClientItemDomain CMSARTICLE = new ClientItemDomain(1);  //来源cmsarticle
    public static final ClientItemDomain GAME = new ClientItemDomain(2);   //来源 gamedb
    public static final ClientItemDomain COMMENTBEAN = new ClientItemDomain(3);   //来源 commentbean
    public static final ClientItemDomain PROFILE = new ClientItemDomain(4);    //user_center   profile
    public static final ClientItemDomain JOYMEWIKI = new ClientItemDomain(5);    //joyme_wiki
    //
    private int code;

    private ClientItemDomain(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ClientItemDomain) o).code) return false;

        return true;
    }

    public static ClientItemDomain getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ClientItemDomain> getAll() {
        return map.values();
    }
}
