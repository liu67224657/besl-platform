package com.enjoyf.platform.service.viewline;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class ViewCategoryType {

    private static Map<String, ViewCategoryType> map = new HashMap<String, ViewCategoryType>();

    //树枝
    public static final ViewCategoryType CATEGORY_BRANCH = new ViewCategoryType("branch");
    //树叶
    public static final ViewCategoryType CATEGORY_LEAF = new ViewCategoryType("leaf");


    private String code;

    private ViewCategoryType(String code){
        this.code = code.toLowerCase();
        map.put(code, this);
    }

    public static ViewCategoryType getByCode(String code){
        if(Strings.isNullOrEmpty(code)){
            return null;
        }
        return map.get(code.toLowerCase());
    }

    public static Collection<ViewCategoryType> getAll(){
        return map.values();
    }

    public String getCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewCategoryType that = (ViewCategoryType) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ViewCategoryType: code=" + code;
    }
    
}
