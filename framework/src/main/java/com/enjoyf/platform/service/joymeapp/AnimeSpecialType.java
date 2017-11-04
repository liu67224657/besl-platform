package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class AnimeSpecialType implements Serializable {

    private static Map<Integer, AnimeSpecialType> map = new HashMap<Integer, AnimeSpecialType>();
    public static Map<Integer, String> mapValue = new HashMap<Integer, String>();

	public static final AnimeSpecialType ANALYSIS = new AnimeSpecialType(1, "分析");
	public static final AnimeSpecialType TV = new AnimeSpecialType(2, "视频");
	public static final AnimeSpecialType COS = new AnimeSpecialType(3, "COS");
	public static final AnimeSpecialType ACTIVITY = new AnimeSpecialType(4, "活动");
	public static final AnimeSpecialType MUSIC = new AnimeSpecialType(5, "音乐");
	public static final AnimeSpecialType AROUND = new AnimeSpecialType(6, "周边");
	public static final AnimeSpecialType SPECIAL = new AnimeSpecialType(7, "专题");
	public static final AnimeSpecialType ZA = new AnimeSpecialType(8, "杂谈");
	public static final AnimeSpecialType PIC = new AnimeSpecialType(9, "图片");
	public static final AnimeSpecialType FUNNY = new AnimeSpecialType(10, "搞笑");
	public static final AnimeSpecialType MODEL = new AnimeSpecialType(11, "模型");

	public static final AnimeSpecialType HAND  = new AnimeSpecialType(12, "手办");

    private int code;
    private String value;

    private AnimeSpecialType(int c, String value) {
        this.code = c;
        this.value = value;
        map.put(code, this);
        mapValue.put(c, value);
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AnimeSpecialType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AnimeSpecialType) o).code) return false;

        return true;
    }

    public static AnimeSpecialType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AnimeSpecialType> getAll() {
        return map.values();
    }
}
