package com.enjoyf.platform.service.point;


import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by zhitaoshi on 2015/7/23.
 */
public class GoodsSeckillField extends AbstractObjectField {
    public static final GoodsSeckillField SECKILL_ID = new GoodsSeckillField("seckillid", ObjectFieldDBType.LONG, true, true);
    public static final GoodsSeckillField GOODS_ID = new GoodsSeckillField("goodsid", ObjectFieldDBType.STRING, true, false);
    public static final GoodsSeckillField SECKILL_TITLE = new GoodsSeckillField("seckilltitle", ObjectFieldDBType.STRING, true, false);
    public static final GoodsSeckillField SECKILL_DESC = new GoodsSeckillField("seckilldesc", ObjectFieldDBType.STRING, true, false);
    public static final GoodsSeckillField SECKILL_PIC = new GoodsSeckillField("seckillpic", ObjectFieldDBType.STRING, true, false);
    public static final GoodsSeckillField START_TIME = new GoodsSeckillField("starttime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GoodsSeckillField END_TIME = new GoodsSeckillField("endtime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GoodsSeckillField SECKILL_TOTAL = new GoodsSeckillField("seckilltotal", ObjectFieldDBType.INT, true, false);
    public static final GoodsSeckillField SECKILL_TIPS = new GoodsSeckillField("seckilltips", ObjectFieldDBType.STRING, true, false);
    public static final GoodsSeckillField GOODS_ACTION_TYPE = new GoodsSeckillField("goodsactiontype", ObjectFieldDBType.INT, true, false);

    public static final GoodsSeckillField REMOVE_STATUS = new GoodsSeckillField("removestatus", ObjectFieldDBType.INT, true, false);

    public static final GoodsSeckillField CREATE_DATE = new GoodsSeckillField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GoodsSeckillField CREATE_USER = new GoodsSeckillField("createuser", ObjectFieldDBType.STRING, true, false);
    public static final GoodsSeckillField MODIFY_DATE = new GoodsSeckillField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GoodsSeckillField MODIFY_USER = new GoodsSeckillField("modifyuser", ObjectFieldDBType.STRING, true, false);
    public static final GoodsSeckillField SECKILL_SUM = new GoodsSeckillField("seckillsum", ObjectFieldDBType.INT, true, false);

    public GoodsSeckillField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GoodsSeckillField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
