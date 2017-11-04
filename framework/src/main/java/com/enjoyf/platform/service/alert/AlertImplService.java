/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.log.Alert;
import com.enjoyf.platform.util.log.AlertImpl;

public class AlertImplService implements AlertImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(AlertImplService.class);
	
    public void log(Alert alert) {
        AlertService service = AlertServiceSngl.get();

        if (service == null) {
            logger.debug("AlertImplService: service is null!");
        } else {
            //--
            // We look to see if we want to filter out the exception
            // so that it doesn't get logged by the alert server.
            //--
        	//TODO: originally it is by "omitStacktrace". figure out a better way to filter

            service.log(alert);
        }
    }

    public void flush() {
    }

    public void close() {
    }
}