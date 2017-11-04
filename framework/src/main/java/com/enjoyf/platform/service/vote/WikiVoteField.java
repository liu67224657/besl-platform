package com.enjoyf.platform.service.vote;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-20
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
public class WikiVoteField extends AbstractObjectField {

    public static final WikiVoteField ARTICLE_ID = new WikiVoteField("_id", ObjectFieldDBType.LONG, true, true);
    public static final WikiVoteField NAME = new WikiVoteField("name", ObjectFieldDBType.STRING, true, false);
    public static final WikiVoteField URL = new WikiVoteField("url", ObjectFieldDBType.STRING, true, false);
    public static final WikiVoteField PIC = new WikiVoteField("pic", ObjectFieldDBType.STRING, true, false);
    public static final WikiVoteField NOSTR = new WikiVoteField("nostr", ObjectFieldDBType.STRING, true, false);
    public static final WikiVoteField KEYWORDS = new WikiVoteField("keywords", ObjectFieldDBType.STRING, true, false);
    public static final WikiVoteField VOTESSUM = new WikiVoteField("votessum", ObjectFieldDBType.INT, true, false);
    public static final WikiVoteField CREATEDATE = new WikiVoteField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final WikiVoteField MODIFYDATE = new WikiVoteField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final WikiVoteField REMOVESTATUS = new WikiVoteField("removestatus", ObjectFieldDBType.STRING, true, false);

    public WikiVoteField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public WikiVoteField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
