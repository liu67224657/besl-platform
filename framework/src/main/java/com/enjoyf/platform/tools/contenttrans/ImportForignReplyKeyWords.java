package com.enjoyf.platform.tools.contenttrans;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-3-3
 * Time: 上午11:33
 * To change this template use File | Settings | File Templates.
 */
public class ImportForignReplyKeyWords {

    private static final Logger logger = LoggerFactory.getLogger(ImportForignReplyKeyWords.class);

    private static ContentHandler contentHandler;

    private static Map<Long, String> contentMap = new HashMap<Long, String>();

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            contentHandler = new ContentHandler("content", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }
        updateContentKeyWords();
        updateReplyKeyWords();
    }

    private static void updateReplyKeyWords() {
        try {
            List<ForignContentReply> list = queryForignContentReply(contentHandler);
            for (ForignContentReply reply : list) {
                logger.info("================begin import data===================== " + reply.getReplyId());
                updateForignContentReply(contentHandler, reply.getReplyId(), contentMap.get(reply.getContentId()));
                logger.info("================finish import data===================== " + reply.getReplyId());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static boolean updateForignContentReply(ContentHandler contentHandler, long replyId, String keyWords) throws DbException {
        return contentHandler.updateForignContentReply(new UpdateExpress().set(ForignContentReplyField.KEY_WORDS, keyWords),
                new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, replyId)));
    }

    private static List<ForignContentReply> queryForignContentReply(ContentHandler contentHandler) throws DbException {
        return contentHandler.queryForignContentReply(new QueryExpress());
    }

    private static void updateContentKeyWords() {
        try {
            List<ForignContent> list = queryForignContent(contentHandler);
            for (ForignContent content : list) {
                logger.info("================begin import data===================== " + content.getContentId());
                String url = content.getContentUrl();
                String keyWords = "";
                if (url.indexOf("http://www.joyme.com/wiki/") >= 0) {
                    keyWords = url.replaceAll("http://www.joyme.com/wiki/", "").replaceAll("/" + content.getForignId(), "").trim();
                } else {
                    keyWords = url.replaceAll("http://", "").replaceAll(".joyme.com/", "").replaceAll(content.getForignId(), "").trim();
                }
                updateForignContent(contentHandler, content.getContentId(), keyWords);
                contentMap.put(content.getContentId(), keyWords);
                logger.info("================finish import data===================== " + content.getContentId());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private static boolean updateForignContent(ContentHandler contentHandler, long contentId, String keyWords) throws DbException {
        return contentHandler.updateForignContent(new UpdateExpress().set(ForignContentField.KEY_WORDS, keyWords),
                new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, contentId)));
    }

    private static List<ForignContent> queryForignContent(ContentHandler contentHandler) throws DbException {
        return contentHandler.queryForignContent(new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_DOMAIN, ForignContentDomain.WIKI.getCode())));
    }


}
