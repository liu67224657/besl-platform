/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class ContentField extends AbstractObjectField {
    //
    //CONTENTID,UNO,CONTENTSUBJECT,CONTENTTAG,CONTENTBODY,IMAGES,AUDIOS,VIDEOS,THUMBIMGLINK,
    //PUBLISHTYPE,CONTENTTYPE,CONTENTPRIVACY,PUBLISHDATE,PUBLISHIP,
    // ROOTCONTENTID,ROOTCONTENTUNO, PARENTCONTENTID, PARENTCONTENTUNO,
    //REPLYTIMES,FORWARDTIMES,FAVORTIMES,UPDATEDATE,REMOVESTATUS
    public static final ContentField CONTENTID = new ContentField("CONTENTID", ObjectFieldDBType.STRING, false, false);
    public static final ContentField UNO = new ContentField("UNO", ObjectFieldDBType.STRING, true, false);
    public static final ContentField CONTENTSUBJECT = new ContentField("CONTENTSUBJECT", ObjectFieldDBType.STRING, true, false);
    public static final ContentField CONTENTTAG = new ContentField("CONTENTTAG", ObjectFieldDBType.STRING, true, false);
    public static final ContentField CONTENTBODY = new ContentField("CONTENTBODY", ObjectFieldDBType.STRING, true, false);
    public static final ContentField IMAGES = new ContentField("IMAGES", ObjectFieldDBType.STRING, true, false);
    public static final ContentField AUDIOS = new ContentField("AUDIOS", ObjectFieldDBType.STRING, true, false);
    public static final ContentField VIDEOS = new ContentField("VIDEOS", ObjectFieldDBType.STRING, true, false);
    public static final ContentField APPS = new ContentField("APPS", ObjectFieldDBType.STRING, true, false);
    public static final ContentField GAMES = new ContentField("GAMES", ObjectFieldDBType.STRING, true, false);

    public static final ContentField THUMBIMGLINK = new ContentField("THUMBIMGLINK", ObjectFieldDBType.STRING, true, false);
    public static final ContentField CONTENTTYPE = new ContentField("CONTENTTYPE", ObjectFieldDBType.INT, true, false);
    public static final ContentField PUBLISHTYPE = new ContentField("PUBLISHTYPE", ObjectFieldDBType.STRING, true, false);
    public static final ContentField PUBLISHDATE = new ContentField("PUBLISHDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public static final ContentField REPLYTIMES = new ContentField("REPLYTIMES", ObjectFieldDBType.INT, true, false);
    public static final ContentField FORWARDTIMES = new ContentField("FORWARDTIMES", ObjectFieldDBType.INT, true, false);
    public static final ContentField FAVORTIMES = new ContentField("FAVORTIMES", ObjectFieldDBType.INT, true, false);


    //
    public static final ContentField DINGIMES = new ContentField("EXTNUM01", ObjectFieldDBType.INT, true, false);//顶
    public static final ContentField CAITIMES = new ContentField("EXTNUM02", ObjectFieldDBType.INT, true, false);//踩
    public static final ContentField VIEWTIMES = new ContentField("EXTNUM03", ObjectFieldDBType.INT, true, false);//踩
    public static final ContentField FLOORTIMES = new ContentField("EXTNUM04", ObjectFieldDBType.INT, true, false);//总楼数

    //
    public static final ContentField LASTREPLYID = new ContentField("EXTSTR01", ObjectFieldDBType.STRING, true, false);//last reply id
//    public static final ContentField RESOURCEID = new ContentField("EXTSTR02", ObjectFieldDBType.STRING, true, false);//resource id

    //
    public static final ContentField UPDATEDATE = new ContentField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ContentField REMOVESTATUS = new ContentField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false); //伪删除标记,xiuyujia 2011-12-07
    public static final ContentField AUDITSTATUS = new ContentField("AUDITSTATUS", ObjectFieldDBType.INT, true, false);//审核类型 2011-12-07

    //
    public ContentField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ContentField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
