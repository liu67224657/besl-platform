package com.enjoyf.platform.service.joymeapp.gameclient;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhimingli
 * Date: 2014/12/31
 * Time: 17:02
 */
public class ArchiveContentType implements Serializable {

    private static Map<Integer, ArchiveContentType> map = new HashMap<Integer, ArchiveContentType>();

    public static final ArchiveContentType NEWS_ARCHIVE = new ArchiveContentType(0, "资讯文章");//非视频类 文章
    public static final ArchiveContentType VIDEO_ARCHIVE = new ArchiveContentType(1, "视频文章");//视频类 文章
    public static final ArchiveContentType GUIDE_ARCHIVE = new ArchiveContentType(4, "攻略文章");

    public static final ArchiveContentType WIKI_DATA = new ArchiveContentType(5, "WIKI资料");

    public static final ArchiveContentType MIYOU_COMMENT = new ArchiveContentType(2, "迷友贴子");
    public static final ArchiveContentType VIRTUAL_MIYOU_COMMENT = new ArchiveContentType(3, "迷友虚拟用户发帖");

    private int code;
    private String name;

    private ArchiveContentType(int c, String n) {
        this.code = c;
        this.name = n;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
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


        if (code != ((ArchiveContentType) o).code) return false;

        return true;
    }

    public static ArchiveContentType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ArchiveContentType> getAll() {
        return map.values();
    }
}
