package com.enjoyf.platform.tools.comment;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.service.content.ForignContent;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.content.ForignContentField;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class UpdateCommentController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCommentController.class);

    private static long CONTENT_ID = 68980l;

    private static ContentHandler contentHandler;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            contentHandler = new ContentHandler("content", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }
        importComment();
    }

    private static void importComment() {
        try {
            do {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.leq(ForignContentField.CONTENT_ID, CONTENT_ID));
                queryExpress.add(QueryCriterions.geq(ForignContentField.CONTENT_ID, CONTENT_ID - 1000l));
                queryExpress.add(QueryCriterions.eq(ForignContentField.CONTENT_DOMAIN, ForignContentDomain.CMS.getCode()));
                queryExpress.add(QuerySort.add(ForignContentField.CONTENT_ID, QuerySortOrder.DESC));
                List<ForignContent> contentList = contentHandler.queryForignContent(queryExpress);
                if (!CollectionUtil.isEmpty(contentList)) {
                    for (ForignContent content : contentList) {
                        System.out.println("---------------------------------------" + content.getContentId() + "-----------------------------------------");
                        String url = content.getContentUrl();
                        if (!url.startsWith("http://")) {
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(ForignContentField.CONTENT_URL, "http://" + url);
//                            updateExpress.set(ForignContentField.KEY_WORDS, "");
                            contentHandler.updateForignContent(updateExpress, new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, content.getContentId())));
                        }
                        CONTENT_ID = content.getContentId();
                        System.out.println("---------------------------------------" + CONTENT_ID + "-----------------------------------------");
                    }

                }

            } while (CONTENT_ID > 0l);
        } catch (DbException e) {
            e.printStackTrace();
        }

        //To change body of created methods use File | Settings | File Templates.
    }

}
