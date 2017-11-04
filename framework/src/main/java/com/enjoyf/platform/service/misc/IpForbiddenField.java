package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-4-6
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
public class IpForbiddenField extends AbstractObjectField {

    public static final IpForbiddenField IPID = new IpForbiddenField("IPID", ObjectFieldDBType.LONG, true, true);
    public static final IpForbiddenField STARTIP = new IpForbiddenField("STARTIP", ObjectFieldDBType.LONG, true, false);
    public static final IpForbiddenField ENDIP = new IpForbiddenField("ENDIP", ObjectFieldDBType.LONG, true, false);
    public static final IpForbiddenField CREATEDATE = new IpForbiddenField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final IpForbiddenField CREATEUSERID = new IpForbiddenField("CREATEUSERID", ObjectFieldDBType.STRING, true, false);
    public static final IpForbiddenField UTILLDATE = new IpForbiddenField("UTILLDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final IpForbiddenField DESC = new IpForbiddenField("DESCRIPTION", ObjectFieldDBType.STRING, true, false);
    public static final IpForbiddenField VALIDSTATUS = new IpForbiddenField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);

    //Constructors
    public IpForbiddenField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }
    public IpForbiddenField(String column, ObjectFieldDBType type){
        super(column, type);
    }

}
