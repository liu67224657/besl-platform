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
public class GameStyleSet implements Serializable {
    private static final String SPLIT_STRING = ",";
    private Set<GameStyle> styleSet = new HashSet<GameStyle>();

    public GameStyleSet() {

    }

    public GameStyleSet add(GameStyle style) {
        styleSet.add(style);
        return this;
    }

    public GameStyleSet add(Collection<GameStyle> style) {
        styleSet.addAll(style);
        return this;
    }

    public String toJsonStr() {
        StringBuffer returnStr = new StringBuffer();
        if (CollectionUtil.isEmpty(styleSet)) {
            return "";
        }
        int idx = 0;
        for (GameStyle gameStyle : styleSet) {
            if (idx > 0) {
                returnStr.append(SPLIT_STRING);
            }
            if (gameStyle != null) {
                returnStr.append(gameStyle.getCode());
            }
            idx++;
        }
        return returnStr.toString();
    }

    public static GameStyleSet parse(String jsonStr) {
        if (StringUtil.isEmpty(jsonStr)) {
            return null;
        }

        GameStyleSet returnValue = new GameStyleSet();

        Set<GameStyle> styleSet = new HashSet<GameStyle>();
        String[] codeArray = jsonStr.split(SPLIT_STRING);
        for (String code : codeArray) {
            GameStyle gameStyle = GameStyle.getByCode(code);
            if (gameStyle != null) {
                styleSet.add(gameStyle);
            }
        }

        if (CollectionUtil.isEmpty(styleSet)) {
            return null;
        }
        returnValue.add(styleSet);
        return returnValue;
    }

    public boolean isChecked(String code) {
        boolean isChecked = false;
        for (GameStyle gameStyle : styleSet) {
            if (gameStyle.getCode().equals(code)) {
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

    public Set<GameStyle> getStyleSet() {
        return styleSet;
    }

    public void setStyleSet(Set<GameStyle> styleSet) {
        this.styleSet = styleSet;
    }
}
