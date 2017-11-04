package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-4
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
public class ClientLineItemField extends AbstractObjectField {
    public static final ClientLineItemField ITEM_ID = new ClientLineItemField("item_id", ObjectFieldDBType.LONG, false, true);
    public static final ClientLineItemField LINE_ID = new ClientLineItemField("line_id", ObjectFieldDBType.LONG, false, true);
    public static final ClientLineItemField TITLE = new ClientLineItemField("title", ObjectFieldDBType.STRING, false, false);
    public static final ClientLineItemField DESC = new ClientLineItemField("linedesc", ObjectFieldDBType.STRING, false, false);

    public static final ClientLineItemField PIC_URL = new ClientLineItemField("pic_url", ObjectFieldDBType.STRING, false, false);
    public static final ClientLineItemField URL = new ClientLineItemField("url", ObjectFieldDBType.STRING, false, false);

    public static final ClientLineItemField DIRECT_ID = new ClientLineItemField("direct_id", ObjectFieldDBType.STRING, false, false);

    public static final ClientLineItemField ITEM_TYPE = new ClientLineItemField("item_type", ObjectFieldDBType.INT, false, false);
    public static final ClientLineItemField ITEM_DOMAIN = new ClientLineItemField("item_domain", ObjectFieldDBType.INT, false, false);
    public static final ClientLineItemField REDIRCT_TYPE = new ClientLineItemField("app_redirect_type", ObjectFieldDBType.INT, false, false);
    public static final ClientLineItemField ITEM_CREATE_DATE = new ClientLineItemField("item_create_date", ObjectFieldDBType.TIMESTAMP, false, false);

    public static final ClientLineItemField DISPLAY_ORDER = new ClientLineItemField("display_order", ObjectFieldDBType.INT, false, false);

    public static final ClientLineItemField VALID_STATUS = new ClientLineItemField("valid_status", ObjectFieldDBType.STRING, false, false);
    public static final ClientLineItemField RATE = new ClientLineItemField("rate", ObjectFieldDBType.STRING, false, false);
    public static final ClientLineItemField APPDISPLAYTYPE = new ClientLineItemField("app_display_type", ObjectFieldDBType.INT, false, false);
    public static final ClientLineItemField AUTHOR = new ClientLineItemField("author", ObjectFieldDBType.STRING, false, false);
    public static final ClientLineItemField CATEGORY = new ClientLineItemField("category", ObjectFieldDBType.STRING, false, false);
    public static final ClientLineItemField CATEGORY_COLOR = new ClientLineItemField("category_color", ObjectFieldDBType.STRING, false, false);
    public static final ClientLineItemField ITEM_PARAM = new ClientLineItemField("item_param", ObjectFieldDBType.STRING, false, false);
    public static final ClientLineItemField DISPLAYTYPE = new ClientLineItemField("is_hot", ObjectFieldDBType.INT, false, false);
    public static final ClientLineItemField STATE_DATE = new ClientLineItemField("state_date", ObjectFieldDBType.TIMESTAMP, false, false);

	public static final ClientLineItemField CONTENTID = new ClientLineItemField("contentid", ObjectFieldDBType.LONG, false, false);


    //
    public ClientLineItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ClientLineItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
