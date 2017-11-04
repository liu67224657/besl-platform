/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-3-14 下午2:03
 * Description:
 */
public class IdNamePair {
    //
    private int id;

    //
    private String name;

    public IdNamePair(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
