package com.enjoyf.platform.service.gameclient.dto;

import com.enjoyf.platform.service.comment.ProfilePicDTO;
import com.enjoyf.platform.service.gameres.gamedb.GameDBSimpleDTO;
import com.enjoyf.platform.service.usercenter.GameClientProfileDTO;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-1-20
 * Time: 上午9:27
 * To change this template use File | Settings | File Templates.
 */
public class GameClientPicDTO implements Serializable{

    private ProfilePicDTO picdto;
    private GameClientProfileDTO profiledto;
    private GameDBSimpleDTO gamedto;

    public ProfilePicDTO getPicdto() {
        return picdto;
    }

    public void setPicdto(ProfilePicDTO picdto) {
        this.picdto = picdto;
    }

    public GameClientProfileDTO getProfiledto() {
        return profiledto;
    }

    public void setProfiledto(GameClientProfileDTO profiledto) {
        this.profiledto = profiledto;
    }

    public GameDBSimpleDTO getGamedto() {
        return gamedto;
    }

    public void setGamedto(GameDBSimpleDTO gamedto) {
        this.gamedto = gamedto;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
