package com.enjoyf.platform.serv.viewline.viewlineitemprocessor;

import com.enjoyf.platform.serv.viewline.ViewLineLogic;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.event.system.ViewLineItemRemoveEvent;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-8
 * Time: 上午9:47
 * To change this template use File | Settings | File Templates.
 */
public class RemoveHotContentProcessor implements ViewLineItemRemoveProcessor {

    private ViewLineLogic viewLineLogic;

    private int hotContentViewLineId = 0;
    private static final String CATEGORY_INDEX_TALK = "talk";
    private static final String HOT_CONTENT_LINE_LOCATIONCODE = "hot";

    public RemoveHotContentProcessor(ViewLineLogic viewLineLogic) {
        this.viewLineLogic = viewLineLogic;
    }

    @Override
    public void process(ViewLineItemRemoveEvent removeEvent) {
        try {
            //从话版移除文章要移除热门文章的文章
            if (removeEvent.getViewCategoryAspect().equals(ViewCategoryAspect.CONTENT_BOARD)) {
                initHotContentIds();

                if (hotContentViewLineId == 0) {
                    GAlerter.lab("hot content viewline is empty.");
                    return;
                }

                UpdateExpress removeHotUpdateExpress = new UpdateExpress()
                        .set(ViewLineItemField.VALIDSTATUS, ValidStatus.REMOVED.getCode());
                QueryExpress removeHotQueryExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ViewLineItemField.DIRECTID, removeEvent.getDirectId()))
                        .add(QueryCriterions.eq(ViewLineItemField.LINEID, hotContentViewLineId));

                viewLineLogic.modifyLineItem(removeHotUpdateExpress, removeHotQueryExpress);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " processRemoveLineItem occured Exception.", e);
        }
    }

    private void initHotContentIds() throws Exception {
        if (hotContentViewLineId == 0) {
            QueryExpress getTalkCategoryExpress = new QueryExpress();
            getTalkCategoryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYASPECT, ViewCategoryAspect.CUSTOM_INDEX.getCode()));
            getTalkCategoryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, CATEGORY_INDEX_TALK));
            ViewCategory talkIndexCategory = viewLineLogic.getCategory(getTalkCategoryExpress);
            if (talkIndexCategory == null) {
                GAlerter.lab(this.getClass().getName() + "talk index category empty.");
                return;
            }

            QueryExpress getHotLineExpress = new QueryExpress();
            getHotLineExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, talkIndexCategory.getCategoryId()));
            getHotLineExpress.add(QueryCriterions.eq(ViewLineField.LOCATIONCODE, HOT_CONTENT_LINE_LOCATIONCODE));
            ViewLine hotContentLine = viewLineLogic.getViewLine(getHotLineExpress);
            if (hotContentLine == null) {
                GAlerter.lab(this.getClass().getName() + "talk index hotcontent line empty.");
                return;
            }
            hotContentViewLineId = hotContentLine.getLineId();
        }
    }
}
