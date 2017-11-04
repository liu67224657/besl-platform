/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.util.FiveProps;

/**
 * Interface to define what our factory bridge class looks like.
 */
public interface FactoryImpl {

    NamingService create(String name);

    /**
     * Create a naming service.
     *
     * @param name The hostname where the service is found.
     * @param port The port for the service.
     */
    NamingService create(String name, int port);

    /**
     * Use properties to decide how to create a naming service.
     */
    NamingService createFromProps(FiveProps props);

    /**
     * Create a registrant object. Clients should call this routine
     * to create the registrant, and then call start() on it to
     * register with the naming service.
     *
     * @param info The info describing the service to be registered.
     */
    Registrant createRegistrant(ServiceInfo info);

    /**
     * Create a registrant object from a properties file.
     */
    Registrant createRegistrant(String sectionName, FiveProps props);
}
