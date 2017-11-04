package com.enjoyf.webapps.joyme.dto.joymeapp;

import com.enjoyf.platform.service.joymeapp.AppCategoryType;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-7-30 下午6:16
 * Description:
 */
public class AppCategoryDTO {
    private boolean awesome;
    private boolean booming;
    private boolean classic;
    private boolean newGame;
    private boolean hot;

    public boolean isAwesome() {
        return awesome;
    }

    public void setAwesome(boolean awesome) {
        this.awesome = awesome;
    }

    public boolean isBooming() {
        return booming;
    }

    public void setBooming(boolean booming) {
        this.booming = booming;
    }

    public boolean isClassic() {
        return classic;
    }

    public void setClassic(boolean classic) {
        this.classic = classic;
    }

    public boolean isNewGame() {
        return newGame;
    }

    public void setNewGame(boolean newGame) {
        this.newGame = newGame;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public static AppCategoryDTO getByAppCategroy(AppCategoryType appCategory) {
        AppCategoryDTO appCategoryDTO = new AppCategoryDTO();
        if (appCategory == null) {
            return appCategoryDTO;
        }

        appCategoryDTO.setAwesome(appCategory.hasAwesome());
        appCategoryDTO.setBooming(appCategory.hasBooming());
        appCategoryDTO.setClassic(appCategory.hasClassic());
        appCategoryDTO.setNewGame(appCategory.hasNewGame());
        appCategoryDTO.setHot(appCategory.hasHot());

        return appCategoryDTO;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
