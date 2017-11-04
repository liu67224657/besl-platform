package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class GoodsField extends AbstractObjectField {

    public static final GoodsField GOODSID = new GoodsField("goods_id", ObjectFieldDBType.LONG, false, true);
    public static final GoodsField GOODSNAME = new GoodsField("goods_name", ObjectFieldDBType.STRING, true, true);
    public static final GoodsField GOODSDESC = new GoodsField("goods_desc", ObjectFieldDBType.STRING, true, true);
    public static final GoodsField GOODSPIC = new GoodsField("goods_pic", ObjectFieldDBType.STRING, true, true);
    public static final GoodsField GOODSTYPE = new GoodsField("goods_type", ObjectFieldDBType.INT, true, true);

    public static final GoodsField GOODSEXPIREDATE = new GoodsField("goods_expire_date", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final GoodsField GOODSAMOUNT = new GoodsField("goods_amount", ObjectFieldDBType.INT, false, true);
    public static final GoodsField GOODSRESETAMOUNT = new GoodsField("goods_reset_amount", ObjectFieldDBType.INT, false, true);
    public static final GoodsField GOODSCONSUMEPOINT = new GoodsField("goods_consume_point", ObjectFieldDBType.INT, false, true);
    public static final GoodsField CONSUMETIMESTYPE = new GoodsField("consume_times_type", ObjectFieldDBType.INT, false, true);
    public static final GoodsField DISPLAYORDER = new GoodsField("display_order", ObjectFieldDBType.INT, false, true);
    public static final GoodsField ISNEW = new GoodsField("is_new", ObjectFieldDBType.BOOLEAN, false, true);
    public static final GoodsField ISHOT = new GoodsField("is_hot", ObjectFieldDBType.BOOLEAN, false, true);
    public static final GoodsField VALIDSTATUS = new GoodsField("valid_status", ObjectFieldDBType.STRING, false, true);
    public static final GoodsField CREATEDATE = new GoodsField("createdate", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final GoodsField CREATEIP = new GoodsField("createip", ObjectFieldDBType.STRING, false, true);
    public static final GoodsField CREATEUSERID = new GoodsField("createuserid", ObjectFieldDBType.STRING, false, true);
    public static final GoodsField SHAREID = new GoodsField("share_id", ObjectFieldDBType.LONG, false, true);
    public static final GoodsField NOTICEBODY = new GoodsField("notice_body", ObjectFieldDBType.STRING, false, true);
    public static final GoodsField DETAIL_URL = new GoodsField("detail_url", ObjectFieldDBType.STRING, false, true);

    public GoodsField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GoodsField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
