/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="erickliu@enjoyfound.com">ericliu</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class WikiResourceField extends AbstractObjectField {
    //RESOURCEID WIKINAME WIKICODE  WIKIURL  ICON   WIKIDESC  REMOVESTATUS   CREATEUSERID  CREATEDATE
    //MODIFYUSERID  MODIFYDATE
    public static final WikiResourceField RESOURCEID = new WikiResourceField("RESOURCEID", ObjectFieldDBType.LONG, false, true);
    public static final WikiResourceField WIKINAME = new WikiResourceField("WIKINAME", ObjectFieldDBType.STRING, false, true);
    public static final WikiResourceField WIKICODE = new WikiResourceField("WIKICODE", ObjectFieldDBType.STRING, false, true);
    public static final WikiResourceField WIKIURL = new WikiResourceField("WIKIURL", ObjectFieldDBType.STRING, true, true);

    public static final WikiResourceField ICON = new WikiResourceField("ICON", ObjectFieldDBType.STRING, true, true);
    public static final WikiResourceField THUMBICON = new WikiResourceField("THUMBICON", ObjectFieldDBType.STRING, true, true);

    public static final WikiResourceField WIKIDESC = new WikiResourceField("WIKIDESC", ObjectFieldDBType.STRING, true, true);
    public static final WikiResourceField REMOVESTATUS = new WikiResourceField("REMOVESTATUS", ObjectFieldDBType.STRING, true, true);

    public static final WikiResourceField TOTALPAGENUM = new WikiResourceField("TOTALPAGENUM", ObjectFieldDBType.INT, true, true);
    public static final WikiResourceField LASTWEEKUPDATEPAGENUM = new WikiResourceField("LASTWEEKUPDATEPAGENUM", ObjectFieldDBType.INT, true, true);
    public static final WikiResourceField LASTUPDATEPAGEDATE = new WikiResourceField("LASTUPDATEPAGEDATE", ObjectFieldDBType.TIMESTAMP, true, true);


    public static final WikiResourceField CREATEUSERID = new WikiResourceField("CREATEUSERID", ObjectFieldDBType.STRING, true, true);
    public static final WikiResourceField CREATEDATE = new WikiResourceField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, true);

    public static final WikiResourceField MODIFYUSERID = new WikiResourceField("LASTMODIFYUSERID", ObjectFieldDBType.STRING, true, true);
    public static final WikiResourceField LASTMODIFYDATE = new WikiResourceField("LASTMODIFYDATE", ObjectFieldDBType.TIMESTAMP, true, true);


    //
    public WikiResourceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public WikiResourceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
