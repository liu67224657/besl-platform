package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-20 下午2:38
 * Description:
 */
public class ExchangeGoodsItemField extends AbstractObjectField {
    public static final ExchangeGoodsItemField EXCHANGE_GOODS_ITEM_ID = new ExchangeGoodsItemField("exchange_goods_item_id", ObjectFieldDBType.LONG, false, true);
    public static final ExchangeGoodsItemField EXCHANGE_GOODS_ID = new ExchangeGoodsItemField("exchange_goods_id", ObjectFieldDBType.LONG, false, true);

    public static final ExchangeGoodsItemField EXCHANGE_STATUS = new ExchangeGoodsItemField("exchange_status", ObjectFieldDBType.STRING, true, false);
    public static final ExchangeGoodsItemField EXCHANGE_TIME = new ExchangeGoodsItemField("exchange_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ExchangeGoodsItemField OWN_USER_NO = new ExchangeGoodsItemField("own_user_no", ObjectFieldDBType.STRING, true, false);
    public static final ExchangeGoodsItemField PROFILEID = new ExchangeGoodsItemField("profileid", ObjectFieldDBType.STRING, true, false);


    public ExchangeGoodsItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ExchangeGoodsItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
