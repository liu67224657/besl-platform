/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: view category configure.
 */
public class ViewCategoryConfig implements Serializable {
    //
    public static final String KEY_DEFAULT_LOCATION_CODE = "def";

    //the category aspect．
    private ViewCategoryAspect categoryAspect;

    //
    private boolean openToPublic = false;

    //
    public ViewCategoryConfig(ViewCategoryAspect categoryAspect) {
        this.categoryAspect = categoryAspect;
    }

    public ViewCategoryAspect getCategoryAspect() {
        return categoryAspect;
    }

    public boolean isOpenToPublic() {
        return openToPublic;
    }

    public void setOpenToPublic(boolean openToPublic) {
        this.openToPublic = openToPublic;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
