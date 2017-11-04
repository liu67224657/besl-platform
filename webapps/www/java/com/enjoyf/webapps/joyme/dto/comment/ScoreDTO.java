package com.enjoyf.webapps.joyme.dto.comment;

import com.enjoyf.platform.util.PageRows;
import org.apache.kahadb.page.Page;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 下午4:07
 * To change this template use File | Settings | File Templates.
 */
public class ScoreDTO {

    private ScoreEntity score;
    private PageRows<MainReplyDTO> mainreplys;

    public ScoreEntity getScore() {
        return score;
    }

    public void setScore(ScoreEntity score) {
        this.score = score;
    }

    public PageRows<MainReplyDTO> getMainreplys() {
        return mainreplys;
    }

    public void setMainreplys(PageRows<MainReplyDTO> mainreplys) {
        this.mainreplys = mainreplys;
    }
}
