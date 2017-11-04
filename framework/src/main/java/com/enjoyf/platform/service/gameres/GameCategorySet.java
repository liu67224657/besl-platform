package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class GameCategorySet implements Serializable {
    private static final String SPLIT_STRING = ",";
    private Set<GameCategory> categorySet = new HashSet<GameCategory>();

    public GameCategorySet() {

    }

    public GameCategorySet add(GameCategory category) {
        categorySet.add(category);
        return this;
    }

    public GameCategorySet add(Collection<GameCategory> category) {
        categorySet.addAll(category);
        return this;
    }

    public String toJsonStr() {
        StringBuffer returnStr = new StringBuffer();
        if (CollectionUtil.isEmpty(categorySet)) {
            return "";
        }
        int idx = 0;
        for (GameCategory gameCategory : categorySet) {
            if (idx > 0) {
                returnStr.append(SPLIT_STRING);
            }
            if (gameCategory != null) {
                returnStr.append(gameCategory.getCode());
            }
            idx++;
        }
        return returnStr.toString();
    }

    public static GameCategorySet parse(String jsonStr) {
        if (StringUtil.isEmpty(jsonStr)) {
            return null;
        }
        GameCategorySet returnValue = new GameCategorySet();

        Set<GameCategory> categorySet = new HashSet<GameCategory>();
        String[] codeArray = jsonStr.split(SPLIT_STRING);
        for (String code : codeArray) {
            GameCategory gameCategory = GameCategory.getByCode(code);
            if (gameCategory != null) {
                categorySet.add(gameCategory);
            }
        }

        if (CollectionUtil.isEmpty(categorySet)) {
            return null;
        }
        returnValue.add(categorySet);
        return returnValue;
    }

    public boolean isChecked(String code) {
        boolean isChecked = false;
        for (GameCategory gameCategory : categorySet) {
            if (gameCategory.getCode().equals(code)) {
                isChecked = true;
                break;
            }
        }
        return isChecked;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public Set<GameCategory> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(Set<GameCategory> categorySet) {
        this.categorySet = categorySet;
    }
}
