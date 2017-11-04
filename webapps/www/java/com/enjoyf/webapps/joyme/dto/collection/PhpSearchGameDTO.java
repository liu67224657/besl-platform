package com.enjoyf.webapps.joyme.dto.collection;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/6/16.
 */
public class PhpSearchGameDTO implements Serializable {
    private long gameId;
    private String gameName;
    private String wikikey;


    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getWikikey() {
        return wikikey;
    }

    public void setWikikey(String wikikey) {
        this.wikikey = wikikey;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }
}
