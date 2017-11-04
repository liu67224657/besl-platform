/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.util.FiveProps;

/**
 * Interface to be implemented by servers that want to start up
 * inside a container (see serv.container package).
 */
public interface ServiceInitializer {
    /**
     * Perform any initialization required prior to starting things up
     * and return the ServiceConfig object to be used by framework for
     * starting up the service.
     */
    public ServiceConfig init(FiveProps props);

    /**
     * Invoked after the service has been started.
     */
    public void postStart();
}
