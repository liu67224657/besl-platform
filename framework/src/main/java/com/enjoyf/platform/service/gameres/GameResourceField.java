/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import org.apache.taglibs.standard.lang.jpath.adapter.StatusIterationContext;

/**
 * @Auther: <a mailto="erickliu@enjoyfound.com">ericliu</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class GameResourceField extends AbstractObjectField {
    //
    public static final GameResourceField RESOURCEID = new GameResourceField("RESOURCEID", ObjectFieldDBType.LONG, true, true);

    public static final GameResourceField CATEGORYID = new GameResourceField("CATEGORYID", ObjectFieldDBType.INT, true, false);
    public static final GameResourceField RESOURCENAME = new GameResourceField("RESOURCENAME", ObjectFieldDBType.STRING, true, true);
    public static final GameResourceField GAMECODE = new GameResourceField("GAMECODE", ObjectFieldDBType.STRING, true, true);
    public static final GameResourceField RESOURCEDESC = new GameResourceField("RESOURCEDESC", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField ICON = new GameResourceField("ICON", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField THUMBIMG = new GameResourceField("THUMBIMG", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField SCREENSHOT = new GameResourceField("SCREENSHOT", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField RESOURCEURL = new GameResourceField("RESOURCEURL", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField DEVELOP = new GameResourceField("DEVELOP", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField PUBLISHCOMPANY = new GameResourceField("PUBLISHCOMPANY", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField RESOURCECATEGORY = new GameResourceField("RESOURCECATEGORY", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField DEVICE = new GameResourceField("DEVICE", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField OPERATIONSUTATUS = new GameResourceField("OPERATIONSUTATUS", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField PUBLISHDATE = new GameResourceField("PUBLISHDATE", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField SYNONYMS = new GameResourceField("SYNONYMS", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField PLAYERNUMBER = new GameResourceField("PLAYERNUMBER", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField PLAYTIME = new GameResourceField("PLAYTIME", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField REFERID = new GameResourceField("REFERID", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField PRICE = new GameResourceField("PRICE", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField RESOURCEVERSION = new GameResourceField("RESOURCEVERSION", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField FILESIZE = new GameResourceField("FILESIZE", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField LANGUAGE = new GameResourceField("LANGUAGE", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField CURRENTRATINGCOUNT = new GameResourceField("CURRENTRATINGCOUNT", ObjectFieldDBType.INT, true, false);
    public static final GameResourceField CURRENTRATING = new GameResourceField("CURRENTRATING", ObjectFieldDBType.FLOAT, true, false);
    public static final GameResourceField TOTALRATING = new GameResourceField("TOTALRATING", ObjectFieldDBType.FLOAT, true, false);
    public static final GameResourceField TOTALRATINGCOUNT = new GameResourceField("TOTALRATINGCOUNT", ObjectFieldDBType.INT, true, false);

    //
    public static final GameResourceField REMOVESTATUS = new GameResourceField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField CREATEDATE = new GameResourceField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    //
    public static final GameResourceField LASTMODIFYDATE = new GameResourceField("LASTMODIFYDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GameResourceField LASTMODIFYUSERID = new GameResourceField("LASTMODIFYUSERID", ObjectFieldDBType.STRING, true, false);

    public static final GameResourceField RESOURCEDOMAIN = new GameResourceField("RESOURCEDOMAIN", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField RESOURCESTYLE = new GameResourceField("RESOURCESTYLE", ObjectFieldDBType.STRING, true, false);

    public static final GameResourceField LOGOSIZE = new GameResourceField("LOGOSIZE", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField SEOKEYWORDS = new GameResourceField("SEOKEYWORDS", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField SEODESCRIPTION = new GameResourceField("SEODESCRIPTION", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField SHOWVALUE = new GameResourceField("SHOWVALUE", ObjectFieldDBType.INT, true, false);
    public static final GameResourceField PLAYINGSUM = new GameResourceField("PLAYINGSUM", ObjectFieldDBType.INT, true, false);
    public static final GameResourceField PLAYEDSUM = new GameResourceField("PLAYEDSUM", ObjectFieldDBType.INT, true, false);

    public static final GameResourceField EVENTDESC = new GameResourceField("EXTSTR01", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField SCORE = new GameResourceField("SCORE", ObjectFieldDBType.STRING, true, false);

    public static final GameResourceField LASTUPDATEDATE = new GameResourceField("LASTUPDATEDATE", ObjectFieldDBType.STRING, true, false);
    public static final GameResourceField BUYLINK = new GameResourceField("BUYLINK", ObjectFieldDBType.STRING, true, false);


    //
    public GameResourceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GameResourceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
