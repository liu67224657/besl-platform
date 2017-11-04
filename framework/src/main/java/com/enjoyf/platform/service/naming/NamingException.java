/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.service.service.ServiceException;


/**
 * A class to define exceptions used by the juser server framework
 * (ie, com.enjoyf.platform.service.juser and com.enjoyf.platform.serv.juser).
 */
@SuppressWarnings("serial")
public class NamingException extends ServiceException {
    //
    public static final int SVC_ALREADY_REGISTERED = ServiceException.BASE_NAMING + 1;
    public static final int SVC_NOT_REGISTERED = ServiceException.BASE_NAMING + 2;
    public static final int UNRECOGNIZED_METRIC = ServiceException.BASE_NAMING + 3;
    public static final int OBJECT_NOT_FOUND = ServiceException.BASE_NAMING + 4;
    public static final int REQUEST_DENIED = ServiceException.BASE_NAMING + 5;
    public static final int REGISTRATION_DISALLOWED = ServiceException.BASE_NAMING + 6;

    public NamingException(int val) {
        this(val, null);
    }
    
    public NamingException(int val, String name) {
    	super(val, name);
    }
}
