package com.enjoyf.webapps.joyme.dto;

import com.enjoyf.platform.service.gameres.ResourceDomain;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-25
 * Time: 下午3:42
 * To change this template use File | Settings | File Templates.
 */
public class RelationMiniResourceDTO {
    private String gameCode;
    private String gameName;
    private String icon;
    private ResourceDomain resourceDomain = ResourceDomain.GROUP;
    //
    public RelationMiniResourceDTO(){

    }

    // getter and setter

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String code) {
        this.gameCode = code;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ResourceDomain getResourceDomain() {
        return resourceDomain;
    }

    public void setResourceDomain(ResourceDomain resourceDomain) {
        this.resourceDomain = resourceDomain;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
