package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-3-21
 * Time: 上午10:56
 * To change this template use File | Settings | File Templates.
 */
public class StatItemField extends AbstractObjectField {

    public static final StatItemField ITEM_ID = new StatItemField("ITEMID", ObjectFieldDBType.LONG, true, true);
    public static final StatItemField DOMAIN = new StatItemField("DOMAIN", ObjectFieldDBType.STRING, true, false);
    public static final StatItemField DOMAINNAME = new StatItemField("DOMAINNAME", ObjectFieldDBType.STRING, true, false);
    public static final StatItemField SECTION = new StatItemField("SECTION", ObjectFieldDBType.STRING, true, false);
    public static final StatItemField SECTIONNAME = new StatItemField("SECTIONNAME", ObjectFieldDBType.STRING, true, false);
    public static final StatItemField DATETYPE = new StatItemField("DATETYPE", ObjectFieldDBType.STRING, true, false);
    public static final StatItemField STATDATE = new StatItemField("STATDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final StatItemField STATVALUE = new StatItemField("STATVALUE", ObjectFieldDBType.LONG, true, false);
    public static final StatItemField EXTDATA = new StatItemField("EXTDATA", ObjectFieldDBType.STRING, true, false);
    public static final StatItemField REPORTDATE = new StatItemField("REPORTDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final StatItemField PUBLISHID = new StatItemField("PUBLISHID", ObjectFieldDBType.STRING, true, false);
    public static final StatItemField URL = new StatItemField("URL", ObjectFieldDBType.STRING, true, false);

    public StatItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public StatItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
