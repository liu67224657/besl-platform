package com.enjoyf.webapps.joyme.dto.collection;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/6/16.
 */
public class PhpArchiveGameDTO implements Serializable {
    private long gameId;
    private String gameName;

    private long archiveId;
    private String archiveTitle;

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

    public long getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(long archiveId) {
        this.archiveId = archiveId;
    }

    public String getArchiveTitle() {
        return archiveTitle;
    }

    public void setArchiveTitle(String archiveTitle) {
        this.archiveTitle = archiveTitle;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }
}
