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
public class TicketAwardField extends AbstractObjectField {

    public static final TicketAwardField TICKET_AWARD_ID = new TicketAwardField("ticket_award_id", ObjectFieldDBType.LONG, true, true);
    public static final TicketAwardField TICKET_ID = new TicketAwardField("ticket_id", ObjectFieldDBType.LONG, true, true);
    public static final TicketAwardField AWARD_LEVEL = new TicketAwardField("award_level", ObjectFieldDBType.INT, true, true);
    public static final TicketAwardField AWARD_NAME = new TicketAwardField("award_name", ObjectFieldDBType.STRING, true, true);
    public static final TicketAwardField AWRD_DESC = new TicketAwardField("award_desc", ObjectFieldDBType.STRING, true, true);
    public static final TicketAwardField AWARD_PIC = new TicketAwardField("award_pic", ObjectFieldDBType.STRING, true, true);
    public static final TicketAwardField AWARD_COUNT = new TicketAwardField("award_count", ObjectFieldDBType.INT, true, true);
    public static final TicketAwardField CURRENT_COUNT = new TicketAwardField("current_count", ObjectFieldDBType.INT, true, true);
    public static final TicketAwardField VALIDSTATUS = new TicketAwardField("validstatus", ObjectFieldDBType.STRING, true, true);
    public static final TicketAwardField CREATEUSERID = new TicketAwardField("createuserid", ObjectFieldDBType.STRING, true, true);
    public static final TicketAwardField CREATEIP = new TicketAwardField("createip", ObjectFieldDBType.STRING, true, true);
    public static final TicketAwardField CREATEDATE = new TicketAwardField("createdate", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final TicketAwardField LASTMODIFYUSERID = new TicketAwardField("lastmodifyuserid", ObjectFieldDBType.STRING, true, true);
    public static final TicketAwardField LASTMODIFYIP = new TicketAwardField("lastmodifyip", ObjectFieldDBType.STRING, true, true);
    public static final TicketAwardField LASTMODIFYDATE = new TicketAwardField("lastmodifydate", ObjectFieldDBType.TIMESTAMP, true, true);


    public TicketAwardField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TicketAwardField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
