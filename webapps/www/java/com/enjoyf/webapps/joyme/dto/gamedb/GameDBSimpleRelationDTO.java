package com.enjoyf.webapps.joyme.dto.gamedb;

import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.social.ObjectRelation;

import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/17
 * Description:
 */
public class GameDBSimpleRelationDTO {
    private GameDBSimpleDTO game;
    private int relationstatus = IntValidStatus.UNVALID.getCode();

    public GameDBSimpleDTO getGame() {
        return game;
    }

    public void setGame(GameDBSimpleDTO game) {
        this.game = game;
    }

    public int getRelationstatus() {
        return relationstatus;
    }

    public void setRelationstatus(int relationstatus) {
        this.relationstatus = relationstatus;
    }
}
