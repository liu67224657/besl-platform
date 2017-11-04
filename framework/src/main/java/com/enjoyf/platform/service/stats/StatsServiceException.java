package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.service.service.ServiceException;

@SuppressWarnings("serial")
public class StatsServiceException extends ServiceException {
	
    public static final int NO_DB_HANDLER = ServiceException.BASE_STATS + 1;

    protected StatsServiceException(int val, String name) {
        super(val, name);
    }
}
