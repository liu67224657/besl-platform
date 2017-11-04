/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.example;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description: the class is used to update or query.
 */
public class ExampleField extends AbstractObjectField {
    //
    public static final ExampleField EXAMPLEID = new ExampleField("EXAMPLEID", ObjectFieldDBType.LONG, false, true);

    public static final ExampleField EXAMPLENAME = new ExampleField("EXAMPLENAME", ObjectFieldDBType.STRING, true, false);
    public static final ExampleField EXAMPLEDISCRIPTION = new ExampleField("EXAMPLEDISCRIPTION", ObjectFieldDBType.STRING, true, false);

    public static final ExampleField CLICKTIMES = new ExampleField("CLICKTIMES", ObjectFieldDBType.INT, true, false);

    public static final ExampleField VALIDSTATUS = new ExampleField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    public static final ExampleField LASTCLICKDATE = new ExampleField("LASTCLICKDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    //
    public ExampleField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ExampleField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
