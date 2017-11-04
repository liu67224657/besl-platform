package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class GiftLotteryField extends AbstractObjectField {
    public static final GiftLotteryField GIFTLOTTERYID = new GiftLotteryField("gift_lottery_id", ObjectFieldDBType.LONG);
    public static final GiftLotteryField GIFTLOTTERYNAME = new GiftLotteryField("gift_lottery_name", ObjectFieldDBType.STRING);
    public static final GiftLotteryField PROBABILITY = new GiftLotteryField("probability", ObjectFieldDBType.DOUBLE);
    public static final GiftLotteryField RETURN_POINT = new GiftLotteryField("return_point", ObjectFieldDBType.INT);
    public static final GiftLotteryField PIC_NAME = new GiftLotteryField("pic_name", ObjectFieldDBType.STRING);
    public static final GiftLotteryField DIS_PIC_NAME = new GiftLotteryField("dis_pic_name", ObjectFieldDBType.STRING);
    public static final GiftLotteryField PIC_KEY = new GiftLotteryField("pic_key", ObjectFieldDBType.STRING);
    public static final GiftLotteryField LOTTERY_TYPE = new GiftLotteryField("lottery_type", ObjectFieldDBType.INT);


    public GiftLotteryField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}