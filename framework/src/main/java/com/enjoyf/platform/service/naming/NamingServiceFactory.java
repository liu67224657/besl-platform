/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.util.FiveProps;

/**
 * Singleton for creating objects dealing with the naming service.
 * Note that this factory deals with creating different kinds of
 * objects, not just NamingService objects.
 */
public class NamingServiceFactory {
    protected static NamingServiceFactory instance = new NamingServiceFactory();

    private FactoryImpl factoryImpl;

    protected NamingServiceFactory() {
        //--
        // In theory, when we switch away from the monitor, this line
        // of code will change to create a "FactoryImplNormal" object.
        //--
        factoryImpl = new FactoryImplNormal();
    }

    public static NamingServiceFactory instance() {
        return instance;
    }

    /**
     * Create a default naming service.
     *
     * @param name The hostname where the service is found.
     */
    public NamingService createDefault(String name) {
        return factoryImpl.create(name);
    }

    /**
     * Create a default naming service.
     *
     * @param name The hostname where the service is found.
     * @param port The port for the service.
     */
    public NamingService createDefault(String name, int port) {
        return factoryImpl.create(name, port);
    }

    /**
     * Use properties to decide what kind of naming service to create.
     */
    public NamingService createFromProps(FiveProps props) {
        return factoryImpl.createFromProps(props);
    }

    /**
     * Create a Registrant object. These objects are used to register
     * with the naming service. When the object gets returned, call
     * the NamingService.registerService() method.
     * Once through using the object, call NamingService.unregisterService().
     * NOTE: don't even try using this if you know
     * you are talking to the monitor, it's not supported.
     */
    public Registrant createRegistrant(ServiceInfo serviceInfo) {
        return factoryImpl.createRegistrant(serviceInfo);
    }

    /**
     * Create a Registrant object from a FiveProps file. This should
     * work for all manner of naming services since the params to
     * configure it are represented via properties.
     *
     * @param sectionName The name of a section under which to find
     *                    params related to the service creation. This comes from the
     *                    props file, eg: if sectionName == "juser", we will look for
     *                    service info under "juser.*" entries.
     * @param props       The properties.
     */
    public Registrant createRegistrant(String sectionName, FiveProps props) {
        return factoryImpl.createRegistrant(sectionName, props);
    }
}
