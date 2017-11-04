package com.enjoyf.platform.tools.comment;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.service.content.ForignContent;
import com.enjoyf.platform.service.content.ForignContentDomain;
import com.enjoyf.platform.service.content.ForignContentField;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-7-2
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class UpdateForignContentController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateForignContentController.class);

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
                queryExpress.add(QueryCriterions.eq(ForignContentField.CONTENT_DOMAIN, ForignContentDomain.WIKI_CONTENT.getCode()));
                queryExpress.add(QuerySort.add(ForignContentField.CONTENT_ID, QuerySortOrder.DESC));
                List<ForignContent> contentList = contentHandler.queryForignContent(queryExpress);
                if (!CollectionUtil.isEmpty(contentList)) {
                    for (ForignContent content : contentList) {
                        System.out.println("---------------------------------------" + content.getContentId() + "-----------------------------------------");
                        String url = URLDecoder.decode(content.getContentUrl(), "utf-8");
                        String title = url.substring(url.lastIndexOf("/") + 1, url.length());
                        UpdateExpress updateExpress = new UpdateExpress();
                        updateExpress.set(ForignContentField.CONTENT_URL, url);
                        updateExpress.set(ForignContentField.CONTENT_TITLE, title);
                        contentHandler.updateForignContent(updateExpress, new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, content.getContentId())));
                    }

                }

            } while (CONTENT_ID > 0l);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //To change body of created methods use File | Settings | File Templates.
    }

}
