/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db;

import com.enjoyf.platform.service.service.ServiceException;


/**
 * A class to define exceptions used by the juser server framework
 * (ie, com.enjoyf.platform.service.juser and com.enjoyf.platform.serv.juser).
 */

@SuppressWarnings("serial")
public class DbLockException extends ServiceException {
	
    public static final int INVALID_DBLOCK_ID = ServiceException.BASE_DB + 1;
    public static final int LOCK_NOT_FOUND = ServiceException.BASE_DB + 2;
    public static final int LOCK_IN_USE = ServiceException.BASE_DB + 3;


    public DbLockException(int val, String name) {
        super(val, name);
    }
}


