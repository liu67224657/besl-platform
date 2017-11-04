package com.enjoyf.webapps.joyme.weblogic.comment;

import com.enjoyf.platform.service.comment.CommentDomain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class CommentFactory {

    private static CommentFactory instance;
    private static Map<Integer, CommentProcessor> map = new HashMap<Integer, CommentProcessor>();

    public static CommentFactory get() {
        if (instance == null) {
            synchronized (CommentFactory.class) {
                if (instance == null) {
                    instance = new CommentFactory();
                }
            }
        }
        return instance;
    }

    public CommentProcessor factory(CommentDomain commentDomain) {
        if (commentDomain == null) {
            return null;
        }
        CommentProcessor returnProcessor = map.containsKey(commentDomain.getCode()) ? map.get(commentDomain.getCode()) : null;
        if (returnProcessor == null) {
            synchronized (map) {
                if (CommentDomain.DIGITAL_SCORE.equals(commentDomain)) {
                    returnProcessor = new DigitalScoreProcessor();
                } else if(CommentDomain.UGCWIKI_COMMENT.equals(commentDomain)){
                    returnProcessor = new WikiReplyProcessor();
                } else if(CommentDomain.DIGITAL_COMMENT.equals(commentDomain)){
                    returnProcessor = new DigitalReplyProcessor();
                } else if(CommentDomain.CMS_COMMENT.equals(commentDomain)){
                    returnProcessor = new CmsReplyProcessor();
                } else if (CommentDomain.GAME_DETIAL.equals(commentDomain)) {
					returnProcessor =new GameReplyProcessor();
				} else if (CommentDomain.JOYME_LIVE.equals(commentDomain)) {
					returnProcessor =new LiveReplyProcessor();
				} else if(CommentDomain.WIKIAPP_COMMENT.equals(commentDomain)){
                    returnProcessor = new WikiReplyProcessor();
                }
            }

            if (returnProcessor != null) {
                map.put(commentDomain.getCode(), returnProcessor);
            }
        }

        return returnProcessor;
    }

}
