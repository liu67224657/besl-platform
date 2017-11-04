package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 13-7-12
 * Time: 上午10:03
 * To change this template use File | Settings | File Templates.
 */
public class TicketField extends AbstractObjectField {

    public static final TicketField TICKET_ID = new TicketField("ticket_id", ObjectFieldDBType.LONG, true, true);
    public static final TicketField TICKET_NAME = new TicketField("ticket_name", ObjectFieldDBType.STRING, true, true);
    public static final TicketField TICKET_DESC = new TicketField("ticket_desc", ObjectFieldDBType.STRING, true, true);
    public static final TicketField BASE_RATE = new TicketField("base_rate", ObjectFieldDBType.INT, true, true);
    public static final TicketField CURR_NUM = new TicketField("curr_num", ObjectFieldDBType.INT, true, true);
    public static final TicketField AWARD_LEVEL_COUNT = new TicketField("award_level_count", ObjectFieldDBType.INT, true, true);
    public static final TicketField VALIDSTATUS = new TicketField("validstatus", ObjectFieldDBType.STRING, true, true);
    public static final TicketField WIN_TYPE = new TicketField("win_type", ObjectFieldDBType.INT, true, true);
    public static final TicketField WIN_CRONEXP = new TicketField("wincronexp", ObjectFieldDBType.STRING, true, true);
    public static final TicketField START_TIME = new TicketField("start_time", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final TicketField END_TIME = new TicketField("end_time", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final TicketField CREATEUSERID = new TicketField("createuserid", ObjectFieldDBType.STRING, true, true);
    public static final TicketField CREATEIP = new TicketField("createip", ObjectFieldDBType.STRING, true, true);
    public static final TicketField CREATEDATE = new TicketField("createdate", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final TicketField LASTMODIFYUSERID = new TicketField("lastmodifyuserid", ObjectFieldDBType.STRING, true, true);
    public static final TicketField LASTMODIFYIP = new TicketField("lastmodifyip", ObjectFieldDBType.STRING, true, true);
    public static final TicketField LASTMODIFYDATE = new TicketField("lastmodifydate", ObjectFieldDBType.TIMESTAMP, true, true);


    public TicketField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TicketField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
