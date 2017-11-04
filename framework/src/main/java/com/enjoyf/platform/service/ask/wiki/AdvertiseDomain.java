package com.enjoyf.platform.service.ask.wiki;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli on 2017-3-22 0022.
 */
public class AdvertiseDomain implements Serializable {
    private static Map<Integer, AdvertiseDomain> map = new HashMap<Integer, AdvertiseDomain>();

    //推荐标签轮播
    public static final AdvertiseDomain CAROUSEL = new AdvertiseDomain(1);


    //推荐列表广告
    public static final AdvertiseDomain ARTICLE_ADVERTISE = new AdvertiseDomain(2);


    private int code;

    public AdvertiseDomain(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AdvertiseDomain: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AdvertiseDomain) o).code) return false;

        return true;
    }

    public static AdvertiseDomain getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AdvertiseDomain> getAll() {
        return map.values();
    }
}
