package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeGoodsField extends AbstractObjectField {

    public static final ExchangeGoodsField GOODSID = new ExchangeGoodsField("exchange_goods_id", ObjectFieldDBType.LONG, false, true);
    public static final ExchangeGoodsField GOODSNAME = new ExchangeGoodsField("exchange_goods_name", ObjectFieldDBType.STRING, true, true);
    public static final ExchangeGoodsField GOODSDESC = new ExchangeGoodsField("exchange_goods_desc", ObjectFieldDBType.STRING, true, true);
    public static final ExchangeGoodsField GOODSPIC = new ExchangeGoodsField("exchange_goods_pic", ObjectFieldDBType.STRING, true, true);
    public static final ExchangeGoodsField GOODSTYPE = new ExchangeGoodsField("exchange_goods_type", ObjectFieldDBType.INT, true, true);
    public static final ExchangeGoodsField GOODSTARTDATE = new ExchangeGoodsField("exchange_goods_start_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ExchangeGoodsField GOODSEXPIREDATE = new ExchangeGoodsField("exchange_goods_end_endtime", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final ExchangeGoodsField GOODSAMOUNT = new ExchangeGoodsField("exchange_goods_amount", ObjectFieldDBType.INT, false, true);
    public static final ExchangeGoodsField GOODSRESETAMOUNT = new ExchangeGoodsField("exchange_goods_resetamount", ObjectFieldDBType.INT, false, true);
    public static final ExchangeGoodsField EXCHANGETIMESTYPE = new ExchangeGoodsField("exchange_time_type", ObjectFieldDBType.INT, false, true);
    public static final ExchangeGoodsField EXCHANGEINTRVAL = new ExchangeGoodsField("exchange_intrval", ObjectFieldDBType.INT, false, true);
    public static final ExchangeGoodsField TAOTIMETYPE = new ExchangeGoodsField("tao_time_type", ObjectFieldDBType.INT, false, true);
    public static final ExchangeGoodsField TAOINTRVAL = new ExchangeGoodsField("tao_intrval", ObjectFieldDBType.INT, false, true);

    public static final ExchangeGoodsField DISPLAYORDER = new ExchangeGoodsField("display_order", ObjectFieldDBType.INT, false, true);
    public static final ExchangeGoodsField ISNEW = new ExchangeGoodsField("is_new", ObjectFieldDBType.BOOLEAN, false, true);
    public static final ExchangeGoodsField ISHOT = new ExchangeGoodsField("is_hot", ObjectFieldDBType.BOOLEAN, false, true);
    public static final ExchangeGoodsField VALIDSTATUS = new ExchangeGoodsField("valid_status", ObjectFieldDBType.STRING, false, true);
    public static final ExchangeGoodsField CREATEDATE = new ExchangeGoodsField("createdate", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ExchangeGoodsField CREATEIP = new ExchangeGoodsField("createip", ObjectFieldDBType.STRING, false, true);
    public static final ExchangeGoodsField CREATEUSERID = new ExchangeGoodsField("createuserid", ObjectFieldDBType.STRING, false, true);
    public static final ExchangeGoodsField SHAREID = new ExchangeGoodsField("share_id", ObjectFieldDBType.LONG, false, true);
    public static final ExchangeGoodsField NOTICEBODY = new ExchangeGoodsField("notice_body", ObjectFieldDBType.STRING, false, true);
    public static final ExchangeGoodsField DETAIL_URL = new ExchangeGoodsField("detail_url", ObjectFieldDBType.STRING, false, true);
    public static final ExchangeGoodsField EXCHANGE_GOODS_POINT = new ExchangeGoodsField("exchange_goods_point", ObjectFieldDBType.INT, false, true);

    public ExchangeGoodsField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ExchangeGoodsField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
