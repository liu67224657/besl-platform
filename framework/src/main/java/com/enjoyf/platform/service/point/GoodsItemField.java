package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class GoodsItemField extends AbstractObjectField {

    public static final GoodsItemField GOODSITEM_ID = new GoodsItemField("goods_item_id", ObjectFieldDBType.LONG, true, true);
    public static final GoodsItemField GOODSID = new GoodsItemField("goods_id", ObjectFieldDBType.LONG, true, false);
    public static final GoodsItemField EXCHANGE_STATUS = new GoodsItemField("exchange_status", ObjectFieldDBType.STRING, true, false);
    public static final GoodsItemField OWN_UNO = new GoodsItemField("own_uno", ObjectFieldDBType.STRING, true, false);
    public static final GoodsItemField EXCHANGE_DATE = new GoodsItemField("exchange_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GoodsItemField PROFILEID = new GoodsItemField("profileid", ObjectFieldDBType.STRING, true, false);


    public GoodsItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GoodsItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }


}
