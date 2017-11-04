/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="erickliu@enjoyfound.com">ericliu</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class HotContentField implements ObjectField {
    //
    private static Map<String, HotContentField> map = new HashMap<String, HotContentField>();

    //CONTENTID CONTENTUNO HOTTYPE CONTENTTYPE CONTENTTAG SEARCHTAG PUBLISHTYPE HOTRATE  HOTDATE

    public static final HotContentField HOTTYPE = new HotContentField("HOTTYPE", ObjectFieldDBType.STRING, true, false);
    public static final HotContentField CONTENTTYPE = new HotContentField("CONTENTTYPE", ObjectFieldDBType.STRING, true, false);
    public static final HotContentField CONTENTTAG = new HotContentField("CONTENTTAG", ObjectFieldDBType.STRING, true, false);
    public static final HotContentField SEARCHTAG = new HotContentField("SEARCHTAG", ObjectFieldDBType.STRING, true, false);
    public static final HotContentField PUBLISHTYPE = new HotContentField("PUBLISHTYPE", ObjectFieldDBType.STRING, true, false);

    public static final HotContentField HOTRATE = new HotContentField("HOTRATE", ObjectFieldDBType.INT, true, false);
    public static final HotContentField HOTDATE = new HotContentField("HOTDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    private String column;
    private ObjectFieldDBType type;
    private boolean modify = false;
    private boolean uniquene = false;

    //
    public HotContentField(String cn, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        this.column = cn;
        this.type = type;
        this.modify = modify;
        this.uniquene = uniquene;

        map.put(this.column, this);
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public ObjectFieldDBType getType() {
        return type;
    }

    @Override
    public boolean isModify() {
        return modify;
    }

    @Override
    public boolean isUniquene() {
        return uniquene;
    }

    @Override
    public int hashCode() {
        return column.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof HotContentField)) {
            return false;
        }

        return column.equalsIgnoreCase(((HotContentField) obj).getColumn());
    }

    public static HotContentField getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }
}
