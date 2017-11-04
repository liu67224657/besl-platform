/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.util.ArrayList;
import java.util.List;

import com.enjoyf.platform.util.FiveProps;

/**
 * This class is used to configure a jvm that uses ServerThread objects
 * and registers with the naming service. It is used in a call to
 * MainInit.init(). Note that there are several variants of the init()
 * method in MainInit. Most of them are for simpler initialization (rather
 * than constructing this object). However, for full control over
 * initialization, this object can be used.
 */
public class MainConfig {
    private FiveProps servProps;
    private List<ServiceConfig> serviceConfigList = new ArrayList<ServiceConfig>();

    private String processId = null;
    private boolean initServicesFlag = true;
    private boolean initLoggerFlag = true;

    /**
     *
     */
    public MainConfig() {
    }

    /**
     *
     */
    public MainConfig(FiveProps props, ServiceConfig serviceConfig) {
        servProps = props;
        serviceConfigList.add(serviceConfig);
    }

    /**
     * Ctor this object with 1 server object to manage.
     */
    public MainConfig(ServerWrap serverWrap) {
        serviceConfigList.add(new ServiceConfig(serverWrap));
    }

    /**
     * @param mainProps
     * @param serverWrap
     */
    public MainConfig(FiveProps mainProps, ServerWrap serverWrap) {
        this(mainProps, new ServiceConfig(serverWrap));
    }

    public void setInitLogger(boolean flag) {
        initLoggerFlag = flag;
    }

    public boolean shouldInitLogger() {
        return initLoggerFlag;
    }

    public FiveProps getServProps() {
        return servProps;
    }

    /**
     * Add a ServerWrap.
     */
    public void addServer(ServerWrap serverWrap) {
        if (serviceConfigList.size() == 0) {
            serviceConfigList.add(new ServiceConfig(serverWrap));
        } else {
            //--
            // TODO: This is clunky in that when this method gets invoked
            // the user is only registering one ServiceConfig object, and
            // that's the one we want to add the ServerWrap object to.
            // The fix is to remove this method and change client code to
            // use new init methods.
            //--
            ServiceConfig serviceConfig = serviceConfigList.get(0);
            serviceConfig.addServerWrap(serverWrap);
        }
    }

    /**
     * Specify whether or not you want to call ServiceInit.instance().init()
     * DEFAULT: true.
     *
     * @param flag Set to true if you want to initialize the
     *             services.
     */
    public void setInitServices(boolean flag) {
        initServicesFlag = flag;
    }

    public boolean shouldInitServices() {
        return initServicesFlag;
    }

    /**
     * Set the logicProcess id of the server. Used in alert messages.
     * If left unset, it will use one from any services
     * specified. If none set, it will just use ANONYMOUS.
     */
    public void setProcessId(String id) {
        processId = id;
    }

    public String getProcessId() {
        return processId;
    }

    public List<ServiceConfig> getServiceConfigList() {
        return serviceConfigList;
    }

    public void setServProps(FiveProps props) {
        servProps = props;
    }
}