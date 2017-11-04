/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.weblogic.blogwebsite;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午4:29
 * Description:
 */
public class BlogSelfSetData implements Serializable {
    //
    private String bgImg;

    //
    public BlogSelfSetData() {
    }

    public BlogSelfSetData(String jsonStr) {
        //todo
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public String toJsonStr() {
        //todo
        return null;
    }

    public static BlogSelfSetData parse(String jsonStr) {
        return new BlogSelfSetData(jsonStr);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
