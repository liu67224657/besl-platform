package com.enjoyf.webapps.tools.weblogic.dto.game;

import com.enjoyf.platform.service.gameres.GamePrivacy;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-4-25
 * Time: 下午2:55
 * To change this template use File | Settings | File Templates.
 */
public class GamePrivacyDTO {
    private GamePrivacy gamePrivacy;
    private String screenName;

    public GamePrivacy getGamePrivacy() {
        return gamePrivacy;
    }

    public void setGamePrivacy(GamePrivacy gamePrivacy) {
        this.gamePrivacy = gamePrivacy;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
