package com.enjoyf.platform.webapps.common.dto.index;

import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public class IndexGameContentDTO {
    private List<ViewLineItemElementDTO> contents;//头条
    private ViewLineItemElementDTO game; //结伴玩
    private int toDayNum;

    public List<ViewLineItemElementDTO> getContents() {
        return contents;
    }

    public void setContents(List<ViewLineItemElementDTO> contents) {
        this.contents = contents;
    }

    public ViewLineItemElementDTO getGame() {
        return game;
    }

    public void setGame(ViewLineItemElementDTO game) {
        this.game = game;
    }

    public int getToDayNum() {
        return toDayNum;
    }

    public void setToDayNum(int toDayNum) {
        this.toDayNum = toDayNum;
    }
}
