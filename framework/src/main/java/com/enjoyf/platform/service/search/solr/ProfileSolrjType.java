package com.enjoyf.platform.service.search.solr;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class ProfileSolrjType implements Serializable {

    private static Map<Integer, ProfileSolrjType> map = new HashMap<Integer, ProfileSolrjType>();

    //昵称、签名联合搜索
    public static final ProfileSolrjType NICKNAME = new ProfileSolrjType(1);

    //正在玩的游戏
    public static final ProfileSolrjType GAMES = new ProfileSolrjType(2);

    //地址类
    public static final ProfileSolrjType ADDRESS = new ProfileSolrjType(3);

    //年龄
    public static final ProfileSolrjType AGE = new ProfileSolrjType(4);
    //
    private int code;

    private ProfileSolrjType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ProfileSolrjType:code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ProfileSolrjType) o).code) return false;

        return true;
    }

    public static ProfileSolrjType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ProfileSolrjType> getAll() {
        return map.values();
    }
}
